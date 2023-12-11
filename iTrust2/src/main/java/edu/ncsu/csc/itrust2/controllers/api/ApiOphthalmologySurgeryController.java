package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.forms.UpdateOfficeVisitForm;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.EmailService;
import edu.ncsu.csc.itrust2.services.OfficeVisitMutationService;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
import edu.ncsu.csc.itrust2.services.OphthalmologySurgeryService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final EmailService emailService;

    @Operation(summary = "Ophthalmologist: 안과 수술 문서 생성")
    @PostMapping("/officevisits/ophthalmologySurgery")
    @PreAuthorize("hasRole('ROLE_OPH')")
    public OfficeVisit createOphthalmologySurgery(
            @Parameter(description = "DB에 저장할 정보들입니다.") @RequestBody
                    final OphthalmologySurgeryForm ophthalmologySurgeryForm) {
        OfficeVisit temp =
                officeVisitMutationService.createForOphthalmologySurgery(ophthalmologySurgeryForm);
        String patientName = temp.getPatient().getUsername();
        String currentUserName = loggerUtil.getCurrentUsername();
        loggerUtil.log(TransactionType.OPHTHALMOLOGY_ADD_SURGERY, currentUserName, patientName);
        loggerUtil.log(TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE, patientName);
        emailService.sendEmail(
                "iTrust2 System",
                patientName,
                "Your Ophthalmology Surgery Office Visit has been created",
                "Your Ophthalmology Surgery Office Visit has been created. Please log in to iTrust2"
                        + " to view the creation.");
        return temp;
    }

    @Operation(summary = "Ophthalmologist: 안과 수술 문서 수정")
    @PatchMapping("/officevisits/ophthalmologySurgery/{id}")
    @PreAuthorize("hasRole('ROLE_OPH')")
    public OfficeVisit updateOphthalmologySurgery(
            @Parameter(description = "수정할 Office Visit의 ID입니다.") @PathVariable final Long id,
            @Parameter(description = "수정할 정보들입니다.") @RequestBody
                    final UpdateOfficeVisitForm updateOfficeVisitForm) {
        OfficeVisit temp =
                officeVisitMutationService.updateForOphthalmologySurgery(id, updateOfficeVisitForm);
        String patientName = temp.getPatient().getUsername();
        String currentUserName = loggerUtil.getCurrentUsername();
        loggerUtil.log(TransactionType.OPHTHALMOLOGY_EDIT_SURGERY, currentUserName, patientName);
        loggerUtil.log(TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE, patientName);
        emailService.sendEmail(
                "iTrust2 System",
                patientName,
                "Your Ophthalmology Surgery Office Visit has been updated",
                "Your Ophthalmology Surgery Office Visit has been updated. Please log in to iTrust2"
                        + " to view the changes.");
        return temp;
    }
}
