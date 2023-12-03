package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * This is the validated database-persisted office visit representation
 *
 * @author Kai Presler-Marshall
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "office_visit")
public class OfficeVisit extends DomainObject {

    /** The patient of this office visit */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The hcp of this office visit */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hcp_id", columnDefinition = "varchar(100)")
    private User hcp;

    /** The basic health metric data associated with this office visit. */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basichealthmetrics_id")
    private BasicHealthMetrics basicHealthMetrics;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ophthalmology_surgery_id")
    private OphthalmologySurgery ophthalmologySurgery;

    /** The date of this office visit */
    @Setter
    @NotNull @Basic
    // Allows the field to show up nicely in the database
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime date;

    /** The id of this office visit */
    @Setter // TODO: remove
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The type of this office visit */
    @Setter
    @NotNull @Enumerated(EnumType.STRING)
    private AppointmentType type;

    /** The hospital of this office visit */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hospital_id", columnDefinition = "varchar(100)")
    private Hospital hospital;

    /**
     * The set of diagnoses associated with this visits Marked transient so not serialized or saved
     * in DB If removed, serializer gets into an infinite loop
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Diagnosis> diagnoses;

    /** The notes of this office visit */
    @Setter private String notes;

    /** The appointment of this office visit */
    @Setter
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private AppointmentRequest appointment;

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Prescription> prescriptions;

    public int calculateAge(final Patient p) {
        final var dob = p.getDateOfBirth();
        final var date = getDate();
        int age = date.getYear() - dob.getYear();

        // Remove the -1 when changing the dob to OffsetDateTime
        if (date.getMonthValue() < dob.getMonthValue()) {
            age -= 1;
        } else if (date.getMonthValue() == dob.getMonthValue()) {
            if (date.getDayOfMonth() < dob.getDayOfMonth()) {
                age -= 1;
            }
        }

        return age;
    }

    public void setBasicHealthMetrics(BasicHealthMetrics basicHealthMetrics) {
        final Patient p = (Patient) getPatient();

        if (p == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient cannot be null");
        }

        if (basicHealthMetrics == null) {
            this.basicHealthMetrics = null;
            return;
        }

        if (p.getDateOfBirth() == null) {
            this.basicHealthMetrics = basicHealthMetrics;
            return; // we're done, patient can't be tested against
        }

        final var age = calculateAge(p);

        if (age < 3) {
            basicHealthMetrics.validateUnder3();
        } else if (age < 12) {
            basicHealthMetrics.validateUnder12();
        } else {
            basicHealthMetrics.validate12AndOver();
        }

        this.basicHealthMetrics = basicHealthMetrics;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        if (diagnoses == null) {
            this.diagnoses = null;
            return;
        }

        for (final Diagnosis d : diagnoses) {
            if (d.getNote().length() > 500) {
                throw new IllegalArgumentException(
                        "Dagnosis note too long (500 character max) : " + d.getNote());
            }
            if (d.getCode() == null) {
                throw new IllegalArgumentException("Diagnosis Code missing!");
            }

            d.setVisit(this);
        }
        this.diagnoses = diagnoses;
    }

    public void setOphthalmologySurgery(OphthalmologySurgery ophthalmologySurgery) {
        if (ophthalmologySurgery.getLeftVisualAcuityResult() == null
                || ophthalmologySurgery.getRightVisualAcuityResult() == null
                || ophthalmologySurgery.getLeftSphere() == null
                || ophthalmologySurgery.getRightSphere() == null
                || (ophthalmologySurgery.getRightCylinder() != null
                        && ophthalmologySurgery.getRightAxis() == null)
                || (ophthalmologySurgery.getLeftCylinder() != null
                        && ophthalmologySurgery.getLeftAxis() == null)
                || ophthalmologySurgery.getSurgeryType() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Not all necessary fields for ophthalmology surgery metrics were submitted");
        }

        this.ophthalmologySurgery = ophthalmologySurgery;
    }
}
