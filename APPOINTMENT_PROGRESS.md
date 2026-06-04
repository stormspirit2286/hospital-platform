# Appointment Service Progress

## Purpose

`appointment-service` la core service cua project hospital-platform.

Service nay quan ly:

```text
departments
doctors
doctor_schedules
appointments
appointment_status_history
```

Day la service dung de hoc sau:

```text
Spring Boot business service
PostgreSQL transaction + unique constraints
Redis cache / lock / idempotency
Kafka appointment events
Distributed consistency
```

Nguyen tac:

- Khong query DB cua service khac.
- Khong foreign key sang `patient-service`.
- `appointments.patient_id` chi la UUID tham chieu patient do `patient-service` so huu.
- Redis/Kafka chi them sau khi booking basic bang PostgreSQL chay dung.

---

## Build Phases

### Phase A - PostgreSQL Basic Appointment

Goal:

```text
Build domain, migration, CRUD basics, available slots, appointment booking,
status transitions, and DB-level duplicate protection.
```

Scope:

- Department CRUD first slice.
- Doctor CRUD first slice.
- Doctor schedule creation.
- Available slots calculation.
- Appointment booking.
- Appointment status transitions.
- Appointment status history.

No Redis yet.

No Kafka yet.

### Phase B - Redis

Goal:

```text
Add cache, distributed lock, and idempotency to booking flow.
```

Use cases:

```text
doctor:{doctorId}:slots:{date}
lock:doctor:{doctorId}:slot:{slotStart}
idempotency:appointment:{userId}:{idempotencyKey}
```

### Phase C - Kafka

Goal:

```text
Publish appointment lifecycle events and let downstream services consume them.
```

Events:

```text
AppointmentBooked
AppointmentConfirmed
AppointmentCancelled
PatientCheckedIn
AppointmentStarted
AppointmentCompleted
AppointmentNoShow
```

Preferred pattern:

```text
Outbox table in same transaction
-> poller publishes to Kafka
-> consumers handle idempotency by eventId
```

---

## Current Status

Date: 2026-06-01

### Done

- `appointment-service` module exists.
- Runs on port `8083`.
- Eureka client config exists.
- Config Server import exists.
- Dependencies already present:
  - Spring WebMVC
  - Spring Data JPA
  - Validation
  - Liquibase
  - PostgreSQL
  - Redis
  - Kafka
  - Eureka Client
  - Config Client
  - Lombok
  - MapStruct

### Runtime Config Done

File:

```text
appointment-service/src/main/resources/application.yaml
```

Current config:

```text
server.port=8083
spring.application.name=appointment-service
spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_appointment
spring.datasource.username=hospital
spring.datasource.password=hospital123
spring.liquibase.change-log=classpath:/db/changelog/db.changelog-master.yaml
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

### Liquibase Done

Files:

```text
appointment-service/src/main/resources/db/changelog/db.changelog-master.yaml
appointment-service/src/main/resources/db/changelog/changes/001-create-appointment-core-tables.sql
```

Created tables:

```text
departments
doctors
doctor_schedules
appointments
appointment_status_history
databasechangelog
databasechangeloglock
```

Important constraints and indexes:

```text
doctors.department_id -> departments.department_id
doctor_schedules.doctor_id -> doctors.doctor_id
appointments.doctor_id -> doctors.doctor_id
appointments.department_id -> departments.department_id
appointment_status_history.appointment_id -> appointments.appointment_id

UNIQUE doctor_schedules(doctor_id, work_date, start_time, end_time)
UNIQUE active appointment by doctor/date/start_time
UNIQUE active appointment by patient/date/start_time
UNIQUE appointment idempotency by booked_by_user_id + idempotency_key when idempotency_key is not null
```

No FK to `patient-service`.

### Entity Layer Done

Files:

```text
entity/BaseEntity.java
entity/Department.java
entity/Doctor.java
entity/DoctorSchedule.java
entity/Appointment.java
entity/AppointmentStatusHistory.java
entity/enums/AppointmentStatus.java
entity/enums/DepartmentStatus.java
entity/enums/DoctorStatus.java
entity/enums/DoctorScheduleStatus.java
config/JpaConfig.java
```

Status enums:

```text
AppointmentStatus:
REQUESTED
CONFIRMED
CHECKED_IN
IN_PROGRESS
COMPLETED
CANCELLED
NO_SHOW

DepartmentStatus:
ACTIVE
INACTIVE

DoctorStatus:
ACTIVE
INACTIVE

DoctorScheduleStatus:
ACTIVE
CANCELLED
```

JPA notes:

- All enums use `EnumType.STRING`.
- Associations inside appointment-service use lazy relationships.
- `Appointment.patientId` is UUID only, not `@ManyToOne`.
- `Appointment.version` uses `@Version`.
- `BaseEntity` uses JPA auditing for `createdAt` and `updatedAt`.

### Verification Done

Command:

```text
cd appointment-service
./mvnw test
```

Result:

```text
BUILD SUCCESS
Liquibase ran 9 changesets
Hibernate ddl-auto=validate passed
appointment-service registered with Eureka during context test
```

---

## Next Steps

### Step 1 - Repositories

Create:

```text
repository/DepartmentRepository.java
repository/DoctorRepository.java
repository/DoctorScheduleRepository.java
repository/AppointmentRepository.java
repository/AppointmentStatusHistoryRepository.java
```

Needed queries:

```text
Department:
- existsByName
- findByStatus

