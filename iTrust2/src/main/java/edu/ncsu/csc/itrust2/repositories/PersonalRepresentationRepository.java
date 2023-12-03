package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepresentationRepository
        extends JpaRepository<PersonalRepresentation, Long> {
    List<PersonalRepresentation> findAllByPatient(Patient patient);

    List<PersonalRepresentation> findAllByPersonalRepresentative(Patient personalRepresentative);

    PersonalRepresentation findByPatientAndPersonalRepresentative(
            Patient patient, Patient representative);

    @Query("SELECT COUNT(pr) > 0 FROM PersonalRepresentation pr WHERE pr.patient.username = : currentUsername AND pr.personalRepresentative.username = :currentUsername")
    boolean isRepresentative(@Param("username") String username, @Param("username") String currentUsername);
}
