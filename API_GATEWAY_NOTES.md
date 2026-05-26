# API Gateway Notes

## 1. Vai Tro Cua API Gateway

Trong project hospital-platform, `api-gateway` la cua vao duy nhat cho client.

Client khong nen goi truc tiep:

```text
auth-service
patient-service
appointment-service
```

Client nen goi qua:

```text
api-gateway -> internal services
```

Vi du:

```text
Client
-> http://localhost:8080/api/auth/**
-> api-gateway
-> auth-service
```

Nhiem vu chinh cua API Gateway:

```text
1. Dieu huong request toi service dung.
2. Validate JWT access token.
3. Forward user identity xuong service noi bo.
4. Gan correlation id de trace request.
5. Rate limit cac API nhay cam.
6. Cau hinh CORS cho frontend.
7. Log request/response o bien he thong.
8. Chuan hoa loi o tang gateway.
```

---

## 2. CorrelationIdFilter Dung De Lam Gi?

`CorrelationIdFilter` khong phai la security feature. No la observability/debugging feature.

Trong production, mot request co the di qua nhieu thanh phan:

```text
Client
-> api-gateway
-> appointment-service
-> Redis
-> PostgreSQL
-> Kafka
-> notification-service
```

Neu khong co correlation id, khi loi xay ra se rat kho noi log cua cac service voi nhau.

`CorrelationIdFilter` se dam bao moi request co mot header:

```text
X-Correlation-Id: <uuid>
```

Neu client da gui `X-Correlation-Id`, gateway giu lai.

Neu client chua gui, gateway tu sinh UUID moi.

Sau do gateway:

```text
1. Forward X-Correlation-Id xuong service phia sau.
2. Tra lai X-Correlation-Id trong response.
3. Log method, path, status, duration, correlationId.
```

Vi du log:

```text
api-gateway          correlationId=abc-123 path=/api/appointments status=200
appointment-service  correlationId=abc-123 create appointment
appointment-service  correlationId=abc-123 publish AppointmentBooked
notification-service correlationId=abc-123 consume AppointmentBooked
```

Khi user bao loi, minh co the search theo `correlationId` de thay toan bo flow.

---

## 3. Cac Filter Thuong Co Trong Production Gateway

### 3.1 CorrelationIdFilter

Dung de trace mot request xuyen qua nhieu service.

Header:

```text
X-Correlation-Id
```

### 3.2 JwtAuthenticationFilter

Dung de validate JWT access token tai gateway.

Flow:

```text
Client gui Authorization: Bearer <access_token>
-> api-gateway verify JWT
-> api-gateway extract userId, role, permissions
-> api-gateway forward identity xuong internal service
```

Internal headers co the la:

```text
X-User-Id
X-User-Role
X-Correlation-Id
```

### 3.3 AuthorizationFilter

Dung de chan route theo role o muc coarse-grained.

Vi du:

```text
PATIENT  -> /api/patients/me
ADMIN    -> /api/admin/**
DOCTOR   -> /api/medical-records/**
```

Gateway chi nen check rule tong quat theo route.

Rule nghiep vu chi tiet van phai nam trong service.

Vi du:

```text
PATIENT chi duoc xem profile cua chinh minh.
DOCTOR chi duoc xem patient co appointment voi minh.
```

Nhung rule nay nen nam trong `patient-service` hoac service nghiep vu tuong ung.

### 3.4 RateLimitFilter

Dung de chong spam hoac brute force.

Vi du:

```text
/api/auth/login        -> 5 requests/minute/IP
/api/auth/refresh      -> 20 requests/minute/user
/api/appointments      -> 10 requests/minute/user
```

Thuong dung Redis de luu counter/rate-limit state.

### 3.5 CorsFilter

Dung de cho phep frontend goi API hop le.

Local frontend:

```text
http://localhost:5173
http://localhost:3000
```

Production frontend:

```text
https://app.yourdomain.com
```

### 3.6 RequestLoggingFilter

Dung de log request o bien he thong.

Nen log:

```text
method
path
status
durationMs
correlationId
```

Khong nen log:

```text
password
access token
refresh token
personal sensitive data
```

### 3.7 HeaderSanitizingFilter

Dung de chan client gia mao internal headers.

Vi du client co the co tinh gui:

```text
X-User-Id: admin-user-id
X-User-Role: ADMIN
```

Gateway phai remove cac header nay tu request ben ngoai, sau do tu set lai sau khi validate JWT.

### 3.8 ErrorHandlingFilter

Dung de chuan hoa loi o tang gateway.

Vi du:

```json
{
  "code": "UNAUTHORIZED",
  "message": "Missing or invalid token",
  "correlationId": "abc-123"
}
```

---

## 4. Gateway Co Nen Goi Auth-Service De Validate JWT Khong?

