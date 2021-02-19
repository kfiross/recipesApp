package com.recipesapp.recipesapp.model;

import com.google.firebase.firestore.DocumentSnapshot;
import com.recipesapp.recipesapp.R;

import java.util.HashMap;

/**
 * Class represents a recipe's category such
 * ex.: 'Meat', 'Fish'
 */
public class Category {
    private String mId;
    private HashMap<String,String> mName;

    public Category(String id,  HashMap<String,String> name){
        this.mId = id;
        this.mName = name;
    }

    /**
     * return the right background color for the category
     */
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


    /**
     * Constructing a category from a snapshot of document
     */
    public static Category fromDocument(DocumentSnapshot snapshot){
        return new Category(
                snapshot.getId(),
                (HashMap<String,String>) snapshot.get("name")
        );
    }

    public HashMap<String, String> getName() {
        return mName;
    }

    public void setName(HashMap<String, String> mName) {
        this.mName = mName;
    }
}
