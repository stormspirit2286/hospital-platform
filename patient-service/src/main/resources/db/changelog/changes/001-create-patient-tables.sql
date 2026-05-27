--liquibase formatted sql

--changeset duy:001-create-patients
CREATE TABLE IF NOT EXISTS patients (
    patient_id  UUID         PRIMARY KEY,
    user_id     UUID         UNIQUE,
    first_name  VARCHAR(80)  NOT NULL,
    last_name   VARCHAR(80)  NOT NULL,
    email       VARCHAR(150),
    phone       VARCHAR(30)  NOT NULL,
    date_of_birth DATE       NOT NULL,
    gender      VARCHAR(20),
    address     VARCHAR(255),
    city        VARCHAR(100),
    status      VARCHAR(30)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now()
);

--changeset duy:001-index-patients
CREATE INDEX IF NOT EXISTS idx_patients_user_id   ON patients(user_id);
CREATE INDEX IF NOT EXISTS idx_patients_phone     ON patients(phone);
CREATE INDEX IF NOT EXISTS idx_patients_email     ON patients(email);
CREATE INDEX IF NOT EXISTS idx_patients_name      ON patients(last_name, first_name);

--changeset duy:001-create-emergency-contacts
CREATE TABLE IF NOT EXISTS emergency_contacts (
    contact_id  UUID         PRIMARY KEY,
    patient_id  UUID         NOT NULL,
    full_name   VARCHAR(150) NOT NULL,
    relationship VARCHAR(80),
    phone       VARCHAR(30)  NOT NULL,
    address     VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now()
);

--changeset duy:001-index-emergency-contacts
CREATE INDEX IF NOT EXISTS idx_emergency_contacts_patient_id ON emergency_contacts(patient_id);

--changeset duy:001-create-patient-insurances
CREATE TABLE IF NOT EXISTS patient_insurances (
    insurance_id  UUID         PRIMARY KEY,
    patient_id    UUID         NOT NULL,
    provider_name VARCHAR(150) NOT NULL,
    policy_number VARCHAR(100) NOT NULL,
    valid_from    DATE,
    valid_to      DATE,
    status        VARCHAR(30)  NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now()
);

--changeset duy:001-index-patient-insurances
CREATE INDEX IF NOT EXISTS idx_patient_insurances_patient_id ON patient_insurances(patient_id);
