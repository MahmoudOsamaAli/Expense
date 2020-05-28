package com.example.expense.view.activities.placeDetails;

import com.example.expense.pojo.PlaceImage;
import com.example.expense.pojo.locationModel;

import java.util.ArrayList;

public interface PlaceDetailsView {

//    void onGetLocations(ArrayList<locationModel> list);
//    void onGetPlaceImages(ArrayList<PlaceImage> list);
//    void onGetSeekBarRating(ArrayList<Integer> list);
    void onGetChartRating(double value);

    void onSaveFavoritePlace(boolean status);

    void onRemoveFavoritePlace(boolean status);
}
