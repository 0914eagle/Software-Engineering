package edu.ncsu.csc.itrust2.models.enums;

import lombok.Getter;

@Getter
public enum MealType {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack");

    private final String name;

    MealType(final String name) {
        this.name = name;
    }

    public static MealType parse(final String mealTypeStr) {
        for (final MealType mealType : values()) {
            if (mealType.getName().equals(mealTypeStr)) {
                return mealType;
            }
        }
        return null;
    }
}
