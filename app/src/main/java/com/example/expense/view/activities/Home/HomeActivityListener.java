package com.example.expense.view.activities.Home;

import com.example.expense.pojo.Model.PlaceModel;

import java.util.ArrayList;

public interface HomeActivityListener {
    void readPlacesByCategoryFromFirebase(ArrayList<PlaceModel> places);
}
