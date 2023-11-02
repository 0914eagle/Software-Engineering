package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static edu.ncsu.csc.itrust2.services.Service.*;

@Component
@RequiredArgsConstructor
@Service
public class FoodDiaryService {
    private final FoodDiaryRepository foodDiaryRepository;

    private final PatientService patientService;

    public List<FoodDiary> listByPatient(String patientName) {

        final Patient patient = (Patient) patientService.findByName(patientName);
        return foodDiaryRepository.findAllByPatient(patient);
    }

    public boolean existsByCode(final Long id) {
        return foodDiaryRepository.existsById(id);
    }

    public FoodDiary addFoodDiary(final FoodDiaryForm form, final String patientName) {
        try {
            final FoodDiary foodDiary = new FoodDiary(form);
            final Patient patient = (Patient) patientService.findByName(patientName);
            foodDiary.setPatient(patient);

            // Make sure code does not conflict with existing drugs
            if (existsByCode(foodDiary.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "FoodDiary with Id " + foodDiary.getId() + " already exists");
            }
            return foodDiaryRepository.save(foodDiary);
        } catch (final Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not create " + form.getId() + " because of " + e.getMessage());
        }
    }
}
