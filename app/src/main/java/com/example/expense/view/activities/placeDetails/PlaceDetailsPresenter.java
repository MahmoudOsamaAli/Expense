package com.example.expense.view.activities.placeDetails;


import com.example.expense.configs.App;
import com.example.expense.data.sqllite.DBProcess;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.activities.selectedCategory.DBPlaceDetailsCallback;

import java.util.ArrayList;

class PlaceDetailsPresenter implements DBPlaceDetailsCallback {

    private PlaceDetailsView view;
    DBProcess dbProcess;
    private PlaceDetails mCurrent;

    PlaceDetailsPresenter(PlaceDetailsView view, PlaceDetails context) {
        this.view = view;
        this.mCurrent = context;
        this.dbProcess = new DBProcess(mCurrent);
    }
//    private ArrayList<locationModel> getLocations(){

//    }
//    private ArrayList<PlaceImage> getPlaceImages(){

    //    }
    private ArrayList<Integer> getSeekBarValues() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(85);
        list.add(6);
        list.add(9);
        return list;
    }

    private double getTotalRating() {
        return 8.8;
    }

    //    void getLocationsList(){
//        view.onGetLocations(getLocations());
//    }
//    void getPlaceImagesList(){
//        view.onGetPlaceImages(getPlaceImages());
//    }
//    void getRatings(){
//        view.onGetSeekBarRating(getSeekBarValues());
//    }
    void getChartRating() {
        view.onGetChartRating(getTotalRating());
    }

    public void saveFavoritePlace(PlaceModel place) {
        try {
            if (App.mHandler != null) {
                App.mHandler.post(() -> {
                    try {
                        dbProcess.saveFavorite(place, this);
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
                        dbProcess.removeFavorite(place, this);
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
    public void onSaveFavoritePlace(boolean status, Throwable t) {
        if (status) {
            view.onSaveFavoritePlace(true);
        } else {
            t.printStackTrace();
        }
    }

    @Override
    public void onRemoveFavoritePlace(boolean status, Throwable t) {
        if (status) {
            view.onRemoveFavoritePlace(true);
        } else {
            t.printStackTrace();
        }
    }
}
