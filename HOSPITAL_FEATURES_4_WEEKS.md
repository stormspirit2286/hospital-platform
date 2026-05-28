# Hospital Microservices Project - 5 Week Feature Scope

## Mục tiêu

Xây dựng hệ thống quản lý phòng khám/bệnh viện vừa đủ để học sâu Java Spring Boot microservices:

- ReactJS client theo role.
- Spring Boot microservices.
- PostgreSQL cho dữ liệu chính.
- Redis cho cache, idempotency, rate limit, lock.
- Kafka cho event-driven flow.
- API Gateway, Eureka Discovery, Config Server.
- Docker Compose local.
- Deployment AWS ở mức production-like.

Scope này không nhằm làm một HIS đầy đủ. Mục tiêu là master một flow backend thực tế:

```text
Patient đặt lịch
-> Appointment Service giữ slot
-> Kafka event
-> Notification Service gửi thông báo
-> Receptionist check-in
-> Doctor khám và tạo medical record
-> Pharmacy xử lý prescription
-> Billing Service tạo invoice
-> Payment update
-> Notification Service báo kết quả
```

Chỉ dùng dữ liệu giả lập. Không dùng dữ liệu bệnh nhân thật.

---

## Roles

### ADMIN

- Quản lý department.
- Quản lý doctor.
- Quản lý receptionist.
- Quản lý billing staff.
- Xem dashboard tổng quan.

### PATIENT

- Xem/cập nhật hồ sơ cá nhân.
- Tìm department/doctor.
- Xem slot khám còn trống.
- Đặt lịch khám.
- Hủy lịch khám khi còn được phép.
- Xem lịch sử appointment.
- Xem medical record của bản thân.
- Xem invoice/payment status.
- Nhận notification.

### DOCTOR

- Xem lịch khám theo ngày.
- Xem danh sách patient đã check-in.
- Xem patient summary.
- Tạo encounter/medical record.
- Ghi diagnosis, treatment note.
- Tạo prescription.
- Hoàn tất buổi khám.

### RECEPTIONIST

- Tìm patient.
- Xác nhận appointment.
- Check-in patient.
- Mark no-show.
- Tạo appointment hộ patient.

### BILLING_STAFF

- Xem invoice.
- Thêm invoice item.
- Xác nhận payment.
- Mark invoice paid/failed/cancelled.

### PHARMACIST

- Xem prescription cần cấp thuốc.
- Xem medicine catalog.
- Cập nhật medicine stock.
- Mark prescription dispensed.
- Xem lịch sử cấp thuốc.

---

## Services

### api-gateway

Trách nhiệm:

- Route request tới service nội bộ.
- Validate JWT.
- Gắn `X-User-Id`, `X-User-Role`, `X-Correlation-Id` vào request.
- Rate limit một số API nhạy cảm bằng Redis.
- Public routes:
  - login
  - refresh token
  - health check
- Private routes:
  - patient
  - appointment
  - medical record
  - billing
  - admin

Nên có:

- Global CORS config.
- Gateway filter cho correlation id.
- Gateway auth filter.
- Request logging tối thiểu.

### discovery-service

Trách nhiệm:

- Eureka Server.
- Các service đăng ký vào Eureka.
- Gateway route theo service name.

### config-service

Trách nhiệm:

- Centralized config.
- Profile:
  - `local`
  - `docker`
  - `aws`
- Config cần quản lý:
  - datasource
  - redis
  - kafka
  - jwt public/secret config
  - service URL
  - logging level

### auth-service

Bạn đã mạnh auth, nên service này làm vừa đủ:

- Login.
- Refresh token.
- Logout.
- Validate token nếu gateway cần gọi.
- Role/permission seed data.

Không cần dành quá nhiều thời gian cho service này.

### patient-service

API chính:

- `POST /patients`
- `GET /patients/{id}`
- `PATCH /patients/{id}`
- `GET /patients/search?keyword=...`
- `GET /patients/{id}/summary`
- `GET /patients/me`
- `PATCH /patients/me`

Feature:

- Patient profile.
- Emergency contact.
- Insurance info giả lập.
- Search patient theo name/phone/email.
- Patient summary dùng cho doctor.

Validation:

- Email hợp lệ.
- Phone không trống.
- Date of birth không được ở tương lai.
- Gender enum.

