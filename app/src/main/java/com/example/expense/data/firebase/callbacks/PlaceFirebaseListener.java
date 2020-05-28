package com.example.expense.data.firebase.callbacks;

import com.example.expense.pojo.Model.PlaceModel;

import java.util.ArrayList;

public interface PlaceFirebaseListener {
    void onReadPlaceByCategory(ArrayList<PlaceModel> data);

    void onRequestPlaceSuccess(boolean status, Throwable t);

    void onEditPlaceSuccess(boolean status, Throwable t);

    void onDeletePlace(boolean status);

    void onReadFavoritePlaces(ArrayList<PlaceModel> places);

    void onSaveFavoritePlace(boolean status, Throwable t);

    void onRemoveFavoritePlaceFromFirebase(boolean status, Throwable t);

    void onSaveFavoritePlaceIntoFirebase(boolean status, Throwable t);

}
