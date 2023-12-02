package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.controllers.api.ApiPersonalRepresentationController;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.security.LogEntry;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PersonalRepresentationServiceTest {
    @Mock private PersonalRepresentationRepository PersonalRepresentationRepository;
    @Mock private PatientService patientService;
    @Mock private AppointmentRequestService appointmentRequestService;
    @Mock private BasicHealthMetricsService basicHealthMetricsService;
    @Mock private EmergencyPatientService emergencyPatientService;
    @Mock private LoggerUtil loggerUtil;
    @Mock private PersonalRepresentationService mockpersonalRepresentationService;
    @InjectMocks private PersonalRepresentationService personalRepresentationService;
    @InjectMocks private ApiPersonalRepresentationController apiController;
    @Mock private PatientRepository patientRepository;

    @Test
    public void testSetPersonalRepresentation() {
        final String patient = "patient";
        final String representative = "representative";

        final UserForm patientUserForm = new UserForm(patient, "123456", Role.ROLE_PATIENT, 1);
        final UserForm representativeUserForm = new UserForm(representative, "123456", Role.ROLE_PATIENT, 1);
        final Patient patientUser = new Patient(patientUserForm);
        final Patient representativeUser= new Patient(representativeUserForm);

        given(patientRepository.findByUsername(patient)).willReturn(patientUser);
        given(patientRepository.findByUsername(representative)).willReturn(representativeUser);
        given(PersonalRepresentationRepository.save(any(PersonalRepresentation.class))).willAnswer(invocation -> invocation.getArgument(0));

        final PersonalRepresentation result = personalRepresentationService.setPersonalRepresentation(patient, representative);

        verify(PersonalRepresentationRepository).save(any(PersonalRepresentation.class));
        assertEquals(patientUser, result.getPatient());
        assertEquals(representativeUser, result.getPersonalRepresentative());
    }

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
    public void testListPatientLogs() {
        // 환자 사용자 이름과 관련 객체 설정
        final String currentUsername = "currentUsername";
        final String representingPatientUsername = "patient1";
        final UserForm patient1Form =
                new UserForm(representingPatientUsername, "123456", Role.ROLE_PATIENT, 1);
        Patient representingPatientUser = new Patient(patient1Form);

        // 데이터 준비
        LogEntry log1 =
                new LogEntry(
                        TransactionType.LOGIN_SUCCESS,
                        representingPatientUsername,
                        null,
                        "User has logged in successfully");
        List<LogEntry> expectedLogs = new ArrayList<>();
        expectedLogs.add(log1);

        // 모킹 설정
        given(patientService.findByName(representingPatientUsername))
                .willReturn(representingPatientUser);
        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);

        PersonalRepresentation representation = new PersonalRepresentation();
        PersonalRepresentationRepository.save(representation);
        mockpersonalRepresentationService.setPersonalRepresentation(
                representingPatientUsername, currentUsername);

        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUsername, representingPatientUsername))
                .willReturn(true);
        given(loggerUtil.getAllForUser(representingPatientUsername)).willReturn(expectedLogs);

        // 정상 시나리오 테스트
        List<LogEntry> actualLogs = apiController.listPatientLogs(representingPatientUsername);
        assertEquals(expectedLogs, actualLogs);

        // 예외 시나리오 테스트
        mockpersonalRepresentationService.cancelPersonalRepresentation(
                representingPatientUsername, currentUsername);
        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUsername, representingPatientUsername))
                .willReturn(false);
        assertThatThrownBy(() -> apiController.listPatientLogs(representingPatientUsername))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Access denied. 대리인 관계의 환자가 아닙니다.");
    }

    @Test
    public void testListPatientMedicalRecords() {
        final String patientUsername = "patient1";
        final String currentUserUsername = "currentUsername";
        final UserForm patientUserForm =
                new UserForm(patientUsername, "123456", Role.ROLE_PATIENT, 1);
        final Patient patient = new Patient(patientUserForm);
        final List<BasicHealthMetrics> expectedHealthMetrics =
                Arrays.asList(new BasicHealthMetrics(), new BasicHealthMetrics());

        given(patientService.findByName(patientUsername)).willReturn(patient);
        given(loggerUtil.getCurrentUsername()).willReturn(currentUserUsername);

        PersonalRepresentation representation = new PersonalRepresentation();
        PersonalRepresentationRepository.save(representation);
        mockpersonalRepresentationService.setPersonalRepresentation(
                patientUsername, currentUserUsername);

        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUserUsername, patientUsername))
                .willReturn(true);
        given(basicHealthMetricsService.findByPatient(patient)).willReturn(expectedHealthMetrics);

        List<BasicHealthMetrics> actualHealthMetrics =
                apiController.listPatientMedicalRecords(patientUsername);
        assertEquals(expectedHealthMetrics, actualHealthMetrics);

        mockpersonalRepresentationService.cancelPersonalRepresentation(
                patientUsername, currentUserUsername);
        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUserUsername, patientUsername))
                .willReturn(false);
        try {
            apiController.listPatientMedicalRecords(patientUsername);
            fail("Expected a ResponseStatusException to be thrown");
        } catch (ResponseStatusException e) {
            assertEquals("Access denied. 대리인 관계의 환자가 아닙니다.", e.getReason());
        }
    }

    @Test
    public void testListPatientDiagnosesIn60Days() {
        final String patientUsername = "patient1";
        final String currentUserUsername = "currentUsername";
        final UserForm patientUserForm =
                new UserForm(patientUsername, "123456", Role.ROLE_PATIENT, 1);
        List<Diagnosis> expectedDiagnoses = Arrays.asList(new Diagnosis(), new Diagnosis());

        PersonalRepresentation representation = new PersonalRepresentation();
        PersonalRepresentationRepository.save(representation);
        mockpersonalRepresentationService.setPersonalRepresentation(
                patientUsername, currentUserUsername);

        given(loggerUtil.getCurrentUsername()).willReturn(currentUserUsername);
        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUserUsername, patientUsername))
                .willReturn(true);
        given(emergencyPatientService.getRecentDiagnoses(patientUsername))
                .willReturn(expectedDiagnoses);

        List<Diagnosis> actualDiagnoses =
                apiController.listPatientDiagnosesIn60Days(patientUsername);
        assertEquals(expectedDiagnoses, actualDiagnoses);

        mockpersonalRepresentationService.cancelPersonalRepresentation(
                patientUsername, currentUserUsername);
        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUserUsername, patientUsername))
                .willReturn(false);
        try {
            apiController.listPatientDiagnosesIn60Days(patientUsername);
            fail("Expected an ResponseStatusException to be thrown");
        } catch (ResponseStatusException e) {
            assertEquals("Access denied. 대리인 관계의 환자가 아닙니다.", e.getReason());
        }
    }

    @Test
    public void testListPatientAppointments() {
        final String currentUsername = "currentUsername";
        final String representingPatientUsername = "patient1";
        final UserForm patient1Form =
                new UserForm(representingPatientUsername, "123456", Role.ROLE_PATIENT, 1);
        Patient representingPatientUser = new Patient(patient1Form);

        List<AppointmentRequest> expectedAppointments =
                Arrays.asList(new AppointmentRequest(), new AppointmentRequest());
        given(patientService.findByName(representingPatientUsername))
                .willReturn(representingPatientUser);
        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);
        PersonalRepresentation representation = new PersonalRepresentation();

        PersonalRepresentationRepository.save(representation);
        mockpersonalRepresentationService.setPersonalRepresentation(
                representingPatientUsername, currentUsername);

        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUsername, representingPatientUsername))
                .willReturn(true);
        given(appointmentRequestService.findByPatient(representingPatientUser))
                .willReturn(expectedAppointments);
        List<AppointmentRequest> actualAppointments =
                apiController.listPatientAppointments(representingPatientUsername);
        assertEquals(expectedAppointments, actualAppointments);

        mockpersonalRepresentationService.cancelPersonalRepresentation(
                representingPatientUsername, currentUsername);
        given(
                        mockpersonalRepresentationService.isRepresentative(
                                currentUsername, representingPatientUsername))
                .willReturn(false);
        try {
            apiController.listPatientAppointments(representingPatientUsername);
            fail("Expected an ResponseStatusException to be thrown");
        } catch (ResponseStatusException e) {
            assertEquals("Access denied. 대리인 관계의 환자가 아닙니다.", e.getReason());
        }
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
