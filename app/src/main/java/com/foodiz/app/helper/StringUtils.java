package com.foodiz.app.helper;

import android.content.Context;

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
