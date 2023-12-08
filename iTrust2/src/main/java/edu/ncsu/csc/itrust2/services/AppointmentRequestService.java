package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.AppointmentRequestForm;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.Status;
import edu.ncsu.csc.itrust2.repositories.AppointmentRequestRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class AppointmentRequestService extends Service {

    private final AppointmentRequestRepository appointmentRequestRepository;

    private final UserService userService;

    @Override
    protected JpaRepository getRepository() {
        return appointmentRequestRepository;
    }

    public List<AppointmentRequest> findByPatient(final User patient) {
        return appointmentRequestRepository.findByPatientAndDateAfter(patient, ZonedDateTime.now());
    }

    public List<AppointmentRequest> findByHcp(final User hcp) {
        return appointmentRequestRepository.findByHcpAndDateAfter(hcp, ZonedDateTime.now());
    }

    public Optional<AppointmentRequest> findByHcpAndPatientAndDate(
            final User hcp, final User patient, final ZonedDateTime date) {
        return appointmentRequestRepository.findByHcpAndPatientAndDate(hcp, patient, date);
    }

    public AppointmentRequest build(final AppointmentRequestForm raf) {
        final AppointmentRequest ar = new AppointmentRequest();

        if (raf.getId() != null) {
            ar.setId(Long.parseLong(raf.getId()));
        }

        ar.setPatient(userService.findByName(raf.getPatient()));
        ar.setHcp(userService.findByName(raf.getHcp()));
        ar.setComments(raf.getComments());

        final ZonedDateTime requestDate = ZonedDateTime.parse(raf.getDate());
        if (requestDate.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException(
                    "Cannot request an appointment before the current time");
        }
        ar.setDate(requestDate);

        Status s = null;
        try {
            s = Status.valueOf(raf.getStatus());
        } catch (final NullPointerException npe) {
            s = Status.PENDING; /*
                                 * Incoming AppointmentRequests will come in
                                 * from the form with no status. Set status to
                                 * Pending until it is adjusted further
                                 */
        }
        ar.setStatus(s);
        AppointmentType at = null;
        try {
            at = AppointmentType.valueOf(raf.getType());
        } catch (final NullPointerException npe) {
            at = AppointmentType.GENERAL_CHECKUP; /*
                                                   * If for some reason we don't
                                                   * have a type, default to
                                                   * general checkup
                                                   */
        }
        ar.setType(at);

        ar.setName(raf.getName());
        ar.setAbbreviation(raf.getAbbreviation());
        ar.setCptCode(raf.getCptCode());
        ar.setLongComment(raf.getLongComment());

        return ar;
    }
}
