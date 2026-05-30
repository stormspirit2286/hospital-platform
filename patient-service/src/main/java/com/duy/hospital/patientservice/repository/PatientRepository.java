package com.duy.hospital.patientservice.repository;

import com.duy.hospital.patientservice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    @Query(
            "SELECT p FROM Patient p " +
            "LEFT JOIN FETCH p.insurance " +
            "LEFT JOIN FETCH p.emergencyContacts " +
            "WHERE p.patientId = :id"
    )
    Optional<Patient> findByPatientId(@Param("id") UUID id);
}
