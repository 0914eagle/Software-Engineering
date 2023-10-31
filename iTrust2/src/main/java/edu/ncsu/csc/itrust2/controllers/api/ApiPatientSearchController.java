package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.services.PatientSearchService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/patientSearch")
public class ApiPatientSearchController {
    private final PatientSearchService patientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public List<Patient> listPatientsByName(@RequestParam String nameQuery) {
        if (nameQuery == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientName is required");
        }

        return patientService.listByPatientName(nameQuery);
    }
}
