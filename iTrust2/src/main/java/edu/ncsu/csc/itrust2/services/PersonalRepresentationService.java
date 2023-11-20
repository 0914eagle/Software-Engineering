package edu.ncsu.csc.itrust2.services;

import java.util.List;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PersonalRepresentationService {
    private final PatientService patientService;
    private final UserService userService;
    private final LoggerUtil loggerUtil;
    private final PersonalRepresentationRepository personalRepresentationRepository;
    public List<PersonalRepresentation> listByPatient(String patientName) {
        final Patient patient = (Patient) patientService.findByName(patientName);
        return personalRepresentationRepository.findAllByPatient(patient);
    }
    public List<PersonalRepresentation> listByRepresenting(String patientName) {
        final Patient patient = (Patient) patientService.findByName(patientName);
        return personalRepresentationRepository.findALLByPersonalRepresentative(patient);
    }
}
