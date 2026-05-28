# Hospital Microservices Project - Master Plan

## 1. Mục Tiêu

Xây dựng hệ thống quản lý phòng khám/bệnh viện theo hướng Java Spring Boot microservices để học sâu:

- Java Spring Boot backend.
- Spring Cloud Gateway.
- Eureka Discovery.
- Config Server.
- PostgreSQL.
- Liquibase.
- Redis.
- Kafka.
- Distributed system patterns.
- AWS deployment.

ReactJS làm sau cùng vì frontend không phải trọng tâm học của project này.

Mục tiêu không phải làm một hệ thống HIS đầy đủ. Scope đúng là:

```text
Clinic/Hospital Appointment & Patient Flow System
```

Flow chính:

```text
Patient đặt lịch
-> Appointment Service giữ slot
-> Kafka event
-> Notification Service gửi thông báo
-> Receptionist check-in
-> Doctor khám và tạo medical record
-> Pharmacy xử lý prescription
-> Billing tạo invoice
-> Payment update
-> Reporting build dashboard
```

Chỉ dùng dữ liệu giả lập. Không dùng dữ liệu bệnh nhân thật.

---

## 2. Distributed System Là Gì Trong Project Này?

Distributed system ở đây nghĩa là:

```text
Nhiều service chạy độc lập, giao tiếp qua network/API/Kafka,
mỗi service có database riêng, và hệ thống phải xử lý lỗi từng phần.
```

Project này có tính distributed vì:

- Nhiều service chạy riêng process/container.
- Mỗi service sở hữu database/schema riêng.
- Service giao tiếp qua HTTP và Kafka.
- Một service có thể lỗi trong khi service khác vẫn chạy.
- Kafka consumer có thể consume trễ hoặc consume lại.
- Dữ liệu giữa service không nhất quán tức thì.
- Cần retry, DLQ, idempotency, lock, logs, correlation id.

Điểm quan trọng để nói chuyện phỏng vấn:

```text
POST /appointments
-> Idempotency-Key
-> Redis lock slot
-> PostgreSQL unique constraint
-> AppointmentBooked Kafka event
-> notification/reporting consume event
-> retry/DLQ
-> correlation id trace toàn flow
```

---

## 3. Technology Decisions

### Backend

- Java Spring Boot.
- Spring Web.
- Spring Security.
- Spring Data JPA.
- MapStruct.
- Bean Validation.
- Liquibase.
- Spring Cloud Gateway.
- Eureka Server/Client.
- Spring Cloud Config.
- Spring Kafka.
- Redis client.
- Resilience4j nếu có service-to-service sync call.
- Actuator.

### Database

Dùng toàn bộ PostgreSQL là được và nên làm ở giai đoạn này.

Khuyến nghị:

```text
Một PostgreSQL server
-> nhiều database hoặc nhiều schema
-> mỗi service sở hữu database/schema riêng
```

Ví dụ:

```text
hospital_auth
hospital_patient
hospital_appointment
hospital_medical_record
hospital_pharmacy
hospital_billing
hospital_notification
hospital_reporting
```

Nguyên tắc:

- Không query trực tiếp DB của service khác.
- Không dùng foreign key xuyên service.
- Tham chiếu chéo bằng id.
- Đồng bộ bằng API hoặc Kafka event.

### Migration

Chọn Liquibase-first.

Lý do:

- Phù hợp Spring Boot enterprise.
- Nhiều công ty Java dùng Liquibase.
- Quản lý changeSet rõ ràng.
- Dễ nói chuyện phỏng vấn vì sát thực tế công ty.

Flyway vẫn tốt nếu muốn SQL-first, nhưng project này ưu tiên Liquibase.

### Redis

Dùng cho:

- Cache.
- Idempotency key.
- Distributed lock cơ bản.
- Rate limit.
- Token blacklist nếu cần.

### Kafka

Dùng cho:

- Event-driven communication.
- Decouple service.
- Retry/DLQ.
- Consumer idempotency.
- Reporting read model.

### AWS

Dùng để học deployment production-like:

- ECR.
- EC2/ECS.
- RDS PostgreSQL.
- ElastiCache Redis/Valkey.
- S3.
- CloudFront.
- ALB.
- CloudWatch Logs.
- SSM Parameter Store hoặc Secrets Manager.
- AWS Budgets.

