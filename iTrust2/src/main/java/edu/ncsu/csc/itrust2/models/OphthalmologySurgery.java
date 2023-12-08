package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Schema(description = "안과 수술 문서입니다. Office visit과 one to one 관계입니다.")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "ophthalmology_surgery")
public class OphthalmologySurgery extends DomainObject {

    @Schema(description = "고유 아이디입니다.")
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "왼쪽 눈에 대한 visual acuity result 값입니다.")
    @NotNull private Integer leftVisualAcuityResult;

    @Schema(description = "오른쪽 눈에 대한 visual acuity result 값입니다.")
    @NotNull private Integer rightVisualAcuityResult;

    @Schema(description = "왼쪽 눈의 Sphere 값입니다.")
    @NotNull private Float leftSphere;

    @Schema(description = "오른쪽 눈의 Sphere 값입니다.")
    @NotNull private Float rightSphere;

    @Schema(description = "왼쪽 눈의 axis 값입니다.")
    private Integer leftAxis;

    @Schema(description = "오른쪽 눈의 axis 값입니다.")
    private Integer rightAxis;

    @Schema(description = "왼쪽 눈의 cylinder 값입니다.")
    private Float leftCylinder;

    @Schema(description = "오른쪽 눈의 cylinder 값입니다.")
    private Float rightCylinder;

    @Schema(description = "환자가 받은 수술의 종류입니다.")
    @NotNull @Setter
    private OphthalmologySurgeryType surgeryType;

    @Schema(description = "수술을 받은 환자입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The hcp of this office visit */
    @Schema(description = "수술을 한 안과의사입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hcp_id", columnDefinition = "varchar(100)")
    private User hcp;

    public void checkVisualAcuityResultValid(final Integer visualAcuityResult) {
        if (visualAcuityResult == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Visual acuity result cannot be null");
        }
        if (visualAcuityResult < 20 || visualAcuityResult > 200) {
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sphere cannot be null");
        }
        if (Math.abs(sphere) >= 100.0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Sphere must be up to two digits.");
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
        if (cylinder == null) return;

        if (axis == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Axis cannot be null if Cylinder is not null.");
        }
        if (Math.abs(cylinder) >= 100.0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cylinder must be up to two digits.");
        }
    }

    public void setLeftCylinder(final Float leftCylinder) {
        checkCylinderValid(leftCylinder, this.leftAxis);
        this.leftCylinder = leftCylinder;
    }

    public void setRightCylinder(final Float rightCylinder) {
        checkCylinderValid(rightCylinder, this.rightAxis);
        this.rightCylinder = rightCylinder;
    }

    public void checkAxisValid(final Integer axis) {
        if (axis == null) return;

        if (axis < 1 || axis > 180) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Axis must be the value between 1 and 180.");
        }
    }

    public void setLeftAxis(final Integer leftAxis) {
        checkAxisValid(leftAxis);
        this.leftAxis = leftAxis;
    }

    public void setRightAxis(final Integer rightAxis) {
        checkAxisValid(rightAxis);
        this.rightAxis = rightAxis;
    }
}
