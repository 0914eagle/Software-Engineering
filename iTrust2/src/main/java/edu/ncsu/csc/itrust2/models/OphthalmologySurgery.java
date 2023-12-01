package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.models.enums.OphthalmologySurgeryType;

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

    @NotNull @Setter private Integer leftVisualActuityResult;

    @NotNull @Setter private Integer rightVisualActuityResult;

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
}
