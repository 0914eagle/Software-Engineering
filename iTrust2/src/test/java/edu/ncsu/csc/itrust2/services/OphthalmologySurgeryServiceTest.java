package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.forms.UpdateOphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;
import edu.ncsu.csc.itrust2.repositories.OphthalmologySurgeryRepository;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OphthalmologySurgeryServiceTest {
    @Mock private UserService userService;
    @Mock private OphthalmologySurgeryRepository ophthalmologySurgeryRepository;
    @InjectMocks private OphthalmologySurgeryService ophthalmologySurgeryService;

    @Test
    public void testCreate() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        OphthalmologySurgeryForm osf =
                new OphthalmologySurgeryForm(
                        123,
                        45,
                        12f,
                        34f,
                        12f,
                        34f,
                        123,
                        45,
                        OphthalmologySurgeryType.CATARACT_SURGERY);
        osf.setPatient(patientName);
        osf.setHcp(hcpName);

        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(hcpName))).willReturn(hcp);

        final var id = 124_516_123L;

        given(ophthalmologySurgeryRepository.save(any()))
                .will(
                        invocation -> {
                            OphthalmologySurgery ophthalmologySurgery = invocation.getArgument(0);
                            ophthalmologySurgery.setId(id);
                            return ophthalmologySurgery;
                        });

        OphthalmologySurgery createdOphthalmologySurgery = ophthalmologySurgeryService.create(osf);

        assertNotNull(createdOphthalmologySurgery);
        assertEquals(id, (long) createdOphthalmologySurgery.getId());
        assertEquals(patient, createdOphthalmologySurgery.getPatient());
        assertEquals(hcp, createdOphthalmologySurgery.getHcp());
        assertEquals(123, (int) createdOphthalmologySurgery.getLeftVisualAcuityResult());
        assertEquals(45, (int) createdOphthalmologySurgery.getRightVisualAcuityResult());
        assertEquals(12f, createdOphthalmologySurgery.getLeftSphere(), 0.001);
        assertEquals(34f, createdOphthalmologySurgery.getRightSphere(), 0.001);
        assertEquals(12f, createdOphthalmologySurgery.getLeftCylinder(), 0.001);
        assertEquals(34f, createdOphthalmologySurgery.getRightCylinder(), 0.001);
        assertEquals(123, (int) createdOphthalmologySurgery.getLeftAxis());
        assertEquals(45, (int) createdOphthalmologySurgery.getRightAxis());
    }

    @Test
    public void testUpdate() {
        final var id = 124_516_123L;

        UpdateOphthalmologySurgeryForm updateOsf = new UpdateOphthalmologySurgeryForm();
        updateOsf.setLeftVisualAcuityResult(123);
        updateOsf.setRightVisualAcuityResult(45);
        updateOsf.setLeftSphere(12f);
        updateOsf.setRightSphere(34f);
        updateOsf.setLeftCylinder(12f);
        updateOsf.setRightCylinder(34f);
        updateOsf.setLeftAxis(123);
        updateOsf.setRightAxis(45);
        updateOsf.setSurgeryType(OphthalmologySurgeryType.CATARACT_SURGERY);

        OphthalmologySurgery existingOs = new OphthalmologySurgery();
        existingOs.setId(id);
        given(ophthalmologySurgeryRepository.findById(eq(id))).willReturn(Optional.of(existingOs));

        given(ophthalmologySurgeryRepository.save(any()))
                .will(
                        invocation -> {
                            OphthalmologySurgery ophthalmologySurgery = invocation.getArgument(0);
                            ophthalmologySurgery.setId(id);
                            return ophthalmologySurgery;
                        });

        OphthalmologySurgery updatedOphthalmologySurgery =
                ophthalmologySurgeryService.update(id, updateOsf);

        assertNotNull(updatedOphthalmologySurgery);
        assertEquals(id, (long) updatedOphthalmologySurgery.getId());
        assertEquals(123, (int) updatedOphthalmologySurgery.getLeftVisualAcuityResult());
        assertEquals(45, (int) updatedOphthalmologySurgery.getRightVisualAcuityResult());
        assertEquals(12f, updatedOphthalmologySurgery.getLeftSphere(), 0.001);
        assertEquals(34f, updatedOphthalmologySurgery.getRightSphere(), 0.001);
        assertEquals(12f, updatedOphthalmologySurgery.getLeftCylinder(), 0.001);
        assertEquals(34f, updatedOphthalmologySurgery.getRightCylinder(), 0.001);
        assertEquals(123, (int) updatedOphthalmologySurgery.getLeftAxis());
        assertEquals(45, (int) updatedOphthalmologySurgery.getRightAxis());
    }

    @Test
    public void testUpdateNonExistingOphthalmologySurgery() {
        // Arrange
        Long id = 1L;
        UpdateOphthalmologySurgeryForm updateOphthalmologySurgeryForm =
                new UpdateOphthalmologySurgeryForm();

        given(ophthalmologySurgeryRepository.findById(id)).willReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () ->
                                ophthalmologySurgeryService.update(
                                        id, updateOphthalmologySurgeryForm));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(
                "Ophthalmology surgery with the id " + id + " doesn't exist",
                exception.getReason());
    }

    @Test
    public void testUpdateIfAllFieldIsNull() {
        final var id = 124_516_123L;

        UpdateOphthalmologySurgeryForm updateOsf = new UpdateOphthalmologySurgeryForm();

        OphthalmologySurgery existingOs = new OphthalmologySurgery();
        existingOs.setLeftVisualAcuityResult(123);
        existingOs.setRightVisualAcuityResult(45);
        existingOs.setLeftSphere(12f);
        existingOs.setRightSphere(34f);
        existingOs.setLeftAxis(123);
        existingOs.setRightAxis(45);
        existingOs.setLeftCylinder(12f);
        existingOs.setRightCylinder(34f);
        existingOs.setSurgeryType(OphthalmologySurgeryType.CATARACT_SURGERY);

        given(ophthalmologySurgeryRepository.findById(eq(id))).willReturn(Optional.of(existingOs));

        given(ophthalmologySurgeryRepository.save(any()))
                .will(
                        invocation -> {
                            OphthalmologySurgery ophthalmologySurgery = invocation.getArgument(0);
                            ophthalmologySurgery.setId(id);
                            return ophthalmologySurgery;
                        });

        OphthalmologySurgery updatedOphthalmologySurgery =
                ophthalmologySurgeryService.update(id, updateOsf);

        assertNotNull(updatedOphthalmologySurgery);
        assertEquals(id, (long) updatedOphthalmologySurgery.getId());
        assertEquals(123, (int) updatedOphthalmologySurgery.getLeftVisualAcuityResult());
        assertEquals(45, (int) updatedOphthalmologySurgery.getRightVisualAcuityResult());
        assertEquals(12f, updatedOphthalmologySurgery.getLeftSphere(), 0.001);
        assertEquals(34f, updatedOphthalmologySurgery.getRightSphere(), 0.001);
        assertEquals(12f, updatedOphthalmologySurgery.getLeftCylinder(), 0.001);
        assertEquals(34f, updatedOphthalmologySurgery.getRightCylinder(), 0.001);
        assertEquals(123, (int) updatedOphthalmologySurgery.getLeftAxis());
        assertEquals(45, (int) updatedOphthalmologySurgery.getRightAxis());
    }
}
