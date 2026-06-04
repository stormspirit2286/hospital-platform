--liquibase formatted sql

--changeset duy:001-create-departments
CREATE TABLE IF NOT EXISTS departments (
    department_id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

--changeset duy:001-create-doctors
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id UUID PRIMARY KEY,
    user_id UUID UNIQUE,
    department_id UUID NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(30),
    specialization VARCHAR(150),
    license_number VARCHAR(100) UNIQUE,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_doctors_department
        FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

--changeset duy:001-index-doctors
CREATE INDEX IF NOT EXISTS idx_doctors_department_id ON doctors(department_id);
CREATE INDEX IF NOT EXISTS idx_doctors_user_id ON doctors(user_id);

--changeset duy:001-create-doctor-schedules
CREATE TABLE IF NOT EXISTS doctor_schedules (
    schedule_id UUID PRIMARY KEY,
    doctor_id UUID NOT NULL,
    work_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_minutes INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_doctor_schedules_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    CONSTRAINT uq_doctor_schedules_doctor_date_time
        UNIQUE (doctor_id, work_date, start_time, end_time),
    CONSTRAINT chk_doctor_schedules_time_range
        CHECK (start_time < end_time),
    CONSTRAINT chk_doctor_schedules_slot_minutes
        CHECK (slot_minutes > 0)
);

--changeset duy:001-index-doctor-schedules
CREATE INDEX IF NOT EXISTS idx_doctor_schedules_doctor_date ON doctor_schedules(doctor_id, work_date);
CREATE INDEX IF NOT EXISTS idx_doctor_schedules_status_date ON doctor_schedules(status, work_date);

--changeset duy:001-create-appointments
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    doctor_id UUID NOT NULL,
    department_id UUID NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    reason TEXT,
    status VARCHAR(30) NOT NULL,
    booked_by_user_id UUID,
    idempotency_key VARCHAR(120),
    checked_in_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_appointments_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    CONSTRAINT fk_appointments_department
        FOREIGN KEY (department_id) REFERENCES departments(department_id),
    CONSTRAINT chk_appointments_time_range
        CHECK (start_time < end_time)
);

--changeset duy:001-index-appointments
CREATE UNIQUE INDEX IF NOT EXISTS uq_appointments_doctor_slot_active
    ON appointments (doctor_id, appointment_date, start_time)
    WHERE status NOT IN ('CANCELLED', 'NO_SHOW');

CREATE UNIQUE INDEX IF NOT EXISTS uq_appointments_patient_slot_active
    ON appointments (patient_id, appointment_date, start_time)
    WHERE status NOT IN ('CANCELLED', 'NO_SHOW');

CREATE UNIQUE INDEX IF NOT EXISTS uq_appointments_idempotency
    ON appointments (booked_by_user_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_appointments_patient_date
    ON appointments(patient_id, appointment_date DESC);

CREATE INDEX IF NOT EXISTS idx_appointments_doctor_date_status
    ON appointments(doctor_id, appointment_date, status);

CREATE INDEX IF NOT EXISTS idx_appointments_status_date
    ON appointments(status, appointment_date);

CREATE INDEX IF NOT EXISTS idx_appointments_department_date
    ON appointments(department_id, appointment_date);

--changeset duy:001-create-appointment-status-history
CREATE TABLE IF NOT EXISTS appointment_status_history (
    history_id UUID PRIMARY KEY,
    appointment_id UUID NOT NULL,
    old_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    changed_by_user_id UUID,
    reason TEXT,
    changed_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_appointment_status_history_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

--changeset duy:001-index-appointment-status-history
CREATE INDEX IF NOT EXISTS idx_appointment_status_history_appointment
    ON appointment_status_history(appointment_id, changed_at);
