# Hospital Platform - Current Progress

## Snapshot

Date: 2026-05-28

Project root:

```text
hospital-platform
```

Current modules created in IntelliJ:

```text
api-gateway
appointment-service
auth-service
config-service
discovery-service
patient-service
```

This is the correct first milestone for the backend-first plan.

Build verification:

```text
config-service      compile OK
discovery-service   compile OK
api-gateway         compile OK
appointment-service compile OK
auth-service        compile OK
patient-service     compile OK
```

Note:

- Maven Wrapper printed one cache rename warning during parallel compile, but the command still exited successfully.
- `auth-service` compile OK, but does not start yet because datasource config is missing for JPA/Liquibase.

Local infra:

```text
docker-compose.yml created
PostgreSQL 16 configured
Redis 7 configured
Kafka 3.9 KRaft configured
docker compose config OK
```

---

## What Has Been Created

### config-service

Purpose:

- Centralized configuration server.
- Runs on port `8888`.

Current status:

- Module exists.
- `spring-cloud-config-server` dependency exists.
- `@EnableConfigServer` added.
- Native config profile enabled.

Config:

```yaml
server:
  port: 8888

spring:
  application:
    name: config-service
  profiles:
    active: native
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/config
```

Next:

- Create `src/main/resources/config`.
- Move shared service configs into config-service later.

### discovery-service

Purpose:

- Eureka Server.
- Runs on port `8761`.

Current status:

- Module exists.
- `spring-cloud-starter-netflix-eureka-server` dependency exists.
- `@EnableEurekaServer` exists.
- Config Server import exists.

Expected URL:

```text
http://localhost:8761
```

### api-gateway

Purpose:

- Entry point for client requests.
- Runs on port `8080`.
- Routes to backend services through Eureka.

Current status:

- Module exists.
- Gateway dependency exists.
- Eureka Client dependency exists.
- Redis reactive dependency exists.
- Security/OAuth2 Resource Server dependencies exist.
- Config Server import added.
- Correlation ID global filter implemented.
- JWT validation implemented with Spring Security WebFlux Resource Server.
- Gateway validates HS256 access tokens offline using the same issuer/secret contract as auth-service.
- Public endpoints:
  - `POST /api/auth/login`
  - `POST /api/auth/refresh`
  - `GET /actuator/health`
  - `GET /actuator/info`
- Protected endpoints:
  - `/api/auth/logout`
  - `/api/auth/me`
  - `/api/v1/patients/**`
  - `/api/appointments/**`
  - `/api/departments/**`
  - `/api/doctors/**`
- Unknown routes are denied by default.
- Authenticated requests forward trusted internal identity headers:
  - `X-User-Id`
  - `X-User-Email`
  - `X-User-Roles`
- Client-provided identity headers are stripped first to prevent spoofing.
- Initial routes exist:
  - `/api/auth/** -> auth-service`
  - `/api/v1/patients/** -> patient-service`
  - `/api/appointments/**`, `/api/departments/**`, `/api/doctors/** -> appointment-service`

