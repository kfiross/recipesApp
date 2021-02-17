package com.recipesapp.recipesapp.views.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

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
        mBinding.btnAddPhoto.setOnClickListener(v -> addPhoto());
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
                mBinding.setCanAddRecipe(checkForm());
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
                mBinding.setCanAddRecipe(checkForm());
            }
        });


        vmRecipe.getSelected().observe(getActivity(), recipe -> {
            mBinding.setRecipe(recipe);
            mBinding.setCanAddRecipe(checkForm());
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
        FirestoreUtils.addRecipe(mBinding.getRecipe())

                .addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()) {
                final String newRecipeId = task1.getResult().getId();

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
        if(mBinding.etName.length() == 0)
            result = false;
        else if(mBinding.etTime.length() == 0)
            result = false;
        else if(mBinding.getRecipe().getIngredients().isEmpty())
            result = false;
        else if(mBinding.getRecipe().getSteps().isEmpty())
            result = false;

        Log.d("checkForm", String.valueOf(result));

        return result;
    }
}