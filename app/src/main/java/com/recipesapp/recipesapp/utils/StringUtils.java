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

    public static String getIngredientTypeName(Context context, IngredientUnit type, double count){
        if(count % 1.0 != 0){
            return beautifyNum(context, count) + " " + getIngredientTypeName(context, type, 1);
        }
        switch (type){
            case ML:
                return (int)count + " " + getLocaleString(R.string.ml, context);
            case GRAMS:
                return (int)count + " " + getLocaleString(R.string.g, context);

            case KG:
                return (int)count + " " + getLocaleString(R.string.kg, context);

                // todo: fix .5f units
            case SPOONS:
                return context.getResources().getQuantityString(R.plurals.numberSpoons, (int)count);

            case TEASPOONS:
                return context.getResources().getQuantityString(R.plurals.numberTeaspoons, (int)count);

            case CUPS:
                return context.getResources().getQuantityString(R.plurals.numberCups, (int)count);
        }

        return "";
    }

    public static String[] getIngredientsTypesName(Context context) {
        return new String[]{
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
    public static String beautifyNum(Context context, double num){
        if(num % 1f == 0.0){
            return String.valueOf((int)num);
        }
        else if (num < 1){
            if (num % 1f == 0.25){
                return "1/4";
            }
            if (num % 1f - 1/3.d < 0.001){
                return "1/3";
            }
            if (num % 1f == 0.5){
                return "1/2";
            }
            if (num % 1f == 0.75){
                return "3/4";
            }
            if (num % 1f - 2/3.d < 0.001){
                return "2/3";
            }
        }
        else if (num > 1){
            return beautifyNum(context, (int)num) + " " + getLocaleString(R.string.and, context) + beautifyNum(context, num % 1f);
        }
        return String.valueOf(num);
    }
}
