package com.duy.hospital.appointmentservice.mapper;

import com.duy.hospital.appointmentservice.dto.request.DepartmentRequest;
import com.duy.hospital.appointmentservice.dto.response.DepartmentResponse;
import com.duy.hospital.appointmentservice.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DepartmentMapper {
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "doctors", ignore = true)
    Department toEntity(DepartmentRequest departmentRequest);

    DepartmentResponse toResponse(Department department);
}