### appointment-service

Đây là service quan trọng nhất.

API department/doctor/schedule:

- `POST /departments`
- `GET /departments`
- `POST /doctors`
- `GET /doctors`
- `GET /doctors/{doctorId}`
- `POST /doctors/{doctorId}/schedules`
- `GET /doctors/{doctorId}/available-slots?date=yyyy-mm-dd`

API appointment:

- `POST /appointments`
- `GET /appointments/{id}`
- `GET /appointments/me`
- `GET /appointments?doctorId=&date=&status=`
- `PATCH /appointments/{id}/confirm`
- `PATCH /appointments/{id}/cancel`
- `PATCH /appointments/{id}/check-in`
- `PATCH /appointments/{id}/start`
- `PATCH /appointments/{id}/complete`
- `PATCH /appointments/{id}/no-show`

Appointment status:

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
- Chỉ appointment `REQUESTED` hoặc `CONFIRMED` mới được cancel.
- Chỉ receptionist/admin được check-in.
- Chỉ doctor được start/complete appointment.
- Khi book appointment, cần idempotency key để tránh tạo trùng.

Redis use cases:

- Cache available slots theo doctor/date.
- Lock khi book slot:

```text
lock:doctor:{doctorId}:slot:{slotStart}
```

- Idempotency:

```text
idempotency:appointment:{userId}:{idempotencyKey}
```

Kafka events:

- `AppointmentBooked`
- `AppointmentConfirmed`
- `AppointmentCancelled`
- `PatientCheckedIn`
- `AppointmentStarted`
- `AppointmentCompleted`
- `AppointmentNoShow`

### medical-record-service

API:

- `POST /encounters`
- `GET /encounters/{id}`
- `GET /patients/{patientId}/encounters`
- `POST /encounters/{id}/diagnoses`
- `POST /encounters/{id}/prescriptions`
- `PATCH /encounters/{id}/complete`

Feature:

- Tạo encounter khi doctor bắt đầu khám.
- Ghi chief complaint.
- Ghi diagnosis.
- Ghi treatment note.
- Tạo prescription.
- Hoàn tất encounter.

Business rules:

- Chỉ doctor phụ trách appointment mới được tạo/sửa encounter.
- Encounter completed thì không cho sửa nội dung chính, trừ admin override.
- Khi encounter completed, publish event để billing tạo invoice.

Kafka events:

- `EncounterCreated`
- `PrescriptionCreated`
- `ConsultationCompleted`

### billing-service

API:

- `GET /invoices/{id}`
- `GET /patients/{patientId}/invoices`
- `POST /invoices`
- `POST /invoices/{id}/items`
- `PATCH /invoices/{id}/mark-paid`
- `PATCH /invoices/{id}/mark-failed`
- `PATCH /invoices/{id}/cancel`

Feature:

- Tạo invoice từ event `ConsultationCompleted`.
- Tạo invoice item:
  - consultation fee
  - medicine fee giả lập
  - lab fee giả lập
- Payment giả lập.
- Invoice status:

```text
DRAFT
ISSUED
PAID
FAILED
CANCELLED
```

Kafka events:

- `InvoiceCreated`
- `PaymentSucceeded`
- `PaymentFailed`

### pharmacy-service

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

- Quản lý medicine catalog ở mức vừa đủ.
- Quản lý stock theo medicine.
- Consume event `PrescriptionCreated`.
- Tạo dispensing task cho pharmacist.
- Khi dispense, trừ stock bằng transaction.
- Nếu thiếu thuốc, mark prescription `PARTIALLY_DISPENSED` hoặc `REJECTED`.

Business rules:

- Chỉ pharmacist/admin được cập nhật medicine và stock.
- Không cho dispense nếu stock không đủ, trừ khi chọn partial dispense.
- Trừ stock phải dùng atomic update hoặc optimistic locking.
- Một prescription chỉ được dispense một lần thành công.

Kafka events:

- `PrescriptionReceived`
- `PrescriptionDispensed`
- `MedicineStockLow`

### notification-service

API:

- `GET /notifications/me`
- `PATCH /notifications/{id}/read`

Consumer:

- Consume appointment events.
- Consume consultation/payment events.
- Lưu notification log.
- Giả lập gửi email/SMS bằng log.

