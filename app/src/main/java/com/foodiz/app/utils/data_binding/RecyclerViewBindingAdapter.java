package com.foodiz.app.utils.data_binding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.foodiz.app.views.adapters.MyListAdapter;

import java.util.List;

public class RecyclerViewBindingAdapter {
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<String> items) {

        ((MyListAdapter) recyclerView.getAdapter()).submitList(items);
    }
}
