package com.foodiz.app.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Recipe implements Parcelable {

    //-------DATA MEMBERS----------------------
    private String id;
    private Integer group;
    private String name;
    private List<String> ingredients;
    private List<String> steps;
    private String photo;
    //-----------------------------------------
    public Recipe(){ }

    public Recipe(String id, Integer group, String name, List<String> ingredients, List<String> steps, String photo
    ) {
        this.id = id;
        this.group = group;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.photo = photo;
    }

    protected Recipe(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            this.group = null;
        } else {
            this.group = in.readInt();
        }
        this.name = in.readString();
        this.ingredients = in.createStringArrayList();
        this.steps = in.createStringArrayList();
        this.photo = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public static Recipe fromDocument(DocumentSnapshot snapshot) {
        return new Recipe(
                snapshot.getId(),
                snapshot.get("category", Integer.class),
                snapshot.get("name", String.class),
                (List<String>) snapshot.get("ingredients"),
                (List<String>) snapshot.get("steps"),
                (String) snapshot.get("image")
        );
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return this.steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getName() {
        if(this.name != null )
            return this.name;
        return "";



    }

    public void setName(String mName) {
        this.name = mName;
    }


    public int getGroup() {
        if( this.group != null)
            return this.group;
        return -1;

    }

    public void setCategory(int group) {
        this.group = group;
    }

    public String getImage() {
        return this.photo;
    }

    public void setImage(String photo) {
        this.photo = photo;
    }

    public Map<String,Object> toJson() {
        Map<String,Object> map = new HashMap<>();
        map.put("category", group);
        map.put("name", name);
        map.put("ingredients", ingredients);
        map.put("steps", steps);
        map.put("image", photo);

        return map;
    }


    public boolean hasIngredientsWithName(String name){
        return ingredients.contains(name);
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    public List<String> getPhotos(){
        return List.of(photo);
    }

    public boolean containsIngredient(String ingredientName) {
        if(ingredients==null || ingredients.isEmpty()){
            return false;
        }

        return this.ingredients.contains(ingredientName);
    }

    public void addIngredient(String ingredient) {
        if(ingredient == null){
            ingredients = new ArrayList<>();
        }

        ingredients.add(ingredient);
    }

    public void addStep(String step) {
        if(steps == null){
            steps = new ArrayList<>();
        }
        steps.add(step);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        if (group == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(group);
        }
        dest.writeString(name);
        dest.writeStringList(ingredients);
        dest.writeStringList(steps);
        dest.writeString(photo);
    }

    public String getIngredientsString(){
        StringBuilder builder = new StringBuilder();
        for(String ingredients: ingredients){
            builder.append(" - ").append(ingredients).append("\n");
        }

        return builder.toString();
    }

    public String getStepsString(){
        StringBuilder builder = new StringBuilder();
        int count = 1;
        for(String step: steps){
            builder.append(count).append(") ").append(step).append("\n");
            count++;
        }

        return builder.toString();
    }
}
