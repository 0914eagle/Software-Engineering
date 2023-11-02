package edu.ncsu.csc.itrust2.models;

import com.google.gson.annotations.JsonAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class FoodDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "username", columnDefinition = "varchar(20)")
    private Patient patient;

    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    ZonedDateTime date;

    String mealType;
    String foodName;
    Integer servingsNum;
    Integer caloriesPerServing;
    Integer fatGramsPerServing;
    Integer sodiumMilligramsPerServing;
    Integer carbsGramsPerServing;
    Integer sugarsGramsPerServing;
    Integer fiberGramsPerServing;
    Integer proteinGramsPerServing;
    Integer fatGramsTotal;
    Integer sodiumMilligramsTotal;
    Integer carbsGramsTotal;
    Integer sugarsGramsTotal;
    Integer fiberGramsTotal;
    Integer proteinGramsTotal;

    public FoodDiary(final FoodDiaryForm form) {
        setId(form.getId());
        setDate(form.getDate());
        setMealType(form.getMealType());
        setFoodName(form.getFoodName());
        setServingsNum(form.getServingsNum());
        setCaloriesPerServing(form.getCaloriesPerServing());
        setFatGramsPerServing(form.getFatGramsPerServing());
        setSodiumMilligramsPerServing(form.getSodiumMilligramsPerServing());
        setCarbsGramsPerServing(form.getCarbsGramsPerServing());
        setSugarsGramsPerServing(form.getSugarsGramsPerServing());
        setFiberGramsPerServing(form.getFiberGramsPerServing());
        setProteinGramsPerServing(form.getProteinGramsPerServing());
        setFatGramsTotal(form.getServingsNum() * form.getFatGramsPerServing());
        setSodiumMilligramsTotal(form.getServingsNum() * form.getSodiumMilligramsPerServing());
        setCarbsGramsTotal(form.getServingsNum() * form.getCarbsGramsPerServing());
        setSugarsGramsTotal(form.getServingsNum() * form.getSugarsGramsPerServing());
        setFiberGramsTotal(form.getServingsNum() * form.getFiberGramsPerServing());
        setProteinGramsTotal(form.getServingsNum() * form.getProteinGramsPerServing());
    }
}
