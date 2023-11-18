package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.services.FoodDiaryService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ApiFoodDiaryController {
    private final FoodDiaryService foodDiaryService;
    private final LoggerUtil loggerUtil;

    @GetMapping("/foodDiaries")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<FoodDiary> listFoodDiariesByCurrentPatient() {
        final String patientName = loggerUtil.getCurrentUsername();
        return foodDiaryService.listByPatient(patientName);
    }

    @PostMapping("/foodDiaries")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public FoodDiary addFoodDiary(@RequestBody final FoodDiaryForm foodDiary) {
        final String patientName = loggerUtil.getCurrentUsername();
        return foodDiaryService.addFoodDiary(foodDiary, patientName);
    }

    @GetMapping("/patients/{patientName}/foodDiaries")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<FoodDiary> listFoodDiariesByPatientId(@PathVariable final String patientName) {
        return foodDiaryService.listByPatient(patientName);
    }
}
