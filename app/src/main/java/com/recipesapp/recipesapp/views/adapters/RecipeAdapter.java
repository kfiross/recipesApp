package com.recipesapp.recipesapp.views.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Recipe;
import com.recipesapp.recipesapp.databinding.ItemRecipeLayoutBinding;
import com.recipesapp.recipesapp.utils.FirestoreUtils;

import java.util.ArrayList;
import java.util.function.Function;

import kotlin.jvm.functions.Function2;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ItemViewHolder>{

    private ArrayList<Recipe> mRecipes;
    private ArrayList<String> mFavIds;

    public RecipeAdapter(ArrayList<Recipe> recipes, ArrayList<String> favIds) {
        mRecipes = recipes;
        mFavIds = favIds;
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
            mItemBinding.setIsFav(mFavIds.contains(recipe.getId()));

            mItemBinding.getRoot().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putParcelable("recipe", recipe);
                mNavController.navigate(R.id.recipeDetailsFragment, args);
            });

            mItemBinding.btnAddToFav.setOnClickListener(v -> {
                // remove if already in favs
                if(mItemBinding.getIsFav()){
                    MainActivity.preferencesConfig.removeFavId(recipe.getId());
                    FirestoreUtils.removeFromMyFavs(recipe.getId());
                }
                // if not, add it
                else{
                    MainActivity.preferencesConfig.addFavId(recipe.getId());
                    FirestoreUtils.addToMyFavs(recipe.getId());
                }
                // update UI
                mItemBinding.setIsFav(!mItemBinding.getIsFav());
            });
        }
    }
}
