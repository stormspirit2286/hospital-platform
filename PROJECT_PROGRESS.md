# Hospital Platform - Current Progress

## Snapshot

Date: 2026-05-25

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
- Initial routes exist:
  - `/api/auth/** -> auth-service`
  - `/api/patients/** -> patient-service`
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
            - Path=/api/patients/**

        - id: appointment-service
          uri: lb://appointment-service
          predicates:
            - Path=/api/appointments/**,/api/departments/**,/api/doctors/**
```

Next:

- Add correlation id filter.
- Add temporary permissive security config while bootstrapping.
- Later add JWT validation and user header forwarding.

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

Next:

- Create package structure.
- Create patient entity/request/response/mapper.
- Create first Liquibase changelog.
- Create CRUD APIs.

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
4. patient-service     -> http://localhost:8082
5. api-gateway         -> http://localhost:8080
```

Why gateway last:

- Gateway routes to services through Eureka.
- It is easier to verify service registration before testing gateway routes.

---

## Next Immediate Checklist

### Step 1 - Verify boot sequence

- Start `config-service`.
- Start `discovery-service`.
- Open `http://localhost:8761`.
- Start `auth-service`.
- Start `patient-service`.
- Confirm both services appear in Eureka dashboard.
- Start `api-gateway`.

### Step 2 - Add temporary health endpoints if needed

If a service has no controller yet, use Actuator:

```text
/actuator/health
```

Add this later if needed:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
```

### Step 3 - patient-service first real code

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
POST /api/patients
GET  /api/patients/{id}
```

### Step 4 - Liquibase first migration

Create:

```text
patient-service/src/main/resources/db/changelog/db.changelog-master.yaml
patient-service/src/main/resources/db/changelog/changes/001-create-patients.yaml
```

First table:

```text
patients
```

### Step 5 - Database setup

Create local PostgreSQL databases:

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
- No Docker Compose yet.
- No actual business code yet.
- No Liquibase changelog files yet.
- No database connection config yet.
- `auth-service` startup currently fails until datasource/Liquibase config is added.
- `patient-service` and `appointment-service` will also fail at runtime until datasource/Liquibase config is added.
- Kafka broker config is not added yet.
- Redis connection config is not added yet.
- No gateway security filter yet.
- No correlation id filter yet.
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
