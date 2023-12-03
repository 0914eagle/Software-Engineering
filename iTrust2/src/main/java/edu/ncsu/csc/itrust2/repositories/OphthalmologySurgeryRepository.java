package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.User;

import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OphthalmologySurgeryRepository extends JpaRepository<OphthalmologySurgery, Long> {
    List<OphthalmologySurgery> findByPatient(@NotNull User patient);

    List<OphthalmologySurgery> findByHcp(@NotNull User hcp);
}
