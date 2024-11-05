package com.cajucard.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MCC {
    MCC_5411("5411"),
    MCC_5412("5412"),
    MCC_5811("5811"),
    MCC_5812("5812"),
    MCC_0000("0000");

    private final String value;

    MCC(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MCC fromValue(String value) {
        for (MCC mcc : MCC.values()) {
            if (mcc.value.equals(value)) {
                return mcc;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