Thong thuong voi JWT access token, gateway khong can goi `auth-service` moi lan validate token.

Flow dung hon:

```text
1. Client login vao auth-service.
2. auth-service cap access token va refresh token.
3. Client goi API bang access token.
4. api-gateway verify JWT offline bang secret/public key.
5. api-gateway forward user identity xuong service.
```

Ly do khong nen goi `auth-service` moi request:

```text
1. Tang latency.
2. Auth-service thanh bottleneck.
3. Auth-service down thi toan he thong bi anh huong.
4. JWT sinh ra de co the verify offline.
```

Tuy nhien `auth-service` van rat quan trong.

`auth-service` phu trach:

```text
login
refresh token
logout
issue access token
issue refresh token
manage users
manage roles
password hashing
token revoke/blacklist neu can
```

---

## 5. Security Flow De Nho

Tong quan:

```text
auth-service: cap token
api-gateway: verify token
business services: xu ly nghiep vu dua tren identity header
```

Flow chi tiet:

```text
POST /api/auth/login
-> api-gateway route toi auth-service
-> auth-service verify email/password
-> auth-service tra access token + refresh token

GET /api/patients/me
-> client gui Authorization: Bearer <access_token>
-> api-gateway verify JWT
-> api-gateway set X-User-Id va X-User-Role
-> patient-service doc X-User-Id
-> patient-service tra profile cua user hien tai
```

---

## 6. Vi Sao Chua Lam JWT Filter Ngay?

Chua nen lam JWT filter truoc khi `auth-service` co login/token that.

Gateway JWT filter can biet ro contract cua token:

```text
JWT dung HS256 hay RS256?
Secret/private key dat o dau?
Claim user id ten gi? sub hay userId?
Role claim ten gi? roles hay authorities?
Access token het han sau bao lau?
Refresh token luu DB nhu the nao?
Logout co blacklist access token khong?
```

Hien tai `auth-service` moi co:

```text
Liquibase schema
roles seed data
database connection
Eureka registration
```

Chua co:

```text
User entity/repository
PasswordEncoder
Login API
JWT issuer
Refresh token API
SecurityConfig
Token claims contract
```

Neu lam JWT filter ngay, gateway se phai doan token format. Sau nay auth-service lam khac thi gateway phai sua lai.

Thu tu dung:

```text
1. api-gateway route + correlation id.
2. auth-service login/JWT issuer.
3. Chot JWT claims contract.
4. api-gateway JWT validation filter.
5. api-gateway forward X-User-Id, X-User-Role.
6. business services doc internal headers.
```

---

## 7. Trang Thai Hien Tai Cua api-gateway

Da co:

```text
1. Route config.
2. Eureka client.
3. Temporary permit-all security config.
4. CorrelationIdFilter.
5. Request/response logging co correlation id.
6. Actuator gateway routes endpoint.
```

Dang co route:

```text
/api/auth/**                                -> auth-service
/api/patients/**                            -> patient-service
/api/appointments/**,/api/departments/**,
/api/doctors/**                             -> appointment-service
```

Test route hien tai:

```text
GET http://localhost:8080/api/auth/not-found
```

Ket qua hien tai:

```text
401 Unauthorized
X-Correlation-Id: <value>
```

`401` la do `auth-service` chua co security config/public auth API. Dieu nay chung minh gateway da route sang auth-service, khong phai loi route cua gateway.

---

## 8. Thu Tu Service Nen Start O Giai Doan Nay

Hien tai nen start:

```text
1. config-service      -> port 8888
2. discovery-service   -> port 8761
3. auth-service        -> port 8081
4. api-gateway         -> port 8080
```

Ly do van nen start `auth-service`:

```text
api-gateway route /api/auth/** toi auth-service.
Neu auth-service khong chay, gateway route se gap loi khong tim thay instance.
```

Chua can start:

```text
patient-service
appointment-service
```

Ly do:

```text
Hai service nay chua co datasource/Liquibase va business code day du.
```

Sau khi start, kiem tra:

```text
http://localhost:8761
```

Can thay:

```text
AUTH-SERVICE
API-GATEWAY
```

Kiem tra gateway:

```text
GET http://localhost:8080/actuator/health
GET http://localhost:8080/actuator/gateway/routes
```

---

## 9. Buoc Tiep Theo

Buoc tiep theo nen lam la `auth-service`:

```text
1. Tao entity User, Role, RefreshToken.
2. Tao repository.
3. Tao DTO login/refresh/me.
4. Tao PasswordEncoder.
5. Tao JwtService de issue token.
6. Tao AuthController.
7. Cau hinh public routes cho /api/auth/login va /api/auth/refresh.
8. Chot JWT claims contract cho gateway dung sau nay.
```
