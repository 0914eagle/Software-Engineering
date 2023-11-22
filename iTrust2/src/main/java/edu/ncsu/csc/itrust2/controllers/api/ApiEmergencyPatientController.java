package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.records.EmergencyPatientInfo;
import edu.ncsu.csc.itrust2.services.EmergencyPatientService;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[UC15] 응급 상황 환자 상세 정보 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/emergency/patients")
public class ApiEmergencyPatientController {
    private final EmergencyPatientService emergencyPatientService;

    @Operation(summary = "HCP, ER: 환자의 응급기록 상세 조회")
    @GetMapping("/{patientName}")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public EmergencyPatientInfo getPatientInfo(
            @Parameter(description = "조회활 환자의 username") @PathVariable String patientName) {
        return emergencyPatientService.getPatientInformation(patientName);
    }

    @Operation(summary = "HCP, ER: 지난 60일 이내의 단기 진단에 대한 모든 진단 코드 목록 조회")
    @GetMapping("/{patientName}/recentDiagnoses")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public List<Diagnosis> getDiagnosesIn60Days(
            @Parameter(description = "조회활 환자의 username") @PathVariable String patientName) {
        return emergencyPatientService.getRecentDiagnoses(patientName);
    }

    @Operation(summary = "HCP, ER: 지난 90일 이내의 모든 처방전 목록 조회")
    @GetMapping("/{patientName}/recentPrescriptions")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public List<Prescription> getPrescriptionsIn90Days(
            @Parameter(description = "조회활 환자의 username") @PathVariable String patientName) {
        return emergencyPatientService.getRecentPrescriptions(patientName);
    }
}
