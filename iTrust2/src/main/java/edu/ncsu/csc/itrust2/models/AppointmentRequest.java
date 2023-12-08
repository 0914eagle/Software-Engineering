package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.Status;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.gson.annotations.JsonAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Backing object for the Appointment Request system. This is the object that is actually stored in
 * the database and reflects the persistent information we have on the appointment request.
 *
 * @author Kai Presler-Marshall
 */
@Schema(description = "환자가 보낸 예약 요청입니다.")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "appointment_request")
public class AppointmentRequest extends DomainObject {
    /** ID of the AppointmentRequest */
    @Schema(description = "고유 아이디입니다.")
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The Patient who is associated with this AppointmentRequest */
    @Schema(description = "예약 요청을 보내는 환자입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The HCP who is associated with this AppointmentRequest */
    @Schema(description = "환자가 예약을 요청하는 의사입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hcp_id", columnDefinition = "varchar(100)")
    private User hcp;

    /** When this AppointmentRequest has been scheduled to take place */
    @Schema(description = "환자가 예약하고자 하는 시간입니다.")
    @Setter
    @NotNull @Basic
    // Allows the field to show up nicely in the database
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime date;

    /**
     * Store the Enum in the DB as a string as it then makes the DB info more legible if it needs to
     * be read manually.
     */
    @Schema(description = "환자가 보낸 예약 요청의 종류입니다. 안과 수술 예약, 안과 예약, 일반 예약이 가능합니다.")
    @Setter
    @NotNull @Enumerated(EnumType.STRING)
    private AppointmentType type;

    /** Any (optional) comments on the AppointmentRequest */
    @Schema(description = "환자가 보내는 50자 이내의 comment입니다.")
    @Setter
    private String comments;

    /** The Status of the AppointmentRequest */
    @Schema(description = "환자가 보낸 예약 요청의 상태입니다. 승인, 거절, 미결정이 가능합니다.")
    @Setter
    @NotNull @Enumerated(EnumType.STRING)
    private Status status;

    @Schema(description = "안과 예약에서만 쓰이는 환자가 보내는 이름입니다. 250자 이내의 alphabet과 숫자, '-'으로 구성된 문자열입니다.")
    @Setter
    private String name;

    @Schema(description = "안과 예약에서만 쓰이는 환자가 보내는 약어입니다. 10자 이내의 alphabet과 숫자, '-'으로 구성된 문자열입니다.")
    @Setter
    private String abbreviation;

    @Schema(description = "안과 예약에서만 쓰이는 환자가 보내는 CPT 코드입니다. 90으로 시작하는 5자리의 숫자입니다.")
    @Setter
    private String cptCode;

    @Schema(description = "안과 예약에서만 쓰이는 환자가 보내는 500자 이내의 comment입니다.")
    @Setter
    private String longComment;
}
