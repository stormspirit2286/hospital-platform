package com.duy.hospital.appointmentservice.service.impl;

import com.duy.hospital.appointmentservice.dto.request.DepartmentRequest;
import com.duy.hospital.appointmentservice.dto.response.DepartmentResponse;
import com.duy.hospital.appointmentservice.dto.response.ResponseCode;
import com.duy.hospital.appointmentservice.entity.Department;
import com.duy.hospital.appointmentservice.exception.AppException;
import com.duy.hospital.appointmentservice.mapper.DepartmentMapper;
import com.duy.hospital.appointmentservice.repository.DepartmentRepository;
import com.duy.hospital.appointmentservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;
    private final DepartmentRepository departmentRepository;


    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new AppException(ResponseCode.DUPLICATE_DEPARTMENT_NAME);
        }
        Department department = departmentMapper.toEntity(request);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse updateDepartment(UUID departmentId, DepartmentRequest request) {

        return null;
    }

    @Override
    public void deleteDepartment(UUID departmentId) {

    }

    @Override
    public DepartmentResponse getDepartmentById(UUID departmentId) {
        return null;
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return List.of();
    }
}
