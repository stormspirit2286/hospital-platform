package com.duy.hospital.appointmentservice.repository;

import com.duy.hospital.appointmentservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String departmentName);
    boolean existsByDepartmentId(UUID departmentId);
}
