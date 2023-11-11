package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;

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

    public List<FoodDiary> listByPatient(String patientName) {

        final Patient patient = (Patient) patientService.findByName(patientName);
        return foodDiaryRepository.findAllByPatient(patient);
    }

    public FoodDiary addFoodDiary(final FoodDiaryForm form, String patientName) {
        try {
            final Patient patient = (Patient) patientService.findByName(patientName);
            final FoodDiary foodDiary = new FoodDiary(form, patient);

            return foodDiaryRepository.save(foodDiary);
        } catch (final Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not create " + form.getFoodName() + " because of " + e.getMessage());
        }
    }
}
