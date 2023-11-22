package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.records.EmergencyPatientInfo;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EmergencyPatientServiceTest {

    @Mock private DiagnosisRepository diagnosisRepository;
    @Mock private OfficeVisitRepository officeVisitRepository;
    @Mock private PatientService patientService;

    @Mock private LoggerUtil loggerUtil;

    @InjectMocks private EmergencyPatientService emergencyPatientService;

    @Test
    public void testGetPatientInformationAsDoctor() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, Month.JANUARY, 1));
        patient.setGender(Gender.parse("Male"));
        patient.setBloodType(BloodType.parse("A+"));
        patient.setRoles(Set.of(Role.ROLE_PATIENT));

        String currentUserName = "TestUser";
        final Personnel currentUser = new Personnel();
        currentUser.setUsername(currentUserName);
        currentUser.setRoles(Set.of(Role.ROLE_HCP));

        given(patientService.findByName(eq(patientName))).willReturn(patient);
        given(patientService.findByName(eq(currentUserName))).willReturn(currentUser);
        given(loggerUtil.getCurrentUsername()).willReturn(currentUserName);

        EmergencyPatientInfo result = emergencyPatientService.getPatientInformation(patientName);

        assertEquals(patient.getUsername(), result.username());
        assertEquals(patient.getFirstName(), result.firstName());
        assertEquals(patient.getPreferredName(), result.preferredName());
        assertEquals(patient.getLastName(), result.lastName());
        assertEquals(patient.getDateOfBirth(), result.dateOfBirth());
        assertEquals(patient.getGender(), result.gender());
        assertEquals(patient.getBloodType(), result.bloodType());
    }

    @Test
    public void testGetPatientInformationAsER() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, Month.JANUARY, 1));
        patient.setGender(Gender.parse("Male"));
        patient.setBloodType(BloodType.parse("A+"));
        patient.setRoles(Set.of(Role.ROLE_PATIENT));

        String currentUserName = "TestUser";
        final Personnel currentUser = new Personnel();
        currentUser.setUsername(currentUserName);
        currentUser.setRoles(Set.of(Role.ROLE_ER));

        given(patientService.findByName(eq(patientName))).willReturn(patient);
        given(patientService.findByName(eq(currentUserName))).willReturn(currentUser);
        given(loggerUtil.getCurrentUsername()).willReturn(currentUserName);

        EmergencyPatientInfo result = emergencyPatientService.getPatientInformation(patientName);

        assertEquals(patient.getUsername(), result.username());
        assertEquals(patient.getFirstName(), result.firstName());
        assertEquals(patient.getPreferredName(), result.preferredName());
        assertEquals(patient.getLastName(), result.lastName());
        assertEquals(patient.getDateOfBirth(), result.dateOfBirth());
        assertEquals(patient.getGender(), result.gender());
        assertEquals(patient.getBloodType(), result.bloodType());
    }

    private void mockGetRecentOfficeVisits(String givenPatientName, List<OfficeVisit> expected) {
        final var patient = new Patient();
        given(patientService.findByName(givenPatientName)).willReturn(patient);
        given(
                        officeVisitRepository.findByDateBetweenAndPatientOrderByDateDesc(
                                any(), any(), eq(patient)))
                .willReturn(expected);
    }

    @Test
    public void testGetRecentOfficeVisits() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        List<OfficeVisit> expectedOfficeVisits = new ArrayList<>();
        OfficeVisit officeVisit1 = new OfficeVisit();
        officeVisit1.setPatient(patient);
        officeVisit1.setDate(ZonedDateTime.now());
        officeVisit1.setType(AppointmentType.GENERAL_CHECKUP);
        officeVisit1.setNotes("Regular checkup");
        expectedOfficeVisits.add(officeVisit1);

        given(patientService.findByName(eq(patientName))).willReturn(patient);
        given(
                        officeVisitRepository.findByDateBetweenAndPatientOrderByDateDesc(
                                any(), any(), eq(patient)))
                .willReturn(expectedOfficeVisits);

        List<OfficeVisit> result = emergencyPatientService.getRecentOfficeVisits(patientName, 7);

        assertEquals(expectedOfficeVisits, result);
    }

    @Test
    public void testGetRecentDiagnoses() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        List<OfficeVisit> expectedOfficeVisits = new ArrayList<>();
        OfficeVisit officeVisit1 = new OfficeVisit();
        officeVisit1.setPatient(patient);
        officeVisit1.setDate(ZonedDateTime.now());
        officeVisit1.setType(AppointmentType.GENERAL_CHECKUP);
        officeVisit1.setNotes("Regular checkup");
        expectedOfficeVisits.add(officeVisit1);

        List<Diagnosis> expectedDiagnosis = new ArrayList<>();
        Diagnosis diagnosis1 = new Diagnosis();
        diagnosis1.setVisit(officeVisit1);
        expectedDiagnosis.add(diagnosis1);

        Diagnosis diagnosis2 = new Diagnosis();
        diagnosis2.setVisit(officeVisit1);
        expectedDiagnosis.add(diagnosis2);

        mockGetRecentOfficeVisits(patientName, expectedOfficeVisits);
        given(diagnosisRepository.findByVisit(eq(officeVisit1))).willReturn(expectedDiagnosis);

        List<Diagnosis> result = emergencyPatientService.getRecentDiagnoses(patientName);

        assertEquals(expectedDiagnosis, result);
    }

    @Test
    public void testGetRecentPrescriptions() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        List<Prescription> expectedPrescriptions = new ArrayList<>();
        Prescription prescription1 = new Prescription();
        expectedPrescriptions.add(prescription1);

        List<OfficeVisit> expectedOfficeVisits = new ArrayList<>();
        OfficeVisit officeVisit1 = new OfficeVisit();
        officeVisit1.setPatient(patient);
        officeVisit1.setDate(ZonedDateTime.now());
        officeVisit1.setType(AppointmentType.GENERAL_CHECKUP);
        officeVisit1.setNotes("Regular checkup");
        officeVisit1.setPrescriptions(expectedPrescriptions);
        expectedOfficeVisits.add(officeVisit1);

        mockGetRecentOfficeVisits(patientName, expectedOfficeVisits);

        List<Prescription> result = emergencyPatientService.getRecentPrescriptions(patientName);

        assertEquals(expectedPrescriptions, result);
    }
}
