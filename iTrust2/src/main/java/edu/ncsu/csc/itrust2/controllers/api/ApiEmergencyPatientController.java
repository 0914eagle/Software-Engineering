package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.services.EmergencyPatientService;

import java.util.*;

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
    public PatientInfo getPatientInfo(@PathVariable String patientName) {
        return emergencyPatientService.getPatientInformation(patientName);
    }

    @GetMapping("/{patientName}/recentDiagnoses")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public List<Diagnosis> getDiagnosesIn60Days(@PathVariable String patientName) {
        return emergencyPatientService.getRecentDiagnoses(patientName);
    }

    @GetMapping("/{patientName}/recentPrescriptions")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public List<Prescription> getPrescriptionsIn90Days(@PathVariable String patientName) {
        return emergencyPatientService.getRecentPrescriptions(patientName);
    }
}
