package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.PatientForm;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.PatientService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for providing various REST API endpoints for the Patient model.
 *
 * @author Kai Presler-Marshall
 */
@RestController
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class APIPatientController extends APIController {

    private final PatientService patientService;

    private final UserService userService;

    private final LoggerUtil loggerUtil;

    /**
     * Retrieves and returns a list of all Patients stored in the system
     *
     * @return list of patients
     */
    @GetMapping("/patients")
    public List<Patient> getPatients() {
        return (List<Patient>) patientService.findAll();
    }

    /**
     * If you are logged in as a patient, then you can use this convenience lookup to find your own
     * information without remembering your id. This allows you the shorthand of not having to look
     * up the id in between.
     *
     * @return The patient object for the currently authenticated user.
     */
    @GetMapping("/patient")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity getPatient() {
        final User self = userService.findByName(loggerUtil.getCurrentUsername());
        final Patient patient = (Patient) patientService.findByName(self.getUsername());
        if (patient == null) {
            return new ResponseEntity(
                    errorResponse("Could not find a patient entry for you, " + self.getUsername()),
                    HttpStatus.NOT_FOUND);
        } else {
            loggerUtil.log(TransactionType.VIEW_DEMOGRAPHICS, self);
            return new ResponseEntity(patient, HttpStatus.OK);
        }
    }

    /**
     * Retrieves and returns the Patient with the username provided
     *
     * @param username The username of the Patient to be retrieved, as stored in the Users table
     * @return response
     */
    @GetMapping("/patients/{username}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public ResponseEntity getPatient(@PathVariable("username") final String username) {
        final Patient patient = (Patient) patientService.findByName(username);
        if (patient == null) {
            return new ResponseEntity(
                    errorResponse("No Patient found for username " + username),
                    HttpStatus.NOT_FOUND);
        } else {
            loggerUtil.log(
                    TransactionType.PATIENT_DEMOGRAPHICS_VIEW,
                    loggerUtil.getCurrentUsername(),
                    username,
                    "HCP retrieved demographics for patient with username " + username);
            return new ResponseEntity(patient, HttpStatus.OK);
        }
    }

    /**
     * Updates the Patient with the id provided by overwriting it with the new Patient record that
     * is provided. If the ID provided does not match the ID set in the Patient provided, the update
     * will not take place
     *
     * @param id The username of the Patient to be updated
     * @param patientF The updated Patient to save
     * @return response
     */
    @PutMapping("/patients/{id}")
    public ResponseEntity updatePatient(
            @PathVariable final String id, @RequestBody final PatientForm patientF) {
        // check that the user is an HCP or a patient with username equal to id
        boolean userEdit = false; // true if user edits his or her own
        // demographics, false if hcp edits them
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final SimpleGrantedAuthority hcp = new SimpleGrantedAuthority("ROLE_HCP");
        try {
            userEdit = !auth.getAuthorities().contains(hcp);

            if (!auth.getName().equals(id) && userEdit) {
                return new ResponseEntity(
                        errorResponse("You do not have permission to edit this record"),
                        HttpStatus.UNAUTHORIZED);
            }

        } catch (final Exception e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        try {
            final Patient patient = (Patient) patientService.findByName(id);

            // Shouldn't be possible but let's check anyways
            if (null == patient) {
                return new ResponseEntity(errorResponse("Patient not found"), HttpStatus.NOT_FOUND);
            }

            patient.update(patientF);

            final User dbPatient = patientService.findByName(id);
            if (null == dbPatient) {
                return new ResponseEntity(
                        errorResponse("No Patient found for id " + id), HttpStatus.NOT_FOUND);
            }
            patientService.save(patient);

            // Log based on whether user or hcp edited demographics
            if (userEdit) {
                loggerUtil.log(
                        TransactionType.EDIT_DEMOGRAPHICS,
                        loggerUtil.getCurrentUsername(),
                        "User with username "
                                + patient.getUsername()
                                + "updated their demographics");
            } else {
                loggerUtil.log(
                        TransactionType.PATIENT_DEMOGRAPHICS_EDIT,
                        loggerUtil.getCurrentUsername(),
                        patient.getUsername(),
                        "HCP edited demographics for patient with username "
                                + patient.getUsername());
            }
            return new ResponseEntity(patient, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse(
                            "Could not update "
                                    + patientF.toString()
                                    + " because of "
                                    + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get the patient's zip code to autofill Find Expert Form
     *
     * @return Response entity with status and response data (zip or error message)
     */
    @GetMapping("/patient/findexperts/getzip")
    @PreAuthorize("hasRole( 'ROLE_PATIENT')")
    public ResponseEntity getPatientZip() {
        final String user = loggerUtil.getCurrentUsername();
        if (user == null) {
            return new ResponseEntity(errorResponse("Patient not found"), HttpStatus.NOT_FOUND);
        }
        final String zip = ((Patient) patientService.findByName(user)).getZip();
        if (zip == null) {
            return new ResponseEntity(
                    errorResponse("Patient does not have zip stored"), HttpStatus.NO_CONTENT);
        } else {
            final String[] zipParts = zip.split("-");
            return new ResponseEntity(zipParts, HttpStatus.OK);
        }
    }
}
