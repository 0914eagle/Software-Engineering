package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.Drug;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.PrescriptionRepository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PrescriptionServiceTest {
    @Mock private PrescriptionRepository prescriptionRepository;
    @Mock private DrugService drugService;
    @Mock private UserService userService;
    @InjectMocks private PrescriptionService prescriptionService;

    @Test
    public void testFindByPatient() {
        final User patient = new Patient();

        List<Prescription> expected = List.of();

        given(prescriptionRepository.findByPatient(patient)).willReturn(expected);

        var actual = prescriptionService.findByPatient(patient);

        assertEquals(expected, actual);
    }

    @Test
    public void testBuild() {
        final var drugCode = "drug";
        final var drug = new Drug();
        drug.setCode(drugCode);

        final var patientName = "patient";
        final var patient = new Patient();
        patient.setUsername(patientName);

        final var dosage = 35;
        final var renewals = 1;
        final var startDateString = "2021-01-01";
        final var endDateString = "2021-02-01";

        final var form = new PrescriptionForm();
        form.setDrug(drugCode);
        form.setDosage(dosage);
        form.setRenewals(renewals);
        form.setPatient(patientName);
        form.setStartDate(startDateString);
        form.setEndDate(endDateString);

        given(drugService.findByCode(drugCode)).willReturn(drug);
        given(userService.findByName(patientName)).willReturn(patient);

        final var expected = new Prescription();
        expected.setDrug(drug);
        expected.setDosage(dosage);
        expected.setRenewals(renewals);
        expected.setPatient(patient);
        expected.setStartDate(java.time.LocalDate.parse(startDateString));
        expected.setEndDate(java.time.LocalDate.parse(endDateString));

        var actual = prescriptionService.build(form);

        assertEquals(expected.getDrug(), actual.getDrug());
        assertEquals(expected.getDosage(), actual.getDosage());
        assertEquals(expected.getRenewals(), actual.getRenewals());
        assertEquals(expected.getPatient(), actual.getPatient());
        assertEquals(expected.getStartDate(), actual.getStartDate());
        assertEquals(expected.getEndDate(), actual.getEndDate());
    }
}
