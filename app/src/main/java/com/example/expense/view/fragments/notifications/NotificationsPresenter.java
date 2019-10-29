package com.example.expense.view.fragments.notifications;

import com.example.expense.data.Data;
import com.example.expense.pojo.RestaurantModel;

import java.util.ArrayList;

public class NotificationsPresenter {

    private NotificationsView view;

    public NotificationsPresenter(NotificationsView view) {
        this.view = view;
    }
    private ArrayList<RestaurantModel> getNotificationsData (){
        return Data.getSelectedCategoryData();
    }
    void getNotifications(){
        view.onGetNotifications(getNotificationsData());
    }
}
