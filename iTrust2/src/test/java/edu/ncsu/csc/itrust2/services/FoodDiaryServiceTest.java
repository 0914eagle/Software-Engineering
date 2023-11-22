package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FoodDiaryServiceTest {
    @Mock private FoodDiaryRepository foodDiaryRepository;

    @Mock private PatientService patientService;

    @InjectMocks private FoodDiaryService foodDiaryService;

    @Mock private LoggerUtil loggerUtil;

    @Test
    public void testAddFoodDiarySuccess() {
        final var date = ZonedDateTime.of(2023, 11, 11, 12, 34, 56, 0, ZoneId.of("UTC"));
        final var patient = new Patient();
        final var foodDiaryForm =
                new FoodDiaryForm(date, "Breakfast", "foodName", 3, 10, 20, 30, 40, 50, 60, 70);

        given(patientService.findByName(any(String.class))).willReturn(patient);

        given(foodDiaryRepository.save(any(FoodDiary.class)))
                .willAnswer(invocation -> invocation.getArgument(0, FoodDiary.class));

        final var result = foodDiaryService.addFoodDiary(foodDiaryForm, "patient");

        assertEquals(result.getPatient(), patient);
        FoodDiaryTest.assertFoodDiaryEquals(result, foodDiaryForm);
    }

    @Test
    public void testAddFoodDiaryFailure() {
        given(patientService.findByName(any(String.class))).willThrow(RuntimeException.class);

        final var foodDiaryForm = new FoodDiaryForm();

        assertThrows(
                ResponseStatusException.class,
                () -> foodDiaryService.addFoodDiary(foodDiaryForm, "patient"));
    }

    @Test
    public void testListByPatient() {
        final String patientUsername1 = "patientUser1";
        final UserForm userForm = new UserForm(patientUsername1, "123456", Role.ROLE_PATIENT, 1);
        final Patient patientUser1 = new Patient(userForm);

        when(loggerUtil.getCurrentUsername()).thenReturn(patientUsername1);

        List<FoodDiary> foodDiaryList = new ArrayList<FoodDiary>();

        given(patientService.findByName(any(String.class))).willReturn(patientUser1);
        given(foodDiaryRepository.findAllByPatient(patientUser1)).willReturn(foodDiaryList);

        final List<FoodDiary> result = foodDiaryService.listByPatient("patientUser1");

        assertEquals(result, foodDiaryList);
    }
}
