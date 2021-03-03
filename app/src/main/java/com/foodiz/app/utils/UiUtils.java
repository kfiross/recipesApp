package com.foodiz.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

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

        builder.setPositiveButton("Yes", yesListener);
        builder.setPositiveButton("No", null);

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

        builder.setPositiveButton("OK", okListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showToastShort(Context context, String title){
        Toast.makeText(context, title, Toast.LENGTH_LONG).show();
    }

    public static void showToastLong(Context context, String title){
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
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
}
