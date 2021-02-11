package com.recipesapp.recipesapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe implements Parcelable {
    private String mId;

    @Nullable
    private Integer mCategory;
    @Nullable
    private String mName;
    @Nullable
    private Integer mMakingTime;
    @Nullable
    private List<String> mIngredients;
    @Nullable
    private List<String> mSteps;
    @Nullable
    private String mImage;

    protected Recipe(Parcel in) {
        mId = in.readString();
        mCategory = in.readInt();
        mName = in.readString();
        mMakingTime = in.readInt();
        mIngredients = in.createStringArrayList();
        mSteps = in.createStringArrayList();
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

    public Recipe(){
        mIngredients = new ArrayList<>();
        mSteps = new ArrayList<>();
    }

    public Recipe(String id, @Nullable Integer category,@Nullable  String name,@Nullable  Integer time,@Nullable  List<String> ingredients,@Nullable  List<String> steps,@Nullable  String image) {
        mId = id;
        mCategory = category;
        mName = name;
        mMakingTime = time;
        mIngredients = ingredients;
        mSteps = steps;
        mImage = image;
    }

    public static Recipe fromDocument(DocumentSnapshot snapshot) {
        return new Recipe(
                snapshot.getId(),
                snapshot.get("category", Integer.class),
                snapshot.get("name", String.class),
                snapshot.get("time", Integer.class),
                (List<String>) snapshot.get("ingredients"),
                (List<String>) snapshot.get("steps"),
                (String) snapshot.get("image")
        );
    }


    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public List<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<String> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<String> getSteps() {
        return mSteps;
    }

    public void setSteps(List<String> mSteps) {
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

    public int getMakingTime() {
        return mMakingTime == null ? 0 : mMakingTime;
    }

    public void setMakingTime(int mMakingTime) {
        this.mMakingTime = mMakingTime;
    }

    public String getMakingTimeString(){
        return this.mMakingTime + " Minutes";
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeInt(mCategory);
        dest.writeString(mName);
        dest.writeInt(mMakingTime);
        dest.writeStringList(mIngredients);
        dest.writeStringList(mSteps);
        dest.writeString(mImage);
    }

    public HashMap<String,Object> toJson() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("category", mCategory);
        map.put("name", mName);
        map.put("time", mMakingTime);
        map.put("ingredients", mIngredients);
        map.put("steps", mSteps);
        map.put("image", mImage);

        return map;
    }
}
