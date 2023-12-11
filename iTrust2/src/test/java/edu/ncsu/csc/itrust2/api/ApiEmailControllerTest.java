package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.controllers.api.APIEmailController;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.EmailService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiEmailControllerTest {
    @Mock private EmailService emailService;
    @Mock private LoggerUtil loggerUtil;
    @Mock private UserService userService;
    @InjectMocks private APIEmailController apiEmailController;

    @Test
    public void testViewInbox() {
        final String currentUsername = "currentUsername";
        final UserForm currentUserForm =
                new UserForm(currentUsername, "123456", Role.ROLE_PATIENT, 1);
        final User sender1 = new Patient(currentUserForm);
        sender1.setEmail("hajinkids3106@gmail.com");
        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);

        final String receiver1Username = "receiver1Username";
        final UserForm receiverUserForm =
                new UserForm(receiver1Username, "123456", Role.ROLE_PATIENT, 1);
        final User receiver1 = new Patient(receiverUserForm);
        receiver1.setEmail("12345@itrust.com");
        given(userService.findByName(receiver1Username)).willReturn(receiver1);

        final String receiver2Username = "receiver2Username";
        final UserForm receiver2UserForm =
                new UserForm(receiver2Username, "123456", Role.ROLE_HCP, 1);
        final User receiver2 = new Personnel(receiver2UserForm);
        receiver1.setEmail("abcde@itrust.com");

        Email testEmail1 = new Email("currentUsername", receiver1, "Subject1", "Body1");
        Email testEmail2 = new Email("currentUsername", receiver2, "Subject2", "Body2");
        List<Email> expectedEmail1 = List.of(testEmail1);
        List<Email> expectedEmail2 = List.of(testEmail2);

        emailService.sendEmail("currentUsername", receiver1Username, "Subject1", "Body1");
        emailService.sendEmail("currentUsername", receiver2Username, "Subject1", "Body1");
        given(emailService.findByReceiver(receiver1)).willReturn(expectedEmail1);

        given(loggerUtil.getCurrentUsername()).willReturn(receiver1Username);
        List<Email> actualEmail1 = apiEmailController.viewInbox();
        assertNotNull(actualEmail1);
        assertEquals(expectedEmail1, actualEmail1);
    }

    @Test
    public void testViewOutbox() {
        final String sender1Username = "sender1Username";
        final UserForm currentUserForm =
                new UserForm(sender1Username, "123456", Role.ROLE_PATIENT, 1);
        final User sender1 = new Patient(currentUserForm);
        sender1.setEmail("hajinkids3106@gmail.com");
        given(loggerUtil.getCurrentUsername()).willReturn(sender1Username);

        final String receiver1Username = "receiver1Username";
        final UserForm receiverUserForm =
                new UserForm(receiver1Username, "123456", Role.ROLE_PATIENT, 1);
        final User receiver1 = new Patient(receiverUserForm);
        receiver1.setEmail("12345@itrust2.com");

        final String receiver2Username = "receiver2Username";
        final UserForm receiver2UserForm =
                new UserForm(receiver2Username, "123456", Role.ROLE_HCP, 1);
        final User receiver2 = new Personnel(receiver2UserForm);
        receiver1.setEmail("abcde@itrust2.com");

        Email testEmail1 = new Email("sender1Username", receiver1, "Subject1", "Body1");
        Email testEmail2 = new Email("sender1Username", receiver2, "Subject2", "Body2");
        List<Email> expectedEmails = Arrays.asList(testEmail1, testEmail2);

        emailService.sendEmail("sender1Username", receiver1Username, "Subject1", "Body1");
        emailService.sendEmail("sender1Username", receiver2Username, "Subject2", "Body2");
        given(emailService.findBySender(sender1Username)).willReturn(expectedEmails);

        given(loggerUtil.getCurrentUsername()).willReturn(sender1Username);
        List<Email> actualEmails = apiEmailController.viewOutbox();

        assertNotNull(actualEmails);
        assertEquals(expectedEmails, actualEmails);
    }
}
