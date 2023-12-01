package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.EmailForm;
import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.services.EmailService;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[UC23] 이메일 전송 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class APIEmailController extends APIController {

    private final EmailService service;

    @GetMapping("emails")
    public List<Email> getEmails() {
        return (List<Email>) service.findAll();
    }

    @PostMapping("emails/send")
    public Email sendEmail(
            @Parameter(description = "발송할 이메일 양식(발신자, 수신자(이메일), 제목, 내용)입니다.") @NotNull EmailForm emailForm) {
        return service.sendEmail(emailForm);
    }
}
