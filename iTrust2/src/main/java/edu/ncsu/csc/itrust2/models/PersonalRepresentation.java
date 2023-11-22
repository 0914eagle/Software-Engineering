package edu.ncsu.csc.itrust2.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "대리인 관계입니다. 피대리인 환자와 대리인 사이의 many-to-many 관계를 나타냅니다.")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "personal_representation")
public class PersonalRepresentation {
    @Schema(description = "고유 아이디입니다.")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "피대리인 환자입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private Patient patient;

    @Schema(description = "대리인입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "personal_representative_id", columnDefinition = "varchar(100)")
    private Patient personalRepresentative;

    public PersonalRepresentation(final Patient patient, final Patient personalRepresentative) {
        setPatient(patient);
        setPersonalRepresentative(personalRepresentative);
    }
}
