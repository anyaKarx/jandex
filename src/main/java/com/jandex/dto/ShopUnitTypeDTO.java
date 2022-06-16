package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип элемента - категория или товар
 */
public enum ShopUnitTypeDTO {
    OFFER("OFFER"),
    CATEGORY("CATEGORY");

    private String value;

    ShopUnitTypeDTO(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ShopUnitTypeDTO fromValue(String text) {
        for (ShopUnitTypeDTO b : ShopUnitTypeDTO.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
