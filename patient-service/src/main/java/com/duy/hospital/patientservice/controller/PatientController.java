package com.duy.hospital.patientservice.controller;

import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.response.*;
import com.duy.hospital.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(
            @Valid @RequestBody PatientRequest request) {
        PatientResponse created = patientService.createPatient(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(ResponseCode.CREATED, created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PatientResponse>>> getPatients(Pageable pageable) {
        PageResponse<PatientResponse> patients = patientService.getPatients(pageable);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, patients));
    }

    @GetMapping("/summaries")
    public ResponseEntity<ApiResponse<PageResponse<PatientSummaryResponse>>> getPatientSummaries(Pageable pageable) {
        PageResponse<PatientSummaryResponse> summaries = patientService.getPatientSummaries(pageable);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, summaries));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable UUID patientId) {
        PatientResponse patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, patient));
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody PatientRequest request) {
        PatientResponse updated = patientService.updatePatient(patientId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, updated));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable UUID patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.NO_CONTENT, null));
    }
}
