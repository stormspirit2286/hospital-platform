package com.duy.hospital.appointmentservice.service.impl;

import com.duy.hospital.appointmentservice.dto.request.DoctorRequest;
import com.duy.hospital.appointmentservice.dto.response.DoctorResponse;
import com.duy.hospital.appointmentservice.dto.response.DoctorSummaryResponse;
import com.duy.hospital.appointmentservice.dto.response.ResponseCode;
import com.duy.hospital.appointmentservice.entity.Department;
import com.duy.hospital.appointmentservice.entity.Doctor;
import com.duy.hospital.appointmentservice.exception.AppException;
import com.duy.hospital.appointmentservice.mapper.DoctorMapper;
import com.duy.hospital.appointmentservice.repository.DepartmentRepository;
import com.duy.hospital.appointmentservice.repository.DoctorRepository;
import com.duy.hospital.appointmentservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        log.info("Creating doctor: fullName={}, departmentId={}", request.getFullName(), request.getDepartmentId());
        Department department = departmentRepository
                .getDepartmentById(request.getDepartmentId())
                .orElseThrow(() -> new AppException(ResponseCode.DEPARTMENT_NOT_FOUND));
        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setDepartment(department);
        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor created: doctorId={}", saved.getDoctorId());
        return doctorMapper.toDoctorResponse(saved);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(UUID doctorId, DoctorRequest request) {
        log.info("Updating doctor: doctorId={}", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new AppException(ResponseCode.DOCTOR_NOT_FOUND));
        if (!request.getDepartmentId().equals(doctor.getDepartment().getDepartmentId())) {
            Department department = departmentRepository
                    .getDepartmentById(request.getDepartmentId())
                    .orElseThrow(() -> new AppException(ResponseCode.DEPARTMENT_NOT_FOUND));
            doctor.setDepartment(department);
        }
        doctorMapper.updateEntity(doctor, request);
        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor updated: doctorId={}", saved.getDoctorId());
        return doctorMapper.toDoctorResponse(saved);
    }

    @Override
    @Transactional
    public void deleteDoctor(UUID doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new AppException(ResponseCode.DOCTOR_NOT_FOUND));
        doctorRepository.delete(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(UUID doctorId) {
        return doctorRepository.findById(doctorId)
                .map(doctorMapper::toDoctorResponse)
                .orElseThrow(() -> new AppException(ResponseCode.DOCTOR_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorSummaryResponse> getDoctorsByDepartment(UUID departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new AppException(ResponseCode.DEPARTMENT_NOT_FOUND);
        }
        return doctorRepository.findByDepartmentId(departmentId).stream()
                .map(doctorMapper::toDoctorSummaryResponse)
                .toList();
    }
}
