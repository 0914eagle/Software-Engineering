package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.repositories.EmailRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock private LoggerUtil loggerUtil;
    @Mock private UserService userService;
    @Mock private EmailRepository emailRepository;
    @InjectMocks private EmailService emailService;
    @Mock private JavaMailSender emailSender;

    @Test
    public void testSendEmail() {
        final String fixedSystemEmail = "hajinkids3106@gmail.com";
        final String currentUsername = "mockEmailService";
        final UserForm currentUserForm = new UserForm(currentUsername, "123456", Role.ROLE_HCP, 1);
        User sender1 = new Personnel(currentUserForm);
        sender1.setEmail(fixedSystemEmail);

        final String receiverUsername = "receiverUsername";
        final UserForm receiverUserForm =
                new UserForm(receiverUsername, "123456", Role.ROLE_PATIENT, 1);
        User receiver1 = new Patient(receiverUserForm);
        receiver1.setEmail("judy01@yonsei.ac.kr");

        given(userService.findByName(receiverUsername)).willReturn(receiver1);

        Email expectedEmail =
                new Email(sender1.getUsername(), receiver1, "Test Subject", "Test Message Body");
        List<Email> expectedEmails = List.of(expectedEmail);
        given(emailRepository.findByReceiver(any(User.class))).willReturn(expectedEmails);

        emailService.sendEmail(
                sender1.getUsername(), receiverUsername, "Test Subject", "Test Message Body");

        verify(emailRepository).save(any(Email.class));
        List<Email> actualEmail = emailService.findByReceiver(receiver1);

        assertNotNull(actualEmail);
        assertEquals(expectedEmails, actualEmail);
    }

    @Test
    public void testFindBySender() {
        final var sender = "sender";

        final var expected = List.of(new Email());
        given(emailRepository.findBySender(sender)).willReturn(expected);

        final var actual = emailService.findBySender(sender);

        assertEquals(expected, actual);
    }
}
