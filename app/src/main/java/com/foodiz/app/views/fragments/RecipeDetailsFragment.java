package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.databinding.FragmentRecipeDetailsBinding;
import com.foodiz.app.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {

    private Recipe mSelectedRecipe;
    private FragmentRecipeDetailsBinding mBinding;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mSelectedRecipe = args.getParcelable("recipe");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_details, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mBinding.setRecipe(mSelectedRecipe);
        mBinding.setIsRTL(true);
        mBinding.setIsMy(MainActivity.preferencesConfig.readMyRecipesIds().contains(mSelectedRecipe.getId()));
        mBinding.setNavController(navController);

        mBinding.btnGotoEdit.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putParcelable("recipe", mSelectedRecipe);
            navController.navigate(R.id.recipeEditFragment, args);
        });
    }
}