Kafka:

- Tiết kiệm: Kafka container trên EC2.
- Managed để học ngắn hạn: Amazon MSK.

Với budget 200 USD, không để MSK/Fargate/RDS/ElastiCache chạy 24/7 quá lâu.

---

## 4. Service List

### 1. config-service

Centralized config.

Quản lý:

- datasource config.
- kafka config.
- redis config.
- jwt config.
- service URL.
- logging level.
- profile `local`, `docker`, `aws`.

### 2. discovery-service

Eureka Server.

Nhiệm vụ:

- Các service register vào Eureka.
- Gateway route theo service name.
- Không hardcode host/port giữa các service.

### 3. api-gateway

Cửa vào duy nhất của backend.

Nhiệm vụ:

- Route request.
- Validate JWT.
- Global CORS.
- Correlation ID filter.
- Forward headers:

```text
X-User-Id
X-User-Role
X-Correlation-Id
```

- Rate limit API nhạy cảm bằng Redis.

### 4. auth-service

Làm tối thiểu vì auth bạn đã nắm chắc.

API:

- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `GET /auth/me`

DB:

- `users`
- `roles`
- `user_roles`
- `refresh_tokens`

Roles:

- `ADMIN`
- `PATIENT`
- `DOCTOR`
- `RECEPTIONIST`
- `BILLING_STAFF`
- `PHARMACIST`

### 5. patient-service

Quản lý bệnh nhân.

API:

- `POST /patients`
- `GET /patients/{id}`
- `PATCH /patients/{id}`
- `GET /patients/search?keyword=...`
- `GET /patients/me`
- `PATCH /patients/me`
- `GET /patients/{id}/summary`

Feature:

- Patient profile.
- Emergency contact.
- Insurance info giả lập.
- Search theo name/phone/email.

### 6. appointment-service

Service trung tâm của project.

API:

- department CRUD.
- doctor CRUD.
- doctor schedule.
- available slots.
- book appointment.
- cancel appointment.
- confirm appointment.
- check-in.
- start appointment.
- complete appointment.
- mark no-show.

Status:

```text
REQUESTED
CONFIRMED
CHECKED_IN
IN_PROGRESS
COMPLETED
CANCELLED
NO_SHOW
```

Business rules:

- Một doctor không thể có 2 appointment cùng slot.
- Patient không thể đặt 2 appointment cùng thời điểm.
- Chỉ `REQUESTED`/`CONFIRMED` được cancel.
- Receptionist/admin check-in.
- Doctor start/complete.
- Book appointment phải có idempotency key.

Redis:

- cache available slots.
- idempotency key.
- lock doctor slot.

Kafka events:

- `AppointmentBooked`
- `AppointmentConfirmed`
- `AppointmentCancelled`
- `PatientCheckedIn`
- `AppointmentStarted`
- `AppointmentCompleted`
- `AppointmentNoShow`

### 7. notification-service

Consumer service đầu tiên, dễ kiểm chứng Kafka.

API:

- `GET /notifications/me`
- `PATCH /notifications/{id}/read`

Feature:

- Consume appointment/medical/billing/pharmacy events.
- Tạo notification log.
- Giả lập gửi email/SMS bằng log.
- Consumer idempotency bằng `event_id`.
- Retry + DLQ.

### 8. medical-record-service

Quản lý quá trình khám.

API:

- `POST /encounters`
- `GET /encounters/{id}`
- `GET /patients/{patientId}/encounters`
- `POST /encounters/{id}/diagnoses`
- `POST /encounters/{id}/prescriptions`
- `PATCH /encounters/{id}/complete`

Feature:

- Encounter.
- Chief complaint.
- Clinical notes.
- Diagnosis.
- Treatment plan.
- Prescription.
- Complete consultation.

Kafka events:

- `EncounterCreated`
- `PrescriptionCreated`
- `ConsultationCompleted`

### 9. pharmacy-service

Quản lý thuốc và cấp thuốc theo prescription.

API:

