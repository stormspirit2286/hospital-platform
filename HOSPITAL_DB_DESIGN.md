# Hospital Microservices Database Design

## Có Dùng Toàn Bộ PostgreSQL Được Không?

Có. Với project học Java Spring Boot microservices, dùng toàn bộ PostgreSQL là hợp lý.

Khuyến nghị:

```text
Một PostgreSQL server
-> nhiều database hoặc nhiều schema
-> mỗi service sở hữu database/schema riêng
```

Ví dụ local/dev:

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

Hoặc dùng một database `hospital_platform` với nhiều schema:

```text
auth
patient
appointment
medical_record
pharmacy
billing
notification
reporting
```

Trong microservices, nguyên tắc quan trọng là:

- Mỗi service sở hữu dữ liệu của mình.
- Service khác không query trực tiếp bảng của service này.
- Không dùng foreign key xuyên database/schema giữa các service.
- Tham chiếu chéo bằng id, ví dụ `patient_id`, `appointment_id`, `doctor_id`.
- Đồng bộ trạng thái bằng API hoặc Kafka event.

Vì sao không nên dùng nhiều loại database ngay:

- Bạn đang muốn học sâu Spring Boot, Kafka, Redis, PostgreSQL, AWS.
- Thêm MongoDB/DynamoDB quá sớm sẽ làm loãng trọng tâm.
- PostgreSQL đủ mạnh cho transactional data, report, index, JSONB nếu cần.

Redis không thay PostgreSQL. Redis dùng cho cache/idempotency/lock/rate limit.

Kafka không thay PostgreSQL. Kafka dùng để truyền event giữa service.

---

## Database Ownership

| Service | Database/Schema | Sở hữu dữ liệu |
|---|---|---|
| auth-service | auth | users, roles, refresh tokens |
| patient-service | patient | patient profile, emergency contact, insurance |
| appointment-service | appointment | departments, doctors, schedules, appointments |
| medical-record-service | medical_record | encounters, diagnoses, prescriptions |
| pharmacy-service | pharmacy | medicines, medicine stock, prescription dispensing |
| billing-service | billing | invoices, invoice items, payments |
| notification-service | notification | notification log, processed events |
| reporting-service | reporting | read models, dashboard aggregates, processed events |

---

## Shared Design Rules

Tất cả bảng nên có:

```sql
created_at TIMESTAMP NOT NULL DEFAULT now()
updated_at TIMESTAMP NOT NULL DEFAULT now()
```

Các bảng business quan trọng nên có:

```sql
status VARCHAR(30) NOT NULL
version BIGINT NOT NULL DEFAULT 0
```

Quy ước id:

- Dùng `UUID` cho entity public/business chính.
- Có thể dùng `BIGSERIAL` nếu muốn đơn giản.
- Với microservices, `UUID` dễ hơn khi truyền event.

Khuyến nghị dùng UUID:

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

Hoặc trong app Java tạo UUID bằng `UUID.randomUUID()`.

---

## auth-service Database

### users

