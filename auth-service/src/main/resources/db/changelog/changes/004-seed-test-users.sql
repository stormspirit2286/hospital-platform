--liquibase formatted sql

--changeset duy:004-seed-test-users
INSERT INTO users (user_id, email, password_hash, full_name, phone, status) VALUES
    (
        '22222222-2222-2222-2222-222222222222',
        'doctor1@hospital.local',
        '$2b$10$vevguPhf.AT/Jmg883s0gONywUZ.9TwHEPyEn4ZwxXHmSxXDJ4gv2',
        'Doctor One',
        '0911111111',
        'ACTIVE'
    ),
    (
        '33333333-3333-3333-3333-333333333333',
        'patient1@hospital.local',
        '$2b$10$jebxlnFN6MVyA4iNjrXZyu7THFesQP6n3gvyyyvAnojIPsOQQ4PqC',
        'Patient One',
        '0922222222',
        'ACTIVE'
    ),
    (
        '44444444-4444-4444-4444-444444444444',
        'reception1@hospital.local',
        '$2b$10$vQ0lM6mX4hNLBTtVCDzBL.6N5.hIxymQgj17G8Mww61k1YOSFCTNK',
        'Reception One',
        '0933333333',
        'ACTIVE'
    )
ON CONFLICT (email) DO NOTHING;

--changeset duy:004-assign-test-user-roles
INSERT INTO user_roles (user_id, role_id)
SELECT '22222222-2222-2222-2222-222222222222', role_id FROM roles WHERE code = 'DOCTOR'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT '33333333-3333-3333-3333-333333333333', role_id FROM roles WHERE code = 'PATIENT'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT '44444444-4444-4444-4444-444444444444', role_id FROM roles WHERE code = 'RECEPTIONIST'
ON CONFLICT (user_id, role_id) DO NOTHING;
