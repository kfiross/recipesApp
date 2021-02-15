package com.recipesapp.recipesapp.data.model;

import android.annotation.SuppressLint;
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
    @Nullable
    private Integer mType;

    public Ingredient(String name, double count, @Nullable Integer type){
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
                map.get("count") == null ? 0 : ((Long)(map.get("count"))).doubleValue(),
                map.get("type") == null ? null : ((Long)map.get("type")).intValue()
        );
    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getCount() {
        return mCount;
    }

    public void setCount(double mCount) {
        this.mCount = mCount;
    }

    public @Nullable Integer getType() {
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

    @SuppressLint("DefaultLocale")
    @NonNull
    public String toString2(Context context) {
        if(mType == null) {
            return mCount == 0 ? mName : String.format("%d %s", (int)mCount, mName);
        }
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
