package com.foodiz.app.views.fragments;

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

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.databinding.FragmentAddRecipeBinding;
import com.foodiz.app.model.Recipe;
import com.foodiz.app.utils.FirestoreUtils;
import com.foodiz.app.utils.TextChangedListener;
import com.foodiz.app.utils.UiUtils;
import com.foodiz.app.viewmodels.shared.RecipeSharedViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding mBinding;
    private RecipeSharedViewModel vmRecipe;
    private Observer<Recipe> observer;

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

        mBinding.setRecipe(new Recipe());
        mBinding.setEditMode(false);
        
        setUpViews();
        vmRecipe.select(mBinding.getRecipe());

        observer = recipe -> {
            mBinding.setRecipe(recipe);
            mBinding.setCanAddRecipe(checkForm());
        };
        vmRecipe.getSelected().observe(getActivity(), observer);

    }

    private void setUpViews() {
        setupButtons();
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
        mBinding.btnAddRecipe.setOnClickListener(v -> addRecipe());

        mBinding.formLayout.imgRecipe.setOnClickListener(v -> uploadPhoto());
    }


    private void setupForm() {
        mBinding.formLayout.etName.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                mBinding.getRecipe().setName(aNew);
                mBinding.setCanAddRecipe(checkForm());
            }
        });

        mBinding.formLayout.etIngredients.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                mBinding.getRecipe().setIngredients(aNew);
                mBinding.setCanAddRecipe(checkForm());
            }
        });

        mBinding.formLayout.etSteps.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                mBinding.getRecipe().setSteps(aNew);
                mBinding.setCanAddRecipe(checkForm());
            }
        });






    }

    private void addRecipe(){
        // add to recipes
        FirestoreUtils.addRecipe(mBinding.getRecipe())

                .addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()) {
                final String newRecipeId = task1.getResult().getId();

                // upload an image in case user added one
                if(mBinding.getImageUri() != null){
                    final String imagePath = String.format("images/%s.jpg", newRecipeId);
                    FirestoreUtils.uploadPhoto(mBinding.getImageUri(), imagePath)
                            .addOnCompleteListener(task -> {
                                StorageReference imageStorageRef =
                                        FirebaseStorage.getInstance().getReference(imagePath);
                                imageStorageRef.getDownloadUrl().addOnCompleteListener(taskUrl -> {
                                    if(taskUrl.isSuccessful()){
                                        FirebaseFirestore.getInstance().collection("recipes").document(newRecipeId)
                                                .update("image", taskUrl.getResult().toString());
                                    }
                                });
                            });
                }

                // keep cached updated
                MainActivity.preferencesConfig.addIdToMyRecipes(newRecipeId);

                // also update the user's document
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
        cleanForm();
    }

    private void cleanForm(){
        mBinding.formLayout.etName.setText("");
        mBinding.formLayout.etIngredients.setText("");
        mBinding.formLayout.etSteps.setText("");

        vmRecipe.select(new Recipe());
        mBinding.setCanAddRecipe(false);
    }

    private void uploadPhoto(){
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

    /**
     * returns if the form is correct
     */
    private boolean checkForm(){
        boolean result = true;
        if(mBinding.formLayout.etName.length() == 0)
            result = false;
        else if(mBinding.formLayout.etIngredients.length() == 0)
            result = false;
        else if(mBinding.formLayout.etSteps.length() == 0)
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