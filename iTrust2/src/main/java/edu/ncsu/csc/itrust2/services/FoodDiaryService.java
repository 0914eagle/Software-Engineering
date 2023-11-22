package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class FoodDiaryService {
    private final FoodDiaryRepository foodDiaryRepository;
    private final PatientService patientService;
    private final UserService userService;
    final LoggerUtil loggerUtil;

    public List<FoodDiary> listByPatient(String patientName) {

        final Patient patient = (Patient) patientService.findByName(patientName);

        String currentUserName = loggerUtil.getCurrentUsername();
        User currentUser = userService.findByName(currentUserName);

        if (currentUser.isDoctor()) {
            loggerUtil.log(TransactionType.HCP_VIEW_FOOD_DIARY_ENTRY, currentUserName, patientName);
        } else {

            loggerUtil.log(TransactionType.PATIENT_VIEW_FOOD_DIARY_ENTRY, currentUserName);
        }
        return foodDiaryRepository.findAllByPatient(patient);
    }

    public FoodDiary addFoodDiary(final FoodDiaryForm form, String patientName) {
        String currentUserName = loggerUtil.getCurrentUsername();

        try {
            final Patient patient = (Patient) patientService.findByName(patientName);
            final FoodDiary foodDiary = new FoodDiary(form, patient);
            loggerUtil.log(TransactionType.CREATE_FOOD_DIARY_ENTRY, currentUserName);
            return foodDiaryRepository.save(foodDiary);
        } catch (final Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not create " + form.getFoodName() + " because of " + e.getMessage());
        }
    }
}
