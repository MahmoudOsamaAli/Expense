package com.example.expense.view.fragments.home;

import com.example.expense.pojo.HomeViewModel;

import java.util.ArrayList;

public interface HomeView {
    void onGetCategories(ArrayList<HomeViewModel> list);
}
