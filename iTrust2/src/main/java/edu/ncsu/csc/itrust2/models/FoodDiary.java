package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;

import java.time.ZonedDateTime;
import javax.persistence.*;

import com.google.gson.annotations.JsonAdapter;
import edu.ncsu.csc.itrust2.models.enums.MealType;
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
    @Setter Integer fatGramsTotal;
    @Setter Integer sodiumMilligramsTotal;
    @Setter Integer carbsGramsTotal;
    @Setter Integer sugarsGramsTotal;
    @Setter Integer fiberGramsTotal;
    @Setter Integer proteinGramsTotal;

    public FoodDiary(final FoodDiaryForm form, final Patient patient) {
        setDate(form.getDate());
        setPatient(patient);
        setMealType(MealType.parse(form.getMealType()));
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
