package com.duy.hospital.appointmentservice.repository;

import com.duy.hospital.appointmentservice.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    @Query("SELECT d FROM Doctor d JOIN FETCH d.department WHERE d.department.departmentId = :departmentId")
    List<Doctor> findByDepartmentId(@Param("departmentId") UUID departmentId);
}
