package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.services.FoodDiaryService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[UC19] 음식 일기 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ApiFoodDiaryController {
    private final FoodDiaryService foodDiaryService;
    private final LoggerUtil loggerUtil;

    @Operation(summary = "Patient: 자신의 음식 일기 목록 조회")
    @GetMapping("/foodDiaries")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<FoodDiary> listFoodDiariesByCurrentPatient() {
        final String patientName = loggerUtil.getCurrentUsername();
        return foodDiaryService.listByPatient(patientName);
    }

    @Operation(summary = "Patient: 음식 일기 추가")
    @PostMapping("/foodDiaries")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public FoodDiary addFoodDiary(
            @Parameter(description = "환자의 foodDiary입니다.") @RequestBody
                    final FoodDiaryForm foodDiary) {
        final String patientName = loggerUtil.getCurrentUsername();
        return foodDiaryService.addFoodDiary(foodDiary, patientName);
    }

    @Operation(summary = "HCP: 환자 이름으로 음식 일기 목록 조회")
    @GetMapping("/patients/{patientName}/foodDiaries")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<FoodDiary> listFoodDiariesByPatientId(
            @Parameter(description = "조회할 환자의 username 입니다.") @PathVariable
                    final String patientName) {
        return foodDiaryService.listByPatient(patientName);
    }
}