- `GET /medicines`
- `POST /medicines`
- `PATCH /medicines/{id}`
- `GET /prescriptions/pending`
- `GET /prescriptions/{id}`
- `PATCH /prescriptions/{id}/dispense`
- `GET /medicine-stock?medicineId=`
- `PATCH /medicine-stock/{id}/adjust`

Feature:

- Medicine catalog.
- Medicine stock.
- Consume `PrescriptionCreated`.
- Tạo dispensing task.
- Pharmacist dispense medicine.
- Atomic update stock.
- Stock movement history.

Kafka events:

- `PrescriptionReceived`
- `PrescriptionDispensed`
- `MedicineStockLow`

### 10. billing-service

Quản lý hóa đơn và payment giả lập.

API:

- `GET /invoices/{id}`
- `GET /patients/{patientId}/invoices`
- `POST /invoices`
- `POST /invoices/{id}/items`
- `PATCH /invoices/{id}/mark-paid`
- `PATCH /invoices/{id}/mark-failed`
- `PATCH /invoices/{id}/cancel`

Feature:

- Consume `ConsultationCompleted`.
- Tạo invoice.
- Invoice item.
- Payment success/failure.

Kafka events:

- `InvoiceCreated`
- `PaymentSucceeded`
- `PaymentFailed`

### 11. reporting-service

Read model/dashboard service.

API:

- `GET /reports/appointments/daily?date=`
- `GET /reports/appointments/monthly?month=`
- `GET /reports/billing/revenue/monthly?year=`
- `GET /reports/doctors/utilization?from=&to=`
- `GET /reports/pharmacy/medicine-usage?from=&to=`

Feature:

- Consume events từ các service.
- Build bảng report riêng.
- Không query DB của service khác.
- Chấp nhận eventual consistency.

---

## 5. Backend-First Build Order

Không làm React trước. React làm cuối.

Thứ tự:

```text
1. Repo structure + local infra
2. Config service
3. Discovery service
4. API Gateway
5. Auth service tối thiểu
6. Patient service
7. Appointment service
8. Redis cho appointment
9. Kafka appointment events
10. Notification service
11. Medical record service
12. Pharmacy service
13. Billing service
14. Reporting service
15. Hardening
16. AWS deployment
17. React integration cuối cùng
```

Lý do không bắt đầu auth sâu:

- Bạn đã làm nhiều project auth.
- Auth chỉ cần đủ để gateway/security flow chạy.
- Trọng tâm học là distributed flow: appointment, Redis, Kafka, DB consistency.

---

## 6. Suggested Repo Structure

Mono-repo để học trong 5 tuần:

```text
hospital-platform/
  api-gateway/
  discovery-service/
  config-service/
  auth-service/
  patient-service/
  appointment-service/
  medical-record-service/
  pharmacy-service/
  billing-service/
  notification-service/
  reporting-service/
  docker-compose.yml
  docs/
```

Mỗi service nên có package:

```text
controller
service
service.impl
repository
entity
dto.request
dto.response
mapper
exception
config
event
```

---

## 7. 5 Week Plan

### Week 1 - Platform Skeleton + Core Services

Must finish:

- Mono-repo.
- Docker Compose local:
  - PostgreSQL
  - Redis
  - Kafka
  - Eureka
  - Config Server
- `config-service`.
- `discovery-service`.
- `api-gateway`.
- `auth-service` minimal.
- `patient-service`.
- `appointment-service` basic.
- Liquibase migration cho service DB.
- Gateway route được request.
- JWT qua gateway.
- Patient CRUD.
- Department/doctor CRUD.
- Available slots API.
- Book appointment basic bằng PostgreSQL.

### Week 2 - Appointment + Redis + Kafka + Notification

Must finish:

- Appointment transaction.
- Unique index chống double booking.
- Redis cache available slots.
- Redis idempotency cho `POST /appointments`.
- Redis lock khi book slot.
- Kafka producer trong appointment-service.
- Event envelope chuẩn.
- `AppointmentBooked`.
- `AppointmentCancelled`.
- notification-service consume event.
- Notification log.
- Retry consumer cơ bản.
- DLQ topic.
- Không tạo trùng appointment khi retry request.

### Week 3 - Medical Record + Pharmacy

Must finish:

