package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.services.FoodDiaryService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/foodDiaries")
public class ApiFoodDiaryController {
    private final FoodDiaryService foodDiaryService;
    private final String patient_name = LoggerUtil.currentUser();

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<FoodDiary> listFoodDiariesByPatient() {
        return foodDiaryService.listByPatient(patient_name);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public FoodDiary addFoodDiary(@RequestBody final FoodDiaryForm foodDiary) {
        return foodDiaryService.addFoodDiary(foodDiary, patient_name);
    }
}
