package edu.ncsu.csc.itrust2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to represent a Diagnosis made by an HCP as part of an Office Visit
 *
 * @author Thomas
 * @author Kai Presler-Marshall
 */
@Schema(description = "환자에 대한 진단 정보입니다.")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "diagnosis")
public class Diagnosis extends DomainObject {
    @Schema(description = "고유 아이디입니다.")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "방문 정보입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "visit_id", nullable = false)
    @JsonBackReference
    private OfficeVisit visit;

    @Schema(description = "노트 기록입니다.")
    @Setter
    private String note;

    @Schema(description = "진단 코드입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "code_id")
    private ICDCode code;

    @Override
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the Diagnosis
     *
     * @param id The new ID of the Diagnosis. For Hibernate.
     */
    public void setId(final Long id) {
        this.id = id;
    }
}
