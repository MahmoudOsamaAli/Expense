package com.example.expense.data.firebase.callbacks;



import com.example.expense.pojo.Model.LocationModel;

import java.util.ArrayList;

public interface LocationFBListener {

    void onReadPlaceLocation(ArrayList<LocationModel> data, Throwable t);
}
