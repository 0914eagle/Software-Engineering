package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.services.OfficeVisitMutationService;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
import edu.ncsu.csc.itrust2.services.OphthalmologySurgeryService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
@Tag(name = "[UC22] 안과 수술 API")
public class ApiOphthalmologySurgeryController extends APIController {

    private final OfficeVisitService officeVisitService;
    private final LoggerUtil loggerUtil;
    private final OphthalmologySurgeryService ophthalmologySurgeryService;
    private final UserService userService;
    private final OfficeVisitMutationService officeVisitMutationService;

    @Operation(summary = "Ophthalmologist: 안과 수술 문서 생성")
    @PostMapping("/officevisits/ophthalmologySurgery")
    @PreAuthorize("hasRole('ROLE_OPH')")
    public OfficeVisit createOphthalmologySurgery(
            @Parameter(description = "DB에 저장할 정보들입니다.") @RequestBody
                    final OphthalmologySurgeryForm ophthalmologySurgeryForm) {

        return officeVisitMutationService.createForOphthalmologySurgery(ophthalmologySurgeryForm);
    }

    @GetMapping("/officevisits/ophthalmologySurgery/oph")
    @PreAuthorize("hasRole('ROLE_OPH')")
    public List<OphthalmologySurgery> listOphthalmologySurgeriesByCurrentOPH() {
        final User self = userService.findByName(loggerUtil.getCurrentUsername());
        return ophthalmologySurgeryService.findByOPH(self);
    }

    @GetMapping("/officevisits/ophthalmologySurgery/patient")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<OphthalmologySurgery> listOphthalmologySurgeriesByCurrentPatient() {
        final User self = userService.findByName(loggerUtil.getCurrentUsername());
        return ophthalmologySurgeryService.findByPatient(self);
    }
}
