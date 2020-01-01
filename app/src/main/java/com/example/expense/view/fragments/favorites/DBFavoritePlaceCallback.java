package com.example.expense.view.fragments.favorites;

import com.example.expense.pojo.Model.PlaceModel;

import java.util.ArrayList;

public interface DBFavoritePlaceCallback {

    void readFavorites(ArrayList<PlaceModel> places);

    void removeFavorites(String placeID);
}
