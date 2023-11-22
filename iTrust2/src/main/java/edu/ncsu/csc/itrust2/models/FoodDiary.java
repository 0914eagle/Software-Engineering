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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Schema(description = "환자가 등록한 FoodDiary입니다.")
@NoArgsConstructor
@Getter
@Entity
public class FoodDiary {
    @Schema(description = "고유 아이디입니다.")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Schema(description = "FoodDiary를 등록하려는 환자에 대한 정보입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "username", columnDefinition = "varchar(20)")
    private Patient patient;

    @Schema(description = "FoodDiary의 날짜입니다.")
    @Setter
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    ZonedDateTime date;

    @Schema(description = "FoodDiary의 식사 종류입니다.")
    @Setter
    @Enumerated(EnumType.STRING)
    MealType mealType;

    @Schema(description = "FoodDiary에 등록할 음식 이름입니다.")
    @Setter
    String foodName;

    @Schema(description = "FoodDiary에 등록할 음식의 서빙 수입니다.")
    @Setter
    Integer servingsNum;

    @Schema(description = "FoodDiary에 등록할 음식의 1회 제공량에 대한 칼로리입니다.")
    @Setter
    Integer caloriesPerServing;

    @Schema(description = "FoodDiary에 등록할 음식의 1회 제공량에 대한 지방 함량입니다.")
    @Setter
    Integer fatGramsPerServing;

    @Schema(description = "FoodDiary에 등록할 음식의 1회 제공량에 대한 나트륨 함량입니다.")
    @Setter
    Integer sodiumMilligramsPerServing;

    @Schema(description = "FoodDiary에 등록할 음식의 1회 제공량에 대한 탄수화물 함량입니다.")
    @Setter
    Integer carbsGramsPerServing;

    @Schema(description = "FoodDiary에 등록할 음식의 1회 제공량에 대한 당류 함량입니다.")
    @Setter
    Integer sugarsGramsPerServing;

    @Schema(description = "FoodDiary에 등록할 음식의 1회 제공량에 대한 식이섬유 함량입니다.")
    @Setter
    Integer fiberGramsPerServing;

    @Schema(description = "FoodDiary에 등록할 음식의 1회 제공량에 대한 단백질 함량입니다.")
    @Setter
    Integer proteinGramsPerServing;

    @Schema(description = "FoodDiary에 등록할 음식의 총 칼로리입니다.")
    @Setter
    Integer caloriesTotal;

    @Schema(description = "FoodDiary에 등록할 음식의 총 지방 함량입니다.")
    @Setter
    Integer fatGramsTotal;

    @Schema(description = "FoodDiary에 등록할 음식의 총 나트륨 함량입니다.")
    @Setter
    Integer sodiumMilligramsTotal;

    @Schema(description = "FoodDiary에 등록할 음식의 총 탄수화물 함량입니다.")
    @Setter
    Integer carbsGramsTotal;

    @Schema(description = "FoodDiary에 등록할 음식의 총 당류 함량입니다.")
    @Setter
    Integer sugarsGramsTotal;

    @Schema(description = "FoodDiary에 등록할 음식의 총 식이섬유 함량입니다.")
    @Setter
    Integer fiberGramsTotal;

    @Schema(description = "FoodDiary에 등록할 음식의 총 단백질 함량입니다.")
    @Setter
    Integer proteinGramsTotal;

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
