package com.example.expense.view.fragments.favorites;

import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.pojo.RestaurantModel;

import java.util.ArrayList;

public interface FavoritesView {
    void onReadFavorites(ArrayList<PlaceModel> places);

    void onRemoveFavorite(String placeID);
}
