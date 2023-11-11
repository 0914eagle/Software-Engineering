package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.models.enums.MealType;

import java.time.ZonedDateTime;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@Getter
@Entity
public class FoodDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "username", columnDefinition = "varchar(20)")
    private Patient patient;

    @Setter
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    ZonedDateTime date;

    @Setter
    @Enumerated(EnumType.STRING)
    MealType mealType;

    @Setter String foodName;
    @Setter Integer servingsNum;
    @Setter Integer caloriesPerServing;
    @Setter Integer fatGramsPerServing;
    @Setter Integer sodiumMilligramsPerServing;
    @Setter Integer carbsGramsPerServing;
    @Setter Integer sugarsGramsPerServing;
    @Setter Integer fiberGramsPerServing;
    @Setter Integer proteinGramsPerServing;
    @Setter Integer caloriesTotal;
    @Setter Integer fatGramsTotal;
    @Setter Integer sodiumMilligramsTotal;
    @Setter Integer carbsGramsTotal;
    @Setter Integer sugarsGramsTotal;
    @Setter Integer fiberGramsTotal;
    @Setter Integer proteinGramsTotal;

    public FoodDiary(final FoodDiaryForm form, final Patient patient) {
        final var servingsNum = form.getServingsNum();
        setDate(form.getDate());
        setPatient(patient);
        setMealType(MealType.parse(form.getMealType()));
        setFoodName(form.getFoodName());
        setServingsNum(servingsNum);
        setCaloriesPerServing(form.getCaloriesPerServing());
        setFatGramsPerServing(form.getFatGramsPerServing());
        setSodiumMilligramsPerServing(form.getSodiumMilligramsPerServing());
        setCarbsGramsPerServing(form.getCarbsGramsPerServing());
        setSugarsGramsPerServing(form.getSugarsGramsPerServing());
        setFiberGramsPerServing(form.getFiberGramsPerServing());
        setProteinGramsPerServing(form.getProteinGramsPerServing());
        setCaloriesTotal(servingsNum * form.getCaloriesPerServing());
        setFatGramsTotal(servingsNum * form.getFatGramsPerServing());
        setSodiumMilligramsTotal(servingsNum * form.getSodiumMilligramsPerServing());
        setCarbsGramsTotal(servingsNum * form.getCarbsGramsPerServing());
        setSugarsGramsTotal(servingsNum * form.getSugarsGramsPerServing());
        setFiberGramsTotal(servingsNum * form.getFiberGramsPerServing());
        setProteinGramsTotal(servingsNum * form.getProteinGramsPerServing());
    }
}
