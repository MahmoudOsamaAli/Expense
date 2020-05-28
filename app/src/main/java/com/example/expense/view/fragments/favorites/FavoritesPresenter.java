package com.example.expense.view.fragments.favorites;


import com.example.expense.configs.App;
import com.example.expense.data.sqllite.DBProcess;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.activities.Home.HomeActivity;
import com.example.expense.view.activities.selectedCategory.DBPlaceDetailsCallback;

import java.util.ArrayList;

public class FavoritesPresenter implements DBFavoritePlaceCallback {

    private FavoritesView view;
    private DBProcess dbProcess;
    private HomeActivity mCurrent;

    FavoritesPresenter(FavoritesView view, HomeActivity context) {
        this.view = view;
        this.mCurrent = context;
        this.dbProcess = new DBProcess(mCurrent);
    }

//    private ArrayList<RestaurantModel> getFavoriteFromDatabase(){

    //    }

    void getFavorites() {
        try {
            if (App.mHandler != null) {
                App.mHandler.post(() -> {
                    try {
                        dbProcess.readFavorites(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFavoritePlace(PlaceModel place) {
        try {
            if (App.mHandler != null) {
                App.mHandler.post(() -> {
                    try {
                        dbProcess.removeFavorite(place, (DBPlaceDetailsCallback) this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFavorites(ArrayList<PlaceModel> places) {
        try {
            if (view != null) {
                view.onReadFavorites(places);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFavorites(String placeID) {

    }
}
