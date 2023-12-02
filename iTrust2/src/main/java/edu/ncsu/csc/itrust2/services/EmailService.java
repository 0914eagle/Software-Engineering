package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.EmailRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailService extends Service {
    private final EmailRepository repository;
    private final UserService userService;
    final LoggerUtil loggerUtil;

    @Autowired private JavaMailSender emailSender;

    @Transactional
    public void sendEmail(
            @NotNull String senderName, String receiverName, String subject, String messageBody) {
        final String fixedSystemEmail = "hajinkids3106@gmail.com";

        User Sender = userService.findByName(senderName);

        User receiver = userService.findByName(receiverName);
        String receiverEmail = receiver.getEmail();

        if (isEmail(receiverEmail)) {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fixedSystemEmail);
            message.setTo(receiverEmail);
            message.setSubject("[iTrust2]" + subject);
            message.setText(senderName + "로부터 전송된 메세지입니다.\n" + messageBody);

            emailSender.send(message);
        }
        Email email = new Email(senderName, receiver, subject, messageBody);

        repository.save(email);
        //getRepository().save(email);
    }

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public List<Email> findByReceiver(final User receiver) {
        return repository.findByReceiver(receiver);
    }

    public static boolean isEmail(String email) {
        boolean validation = false;

        if (email.isEmpty()) {
            return false;
        }

        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            validation = true;
        }

        return validation;
    }

    public List<Email> findBySender(final String sender) {
        return repository.findBySender(sender);
    }
}
