package com.foodiz.app.views.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.databinding.FragmentAddingRecipeBinding;
import com.foodiz.app.model.Recipe;
import com.foodiz.app.utils.FirestoreUtils;
import com.foodiz.app.utils.UiUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddingRecipeFragment extends Fragment {

    private FragmentAddingRecipeBinding mBinding;
    private Recipe mRecipe;
    public AddingRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
             mRecipe = args.getParcelable("recipe");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_adding_recipe, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize binding variables

        mBinding.setRecipe(mRecipe==null ? new Recipe() : mRecipe);
        mBinding.setEditMode(mRecipe != null);

        mBinding.formLayout.setStepsCount(mRecipe==null ? 5 : mRecipe.getSteps().size());
        mBinding.formLayout.setIngredientsCount(mRecipe==null ? 5 : mRecipe.getIngredients().size());
        
        setUpViews();

        // fill in the ingredients & steps of the recipe we about to edit
        if(mRecipe != null){
            for(int i=0; i< mBinding.formLayout.getIngredientsCount(); i++) {
                EditText etIngredient = (EditText) mBinding.formLayout.layoutIngredients.getChildAt(i);
                etIngredient.setText(mBinding.getRecipe().getIngredients().get(i));
            }

            for(int i=0; i<mBinding.formLayout.getStepsCount(); i++) {
                EditText etStep = (EditText) mBinding.formLayout.layoutSteps.getChildAt(i);
                etStep.setText(mBinding.getRecipe().getSteps().get(i));
            }

            // also the category
            mBinding.formLayout.spinnerCategory.setSelection(mBinding.getRecipe().getCategory());
        }
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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.my_spinner_item_layout,
                getResources().getStringArray(R.array.categories)
        );

        adapter.setDropDownViewResource(R.layout.my_spinner_item_dropdown_layout);

        mBinding.formLayout.spinnerCategory.setAdapter(adapter);
    }


    private void setupButtons() {
        mBinding.btnAddRecipe.setOnClickListener(v -> addRecipe());

        mBinding.formLayout.btnAddPhoto.setOnClickListener(v -> uploadPhoto());

        mBinding.formLayout.btnAddIngredient.setOnClickListener(v -> addIngredient());
        mBinding.formLayout.btnRemoveIngredient.setOnClickListener(v -> removedIngredient());
        mBinding.formLayout.btnAddStep.setOnClickListener(v -> addStep());
        mBinding.formLayout.btnRemoveStep.setOnClickListener(v -> removeStep());

//        mBinding.formLayout.imgRecipe.setOnClickListener(v -> uploadPhoto());
    }

    private void addIngredient(){
        addEditTextToLayout(mBinding.formLayout.layoutIngredients);
        mBinding.formLayout.setIngredientsCount(mBinding.formLayout.layoutIngredients.getChildCount());
    }

    private void removedIngredient(){
        if(mBinding.formLayout.getIngredientsCount() >= 3) {
            removeLastChildFromLayout(mBinding.formLayout.layoutIngredients);
            mBinding.formLayout.setIngredientsCount(mBinding.formLayout.layoutIngredients.getChildCount());
        }
    }

    private void addStep(){
        addEditTextToLayout(mBinding.formLayout.layoutSteps);
        mBinding.formLayout.setStepsCount(mBinding.formLayout.layoutSteps.getChildCount());
    }

    private void removeStep(){
        if(mBinding.formLayout.getStepsCount() >= 3) {
            removeLastChildFromLayout(mBinding.formLayout.layoutSteps);
            mBinding.formLayout.setStepsCount(mBinding.formLayout.layoutSteps.getChildCount());
        }
    }


    private void setupForm() {
        // add places to enter ingredients
        for(int i=0; i< mBinding.formLayout.getIngredientsCount(); i++) {
            addEditTextToLayout(mBinding.formLayout.layoutIngredients);
        }

        // add places to enter steps
        for(int i=0; i<mBinding.formLayout.getStepsCount(); i++) {
            addEditTextToLayout(mBinding.formLayout.layoutSteps);
        }

    }

    private void addRecipe(){
        if(!checkForm()){
            UiUtils.showSnackbar(
                    this.getView(),
                    "Please enter all data!",
                    2500
            );
            return;
        }


        Recipe recipe = mBinding.getRecipe();
        if(recipe == null){
            recipe = new Recipe();
        }

        // update name
        recipe.setName(mBinding.formLayout.etName.getText().toString());

        // update ingredients
        for(int i=0; i<mBinding.formLayout.layoutIngredients.getChildCount();i++){
            EditText etIngredient = (EditText) mBinding.formLayout.layoutIngredients.getChildAt(i);
            String ingredient = etIngredient.getText().toString();
            if(!ingredient.isEmpty()){
                recipe.addIngredient(ingredient);
            }

        }

        // updating steps
        for(int i=0; i<mBinding.formLayout.layoutSteps.getChildCount();i++){
            EditText etStep = (EditText) mBinding.formLayout.layoutSteps.getChildAt(i);
            String step = etStep.getText().toString();
            if(!step.isEmpty()){
                recipe.addStep(step);
            }
        }

        // add to recipes
        FirestoreUtils.addRecipe(recipe)

                .addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()) {
                final String newRecipeId = task1.getResult().getId();

                // upload an image in case user added one
                if(mBinding.getImageUri() != null){
                    final String imagePath = String.format("photos/recipes/%s.jpg", newRecipeId);
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
                        UiUtils.showSnackbar(
                                this.getView(),
                                getString(R.string.reciped_added),
                                2500
                        );
                    }
                    else{
                        UiUtils.showSnackbar(
                                this.getView(),
                                getString(R.string.recipe_insert_failed),
                                2500
                        );
                    }
                });
            }
        });
        cleanForm();
    }

    private void cleanForm(){
        mBinding.formLayout.etName.setText("");

        for(int i=0; i<mBinding.formLayout.layoutIngredients.getChildCount(); i++){
            ((EditText)(mBinding.formLayout.layoutIngredients.getChildAt(i))).setText("");
        }

        for(int i=0; i<mBinding.formLayout.layoutSteps.getChildCount(); i++){
            ((EditText)(mBinding.formLayout.layoutSteps.getChildAt(i))).setText("");
        }
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1000);
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
        else if(!isIngredientsValid())
            result = false;
        else if(!isStepsValid())
            result = false;

        return result;
    }

    private boolean isIngredientsValid(){
        for(int i=0; i<mBinding.formLayout.layoutIngredients.getChildCount(); i++){
            EditText etStep = (EditText) mBinding.formLayout.layoutIngredients.getChildAt(i);
            if(etStep.length() == 0){
                return false;
            }
        }
        return true;
    }

    private boolean isStepsValid(){
        for(int i=0; i<mBinding.formLayout.layoutSteps.getChildCount(); i++){
            EditText etStep = (EditText) mBinding.formLayout.layoutSteps.getChildAt(i);
            if(etStep.length() == 0){
                return false;
            }
        }
        return true;
    }


    private void addEditTextToLayout(LinearLayout layout){
        EditText child = new EditText(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,12,0,12);
        child.setMinHeight(120);
        child.setLayoutParams(params);
        child.setPadding(28,0,28,0);
        child.setBackgroundResource(R.drawable.bg_edit_text);

        child.setBackgroundTintList(
                ContextCompat.getColorStateList(getContext(), R.color.bgEditColor)
        );

        child.setMaxLines(3);

        layout.addView(child);
    }

    private void removeLastChildFromLayout(LinearLayout layout){
        layout.removeViewAt(layout.getChildCount()-1);
    }
}