# Next Steps Guide — Patient Service & Appointment Service

Tài liệu này là guide thực thi sau khi config-service / discovery-service / api-gateway / auth-service đã verified (xem [API_GATEWAY_NOTES.md](API_GATEWAY_NOTES.md) section 10).

Tham chiếu:

- Build order: [HOSPITAL_PROJECT_MASTER_PLAN.md](HOSPITAL_PROJECT_MASTER_PLAN.md) section 5.
- Feature scope: [HOSPITAL_FEATURES_4_WEEKS.md](HOSPITAL_FEATURES_4_WEEKS.md).
- DB schema: [HOSPITAL_DB_DESIGN.md](HOSPITAL_DB_DESIGN.md).

---

## 1. Service Nào Trước?

Theo master plan, thứ tự còn lại là:

```text
6. patient-service          <-- LAM TIEP THEO (Week 1)
7. appointment-service basic (Week 1 cuoi)
8. Redis cho appointment    (Week 2)
9. Kafka appointment events (Week 2)
10. notification-service    (Week 2)
11. medical-record-service  (Week 3)
12. pharmacy-service        (Week 3)
13. billing-service         (Week 4)
14. reporting-service       (Week 4)
```

Lý do làm `patient-service` trước `appointment-service`:

```text
1. appointment-service tham chieu patient_id => can patient ton tai truoc.
2. patient-service la service CRUD don gian nhat => warm-up cho pattern Spring Boot business service.
3. Co dip kiem tra X-User-Id identity forward tu gateway xuong downstream service.
4. Khong dinh den Redis/Kafka => focus vao Spring Web + JPA + Liquibase + Security context downstream.
```

---

## 2. patient-service — Definition Of Done (Week 1)

Coi nhu xong khi:

- [x] Service start, register len Eureka, route `/api/v1/patients/**` qua gateway hoat dong.
- [x] Liquibase tao day du 3 bang: `patients`, `emergency_contacts`, `patient_insurances` (schema xem [HOSPITAL_DB_DESIGN.md](HOSPITAL_DB_DESIGN.md)).
- [x] Entity layer cho patient-service da tao xong:
  - `BaseEntity`
  - `Patient`
  - `EmergencyContact`
  - `PatientInsurance`
- [x] API first slice co the goi qua gateway:
  - [x] `POST /api/v1/patients`
  - [x] `GET  /api/v1/patients/{id}`
  - [x] `PATCH /api/v1/patients/{id}`
  - [x] `GET  /api/v1/patients?search=...&page=&size=`
  - [x] `GET  /api/v1/patients/summaries?search=...&page=&size=`
  - [x] `GET  /api/v1/patients/me`
  - [x] `PATCH /api/v1/patients/me`
- [x] Authorization theo role + ownership rule:
  - PATIENT: doc/sua record `user_id = X-User-Id` cua chinh minh qua `/me`.
  - DOCTOR / RECEPTIONIST / ADMIN: doc record bat ky, search, view summaries.
  - Chi ADMIN / RECEPTIONIST duoc tao/sua/xoa patient ho.
- [ ] Validation chuan (email, phone not blank, dob not future da co; gender/status enum con de String trong first slice).
- [x] GlobalExceptionHandler tra ApiResponse error format rieng cua patient-service.
- [x] Ham search co paging (`Pageable`), khong tra ve toan bo bang.
- [ ] Smoke test full qua gateway: dang nhap PATIENT -> goi `/api/v1/patients/me` -> 404 (chua co record) -> ADMIN tao patient -> PATIENT goi `/me` -> 200.

Update 2026-06-01:

- Patient-service da du foundation slice de chuyen sang `appointment-service`.
- Route thuc te la `/api/v1/patients/**`, khong phai `/api/patients/**`.
- Search duoc gop vao query param `search`, khong tao route rieng `/search`.
- Chua strict-complete cac enhancement: `GET /api/v1/patients/{id}/summary`, update emergency contact/insurance qua `/me`, enum hoa gender/status, full gateway smoke test.

---

## 3. patient-service — Step By Step

### Step 1. Chuan bi infra

