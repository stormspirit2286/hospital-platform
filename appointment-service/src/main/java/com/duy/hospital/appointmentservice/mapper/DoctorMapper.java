package com.duy.hospital.appointmentservice.mapper;

import com.duy.hospital.appointmentservice.dto.request.DoctorRequest;
import com.duy.hospital.appointmentservice.dto.response.DoctorResponse;
import com.duy.hospital.appointmentservice.dto.response.DoctorSummaryResponse;
import com.duy.hospital.appointmentservice.entity.Doctor;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DoctorMapper {

    @Mapping(target = "departmentId", source = "department.departmentId")
    @Mapping(target = "departmentName", source = "department.name")
    DoctorResponse toDoctorResponse(Doctor doctor);

    @Mapping(target = "departmentId", source = "department.departmentId")
    @Mapping(target = "departmentName", source = "department.name")
    DoctorSummaryResponse toDoctorSummaryResponse(Doctor doctor);

    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "schedules", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "department", ignore = true)
    Doctor toEntity(DoctorRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    void updateEntity(@MappingTarget Doctor doctor, DoctorRequest request);
}
