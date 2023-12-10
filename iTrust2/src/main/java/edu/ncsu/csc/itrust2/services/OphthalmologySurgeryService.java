package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.forms.UpdateOphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.repositories.OphthalmologySurgeryRepository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class OphthalmologySurgeryService {

    private final UserService userService;
    private final OphthalmologySurgeryRepository ophthalmologySurgeryRepository;

    public OphthalmologySurgery create(final OphthalmologySurgeryForm officeVisitForm) {
        final OphthalmologySurgery ophthalmologySurgery = new OphthalmologySurgery();

        ophthalmologySurgery.setPatient(userService.findByName(officeVisitForm.getPatient()));
        ophthalmologySurgery.setHcp(userService.findByName(officeVisitForm.getHcp()));

        ophthalmologySurgery.setLeftVisualAcuityResult(officeVisitForm.getLeftVisualAcuityResult());
        ophthalmologySurgery.setRightVisualAcuityResult(
                officeVisitForm.getRightVisualAcuityResult());

        ophthalmologySurgery.setRightSphere(officeVisitForm.getRightSphere());
        ophthalmologySurgery.setLeftSphere(officeVisitForm.getLeftSphere());

        ophthalmologySurgery.setRightAxis(officeVisitForm.getRightAxis());
        ophthalmologySurgery.setLeftAxis(officeVisitForm.getLeftAxis());

        ophthalmologySurgery.setRightCylinder(officeVisitForm.getRightCylinder());
        ophthalmologySurgery.setLeftCylinder(officeVisitForm.getLeftCylinder());

        ophthalmologySurgery.setSurgeryType(officeVisitForm.getSurgeryType());

        return ophthalmologySurgeryRepository.save(ophthalmologySurgery);
    }

    public OphthalmologySurgery update(
            final Long id, final UpdateOphthalmologySurgeryForm ophthalmologySurgeryForm) {
        Optional<OphthalmologySurgery> osOptional = ophthalmologySurgeryRepository.findById(id);
        if (osOptional.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ophthalmology surgery with the id " + id + " doesn't exist");
        }
        final OphthalmologySurgery ophthalmologySurgery = osOptional.get();

        if (ophthalmologySurgeryForm.getLeftVisualAcuityResult() != null)
            ophthalmologySurgery.setLeftVisualAcuityResult(
                    ophthalmologySurgeryForm.getLeftVisualAcuityResult());
        if (ophthalmologySurgeryForm.getRightVisualAcuityResult() != null)
            ophthalmologySurgery.setRightVisualAcuityResult(
                    ophthalmologySurgeryForm.getRightVisualAcuityResult());

        if (ophthalmologySurgeryForm.getRightSphere() != null)
            ophthalmologySurgery.setRightSphere(ophthalmologySurgeryForm.getRightSphere());
        if (ophthalmologySurgeryForm.getLeftSphere() != null)
            ophthalmologySurgery.setLeftSphere(ophthalmologySurgeryForm.getLeftSphere());

        if (ophthalmologySurgeryForm.getRightAxis() != null)
            ophthalmologySurgery.setRightAxis(ophthalmologySurgeryForm.getRightAxis());
        if (ophthalmologySurgeryForm.getLeftAxis() != null)
            ophthalmologySurgery.setLeftAxis(ophthalmologySurgeryForm.getLeftAxis());

        if (ophthalmologySurgeryForm.getRightCylinder() != null)
            ophthalmologySurgery.setRightCylinder(ophthalmologySurgeryForm.getRightCylinder());
        if (ophthalmologySurgeryForm.getLeftCylinder() != null)
            ophthalmologySurgery.setLeftCylinder(ophthalmologySurgeryForm.getLeftCylinder());

        if (ophthalmologySurgeryForm.getSurgeryType() != null)
            ophthalmologySurgery.setSurgeryType(ophthalmologySurgeryForm.getSurgeryType());

        return ophthalmologySurgeryRepository.save(ophthalmologySurgery);
    }
}
