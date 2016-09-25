package com.example.daidaijie.syllabusapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by daidaijie on 2016/9/25.
 */

public class TakeOutSubMenu {

    @SerializedName("sub_menu")
    private String mName;

    @SerializedName("sub_list")
    private List<Dishes> mDishes;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<Dishes> getDishes() {
        return mDishes;
    }

    public void setDishes(List<Dishes> dishes) {
        mDishes = dishes;
    }
}
