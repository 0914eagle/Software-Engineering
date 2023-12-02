package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;

import java.util.regex.Pattern;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ophthalmology_surgery")
public class OphthalmologySurgery extends DomainObject {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull @Setter private Integer leftVisualAcuityResult;

    @NotNull @Setter private Integer rightVisualAcuityResult;

    @NotNull @Setter private Float leftSphere;

    @NotNull @Setter private Float rightSphere;

    @Setter private Float leftCylinder;

    @Setter private Float rightCylinder;

    @Setter private Integer leftAxis;

    @Setter private Integer rightAxis;

    @Setter private OphthalmologySurgeryType surgeryType;

    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The hcp of this office visit */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hcp_id", columnDefinition = "varchar(100)")
    private User hcp;

    public boolean checkVisualAcuityResultValid(final Integer visualAcuityResult) {
        if (visualAcuityResult == null) {
            throw new IllegalArgumentException("visual acuity result cannot be null");
        }
        if (leftVisualAcuityResult < 20 || leftVisualAcuityResult > 200) {
            throw new IllegalArgumentException(
                    "visual acuity result must be the value between 20 and 200.");
        }
        return true;
    }

    public void setLeftVisualAcuityResult(final Integer leftVisualAcuityResult) {
        boolean flag = checkVisualAcuityResultValid(leftVisualAcuityResult);
        if (flag) {
            this.leftVisualAcuityResult = leftVisualAcuityResult;
        }
    }

    public void setRightVisualAcuityResult(final Integer rightVisualAcuityResult) {
        boolean flag = checkVisualAcuityResultValid(rightVisualAcuityResult);
        if (flag) {
            this.rightVisualAcuityResult = rightVisualAcuityResult;
        }
    }

    public boolean checkSphereValid(final Float sphere) {
        if (sphere == null) {
            throw new IllegalArgumentException("sphere cannot be null");
        }
        String pattern = "^[-]?\\d+(\\.[0-9]{1,2})?$";
        if (!Pattern.matches(pattern, String.valueOf(sphere))) {
            throw new IllegalArgumentException(
                    "sphere must be the floating point number up to two digits.");
        }
        return true;
    }

    public void setLeftSphere(final Float leftSphere) {
        boolean flag = checkSphereValid(leftSphere);
        if (flag) {
            this.leftSphere = leftSphere;
        }
    }

    public void setRightSphere(final Float rightSphere) {
        boolean flag = checkSphereValid(rightSphere);
        if (flag) {
            this.rightSphere = rightSphere;
        }
    }
}
