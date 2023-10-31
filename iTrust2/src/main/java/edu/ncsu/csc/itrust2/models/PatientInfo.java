package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;

import java.time.LocalDate;

public class PatientInfo extends Patient {
    private final String firstName;
    private final String preferredName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final BloodType bloodType;

    public PatientInfo(
            String firstName,
            String preferredName,
            String lastName,
            LocalDate dateOfBirth,
            Gender gender,
            BloodType bloodType) {
        this.firstName = firstName;
        this.preferredName = preferredName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
    }

    @Override
    public String toString() {
        return "PatientInfo{"
                + "firstName='"
                + this.firstName
                + "'"
                + ", preferredName='"
                + this.preferredName
                + "'"
                + ", lastName='"
                + this.lastName
                + "'"
                + ", dateOfBirth='"
                + this.dateOfBirth
                + "'"
                + ", gender='"
                + this.gender
                + "'"
                + ", bloodType="
                + this.bloodType
                + "}";
    }
}
