package com.recipesapp.recipesapp.views.fragments.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.databinding.MyDialogFragmentBinding;
import com.recipesapp.recipesapp.model.Recipe;
import com.recipesapp.recipesapp.utils.ScreenSize;
import com.recipesapp.recipesapp.utils.TextChangedListener;
import com.recipesapp.recipesapp.viewmodels.shared.RecipeSharedViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDialogFragment extends DialogFragment {
    private RecipeSharedViewModel vmRecipe;
    private MyDialogFragmentBinding mBinding;
    private int mType;
    private int mSelectedIndex = -1;  // -1 means new
    private String mName;


    public StepDialogFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.my_dialog_fragment, container, false);

        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnAdd.setOnClickListener(v -> add());
        if(mSelectedIndex != -1){
            mBinding.setName(vmRecipe.getSelected().getValue().getSteps().get(mSelectedIndex));
        }
        else{
            mBinding.setName("");
        }
        mBinding.setInEdit(mSelectedIndex != -1);

        mBinding.etName.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
//                mBinding.setName(aNew.trim());
                mBinding.btnAdd.setEnabled(!aNew.trim().isEmpty());
            }
        });
    }

    private void add() {
        String data = mBinding.etName.getText().toString();
        Recipe editedRecipe = vmRecipe.getSelected().getValue();

        if(mSelectedIndex == -1){
            editedRecipe.addStep(data);
        }
        else {
            editedRecipe.updateStep(mSelectedIndex, data);
        }


        vmRecipe.select(editedRecipe);

        this.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = ScreenSize.wp(getContext(), 90);
        params.height = ScreenSize.hp(getContext(), 40);

        getDialog().getWindow().setAttributes(params);
    }
}