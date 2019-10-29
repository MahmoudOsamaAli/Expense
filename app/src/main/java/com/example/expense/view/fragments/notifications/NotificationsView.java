package com.example.expense.view.fragments.notifications;

import com.example.expense.pojo.RestaurantModel;

import java.util.ArrayList;

public interface NotificationsView {
    void onGetNotifications(ArrayList<RestaurantModel> data);
}
