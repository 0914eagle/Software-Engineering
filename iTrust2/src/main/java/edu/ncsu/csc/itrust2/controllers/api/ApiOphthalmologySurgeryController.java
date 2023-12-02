package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;

import lombok.RequiredArgsConstructor;
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
    public OfficeVisit createOphthalmologySurgery(
            @RequestBody final OphthalmologySurgeryForm ophthalmologySurgeryForm) {
        return officeVisitService.addOphthalmologySurgery(ophthalmologySurgeryForm);
    }
}