Notification types:

- Appointment booked.
- Appointment cancelled.
- Reminder.
- Consultation completed.
- Invoice created.
- Payment succeeded/failed.

Kafka:

- Có retry.
- Có dead letter topic.
- Consumer idempotency theo `event_id`.

### reporting-service

API:

- `GET /reports/appointments/daily?date=`
- `GET /reports/appointments/monthly?month=`
- `GET /reports/billing/revenue/monthly?year=`
- `GET /reports/doctors/utilization?from=&to=`
- `GET /reports/pharmacy/medicine-usage?from=&to=`

Feature:

- Consume events từ appointment, medical-record, billing, pharmacy.
- Build read model riêng cho dashboard/report.
- Không query trực tiếp database của service khác.
- Tối ưu cho đọc/report bằng table tổng hợp.

Business rules:

- Report có thể eventual consistency.
- Event consume phải idempotent bằng `event_id`.
- Nếu event tới trễ, report vẫn update được.

Kafka events consumed:

- `AppointmentBooked`
- `AppointmentCompleted`
- `AppointmentCancelled`
- `ConsultationCompleted`
- `InvoiceCreated`
- `PaymentSucceeded`
- `PaymentFailed`
- `PrescriptionDispensed`

---

## ReactJS Client Scope

### Layout

- Login page.
- Sidebar theo role.
- Topbar hiển thị user, role.
- Table/list pages.
- Detail pages.
- Form pages.
- Toast notification.
- Loading/error states.

### Patient Screens

- Patient dashboard.
- Profile.
- Find doctor.
- Available slots.
- Book appointment.
- My appointments.
- Appointment detail.
- My medical records.
- My invoices.
- Notifications.

### Doctor Screens

- Doctor dashboard.
- Today appointments.
- Appointment detail.
- Patient summary.
- Encounter form.
- Prescription form.
- Complete consultation.

### Receptionist Screens

- Appointment search.
- Check-in queue.
- Confirm/cancel appointment.
- Mark no-show.
- Create appointment for patient.

### Billing Screens

- Invoice list.
- Invoice detail.
- Add invoice item.
- Mark paid/failed.

### Pharmacy Screens

- Pending prescriptions.
- Prescription detail.
- Medicine catalog.
- Stock adjustment.
- Dispense medicine.

### Reporting Screens

- Appointment daily/monthly dashboard.
- Revenue dashboard.
- Doctor utilization dashboard.
- Medicine usage dashboard.

### Admin Screens

- Department management.
- Doctor management.
- User/role management tối thiểu.
- System dashboard.

---

## Kafka Topic Design

Suggested topics:

