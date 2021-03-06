package com.recipesapp.recipesapp.model;

import java.util.HashMap;
import java.util.Map;

public enum IngredientUnit {
    ML(0),
    GRAMS(1),
    SPOONS(2),
    TEASPOONS(3),
    CUPS(4),
    KG(5);

    private final int value;

    IngredientUnit(int value) {
        this.value = value;
    }

    private static final Map<Integer, IngredientUnit> mapping = new HashMap<>();

    static {
        for (IngredientUnit unit : IngredientUnit.values()) {
            mapping.put(unit.value, unit);
        }
    }

    public static IngredientUnit fromInt(int value) {
        return mapping.get(value);
    }
}