package com.recipesapp.recipesapp.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Recipe;
import com.recipesapp.recipesapp.databinding.FragmentAddRecipeBinding;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.utils.StringUtils;
import com.recipesapp.recipesapp.utils.TextChangedListener;
import com.recipesapp.recipesapp.utils.UiUtils;
import com.recipesapp.recipesapp.viewmodels.shared.RecipeSharedViewModel;
import com.recipesapp.recipesapp.views.adapters.MyListAdapter;
import com.recipesapp.recipesapp.views.fragments.dialogs.MyDialogFragment;
import com.recipesapp.recipesapp.views.fragments.dialogs.IngredientEditDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecipeFragment extends BaseFragment {

    private FragmentAddRecipeBinding mBinding;
    private RecipeSharedViewModel vmRecipe;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vmRecipe = ViewModelProviders.of(getActivity()).get(RecipeSharedViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_recipe, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize binding variables
        mBinding.setName(StringUtils.getLocaleString(R.string.add_new_recipe, getContext()));


        mBinding.setRecipe(new Recipe());

        ((MainActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        setUpViews();
        vmRecipe.select(mBinding.getRecipe());
    }

    private void setUpViews() {
        setupButtons();
        setupLists();
        setupForm();

        mBinding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBinding.getRecipe().setCategory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBinding.getRecipe().setCategory(0);
            }
        });
    }


    private void setupButtons() {
        mBinding.btnAddIngredient.setOnClickListener(v -> openDialog(0, null));
        mBinding.btnAddStep.setOnClickListener(v -> openDialog(1, null));
        mBinding.btnAddRecipe.setOnClickListener(v -> addRecipe());
    }

    private void setupLists() {
        mBinding.listIngredients.setAdapter(new MyListAdapter(
                        getContext(),
                        new ArrayList<>(),
                        0,
                        (index) -> {
                            //openDialog(0, index);
                            vmRecipe.getSelected().getValue().getIngredients().remove(index);
                            return true;
                        },
                        (index) -> {
                            openDialog(0, index);
                            return true;
                        }
                )
        );
        mBinding.listSteps.setAdapter(new MyListAdapter(
                        getContext(),
                        new ArrayList<>(),
                        1,
                        (index) -> {
                            vmRecipe.getSelected().getValue().getSteps().remove(index);
                            return true;
                        },
                        (index) -> {
                            openDialog(1, index);
                            return true;
                        }
                )
        );
    }

    private void setupForm() {
        mBinding.etName.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                mBinding.getRecipe().setName(aNew);
            }
        });

        mBinding.etTime.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                try {
                    mBinding.getRecipe().setMakingTime(Integer.parseInt(aNew));
                }
                catch (Exception ignored){

                }
            }
        });


        vmRecipe.getSelected().observe(getActivity(), recipe -> {
            mBinding.setRecipe(recipe);
//            mBinding.getFormData().setIngredientList(recipe.getIngredients());
//            mBinding.getFormData().setStepsList(recipe.getSteps());
        });
    }

    private void openDialog(int type, @Nullable Integer index) {
        if(type == 0){
            IngredientEditDialogFragment editIngredientDialog = new IngredientEditDialogFragment();
            Bundle args = new Bundle();
            args.putInt("type", type);
            args.putParcelable("recipe", mBinding.getRecipe());
            if(index != null) {
                args.putInt("index", index);
            }
            editIngredientDialog.setArguments(args);
            editIngredientDialog.show(MainActivity.appFragmentManager, "editIngredientDialog");
        }
        else if (type == 1){
            MyDialogFragment editDialog = new MyDialogFragment();
            Bundle args = new Bundle();
            args.putInt("type", 1);
            args.putParcelable("recipe", mBinding.getRecipe());
            if(index != null) {
                args.putInt("index", index);
            }
            editDialog.setArguments(args);
            editDialog.show(MainActivity.appFragmentManager, "Dialog");
        }


    }

    private void addRecipe(){
        // add to recipes
        FirestoreUtils.addRecipe(mBinding.getRecipe()).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()) {
                final String newRecipeId = task1.getResult().getId();
                FirestoreUtils.addToMyRecipes(newRecipeId).addOnCompleteListener(task2 -> {
                    if(task2.isSuccessful()){
                        UiUtils.showAlertOk(
                                getContext(),
                                "Hooray!",
                                "We added your Recipe!",
                                (dialog, which) -> {}
                        );
                    }
                    else{
                        UiUtils.showAlertOk(
                                getContext(),
                                "Oops! something went wrong",
                                "We couldn't add your Recipe...",
                                (dialog, which) -> {}
                        );
                    }
                });
            }
        });
        resetForm();
    }

    private void resetForm(){
        // mBinding.setRecipe(new Recipe());
        mBinding.etName.setText("");
        mBinding.etTime.setText("");
        vmRecipe.select(new Recipe());
    }
}