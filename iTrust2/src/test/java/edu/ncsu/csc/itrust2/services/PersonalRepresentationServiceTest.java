package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PersonalRepresentationServiceTest {
    @Mock private PersonalRepresentationRepository PersonalRepresentationRepository;

    @Mock private PatientService patientService;
    @InjectMocks private PersonalRepresentationService personalRepresentationService;

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
}
