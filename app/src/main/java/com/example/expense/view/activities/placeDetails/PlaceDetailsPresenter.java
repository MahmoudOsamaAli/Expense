package com.example.expense.view.activities.placeDetails;


import android.util.Log;

import com.example.expense.Utilities.AppUtils;
import com.example.expense.Utilities.PrefManager;
import com.example.expense.configs.App;
import com.example.expense.data.firebase.PlaceFirebaseProcess;
import com.example.expense.data.firebase.callbacks.PlaceFirebaseListener;
import com.example.expense.data.sqllite.DBProcess;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.activities.selectedCategory.DBPlaceDetailsCallback;

import java.util.ArrayList;

class PlaceDetailsPresenter implements DBPlaceDetailsCallback, PlaceFirebaseListener {

    private static final String TAG = "PlaceDetailsPresenter";
    private PlaceDetailsView view;
    private DBProcess dbProcess;
    private PlaceDetails mCurrent;
    private PlaceFirebaseProcess firebaseProcess;
    private PrefManager mPrefManager;

    PlaceDetailsPresenter(PlaceDetailsView view, PlaceDetails context) {
        this.view = view;
        this.mCurrent = context;
        this.dbProcess = new DBProcess(mCurrent);
        this.firebaseProcess = new PlaceFirebaseProcess(mCurrent, this);
        this.mPrefManager = new PrefManager(mCurrent);

    }

    private double getTotalRating() {
        return 8.8;
    }

    void getChartRating() {
        view.onGetChartRating(getTotalRating());
    }

    /**
     * Saving Favorite place Into SQLite DB
     */
    public void saveFavoritePlace(PlaceModel place) {
        try {
            if (App.mHandler != null) {
                App.mHandler.post(() -> {
                    try {
                        dbProcess.saveFavorite(place, this);

                        if (mCurrent != null && AppUtils.inNetwork(mCurrent)) {
                            Log.i(TAG, "saveFavoritePlace(): Network is connected");

                            if (firebaseProcess != null) {
                                if (mPrefManager != null) {
                                    String uid = mPrefManager.readString(PrefManager.USER_ID);
                                    firebaseProcess.saveFavoritePlace(PlaceFirebaseProcess.favorite_users, PlaceFirebaseProcess.favorite_places, uid, place);
                                }
                            }
                        } else {
                            Log.e(TAG, "saveFavoritePlace(): No Network");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removing Favorite place From SQLite DB
     */
    public void removeFavoritePlace(PlaceModel place) {
        try {
            if (App.mHandler != null) {
                App.mHandler.post(() -> {
                    try {
                        dbProcess.removeFavorite(place, this);

                        if (mCurrent != null && AppUtils.inNetwork(mCurrent)) {
                            Log.i(TAG, "saveFavoritePlace(): Network is connected");

                            if (firebaseProcess != null) {
                                if (mPrefManager != null) {
                                    String uid = mPrefManager.readString(PrefManager.USER_ID);
                                    firebaseProcess.removeFavoritePlace(PlaceFirebaseProcess.favorite_users, PlaceFirebaseProcess.favorite_places, uid, place);
                                }
                            }
                        }
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
    public void onReadPlaceByCategory(ArrayList<PlaceModel> data) {
        //Not Needed Here
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

    /**
     * Saving Favorite place Into SQLite DB callback
     */
    @Override
    public void onSaveFavoritePlace(boolean status, Throwable t) {
        if (status) {
            view.onSaveFavoritePlace(true);
        } else {
            t.printStackTrace();
        }
    }

    /**
     * Removing Favorite place From SQLite DB callback
     */
    @Override
    public void onRemoveFavoritePlace(boolean status, Throwable t) {
        if (status) {
            view.onRemoveFavoritePlace(true);
        } else {
            if (t != null) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Removing Favorite place From Firestore callback
     */
    @Override
    public void onRemoveFavoritePlaceFromFirebase(boolean status, Throwable t) {
        if (!status) {
            Log.i(TAG, "onRemoveFavoritePlaceFromFirebase(): status = false");
            if (t != null) {
                t.printStackTrace();
            }
        } else {
            Log.i(TAG, "onRemoveFavoritePlaceFromFirebase(): status = true");
        }
    }

    @Override
    public void onSaveFavoritePlaceIntoFirebase(boolean status, Throwable t) {
        if (!status) {
            Log.i(TAG, "onSaveFavoritePlaceIntoFirebase(): status = false");
            if (t != null) {
                t.printStackTrace();
            }
        } else {
            Log.i(TAG, "onSaveFavoritePlaceIntoFirebase(): status = true");
        }
    }
}