```text
1. Trong docker/postgres/init/01-create-databases.sql: verify co database `hospital_patient`.
   Neu chua co thi them CREATE DATABASE hospital_patient va re-create container.
2. Trong application.yaml cua patient-service:
   - server.port: 8082
   - spring.application.name: patient-service
   - spring.config.import: optional:configserver:http://localhost:8888
   - spring.datasource.url: jdbc:postgresql://localhost:5432/hospital_patient
   - eureka.client.service-url.defaultZone: http://localhost:8761/eureka
   - spring.liquibase.change-log: classpath:/db/changelog/db.changelog-master.yaml
   - spring.jpa.hibernate.ddl-auto: validate
   - spring.jpa.open-in-view: false
   - management.endpoints.web.exposure.include: health,info
3. Trong pom.xml: dependencies chinh cua patient-service:
   - spring-boot-starter-web
   - spring-boot-starter-data-jpa
   - spring-boot-starter-validation
   - spring-boot-starter-security      (chi de read X-User-Id, KHONG bat OAuth2 Resource Server)
   - spring-boot-starter-actuator
   - liquibase-core
   - postgresql (runtime)
   - spring-cloud-starter-config
   - spring-cloud-starter-netflix-eureka-client
   - lombok + mapstruct + lombok-mapstruct-binding
```

Luu y quan trong: `patient-service` KHONG verify JWT, vi gateway da verify roi. Service chi tin tuong 3 header: `X-User-Id`, `X-User-Email`, `X-User-Roles`.

### Step 2. Liquibase migration

```text
src/main/resources/db/changelog/
    db.changelog-master.yaml           # includeAll changes/
    changes/
        001-create-patient-tables.sql  # patients, emergency_contacts, patient_insurances
        002-create-patient-indexes.sql # 4 index theo HOSPITAL_DB_DESIGN.md
```

Copy paste DDL tu [HOSPITAL_DB_DESIGN.md](HOSPITAL_DB_DESIGN.md) section "patient-service Database". Theo Liquibase formatted SQL pattern giong auth-service:

```sql
--liquibase formatted sql
--changeset duy:001-create-patients
CREATE TABLE IF NOT EXISTS patients ( ... );
```

### Step 3. Package structure

```text
com.duy.hospital.patientservice
    PatientServiceApplication.java
    config/
        JpaConfig.java             # DONE - JPA auditing
        SecurityConfig.java        # DONE - headers-based authn, KHONG dung JWT
    security/
        HeaderAuthenticationFilter.java # DONE
        AuthenticatedUser.java          # DONE
    controller/
        PatientController.java     # DONE
    service/
        PatientService.java        # DONE
        impl/PatientServiceImpl.java # DONE
    repository/
        PatientRepository.java          # DONE - Aggregate Root repository
    entity/
        BaseEntity.java            # DONE
        Patient.java               # DONE
        EmergencyContact.java      # DONE
        PatientInsurance.java      # DONE
        PatientStatus.java         # TODO later, status kept as String for first slice
        Gender.java                # TODO later, gender kept as String for first slice
    dto/
        request/
            PatientRequest.java
            PatientUpdateRequest.java
        response/
            PatientResponse.java
            PatientSummaryResponse.java
            PageResponse.java
    mapper/
        PatientMapper.java          # MapStruct
    exception/                       # COPY format tu auth-service
        ApiException.java
        ErrorCode.java
        ErrorResponse.java
        GlobalExceptionHandler.java
```

Update 2026-05-27:

- Patient entity work is complete for the first persistence slice.
- `JpaConfig` enables auditing for `BaseEntity.createdAt` and `BaseEntity.updatedAt`.
- First SQL Liquibase changelog exists at `patient-service/src/main/resources/db/changelog/changes/001-create-patient-tables.sql`.
- Next coding slice should start from repositories + DTOs + mapper, then service/controller.

### Step 4. Security: doc identity tu gateway

Vi gateway da forward 3 header `X-User-Id` / `X-User-Email` / `X-User-Roles`, patient-service chi can convert headers -> Spring Security `Authentication`. Cach tiep can:

