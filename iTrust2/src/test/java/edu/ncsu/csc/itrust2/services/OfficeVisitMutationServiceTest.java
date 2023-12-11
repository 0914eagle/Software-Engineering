package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.DiagnosisForm;
import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.forms.UpdateOfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.UpdateOphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.ICDCode;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static edu.ncsu.csc.itrust2.services.OphthalmologyAppointmentRequestServiceTest.assertAppointmentRequestEquals;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OfficeVisitMutationServiceTest {
    @Mock private OfficeVisitRepository officeVisitRepository;

    @Mock private UserService userService;

    @Mock private AppointmentRequestService appointmentRequestService;

    @Mock private HospitalService hospitalService;

    @Mock private BasicHealthMetricsService bhmService;

    @Mock private PrescriptionService prescriptionService;

    @Mock private DiagnosisService diagnosisService;

    @Mock private OphthalmologySurgeryService ophthalmologySurgeryService;
    @InjectMocks private OfficeVisitMutationService officeVisitMutationService;

    private void mockAssertNotExistsById(String idString) {
        Long id = null;
        if (idString != null) id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(false);
    }

    private void mockAssertExistsById(String idString) {
        Long id = null;
        if (idString != null) id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(true);
    }

    private void mockGetAppointmentRequest(
            final OfficeVisit ov, AppointmentRequest appointmentRequest) {
        given(
                        appointmentRequestService.findByHcpAndPatientAndDate(
                                eq(ov.getHcp()), eq(ov.getPatient()), eq(ov.getDate())))
                .willReturn(Optional.of(appointmentRequest));
    }

    private void mockGetBasicHealthMetrics(
            final OfficeVisitForm ovf, BasicHealthMetrics basicHealthMetrics) {
        given(bhmService.build(eq(ovf))).willReturn(basicHealthMetrics);
    }

    private void mockGetDiagnoses(
            final List<DiagnosisForm> diagnosisForms, List<Diagnosis> diagnoses) {
        given(diagnosisService.build(eq(diagnosisForms.get(0)))).willReturn(diagnoses.get(0));
    }

    private void mockGetPrescriptions(
            final List<PrescriptionForm> prescriptionForms, List<Prescription> prescriptions) {
        given(prescriptionService.build(eq(prescriptionForms.get(0))))
                .willReturn(prescriptions.get(0));
    }

    private void mockGetOphthalmologySurgery(
            final OphthalmologySurgeryForm ophthalmologySurgeryForm,
            OphthalmologySurgery ophthalmologySurgery) {
        given(ophthalmologySurgeryService.create(any())).willReturn(ophthalmologySurgery);
    }

    @Test
    public void testCreate() {
        String hospitalName = "TestHospital";
        String address = "TestAddress";
        String zip = "TestZip";
        String state = "TestState";
        String testStr = "test";
        final Hospital hospital = new Hospital(hospitalName, address, zip, state);

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        final ICDCode code = new ICDCode("T10", "Test 10");
        DiagnosisForm diagnosisForm1 = new DiagnosisForm();
        diagnosisForm1.setNote(testStr);
        List<DiagnosisForm> diagnosisForms = Arrays.asList(diagnosisForm1);
        Diagnosis diagnosis1 = new Diagnosis();
        diagnosis1.setNote(testStr);
        diagnosis1.setCode(code);
        List<Diagnosis> diagnoses = Arrays.asList(diagnosis1);

        PrescriptionForm prescriptionForm1 = new PrescriptionForm();
        List<PrescriptionForm> prescriptionForms = Arrays.asList(prescriptionForm1);
        Prescription prescription1 = new Prescription();
        List<Prescription> prescriptions = Arrays.asList(prescription1);

        OfficeVisitForm ovf = new OfficeVisitForm();
        ovf.setId("124516123");
        ovf.setPatient(patientName);
        ovf.setHcp(hcpName);
        ovf.setNotes(testStr);
        ovf.setDate(String.valueOf(date));
        ovf.setPreScheduled("true");
        ovf.setHospital(hospitalName);
        ovf.setDiagnoses(diagnosisForms);
        ovf.setPrescriptions(prescriptionForms);
        ovf.setType("GENERAL_CHECKUP");

        OfficeVisit ov = new OfficeVisit();
        ov.setPatient(patient);
        ov.setHcp(hcp);
        ov.setDate(date);
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        BasicHealthMetrics basicHealthMetrics = new BasicHealthMetrics();

        mockAssertNotExistsById(ovf.getId());
        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(hcpName))).willReturn(hcp);
        mockGetAppointmentRequest(ov, appointmentRequest);
        given(hospitalService.findByName(eq(hospitalName))).willReturn(hospital);
        mockGetBasicHealthMetrics(ovf, basicHealthMetrics);
        mockGetDiagnoses(diagnosisForms, diagnoses);
        mockGetPrescriptions(prescriptionForms, prescriptions);

        final var id = 124_516_123L;

        given(officeVisitRepository.save(any()))
                .will(
                        invocation -> {
                            OfficeVisit officeVisit = invocation.getArgument(0);
                            officeVisit.setId(id);
                            return officeVisit;
                        });

        OfficeVisit createdOfficeVisit = officeVisitMutationService.create(ovf);

        assertNotNull(createdOfficeVisit);
        assertEquals(id, (long) createdOfficeVisit.getId());
        assertEquals(patient, createdOfficeVisit.getPatient());
        assertEquals(hcp, createdOfficeVisit.getHcp());
        assertEquals(testStr, createdOfficeVisit.getNotes());
        assertEquals(date, createdOfficeVisit.getDate());
        assertAppointmentRequestEquals(appointmentRequest, createdOfficeVisit.getAppointment());
        assertEquals(hospital, createdOfficeVisit.getHospital());
        assertThat(basicHealthMetrics.equals(createdOfficeVisit.getBasicHealthMetrics())).isTrue();
        assertEquals(diagnosis1, createdOfficeVisit.getDiagnoses().get(0));
        assertEquals(prescription1, createdOfficeVisit.getPrescriptions().get(0));
        assertEquals(AppointmentType.GENERAL_CHECKUP, createdOfficeVisit.getType());
    }

    @Test
    public void testUpdate() {
        String hospitalName = "TestHospital";
        String address = "TestAddress";
        String zip = "TestZip";
        String state = "TestState";
        String testStr = "test";
        final Hospital hospital = new Hospital(hospitalName, address, zip, state);

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        final ICDCode code = new ICDCode("T10", "Test 10");
        DiagnosisForm diagnosisForm1 = new DiagnosisForm();
        diagnosisForm1.setNote(testStr);
        List<DiagnosisForm> diagnosisForms = Arrays.asList(diagnosisForm1);
        Diagnosis diagnosis1 = new Diagnosis();
        diagnosis1.setNote(testStr);
        diagnosis1.setCode(code);
        List<Diagnosis> diagnoses = Arrays.asList(diagnosis1);

        PrescriptionForm prescriptionForm1 = new PrescriptionForm();
        List<PrescriptionForm> prescriptionForms = Arrays.asList(prescriptionForm1);
        Prescription prescription1 = new Prescription();
        List<Prescription> prescriptions = Arrays.asList(prescription1);

        OfficeVisitForm ovf = new OfficeVisitForm();
        ovf.setId("124516123");
        ovf.setPatient(patientName);
        ovf.setHcp(hcpName);
        ovf.setNotes(testStr);
        ovf.setDate(String.valueOf(date));
        ovf.setPreScheduled("true");
        ovf.setHospital(hospitalName);
        ovf.setDiagnoses(diagnosisForms);
        ovf.setPrescriptions(prescriptionForms);
        ovf.setType("GENERAL_CHECKUP");

        OfficeVisit ov = new OfficeVisit();
        ov.setPatient(patient);
        ov.setHcp(hcp);
        ov.setDate(date);
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        BasicHealthMetrics basicHealthMetrics = new BasicHealthMetrics();

        mockAssertExistsById(ovf.getId());
        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(hcpName))).willReturn(hcp);
        mockGetAppointmentRequest(ov, appointmentRequest);
        given(hospitalService.findByName(eq(hospitalName))).willReturn(hospital);
        mockGetBasicHealthMetrics(ovf, basicHealthMetrics);
        mockGetDiagnoses(diagnosisForms, diagnoses);
        mockGetPrescriptions(prescriptionForms, prescriptions);

        final var id = 124_516_123L;

        given(officeVisitRepository.save(any()))
                .will(
                        invocation -> {
                            OfficeVisit officeVisit = invocation.getArgument(0);
                            officeVisit.setId(id);
                            return officeVisit;
                        });

        OfficeVisit createdOfficeVisit = officeVisitMutationService.update(ovf);

        assertNotNull(createdOfficeVisit);
        assertEquals(id, (long) createdOfficeVisit.getId());
        assertEquals(patient, createdOfficeVisit.getPatient());
        assertEquals(hcp, createdOfficeVisit.getHcp());
        assertEquals(testStr, createdOfficeVisit.getNotes());
        assertEquals(date, createdOfficeVisit.getDate());
        assertAppointmentRequestEquals(appointmentRequest, createdOfficeVisit.getAppointment());
        assertEquals(hospital, createdOfficeVisit.getHospital());
        assertThat(basicHealthMetrics.equals(createdOfficeVisit.getBasicHealthMetrics())).isTrue();
        assertEquals(diagnosis1, createdOfficeVisit.getDiagnoses().get(0));
        assertEquals(prescription1, createdOfficeVisit.getPrescriptions().get(0));
        assertEquals(AppointmentType.GENERAL_CHECKUP, createdOfficeVisit.getType());
    }

    @Test
    public void testCreateForOphthalmologySurgery() {
        String hospitalName = "TestHospital";
        String address = "TestAddress";
        String zip = "TestZip";
        String state = "TestState";
        String testStr = "test";
        final Hospital hospital = new Hospital(hospitalName, address, zip, state);

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        final ICDCode code = new ICDCode("T10", "Test 10");
        DiagnosisForm diagnosisForm1 = new DiagnosisForm();
        diagnosisForm1.setNote(testStr);
        List<DiagnosisForm> diagnosisForms = Arrays.asList(diagnosisForm1);
        Diagnosis diagnosis1 = new Diagnosis();
        diagnosis1.setNote(testStr);
        diagnosis1.setCode(code);
        List<Diagnosis> diagnoses = Arrays.asList(diagnosis1);

        PrescriptionForm prescriptionForm1 = new PrescriptionForm();
        List<PrescriptionForm> prescriptionForms = Arrays.asList(prescriptionForm1);
        Prescription prescription1 = new Prescription();
        List<Prescription> prescriptions = Arrays.asList(prescription1);

        OphthalmologySurgeryForm osf = new OphthalmologySurgeryForm();
        osf.setId("124516123");
        osf.setPatient(patientName);
        osf.setHcp(hcpName);
        osf.setNotes(testStr);
        osf.setDate(String.valueOf(date));
        osf.setPreScheduled("true");
        osf.setHospital(hospitalName);
        osf.setDiagnoses(diagnosisForms);
        osf.setPrescriptions(prescriptionForms);
        osf.setType("OPHTHALMOLOGY_SURGERY");

        OphthalmologySurgery os = new OphthalmologySurgery();
        os.setLeftVisualAcuityResult(123);
        os.setRightVisualAcuityResult(123);
        os.setLeftSphere(23f);
        os.setRightSphere(23f);
        os.setId(124_516_123L);
        os.setSurgeryType(OphthalmologySurgeryType.CATARACT_SURGERY);

        OfficeVisit ov = new OfficeVisit();
        ov.setPatient(patient);
        ov.setHcp(hcp);
        ov.setDate(date);
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        BasicHealthMetrics basicHealthMetrics = new BasicHealthMetrics();

        mockAssertNotExistsById(osf.getId());
        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(hcpName))).willReturn(hcp);
        mockGetAppointmentRequest(ov, appointmentRequest);
        given(hospitalService.findByName(eq(hospitalName))).willReturn(hospital);
        mockGetBasicHealthMetrics(osf, basicHealthMetrics);
        mockGetDiagnoses(diagnosisForms, diagnoses);
        mockGetPrescriptions(prescriptionForms, prescriptions);
        mockGetOphthalmologySurgery(osf, os);

        final var id = 124_516_123L;

        given(officeVisitRepository.save(any()))
                .will(
                        invocation -> {
                            OfficeVisit officeVisit = invocation.getArgument(0);
                            officeVisit.setId(id);
                            return officeVisit;
                        });

        OfficeVisit createdOfficeVisit =
                officeVisitMutationService.createForOphthalmologySurgery(osf);

        assertNotNull(createdOfficeVisit);
        assertEquals(id, (long) createdOfficeVisit.getId());
        assertEquals(patient, createdOfficeVisit.getPatient());
        assertEquals(hcp, createdOfficeVisit.getHcp());
        assertEquals(testStr, createdOfficeVisit.getNotes());
        assertEquals(date, createdOfficeVisit.getDate());
        assertAppointmentRequestEquals(appointmentRequest, createdOfficeVisit.getAppointment());
        assertEquals(hospital, createdOfficeVisit.getHospital());
        assertThat(basicHealthMetrics.equals(createdOfficeVisit.getBasicHealthMetrics())).isTrue();
        assertEquals(diagnosis1, createdOfficeVisit.getDiagnoses().get(0));
        assertEquals(prescription1, createdOfficeVisit.getPrescriptions().get(0));
        assertEquals(AppointmentType.OPHTHALMOLOGY_SURGERY, createdOfficeVisit.getType());
        assertEquals(os, createdOfficeVisit.getOphthalmologySurgery());
    }

    @Test
    public void testUpdateForOphthalmologySurgery() {
        Long officeVisitId = 1L;
        UpdateOfficeVisitForm updateOfficeVisitForm = new UpdateOfficeVisitForm();
        UpdateOphthalmologySurgeryForm updateOsf = new UpdateOphthalmologySurgeryForm();
        updateOfficeVisitForm.setId("1");
        updateOfficeVisitForm.setDate("2023-01-01T10:00:00Z");
        updateOfficeVisitForm.setOphthalmologySurgery(updateOsf);
        updateOfficeVisitForm.setNotes("Updated Notes");

        final var id = 124_516_123L;
        OfficeVisit existingOfficeVisit = new OfficeVisit();
        OphthalmologySurgery existingOs = new OphthalmologySurgery();
        existingOs.setLeftVisualAcuityResult(123);
        existingOs.setRightVisualAcuityResult(123);
        existingOs.setLeftSphere(23f);
        existingOs.setRightSphere(23f);
        existingOs.setId(id);
        existingOs.setSurgeryType(OphthalmologySurgeryType.CATARACT_SURGERY);

        existingOfficeVisit.setId(1L);
        existingOfficeVisit.setDate(ZonedDateTime.parse("2022-01-01T09:00:00Z"));
        existingOfficeVisit.setOphthalmologySurgery(existingOs);
        existingOfficeVisit.setNotes("Original Notes");
        existingOfficeVisit.setType(AppointmentType.GENERAL_CHECKUP);

        given(officeVisitRepository.findById(officeVisitId))
                .willReturn(Optional.of(existingOfficeVisit));
        given(ophthalmologySurgeryService.update(eq(id), eq(updateOsf))).willReturn(existingOs);

        given(officeVisitRepository.save(any()))
                .will(
                        invocation -> {
                            OfficeVisit officeVisit = invocation.getArgument(0);
                            officeVisit.setId(id);
                            return officeVisit;
                        });

        OfficeVisit updatedOfficeVisit =
                officeVisitMutationService.updateForOphthalmologySurgery(
                        officeVisitId, updateOfficeVisitForm);

        assertEquals(
                updatedOfficeVisit.getDate(), ZonedDateTime.parse(updateOfficeVisitForm.getDate()));
        assertEquals(updatedOfficeVisit.getOphthalmologySurgery(), existingOs);
        assertEquals(updatedOfficeVisit.getNotes(), updateOfficeVisitForm.getNotes());
    }

    @Test
    public void testUpdateForOphthalmologySurgeryNotFound() {
        Long officeVisitId = 1L;
        UpdateOfficeVisitForm updateOfficeVisitForm = new UpdateOfficeVisitForm();
        UpdateOphthalmologySurgeryForm updateOphthalmologySurgeryForm =
                new UpdateOphthalmologySurgeryForm();
        updateOfficeVisitForm.setDate("2023-01-01T10:00:00Z");
        updateOfficeVisitForm.setOphthalmologySurgery(updateOphthalmologySurgeryForm);
        updateOfficeVisitForm.setNotes("Updated Notes");
        given(officeVisitRepository.findById(officeVisitId)).willReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () ->
                                officeVisitMutationService.updateForOphthalmologySurgery(
                                        officeVisitId, updateOfficeVisitForm));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(
                "Office visit with the id " + officeVisitId + " doesn't exist",
                exception.getReason());
    }

    @Test
    public void testAssertNotExistsByIdWhenIdIsNull() {
        String idString = null;

        Long result = officeVisitMutationService.assertNotExistsById(idString);

        assertNull(result);
    }

    @Test
    public void testAssertNotExistsByIdWhenIdExists() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(true);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> officeVisitMutationService.assertNotExistsById(idString));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Office visit with the id 1 already exists", exception.getReason());
    }

    @Test
    public void testAssertNotExistsByIdWhenIdDoesNotExist() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(false);

        Long result = officeVisitMutationService.assertNotExistsById(idString);

        assertEquals(id, (long) result);
    }

    @Test
    public void testAssertExistsByIdWhenIdIsNull() {
        String idString = null;

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> officeVisitMutationService.assertExistsById(idString));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    public void testAssertExistsByIdWhenIdDoesNotExist() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(false);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> officeVisitMutationService.assertExistsById(idString));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Office visit with the id 1 doesn't exist", exception.getReason());
    }

    @Test
    public void testAssertExistsByIdWhenIdExists() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(true);

        Long result = officeVisitMutationService.assertExistsById(idString);

        assertEquals(id, (long) result);
    }

    @Test
    public void testGetAppointmentRequestWhenPreScheduledIsNull() {
        OfficeVisit officeVisit = new OfficeVisit();
        String preScheduled = null;

        AppointmentRequest result =
                officeVisitMutationService.getAppointmentRequest(officeVisit, preScheduled);

        assertNull(result);
    }

    @Test
    public void testGetAppointmentRequestWhenNoMatchFound() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        OfficeVisit officeVisit = new OfficeVisit();
        officeVisit.setPatient(patient);
        officeVisit.setHcp(hcp);
        officeVisit.setDate(date);
        String preScheduled = "true";
        given(appointmentRequestService.findByHcpAndPatientAndDate(eq(hcp), eq(patient), eq(date)))
                .willReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () ->
                                officeVisitMutationService.getAppointmentRequest(
                                        officeVisit, preScheduled));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Marked as pre-schedule but no match can be found", exception.getReason());
    }

    @Test
    public void testGetAppointmentRequestWhenMatchFound() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        OfficeVisit officeVisit = new OfficeVisit();
        officeVisit.setPatient(patient);
        officeVisit.setHcp(hcp);
        officeVisit.setDate(date);
        String preScheduled = "true";
        AppointmentRequest appointmentRequest = new AppointmentRequest();

        given(appointmentRequestService.findByHcpAndPatientAndDate(eq(hcp), eq(patient), eq(date)))
                .willReturn(Optional.of(appointmentRequest));

        AppointmentRequest result =
                officeVisitMutationService.getAppointmentRequest(officeVisit, preScheduled);

        assertNotNull(result);
        assertEquals(appointmentRequest, result);
    }

    @Test
    public void testGetBasicHealthMetrics() {
        OfficeVisitForm ovf = new OfficeVisitForm();
        BasicHealthMetrics basicHealthMetrics = new BasicHealthMetrics();
        given(bhmService.build(eq(ovf))).willReturn(basicHealthMetrics);

        BasicHealthMetrics result = officeVisitMutationService.getBasicHealthMetrics(ovf);

        assertNotNull(result);
        assertEquals(basicHealthMetrics, result);
    }

    @Test
    public void testGetDiagnosesWhenDiagnosisFormsIsNull() {
        List<DiagnosisForm> diagnosisForms = null;

        List<Diagnosis> result = officeVisitMutationService.getDiagnoses(diagnosisForms);

        assertNull(result);
    }

    @Test
    public void testGetDiagnosesWhenDiagnosisFormsIsNotNull() {
        DiagnosisForm diagnosisForm1 = new DiagnosisForm();
        DiagnosisForm diagnosisForm2 = new DiagnosisForm();
        List<DiagnosisForm> diagnosisForms = Arrays.asList(diagnosisForm1, diagnosisForm2);
        Diagnosis diagnosis1 = new Diagnosis();
        Diagnosis diagnosis2 = new Diagnosis();
        given(diagnosisService.build(eq(diagnosisForm1))).willReturn(diagnosis1);
        given(diagnosisService.build(eq(diagnosisForm2))).willReturn(diagnosis2);

        List<Diagnosis> result = officeVisitMutationService.getDiagnoses(diagnosisForms);

        assertNotNull(result);
        assertEquals(diagnosis1, result.get(0));
        assertEquals(diagnosis2, result.get(1));
    }

    @Test
    public void testGetPrescriptionsWhenDiagnosisFormsIsNull() {
        List<PrescriptionForm> prescriptionForms = null;

        List<Prescription> result = officeVisitMutationService.getPrescriptions(prescriptionForms);

        assertNull(result);
    }

    @Test
    public void testGetPrescriptionsWhenDiagnosisFormsIsNotNull() {
        PrescriptionForm prescriptionForm1 = new PrescriptionForm();
        PrescriptionForm prescriptionForm2 = new PrescriptionForm();
        List<PrescriptionForm> prescriptionForms =
                Arrays.asList(prescriptionForm1, prescriptionForm2);
        Prescription prescription1 = new Prescription();
        Prescription prescription2 = new Prescription();
        given(prescriptionService.build(eq(prescriptionForm1))).willReturn(prescription1);
        given(prescriptionService.build(eq(prescriptionForm2))).willReturn(prescription2);

        List<Prescription> result = officeVisitMutationService.getPrescriptions(prescriptionForms);

        assertNotNull(result);
        assertEquals(prescription1, result.get(0));
        assertEquals(prescription2, result.get(1));
    }

    @Test
    public void testGetOphthalmologySurgery() {
        OphthalmologySurgeryForm ovf = new OphthalmologySurgeryForm();
        OphthalmologySurgery ophthalmologySurgery = new OphthalmologySurgery();
        given(ophthalmologySurgeryService.create(eq(ovf))).willReturn(ophthalmologySurgery);

        OphthalmologySurgery result = officeVisitMutationService.getOphthalmologySurgery(ovf);

        assertNotNull(result);
        assertEquals(ophthalmologySurgery, result);
    }
}
