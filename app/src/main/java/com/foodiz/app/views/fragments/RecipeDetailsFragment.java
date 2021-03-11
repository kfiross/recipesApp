package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.foodiz.app.utils.FirestoreUtils;
import com.foodiz.app.utils.UiUtils;

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
        mBinding.setIsMy(MainActivity.preferencesConfig.readMyRecipesIds().contains(mSelectedRecipe.getId()));
        mBinding.setNavController(navController);

        mBinding.btnGotoEdit.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putParcelable("recipe", mSelectedRecipe);
            navController.navigate(R.id.addRecipeFragment, args);
        });

        mBinding.btnRemove.setOnClickListener(v -> {

            UiUtils.showAlertYesNo(
                    getContext(),
                    getString(R.string.title_remove_recipe),
                    getString(R.string.subtitle_remove_recipe),
                    (dialog, which) -> {
                        CountDownTimer timer = new CountDownTimer(5 * 1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                FirestoreUtils.removeFromMyFavs(mSelectedRecipe.getId());

                            }
                        };
                        timer.start();

                        UiUtils.showSnackbar(getView(), "Recipe removed!", 4800, "Undo", (_v) -> {
                            timer.cancel();
                        });
                    });
        });
    }
}