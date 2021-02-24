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
import com.recipesapp.recipesapp.databinding.FragmentMyRecipesBinding;
import com.recipesapp.recipesapp.model.Recipe;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.views.adapters.RecipeAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRecipesFragment extends Fragment {

    private FragmentMyRecipesBinding mBinding;
    private RecyclerView mRecyclerView;

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_my_recipes, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        getActivity().setTitle("");

        mRecyclerView = mBinding.recyclerViewRecipes;
        setupRecyclerView(new ArrayList<>());

        fetchDocs();
    }

    private void fetchDocs() {

        final ArrayList<String> favsIds = new ArrayList<>(MainActivity.preferencesConfig.readMyRecipesIds());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            // Background work here
            ArrayList<Recipe> recipes = new ArrayList<>();
            for (String id : favsIds) {
                try {
                    DocumentSnapshot documentSnapshot = FirestoreUtils.getRecipe(id);

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

    private void setupRecyclerView(ArrayList<Recipe> recipes) {
        RecipeAdapter categoryAdapter = new RecipeAdapter(recipes);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}