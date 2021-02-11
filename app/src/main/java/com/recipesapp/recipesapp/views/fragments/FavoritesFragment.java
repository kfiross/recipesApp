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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Recipe;
import com.recipesapp.recipesapp.databinding.FragmentRecipesBinding;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.views.adapters.RecipeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends BaseFragment {

    private FragmentRecipesBinding mBinding;
    private RecyclerView mRecyclerView;
    private List<String> mFavsIds;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFavsIds = new ArrayList<>();
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

        mBinding.setName("My Favourites");

        setupRecyclerView(new ArrayList<>());

        fetchDocs();

//        String uid = FirebaseAuth.getInstance().getUid();
//        Task<DocumentSnapshot> task = FirebaseFirestore.getInstance().document("user/" + uid).get();
//        task.addOnCompleteListener(it -> {
//            if (it.isSuccessful()) {
//
//                List<String> ids = (List<String>) it.getResult().get("favourites");
//                if (ids != null) {
//                    mFavsIds.clear();
//                    mFavsIds.addAll(ids);
//                }
//                fetchDocs();
//            } else {
//                //error
//            }
//        });
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

//        FirebaseFirestore.getInstance().collection("recipes").addSnapshotListener(
//                (documentSnapshots, error) -> {
//
//                    List<DocumentSnapshot> docs = documentSnapshots.getDocuments();
//                    ArrayList<Recipe> recipes = new ArrayList<>();
//                    for (int i = 0; i < docs.size(); i++) {
//                        DocumentSnapshot documentSnapshot = docs.get(i);
//                        // documentSnapshot.toObject(Recipe.class);
//
//                        Recipe newRecipe = Recipe.fromDocument(documentSnapshot);
//
//                        if(isIncluded(newRecipe)) {
//                            recipes.add(newRecipe);
//                        }
//                    }
//                    setupRecyclerView(recipes);
//
//                });
//    }

    private void setupRecyclerView(ArrayList<Recipe> recipes){
        RecipeAdapter categoryAdapter = new RecipeAdapter(recipes);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}