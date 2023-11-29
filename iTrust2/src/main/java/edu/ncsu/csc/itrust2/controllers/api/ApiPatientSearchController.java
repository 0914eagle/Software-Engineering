package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.services.PatientSearchService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "[UC15] 환자 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/patientSearch")
public class ApiPatientSearchController {
    private final PatientSearchService patientService;

    @Operation(summary = "HCP, ER: 환자를 이름으로 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER', 'ROLE_PATIENT')")
    public List<Patient> listPatientsByName(
            @Parameter(description = "조회할 환자의 이름입니다.") @RequestParam String nameQuery) {
        if (nameQuery == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientName is required");
        }

        return patientService.listByPatientName(nameQuery);
    }
}
