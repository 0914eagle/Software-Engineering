package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOphthalmologySurgeryForm {
    private Integer leftVisualAcuityResult;
    private Integer rightVisualAcuityResult;
    private Float leftSphere;
    private Float rightSphere;
    private Float leftCylinder;
    private Float rightCylinder;
    private Integer leftAxis;
    private Integer rightAxis;
    private OphthalmologySurgeryType surgeryType;
}
