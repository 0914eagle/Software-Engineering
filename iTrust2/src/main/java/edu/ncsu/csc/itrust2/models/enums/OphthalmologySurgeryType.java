package edu.ncsu.csc.itrust2.models.enums;

import lombok.Getter;

@Getter
public enum OphthalmologySurgeryType {
    CATARACT_SURGERY("cataract surgery"),

    LASER_SURGERY("laser surgery"),

    REFRACTIVE_SURGERY("refractive surgery");

    private final String name;

    OphthalmologySurgeryType(final String name) {
        this.name = name;
    }

    public static OphthalmologySurgeryType parseValue(final String name) {
        for (final OphthalmologySurgeryType surgeryType : values()) {
            if (surgeryType.getName().equals(name)) {
                return surgeryType;
            }
        }
        return null;
    }
}
