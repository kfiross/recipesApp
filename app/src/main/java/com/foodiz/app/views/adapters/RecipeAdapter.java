package com.foodiz.app.views.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.model.Recipe;
import com.foodiz.app.databinding.ItemRecipeLayoutBinding;
import com.foodiz.app.utils.FirestoreUtils;
import com.foodiz.app.utils.StringUtils;
import com.foodiz.app.utils.UiUtils;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ItemViewHolder>{

    private ArrayList<Recipe> mRecipes;
    private final ArrayList<String> mFavIds;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        mFavIds = new ArrayList<>(MainActivity.preferencesConfig.readFavsIds());
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

    public void setItems(ArrayList<Recipe> newList) {
        mRecipes = newList;
        notifyDataSetChanged();
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
                // remove if already in favourites
                if(mItemBinding.getIsFav()){
                    MainActivity.preferencesConfig.removeFavId(recipe.getId());
                    FirestoreUtils.delRecipeFromFev(recipe.getId());

                    UiUtils.showSnackbar(
                            itemView,
                            StringUtils.getLocaleString(R.string.removed_from_fav, itemView.getContext()),
                            null,
                            StringUtils.getLocaleString(R.string.undo, itemView.getContext()),
                            view -> {
                                // add it back
                                MainActivity.preferencesConfig.addFavId(recipe.getId());
                                FirestoreUtils.addRecipeToFev(recipe.getId());
                            }
                    );
                }
                // if not, add it
                else{
                    MainActivity.preferencesConfig.addFavId(recipe.getId());
                    FirestoreUtils.addRecipeToFev(recipe.getId());

                    UiUtils.showSnackbar(
                            itemView,
                            StringUtils.getLocaleString(R.string.add_to_fav, itemView.getContext()),
                            null
                    );

                }
                // update UI
                mItemBinding.setIsFav(!mItemBinding.getIsFav());
            });
        }
    }
}
