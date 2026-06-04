package com.duy.hospital.appointmentservice.service.impl;

import com.duy.hospital.appointmentservice.dto.request.DepartmentRequest;
import com.duy.hospital.appointmentservice.dto.request.UpdateDepartmentRequest;
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
    @Transactional
    public DepartmentResponse updateDepartment(UUID departmentId, UpdateDepartmentRequest request) {
        Department department = departmentRepository
                                .getDepartmentById(departmentId)
                                .orElseThrow(() -> new AppException(ResponseCode.DEPARTMENT_NOT_FOUND));
        if (departmentRepository.existsByNameAndDepartmentIdNot(request.getName(), departmentId)) {
            throw new AppException(ResponseCode.DUPLICATE_DEPARTMENT_NAME);
        }
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setStatus(request.getStatus());
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(ResponseCode.DEPARTMENT_NOT_FOUND));
        departmentRepository.delete(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .map(departmentMapper::toResponse)
                .orElseThrow(() -> new AppException(ResponseCode.DEPARTMENT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponse)
                .toList();
    }
}