Doctor:
- findByDepartmentId
- findByUserId
- existsByLicenseNumber

DoctorSchedule:
- find active schedules by doctor/date

Appointment:
- find by patientId/date
- find by doctor/date/status
- find active appointment by doctor/date/startTime
- find active appointment by patient/date/startTime

AppointmentStatusHistory:
- find by appointmentId ordered by changedAt
```

### Step 2 - DTOs And Mappers

Create request DTOs:

```text
CreateDepartmentRequest
UpdateDepartmentRequest
CreateDoctorRequest
UpdateDoctorRequest
CreateDoctorScheduleRequest
CreateAppointmentRequest
CancelAppointmentRequest
```

Create response DTOs:

```text
DepartmentResponse
DoctorResponse
DoctorScheduleResponse
AvailableSlotResponse
AppointmentResponse
AppointmentStatusHistoryResponse
PageResponse
ApiResponse / ApiError / ResponseCode
```

Create mapper:

```text
mapper/DepartmentMapper.java
mapper/DoctorMapper.java
mapper/DoctorScheduleMapper.java
mapper/AppointmentMapper.java
```

### Step 3 - Header-Based Security

Mirror patient-service pattern:

```text
security/AuthenticatedUser.java
security/HeaderAuthenticationFilter.java
config/SecurityConfig.java
```

Headers from gateway:

```text
X-User-Id
X-User-Email
X-User-Roles
```

Initial role rules:

```text
ADMIN:
- manage departments
- manage doctors
- manage schedules

RECEPTIONIST:
- create appointment for patient
- confirm appointment
- check-in
- mark no-show

PATIENT:
- book own appointment
- view own appointments
- cancel own appointment if allowed

DOCTOR:
- view own daily appointments
- start appointment
- complete appointment
```

### Step 4 - Department And Doctor APIs

Base route target:

```text
/api/v1/departments
/api/v1/doctors
```

Implement first:

```text
POST /api/v1/departments
GET  /api/v1/departments
POST /api/v1/doctors
GET  /api/v1/doctors
GET  /api/v1/doctors/{doctorId}
```

### Step 5 - Doctor Schedule And Available Slots

Implement:

```text
POST /api/v1/doctors/{doctorId}/schedules
GET  /api/v1/doctors/{doctorId}/available-slots?date=yyyy-MM-dd
```

Rules:

```text
work_date required
start_time < end_time
slot_minutes > 0
schedule status default ACTIVE
available slots are generated from active schedules minus active appointments
```

### Step 6 - Appointment Booking Basic

Implement:

```text
POST /api/v1/appointments
GET  /api/v1/appointments/{appointmentId}
GET  /api/v1/appointments/me
GET  /api/v1/appointments?doctorId=&date=&status=
```

Booking rules:

```text
Patient cannot double-book same date/start_time.
Doctor cannot be double-booked same date/start_time.
DB unique indexes are final protection.
Initial status = REQUESTED.
Insert appointment_status_history row on creation.
```

### Step 7 - Status Transitions

Implement:

```text
PATCH /api/v1/appointments/{appointmentId}/confirm
PATCH /api/v1/appointments/{appointmentId}/cancel
PATCH /api/v1/appointments/{appointmentId}/check-in
PATCH /api/v1/appointments/{appointmentId}/start
PATCH /api/v1/appointments/{appointmentId}/complete
PATCH /api/v1/appointments/{appointmentId}/no-show
```

Transition rules:

```text
REQUESTED -> CONFIRMED
REQUESTED -> CANCELLED
CONFIRMED -> CANCELLED
CONFIRMED -> CHECKED_IN
CONFIRMED -> NO_SHOW
CHECKED_IN -> IN_PROGRESS
IN_PROGRESS -> COMPLETED
```

Every transition writes `appointment_status_history`.

---

## Definition Of Done For Phase A

Phase A is done when:

- [x] Liquibase creates core appointment tables.
- [x] JPA entities validate against DB schema.
- [ ] Repositories exist.
- [ ] DTOs and mappers exist.
- [ ] Header-based security exists.
- [ ] Department APIs work.
- [ ] Doctor APIs work.
- [ ] Schedule APIs work.
- [ ] Available slots API works.
- [ ] Appointment booking works.
- [ ] Appointment status transitions work.
- [ ] Status history is written.
- [ ] Gateway routes use `/api/v1/appointments/**`, `/api/v1/departments/**`, `/api/v1/doctors/**`.
- [ ] Smoke test via gateway passes.

Only after this checklist passes should Redis Phase B start.
