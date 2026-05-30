package com.duy.hospital.patientservice.mapper;

import com.duy.hospital.patientservice.dto.request.EmergencyContactRequest;
import com.duy.hospital.patientservice.dto.request.InsuranceRequest;
import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.response.EmergencyContactResponse;
import com.duy.hospital.patientservice.dto.response.InsuranceResponse;
import com.duy.hospital.patientservice.dto.response.PatientResponse;
import com.duy.hospital.patientservice.entity.EmergencyContact;
import com.duy.hospital.patientservice.entity.Patient;
import com.duy.hospital.patientservice.entity.PatientInsurance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PatientMapper {

    PatientResponse toResponse(Patient patient);

    InsuranceResponse toResponse(PatientInsurance insurance);

    EmergencyContactResponse toResponse(EmergencyContact contact);


    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "insurance", ignore = true)
    @Mapping(target = "emergencyContacts", ignore = true)
    Patient toEntity(PatientRequest request);

    @Mapping(target = "insuranceId", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", ignore = true)
    PatientInsurance toEntity(InsuranceRequest request);

    @Mapping(target = "contactId", ignore = true)
    @Mapping(target = "patient", ignore = true)
    EmergencyContact toEntity(EmergencyContactRequest request);
}
