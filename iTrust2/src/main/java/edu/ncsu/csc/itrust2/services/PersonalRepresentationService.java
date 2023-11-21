package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalRepresentationService {

    private final PersonalRepresentationRepository personalRepresentationRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;

    @Transactional
    public void cancelPersonalRepresentation(String patientName, String representativeName) {
        Patient patient = patientRepository.findByUsername(patientName);
        Patient representative = patientRepository.findByUsername(representativeName);

        PersonalRepresentation personalRepresentation
                = personalRepresentationRepository
                .findByPatientAndPersonalRepresentative(patient, representative);

        personalRepresentationRepository.delete(personalRepresentation);
    }

    public List<PersonalRepresentation> listByPatient(String patientName) {
        final Patient patient = (Patient) patientService.findByName(patientName);
        return personalRepresentationRepository.findAllByPatient(patient);
    }

    public List<PersonalRepresentation> listByRepresenting(String patientName) {
        final Patient patient = (Patient) patientService.findByName(patientName);
        return personalRepresentationRepository.findAllByPersonalRepresentative(patient);

    }
}