package com.recipesapp.recipesapp.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.recipesapp.recipesapp.R;

import java.util.Locale;

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

    public static String getIngredientTypeName(Context context, int type, int count){
        switch (type){
            case 0:
                return count + " " + getLocaleString(R.string.ml, context);
            case 1:
                return count + " " + getLocaleString(R.string.g, context);

            case 2:
                return context.getResources().getQuantityString(R.plurals.numberSpoons, count);

            case 3:
                return context.getResources().getQuantityString(R.plurals.numberTeaspoons, count);

//            case 4:
//                return context.getResources().getQuantityString(R.plurals.numberTeaspoons, count);
        }

        return "";
    }

    public static String[] getIngredientsTypesName(Context context) {
        return new String[]{
                getIngredientTypeName(context, 0, 0).split(" ")[1],
                getIngredientTypeName(context, 1, 0).split(" ")[1],
                getIngredientTypeName(context, 2, 10).split(" ")[1],
                getIngredientTypeName(context, 3, 10).split(" ")[1],
        };
    }
}
