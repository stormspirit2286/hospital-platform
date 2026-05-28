--liquibase formatted sql

--changeset duy:003-drop-provider-name
ALTER TABLE patient_insurances
    DROP COLUMN provider_name;

--changeset duy:003-rename-policy-number-to-card-number
ALTER TABLE patient_insurances
    RENAME COLUMN policy_number TO card_number;

--changeset duy:003-shrink-card-number-length
ALTER TABLE patient_insurances
    ALTER COLUMN card_number TYPE VARCHAR(15);

--changeset duy:003-unique-card-number
ALTER TABLE patient_insurances
    ADD CONSTRAINT uq_patient_insurances_card_number UNIQUE (card_number);

--changeset duy:003-add-initial-facility-code
ALTER TABLE patient_insurances
    ADD COLUMN initial_facility_code VARCHAR(20);

--changeset duy:003-add-benefit-rate
ALTER TABLE patient_insurances
    ADD COLUMN benefit_rate INTEGER;
