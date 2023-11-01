package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.FoodDiary;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/** A form for REST API communication. Contains fields for constructing FoodDiary objects. */
@Getter
@Setter
@NoArgsConstructor
public class FoodDiaryForm {
    private Long id;
    private ZonedDateTime date;
    private String mealType;
    private String foodName;
    private Integer servingsNum;
    private Integer caloriesPerServing;
    private Integer fatGramsPerServing;
    private Integer sodiumMilligramsPerServing;
    private Integer carbsGramsPerServing;
    private Integer sugarsGramsPerServing;
    private Integer fiberGramsPerServing;
    private Integer proteinGramsPerServing;
    private Integer fatGramsTotal;
    private Integer sodiumMilligramsTotal;
    private Integer carbsGramsTotal;
    private Integer sugarsGramsTotal;
    private Integer fiberGramsTotal;
    private Integer proteinGramsTotal;

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
