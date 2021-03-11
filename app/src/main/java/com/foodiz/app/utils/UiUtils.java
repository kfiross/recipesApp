package com.foodiz.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.foodiz.app.R;
import com.google.android.material.snackbar.Snackbar;

public class UiUtils {
    public static void showAlertYesNo(
            Context context,
            String title,
            String message,
            DialogInterface.OnClickListener yesListener
    ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.yes), yesListener);
        builder.setNegativeButton(context.getString(R.string.no), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showAlertOk(
            Context context,
            String title,
            String message,
            DialogInterface.OnClickListener okListener
    ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.ok), okListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showSnackbar(View view, String title, Integer duration){
        if(duration == null)
            duration = 5000;

        Snackbar.make(view, title, duration).show();
    }

    public static void showSnackbar(
            View view,
            String title,
            Integer duration,
            String actionName,
            View.OnClickListener actionListener
    ){
        if(duration == null)
            duration = 5000;

        Snackbar.make(view, title, duration)
                .setAction(actionName, actionListener)
                .show();
    }

    public static void enableFullyTransparentStatusBar(Activity activity){
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
    public static void disableFullyTransparentStatusBar(Activity activity){
        activity.getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
        activity.getWindow().setStatusBarColor(activity.getColor(R.color.colorPrimaryDark));
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
