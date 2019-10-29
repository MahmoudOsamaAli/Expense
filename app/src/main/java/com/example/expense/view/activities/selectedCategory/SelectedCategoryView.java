package com.example.expense.view.activities.selectedCategory;


import com.example.expense.pojo.RestaurantModel;

import java.util.ArrayList;

public interface SelectedCategoryView {
    void onGetCategoryData(ArrayList<RestaurantModel> list);
}
