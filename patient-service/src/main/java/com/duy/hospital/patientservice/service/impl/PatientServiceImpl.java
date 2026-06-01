package com.duy.hospital.patientservice.service.impl;

import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.request.PatientUpdateRequest;
import com.duy.hospital.patientservice.dto.response.PageResponse;
import com.duy.hospital.patientservice.dto.response.PatientResponse;
import com.duy.hospital.patientservice.dto.response.PatientSummaryResponse;
import com.duy.hospital.patientservice.dto.response.ResponseCode;
import com.duy.hospital.patientservice.entity.EmergencyContact;
import com.duy.hospital.patientservice.entity.Patient;
import com.duy.hospital.patientservice.entity.PatientInsurance;
import com.duy.hospital.patientservice.entity.enums.InsuranceStatus;
import com.duy.hospital.patientservice.exception.AppException;
import com.duy.hospital.patientservice.mapper.PatientMapper;
import com.duy.hospital.patientservice.repository.PatientRepository;
import com.duy.hospital.patientservice.security.AuthenticatedUser;
import com.duy.hospital.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest patientRequest) {
        log.info("creating new Patient - {} {}", patientRequest.getFirstName(), patientRequest.getLastName());
        if (patientRequest.getUserId() != null && patientRepository.existsByUserId(patientRequest.getUserId())) {
            throw new AppException(ResponseCode.PATIENT_ALREADY_EXISTS);
        }
        Patient patient = patientMapper.toEntity(patientRequest);
        if (patientRequest.getInsurance() != null) {
            PatientInsurance patientInsurance = patientMapper.toEntity(patientRequest.getInsurance());
            patientInsurance.setPatient(patient);
            patientInsurance.setStatus(InsuranceStatus.ACTIVE);
            patient.setInsurance(patientInsurance);
        }

        if (patientRequest.getEmergencyContacts() != null) {
            List<EmergencyContact> emergencyContacts = patientRequest.getEmergencyContacts().stream()
                    .map(contact -> {
                        EmergencyContact emergencyContact = patientMapper.toEntity(contact);
                        emergencyContact.setPatient(patient);
                        return emergencyContact;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            patient.setEmergencyContacts(emergencyContacts);
        }
        Patient patientCreated = patientRepository.save(patient);
        log.info("Created Patient - {}", patientCreated.getPatientId());
        return patientMapper.toResponse(patientCreated);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PatientResponse> getPatients(String search, Pageable pageable) {
        Page<Patient> patients = patientRepository.searchAllWithInsurance(normalizeSearch(search), pageable);

        List<UUID> ids = patients.getContent().stream()
                .map(Patient::getPatientId)
                .toList();

        if (!ids.isEmpty()) {
            patientRepository.findAllWithEmergencyContacts(ids);
        }

        return PageResponse.<PatientResponse>builder()
                .content(patients.getContent().stream()
                        .map(patientMapper::toResponse)
                        .collect(Collectors.toList()))
                .page(patients.getNumber())
                .size(patients.getSize())
                .totalElements(patients.getTotalElements())
                .totalPages(patients.getTotalPages())
                .first(patients.isFirst())
                .last(patients.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PatientSummaryResponse> getPatientSummaries(String search, Pageable pageable) {
        Page<PatientSummaryResponse> page = patientRepository.searchSummaries(normalizeSearch(search), pageable);
        return PageResponse.from(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(UUID patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new AppException(ResponseCode.PATIENT_NOT_FOUND));
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(UUID patientId, PatientUpdateRequest request) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new AppException(ResponseCode.PATIENT_NOT_FOUND));
        patientMapper.updateEntity(request, patient);
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getMyPatient(AuthenticatedUser user) {
        Patient patient = patientRepository.findByUserId(user.userId())
                .orElseThrow(() -> new AppException(ResponseCode.PATIENT_NOT_FOUND));
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse updateMyPatient(AuthenticatedUser user, PatientUpdateRequest request) {
        Patient patient = patientRepository.findByUserId(user.userId())
                .orElseThrow(() -> new AppException(ResponseCode.PATIENT_NOT_FOUND));
        updatePatientOwnedFields(request, patient);
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional
    public void deletePatient(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new AppException(ResponseCode.PATIENT_NOT_FOUND);
        }
        patientRepository.deleteById(patientId);
    }

    private String normalizeSearch(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }
        return search.trim();
    }

    private void updatePatientOwnedFields(PatientUpdateRequest request, Patient patient) {
        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            patient.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            patient.setCity(request.getCity());
        }
    }
}
