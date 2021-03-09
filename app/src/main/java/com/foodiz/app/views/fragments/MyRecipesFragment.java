package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.model.Recipe;
import com.foodiz.app.databinding.FragmentMyRecipesBinding;
import com.foodiz.app.utils.FirestoreUtils;
import com.foodiz.app.views.adapters.RecipeAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRecipesFragment extends BaseFragment {

    //------------DATA MEMBERS----------------------
    private RecyclerView recyclerView;
    private FragmentMyRecipesBinding recipesBinding;
    //---------------------------------------------



    public MyRecipesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recipesBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_my_recipes, container, false);

        return recipesBinding.getRoot();
    }


    private void getDocuments() {

        final ArrayList<String> idOfMyFevs = new ArrayList<>(MainActivity.preferencesConfig.readMyRecipesIds());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler myHandle = new Handler(Looper.getMainLooper());

        executor.execute(() ->
        {


            ArrayList<Recipe> recipes = new ArrayList<>();
            for (String singleId : idOfMyFevs) {
                try {
                    DocumentSnapshot documentSnapshot = FirestoreUtils.recipeFromDb(singleId);

                    Recipe newRecipe = Recipe.fromDocument(documentSnapshot);
                    recipes.add(newRecipe);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }


            myHandle.post(() -> {

                initialRecyclerView(recipes);
            });
        });
    }



    private void initialRecyclerView(ArrayList<Recipe> recipes) {
        RecipeAdapter categoryAdapter = new RecipeAdapter(recipes);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = recipesBinding.recyclerViewRecipes;
        initialRecyclerView(new ArrayList<>());

        getDocuments();
    }



}