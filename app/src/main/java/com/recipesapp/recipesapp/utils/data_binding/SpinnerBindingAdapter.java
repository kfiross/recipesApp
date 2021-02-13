package com.recipesapp.recipesapp.utils.data_binding;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import java.util.ArrayList;

public class SpinnerBindingAdapter {

    @BindingAdapter(value = {"items", "itemLayout", "dropDownLayout"})
    public static void setItems(Spinner spinner, ArrayList<String> items, int itemLayout, int dropDownLayout) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                itemLayout,
                items
        );

        spinner.setAdapter(adapter);
    }


    @BindingAdapter(value = {"items", "itemLayout", "dropDownLayout"})
    public static void setItems(Spinner spinner, String[] items, int itemLayout, int dropDownLayout) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                itemLayout,
                items
        );

        adapter.setDropDownViewResource(dropDownLayout);

        spinner.setAdapter(adapter);
    }


    @BindingAdapter(value = {"items", "itemLayout", "dropDownLayout", "startPosition"})
    public static void setItems(Spinner spinner, String[] items, int itemLayout, int dropDownLayout, int startPosition) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                itemLayout,
                items
        );

        adapter.setDropDownViewResource(dropDownLayout);

        spinner.setAdapter(adapter);

        spinner.setSelection(startPosition);
    }


    @BindingAdapter(value = {"items", "itemLayout", "dropDownLayout", "startPosition"})
    public static void setItems(Spinner spinner, ArrayList<String> items, int itemLayout, int dropDownLayout, int startPosition) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                itemLayout,
                items
        );

        adapter.setDropDownViewResource(dropDownLayout);

        spinner.setAdapter(adapter);

        spinner.setSelection(startPosition);
    }
}
