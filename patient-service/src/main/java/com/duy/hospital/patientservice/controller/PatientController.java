package com.duy.hospital.patientservice.controller;

import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.request.PatientUpdateRequest;
import com.duy.hospital.patientservice.dto.response.*;
import com.duy.hospital.patientservice.security.AuthenticatedUser;
import com.duy.hospital.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<PageResponse<PatientResponse>>> getPatients(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        PageResponse<PatientResponse> patients = patientService.getPatients(search, pageable);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, patients));
    }

    @GetMapping("/summaries")
    public ResponseEntity<ApiResponse<PageResponse<PatientSummaryResponse>>> getPatientSummaries(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        PageResponse<PatientSummaryResponse> summaries = patientService.getPatientSummaries(search, pageable);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, summaries));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PatientResponse>> getMyPatient(
            @AuthenticationPrincipal AuthenticatedUser user) {
        PatientResponse patient = patientService.getMyPatient(user);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, patient));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<PatientResponse>> updateMyPatient(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody PatientUpdateRequest request) {
        PatientResponse updated = patientService.updateMyPatient(user, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, updated));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientById(
            @PathVariable UUID patientId) {
        PatientResponse patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, patient));
    }

    @PatchMapping("/{patientId}")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody PatientUpdateRequest request) {
        PatientResponse updated = patientService.updatePatient(patientId, request);
        return ResponseEntity.ok(ApiResponse.of(ResponseCode.SUCCESS, updated));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable UUID patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }
}
