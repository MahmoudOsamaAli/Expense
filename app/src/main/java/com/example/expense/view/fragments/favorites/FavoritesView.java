package com.example.expense.view.fragments.favorites;

import com.example.expense.pojo.RestaurantModel;

import java.util.ArrayList;

public interface FavoritesView {
    void onGetFavoriteData(ArrayList<RestaurantModel> list);
}
