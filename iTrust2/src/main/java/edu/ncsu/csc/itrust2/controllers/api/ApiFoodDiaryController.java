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
@RequestMapping("/api/v1")
public class ApiFoodDiaryController {
    private final FoodDiaryService foodDiaryService;

    @GetMapping("/foodDiaries")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<FoodDiary> listFoodDiariesByCurrentPatient() {
        final String patientName = LoggerUtil.currentUser();
        return foodDiaryService.listByPatient(patientName);
    }

    @PostMapping("/foodDiaries")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public FoodDiary addFoodDiary(@RequestBody final FoodDiaryForm foodDiary) {
        final String patientName = LoggerUtil.currentUser();
        return foodDiaryService.addFoodDiary(foodDiary, patientName);
    }

    @GetMapping("/patients/{patientName}/foodDiaries")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<FoodDiary> listFoodDiariesByPatientId(@PathVariable final String patientName) {
        return foodDiaryService.listByPatient(patientName);
    }
}
