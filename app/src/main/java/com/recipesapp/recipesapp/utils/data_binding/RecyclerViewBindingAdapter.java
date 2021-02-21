package com.recipesapp.recipesapp.utils.data_binding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.recipesapp.recipesapp.views.adapters.MyListAdapter;

import java.util.List;

public class RecyclerViewBindingAdapter {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<String> items) {

        ((MyListAdapter) recyclerView.getAdapter()).submitList(items);
    }
}
