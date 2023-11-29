package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PersonalRepresentationServiceTest {
    @Mock private PersonalRepresentationRepository PersonalRepresentationRepository;

    @Mock private PatientService patientService;
    @InjectMocks private PersonalRepresentationService personalRepresentationService;
    @Mock private PatientRepository patientRepository;

    @Test
    public void testListByPatient() {
        final String patient1 = "patient1";
        final UserForm patient1Form = new UserForm(patient1, "123456", Role.ROLE_PATIENT, 1);
        final Patient patient1User = new Patient(patient1Form);

        List<PersonalRepresentation> personalRepresentations =
                new ArrayList<PersonalRepresentation>();
        given(patientService.findByName(any(String.class))).willReturn(patient1User);
        given(PersonalRepresentationRepository.findAllByPatient(patient1User))
                .willReturn(personalRepresentations);

        final List<PersonalRepresentation> result =
                personalRepresentationService.listByPatient("patient1");

        assertEquals(result, personalRepresentations);
    }

    @Test
    public void testListByRepresenting() {
        final String patient1 = "patient1";
        final UserForm patient1Form = new UserForm(patient1, "123456", Role.ROLE_PATIENT, 1);
        final Patient patient1User = new Patient(patient1Form);

        List<PersonalRepresentation> personalRepresentations =
                new ArrayList<PersonalRepresentation>();
        given(patientService.findByName(any(String.class))).willReturn(patient1User);
        given(PersonalRepresentationRepository.findAllByPersonalRepresentative(patient1User))
                .willReturn(personalRepresentations);

        final List<PersonalRepresentation> result =
                personalRepresentationService.listByRepresenting("patient1");

        assertEquals(result, personalRepresentations);
    }

    @Test
    public void testCancelPersonalRepresentation() {
        final String patient = "patient";
        final String representative = "representative";
        final UserForm patientUserForm = new UserForm(patient, "12345", Role.ROLE_PATIENT, 1);
        final UserForm representativeUserForm =
                new UserForm(representative, "67890", Role.ROLE_PATIENT, 1);
        final Patient patientUser = new Patient(patientUserForm);
        final Patient representativeUser = new Patient(representativeUserForm);

        final PersonalRepresentation personalRepresentation =
                new PersonalRepresentation(patientUser, representativeUser);

        given(patientRepository.findByUsername(patient)).willReturn(patientUser);
        given(patientRepository.findByUsername(representative)).willReturn(representativeUser);

        given(
                        PersonalRepresentationRepository.findByPatientAndPersonalRepresentative(
                                patientUser, representativeUser))
                .willReturn(personalRepresentation);

        personalRepresentationService.cancelPersonalRepresentation(patient, representative);

        verify(PersonalRepresentationRepository).delete(personalRepresentation);
    }
}