```text
appointment.events
medical-record.events
pharmacy.events
billing.events
reporting.dlq
notification.dlq
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

- Message key nên là aggregate id, ví dụ `appointmentId`, `encounterId`, `invoiceId`.
- Consumer phải idempotent bằng `eventId`.
- Event version phải có từ đầu.
- Không publish object entity thô. Publish event payload có chủ đích.

---

## Redis Use Cases

### Cache

- `department:list`
- `doctor:{doctorId}:profile`
- `doctor:{doctorId}:slots:{date}`
- `medicine:{medicineId}:detail`
- `medicine:list:active`

### Idempotency

- `idempotency:appointment:{userId}:{key}`
- TTL: 24h.
- Lưu request hash + response summary.

### Lock

- `lock:doctor:{doctorId}:slot:{slotStart}`
- TTL ngắn: 5-15s.
- Chỉ dùng để bảo vệ đoạn critical section khi book slot.
- Database unique constraint vẫn là lớp bảo vệ cuối cùng.

### Rate Limit

- Login.
- Book appointment.
- Search patient.
- Stock adjustment.

---

## AWS Target Architecture

Frontend:

```text
React build -> S3 -> CloudFront -> ACM HTTPS
```

Backend:

```text
ALB -> ECS services
```

Container registry:

```text
ECR
```

Database:

```text
RDS PostgreSQL
```

Cache:

```text
ElastiCache for Redis/Valkey
```

Kafka:

Option tiết kiệm:

```text
Kafka container on EC2
```

Option managed để học ngắn hạn:

```text
Amazon MSK
```

Monitoring:

```text
CloudWatch Logs
CloudWatch Metrics
AWS Budgets
```

Secrets:

```text
SSM Parameter Store hoặc Secrets Manager
```

Note:

- Với budget 200 USD, không nên để MSK/Fargate/RDS/ElastiCache chạy 24/7 quá lâu.
- Dùng AWS Budgets ngay từ ngày đầu.
- Khi không test, scale service về 0 hoặc stop/delete tài nguyên tốn tiền.

---

## 5 Week Plan

### Week 1 - Foundation + Core Services

Deliverables:

- Repo multi-module hoặc multi-repo rõ ràng.
- Docker Compose local:
  - PostgreSQL
  - Redis
  - Kafka
  - Eureka
  - Config Server
- `api-gateway`.
- `auth-service` tối thiểu.
- `patient-service`.
- `appointment-service` basic.
- React login + dashboard shell.

Must finish:

- Gateway route được tới service.
- JWT qua gateway.
- Patient CRUD.
- Doctor/department CRUD.
- Available slots API.
- Book appointment basic.
- Liquibase migration cho service DB.

### Week 2 - Redis + Kafka Flow

Deliverables:

- Redis cache available slots.
- Redis idempotency cho `POST /appointments`.
- Redis lock khi book slot.
- Kafka producer trong appointment-service.
- notification-service consume event.
- React patient booking flow hoàn chỉnh.

Must finish:

- `AppointmentBooked` event.
- `AppointmentCancelled` event.
- Notification log.
- Retry consumer cơ bản.
- DLQ topic cho notification.
- Không tạo trùng appointment khi retry request.

### Week 3 - Medical Record + Pharmacy

Deliverables:

- Doctor dashboard.
- Encounter/medical record flow.
- Prescription flow.
- Pharmacy service.
- Medicine catalog.
- Prescription dispensing flow.
- Kafka event từ medical-record sang pharmacy.

Must finish:

- Check-in appointment.
- Doctor start appointment.
- Create encounter.
- Create prescription.
- `PrescriptionCreated` event.
- pharmacy-service consume prescription.
- Pharmacist dispense medicine.
- Stock không bị âm khi dispense.

### Week 4 - Billing + Reporting + Distributed System Hardening

Deliverables:

- Billing service.
- Kafka event từ consultation sang billing.
- Invoice/payment giả lập.
- Reporting service.
- Read model cho dashboard.
- Consumer idempotency cho notification/reporting/billing/pharmacy.
- Retry + DLQ rõ ràng.

Must finish:

- Complete consultation.
- Billing auto-create invoice from `ConsultationCompleted`.
- Payment success/failure.
- Notification cho invoice/payment.
- reporting-service consume ít nhất 4 loại event.
- Dashboard revenue theo tháng.
- Dashboard appointment theo ngày/status.
- Logs có correlation id đi qua request và Kafka event.

### Week 5 - AWS Deployment + Final Polish

Deliverables:

- Docker image push ECR.
- Deploy frontend S3 + CloudFront.
- Deploy backend ECS/EC2.
- RDS PostgreSQL.
- ElastiCache Redis/Valkey hoặc Redis trên EC2 nếu muốn tiết kiệm.
- Kafka on EC2 hoặc MSK thử ngắn hạn.
- CloudWatch logs.
- Basic dashboard.
- README architecture.
- README deployment.
- Demo script.

Must finish:

- Public HTTPS frontend.
- Backend chạy qua gateway.
- RDS migration chạy được.
- Logs có correlation id.
- Health check.
- README deployment.
- Cost checklist.
- AWS Budgets configured.
- Demo end-to-end chạy được từ React.

---

## Definition of Done

Project được xem là hoàn thành khi:

- Người dùng patient đặt được lịch từ React.
- Appointment không bị duplicate khi retry.
- Slot không bị double-book ở cả Redis lock và DB constraint.
- Kafka event chạy qua ít nhất 3 service.
- Notification consumer có idempotency.
- Doctor tạo được medical record.
- Pharmacy dispense được prescription và trừ stock đúng.
- Billing tạo được invoice từ event.
- Payment update được trạng thái.
- Reporting service build được read model từ Kafka events.
- Tất cả service có migration.
- Deploy được lên AWS.
- Có log để trace một request từ gateway tới Kafka consumer.
