package com.example.expense.view.activities.selectedCategory;

import android.content.Context;

import com.example.expense.configs.App;
import com.example.expense.data.firebase.PlaceFirebaseProcess;
import com.example.expense.data.firebase.callbacks.PlaceFirebaseListener;
import com.example.expense.data.sqllite.DBProcess;
import com.example.expense.pojo.Model.PlaceModel;

import java.util.ArrayList;

class SelectedCategoryPresenter implements PlaceFirebaseListener {

    private SelectedCategoryView viewCallback;
    private final String TAG = "HomeActPresenter";
    private Context context;
    private PlaceFirebaseProcess placeFBProcess;
    private DBProcess dbProcess;

    SelectedCategoryPresenter(SelectedCategoryView listener, Context context) {
        this.viewCallback = listener;
        this.context = context;
        this.dbProcess = new DBProcess(this.context);
    }

    void readPlaceByCategoryFromFireStore(String category) {
        placeFBProcess = new PlaceFirebaseProcess(context, this);
        placeFBProcess.readPlacesByCategory(PlaceFirebaseProcess.categories_collection, category, PlaceFirebaseProcess.places_collection);
    }

    @Override
    public void onReadPlaceByCategory(ArrayList<PlaceModel> data) {
        try {
            //save places into SQLite
            savePlacesIntoSQLite(data);

            // return data to activity
            if (viewCallback != null) {
                viewCallback.readPlacesByCategoryFromFirebase(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPlaceSuccess(boolean status, Throwable t) {
            //Not Needed Here
    }

    @Override
    public void onEditPlaceSuccess(boolean status, Throwable t) {
        //Not Needed Here
    }

    @Override
    public void onDeletePlace(boolean status) {
        //Not Needed Here
    }

    @Override
    public void onReadFavoritePlaces(ArrayList<PlaceModel> places) {
        //Not Needed Here
    }

    @Override
    public void onSaveFavoritePlace(boolean status, Throwable t) {
        //Not Needed Here
    }

    @Override
    public void onRemoveFavoritePlaceFromFirebase(boolean status, Throwable t) {
        //Not Needed Here
    }

    @Override
    public void onSaveFavoritePlaceIntoFirebase(boolean status, Throwable t) {

    }

    private void savePlacesIntoSQLite(ArrayList<PlaceModel> data) {
        try {
            if (!data.isEmpty()) {
                if (App.mHandler != null) {
                    App.mHandler.post(() -> {
                        try {
                            dbProcess.insertPlace(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
