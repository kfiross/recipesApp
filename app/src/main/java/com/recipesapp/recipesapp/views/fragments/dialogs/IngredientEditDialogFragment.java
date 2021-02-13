package com.recipesapp.recipesapp.views.fragments.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Ingredient;
import com.recipesapp.recipesapp.data.model.Recipe;
import com.recipesapp.recipesapp.databinding.DialogEditIngredientFragmentBinding;
import com.recipesapp.recipesapp.utils.ScreenSize;
import com.recipesapp.recipesapp.viewmodels.shared.RecipeSharedViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientEditDialogFragment extends DialogFragment {
    private RecipeSharedViewModel vmRecipe;
    private DialogEditIngredientFragmentBinding mBinding;
    private int mType;

    private String mIngredientName;
    private int mIngredientQuantity;
    private int mIngredientQuantityType;

    public IngredientEditDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vmRecipe = ViewModelProviders.of(getActivity()).get(RecipeSharedViewModel.class);
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt("type");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.dialog_edit_ingredient_fragment, container, false);

        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mType == 0){
            mBinding.setTitle("Enter Ingredient");
        }
        else if(mType == 1){
            mBinding.setTitle("Enter Step");
        }

//        ingredient = new Ingredient("", 0 , 0);
//        mBinding.setIngredient(ingredient);
        mBinding.setQuantity(100);
        mBinding.setType(1);



        mBinding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBinding.setType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.btnAddQuantity.setOnClickListener(v -> {
            int prevQuantity = mBinding.getQuantity();
            mBinding.setQuantity(prevQuantity+10);

        });

        mBinding.btnSubstructQuantity.setOnClickListener(v -> {
            int prevQuantity = mBinding.getQuantity();
            // minimum is 10 grams
            if(prevQuantity >= 20) {
                mBinding.setQuantity(prevQuantity - 10);
            }

        });
        mBinding.btnAdd.setOnClickListener(v -> add());
    }

    private void add() {
        String name = mBinding.editTextData.getText().toString();
        int count = mBinding.getQuantity();
        int type = mBinding.getType();

        Recipe editedRecipe = vmRecipe.getSelected().getValue();

        editedRecipe.getIngredients().add(new Ingredient(name, count ,type));

        vmRecipe.select(editedRecipe);
        this.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = ScreenSize.wp(getContext(), 90);
        params.height = ScreenSize.hp(getContext(), 50);

        getDialog().getWindow().setAttributes(params);
    }
}