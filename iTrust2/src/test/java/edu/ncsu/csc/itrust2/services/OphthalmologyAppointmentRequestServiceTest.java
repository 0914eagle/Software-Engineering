package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.AppointmentRequestForm;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.enums.*;
import edu.ncsu.csc.itrust2.repositories.AppointmentRequestRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OphthalmologyAppointmentRequestServiceTest {
    @Mock private UserService userService;
    @Mock private AppointmentRequestRepository appointmentRequestRepository;
    @InjectMocks private AppointmentRequestService appointmentRequestService;

    @Test
    public void testListByPatient() {
        final var currentYear = ZonedDateTime.now().getYear();
        final var date = ZonedDateTime.of(currentYear + 1, 12, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "TestOph";
        final Personnel ophUser = new Personnel();
        ophUser.setUsername(ophName);
        ophUser.setRoles(Set.of(Role.ROLE_HCP, Role.ROLE_OPH));

        AppointmentRequest expectedOphAppointment =
                new AppointmentRequest(
                        1L,
                        patient,
                        ophUser,
                        date,
                        AppointmentType.GENERAL_OPHTHALMOLOGY,
                        "test comment",
                        Status.PENDING,
                        "test name",
                        "test abb",
                        "90000",
                        "test long comment");

        List<AppointmentRequest> expectedFindByPatient = new ArrayList<>();
        expectedFindByPatient.add(expectedOphAppointment);

        given(appointmentRequestRepository.findByPatientAndDateAfter(eq(patient), any()))
                .willReturn(expectedFindByPatient);

        final List<AppointmentRequest> result = appointmentRequestService.findByPatient(patient);

        assertEquals(expectedFindByPatient, result);
    }

    @Test
    public void testListByHCP() {
        final var currentYear = ZonedDateTime.now().getYear();
        final var date = ZonedDateTime.of(currentYear + 1, 12, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "TestOph";
        final Personnel ophUser = new Personnel();
        ophUser.setUsername(ophName);
        ophUser.setRoles(Set.of(Role.ROLE_HCP, Role.ROLE_OPH));

        AppointmentRequest expectedOphAppointment =
                new AppointmentRequest(
                        1L,
                        patient,
                        ophUser,
                        date,
                        AppointmentType.GENERAL_OPHTHALMOLOGY,
                        "test comment",
                        Status.PENDING,
                        "test name",
                        "test abb",
                        "90000",
                        "test long comment");

        List<AppointmentRequest> expectedFindByPatient = new ArrayList<>();
        expectedFindByPatient.add(expectedOphAppointment);

        given(appointmentRequestRepository.findByHcpAndDateAfter(eq(ophUser), any()))
                .willReturn(expectedFindByPatient);

        final List<AppointmentRequest> result = appointmentRequestService.findByHcp(ophUser);

        assertEquals(expectedFindByPatient, result);
    }

    @Test
    public void testListByHcpAndPatient() {
        final var currentYear = ZonedDateTime.now().getYear();
        final var date = ZonedDateTime.of(currentYear + 1, 12, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "TestOph";
        final Personnel ophUser = new Personnel();
        ophUser.setUsername(ophName);
        ophUser.setRoles(Set.of(Role.ROLE_HCP, Role.ROLE_OPH));

        AppointmentRequest expectedOphAppointment =
                new AppointmentRequest(
                        1L,
                        patient,
                        ophUser,
                        date,
                        AppointmentType.GENERAL_OPHTHALMOLOGY,
                        "test comment",
                        Status.PENDING,
                        "test name",
                        "test abb",
                        "90000",
                        "test long comment");

        given(
                        appointmentRequestRepository.findByHcpAndPatientAndDate(
                                eq(ophUser), eq(patient), eq(date)))
                .willReturn(Optional.of(expectedOphAppointment));

        final Optional<AppointmentRequest> result =
                appointmentRequestService.findByHcpAndPatientAndDate(ophUser, patient, date);

        assertTrue(result.isPresent());
        assertEquals(expectedOphAppointment, result.get());
    }

    @Test
    public void testBuild() {
        final var currentYear = ZonedDateTime.now().getYear();
        final var date = ZonedDateTime.of(currentYear + 1, 12, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "TestOph";
        final Personnel ophUser = new Personnel();
        ophUser.setUsername(ophName);
        ophUser.setRoles(Set.of(Role.ROLE_HCP, Role.ROLE_OPH));

        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(ophName))).willReturn(ophUser);

        AppointmentRequest expectedOphAppointment =
                new AppointmentRequest(
                        1L,
                        patient,
                        ophUser,
                        date,
                        AppointmentType.GENERAL_OPHTHALMOLOGY,
                        "test comment",
                        Status.PENDING,
                        "test name",
                        "test abb",
                        "90000",
                        "test long comment");

        AppointmentRequestForm ophAppointmentForm =
                new AppointmentRequestForm(expectedOphAppointment);

        AppointmentRequest result = appointmentRequestService.build(ophAppointmentForm);

        assertAppointmentRequestEquals(expectedOphAppointment, result);
    }

    @Test
    public void testBuildNullStatusAndType() {
        final var currentYear = ZonedDateTime.now().getYear();
        final var date = ZonedDateTime.of(currentYear + 1, 12, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "TestOph";
        final Personnel ophUser = new Personnel();
        ophUser.setUsername(ophName);
        ophUser.setRoles(Set.of(Role.ROLE_HCP, Role.ROLE_OPH));

        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(ophName))).willReturn(ophUser);

        AppointmentRequest expectedAppointment =
                new AppointmentRequest(
                        1L,
                        patient,
                        ophUser,
                        date,
                        AppointmentType.GENERAL_CHECKUP,
                        "test comment",
                        Status.PENDING,
                        "test name",
                        "test abb",
                        "90000",
                        "test long comment");

        AppointmentRequestForm appointmentForm = new AppointmentRequestForm(expectedAppointment);
        appointmentForm.setType(null);
        appointmentForm.setStatus(null);

        AppointmentRequest result = appointmentRequestService.build(appointmentForm);

        assertAppointmentRequestEquals(expectedAppointment, result);
    }

    @Test
    public void testBuildThrowsExceptionWhenDateIsBeforeNow() {
        final var currentYear = ZonedDateTime.now().getYear();
        final var date = ZonedDateTime.of(currentYear - 1, 1, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "TestOph";
        final Personnel ophUser = new Personnel();
        ophUser.setUsername(ophName);

        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(ophName))).willReturn(ophUser);

        AppointmentRequestForm appointmentForm = new AppointmentRequestForm();
        appointmentForm.setId("1");
        appointmentForm.setPatient("TestPatient");
        appointmentForm.setHcp("TestOph");
        appointmentForm.setDate(date.toString());
        appointmentForm.setType(AppointmentType.GENERAL_OPHTHALMOLOGY.toString());
        appointmentForm.setComments("test comment");
        appointmentForm.setStatus(Status.PENDING.toString());
        appointmentForm.setName("test name");
        appointmentForm.setAbbreviation("test abb");
        appointmentForm.setCptCode("90000");
        appointmentForm.setLongComment("test long comment");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentRequestService.build(appointmentForm));

        String expectedErrorMessage = "Cannot request an appointment before the current time";
        String actualErrorMessage = exception.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    public static void assertAppointmentRequestEquals(
            AppointmentRequest expectedOphAppointment, AppointmentRequest result) {
        assertEquals(expectedOphAppointment.getId(), result.getId());
        assertEquals(expectedOphAppointment.getPatient(), result.getPatient());
        assertEquals(expectedOphAppointment.getHcp(), result.getHcp());
        assertEquals(expectedOphAppointment.getDate(), result.getDate());
        assertEquals(expectedOphAppointment.getType(), result.getType());
        assertEquals(expectedOphAppointment.getComments(), result.getComments());
        assertEquals(expectedOphAppointment.getStatus(), result.getStatus());
        assertEquals(expectedOphAppointment.getName(), result.getName());
        assertEquals(expectedOphAppointment.getAbbreviation(), result.getAbbreviation());
        assertEquals(expectedOphAppointment.getCptCode(), result.getCptCode());
        assertEquals(expectedOphAppointment.getLongComment(), result.getLongComment());
    }
}
