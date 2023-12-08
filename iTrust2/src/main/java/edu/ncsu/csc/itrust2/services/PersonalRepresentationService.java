package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PersonalRepresentationService {

    private final PersonalRepresentationRepository personalRepresentationRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;

    @Transactional
    public PersonalRepresentation setPersonalRepresentation(
            String patientName, String representativeName) {
        if (patientName.equals(representativeName))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "환자와 대리인이 같을 수 없습니다.");

        Patient patient = patientRepository.findByUsername(patientName);
        Patient representative = patientRepository.findByUsername(representativeName);

        PersonalRepresentation personalRepresentation =
                new PersonalRepresentation(patient, representative);

        return personalRepresentationRepository.save(personalRepresentation);
    }

    @Transactional
    public void cancelPersonalRepresentation(String patientName, String representativeName) {
        Patient patient = patientRepository.findByUsername(patientName);
        Patient representative = patientRepository.findByUsername(representativeName);

        PersonalRepresentation personalRepresentation =
                personalRepresentationRepository.findByPatientAndPersonalRepresentative(
                        patient, representative);

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

    public boolean isRepresentative(String currentUsername, String patientName) {
        final Patient currentUser = (Patient) patientService.findByName(currentUsername);
        final Patient patient = (Patient) patientService.findByName(patientName);
        return personalRepresentationRepository.existsByPatientAndPersonalRepresentative(
                patient, currentUser);
    }
}
