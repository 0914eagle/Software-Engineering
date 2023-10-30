package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.FoodDiary;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * A form for REST API communication. Contains fields for constructing FoodDiary objects.
 *
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class FoodDiaryForm {
        private Long id;
        private String date;
        private String mealType;
        private String foodName;
        private String servingsNum;
        private String caloriesPerServing;
        private String fatGramsPerServing;
        private String sodiumMilligramsPerServing;
        private String carbsGramsPerServing;
        private String sugarsGramsPerServing;
        private String fiberGramsPerServing;
        private String proteinGramsPerServing;
        private String fatGramsTotal;
        private String sodiumMilligramsTotal;
        private String carbsGramsTotal;
        private String sugarsGramsTotal;
        private String fiberGramsTotal;
        private String proteinGramsTotal;

        /**
        * Constructs a new form with information from the given food diary.
        *
        * @param foodDiary the food diary object
        */
        public FoodDiaryForm(@NotNull final FoodDiary foodDiary) {
            setId(foodDiary.getId());
            setDate(foodDiary.getDate());
            setMealType(foodDiary.getMealType());
            setFoodName(foodDiary.getFoodName());
            setServingsNum(foodDiary.getServingsNum());
            setCaloriesPerServing(foodDiary.getCaloriesPerServing());
            setFatGramsPerServing(foodDiary.getFatGramsPerServing());
            setSodiumMilligramsPerServing(foodDiary.getSodiumMilligramsPerServing());
            setCarbsGramsPerServing(foodDiary.getCarbsGramsPerServing());
            setSugarsGramsPerServing(foodDiary.getSugarsGramsPerServing());
            setFiberGramsPerServing(foodDiary.getFiberGramsPerServing());
            setProteinGramsPerServing(foodDiary.getProteinGramsPerServing());
            setFatGramsTotal(foodDiary.getFatGramsTotal());
            setSodiumMilligramsTotal(foodDiary.getSodiumMilligramsTotal());
            setCarbsGramsTotal(foodDiary.getCarbsGramsTotal());
            setSugarsGramsTotal(foodDiary.getSugarsGramsTotal());
            setFiberGramsTotal(foodDiary.getFiberGramsTotal());
            setProteinGramsTotal(foodDiary.getProteinGramsTotal());
        }
}
