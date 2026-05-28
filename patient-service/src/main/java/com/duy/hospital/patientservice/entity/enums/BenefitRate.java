package com.duy.hospital.patientservice.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BenefitRate {

    RATE_80(80),
    RATE_95(95),
    RATE_100(100);

    private final int percent;

    public static BenefitRate fromPercent(int percent) {
        for (BenefitRate r : values()) {
            if (r.percent == percent) return r;
        }
        throw new IllegalArgumentException("Unsupported benefit rate: " + percent);
    }
}

