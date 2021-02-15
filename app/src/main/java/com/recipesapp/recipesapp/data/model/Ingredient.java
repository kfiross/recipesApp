package com.recipesapp.recipesapp.data.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.utils.StringUtils;


import java.util.Map;

public class Ingredient implements Parcelable {
    private String mName;
    private double mCount;
    private int mType;

    public Ingredient(String name, double count, int type){
        mName = name;
        mCount = count;
        mType = type;
    }


    protected Ingredient(Parcel in) {
        mName = in.readString();
        mCount = in.readDouble();
        mType = in.readInt();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public static Ingredient fromDocument(DocumentSnapshot snapshot){
        return Ingredient.fromMap(snapshot.getData());

    }

    public static Ingredient fromMap(@Nullable Map<String, Object> map) {
        if(map == null){
            return new Ingredient("", 0, 0);
        }
        return new Ingredient(
                (String) map.get("name"),
                ((Long)map.get("count")).doubleValue(),
                ((Long)map.get("type")).intValue()
        );
    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getQuantity() {
        return mCount;
    }

    public void setQuantity(double mQuantity) {
        this.mCount = mQuantity;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%d %d %s", mCount, mType, mName);
    }

    @NonNull
    public String toString2(Context context) {
        String typeName = StringUtils.getIngredientTypeName(context, mType, mCount);
        return String.format("%s %s", typeName, mName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mCount);
        dest.writeInt(mType);
    }
}
