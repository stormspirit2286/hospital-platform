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

---

## 10. Smoke Test End-To-End (2026-05-26)

Muc tieu:

```text
Verify chain config-service -> discovery-service -> auth-service -> api-gateway hoat dong.
Test cac flow auth chinh qua gateway: login, /me, refresh + rotation, logout, error cases.
```

### 10.1 Stack Run Order

```text
config-service     :8888  UP
discovery-service  :8761  UP
auth-service       :8081  UP, registered on Eureka
api-gateway        :8080  UP, registered on Eureka
```

Eureka apps thay duoc:

```text
API-GATEWAY    192.168.x.x:api-gateway:8080   UP
AUTH-SERVICE   192.168.x.x:auth-service:8081  UP
```

### 10.2 Test Users (seed qua Liquibase)

File migration moi: `auth-service/src/main/resources/db/changelog/changes/004-seed-test-users.sql`.

Password duoc hash bang BCrypt rounds=10 va seed vao bang `users`, role gan vao `user_roles`.

```text
admin@hospital.local       Admin@12345       ADMIN
doctor1@hospital.local     Doctor@12345      DOCTOR
patient1@hospital.local    Patient@12345     PATIENT
reception1@hospital.local  Reception@12345   RECEPTIONIST
```

Tat ca request smoke test goi qua gateway `http://localhost:8080`, KHONG goi truc tiep auth-service.

### 10.3 Ket Qua Tung Test Case

| # | Kich Ban                                                  | Endpoint                            | Ky Vong                       | Thuc Te |
|---|-----------------------------------------------------------|-------------------------------------|-------------------------------|---------|
| 1 | Login admin                                               | POST /api/auth/login                | 200 + accessToken + refreshToken + roles=[ADMIN]       | OK |
| 2 | Login doctor1                                             | POST /api/auth/login                | 200 + roles=[DOCTOR]          | OK |
| 3 | Login patient1                                            | POST /api/auth/login                | 200 + roles=[PATIENT]         | OK |
| 4 | Login reception1                                          | POST /api/auth/login                | 200 + roles=[RECEPTIONIST]    | OK |
| 5 | GET /me voi access token hop le                           | GET  /api/auth/me                   | 200 + user JSON               | OK |
| 6 | GET /me KHONG co token                                    | GET  /api/auth/me                   | 401                           | OK |
| 7 | GET /me voi token rac                                     | GET  /api/auth/me                   | 401                           | OK |
| 8 | Login sai password                                        | POST /api/auth/login                | 401 INVALID_CREDENTIALS       | OK |
| 9 | Refresh -> cap moi va rotate                              | POST /api/auth/refresh              | 200, refresh token moi != cu  | OK |
| 10| Reuse refresh token cu sau khi rotate                     | POST /api/auth/refresh              | 401 INVALID_REFRESH_TOKEN     | OK |
| 11| Logout (Bearer access + refresh body)                     | POST /api/auth/logout               | 200 Logged out successfully   | OK |
| 12| Refresh lai sau khi logout                                | POST /api/auth/refresh              | 401 INVALID_REFRESH_TOKEN     | OK |
| 13| Goi route protected khong token                           | GET  /api/patients/me               | 401                           | OK |
| 14| Goi route khong dinh nghia                                | GET  /api/unknown                   | 401 (deny by default)         | OK |
| 15| Forward X-Correlation-Id tren routed response             | POST /api/auth/login + header       | echo lai dung gia tri client gui | OK |

Luu y test case 11: `/api/auth/logout` o gateway la PROTECTED route, nen phai gui ca `Authorization: Bearer <accessToken>` lan refresh token trong body. Goi logout chi voi body se bi gateway tra 401.

Luu y test case 15: `beforeCommit` cua `CorrelationIdFilter` apply tren request co route forward. Endpoint actuator/health duoc serve truc tiep boi gateway nen response co the khong co header `X-Correlation-Id`; voi cac route forward thuc su (vi du `/api/auth/login`) header duoc echo lai dung.

### 10.4 Ket Luan

```text
config-service, discovery-service, auth-service, api-gateway: san sang cho giai doan business service.
JWT HS256 issuer=hospital-auth-service: gateway verify offline OK.
Refresh token opaque + SHA-256 hash + rotation + revoke-on-logout: hoat dong dung.
Identity forward (X-User-Id, X-User-Email, X-User-Roles) duoc tin cay vi gateway sanitize header truoc khi set lai.
```

Phan con thieu (theo dau cho cac buoc sau):

```text
1. Rate limit /api/auth/login va /api/auth/refresh qua Redis (chong brute force).
2. CorsFilter cho frontend.
3. ErrorHandlingFilter chuan hoa body loi tai gateway (4xx/5xx tu downstream).
4. Coarse-grained authorization theo role tai gateway (ADMIN/DOCTOR/PATIENT...).
5. auth-service: enforce issuer trong JwtDecoder, handle UUID parse exception trong /me.
6. Dua secret va DB credentials ra config server hoac env vars cho ngoai moi truong local.
```