- Check-in appointment.
- Doctor start appointment.
- Create encounter.
- Diagnosis.
- Prescription.
- `PrescriptionCreated` event.
- pharmacy-service consume prescription.
- Medicine catalog.
- Medicine stock.
- Dispensing task.
- Pharmacist dispense medicine.
- Atomic stock update.
- Stock không âm.
- `PrescriptionDispensed` event.

### Week 4 - Billing + Reporting + Distributed Hardening

Must finish:

- Complete consultation.
- `ConsultationCompleted` event.
- billing-service consume event.
- Auto-create invoice.
- Add invoice item.
- Payment success/failure.
- `InvoiceCreated`.
- `PaymentSucceeded`.
- `PaymentFailed`.
- reporting-service consume ít nhất 4 loại event.
- Read model:
  - appointment daily/status.
  - monthly revenue.
  - doctor utilization.
  - medicine usage.
- Consumer idempotency cho notification/reporting/billing/pharmacy.
- Correlation ID trong request và Kafka event.
- Logs trace được flow.

### Week 5 - AWS + Final Polish + React Integration

Must finish:

- Docker image push ECR.
- RDS PostgreSQL.
- ElastiCache Redis/Valkey hoặc Redis trên EC2 nếu tiết kiệm.
- Kafka on EC2 hoặc MSK thử ngắn hạn.
- Backend deploy ECS/EC2.
- ALB.
- CloudWatch logs.
- SSM/Secrets.
- AWS Budgets.
- Health check.
- README architecture.
- README deployment.
- React integration:
  - login.
  - patient booking.
  - doctor dashboard.
  - pharmacy dispensing.
  - billing invoice.
  - reporting dashboard.
- Demo end-to-end.

---

## 8. Core Flows

### Flow 1 - Patient Book Appointment

```text
1. Patient login.
2. Gateway validate JWT.
3. Patient xem available slots.
4. appointment-service cache/read slots.
5. Patient book appointment với Idempotency-Key.
6. Gateway rate limit.
7. appointment-service kiểm tra idempotency key.
8. appointment-service lấy Redis lock doctor+slot.
9. appointment-service insert appointment.
10. PostgreSQL unique index bảo vệ double booking.
11. appointment-service publish AppointmentBooked.
12. notification-service consume.
13. reporting-service consume.
```

### Flow 2 - Receptionist Check-In

```text
1. Receptionist xem appointment hôm nay.
2. Patient tới khám.
3. Receptionist check-in.
4. appointment-service đổi CONFIRMED -> CHECKED_IN.
5. appointment-service publish PatientCheckedIn.
6. Doctor dashboard thấy patient trong queue.
```

### Flow 3 - Doctor Consultation

```text
1. Doctor start appointment.
2. appointment-service đổi CHECKED_IN -> IN_PROGRESS.
3. medical-record-service tạo encounter.
4. Doctor nhập diagnosis/note/prescription.
5. medical-record-service publish PrescriptionCreated.
6. pharmacy-service nhận prescription.
7. Doctor complete consultation.
8. medical-record-service publish ConsultationCompleted.
```

### Flow 4 - Pharmacy

```text
1. pharmacy-service consume PrescriptionCreated.
2. Tạo dispensing task.
3. Pharmacist xem pending prescription.
4. Pharmacist dispense medicine.
5. pharmacy-service atomic update stock.
6. pharmacy-service ghi stock movement.
7. pharmacy-service publish PrescriptionDispensed.
```

### Flow 5 - Billing

```text
1. billing-service consume ConsultationCompleted.
2. billing-service tạo invoice.
3. billing staff thêm item nếu cần.
4. billing staff mark paid/failed.
5. billing-service publish payment event.
6. notification-service báo patient.
7. reporting-service update revenue read model.
```

---

## 9. Kafka Design

Suggested topics:

```text
appointment.events
medical-record.events
pharmacy.events
billing.events
notification.dlq
reporting.dlq
```

Event envelope:

```json
{
  "eventId": "uuid",
  "eventType": "AppointmentBooked",
  "eventVersion": 1,
  "occurredAt": "2026-05-23T10:15:30Z",
  "correlationId": "uuid",
  "source": "appointment-service",
  "payload": {}
}
```

Rules:

- Message key là aggregate id.
- Consumer idempotent bằng `eventId`.
- Event version có từ đầu.
- Không publish entity thô.
- Có retry và DLQ.

