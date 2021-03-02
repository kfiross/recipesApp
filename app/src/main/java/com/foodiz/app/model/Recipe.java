package com.foodiz.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class represents a recipe
 */
public class Recipe implements Parcelable {
    private String mId;

    @Nullable
    private Integer mCategory;
    @Nullable
    private String mName;
    @Nullable
    private String mIngredients;
    @Nullable
    private String mSteps;
    @Nullable
    private String mImage;

    public Recipe(){
        
    }

    public Recipe(
            String id,
            @Nullable Integer category,
            @Nullable  String name,
            @Nullable  String ingredients,
            @Nullable  String steps,
            @Nullable  String image
    ) {
        mId = id;
        mCategory = category;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mImage = image;
    }

    protected Recipe(Parcel in) {
        mId = in.readString();
        if (in.readByte() == 0) {
            mCategory = null;
        } else {
            mCategory = in.readInt();
        }
        mName = in.readString();
        mIngredients = in.readString();
        mSteps = in.readString();
        mImage = in.readString();
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
                snapshot.get("ingredients", String.class),
                (String) snapshot.get("steps"),
                (String) snapshot.get("image")
        );
    }


    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getIngredients() {
        return mIngredients;
    }

    public void setIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public String getSteps() {
        return mSteps;
    }

    public void setSteps(String mSteps) {
        this.mSteps = mSteps;
    }

    public String getName() {
        return mName == null ? "" : mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }


    public int getCategory() {
        return mCategory == null ? -1 : mCategory;
    }

    public void setCategory(int mCategory) {
        this.mCategory = mCategory;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public Map<String,Object> toJson() {
        Map<String,Object> map = new HashMap<>();
        map.put("category", mCategory);
        map.put("name", mName);
        map.put("ingredients", mIngredients);
        map.put("steps", mSteps);
        map.put("image", mImage);

        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        if (mCategory == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mCategory);
        }
        dest.writeString(mName);
        dest.writeString(mIngredients);
        dest.writeString(mSteps);
        dest.writeString(mImage);
    }



    public boolean hasIngredientsWithName(String name){
        return mIngredients.contains(name);
    }


    public List<String> getPhotos(){
        return List.of(mImage);
    }

}
