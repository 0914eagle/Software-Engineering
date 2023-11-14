package edu.ncsu.csc.itrust2.records;

import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;

import java.time.LocalDate;

public record EmergencyPatientInfo(
        String username,
        String firstName,
        String preferredName,
        String lastName,
        LocalDate dateOfBirth,
        Gender gender,
        BloodType bloodType) {}
