package com.recipesapp.recipesapp.utils.data_binding;

import android.widget.ListView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.recipesapp.recipesapp.data.model.Ingredient;
import com.recipesapp.recipesapp.views.adapters.MyListAdapter;
import com.recipesapp.recipesapp.views.adapters.MyListAdapter2;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewBindingAdapter {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<String> items) {

        ((MyListAdapter2) recyclerView.getAdapter()).submitList(items);
    }
}
