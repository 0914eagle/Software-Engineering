package edu.ncsu.csc.itrust2.repositories;
import java.util.List;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

import edu.ncsu.csc.itrust2.models.PersonalRepresentation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepresentationRepository extends JpaRepository<PersonalRepresentation, Long> {
    List<PersonalRepresentation> findAllByPatient(Patient patient);
    List<PersonalRepresentation> findALLByPersonalRepresentative(Patient patient);
}
