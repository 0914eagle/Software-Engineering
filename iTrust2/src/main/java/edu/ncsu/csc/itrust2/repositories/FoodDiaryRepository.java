package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.FoodDiary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDiaryRepository extends JpaRepository<FoodDiary, Long> {
    List<FoodDiary> findAllByPatientName(String patientName);
}
