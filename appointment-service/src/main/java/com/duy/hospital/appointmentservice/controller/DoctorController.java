package com.duy.hospital.appointmentservice.controller;

import com.duy.hospital.appointmentservice.dto.request.DoctorRequest;
import com.duy.hospital.appointmentservice.dto.response.ApiResponse;
import com.duy.hospital.appointmentservice.dto.response.DoctorResponse;
import com.duy.hospital.appointmentservice.dto.response.DoctorSummaryResponse;
import com.duy.hospital.appointmentservice.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@Valid @RequestBody DoctorRequest doctorRequest) {
        DoctorResponse doctor = doctorService.createDoctor(doctorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(doctor));
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable UUID doctorId,
            @Valid @RequestBody DoctorRequest doctorRequest) {
        DoctorResponse doctor = doctorService.updateDoctor(doctorId, doctorRequest);
        return ResponseEntity.ok(ApiResponse.success(doctor));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getDoctorById(doctorId)));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<DoctorSummaryResponse>>> getDoctorsByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getDoctorsByDepartment(departmentId)));
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable UUID doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
