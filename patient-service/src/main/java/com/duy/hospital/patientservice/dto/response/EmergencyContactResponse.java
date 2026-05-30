package com.duy.hospital.patientservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmergencyContactResponse {

    private UUID contactId;
    private String fullName;
    private String relationship;
    private String phone;
    private String address;
}

