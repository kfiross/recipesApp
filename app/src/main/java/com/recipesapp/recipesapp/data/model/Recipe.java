package com.recipesapp.recipesapp.data.model;

import android.content.Context;
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
import java.util.Map;

public class Recipe implements Parcelable {
    private String mId;

    @Nullable
    private Integer mCategory;
    @Nullable
    private String mName;
    @Nullable
    private Integer mMakingTime;
    @Nullable
    private List<Ingredient> mIngredients;
    @Nullable
    private List<String> mSteps;
    @Nullable
    private String mImage;

    public Recipe(){
        mIngredients = new ArrayList<>();
        mSteps = new ArrayList<>();
    }

    public Recipe(
            String id,
            @Nullable Integer category,
            @Nullable  String name,
            @Nullable  Integer time,
            @Nullable  List<Ingredient> ingredients,
            @Nullable  List<String> steps,
            @Nullable  String image
    ) {
        mId = id;
        mCategory = category;
        mName = name;
        mMakingTime = time;
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
        if (in.readByte() == 0) {
            mMakingTime = null;
        } else {
            mMakingTime = in.readInt();
        }
        mIngredients = in.createTypedArrayList(Ingredient.CREATOR);
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

    public static Recipe fromDocument(DocumentSnapshot snapshot) {
        List<Map<String, Object>> ingredientsListMap = (List<Map<String, Object>>) snapshot.get("ingredients");
        List<Ingredient> ingredientsList = new ArrayList<>();
        if(ingredientsListMap != null){
            for(Object map : ingredientsListMap){
                if (map instanceof Map) {
                    ingredientsList.add(Ingredient.fromMap((Map<String,Object>)map));
                }
            }
        }
        return new Recipe(
                snapshot.getId(),
                snapshot.get("category", Integer.class),
                snapshot.get("name", String.class),
                snapshot.get("time", Integer.class),
                ingredientsList,
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

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> mIngredients) {
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

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
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
        if (mMakingTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mMakingTime);
        }
        dest.writeTypedList(mIngredients);
        dest.writeStringList(mSteps);
        dest.writeString(mImage);
    }

    public List<String> getIngredientsStrings(Context context){
        List<String> items = new ArrayList<>();
        if(mIngredients != null) {
            for (Ingredient ingredient : mIngredients) {
                items.add(ingredient.toString2(context));
            }
        }
        return items;
    }
}
