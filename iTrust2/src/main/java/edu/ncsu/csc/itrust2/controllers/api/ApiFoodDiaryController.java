package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.services.FoodDiaryService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foodDiaries")
public class ApiFoodDiaryController {
    private final FoodDiaryService foodDiaryService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<FoodDiary> listFoodDiariesByPatient() {
        final String patient_name = LoggerUtil.currentUser();
        return foodDiaryService.listByPatient(patient_name);
    }
}
