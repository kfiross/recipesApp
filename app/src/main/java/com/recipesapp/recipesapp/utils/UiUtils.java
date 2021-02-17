package com.recipesapp.recipesapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

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
}
