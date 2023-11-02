package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.User;

import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeVisitRepository extends JpaRepository<OfficeVisit, Long> {

    List<OfficeVisit> findByHcp(@NotNull User hcp);

    List<OfficeVisit> findByPatient(@NotNull User patient);

    List<OfficeVisit> findByHcpAndPatient(@NotNull User hcp, @NotNull User patient);

    List<OfficeVisit> findByDateBetweenAndPatientOrderByDateDesc(
            @NotNull ZonedDateTime begin, @NotNull ZonedDateTime end, @NotNull User patient);
}
