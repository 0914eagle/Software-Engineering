package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientSearchService {

    private final PatientRepository patientRepository;

    public List<Patient> listByPatientName(String nameQuery) {
        return patientRepository.findAllByFirstNameContainsOrLastNameContainsOrUsernameContains(
                nameQuery, nameQuery, nameQuery);
    }
}