---

## 10. Redis Design

Cache:

```text
department:list
doctor:{doctorId}:profile
doctor:{doctorId}:slots:{date}
medicine:{medicineId}:detail
medicine:list:active
```

Idempotency:

```text
idempotency:appointment:{userId}:{key}
```

Lock:

```text
lock:doctor:{doctorId}:slot:{slotStart}
```

Rate limit:

- login.
- book appointment.
- search patient.
- stock adjustment.

Note:

- Redis lock chỉ là lớp hỗ trợ.
- PostgreSQL unique constraint vẫn là lớp bảo vệ cuối cùng.

---

## 11. PostgreSQL Patterns Cần Master

Double booking prevention:

```sql
CREATE UNIQUE INDEX uq_appointments_doctor_slot_active
ON appointments (doctor_id, appointment_date, start_time)
WHERE status NOT IN ('CANCELLED', 'NO_SHOW');
```

Atomic stock update:

```sql
UPDATE medicine_stock
SET available_quantity = available_quantity - :quantity,
    version = version + 1,
    updated_at = now()
WHERE medicine_id = :medicineId
  AND available_quantity >= :quantity;
```

Consumer idempotency:

```sql
CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Dashboard/report:

- `GROUP BY`
- `HAVING`
- date aggregation.
- indexes.
- `EXPLAIN ANALYZE`.

---

## 12. AWS Plan

Target:

```text
React -> S3 + CloudFront
Backend -> ALB -> ECS/EC2
Images -> ECR
DB -> RDS PostgreSQL
Cache -> ElastiCache Redis/Valkey
Logs -> CloudWatch
Secrets -> SSM Parameter Store or Secrets Manager
Kafka -> EC2 container first, MSK optional short-term
```

Cost rules:

- Tạo AWS Budgets ngày đầu.
- Không để MSK chạy lâu.
- Không để nhiều service Fargate 24/7 nếu không cần.
- Khi không test, stop/delete tài nguyên tốn tiền.

---

## 13. React Scope Cuối Cùng

Làm sau backend.

Screens tối thiểu:

- Login.
- Patient booking.
- My appointments.
- Doctor today appointments.
- Encounter form.
- Pharmacy pending prescriptions.
- Dispense medicine.
- Invoice list/detail.
- Reporting dashboard.

Vì bạn đã mạnh React, phần này là integration/polish, không phải trọng tâm học.

---

## 14. Definition of Done

Project hoàn thành khi:

- Patient đặt được lịch từ UI/API.
- Appointment không duplicate khi retry.
- Slot không double-book nhờ Redis lock + PostgreSQL constraint.
- Kafka event chạy qua ít nhất 4 service.
- Notification consumer idempotent.
- Doctor tạo được medical record.
- Pharmacy dispense được prescription và trừ stock đúng.
- Billing tạo invoice từ event.
- Payment update được trạng thái.
- Reporting build read model từ Kafka event.
- Mỗi service có Liquibase migration.
- Gateway validate JWT và forward user headers.
- Logs có correlation id.
- Có retry/DLQ.
- Deploy được lên AWS.
- Có README architecture và deployment.

---

## 15. Interview Talking Points

Khi phỏng vấn, project này dùng để nói về:

- Microservices boundary.
- API Gateway.
- Eureka discovery.
- Config server.
- Database per service.
- PostgreSQL transaction/constraint/index.
- Redis cache/idempotency/lock.
- Kafka event-driven architecture.
- Retry/DLQ.
- Idempotent consumer.
- Eventual consistency.
- Reporting read model.
- AWS deployment.
- Observability bằng correlation id/logs.

Một câu trả lời tốt:

```text
Em có làm hệ thống quản lý phòng khám theo microservices.
Flow chính là book appointment. Em dùng Redis idempotency key để tránh client retry tạo trùng,
Redis lock để giảm race khi đặt slot, PostgreSQL unique index là lớp bảo vệ cuối cùng.
Sau khi appointment được tạo, service publish Kafka event.
Notification, reporting và các service khác consume event theo kiểu idempotent bằng event_id,
có retry/DLQ, và em dùng correlation id để trace request từ gateway tới Kafka consumer.
```

