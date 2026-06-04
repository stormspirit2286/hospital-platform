package com.duy.hospital.appointmentservice.repository;

import com.duy.hospital.appointmentservice.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DockerScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {
}
