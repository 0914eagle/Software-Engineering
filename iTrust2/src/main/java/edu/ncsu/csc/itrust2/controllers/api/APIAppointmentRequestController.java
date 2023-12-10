package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.AppointmentRequestForm;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.Status;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.AppointmentRequestService;
import edu.ncsu.csc.itrust2.services.EmailService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class that provides REST API endpoints for the AppointmentRequest model. In all requests made to
 * this controller, the {id} provided is a numeric ID that is the primary key of the appointment
 * request in question
 *
 * @author Kai Presler-Marshall
 * @author Matt Dzwonczyk
 */
@Tag(name = "[UC20] 예약 요청 생성 API (일반/안과)")
@RestController
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class APIAppointmentRequestController extends APIController {

    private final AppointmentRequestService service;

    private final LoggerUtil loggerUtil;

    private final UserService userService;

    private final EmailService emailService;

    /**
     * Retrieves a list of all AppointmentRequests in the database
     *
     * @return list of appointment requests
     */
    @GetMapping("/appointmentrequests")
    @PreAuthorize("hasAnyRole('ROLE_HCP')")
    public List<AppointmentRequest> getAppointmentRequests() {
        return (List<AppointmentRequest>) service.findAll();
    }

    /**
     * Retrieves the AppointmentRequest specified by the username provided
     *
     * @return list of appointment requests for the logged in patient
     */
    @GetMapping("/appointmentrequest")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT')")
    public List<AppointmentRequest> getAppointmentRequestsForPatient() {
        final User patient = userService.findByName(loggerUtil.getCurrentUsername());
        final List<AppointmentRequest> requests = service.findByPatient(patient);

        requests.stream()
                .map(AppointmentRequest::getHcp)
                .distinct()
                .forEach(
                        hcp -> {
                            List<AppointmentRequest> patientRequests =
                                    requests.stream()
                                            .filter(request -> request.getHcp().equals(hcp))
                                            .toList();
                            Set<String> logged = new HashSet<>();
                            patientRequests.stream()
                                    .map(AppointmentRequest::getType)
                                    .distinct()
                                    .forEach(
                                            Type -> {
                                                if (Type == AppointmentType.GENERAL_CHECKUP) {
                                                    loggerUtil.log(
                                                            TransactionType
                                                                    .APPOINTMENT_REQUEST_VIEWED,
                                                            patient,
                                                            hcp);
                                                } else {
                                                    if (!logged.contains("oph")) {
                                                        logged.add("oph");
                                                        loggerUtil.log(
                                                                TransactionType
                                                                        .PATIENT_VIEWS_APPT_REQ,
                                                                patient,
                                                                hcp);
                                                    }
                                                }
                                            });
                        });

        return requests.stream().filter(e -> e.getStatus().equals(Status.PENDING)).toList();
    }

    /**
     * Retrieves the AppointmentRequest specified by the username provided
     *
     * @return list of appointment requests for the logged in hcp
     */
    @GetMapping("/appointmentrequestForHCP")
    @PreAuthorize("hasAnyRole('ROLE_HCP')")
    public List<AppointmentRequest> getAppointmentRequestsForHCP() {

        final User hcp = userService.findByName(loggerUtil.getCurrentUsername());

        return service.findByHcp(hcp).stream()
                .filter(e -> e.getStatus().equals(Status.PENDING))
                .toList();
    }

    /**
     * Retrieves the AppointmentRequest specified by the ID provided
     *
     * @param id The (numeric) ID of the AppointmentRequest desired
     * @return The AppointmentRequest corresponding to the ID provided or HttpStatus.NOT_FOUND if no
     *     such AppointmentRequest could be found
     */
    @GetMapping("/appointmentrequests/{id}")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_PATIENT')")
    public ResponseEntity getAppointmentRequest(@PathVariable("id") final Long id) {
        final AppointmentRequest request = (AppointmentRequest) service.findById(id);
        if (null != request) {
            if (request.getType().equals(AppointmentType.GENERAL_CHECKUP)) {
                loggerUtil.log(
                        TransactionType.APPOINTMENT_REQUEST_VIEWED,
                        request.getPatient(),
                        request.getHcp());
            } else {
                loggerUtil.log(
                        TransactionType.OPH_VIEWS_APPT_REQ, request.getPatient(), request.getHcp());
            }

            /* Patient can't look at anyone else's requests */
            final User self = userService.findByName(loggerUtil.getCurrentUsername());
            if (self.getRoles().contains(Role.ROLE_PATIENT) && !request.getPatient().equals(self)) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        }
        return null == request
                ? new ResponseEntity(
                        errorResponse("No AppointmentRequest found for id " + id),
                        HttpStatus.NOT_FOUND)
                : new ResponseEntity(request, HttpStatus.OK);
    }

    /**
     * Creates an AppointmentRequest from the RequestBody provided. Record is automatically saved in
     * the database.
     *
     * @param requestForm The AppointmentRequestForm to be parsed into an AppointmentRequest and
     *     stored
     * @return The parsed and validated AppointmentRequest created from the Form provided,
     *     HttpStatus.CONFLICT if a Request already exists with the ID of the provided request, or
     *     HttpStatus.BAD_REQUEST if another error occurred while parsing or saving the Request
     *     provided
     */
    @Operation(summary = "Patient: 예약 요청을 DB에 저장")
    @PostMapping("/appointmentrequests")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity createAppointmentRequest(
            @Parameter(description = "새로 만들 예약 요청입니다.") @RequestBody
                    final AppointmentRequestForm requestForm) {
        try {
            final AppointmentRequest request = service.build(requestForm);
            if (null != service.findById(request.getId())) {
                return new ResponseEntity(
                        errorResponse(
                                "AppointmentRequest with the id "
                                        + request.getId()
                                        + " already exists"),
                        HttpStatus.CONFLICT);
            }
            service.save(request);
            switch (request.getType()) {
                case GENERAL_CHECKUP:
                    loggerUtil.log(
                            TransactionType.APPOINTMENT_REQUEST_SUBMITTED,
                            request.getPatient(),
                            request.getHcp());
                    break;

                case GENERAL_OPHTHALMOLOGY:
                    loggerUtil.log(
                            TransactionType.PATIENT_REQ_OPH_APPT,
                            request.getPatient(),
                            request.getHcp());
                    break;

                case OPHTHALMOLOGY_SURGERY:
                    loggerUtil.log(
                            TransactionType.PATIENT_REQ_OPH_SURG,
                            request.getPatient(),
                            request.getHcp());
                    break;
            }
            return new ResponseEntity(request, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse(
                            "Error occurred while validating or saving "
                                    + requestForm.toString()
                                    + " because of "
                                    + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the AppointmentRequest with the id provided. This will remove all traces from the
     * system and cannot be reversed.
     *
     * @param id The id of the AppointmentRequest to delete
     * @return response
     */
    @DeleteMapping("/appointmentrequests/{id}")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_PATIENT')")
    public ResponseEntity deleteAppointmentRequest(@PathVariable final Long id) {
        final AppointmentRequest request = (AppointmentRequest) service.findById(id);
        if (null == request) {
            return new ResponseEntity(
                    errorResponse("No AppointmentRequest found for id " + id),
                    HttpStatus.NOT_FOUND);
        }

        /* Patient can't look at anyone else's requests */
        final User self = userService.findByName(loggerUtil.getCurrentUsername());
        if (self.getRoles().contains(Role.ROLE_PATIENT) && !request.getPatient().equals(self)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        try {
            service.delete(request);
            switch (request.getType()) {
                case GENERAL_CHECKUP:
                    loggerUtil.log(
                            TransactionType.APPOINTMENT_REQUEST_DELETED,
                            request.getPatient(),
                            request.getHcp());
                    break;

                case GENERAL_OPHTHALMOLOGY:
                    loggerUtil.log(
                            TransactionType.PATIENT_DELETES_OPH_APPT_REQUEST,
                            request.getPatient(),
                            request.getHcp());
                    break;

                case OPHTHALMOLOGY_SURGERY:
                    loggerUtil.log(
                            TransactionType.PATIENT_DELETES_OPH_SURG,
                            request.getPatient(),
                            request.getHcp());
                    break;
            }
            return new ResponseEntity(id, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse("Could not delete " + request + " because of " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates the AppointmentRequest with the id provided by overwriting it with the new
     * AppointmentRequest that is provided. If the ID provided does not match the ID set in the
     * AppointmentRequest provided, the update will not take place
     *
     * @param id The ID of the AppointmentRequest to be updated
     * @param requestF The updated AppointmentRequestForm to parse, validate, and save
     * @return The AppointmentRequest that is created from the Form that is provided
     */
    @PutMapping("/appointmentrequests/{id}")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_PATIENT')")
    public ResponseEntity updateAppointmentRequest(
            @PathVariable final Long id, @RequestBody final AppointmentRequestForm requestF) {
        try {
            final AppointmentRequest request = service.build(requestF);
            request.setId(id);

            if (null != request.getId() && !id.equals(request.getId())) {
                return new ResponseEntity(
                        errorResponse(
                                "The ID provided does not match the ID of the AppointmentRequest"
                                        + " provided"),
                        HttpStatus.CONFLICT);
            }

            /* Patient can't look at anyone else's requests */
            final User self = userService.findByName(loggerUtil.getCurrentUsername());
            if (self.getRoles().contains(Role.ROLE_PATIENT) && !request.getPatient().equals(self)) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }

            final AppointmentRequest dbRequest = (AppointmentRequest) service.findById(id);
            if (null == dbRequest) {
                return new ResponseEntity(
                        errorResponse("No AppointmentRequest found for id " + id),
                        HttpStatus.NOT_FOUND);
            }

            service.save(request);

            switch (request.getType()) {
                case GENERAL_CHECKUP:
                    loggerUtil.log(
                            TransactionType.APPOINTMENT_REQUEST_UPDATED,
                            request.getPatient(),
                            request.getHcp());
                    break;

                case GENERAL_OPHTHALMOLOGY:
                case OPHTHALMOLOGY_SURGERY:
                    loggerUtil.log(
                            TransactionType.OPH_APPT_REQ_UPDATED,
                            request.getPatient(),
                            request.getHcp());
                    break;
            }

            if (request.getStatus().getCode() == Status.APPROVED.getCode()) {
                switch (request.getType()) {
                    case GENERAL_CHECKUP:
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_REQUEST_APPROVED,
                                request.getPatient(),
                                request.getHcp());
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE,
                                request.getPatient());
                        emailService.sendEmail(
                                "iTrust2 System",
                                request.getPatient().getUsername(),
                                "Your appointment request has been approved",
                                "Your appointment request has been approved. Please log in to"
                                        + " iTrust2 to view the approval.");
                        break;

                    case GENERAL_OPHTHALMOLOGY:
                        loggerUtil.log(
                                TransactionType.OPH_APPT_REQ_APPROVED,
                                request.getPatient(),
                                request.getHcp());
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE,
                                request.getPatient());
                        emailService.sendEmail(
                                "iTrust2 System",
                                request.getPatient().getUsername(),
                                "Your general ophthalmology request has been approved",
                                "Your general ophthalmology request has been approved. Please log"
                                        + " in to iTrust2 to view the approval.");
                        break;

                    case OPHTHALMOLOGY_SURGERY:
                        loggerUtil.log(
                                TransactionType.OPH_SURG_REQ_APPROVED,
                                request.getPatient(),
                                request.getHcp());
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE,
                                request.getPatient());
                        emailService.sendEmail(
                                "iTrust2 System",
                                request.getPatient().getUsername(),
                                "Your ophthalmology surgery request has been approved",
                                "Your ophthalmology surgery request has been approved. Please log"
                                        + " in to iTrust2 to view the approval.");
                        break;
                }
            } else {
                switch (request.getType()) {
                    case GENERAL_CHECKUP:
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_REQUEST_DENIED,
                                request.getPatient(),
                                request.getHcp());
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE,
                                request.getPatient());
                        emailService.sendEmail(
                                "iTrust2 System",
                                request.getPatient().getUsername(),
                                "Your appointment request has been rejected",
                                "Your appointment request has been rejected.");
                        break;

                    case GENERAL_OPHTHALMOLOGY:
                        loggerUtil.log(
                                TransactionType.OPH_APPT_REQ_DENIED,
                                request.getPatient(),
                                request.getHcp());
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE,
                                request.getPatient());
                        emailService.sendEmail(
                                "iTrust2 System",
                                request.getPatient().getUsername(),
                                "Your general ophthalmology request has been rejected",
                                "Your general ophthalmology request has been rejected.");
                        break;

                    case OPHTHALMOLOGY_SURGERY:
                        loggerUtil.log(
                                TransactionType.OPH_SURG_REQ_DENIED,
                                request.getPatient(),
                                request.getHcp());
                        loggerUtil.log(
                                TransactionType.APPOINTMENT_AND_SURGERY_REQUEST_EMAIL_NOTICE,
                                request.getPatient());
                        emailService.sendEmail(
                                "iTrust2 System",
                                request.getPatient().getUsername(),
                                "Your ophthalmology surgery request has been rejected",
                                "Your ophthalmology surgery request has been rejected.");
                        break;
                }
            }

            return new ResponseEntity(request, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse(
                            "Could not update "
                                    + requestF.toString()
                                    + " because of "
                                    + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * View Appointments will retrieve and display all appointments for the logged-in HCP that are
     * in "approved" status
     *
     * @return The page to display for the user
     */
    @GetMapping("/viewAppointments")
    @PreAuthorize("hasAnyRole('ROLE_HCP')")
    public List<AppointmentRequest> upcomingAppointments() {
        final User hcp = userService.findByName(loggerUtil.getCurrentUsername());

        final List<AppointmentRequest> appointment =
                service.findByHcp(hcp).stream()
                        .filter(e -> e.getStatus().equals(Status.APPROVED))
                        .toList();
        /* Log the event */
        if (hcp.getRoles().contains(Role.ROLE_OPH) || hcp.getRoles().contains(Role.ROLE_OD)) {
            appointment.stream()
                    .map(AppointmentRequest::getPatient)
                    .distinct()
                    .forEach(
                            e ->
                                    loggerUtil.log(
                                            TransactionType.OPH_VIEW_UPCOMING_APPOINTMENT, hcp, e));
        } else {
            appointment.stream()
                    .map(AppointmentRequest::getPatient)
                    .distinct()
                    .forEach(
                            e ->
                                    loggerUtil.log(
                                            TransactionType.APPOINTMENT_REQUEST_VIEWED, hcp, e));
        }
        return appointment;
    }
}
