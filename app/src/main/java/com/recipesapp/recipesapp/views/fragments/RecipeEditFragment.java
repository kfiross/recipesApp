package com.recipesapp.recipesapp.views.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.model.Recipe;
import com.recipesapp.recipesapp.databinding.FragmentAddRecipeBinding;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.utils.TextChangedListener;
import com.recipesapp.recipesapp.utils.UiUtils;
import com.recipesapp.recipesapp.viewmodels.shared.RecipeSharedViewModel;
import com.recipesapp.recipesapp.views.adapters.MyListAdapter2;
import com.recipesapp.recipesapp.views.fragments.dialogs.IngredientEditDialogFragment;
import com.recipesapp.recipesapp.views.fragments.dialogs.MyDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeEditFragment extends BaseFragment {

    private FragmentAddRecipeBinding mBinding;
    private RecipeSharedViewModel vmRecipe;
    private Recipe mRecipeData;
    private Observer<Recipe> observer;
    private NavController mNavController;

    public RecipeEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        vmRecipe = ViewModelProviders.of(getActivity()).get(RecipeSharedViewModel.class);
        vmRecipe.select(new Recipe());

        Bundle args = getArguments();
        if(args != null){
            mRecipeData = args.getParcelable("recipe");
        }
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

        mBinding.setRecipe(mRecipeData == null ? new Recipe() : mRecipeData);
        mBinding.setEditMode(true);

        ((MainActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        setUpViews();

        vmRecipe.select(mRecipeData);
        observer = (Observer<Recipe>) recipe -> {
            mBinding.setRecipe(recipe);
            mBinding.setCanAddRecipe(checkForm());

        };
        vmRecipe.getSelected().observe(getActivity(), observer);
    }

    private void setUpViews() {
        setupButtons();
        setupLists();
        setupForm();

        mBinding.formLayout.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mBinding.formLayout.btnAddIngredient.setOnClickListener(v -> openDialog(0, null));
        mBinding.formLayout.btnAddStep.setOnClickListener(v -> openDialog(1, null));
        mBinding.btnAddRecipe.setOnClickListener(v -> updateRecipe());
        mBinding.formLayout.btnAddPhoto.setOnClickListener(v -> addPhoto());
    }

    private void setupLists() {
        mBinding.formLayout.listIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.formLayout.listIngredients.setHasFixedSize(true);

        mBinding.formLayout.listIngredients.setAdapter(new MyListAdapter2(
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

        mBinding.formLayout.listSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.formLayout.listSteps.setHasFixedSize(true);

        mBinding.formLayout.listSteps.setAdapter(new MyListAdapter2(
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
        mBinding.formLayout.etName.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                mBinding.getRecipe().setName(aNew);
                mBinding.setCanAddRecipe(checkForm());
            }
        });

        mBinding.formLayout.etTime.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                try {
                    mBinding.getRecipe().setMakingTime(Integer.parseInt(aNew));

                }
                catch (Exception ignored){

                }
                mBinding.setCanAddRecipe(checkForm());
            }
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

    private void updateRecipe(){
        // add to recipes
        FirestoreUtils.updateRecipe(mBinding.getRecipe())
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                UiUtils.showAlertOk(
                        getContext(),
                        "Hooray!",
                        "We Updated your Recipe!",
                        (dialog, which) -> {}
                );
            }
            else{
                UiUtils.showAlertOk(
                        getContext(),
                        "Oops! something went wrong",
                        "We couldn't update your Recipe...",
                        (dialog, which) -> {}
                );
            }
        });
        resetForm();
    }

    private void deleteRecipe(){
        // add to recipes
        FirestoreUtils.deleteRecipe(mBinding.getRecipe().getId())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        UiUtils.showAlertOk(
                                getContext(),
                                "Action completed successfully",
                                "Your recipe is deleted!",
                                (dialog, which) -> {
                                    mNavController.popBackStack();
                                }
                        );
                    }
                    else{
                        UiUtils.showAlertOk(
                                getContext(),
                                "Oops! something went wrong",
                                "We couldn't delete your Recipe...",
                                (dialog, which) -> {}
                        );
                    }
                });
        resetForm();
    }

    private void resetForm(){
        // mBinding.setRecipe(new Recipe());
        mBinding.formLayout.etName.setText("");
        mBinding.formLayout.etTime.setText("");
        vmRecipe.select(new Recipe());
        mBinding.setCanAddRecipe(false);
    }

    private void addPhoto(){
        if(mBinding.getImageUri() != null){
            UiUtils.showAlertOk(
                   getContext(),
                   getString(R.string.sorry),
                   getString(R.string.error_one_photo_only),
                   null
            );
            return;
        }
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2000);
        }
        else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                mBinding.setImageUri(returnUri);
            }
        }
    }

    private boolean checkForm(){
        boolean result = true;
        if(mBinding.formLayout.etName.length() == 0)
            result = false;
        else if(mBinding.formLayout.etTime.length() == 0)
            result = false;
        else if(mBinding.formLayout.getRecipe().getIngredients().isEmpty())
            result = false;
        else if(mBinding.formLayout.getRecipe().getSteps().isEmpty())
            result = false;

        Log.d("checkForm", String.valueOf(result));

        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vmRecipe.getSelected().removeObserver(observer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}