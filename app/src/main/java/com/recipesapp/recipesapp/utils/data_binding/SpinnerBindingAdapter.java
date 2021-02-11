package com.recipesapp.recipesapp.utils.data_binding;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import java.util.ArrayList;

public class SpinnerBindingAdapter {

    @BindingAdapter(value = {"items", "itemLayout", "dropDownLayout"})
    public static void setItems(Spinner spinner, ArrayList<String> items, int itemLayout, int dropDownLayout) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                itemLayout,
               // R.layout.my_spinner_item_layout,
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

//    @BindingAdapter("dropDownLayout")
//    public static void setDropDownLayout(Spinner spinner, int dropDownLayout) {
//        try {
//            ((ArrayAdapter<String>) spinner.getAdapter()).setDropDownViewResource(dropDownLayout);
//        }
//        catch (Exception ignored){
//
//        }
//
//    }
}
