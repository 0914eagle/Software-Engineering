package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.EmailForm;
import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.EmailRepository;

import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailService extends Service {

    private final EmailRepository repository;
    private final UserService userService;
    private final JavaMailSender javaMailSender;

    public Email sendEmail(@NotNull EmailForm emailForm) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // 이메일 발송
        try {
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailForm.getReceiver());
            mimeMessageHelper.setSubject(emailForm.getSubject());
            mimeMessageHelper.setText(emailForm.getMessageBody(), false); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        // 이메일 발송 내역 저장
        try{
            //User currentReceiver = userService.findByEmail(emailForm.getReceiver());
            User currentReceiver = userService.findByName(emailForm.getReceiver());

            Email email =
                    new Email(
                            emailForm.getSender(),
                            currentReceiver,
                            emailForm.getSubject(),
                            emailForm.getMessageBody());
            return repository.save(email);
        } catch (java.util.InputMismatchException e){
            System.out.println("해당 이메일을 가진 유저가 없습니다.");
        }
        return null;
    }

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public List<Email> findByReceiver(final User receiver) {
        return repository.findByReceiver(receiver);
    }
}
