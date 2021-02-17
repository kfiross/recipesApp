package com.recipesapp.recipesapp.data.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.recipesapp.recipesapp.utils.StringUtils;

import java.util.Map;

public class Ingredient implements Parcelable {
    private String mName;

    private Number mCount;

    @Nullable
    private Integer mType;

    public Ingredient(String name, Number count, @Nullable Integer type){
        mName = name;
        mCount = count;
        mType = type;
    }


    protected Ingredient(Parcel in) {
        mName = in.readString();
        if (in.readByte() == 0) {
            mType = null;
        } else {
            mType = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        if (mType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mType);
        }
    }

    @Override
    public int describeContents() {
        return 0;
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
                map.get("count") == null ? 0 : ((Number)(map.get("count"))),
                map.get("type") == null ? null : ((Long)map.get("type")).intValue()
        );
    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Number getCount() {
        return mCount;
    }

    public void setCount(Number mCount) {
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
            return mCount.intValue() == 0 ? mName : String.format("%d %s", mCount, mName);
        }
        String typeName = StringUtils.getIngredientTypeName(context, mType, mCount.doubleValue());
        return String.format("%s %s", typeName, mName);
    }

}
