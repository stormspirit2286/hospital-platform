package com.duy.hospital.patientservice.exception;

import com.duy.hospital.patientservice.dto.response.ResponseCode;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ResponseCode responseCode;

    public AppException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public AppException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public AppException(ResponseCode responseCode, String message, Throwable cause) {
        super(message, cause);
        this.responseCode = responseCode;
    }
}

