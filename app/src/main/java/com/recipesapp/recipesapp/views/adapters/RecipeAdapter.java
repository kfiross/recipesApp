package com.recipesapp.recipesapp.views.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Recipe;
import com.recipesapp.recipesapp.databinding.ItemRecipeLayoutBinding;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ItemViewHolder>{

    private ArrayList<Recipe> mRecipes;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRecipeLayoutBinding itemBinding =
                ItemRecipeLayoutBinding.inflate(layoutInflater, parent, false);

        return new ItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ItemViewHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private final NavController mNavController;
        private final ItemRecipeLayoutBinding mItemBinding;

        public ItemViewHolder(ItemRecipeLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            mItemBinding = itemBinding;
            mNavController = Navigation.findNavController(
                    (MainActivity) itemBinding.getRoot().getContext(), R.id.nav_host_fragment);
        }

        public void bind(Recipe recipe) {
            mItemBinding.setRecipe(recipe);
            mItemBinding.getRoot().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putParcelable("recipe", recipe);
                mNavController.navigate(R.id.recipeDetailsFragment, args);
            });
        }
    }
}
