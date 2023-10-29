package edu.ncsu.csc.itrust2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class FoodDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String patientName;
    String date;
    String mealType;
    String foodName;
    String servingsNum;
    String caloriesPerServing;
    String fatGramsPerServing;
    String sodiumMilligramsPerServing;
    String carbsGramsPerServing;
    String sugarsGramsPerServing;
    String fiberGramsPerServing;
    String proteinGramsPerServing;
    String fatGramsTotal;
    String sodiumMilligramsTotal;
    String carbsGramsTotal;
    String sugarsGramsTotal;
    String fiberGramsTotal;
    String proteinGramsTotal;
}
