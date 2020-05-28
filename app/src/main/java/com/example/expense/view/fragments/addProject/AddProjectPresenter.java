package com.example.expense.view.fragments.addProject;

import android.content.Context;

import com.example.expense.adapters.ImagesFirebaseProcess;
import com.example.expense.data.firebase.PlaceFirebaseProcess;
import com.example.expense.data.firebase.callbacks.ImageFbListener;
import com.example.expense.data.firebase.callbacks.PlaceFirebaseListener;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.pojo.Model.RequestModel;

import java.io.File;
import java.util.ArrayList;

public class AddProjectPresenter implements PlaceFirebaseListener, ImageFbListener {

    private Context context;
    private PlaceFirebaseProcess placeDb;
    private ImagesFirebaseProcess imageDb;

    private ArrayList<File> imageFiles;
    ArrayList<String> imageUrls;
    private RequestModel place;

    private AddProjectView listener;

    AddProjectPresenter(Context context, AddProjectView mListener) {
        this.context = context;
        this.placeDb = new PlaceFirebaseProcess(context, this);
        this.imageDb = new ImagesFirebaseProcess(this);
        this.listener = mListener;
    }

    void createNewPlace(RequestModel placeModel, ArrayList<File> imagesFiles) {
        try {
            imageUrls = new ArrayList<>();
            place = placeModel;
            imageFiles = imagesFiles;
            if (imagesFiles != null && !imagesFiles.isEmpty()) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    imageDb.addPlaceImage(imageFiles.get(i), i);
                }
            }else{
                placeDb.requestToAddPlace(place, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadPlaceByCategory(ArrayList<PlaceModel> data) {

    }

    @Override
    public void onRequestPlaceSuccess(boolean status, Throwable t) {

        if (!status) {
            if (t != null) {
                t.printStackTrace();
            }
        }
        if (listener != null)
            listener.onAddPlace(status);
    }

    @Override
    public void onEditPlaceSuccess(boolean status, Throwable t) {

    }

    @Override
    public void onDeletePlace(boolean status) {

    }

    @Override
    public void onReadFavoritePlaces(ArrayList<PlaceModel> places) {

    }

    @Override
    public void onSaveFavoritePlace(boolean status, Throwable t) {

    }

    @Override
    public void onRemoveFavoritePlaceFromFirebase(boolean status, Throwable t) {

    }

    @Override
    public void onSaveFavoritePlaceIntoFirebase(boolean status, Throwable t) {

    }

    @Override
    public void onAddImageSuccess(String url, int position) {
        if (position == imageFiles.size() - 1) {
            imageUrls.add(url);
            place.setImagesURL(imageUrls);
            placeDb.requestToAddPlace(place, this);
        } else {
            imageUrls.add(url);
        }
    }

    @Override
    public void onAddImageFailure(Throwable t) {
        t.printStackTrace();
        listener.onAddPlace(false);
    }
}
