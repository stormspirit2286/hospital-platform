package com.duy.hospital.patientservice.repository;

import com.duy.hospital.patientservice.dto.response.PatientSummaryResponse;
import com.duy.hospital.patientservice.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByUserId(UUID userId);

    @Query(
            "SELECT p FROM Patient p " +
            "LEFT JOIN FETCH p.insurance " +
            "LEFT JOIN FETCH p.emergencyContacts " +
            "WHERE p.patientId = :id"
    )
    Optional<Patient> findByPatientId(@Param("id") UUID id);

    @Query(
            value = """
                    SELECT p FROM Patient p
                    LEFT JOIN FETCH p.insurance
                    WHERE :search IS NULL
                       OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR p.phone LIKE CONCAT('%', :search, '%')
                       OR LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(p) FROM Patient p
                    WHERE :search IS NULL
                       OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                       OR p.phone LIKE CONCAT('%', :search, '%')
                       OR LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :search, '%'))
                    """
    )
    Page<Patient> searchAllWithInsurance(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.emergencyContacts WHERE p.patientId IN :ids")
    List<Patient> findAllWithEmergencyContacts(@Param("ids") List<UUID> ids);

    @Query("""
            SELECT new com.duy.hospital.patientservice.dto.response.PatientSummaryResponse(
                p.patientId, p.firstName, p.lastName, p.dateOfBirth,
                p.gender, p.phone, p.email, p.city, p.status
            )
            FROM Patient p
            WHERE :search IS NULL
               OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR p.phone LIKE CONCAT('%', :search, '%')
               OR LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<PatientSummaryResponse> searchSummaries(@Param("search") String search, Pageable pageable);

    @Query(
            "SELECT p FROM Patient p " +
            "LEFT JOIN FETCH p.insurance " +
            "LEFT JOIN FETCH p.emergencyContacts " +
            "WHERE p.userId = :userId"
    )
    Optional<Patient> findByUserId(@Param("userId") UUID userId);
}
