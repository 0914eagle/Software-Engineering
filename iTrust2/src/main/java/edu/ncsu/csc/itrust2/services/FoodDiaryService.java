package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FoodDiaryService {
    private final FoodDiaryRepository foodDiaryRepository;

    public List<FoodDiary> listByPatientName(String patientName) {
        return foodDiaryRepository.findAllByPatientName(patientName);
    }

    public List<FoodDiary> saveDefaults(String patientName) {
        var foodDiary1 = new FoodDiary();
        var foodDiary2 = new FoodDiary();
        var foodDiary3 = new FoodDiary();

        foodDiary1.setPatientName(patientName);
        foodDiary2.setPatientName(patientName);
        foodDiary3.setPatientName(patientName);

        foodDiary1.setDate("2021-01-01");
        foodDiary2.setDate("2021-01-01");
        foodDiary3.setDate("2021-01-01");

        List<FoodDiary> foodDiaries = List.of(foodDiary1, foodDiary2, foodDiary3);

        return foodDiaryRepository.saveAll(foodDiaries);
    }
}