```text
1. Tat csrf, stateless, .authorizeHttpRequests permit /actuator/**.
2. Tat .oauth2ResourceServer().
3. Tao HeaderAuthenticationFilter extends OncePerRequestFilter:
   - Doc X-User-Id (required, neu thieu -> anonymous).
   - Doc X-User-Email.
   - Doc X-User-Roles (comma-separated).
   - Set UsernamePasswordAuthenticationToken voi authorities = ROLE_{role}.
4. Dat filter truoc UsernamePasswordAuthenticationFilter.
5. .authorizeHttpRequests:
   - POST /api/v1/patients => hasAnyRole("ADMIN","RECEPTIONIST")
   - PATCH /api/v1/patients/{id} => hasAnyRole("ADMIN","RECEPTIONIST")
   - DELETE /api/v1/patients/{id} => hasAnyRole("ADMIN","RECEPTIONIST")
   - GET /api/v1/patients/{id} => hasAnyRole("ADMIN","RECEPTIONIST","DOCTOR")
   - GET /api/v1/patients/summaries?search=... => hasAnyRole("ADMIN","RECEPTIONIST","DOCTOR")
   - GET/PATCH /api/v1/patients/me => authenticated()
   - anyRequest() => denyAll()
```

Bao mat: trong production gateway phai la cua duy nhat. Service nay khong nen expose port public. O local thi van listen 0.0.0.0:8082 nhung khong route truc tiep — gateway moi co route forward.

### Step 5. Business rules quan trong

```text
- /api/v1/patients/me:
    - SELECT WHERE user_id = X-User-Id
    - Khong tim thay -> 404 PATIENT_NOT_FOUND.
- POST /api/v1/patients (ADMIN/RECEPTIONIST tao ho):
    - user_id optional. Neu truyen, validate khong trung patient khac (UNIQUE).
    - status mac dinh ACTIVE.
- PATCH /api/v1/patients/{id} (ADMIN/RECEPTIONIST):
    - Khong cho doi user_id sau khi da set.
    - Khong cho doi date_of_birth thanh ngay tuong lai.
- PATCH /api/v1/patients/me (PATIENT):
    - First slice chi cho doi: email, phone, address, city.
    - Later co the mo rong emergency contact, insurance.
    - Khong cho doi: first_name, last_name, date_of_birth, gender, user_id, status.
- Search:
    - Query param `search` optional. Match LIKE '%search%' tren full_name OR phone OR email.
    - LUON co Pageable, mac dinh size=20, max size=100.
    - Sort mac dinh: updated_at DESC.
- Summary (dung cho doctor):
    - First slice tra ve: id, full name, dob, gender, phone, email, city, status.
    - Later co the them latest insurance status neu doctor workflow can.
    - KHONG tra ve dia chi day du, KHONG tra ve insurance card_number/policy_number day du.
```

### Step 6. Cau hinh gateway route (da co)

`api-gateway/src/main/resources/application.yaml` da co route `/api/v1/patients/** -> lb://patient-service`. Khong can them gi. Sau khi start patient-service, kiem tra `http://localhost:8761` thay `PATIENT-SERVICE` register.

### Step 7. Smoke test qua gateway

```text
# 1. Admin tao patient cho user_id cua patient1 (33333333-...).
POST /api/auth/login {admin@hospital.local}
=> luu accessToken admin

POST http://localhost:8080/api/v1/patients
Authorization: Bearer <admin_at>
{ "userId":"33333333-3333-3333-3333-333333333333",
  "firstName":"Patient","lastName":"One","phone":"0922222222",
  "dateOfBirth":"1990-01-01","gender":"MALE" }
=> 201

# 2. Patient1 doc profile cua minh.
POST /api/auth/login {patient1@hospital.local}
=> luu accessToken patient1

GET http://localhost:8080/api/v1/patients/me
Authorization: Bearer <patient1_at>
=> 200 + record vua tao

# 3. Patient1 thu doc record nguoi khac => 403.
GET http://localhost:8080/api/v1/patients/<random-uuid>
Authorization: Bearer <patient1_at>
=> 403 FORBIDDEN

# 4. Doctor1 tim kiem.
POST /api/auth/login {doctor1@hospital.local}
GET http://localhost:8080/api/v1/patients/summaries?search=Patient&page=0&size=10
Authorization: Bearer <doctor1_at>
=> 200 + danh sach

# 5. Patient1 thu PATCH ten => phai bi tu choi.
PATCH http://localhost:8080/api/v1/patients/me
{ "firstName":"Hack" }
=> 400 hoac field bi ignore (tuy implementation).
```

Sau khi pass toan bo => append section "11. patient-service smoke test" vao [API_GATEWAY_NOTES.md](API_GATEWAY_NOTES.md).

### Step 8. Lessons & Pitfalls

