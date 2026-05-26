--liquibase formatted sql

--changeset duy:003-seed-local-admin
INSERT INTO users (user_id, email, password_hash, full_name, phone, status)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'admin@hospital.local',
    '$2a$10$SdLdN.zO383YennDx5nfjOvc.PG0GDgPE1rBp/mc95G/ttRVeOxCS',
    'Local Admin',
    '0900000000',
    'ACTIVE'
)
ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT '11111111-1111-1111-1111-111111111111', role_id
FROM roles
WHERE code = 'ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;
