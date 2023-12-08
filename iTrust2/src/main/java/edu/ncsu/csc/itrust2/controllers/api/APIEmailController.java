package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.EmailForm;
import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.EmailService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[UC23] 이메일 전송 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class APIEmailController extends APIController {

    private final EmailService service;
    private final UserService userService;
    private final LoggerUtil loggerUtil;

    @GetMapping("emails")
    public List<Email> getEmails() {
        return (List<Email>) service.findAll();
    }

    @Operation(summary = "Everyone: 이메일 실제 발송")
    @PostMapping("/emails/send")
    @PreAuthorize("isAuthenticated()")
    public void sendEmail(
            @Parameter(description = "전송하려는 이메일 내용입니다.") @RequestBody @NotNull EmailForm emailForm) {
        String senderName = loggerUtil.getCurrentUsername();
        service.sendEmail(
                senderName,
                emailForm.getReceiver(),
                emailForm.getSubject(),
                emailForm.getMessageBody());
        loggerUtil.log(TransactionType.USER_TO_USER_EMAIL, senderName, emailForm.getReceiver());
    }

    @Operation(summary = "자신이 발송한 메일 목록을 조회합니다.")
    @GetMapping("/Outbox")
    public List<Email> viewOutbox() {
        String currentUsername = loggerUtil.getCurrentUsername();
        return service.findBySender(currentUsername);
    }

    @Operation(summary = "자신이 수신한 메일 목록을 조회합니다.")
    @GetMapping("/Inbox")
    public List<Email> viewInbox() {
        String currentUsername = loggerUtil.getCurrentUsername();
        User currentUser = userService.findByName(currentUsername);
        return service.findByReceiver(currentUser);
    }
}
