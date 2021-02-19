package com.recipesapp.recipesapp.viewmodels.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.recipesapp.recipesapp.model.Recipe;

public class RecipeSharedViewModel extends ViewModel {
    private final MutableLiveData<Recipe> selected = new MutableLiveData<>();

    public void select(Recipe item) {
        selected.setValue(item);
    }


    public LiveData<Recipe> getSelected() {
        return selected;
    }
}
