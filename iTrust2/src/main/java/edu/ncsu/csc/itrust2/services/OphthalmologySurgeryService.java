package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.OphthalmologySurgeryRepository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class OphthalmologySurgeryService {

    private final PatientService patientService;
    private final UserService userService;
    private final OphthalmologySurgeryRepository ophthalmologySurgeryRepository;

    public List<OphthalmologySurgery> findByOPH(String ophName) {
        final User oph = userService.findByName(ophName);
        return ophthalmologySurgeryRepository.findByHcp(oph);
    }

    public List<OphthalmologySurgery> findByPatient(String patientName) {
        final Patient patient = (Patient) patientService.findByName(patientName);
        return ophthalmologySurgeryRepository.findByPatient(patient);
    }

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

    public OphthalmologySurgery update(final Long id, final OphthalmologySurgeryForm ophthalmologySurgeryForm) {
        Optional<OphthalmologySurgery> osOptional = ophthalmologySurgeryRepository.findById(id);
        if (osOptional.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Ophthalmology surgery with the id " + id + " doesn't exist");
        }
        final OphthalmologySurgery ophthalmologySurgery = osOptional.get();

        ophthalmologySurgery.setPatient(userService.findByName(ophthalmologySurgeryForm.getPatient()));
        ophthalmologySurgery.setHcp(userService.findByName(ophthalmologySurgeryForm.getHcp()));

        ophthalmologySurgery.setLeftVisualAcuityResult(ophthalmologySurgeryForm.getLeftVisualAcuityResult());
        ophthalmologySurgery.setRightVisualAcuityResult(
                ophthalmologySurgeryForm.getRightVisualAcuityResult());

        ophthalmologySurgery.setRightSphere(ophthalmologySurgeryForm.getRightSphere());
        ophthalmologySurgery.setLeftSphere(ophthalmologySurgeryForm.getLeftSphere());

        ophthalmologySurgery.setRightAxis(ophthalmologySurgeryForm.getRightAxis());
        ophthalmologySurgery.setLeftAxis(ophthalmologySurgeryForm.getLeftAxis());

        ophthalmologySurgery.setRightCylinder(ophthalmologySurgeryForm.getRightCylinder());
        ophthalmologySurgery.setLeftCylinder(ophthalmologySurgeryForm.getLeftCylinder());

        ophthalmologySurgery.setSurgeryType(ophthalmologySurgeryForm.getSurgeryType());

        return ophthalmologySurgeryRepository.save(ophthalmologySurgery);
    }
}
