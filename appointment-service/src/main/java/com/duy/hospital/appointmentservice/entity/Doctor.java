package com.duy.hospital.appointmentservice.entity;

import com.duy.hospital.appointmentservice.entity.enums.DoctorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "user_id", unique = true)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "specialization", length = 150)
    private String specialization;

    @Column(name = "license_number", unique = true, length = 100)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private DoctorStatus status;

    @OneToMany(mappedBy = "doctor")
    @Builder.Default
    private List<DoctorSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();
}
