package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.models.security.LogEntry;
import edu.ncsu.csc.itrust2.services.AppointmentRequestService;
import edu.ncsu.csc.itrust2.services.BasicHealthMetricsService;
import edu.ncsu.csc.itrust2.services.DiagnosisService;
import edu.ncsu.csc.itrust2.services.PatientService;
import edu.ncsu.csc.itrust2.services.PersonalRepresentationService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "[UC16] 환자 대리인 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ApiPersonalRepresentationController {
    private final PersonalRepresentationService personalRepresentationService;
    private final DiagnosisService diagnosisService;
    private final AppointmentRequestService appointmentRequestService;
    private final BasicHealthMetricsService basicHealthMetricsService;
    private final PatientService patientService;
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

    @Operation(summary = "Patient: 자신의 대리인 지정")
    @PostMapping("/personalRepresentatives/{personalRepresentativeUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void setPersonalRepresentative(
            @Parameter(description = "대리인으로 지정할 환자의 username 입니다.") @PathVariable
                    final String personalRepresentativeUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.setPersonalRepresentation(
                currentUsername, personalRepresentativeUsername);
    }

    @Operation(summary = "HCP: 특정 환자의 대리인 지정")
    @PostMapping(
            "/patients/{patientUsername}/personalRepresentatives/{personalRepresentativeUsername}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public void setPersonalRepresentation(
            @Parameter(description = "대리인을 지정할 특정 환자의 username 입니다.") @PathVariable
                    final String patientUsername,
            @Parameter(description = "대리인으로 지정할 환자의 username 입니다.") @PathVariable
                    final String personalRepresentativeUsername) {
        personalRepresentationService.setPersonalRepresentation(
                patientUsername, personalRepresentativeUsername);
    }

    @Operation(summary = "Patient: 자신의 대리인 지정 해제")
    @DeleteMapping("/personalRepresentatives/{personalRepresentativeUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void cancelPersonalRepresentative(
            @Parameter(description = "지정 해제할 대리인의 username") @PathVariable
                    String personalRepresentativeUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.cancelPersonalRepresentation(
                currentUsername, personalRepresentativeUsername);
    }

    @Operation(summary = "Patient: 자신이 대리하고 있는 환자 지정 해제")
    @DeleteMapping("/representingPatients/{representingPatientUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void cancelRepresentingPatient(
            @Parameter(description = "지정 해제할 대리하고 있는 환자의 username") @PathVariable
                    String representingPatientUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.cancelPersonalRepresentation(
                representingPatientUsername, currentUsername);
    }

    @Operation(summary = "Patient: 특정 환자의 logs 목록 조회")
    @GetMapping("/representingPatients/{representingPatientUsername}/logs")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<LogEntry> listPatientLogs(
            @Parameter(description = "조회할 환자의 username") @PathVariable
                    String representingPatientUsername) {

        Patient patient = (Patient) patientService.findByName(representingPatientUsername);
        String currentUsername = loggerUtil.getCurrentUsername();

        if (!personalRepresentationService.isRepresentative(
                currentUsername, representingPatientUsername)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied. 대리인 관계의 환자가 아닙니다.");
        }
        return loggerUtil.getAllForUser(representingPatientUsername);
    }

    @Operation(summary = "Patient: 특정 환자의 기본 건강 medical records 목록 조회")
    @GetMapping("/representingPatients/{representingPatientUsername}/medicalRecords")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<BasicHealthMetrics> listPatientMedicalRecords(
            @Parameter(description = "조회할 환자의 username") @PathVariable
                    String representingPatientUsername) {

        Patient patient = (Patient) patientService.findByName(representingPatientUsername);
        String currentUsername = loggerUtil.getCurrentUsername();

        if (!personalRepresentationService.isRepresentative(
                currentUsername, representingPatientUsername)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied. 대리인 관계의 환자가 아닙니다.");
        }
        return basicHealthMetricsService.findByPatient(patient);
    }

    @Operation(summary = "Patient: 특정 환자의 diagnosis 목록 조회")
    @GetMapping("/representingPatients/{representingPatientUsername}/diagnosis")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<Diagnosis> listPatientDiagnoses(
            @Parameter(description = "조회할 환자의 username") @PathVariable
                    String representingPatientUsername) {

        Patient patient = (Patient) patientService.findByName(representingPatientUsername);
        String currentUsername = loggerUtil.getCurrentUsername();

        if (!personalRepresentationService.isRepresentative(
                currentUsername, representingPatientUsername)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied. 대리인 관계의 환자가 아닙니다.");
        }
        return diagnosisService.findByPatient(patient);
    }

    @Operation(summary = "Patient: 특정 환자의 appointments 목록 조회")
    @GetMapping("/representingPatients/{representingPatientUsername}/appointments")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<AppointmentRequest> listPatientAppointments(
            @Parameter(description = "조회할 환자의 username") @PathVariable
                    String representingPatientUsername) {

        Patient patient = (Patient) patientService.findByName(representingPatientUsername);
        String currentUsername = loggerUtil.getCurrentUsername();

        if (!personalRepresentationService.isRepresentative(
                currentUsername, representingPatientUsername)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied. 대리인 관계의 환자가 아닙니다.");
        }
        return appointmentRequestService.findByPatient(patient);
    }
}
