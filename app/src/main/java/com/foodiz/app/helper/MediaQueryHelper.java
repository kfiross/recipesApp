package com.foodiz.app.helper;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Helper class to get percentage of sizes easily (used as MediaQuery)
 */
public class MediaQueryHelper {
    /**
     * @param context the app context
     * @param percents how much height of the side, in percents
     */
    public static int hp(Context context, double percents) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        return (int)(height * (percents / 100f));
    }

    /**
     * @param context the app context
     * @param percents how much width of the side, in percents
     */
    public static int  wp(Context context, double percents) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        return (int)(width * (percents / 100f));
    }
}
