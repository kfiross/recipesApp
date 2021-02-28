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
import com.recipesapp.recipesapp.model.Ingredient;
import com.recipesapp.recipesapp.model.Recipe;
import com.recipesapp.recipesapp.databinding.DialogEditIngredientFragmentBinding;
import com.recipesapp.recipesapp.utils.Constants;
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
    private int mSelectedIndex = -1;  // -1 means new

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
        if(args != null) {
            mSelectedIndex = args.getInt("index", -1);
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

//        ingredient = new Ingredient("", 0 , 0);
//        mBinding.setIngredient(ingredient);
     ///   mBinding.setQuantity(100.0);
        mBinding.setType(1);
        mBinding.setIsWithCount(true);



        mBinding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBinding.setType(position);
                switch (mBinding.getType()){
                    case 0:
                    case 1:
                        mBinding.setQuantity(100.0);
                        break;

                    case 2:
                    case 3:
                    case 4:
                        mBinding.setQuantity(1.0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.btnAddQuantity.setOnClickListener(v -> {
            double prevQuantity = mBinding.getQuantity();

            switch (mBinding.getType()){
                case 0:
                case 1:
                    mBinding.setQuantity(prevQuantity+10);
                    break;

                case 2:
                case 3:
                    mBinding.setQuantity(prevQuantity+0.5);
                    break;

                case 4:
                    int prevPos = 0;
                    while(prevQuantity > Constants.SPOON_UNITS[prevPos])
                        prevPos++;

                    mBinding.setQuantity(Constants.SPOON_UNITS[prevPos + 1]);
                    break;
            }



        });

        mBinding.btnSubstructQuantity.setOnClickListener(v -> {
            double prevQuantity = mBinding.getQuantity();


            switch (mBinding.getType()){
                case 0:
                case 1:
                    if(prevQuantity >= 20) {
                        mBinding.setQuantity(prevQuantity - 10);
                    }
                    break;

                case 2:
                case 3:
                    if(prevQuantity > 0) {
                        mBinding.setQuantity(prevQuantity-0.5);

                    }
                    break;

                case 4:
                    int prevPos = 0;
                    while(prevQuantity > Constants.SPOON_UNITS[prevPos])
                        prevPos++;

                    if(prevPos > 1){
                        mBinding.setQuantity(Constants.SPOON_UNITS[prevPos - 1]);
                    }
                    break;
            }

        });

        mBinding.btnAdd.setOnClickListener(v -> add());

        mBinding.checkWithCount.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mBinding.setType(isChecked ? 0 : -1);
            mBinding.setQuantity(isChecked ? 100D : 0);
            mBinding.setIsWithCount(isChecked);

        });
    }

    private void add() {
        String name = mBinding.editTextData.getText().toString();
        double count = mBinding.getQuantity();
        Integer type = mBinding.getType() == -1 ? null : mBinding.getType();

        Recipe editedRecipe = vmRecipe.getSelected().getValue();


        if(mSelectedIndex == -1){
            editedRecipe.getIngredients().add(new Ingredient(name, count ,type));
        }
        else {
            editedRecipe.updateIngredient(mSelectedIndex, new Ingredient(name, count ,type));
        }



        vmRecipe.select(editedRecipe);
        this.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = ScreenSize.wp(getContext(), 93);
        params.height = ScreenSize.hp(getContext(), 50);

        getDialog().getWindow().setAttributes(params);
    }
}