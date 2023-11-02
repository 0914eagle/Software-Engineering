package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.*;

import java.util.*;

import edu.ncsu.csc.itrust2.services.EmergencyPatientService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/emergency/patients")
public class ApiEmergencyPatientController {
    private final EmergencyPatientService emergencyPatientService;
    @GetMapping("/{patientName}")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public ResponseEntity<PatientInfo> getPatientInfo(@PathVariable String patientName) {
        PatientInfo patientInfo = emergencyPatientService.getPatientInformation(patientName);
        return ResponseEntity.ok(patientInfo);
    }

    @GetMapping("/{patientName}/recentDiagnoses")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public ResponseEntity<List<Diagnosis>> getDiagnosesIn60Days(@PathVariable String patientName) {
        List<Diagnosis> diagnoses =
                emergencyPatientService.getRecentDiagnoses(patientName);
        return ResponseEntity.ok(diagnoses);
    }

    @GetMapping("/{patientName}/recentPrescriptions")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public ResponseEntity<List<Prescription>> getPrescriptionsIn90Days(@PathVariable String patientName) {
        List<Prescription> prescriptions =
                emergencyPatientService.getRecentPrescriptions(patientName);
        return ResponseEntity.ok(prescriptions);
    }
}
