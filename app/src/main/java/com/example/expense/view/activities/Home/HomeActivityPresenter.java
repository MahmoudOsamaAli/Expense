package com.example.expense.view.activities.Home;

import android.content.Context;
import android.util.Log;

import com.example.expense.Utilities.AppUtils;
import com.example.expense.Utilities.PrefManager;
import com.example.expense.configs.App;
import com.example.expense.data.firebase.PlaceFirebaseProcess;
import com.example.expense.data.firebase.callbacks.PlaceFirebaseListener;
import com.example.expense.data.sqllite.DBProcess;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.fragments.favorites.DBFavoritePlaceCallback;

import java.util.ArrayList;

public class HomeActivityPresenter implements PlaceFirebaseListener , DBHomeCallback {

    private final String TAG = "HomeActPresenter";

    private HomeActivityListener listener;
    private Context context;
    private PlaceFirebaseProcess placeFBProcess;
    private DBProcess dbProcess;
    private PrefManager mPrefManager;

    HomeActivityPresenter(HomeActivityListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        this.dbProcess = new DBProcess(this.context);
        this.mPrefManager = new PrefManager(context);
    }

    public HomeActivityPresenter(Context context) {
        this.context = context;
    }

    void readPlaceByCategoryFromFireStore() {
        placeFBProcess = new PlaceFirebaseProcess(context, this);

        if(AppUtils.inNetwork(context)) {
            placeFBProcess.readPlacesByCategory(PlaceFirebaseProcess.categories_collection, PlaceFirebaseProcess.restaurant_document, PlaceFirebaseProcess.places_collection);
        }else{
            listener.displayConnectionError();
        }
    }

    void readFavoritePlacesFromFireStore() {
        String UID = mPrefManager.readString(PrefManager.USER_ID);

        if(AppUtils.inNetwork(context)) {
            placeFBProcess.readFavoritePlaces(PlaceFirebaseProcess.favorite_places, PlaceFirebaseProcess.favorite_users, UID);
        }else{
            listener.displayConnectionError();
        }
    }

    @Override
    public void onReadPlaceByCategory(ArrayList<PlaceModel> data) {
        try {
            //save places into SQLite
            savePlacesIntoSQLite(data);

            // return data to activity
            if (listener != null) {
                listener.readPlacesByCategoryFromFirebase(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPlaceSuccess(boolean status, Throwable t) {

    }

    @Override
    public void onEditPlaceSuccess(boolean status, Throwable t) {

    }

    @Override
    public void onDeletePlace(boolean status) {

    }

    @Override
    public void onReadFavoritePlaces(ArrayList<PlaceModel> places) {
            try{
                if (places != null && !places.isEmpty()){
                    Log.i(TAG,"onReadFavoritePlaces(): places != null");
                    Log.i(TAG,"onReadFavoritePlaces(): places size = " + places.size());
                    if (dbProcess != null){
                        Log.i(TAG,"onReadFavoritePlaces(): dbProcess != null");
                        for (PlaceModel place : places) {
                            dbProcess.saveFavorite(place, this);
                        }
                    }
                }else{
                    Log.i(TAG,"onReadFavoritePlaces(): places = null");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    @Override
    public void onSaveFavoritePlace(boolean status, Throwable t) {
            if (status){
                Log.i(TAG,"onSaveFavoritePlace(): saving favorites from Firestore = true");
            }else{
                Log.i(TAG,"onSaveFavoritePlace(): saving favorites from Firestore = false");

                if (t != null){
                    t.printStackTrace();
                }
            }
    }

    @Override
    public void onRemoveFavoritePlaceFromFirebase(boolean status, Throwable t) {

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

    @Override
    public void onSaveFavorites(boolean status, Throwable t) {

    }
}
