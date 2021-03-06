package com.recipesapp.recipesapp.utils;

import android.content.Context;

import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.model.IngredientUnit;


public class StringUtils {
    public static String getLocaleString(
            int resourceId,
            Context context
            //Locale requestedLocale
    ) {
        String result;
        result = context.getText(resourceId).toString();

        return result;
    }

    public static String getIngredientTypeName(Context context, IngredientUnit type, Number count){
        // fix for fractions units
        if(count.doubleValue() % 1.0 != 0){
            return beautifyNum(context, count) + " " + getIngredientTypeName(context, type, 1);
        }
        switch (type){
            case ML:
                return count.intValue() + " " + getLocaleString(R.string.ml, context);
            case GRAMS:
                return count.intValue() + " " + getLocaleString(R.string.g, context);

            case KG:
                return count.intValue() + " " + getLocaleString(R.string.kg, context);

            case SPOONS:
                return context.getResources().getQuantityString(R.plurals.numberSpoons, count.intValue());

            case TEASPOONS:
                return context.getResources().getQuantityString(R.plurals.numberTeaspoons, count.intValue());

            case CUPS:
                return context.getResources().getQuantityString(R.plurals.numberCups, count.intValue());
        }

        return "";
    }

    public static String[] getIngredientsTypesName(Context context) {
        return new String[]{
           //     "Select",
                getIngredientTypeName(context, IngredientUnit.values()[0], 0).split(" ")[1],
                getIngredientTypeName(context, IngredientUnit.values()[1], 0).split(" ")[1],
                getIngredientTypeName(context, IngredientUnit.values()[2], 10).split(" ")[1],
                getIngredientTypeName(context, IngredientUnit.values()[3], 10).split(" ")[1],
                getIngredientTypeName(context, IngredientUnit.values()[4], 10).split(" ")[1],
                getIngredientTypeName(context, IngredientUnit.values()[5], 0).split(" ")[1],
        };
    }

    /**
     * return a number (with fractions) of an item in a clear way as a string,
     */
    public static String beautifyNum(Context context, Number num){
        if(num == null){
            num = 0;
        }
        double dNum = num.doubleValue();

        if(dNum % 1f == 0.0){
            return String.valueOf((int)dNum);
        }
        else if (dNum < 1){
            if (dNum % 1f == 0.25){
                return "1/4";
            }
            if (dNum % 1f - 1/3.d < 0.001){
                return "1/3";
            }
            if (dNum % 1f == 0.5){
                return "1/2";
            }
            if (dNum % 1f == 0.75){
                return "3/4";
            }
            if (dNum % 1f - 2/3.d < 0.001){
                return "2/3";
            }
        }
        else if (dNum > 1){
            return beautifyNum(context, num.intValue()) + " " + getLocaleString(R.string.and, context) + beautifyNum(context, dNum % 1f);
        }
        return String.valueOf(num);
    }
}
