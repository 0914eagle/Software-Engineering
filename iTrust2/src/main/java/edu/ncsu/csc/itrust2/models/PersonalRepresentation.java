package edu.ncsu.csc.itrust2.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "personal_representation")
public class PersonalRepresentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The patient */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private Patient patient;

    /** The PR of the patient */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "personal_representative_id", columnDefinition = "varchar(100)")
    private Patient personalRepresentative;
}