```text
1. HeaderAuthenticationFilter phai check ca request KHONG di qua gateway:
   - Neu gateway down hoac developer goi truc tiep :8082, X-User-Id co the bi spoofing.
   - O production: phai chan port o firewall, hoac thuc thi mTLS giua gateway <-> service.
   - O local: chap nhan rui ro, nhung viet comment ro trong SecurityConfig.

2. JPA open-in-view: false BAT BUOC trong microservices.
   - Tranh lazy load chay query ngoai transaction.
   - Bat buoc fetch tuong minh trong @Transactional service method.

3. MapStruct @Mapper(componentModel = "spring") de inject duoc.

4. PageResponse: tu wrap thay vi expose Spring Page nguyen ban.
   - Loi neu de Page<T> truc tiep: response co field 'pageable.pageNumber'... rat noisy.
   - Wrap: { content, page, size, totalElements, totalPages }.

5. Khong dung @ManyToOne cross-service (vi du Patient -> User).
   - Chi luu user_id UUID.
   - User detail lay tu JWT header neu can hien thi.

6. Khi PATCH partial update: dung BeanUtils.copyProperties co ignoreNullValues
   HOAC viet manually if (req.field() != null) entity.setField(...).
   - Khuyen viet manually de control.

7. Liquibase tip: neu cap nhat schema sau khi da chay, KHONG sua file cu, tao file moi 003-xxx.sql.
```

---

## 4. appointment-service — Preview (Week 1 cuoi -> Week 2)

Day la service trong tam hoc cua project. KHONG bat dau Redis/Kafka tu dau. Theo phasing:

### Phase A — basic (Week 1 cuoi)

```text
1. Liquibase: departments, doctors, doctor_schedules, appointments (KHONG dung index unique), appointment_status_history.
2. CRUD department/doctor cho ADMIN.
3. POST /doctors/{doctorId}/schedules de tao slot template.
4. GET /doctors/{doctorId}/available-slots?date=... TINH SLOTS TRONG memory tu doctor_schedules - appointments da co.
5. POST /appointments: insert thuan tuy bang PostgreSQL.
6. GET /appointments/me, /appointments/{id}.
7. PATCH confirm/cancel/check-in/start/complete/no-show.
8. Bo sung appointment_status_history moi khi doi status.
9. Validation business rules co ban (status transition).
```

Pass Phase A khi: 2 user goi `POST /appointments` cung doctor+slot lien tiep => 1 thanh cong, 1 fail.

### Phase B — Redis (Week 2 dau)

```text
1. Bo sung UNIQUE INDEX uq_appointments_doctor_slot_active (xem HOSPITAL_DB_DESIGN.md).
2. Cache `doctor:{doctorId}:slots:{date}` TTL 5 phut, invalidate khi co appointment moi.
3. Distributed lock `lock:doctor:{doctorId}:slot:{slotStart}` TTL 10s khi POST /appointments.
4. Idempotency-Key header: store `idempotency:appointment:{userId}:{key}` TTL 24h voi response hash.
5. Test concurrent: 50 request cung 1 user + idempotency key => chi 1 record duoc tao.
```

### Phase C — Kafka (Week 2 cuoi)

```text
1. Topic appointment.events partition theo appointment_id.
2. Outbox pattern: trong cung transaction, INSERT vao bang `outbox_events`.
3. Outbox poller publish len Kafka.
4. Su kien: AppointmentBooked, AppointmentCancelled, ... (xem HOSPITAL_FEATURES_4_WEEKS.md).
5. Event envelope chuan (eventId, eventType, version, occurredAt, correlationId, source, payload).
6. notification-service consume + processed_events table cho idempotency.
```

Day la luc Kafka + Redis duoc hoc sau. KHONG nho gop vao Phase A.

---

## 5. Checklist Tong Ket Hom Nay

Ban nen lam theo thu tu nay:

```text
[ ] Tao module patient-service hoan thien Step 1 -> Step 8 o muc 3.
[ ] Smoke test 5 case qua gateway.
[ ] Append ket qua vao API_GATEWAY_NOTES.md.
[ ] Sang Phase A cua appointment-service (chua dung Redis/Kafka).
[ ] Khi appointment Phase A xong, tao file NEXT_STEPS_REDIS_KAFKA.md de bat dau Phase B.
```

Khi cam thay bi cuon vao "lam nhieu thu cung luc", hay quay ve master plan section 5. Day la kim chi nam.
