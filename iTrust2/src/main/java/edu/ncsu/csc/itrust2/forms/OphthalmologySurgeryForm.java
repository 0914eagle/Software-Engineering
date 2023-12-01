package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OphthalmologySurgeryForm {
    private Long id;
    private Integer leftVisualActuityResult;
    private Integer rightVisualActuityResult;
    private Float leftSphere;
    private Float rightSphere;
    private Float leftCylinder;
    private Float rightCylinder;
    private Integer leftAxis;
    private Integer rightAxis;
    private OphthalmologySurgeryType surgeryType;
}
