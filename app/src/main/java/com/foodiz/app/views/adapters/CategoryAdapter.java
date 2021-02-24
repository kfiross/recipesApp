package com.foodiz.app.views.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.model.Category;
import com.foodiz.app.databinding.ItemCategoryLayoutBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>{

    private ArrayList<Category> mCategories;

    public CategoryAdapter(ArrayList<Category> categories) {
        mCategories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCategoryLayoutBinding itemBinding =
                ItemCategoryLayoutBinding.inflate(layoutInflater, parent, false);

        return new ItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ItemViewHolder holder, int position) {
        holder.bind(mCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private final NavController mNavController;
        private final ItemCategoryLayoutBinding itemBinding;

        public ItemViewHolder(ItemCategoryLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            mNavController = Navigation.findNavController(
                    (MainActivity) itemBinding.getRoot().getContext(), R.id.nav_host_fragment);
        }

        public void bind(Category category) {
            itemBinding.setName(category.getName().get("he"));

            int resId = Category.getBgById(category.getId());
            itemBinding.setBackground(
                    ResourcesCompat.getDrawable(itemView.getResources(), resId, null)

            );

            itemBinding.getRoot().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putInt("category_id", Integer.parseInt(category.getId()));
                args.putString("category_name", category.getName().get("he"));
                mNavController.navigate(R.id.recipesFragment, args);
            });
        }
    }
}
