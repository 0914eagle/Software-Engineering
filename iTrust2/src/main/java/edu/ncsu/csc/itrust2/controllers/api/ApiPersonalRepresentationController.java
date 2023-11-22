package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.services.PersonalRepresentationService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[UC16] 환자 대리인 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ApiPersonalRepresentationController {
    private final PersonalRepresentationService personalRepresentationService;
    private final LoggerUtil loggerUtil;

    @Operation(summary = "Patient: 자신의 대리인 목록 조회")
    @GetMapping("/personalRepresentatives")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<PersonalRepresentation> listPersonalRepresentativesByCurrentPatient() {
        final String patientName = loggerUtil.getCurrentUsername();
        return personalRepresentationService.listByPatient(patientName);
    }

    @Operation(summary = "Patient: 자신이 대리하는 환자 목록 조회")
    @GetMapping("/representingPatients")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<PersonalRepresentation> listPersonalRepresentingByCurrentPatient() {
        final String patientName = loggerUtil.getCurrentUsername();
        return personalRepresentationService.listByRepresenting(patientName);
    }

    @Operation(summary = "HCP: 특정 환자의 대리인 목록 조회")
    @GetMapping("/patients/{patientUsername}/representingPatients")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<PersonalRepresentation> listPersonalRepresentingByPatientId(
            @Parameter(description = "조회할 환자의 username 입니다.") @PathVariable
                    final String patientUsername) {
        return personalRepresentationService.listByRepresenting(patientUsername);
    }

    @Operation(summary = "HCP: 특정 환자가 대리하는 환자 목록 조회")
    @GetMapping("/patients/{patientUsername}/personalRepresentatives")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<PersonalRepresentation> listPersonalRepresentativesByPatientId(
            @Parameter(description = "조회할 환자의 username 입니다.") @PathVariable
                    final String patientUsername) {
        return personalRepresentationService.listByPatient(patientUsername);
    }

    @PostMapping("/representatives/{representativeUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void setPersonalRepresentative(@PathVariable String representativeUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.setPersonalRepresentation(
                currentUsername, representativeUsername);
    }

    @PostMapping("/patients/{patientUsername}/representatives/{representativeUsername}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public void setPersonalRepresentation(
            @PathVariable String patientUsername, @PathVariable String representativeUsername) {
        personalRepresentationService.setPersonalRepresentation(
                patientUsername, representativeUsername);
    }

    @DeleteMapping("/personalRepresentatives/{personalRepresentativeUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void cancelPersonalRepresentative(@PathVariable String personalRepresentativeUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.cancelPersonalRepresentation(currentUsername, personalRepresentativeUsername);
    }

    @DeleteMapping("/representingPatients/{representingPatientUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void cancelRepresentingPatient(@PathVariable String representingPatientUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.cancelPersonalRepresentation(representingPatientUsername, currentUsername);
    }
}
