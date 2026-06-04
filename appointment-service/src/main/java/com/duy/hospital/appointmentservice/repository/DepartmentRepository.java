package com.duy.hospital.appointmentservice.repository;

import com.duy.hospital.appointmentservice.dto.response.DepartmentResponse;
import com.duy.hospital.appointmentservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String departmentName);
    boolean existsByNameAndDepartmentIdNot(String name, UUID departmentId);
    Optional<Department> getDepartmentById(UUID departmentId);
}
