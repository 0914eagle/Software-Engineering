package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.enums.MealType;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FoodDiaryTest {
    @Test
    public void testNewFromForm() {
        final var date = ZonedDateTime.of(2023, 11, 11, 12, 34, 56, 0, ZoneId.of("UTC"));
        final var patient = new Patient();
        final var foodDiaryForm =
                new FoodDiaryForm(date, "Breakfast", "foodName", 3, 10, 20, 30, 40, 50, 60, 70);

        final var foodDiary = new FoodDiary(foodDiaryForm, patient);

        assertEquals(foodDiary.getPatient(), patient);
        assertFoodDiaryEquals(foodDiary, foodDiaryForm);
    }

    public static void assertFoodDiaryEquals(FoodDiary entity, FoodDiaryForm form) {
        final var servingsNum = form.getServingsNum().longValue();

        assertEquals(entity.getDate(), form.getDate());
        assertEquals(entity.getMealType(), MealType.parse(form.getMealType()));
        assertEquals(entity.getFoodName(), form.getFoodName());
        assertEquals(entity.getServingsNum(), form.getServingsNum());
        assertEquals(entity.getCaloriesPerServing(), form.getCaloriesPerServing());
        assertEquals(entity.getFatGramsPerServing(), form.getFatGramsPerServing());
        assertEquals(entity.getSodiumMilligramsPerServing(), form.getSodiumMilligramsPerServing());
        assertEquals(entity.getCarbsGramsPerServing(), form.getCarbsGramsPerServing());
        assertEquals(entity.getSugarsGramsPerServing(), form.getSugarsGramsPerServing());
        assertEquals(entity.getFiberGramsPerServing(), form.getFiberGramsPerServing());
        assertEquals(entity.getProteinGramsPerServing(), form.getProteinGramsPerServing());
        assertEquals(
                entity.getCaloriesTotal().longValue(), servingsNum * form.getCaloriesPerServing());
        assertEquals(
                entity.getFatGramsTotal().longValue(), servingsNum * form.getFatGramsPerServing());
        assertEquals(
                entity.getSodiumMilligramsTotal().longValue(),
                servingsNum * form.getSodiumMilligramsPerServing());
        assertEquals(
                entity.getCarbsGramsTotal().longValue(),
                servingsNum * form.getCarbsGramsPerServing());
        assertEquals(
                entity.getSugarsGramsTotal().longValue(),
                servingsNum * form.getSugarsGramsPerServing());
        assertEquals(
                entity.getFiberGramsTotal().longValue(),
                servingsNum * form.getFiberGramsPerServing());
        assertEquals(
                entity.getProteinGramsTotal().longValue(),
                servingsNum * form.getProteinGramsPerServing());
    }
}
