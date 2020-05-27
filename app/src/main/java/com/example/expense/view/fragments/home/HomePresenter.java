package com.example.expense.view.fragments.home;

import com.example.expense.data.Data;
import com.example.expense.pojo.HomeViewModel;

import java.util.ArrayList;

public class HomePresenter {

    private HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }

    private ArrayList<HomeViewModel> getCategories(){
        return Data.getCategoriesData();
    }
    void getCategoriesData(){
        view.onGetCategories(getCategories());
    }
}
