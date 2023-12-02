package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class ApiOphthalmologySurgeryController extends APIController {

    private final OfficeVisitService officeVisitService;

    @PostMapping("/officevisits/ophthalmologySurgery")
    @PreAuthorize("hasRole('ROLE_OPH')")
    public ResponseEntity createOphthalmologySurgery(
            @RequestBody final OphthalmologySurgeryForm ophthalmologySurgeryForm) {
        try {
            final OfficeVisit visit =
                    officeVisitService.buildOphthalmologySurgeryVisit(ophthalmologySurgeryForm);

            if (null != visit.getId() && officeVisitService.existsById(visit.getId())) {
                return new ResponseEntity(
                        errorResponse(
                                "Office visit with the id " + visit.getId() + " already exists"),
                        HttpStatus.CONFLICT);
            }
            officeVisitService.save(visit);
            return new ResponseEntity(visit, HttpStatus.OK);

        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse(
                            "Could not validate or save the OfficeVisit provided due to "
                                    + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
