package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Patient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Size;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByFirstNameContainsOrLastNameContainsOrUsernameContains(@Size(min=1, max=20) String firstName, @Size(min=1, max=30) String lastName, @Size(min=6, max=20) String username);
}
