-- =========================
-- KEYSPACE
-- =========================
CREATE KEYSPACE IF NOT EXISTS doctor_service
    WITH replication = {
        'class': 'SimpleStrategy',
        'replication_factor': 1
        };

USE doctor_service;

-- =========================
-- DOCTOR
-- =========================
CREATE TABLE IF NOT EXISTS doctors (
                                       doctor_id UUID PRIMARY KEY,
                                       name TEXT,
                                       clinic_id UUID,
                                       type TEXT,
                                       specialization TEXT,
                                       is_active BOOLEAN
);

-- =========================
-- CLINIC
-- =========================
CREATE TABLE IF NOT EXISTS clinics (
                                       clinic_id UUID PRIMARY KEY,
                                       name TEXT,
                                       latitude DOUBLE,
                                       longitude DOUBLE,
                                       geohash TEXT,
                                       location_text TEXT,
                                       is_active BOOLEAN
);

-- =========================
-- DOCTOR LOCATION (SEARCH)
-- =========================
CREATE TABLE IF NOT EXISTS doctors_by_location (
                                                   geohash_prefix TEXT,
                                                   doctor_id UUID,
                                                   latitude DOUBLE,
                                                   longitude DOUBLE,
                                                   location_text TEXT,
                                                   PRIMARY KEY ((geohash_prefix), doctor_id)
);

-- =========================
-- APPOINTMENTS (MAIN)
-- =========================
CREATE TABLE IF NOT EXISTS appointments_by_doctor (
                                                      doctor_id UUID,
                                                      appointment_date DATE,
                                                      scheduled_time TIMESTAMP,
                                                      appointment_id UUID,
                                                      patient_id UUID,
                                                      status TEXT,
                                                      created_at TIMESTAMP,
                                                      updated_at TIMESTAMP,
                                                      PRIMARY KEY ((doctor_id, appointment_date), scheduled_time, appointment_id)
) WITH CLUSTERING ORDER BY (scheduled_time ASC);

-- =========================
-- APPOINTMENTS BY STATUS
-- =========================
CREATE TABLE IF NOT EXISTS appointments_by_doctor_status (
                                                             doctor_id UUID,
                                                             status TEXT,
                                                             appointment_date DATE,
                                                             scheduled_time TIMESTAMP,
                                                             appointment_id UUID,
                                                             patient_id UUID,
                                                             PRIMARY KEY ((doctor_id, status, appointment_date), scheduled_time, appointment_id)
) WITH CLUSTERING ORDER BY (scheduled_time ASC);

-- =========================
-- APPOINTMENT COUNT (LIMIT)
-- =========================
CREATE TABLE IF NOT EXISTS appointment_count_by_doctor_date (
                                                                doctor_id UUID,
                                                                appointment_date DATE,
                                                                count INT,
                                                                PRIMARY KEY ((doctor_id, appointment_date))
);