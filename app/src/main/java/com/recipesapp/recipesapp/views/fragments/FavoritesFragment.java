package com.recipesapp.recipesapp.views.fragments;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.databinding.FragmentRecipesBinding;
import com.recipesapp.recipesapp.model.Recipe;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.utils.StringUtils;
import com.recipesapp.recipesapp.views.adapters.RecipeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends BaseFragment {

    private FragmentRecipesBinding mBinding;
    private RecyclerView mRecyclerView;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipes, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = mBinding.recyclerViewRecipes;
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        mBinding.setName(StringUtils.getLocaleString(R.string.favourites, getContext()));

        setupRecyclerView(new ArrayList<>());

        fetchDocs();
    }

    private void fetchDocs() {

        final ArrayList<String> favsIds = new ArrayList<>(MainActivity.preferencesConfig.readFavsIds());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            //Background work here
                ArrayList<Recipe> recipes = new ArrayList<>();
                for (String id : favsIds) {
                    try {
                        DocumentSnapshot documentSnapshot = FirestoreUtils.getRecipe(id);
//                        // documentSnapshot.toObject(Recipe.class);
//

                        Recipe newRecipe = Recipe.fromDocument(documentSnapshot);
                        recipes.add(newRecipe);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            handler.post(() -> {
                //UI Thread work here
                setupRecyclerView(recipes);
            });
        });
    }

    private void setupRecyclerView(ArrayList<Recipe> recipes){
        RecipeAdapter categoryAdapter = new RecipeAdapter(recipes);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}