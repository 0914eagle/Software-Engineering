package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.repositories.OphthalmologySurgeryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
