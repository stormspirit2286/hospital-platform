--liquibase formatted sql

--changeset duy:002-seed-roles
INSERT INTO roles (code, name) VALUES
    ('ADMIN', 'Administrator'),
    ('PATIENT', 'Patient'),
    ('DOCTOR', 'Doctor'),
    ('RECEPTIONIST', 'Receptionist'),
    ('BILLING_STAFF', 'Billing Staff'),
    ('PHARMACIST', 'Pharmacist')
ON CONFLICT (code) DO NOTHING;
