package com.foodiz.app.utils;

import android.content.Context;

import com.foodiz.app.R;

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

    public static String getIngredientTypeName(Context context, int type, double count){
        switch (type){
            case 0:
                return (int)count + " " + getLocaleString(R.string.ml, context);
            case 1:
                return (int)count + " " + getLocaleString(R.string.g, context);

                // todo: fix .5f units
            case 2:
                return context.getResources().getQuantityString(R.plurals.numberSpoons, (int)count);

            case 3:
                return context.getResources().getQuantityString(R.plurals.numberTeaspoons, (int)count);

            case 4:
                return context.getResources().getQuantityString(R.plurals.numberGlasses, (int)count);
        }

        return "";
    }

    public static String[] getIngredientsTypesName(Context context) {
        return new String[]{
                getIngredientTypeName(context, 0, 0).split(" ")[1],
                getIngredientTypeName(context, 1, 0).split(" ")[1],
                getIngredientTypeName(context, 2, 10).split(" ")[1],
                getIngredientTypeName(context, 3, 10).split(" ")[1],
                getIngredientTypeName(context, 4, 10).split(" ")[1],
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
