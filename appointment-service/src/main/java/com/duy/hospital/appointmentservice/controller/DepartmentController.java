package com.duy.hospital.appointmentservice.controller;

import com.duy.hospital.appointmentservice.dto.request.DepartmentRequest;
import com.duy.hospital.appointmentservice.dto.response.ApiResponse;
import com.duy.hospital.appointmentservice.dto.response.DepartmentResponse;
import com.duy.hospital.appointmentservice.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private  final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse departmentResponse = departmentService.createDepartment(request);
        return ResponseEntity.ok(ApiResponse.created(departmentResponse));
    }

}
