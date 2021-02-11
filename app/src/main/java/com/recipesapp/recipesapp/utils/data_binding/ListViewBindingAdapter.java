package com.recipesapp.recipesapp.utils.data_binding;

import android.widget.ListView;

import androidx.databinding.BindingAdapter;

import com.recipesapp.recipesapp.views.adapters.MyListAdapter;

import java.util.List;

public class ListViewBindingAdapter {
    @BindingAdapter("items")
    public static void setItems(ListView listView, List<String> items) {

        ((MyListAdapter) listView.getAdapter()).submitList(items);
    }
}
