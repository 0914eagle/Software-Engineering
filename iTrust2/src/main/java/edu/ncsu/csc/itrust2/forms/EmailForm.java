package edu.ncsu.csc.itrust2.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "현재 User가 전송하려는 이메일 내용입니다.")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailForm {
    @Schema(description = "수신자 name입니다.")
    private String receiver;

    @Schema(description = "이메일 제목입니다.")
    private String subject;

    @Schema(description = "메세지 내용입니다.")
    private String messageBody;
}