Current routes:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/api/auth/**

        - id: patient-service
          uri: lb://patient-service
          predicates:
            - Path=/api/v1/patients/**

        - id: appointment-service
          uri: lb://appointment-service
          predicates:
            - Path=/api/appointments/**,/api/departments/**,/api/doctors/**
```

Next:

- Start `config-service`, `discovery-service`, `auth-service`, then `api-gateway`.
- Test login through gateway and call protected endpoints through gateway only.
- Move `auth.jwt.secret` out of local YAML into Config Server or environment/secrets before production.
- Add role-based authorization rules after core service ownership rules are clear.

### auth-service

Purpose:

- Minimal authentication service.
- Runs on port `8081`.

Current status:

- Module exists.
- JPA dependency exists.
- Liquibase dependency exists.
- PostgreSQL dependency exists.
- Spring Security dependencies exist.
- Eureka Client dependency exists.
- Config Server import added.
- Eureka URL fixed to `http://localhost:8761/eureka`.
- Lombok dependency added.
- MapStruct dependency and annotation processor added.
- `lombok-mapstruct-binding` annotation processor added.

Next:

- Add datasource config for `hospital_auth`.
- Add Liquibase changelog.
- Create package structure.
- Create minimal `users`, `roles`, `user_roles`, `refresh_tokens` Liquibase changelog.
- Create login/refresh/logout/me APIs later.

Latest status:

```text
auth-service starts successfully
Liquibase SQL migration ran successfully
roles seed data inserted
auth-service registered with Eureka successfully
```

Auth API status:

```text
POST /api/auth/login    implemented
POST /api/auth/refresh  implemented
POST /api/auth/logout   implemented
GET  /api/auth/me       implemented
```

Security behavior:

```text
/api/auth/login, /api/auth/refresh, /api/auth/logout are public.
/api/auth/me requires Bearer access token.
Access token is JWT HS256.
Refresh token is random opaque token.
Only refresh token hash is stored in PostgreSQL.
Refresh rotates token: old refresh token is revoked and a new one is issued.
Logout revokes the provided refresh token.
```

Local test user:

```text
email:    admin@hospital.local
password: Admin@12345
role:     ADMIN
```

Created tables:

```text
users
roles
user_roles
refresh_tokens
databasechangelog
databasechangeloglock
```

Note:

- The manual test process was stopped after startup verification, so Maven reported exit code 143. That is expected because the running service was killed intentionally.
- Auth migrations were converted from YAML changesets to Liquibase formatted SQL.
- The master changelog now uses `includeAll`, so future migrations only need a new `.sql` file under `db/changelog/changes`.

Current auth Liquibase files:

```text
db/changelog/db.changelog-master.yaml
db/changelog/changes/001-create-auth-tables.sql
db/changelog/changes/002-seed-roles.sql
db/changelog/changes/003-seed-local-admin.sql
```

Migration rule:

```text
For a new table/column/index/seed change:
1. Create a new file, for example 003-add-user-status-index.sql
2. Start service
3. Liquibase auto-detects it through includeAll
```

SQL file format:

```sql
--liquibase formatted sql

--changeset duy:003-some-change
ALTER TABLE users ADD COLUMN example VARCHAR(100);
```

Dev note:

- Because YAML migrations already ran once before this conversion, an existing local DB volume may need a dev reset before running the SQL migrations from scratch.

### patient-service

Purpose:

- Patient profile service.
- Runs on port `8082`.

Current status:

- Module exists.
- JPA dependency exists.
- Liquibase dependency exists.
- PostgreSQL dependency exists.
- Eureka Client dependency exists.
- Config Server import added.
- Port `8082` added.
- Eureka URL added.
- Lombok exists.
- MapStruct dependency and annotation processor added.
- `lombok-mapstruct-binding` annotation processor added.
- `spring-boot-starter-validation` added (Jakarta Bean Validation).
- `spring-boot-starter-security` added for downstream header-based authorization.
- Package structure created: `controller`, `service`, `service/impl`, `repository`, `entity`, `entity/enums`, `dto/request`, `dto/response`, `mapper`, `exception`, `config`, `security`.

Domain decisions (chốt ngày 2026-05-28):

```text
- Patient <-> PatientInsurance:        1-1 (BHYT only, no life/voluntary insurance)
- Patient <-> EmergencyContact:        1-N (max 2 contacts, enforced at service layer)
- PatientInsurance.card_number:        UNIQUE, fixed 15 chars (2 letters + 13 digits)
- Insurance/Contact creation:          OPTIONAL on patient creation
- patient.status:                      server-managed, not exposed in request DTO
```

JPA / DB design notes:

```text
- PatientInsurance is owning side (holds patient_id FK).
- Patient is inverse side (mappedBy = "patient").
- All associations use FetchType.LAZY by default.
- JOIN FETCH or @EntityGraph to be used when listing patients with insurance.
- @Enumerated(EnumType.STRING) used for all enums (never ORDINAL).
```

Entities created:

```text
entity/BaseEntity.java              (createdAt/updatedAt, auditing)
entity/Patient.java                 (1-1 insurance, 1-N emergencyContacts)
entity/PatientInsurance.java        (BHYT VN fields)
entity/EmergencyContact.java
entity/enums/InsuranceStatus.java   (ACTIVE/EXPIRED/SUSPENDED/INVALID)
entity/enums/BenefitRate.java       (RATE_80/RATE_95/RATE_100 + fromPercent)
```

DTO request created:

```text
dto/request/PatientRequest.java            (full Bean Validation, optional userId, @Valid nested)
dto/request/EmergencyContactRequest.java   (VN phone pattern, size limits)
dto/request/InsuranceRequest.java          (BHYT card pattern ^[A-Z]{2}[0-9]{13}$)
dto/request/PatientUpdateRequest.java      (PATCH first slice)
```

Common response wrapper created:

```text
dto/response/common/ApiResponse.java   (generic <T>, success/error factories,
                                        also accepts ResponseCode)
dto/response/common/ApiError.java      (per-field error: field/code/message/rejectedValue)
dto/response/common/ResponseCode.java  (enum: HttpStatus + code + default message,
                                        covers SUCCESS/CREATED/VALIDATION_FAILED/
                                        PATIENT_NOT_FOUND/DUPLICATE_PHONE etc.)
```

Liquibase migrations (auto-loaded via includeAll):

```text
001-create-patient-tables.sql           (initial patients/insurances/contacts tables)
002-make-insurance-one-to-one.sql       (UNIQUE constraint on patient_insurances.patient_id)
003-refactor-insurance-fields.sql       (drop provider_name/policy_number,
                                         add card_number/participant_type/
                                         initial_facility_code/benefit_rate/continuous_from,
                                         NOT NULL + UNIQUE on card_number + index)
```

Validation rules applied in PatientRequest:

```text
firstName, lastName, phone, dateOfBirth   -> required
email                                     -> optional but must be valid email format
gender, address, city                     -> optional
phone                                     -> VN pattern ^(0|\+84)[0-9]{9,10}$
dateOfBirth                               -> @Past
emergencyContacts                         -> @Valid + @Size(max = 2)
insurance                                 -> @Valid
All string fields                         -> @Size(max = X) matching DB column length
```

Implemented application layer (2026-06-01):

```text
dto/response/PatientResponse + InsuranceResponse + EmergencyContactResponse + PatientSummaryResponse + PageResponse
exception/AppException + GlobalExceptionHandler
mapper/PatientMapper
service/PatientService + service/impl/PatientServiceImpl
controller/PatientController
security/HeaderAuthenticationFilter + AuthenticatedUser + SecurityConfig
```

Current patient-service API route:

```text
Base path: /api/v1/patients

POST   /api/v1/patients
GET    /api/v1/patients?search=&page=&size=
GET    /api/v1/patients/summaries?search=&page=&size=
GET    /api/v1/patients/me
PATCH  /api/v1/patients/me
GET    /api/v1/patients/{patientId}
PATCH  /api/v1/patients/{patientId}
DELETE /api/v1/patients/{patientId}
```

Downstream security status:

```text
api-gateway validates JWT.
api-gateway sanitizes client-provided X-User-* headers.
api-gateway forwards X-User-Id, X-User-Email, X-User-Roles.
patient-service HeaderAuthenticationFilter converts those headers into Spring Security Authentication.
GET/PATCH /me uses @AuthenticationPrincipal AuthenticatedUser and SELECT WHERE user_id = X-User-Id.
```

Role rules:

```text
ADMIN/RECEPTIONIST: create/update/delete patients.
ADMIN/RECEPTIONIST/DOCTOR: read/search/summaries.
Any authenticated role: GET/PATCH /me, but data is scoped by user_id.
```

Patient-service remaining debt before strict completion:

```text
1. Full gateway smoke test with real login tokens.
2. Optional endpoint GET /api/v1/patients/{id}/summary if strict docs require it.
3. Extend PATCH /me to update emergency contact and insurance.
4. Convert gender/status from String to enums if needed.
5. Add focused controller/service tests for authorization and search.
```

Senior notes recorded during morning session:

```text
- ResponseCode is server-side template, ApiResponse is wire format.
  Intentional duplication of code/message/status is a feature, not a bug
  (allows i18n override, decouples API contract from internal enum).
- Bean Validation cross-field rules (validFrom < validTo) handled at service layer
  for now; can be promoted to custom @ValidDateRange later.
- relationship/participantType kept as String for now; convert to enum once
  business value set stabilizes.
- N+1 query risk is accepted in learning phase; mitigation via JOIN FETCH /
  @EntityGraph when needed. SQL logging enabled in application.yaml.
- Single PatientRepository only (Aggregate Root pattern). No separate
  InsuranceRepository / EmergencyContactRepository until a real query-by-child
  use case shows up.
```

### appointment-service

Purpose:

- Appointment/domain core service.
- Runs on port `8083`.
- This is the first service where Redis and Kafka will be used seriously.

Current status:

- Module exists.
- JPA dependency exists.
- Liquibase dependency exists.
- PostgreSQL dependency exists.
- Validation dependency exists.
- Eureka Client dependency exists.
- Config Server import added.
- Kafka dependency exists.
- Redis dependency exists.
- Lombok dependency exists.
- MapStruct dependency and annotation processor added.
- `lombok-mapstruct-binding` annotation processor added.
- API Gateway route added.

Current config:

```yaml
server:
  port: 8083

spring:
  application:
    name: appointment-service
  config:
    import: "optional:configserver:http://localhost:8888"

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

Important dependency decision:

- `spring-boot-starter-data-redis` is kept.
- `spring-boot-starter-data-redis-reactive` was removed from appointment-service.

Reason:

- appointment-service is currently a blocking WebMVC service.
- For cache, idempotency key, slot lock, and simple Redis operations, regular Redis support is enough.
- Reactive Redis can be learned later if the service is intentionally built reactive.

Next:

- Add datasource config for `hospital_appointment`.
- Add Liquibase changelog.
- Create package structure.
- Build department/doctor/schedule/appointment basics first.
- Add Redis idempotency and slot lock after basic booking works.
- Add Kafka `AppointmentBooked` after booking transaction is stable.

---

## Fixes Applied Today

### Docker Compose Infra

Added root-level infra compose:

```text
docker-compose.yml
```

Services:

```text
postgres -> localhost:5432
redis    -> localhost:6379
kafka    -> localhost:9092
```

PostgreSQL credentials:

```text
username: hospital
password: hospital123
default database: hospital_auth
```

Added init SQL:

```text
docker/postgres/init/01-create-databases.sql
```

Databases created on first PostgreSQL volume initialization:

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

Run infra:

```bash
docker compose up -d
```

Check containers:

```bash
docker ps
```

Important note:

- PostgreSQL init scripts run only when the `postgres_data` volume is created for the first time.
- If the volume already exists and a database is missing, create it manually or recreate the volume intentionally.

### Config Server

Added:

```java
@EnableConfigServer
```

Reason:

- Without this annotation, the service is only a normal Spring Boot app, not a Config Server.

### Config Profiles

Changed `config-service` config to:

```yaml
spring:
  profiles:
    active: native
```

Reason:

- Native mode lets Config Server load config files from the local classpath.

### Eureka URL Typo

Fixed in `auth-service`:

```yaml
defaultZone: http://localhost:8761/eureka
```

Previous issue:

```yaml
http:localhost:8761/eureka
```

### patient-service Runtime Config

Added:

```yaml
server:
  port: 8082

spring:
  config:
    import: "optional:configserver:http://localhost:8888"

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

### api-gateway Config Import

Added:

```yaml
spring:
  config:
    import: "optional:configserver:http://localhost:8888"
```

Reason:

- Gateway is a Config Client, so it should know where Config Server is.

---

## MapStruct Setup

MapStruct has been added to:

```text
auth-service
patient-service
appointment-service
```

Dependency:

```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${mapstruct.version}</version>
</dependency>
```

Annotation processor:

```xml
<path>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</path>
<path>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>${mapstruct.version}</version>
</path>
<path>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok-mapstruct-binding</artifactId>
    <version>${lombok-mapstruct-binding.version}</version>
</path>
```

Why this matters:

- MapStruct generates mapper implementations at compile time.
- Lombok generates getters/setters/builders at compile time.
- `lombok-mapstruct-binding` helps MapStruct see Lombok-generated accessors reliably.

Recommended mapper style:

```java
@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toEntity(CreatePatientRequest request);
    PatientResponse toResponse(Patient patient);
}
```

For now, do not create mapper classes until the entity and DTO classes exist.

---

## Expected Start Order

Run services in this order:

```text
1. config-service      -> http://localhost:8888
2. discovery-service   -> http://localhost:8761
3. auth-service        -> http://localhost:8081
4. api-gateway         -> http://localhost:8080
5. patient-service     -> http://localhost:8082 later, after datasource/Liquibase is added
```

Why gateway last:

- Gateway routes to services through Eureka.
- It is easier to verify service registration before testing gateway routes.

---

## Next Immediate Checklist

### Step 1 - Verify current boot sequence

- Start `config-service`.
- Start `discovery-service`.
- Open `http://localhost:8761`.
- Start `auth-service`.
- Start `api-gateway`.
- Confirm `AUTH-SERVICE` and `API-GATEWAY` appear in Eureka dashboard.

### Step 2 - Verify api-gateway

Check gateway health:

```text
GET http://localhost:8080/actuator/health
```

Check registered gateway routes:

```text
GET http://localhost:8080/actuator/gateway/routes
```

Expected explicit routes:

```text
/api/auth/**                                -> lb://auth-service
/api/v1/patients/**                         -> lb://patient-service
/api/appointments/**,/api/departments/**,
/api/doctors/**                             -> lb://appointment-service
```

Current route test:

```text
GET http://localhost:8080/api/auth/not-found
```

Expected result for now:

```text
401 Unauthorized from auth-service
X-Correlation-Id response header exists
```

The `401` is expected because `auth-service` still has default Spring Security and no public auth controller yet.

### Step 3 - auth-service first real API

Minimal auth API has been implemented:

```text
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/logout
GET  /api/auth/me
```

Next auth-service hardening items:

```text
1. Move auth.jwt.secret out of application.yaml into config-service/env/secret manager.
2. Decide whether production should use RS256 instead of HS256.
3. Add gateway JWT validation using the same token contract.
4. Add gateway internal headers: X-User-Id, X-User-Role.
5. Add user management APIs later if needed.
```

### Step 4 - patient-service first real code

Create packages:

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
```

First feature:

```text
POST /api/v1/patients
GET  /api/v1/patients/{id}
```

### Step 5 - patient-service Liquibase first migration

Create:

```text
patient-service/src/main/resources/db/changelog/db.changelog-master.yaml
patient-service/src/main/resources/db/changelog/changes/001-create-patients.yaml
```

First table:

```text
patients
```

### Step 6 - Database setup

Local PostgreSQL databases are created by Docker init script on first volume initialization:

```sql
CREATE DATABASE hospital_auth;
CREATE DATABASE hospital_patient;
CREATE DATABASE hospital_appointment;
CREATE DATABASE hospital_medical_record;
CREATE DATABASE hospital_pharmacy;
CREATE DATABASE hospital_billing;
CREATE DATABASE hospital_notification;
CREATE DATABASE hospital_reporting;
```

---

## Known Gaps

- No root parent `pom.xml` yet. Current setup is independent Maven modules.
- Docker Compose exists for PostgreSQL, Redis, and Kafka.
- No actual business code yet.
- `auth-service` has Liquibase SQL migrations and starts successfully.
- `auth-service` has no login/refresh/logout/me API yet.
- `auth-service` still uses default Spring Security behavior, so gateway-routed auth requests currently return `401`.
- `patient-service` and `appointment-service` will also fail at runtime until datasource/Liquibase config is added.
- Kafka broker config is not added yet.
- Redis connection config is not added yet.
- `api-gateway` has temporary permissive WebFlux security.
- `api-gateway` has correlation id request/response forwarding and basic request logging.
- `api-gateway` uses Spring Cloud Gateway 5 config prefix: `spring.cloud.gateway.server.webflux`.
- No common response/error format yet.
- No MapStruct mapper classes yet because entity/DTO classes do not exist.

---

## Rule For Adding A New Service Later

When adding a new service:

```text
1. Create Spring Boot module.
2. Set spring.application.name.
3. Set server.port.
4. Add Eureka Client.
5. Add config import.
6. Create database/schema.
7. Add Liquibase changelog.
8. Add route in api-gateway.
9. Start service.
10. Confirm it appears in Eureka.
```
