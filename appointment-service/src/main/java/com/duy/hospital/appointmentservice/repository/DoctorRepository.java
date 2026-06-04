package com.duy.hospital.appointmentservice.repository;

import com.duy.hospital.appointmentservice.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
}
