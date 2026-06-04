package com.duy.hospital.appointmentservice.controller;

import com.duy.hospital.appointmentservice.dto.request.DepartmentRequest;
import com.duy.hospital.appointmentservice.dto.request.UpdateDepartmentRequest;
import com.duy.hospital.appointmentservice.dto.response.ApiResponse;
import com.duy.hospital.appointmentservice.dto.response.DepartmentResponse;
import com.duy.hospital.appointmentservice.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private  final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse departmentResponse = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(departmentResponse));
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable UUID departmentId,
            @Valid @RequestBody UpdateDepartmentRequest request
    ) {
        DepartmentResponse departmentResponse = departmentService.updateDepartment(departmentId, request);
        return ResponseEntity.ok(ApiResponse.success(departmentResponse));
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(ApiResponse.success(departmentService.getDepartmentById(departmentId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        return ResponseEntity.ok(ApiResponse.success(departmentService.getAllDepartments()));
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

}
