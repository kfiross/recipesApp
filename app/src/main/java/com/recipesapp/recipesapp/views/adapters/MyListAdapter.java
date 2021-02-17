package com.recipesapp.recipesapp.views.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;

import com.recipesapp.recipesapp.databinding.ListEditItemContentBinding;
import com.recipesapp.recipesapp.databinding.ListItemContentBinding;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class MyListAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private @Nullable Integer mType;
    private @Nullable Boolean inEditMode;
    private Function<Integer, Boolean> onDelete;
    private Function<Integer, Boolean> onEdit;

    public MyListAdapter(@NonNull Context context,  List<String> items, @Nullable Integer type) {
        super(context, 0, items);
        this.items = items;
        mType = type;
        this.inEditMode = false;
    }

    public MyListAdapter(@NonNull Context context, List<String> items, @Nullable Integer type, Function<Integer, Boolean> onDelete, Function<Integer, Boolean> onEdit) {
        super(context, 0, items);
        this.items = items;
        mType = type;
        this.inEditMode = true;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewDataBinding binding;

        String item = this.items.get(position);
        if(mType != null){
            switch (mType){
                case 0:
                    item = String.format(new Locale("he"),"• %s", item);
                    break;

                case 1:
                    item = String.format(new Locale("he"), "%d. %s", position+1, item);
                    break;
            }
        }

        if(inEditMode==null || !inEditMode){
            binding = ListItemContentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );

            ((ListItemContentBinding) binding).setItem(item);
        }
        else {
            binding = ListEditItemContentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );

            ((ListEditItemContentBinding) binding).setItem(item);
            ((ListEditItemContentBinding) binding).btnDeleteItem.setOnClickListener(v -> {
                onDelete.apply(position);
                removeItem(position);
            });
            ((ListEditItemContentBinding) binding).btnEditItem.setOnClickListener(v -> {
                onEdit.apply(position);
            });
        }

        return binding.getRoot();
    }


    @Override
    public int getCount(){
        return items ==null ? 0 :items.size();
    }


    public void submitList(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        this.items.remove(index);
        notifyDataSetChanged();
    }
}