```sql
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(30),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested status:

```text
ACTIVE
INACTIVE
LOCKED
```

### roles

```sql
CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);
```

Role codes:

```text
ADMIN
PATIENT
DOCTOR
RECEPTIONIST
BILLING_STAFF
PHARMACIST
```

### user_roles

```sql
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, role_id)
);
```

Không cần foreign key xuyên service. Trong cùng auth database thì có thể FK tới `users` và `roles`.

### refresh_tokens

```sql
CREATE TABLE refresh_tokens (
    token_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Indexes:

```sql
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);
```

---

## patient-service Database

### patients

```sql
CREATE TABLE patients (
    patient_id UUID PRIMARY KEY,
    user_id UUID UNIQUE,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(30) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested status:

```text
ACTIVE
INACTIVE
DECEASED
```

Indexes:

```sql
CREATE INDEX idx_patients_user_id ON patients(user_id);
CREATE INDEX idx_patients_phone ON patients(phone);
CREATE INDEX idx_patients_email ON patients(email);
CREATE INDEX idx_patients_name ON patients(last_name, first_name);
```

### emergency_contacts

```sql
CREATE TABLE emergency_contacts (
    contact_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    relationship VARCHAR(80),
    phone VARCHAR(30) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

### patient_insurances

```sql
CREATE TABLE patient_insurances (
    insurance_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    provider_name VARCHAR(150) NOT NULL,
    policy_number VARCHAR(100) NOT NULL,
    valid_from DATE,
    valid_to DATE,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

---

## appointment-service Database

### departments

```sql
CREATE TABLE departments (
    department_id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

### doctors

```sql
CREATE TABLE doctors (
    doctor_id UUID PRIMARY KEY,
    user_id UUID UNIQUE,
    department_id UUID NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(30),
    specialization VARCHAR(150),
    license_number VARCHAR(100) UNIQUE,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Indexes:

```sql
CREATE INDEX idx_doctors_department_id ON doctors(department_id);
CREATE INDEX idx_doctors_user_id ON doctors(user_id);
```

### doctor_schedules

```sql
CREATE TABLE doctor_schedules (
    schedule_id UUID PRIMARY KEY,
    doctor_id UUID NOT NULL,
    work_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_minutes INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (doctor_id, work_date, start_time, end_time)
);
```

Suggested status:

```text
ACTIVE
CANCELLED
```

### appointments

```sql
CREATE TABLE appointments (
    appointment_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    department_id UUID NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    reason TEXT,
    status VARCHAR(30) NOT NULL,
    booked_by_user_id UUID,
    idempotency_key VARCHAR(120),
    checked_in_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Important constraints:

```sql
CREATE UNIQUE INDEX uq_appointments_doctor_slot_active
ON appointments (doctor_id, appointment_date, start_time)
WHERE status NOT IN ('CANCELLED', 'NO_SHOW');

CREATE UNIQUE INDEX uq_appointments_patient_slot_active
ON appointments (patient_id, appointment_date, start_time)
WHERE status NOT IN ('CANCELLED', 'NO_SHOW');

CREATE UNIQUE INDEX uq_appointments_idempotency
ON appointments (booked_by_user_id, idempotency_key)
WHERE idempotency_key IS NOT NULL;
```

Query indexes:

```sql
CREATE INDEX idx_appointments_patient_date ON appointments(patient_id, appointment_date DESC);
CREATE INDEX idx_appointments_doctor_date_status ON appointments(doctor_id, appointment_date, status);
CREATE INDEX idx_appointments_status_date ON appointments(status, appointment_date);
```

### appointment_status_history

```sql
CREATE TABLE appointment_status_history (
    history_id UUID PRIMARY KEY,
    appointment_id UUID NOT NULL,
    old_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    changed_by_user_id UUID,
    reason TEXT,
    changed_at TIMESTAMP NOT NULL DEFAULT now()
);
```

---

## medical-record-service Database

### encounters

```sql
CREATE TABLE encounters (
    encounter_id UUID PRIMARY KEY,
    appointment_id UUID NOT NULL UNIQUE,
    patient_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    chief_complaint TEXT,
    clinical_notes TEXT,
    treatment_plan TEXT,
    status VARCHAR(30) NOT NULL,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested status:

```text
IN_PROGRESS
COMPLETED
CANCELLED
```

Indexes:

```sql
CREATE INDEX idx_encounters_patient_id ON encounters(patient_id);
CREATE INDEX idx_encounters_doctor_id ON encounters(doctor_id);
CREATE INDEX idx_encounters_created_at ON encounters(created_at DESC);
```

### diagnoses

```sql
CREATE TABLE diagnoses (
    diagnosis_id UUID PRIMARY KEY,
    encounter_id UUID NOT NULL,
    code VARCHAR(50),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    diagnosis_type VARCHAR(30),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

### prescriptions

```sql
CREATE TABLE prescriptions (
    prescription_id UUID PRIMARY KEY,
    encounter_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested status:

```text
CREATED
DISPENSED
CANCELLED
```

### prescription_items

```sql
CREATE TABLE prescription_items (
    prescription_item_id UUID PRIMARY KEY,
    prescription_id UUID NOT NULL,
    medicine_name VARCHAR(150) NOT NULL,
    dosage VARCHAR(100),
    frequency VARCHAR(100),
    duration_days INT,
    instructions TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
```

---

## pharmacy-service Database

### medicines

```sql
CREATE TABLE medicines (
    medicine_id UUID PRIMARY KEY,
    code VARCHAR(80) NOT NULL UNIQUE,
    name VARCHAR(180) NOT NULL,
    generic_name VARCHAR(180),
    unit VARCHAR(50) NOT NULL,
    manufacturer VARCHAR(150),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested status:

```text
ACTIVE
INACTIVE
DISCONTINUED
```

Indexes:

```sql
CREATE INDEX idx_medicines_name ON medicines(name);
CREATE INDEX idx_medicines_status ON medicines(status);
```

### medicine_stock

```sql
CREATE TABLE medicine_stock (
    stock_id UUID PRIMARY KEY,
    medicine_id UUID NOT NULL,
    available_quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (medicine_id)
);
```

Important constraints:

```sql
ALTER TABLE medicine_stock
ADD CONSTRAINT chk_medicine_stock_non_negative
CHECK (available_quantity >= 0 AND reserved_quantity >= 0);
```

### dispensing_tasks

```sql
CREATE TABLE dispensing_tasks (
    dispensing_task_id UUID PRIMARY KEY,
    prescription_id UUID NOT NULL UNIQUE,
    patient_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT now(),
    dispensed_at TIMESTAMP,
    dispensed_by_user_id UUID,
    rejected_reason TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested status:

```text
PENDING
DISPENSED
PARTIALLY_DISPENSED
REJECTED
CANCELLED
```

### dispensing_items

```sql
CREATE TABLE dispensing_items (
    dispensing_item_id UUID PRIMARY KEY,
    dispensing_task_id UUID NOT NULL,
    medicine_id UUID NOT NULL,
    medicine_name_snapshot VARCHAR(180) NOT NULL,
    requested_quantity INT NOT NULL,
    dispensed_quantity INT NOT NULL DEFAULT 0,
    dosage VARCHAR(100),
    instructions TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Indexes:

```sql
CREATE INDEX idx_dispensing_tasks_status ON dispensing_tasks(status);
CREATE INDEX idx_dispensing_tasks_patient ON dispensing_tasks(patient_id);
CREATE INDEX idx_dispensing_items_task ON dispensing_items(dispensing_task_id);
```

### stock_movements

```sql
CREATE TABLE stock_movements (
    stock_movement_id UUID PRIMARY KEY,
    medicine_id UUID NOT NULL,
    movement_type VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    reference_type VARCHAR(80),
    reference_id UUID,
    reason TEXT,
    created_by_user_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested movement types:

```text
IMPORT
ADJUSTMENT
DISPENSE
RETURN
```

Atomic dispense pattern:

```sql
UPDATE medicine_stock
SET available_quantity = available_quantity - :quantity,
    version = version + 1,
    updated_at = now()
WHERE medicine_id = :medicineId
  AND available_quantity >= :quantity;
```

Nếu affected rows = 0, stock không đủ hoặc medicine không tồn tại.

---

## billing-service Database

### invoices

```sql
CREATE TABLE invoices (
    invoice_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    appointment_id UUID,
    encounter_id UUID,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL,
    subtotal_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    discount_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    tax_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    total_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    issued_at TIMESTAMP,
    paid_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested status:

```text
DRAFT
ISSUED
PAID
FAILED
CANCELLED
```

Indexes:

```sql
CREATE INDEX idx_invoices_patient_id ON invoices(patient_id);
CREATE INDEX idx_invoices_status_created_at ON invoices(status, created_at DESC);
CREATE INDEX idx_invoices_encounter_id ON invoices(encounter_id);
```

### invoice_items

```sql
CREATE TABLE invoice_items (
    invoice_item_id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested item types:

```text
CONSULTATION
MEDICINE
LAB
SERVICE
OTHER
```

### payments

```sql
CREATE TABLE payments (
    payment_id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    transaction_ref VARCHAR(120),
    paid_at TIMESTAMP,
    failed_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested payment status:

```text
PENDING
SUCCEEDED
FAILED
REFUNDED
```

---

## notification-service Database

### notifications

```sql
CREATE TABLE notifications (
    notification_id UUID PRIMARY KEY,
    recipient_user_id UUID,
    recipient_patient_id UUID,
    channel VARCHAR(30) NOT NULL,
    type VARCHAR(80) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    read_at TIMESTAMP,
    sent_at TIMESTAMP,
    failed_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);
```

Suggested channel:

```text
IN_APP
EMAIL
SMS
```

Suggested status:

```text
PENDING
SENT
FAILED
READ
```

Indexes:

```sql
CREATE INDEX idx_notifications_recipient_user ON notifications(recipient_user_id, created_at DESC);
CREATE INDEX idx_notifications_recipient_patient ON notifications(recipient_patient_id, created_at DESC);
CREATE INDEX idx_notifications_status ON notifications(status);
```

### processed_events

Consumer idempotency table.

```sql
CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT now()
);
```

---

## reporting-service Database

Reporting service là read model riêng. Nó consume event và ghi bảng tổng hợp để đọc nhanh. Service này không query trực tiếp database của service khác.

### report_daily_appointments

```sql
CREATE TABLE report_daily_appointments (
    report_date DATE NOT NULL,
    department_id UUID,
    doctor_id UUID,
    status VARCHAR(30) NOT NULL,
    total_count INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (report_date, department_id, doctor_id, status)
);
```

### report_monthly_revenue

```sql
CREATE TABLE report_monthly_revenue (
    report_month DATE NOT NULL,
    department_id UUID,
    total_invoices INT NOT NULL DEFAULT 0,
    paid_invoices INT NOT NULL DEFAULT 0,
    total_revenue NUMERIC(14, 2) NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (report_month, department_id)
);
```

`report_month` nên lưu ngày đầu tháng, ví dụ `2026-05-01`.

### report_doctor_utilization

```sql
CREATE TABLE report_doctor_utilization (
    report_date DATE NOT NULL,
    doctor_id UUID NOT NULL,
    total_slots INT NOT NULL DEFAULT 0,
    booked_slots INT NOT NULL DEFAULT 0,
    completed_appointments INT NOT NULL DEFAULT 0,
    no_show_appointments INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (report_date, doctor_id)
);
```

### report_medicine_usage

```sql
CREATE TABLE report_medicine_usage (
    report_month DATE NOT NULL,
    medicine_id UUID NOT NULL,
    medicine_name_snapshot VARCHAR(180) NOT NULL,
    dispensed_quantity INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (report_month, medicine_id)
);
```

### processed_events

```sql
CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT now()
);
```

---

## Outbox Tables

Nếu có thời gian ở tuần 5, thêm outbox pattern cho service publish event quan trọng.

Ví dụ trong appointment-service:

```sql
CREATE TABLE outbox_events (
    outbox_id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    event_version INT NOT NULL,
    payload JSONB NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    published_at TIMESTAMP
);
```

Suggested status:

```text
PENDING
PUBLISHED
FAILED
```

Index:

```sql
CREATE INDEX idx_outbox_events_status_created_at
ON outbox_events(status, created_at);
```

Ý nghĩa:

- Transaction vừa update appointment vừa insert outbox event.
- Background job đọc outbox và publish Kafka.
- Tránh lỗi DB commit thành công nhưng Kafka publish thất bại.

---

## Cross-Service Reference Strategy

Không FK xuyên service:

```text
appointment.patient_id -> patient-service sở hữu patient
appointment.doctor_id -> appointment-service sở hữu doctor
medical_record.patient_id -> patient-service sở hữu patient
billing.patient_id -> patient-service sở hữu patient
pharmacy.patient_id -> patient-service sở hữu patient
pharmacy.prescription_id -> medical-record-service sở hữu prescription
reporting.* -> read model từ event, không sở hữu source of truth
```

Khi cần hiển thị thông tin:

- Cách 1: gọi API service chủ sở hữu.
- Cách 2: lưu snapshot tối thiểu trong service hiện tại.

Ví dụ invoice có thể lưu snapshot:

```text
patient_name_snapshot
patient_phone_snapshot
```

Điều này giúp invoice không thay đổi khi patient đổi tên/số điện thoại.

---

## Queries Nên Luyện Trong Project

### Appointment dashboard

- Số appointment hôm nay theo status.
- Số appointment theo doctor/date.
- No-show rate theo tháng.
- Doctor utilization: số slot đã book / tổng slot.

### Billing dashboard

- Doanh thu theo ngày/tháng.
- Doanh thu theo department.
- Top patient theo total paid giả lập.
- Invoice unpaid quá 7 ngày.

### Medical dashboard

- Số encounter theo doctor.
- Số prescription theo tháng.
- Diagnosis phổ biến nhất.

### Pharmacy dashboard

- Thuốc sắp hết hàng.
- Thuốc được dispense nhiều nhất theo tháng.
- Prescription pending quá lâu.

### Reporting dashboard

- Appointment theo ngày/status.
- Revenue theo tháng.
- Doctor utilization.
- Medicine usage.

---

## Migration Strategy

Khuyến nghị dùng Liquibase cho mỗi service.

Lý do:

- Phù hợp Spring Boot enterprise.
- Nhiều công ty Java dùng Liquibase.
- Quản lý changeSet rõ ràng.
- Dễ review migration theo id/author.
- Có thể viết rollback khi cần.

Flyway vẫn là alternative tốt nếu muốn SQL-first và nhẹ hơn, nhưng project này ưu tiên Liquibase để sát môi trường công ty hơn.

Ví dụ:

```text
appointment-service
  src/main/resources/db/changelog
    db.changelog-master.yaml
    changes
      001-create-departments.yaml
      002-create-doctors.yaml
      003-create-schedules.yaml
      004-create-appointments.yaml
```

Rule:

- Không sửa migration cũ sau khi đã chạy ở môi trường shared.
- Thêm migration mới để thay đổi schema.
- Mỗi changeSet có `id` và `author` rõ ràng.
- Constraint/index nên đặt tên rõ ràng.
- Seed data tách riêng nếu cần.

---

## PostgreSQL Patterns Cần Master

### Prevent double booking

```sql
CREATE UNIQUE INDEX uq_appointments_doctor_slot_active
ON appointments (doctor_id, appointment_date, start_time)
WHERE status NOT IN ('CANCELLED', 'NO_SHOW');
```

### Atomic update example

Nếu sau này có inventory/medicine stock:

```sql
UPDATE medicine_stock
SET available_quantity = available_quantity - :quantity
WHERE medicine_id = :medicineId
  AND available_quantity >= :quantity;
```

Nếu affected rows = 0, nghĩa là không đủ tồn kho.

### Pagination

MVP dùng page/size được.

Về sau học keyset pagination:

```sql
WHERE created_at < :lastSeenCreatedAt
ORDER BY created_at DESC
LIMIT 20;
```

### Explain

Các query cần chạy `EXPLAIN ANALYZE`:

- Search patient.
- Doctor appointments by date.
- Patient appointment history.
- Invoice list by status.
- Dashboard revenue by month.

---

## Local Development Database Option

Bạn có thể dùng một PostgreSQL container duy nhất:

```yaml
postgres:
  image: postgres:16
  environment:
    POSTGRES_USER: hospital
    POSTGRES_PASSWORD: hospital123
  ports:
    - "5432:5432"
```

Sau đó tạo nhiều database:

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

Mỗi service connect tới database riêng.

Đây là setup tốt nhất cho học microservices nhưng vẫn tiết kiệm tài nguyên.
