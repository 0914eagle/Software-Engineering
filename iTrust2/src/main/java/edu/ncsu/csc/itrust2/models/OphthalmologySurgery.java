package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "ophthalmology_surgery")
public class OphthalmologySurgery extends DomainObject {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull private Integer leftVisualAcuityResult;

    @NotNull private Integer rightVisualAcuityResult;

    @NotNull private Float leftSphere;

    @NotNull private Float rightSphere;

    private Integer leftAxis;

    private Integer rightAxis;

    private Float leftCylinder;

    private Float rightCylinder;

    @NotNull @Setter private OphthalmologySurgeryType surgeryType;

    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The hcp of this office visit */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hcp_id", columnDefinition = "varchar(100)")
    private User hcp;

    public void checkVisualAcuityResultValid(final Integer visualAcuityResult) {
        if (visualAcuityResult == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Visual acuity result cannot be null");
        }
        if (leftVisualAcuityResult < 20 || leftVisualAcuityResult > 200) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Visual acuity result must be the value between 20 and 200.");
        }
    }

    public void setLeftVisualAcuityResult(final Integer leftVisualAcuityResult) {
        checkVisualAcuityResultValid(leftVisualAcuityResult);
        this.leftVisualAcuityResult = leftVisualAcuityResult;
    }

    public void setRightVisualAcuityResult(final Integer rightVisualAcuityResult) {
        checkVisualAcuityResultValid(rightVisualAcuityResult);
        this.rightVisualAcuityResult = rightVisualAcuityResult;
    }

    public void checkSphereValid(final Float sphere) {
        if (sphere == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"Sphere cannot be null");
        }
        BigDecimal sphereBigDecimal = new BigDecimal(sphere.toString());
        if(!(sphereBigDecimal.scale() <= 2 && (sphereBigDecimal.abs().compareTo(BigDecimal.TEN) < 0))){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sphere must be the floating point number up to two digits.");
        }
    }

    public void setLeftSphere(final Float leftSphere) {
        checkSphereValid(leftSphere);
        this.leftSphere = leftSphere;
    }

    public void setRightSphere(final Float rightSphere) {
        checkSphereValid(rightSphere);
        this.rightSphere = rightSphere;
    }

    public void checkCylinderValid(final Float cylinder, final Integer axis) {
        if(leftCylinder != null) {
            if (axis == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Axis cannot be null if Cylinder is not null."
                );
            }
            BigDecimal cylinderBigDecimal = new BigDecimal(cylinder.toString());
            if (!(cylinderBigDecimal.scale() <= 2 && (cylinderBigDecimal.abs().compareTo(BigDecimal.TEN) < 0))) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Sphere must be the floating point number up to two digits.");
            }
        }
    }

    public void setLeftCylinder(final Float leftCylinder){
        checkCylinderValid(leftCylinder, this.leftAxis);
        this.leftCylinder = leftCylinder;
    }

    public void setRightCylinder(final Float rightCylinder){
        checkCylinderValid(rightCylinder, this.rightAxis);
        this.rightCylinder = rightCylinder;
    }

    public void checkAxisValid(final Integer axis){
        if(axis != null){
            if(axis < 1 || axis > 180){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Axis must be the value between 1 and 180.");
            }
        }
    }

    public void setLeftAxis(final Integer leftAxis){
        checkAxisValid(leftAxis);
        this.leftAxis = leftAxis;
    }

    public void setRightAxis(final Integer rightAxis){
        checkAxisValid(rightAxis);
        this.rightAxis = rightAxis;
    }



}
