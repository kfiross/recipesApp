package com.recipesapp.recipesapp.views.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.recipesapp.recipesapp.databinding.ListEditItemContentBinding;
import com.recipesapp.recipesapp.databinding.ListItemContentBinding;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    private List<String> items;
    private @Nullable Integer mType;
    private @Nullable Boolean inEditMode;
    private Function<Integer, Boolean> onDelete;
    private Function<Integer, Boolean> onEdit;

    public MyListAdapter(@NonNull Context context, List<String> items, @Nullable Integer type) {
        this.items = items;
        mType = type;
        this.inEditMode = false;
    }

    public MyListAdapter(@NonNull Context context, List<String> items, @Nullable Integer type, Function<Integer, Boolean> onDelete, Function<Integer, Boolean> onEdit) {
        this.items = items;
        mType = type;
        this.inEditMode = true;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ViewDataBinding binding;


        if(inEditMode==null || !inEditMode){
            binding = ListItemContentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );

        }
        else {
            binding = ListEditItemContentBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
        }

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    public void submitList(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        this.items.remove(index);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding mBinding;

        public ViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(int position) {
            String item = items.get(position);
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
                ((ListItemContentBinding) mBinding).setItem(item);
            }
            else {
                ((ListEditItemContentBinding) mBinding).setItem(item);
                ((ListEditItemContentBinding) mBinding).btnDeleteItem.setOnClickListener(v -> {
                    removeItem(position);
                    onDelete.apply(position);
                });
                ((ListEditItemContentBinding) mBinding).btnEditItem.setOnClickListener(v -> {
                    onEdit.apply(position);
                });
            }
        }


    }


}
