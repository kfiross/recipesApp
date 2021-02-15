package com.recipesapp.recipesapp.utils.data_binding;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.databinding.BindingAdapter;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.recipesapp.recipesapp.R;

import java.util.ArrayList;

public class ChipGroupBindingAdapter {

    @BindingAdapter({"chipsList", "context"})
    public static void chipsList(ChipGroup chipGroup, ArrayList<String> list, Context context){
        if(list == null){
            list = new ArrayList<>();
        }
        chipGroup.removeAllViews();
        for(int i=0; i<list.size(); i++){

            String item = list.get(i);

            Chip chip = new Chip(context);
            chip.setElevation(4.f);
            chip.setChipBackgroundColor(ColorStateList.valueOf(
                    context.getColor(R.color.colorPrimary)
            ));
            chip.setEnabled(true);
            chip.setText(item);
            chip.setMinWidth(220);
            chip.setCloseIcon(context.getDrawable(R.drawable.ic_remove_circle));

            int finalI = i;
            ArrayList<String> finalList = list;
            chip.setOnCloseIconClickListener(v -> {
                chipGroup.removeViewAt(finalI);
                finalList.remove(finalI);
            });
            chipGroup.addView(chip);
        }
    }

}
