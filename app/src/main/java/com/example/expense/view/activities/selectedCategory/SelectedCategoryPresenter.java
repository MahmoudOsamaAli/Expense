package com.example.expense.view.activities.selectedCategory;

import com.example.expense.data.Data;
import com.example.expense.pojo.RestaurantModel;
import com.example.expense.view.activities.selectedCategory.SelectedCategoryView;

import java.util.ArrayList;

class SelectedCategoryPresenter {

    private SelectedCategoryView view;

    SelectedCategoryPresenter(SelectedCategoryView view) {
        this.view = view;
    }

    private ArrayList<RestaurantModel> getCategoryList(){
        return Data.getSelectedCategoryData();
    }
    void getList(){
        view.onGetCategoryData(getCategoryList());
    }
}
