package com.recipesapp.recipesapp.data.model;

import com.google.firebase.firestore.DocumentSnapshot;
import com.recipesapp.recipesapp.R;

import java.util.HashMap;

public class Category {
    private String mId;
    private String mEn;

    public Category(String id, String en){
        this.mId = id;
        this.mEn = en;
    }

    public static int getBgById(String id) {
        switch (id){
            case "0":
                return R.drawable.bg_category_item_yellow;

            case "1":
                return R.drawable.bg_category_item_orange;

            case "2":
                return R.drawable.bg_category_item_red;

            case "3":
                return R.drawable.bg_category_item_blue;

            case "4":
                return R.drawable.bg_category_item_green;
        }
        return -1;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getEn() {
        return mEn;
    }

    public void setEn(String mEn) {
        this.mEn = mEn;
    }

    public static Category fromDocument(DocumentSnapshot snapshot){
        return new Category(
                snapshot.getId(),
                (String) snapshot.get("en")
        );
    }
}
