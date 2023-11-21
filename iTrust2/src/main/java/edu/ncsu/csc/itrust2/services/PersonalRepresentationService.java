package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalRepresentationService {
    private final PatientService patientService;
    private final PersonalRepresentationRepository personalRepresentationRepository;

    public List<PersonalRepresentation> listByPatient(String patientName) {
        final Patient patient = (Patient) patientService.findByName(patientName);
        return personalRepresentationRepository.findAllByPatient(patient);
    }

    public List<PersonalRepresentation> listByRepresenting(String patientName) {
        final Patient patient = (Patient) patientService.findByName(patientName);
        return personalRepresentationRepository.findAllByPersonalRepresentative(patient);
    }
}
