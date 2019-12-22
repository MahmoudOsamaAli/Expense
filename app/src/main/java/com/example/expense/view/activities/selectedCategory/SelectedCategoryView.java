package com.example.expense.view.activities.selectedCategory;


import com.example.expense.pojo.Model.PlaceModel;

import java.util.ArrayList;

public interface SelectedCategoryView {
    void readPlacesByCategoryFromFirebase(ArrayList<PlaceModel> places);
}
