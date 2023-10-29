package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.services.FoodDiaryService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foodDiaries")
public class ApiFoodDiaryController {
    private final FoodDiaryService foodDiaryService;

    @GetMapping
    public List<FoodDiary> listFoodDiariesByPatientName(@RequestParam String patientName) {
        return foodDiaryService.listByPatientName(patientName);
    }

    @PostMapping
    public List<FoodDiary> saveDefaults(@RequestBody String patientName) {
        return foodDiaryService.saveDefaults(patientName);
    }
}
