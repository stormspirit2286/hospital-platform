package com.duy.hospital.patientservice.service.impl;

import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.response.PatientResponse;
import com.duy.hospital.patientservice.entity.EmergencyContact;
import com.duy.hospital.patientservice.entity.Patient;
import com.duy.hospital.patientservice.entity.PatientInsurance;
import com.duy.hospital.patientservice.entity.enums.InsuranceStatus;
import com.duy.hospital.patientservice.mapper.PatientMapper;
import com.duy.hospital.patientservice.repository.PatientRepository;
import com.duy.hospital.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.info("creating new Patient - {}", patientRequest.getFirstName() + " " + patientRequest.getLastName());
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
    public List<PatientResponse> getPatients(Pageable pageable) {
        return List.of();
    }

    @Override
    public PatientResponse getPatientById(UUID patientId) {
        return null;
    }

    @Override
    public PatientResponse updatePatient(UUID patientId, PatientRequest patient) {
        return null;
    }

    @Override
    public void deletePatient(UUID patientId) {

    }
}
