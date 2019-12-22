package com.example.expense.view.activities.selectedCategory;

import android.content.Context;

import com.example.expense.configs.App;
import com.example.expense.data.Data;
import com.example.expense.data.firebase.PlaceFirebaseProcess;
import com.example.expense.data.firebase.callbacks.PlaceFirebaseListener;
import com.example.expense.data.sqllite.DBProcess;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.pojo.RestaurantModel;

import java.util.ArrayList;

class SelectedCategoryPresenter implements PlaceFirebaseListener {

    private SelectedCategoryView viewCallback;
    private final String TAG = "HomeActPresenter";
    private Context context;
    private PlaceFirebaseProcess placeFBProcess;
    private DBProcess dbProcess;

    SelectedCategoryPresenter(SelectedCategoryView viewCallback) {
        this.viewCallback = viewCallback;
    }

    private ArrayList<RestaurantModel> getCategoryList(){
        return Data.getSelectedCategoryData();
    }

    SelectedCategoryPresenter(SelectedCategoryView listener, Context context) {
        this.viewCallback = listener;
        this.context = context;
        this.dbProcess = new DBProcess(this.context);
    }

    public SelectedCategoryPresenter(Context context) {
        this.context = context;
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
    public void onAddPlaceSuccess(boolean status, Throwable t) {

    }

    @Override
    public void onEditPlaceSuccess(boolean status, Throwable t) {

    }

    @Override
    public void onDeletePlace(boolean status) {

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
