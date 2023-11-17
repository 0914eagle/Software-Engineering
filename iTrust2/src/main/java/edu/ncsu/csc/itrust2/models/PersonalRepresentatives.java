package edu.ncsu.csc.itrust2.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "personal_representatives")
public class PersonalRepresentatives {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The patient */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The PR of the patient */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "pr_id", columnDefinition = "varchar(100)")
    private User pr;
}
