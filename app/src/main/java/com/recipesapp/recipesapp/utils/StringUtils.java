package com.recipesapp.recipesapp.utils;

import android.content.Context;
import android.content.res.Configuration;

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
}
