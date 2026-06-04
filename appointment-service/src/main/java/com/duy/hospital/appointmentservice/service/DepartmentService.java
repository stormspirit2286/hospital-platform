package com.duy.hospital.appointmentservice.service;

import com.duy.hospital.appointmentservice.dto.request.DepartmentRequest;
import com.duy.hospital.appointmentservice.dto.request.UpdateDepartmentRequest;
import com.duy.hospital.appointmentservice.dto.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(UUID departmentId, UpdateDepartmentRequest request);
    void deleteDepartment(UUID departmentId);
    DepartmentResponse getDepartmentById(UUID departmentId);
    List<DepartmentResponse> getAllDepartments();
}
