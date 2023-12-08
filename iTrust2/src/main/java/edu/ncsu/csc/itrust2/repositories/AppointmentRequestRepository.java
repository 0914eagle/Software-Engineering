package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.User;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

    List<AppointmentRequest> findByPatient(@NotNull User patient);

    List<AppointmentRequest> findByHcp(@NotNull User hcp);

    Optional<AppointmentRequest> findByHcpAndPatientAndDate(
            @NotNull User hcp, @NotNull User patient, @NotNull ZonedDateTime date);

    List<AppointmentRequest> findByPatientAndDateAfter(
            @NotNull User patient, @NotNull ZonedDateTime now);

    List<AppointmentRequest> findByHcpAndDateAfter(@NotNull User hcp, @NotNull ZonedDateTime now);
}
