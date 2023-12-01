package edu.ncsu.csc.itrust2.forms;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailForm {
    private String sender;
    private String receiver;
    private String subject;
    private String messageBody;
}
