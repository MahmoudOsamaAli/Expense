package com.example.expense.view.activities.displayImage;

import com.example.expense.data.Data;
import com.example.expense.pojo.PlaceImage;

import java.util.ArrayList;

class DisplayImagePresenter {

    private DisplayImageView view;

    DisplayImagePresenter(DisplayImageView view) {
        this.view = view;
    }

    private ArrayList<PlaceImage> getImagesList(){
        return Data.getPlaceImages();
    }
    void getList(){
        view.onGetImages(getImagesList());
    }
}
