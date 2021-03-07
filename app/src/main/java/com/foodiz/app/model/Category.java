package com.foodiz.app.model;

/**
 * Class represents a category of recipe
 * ex.: 'Dinner', 'Breakfast'
 */
public class Category {
    private String mId;
    private String mName;

    public Category(String id, String name){
        this.mId = id;
        this.mName = name;
    }


    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
