package com.foodiz.app.helper;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.foodiz.app.BR;

import java.util.ArrayList;
import java.util.List;

public class RecipeFormData extends BaseObservable {
    private String name;
    private String time;
    private List<String> ingredientList;
    private List<String> stepsList;
    private boolean correct;


    public RecipeFormData() {
        this.name = "";
        this.time = "";
        this.ingredientList = new ArrayList<>();
        this.stepsList = new ArrayList<>();
        this.correct = false;
    }


    private boolean isEnabled() {
        return getName().length() !=0  && getTime().length() != 0 && !getIngredientList().isEmpty() && !getStepsList().isEmpty();
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public boolean getCorrect() {
        return correct;
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setName(String name){
        this.name = name;
        this.correct = isEnabled();
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.correct);
    }

    public void setTime(String time) {
        this.time = time;
        this.correct = isEnabled();
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.password);
        notifyPropertyChanged(BR.correct);
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
        notifyPropertyChanged(BR.correct);
    }


    public List<String> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<String> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<String> stepsList) {
        this.stepsList = stepsList;
    }
}
