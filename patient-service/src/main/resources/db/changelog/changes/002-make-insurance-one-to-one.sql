--liquibase formatted sql

--changeset duy:002-unique-patient-insurance-patient-id
ALTER TABLE patient_insurances
    ADD CONSTRAINT uq_patient_insurances_patient_id UNIQUE (patient_id);

