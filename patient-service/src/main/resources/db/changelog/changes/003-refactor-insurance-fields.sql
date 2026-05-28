--liquibase formatted sql

--changeset duy:003-drop-old-insurance-columns
ALTER TABLE patient_insurances DROP COLUMN IF EXISTS provider_name;
ALTER TABLE patient_insurances DROP COLUMN IF EXISTS policy_number;

--changeset duy:003-add-new-insurance-columns
ALTER TABLE patient_insurances ADD COLUMN IF NOT EXISTS card_number             VARCHAR(15);
ALTER TABLE patient_insurances ADD COLUMN IF NOT EXISTS participant_type        VARCHAR(2);
ALTER TABLE patient_insurances ADD COLUMN IF NOT EXISTS initial_facility_code   VARCHAR(20);
ALTER TABLE patient_insurances ADD COLUMN IF NOT EXISTS benefit_rate            VARCHAR(20);
ALTER TABLE patient_insurances ADD COLUMN IF NOT EXISTS continuous_from         DATE;

--changeset duy:003-alter-status-length
ALTER TABLE patient_insurances ALTER COLUMN status TYPE VARCHAR(30);

--changeset duy:003-card-number-constraints
ALTER TABLE patient_insurances ALTER COLUMN card_number SET NOT NULL;
ALTER TABLE patient_insurances ADD CONSTRAINT uq_patient_insurances_card_number UNIQUE (card_number);

--changeset duy:003-index-card-number
CREATE INDEX IF NOT EXISTS idx_patient_insurances_card_number ON patient_insurances(card_number);

