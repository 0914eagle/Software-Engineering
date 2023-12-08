package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.forms.AppointmentRequestForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.Status;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OphthalmologyAppointmentRequestTest {
    @Test
    public void testMakingOphthalmologyAppointmentUsingConstructor() {
        final var date = ZonedDateTime.of(2023, 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "patientTest";
        final var patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "ophTest";
        final var oph = new Personnel();
        oph.setUsername(ophName);

        final var appointmentRequest =
                new AppointmentRequest(
                        1L,
                        patient,
                        oph,
                        date,
                        AppointmentType.GENERAL_OPHTHALMOLOGY,
                        "test comment",
                        Status.PENDING,
                        "test name",
                        "test abb",
                        "90000",
                        "test long comment");

        final var appointmentRequestForm = new AppointmentRequestForm(appointmentRequest);

        assertAppointmentRequestEquals(appointmentRequest, appointmentRequestForm);
    }

    @Test
    public void testMakingOphthalmologyAppointmentUsingSetter() {
        final var date = ZonedDateTime.of(2023, 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        String patientName = "patientTest";
        final var patient = new Patient();
        patient.setUsername(patientName);

        String ophName = "ophTest";
        final var oph = new Personnel();
        oph.setUsername(ophName);

        final var appointmentRequestUsingSetter = new AppointmentRequest();
        appointmentRequestUsingSetter.setId(1L);
        appointmentRequestUsingSetter.setPatient(patient);
        appointmentRequestUsingSetter.setHcp(oph);
        appointmentRequestUsingSetter.setDate(date);
        appointmentRequestUsingSetter.setType(AppointmentType.GENERAL_OPHTHALMOLOGY);
        appointmentRequestUsingSetter.setComments("test comment");
        appointmentRequestUsingSetter.setStatus(Status.PENDING);
        appointmentRequestUsingSetter.setName("test name");
        appointmentRequestUsingSetter.setAbbreviation("test abb");
        appointmentRequestUsingSetter.setCptCode("90000");
        appointmentRequestUsingSetter.setLongComment("test long comment");

        final var appointmentRequestFormUsingSetter =
                new AppointmentRequestForm(appointmentRequestUsingSetter);

        assertAppointmentRequestEquals(
                appointmentRequestUsingSetter, appointmentRequestFormUsingSetter);
    }

    public static void assertAppointmentRequestEquals(
            AppointmentRequest entity, AppointmentRequestForm form) {
        assertEquals(entity.getId().toString(), form.getId());
        assertEquals(entity.getPatient().getUsername(), form.getPatient());
        assertEquals(entity.getHcp().getUsername(), form.getHcp());
        assertEquals(entity.getDate().toString(), form.getDate());
        assertEquals(entity.getType().toString(), form.getType());
        assertEquals(entity.getComments(), form.getComments());
        assertEquals(entity.getStatus().toString(), form.getStatus());
        assertEquals(entity.getName(), form.getName());
        assertEquals(entity.getAbbreviation(), form.getAbbreviation());
        assertEquals(entity.getComments(), form.getComments());
        assertEquals(entity.getLongComment(), form.getLongComment());
    }
}
