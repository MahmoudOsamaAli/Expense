package com.example.expense.data.firebase;

import android.content.Context;
import android.util.Log;

import com.example.expense.Utilities.PlaceColumns;
import com.example.expense.adapters.ImagesFirebaseProcess;
import com.example.expense.data.firebase.callbacks.LocationFBListener;
import com.example.expense.data.firebase.callbacks.PlaceFirebaseListener;
import com.example.expense.data.sqllite.DBProcess;
import com.example.expense.pojo.Model.LocationModel;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.pojo.Model.RequestModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceFirebaseProcess implements LocationFBListener {

    private FirebaseFirestore db;
    private final static String TAG = "PlaceFirebase";
    private final static String requests = "Requests";

    public final static String categories_collection = "Categories";
    public final static String restaurant_document = "Restaurant";
    public final static String places_collection = "places";
    public final static String favorite_places = "Favorites";
    public final static String favorite_users = "Users";
    public final static String user_document = "User";

    private LocationsFirebaseProcess locationsFirebase;
    private ImagesFirebaseProcess imagesFirebase;
    private DBProcess dbProcess;
    private Context context;
    private PlaceFirebaseListener callback;
    private ArrayList<PlaceModel> placesResult;

    public PlaceFirebaseProcess(Context context, PlaceFirebaseListener callback) {
        this.context = context;
        this.dbProcess = new DBProcess(this.context);
        this.db = FirebaseFirestore.getInstance();
        this.locationsFirebase = new LocationsFirebaseProcess(db, this);
        this.callback = callback;

    }

    public void readPlacesByCategory(String root, String category, String collection) {
        try {
            db.collection(root).document(category).collection(collection).addSnapshotListener((snapshot, e) -> {
                if (e == null) {
                    try {
                        //initializing places result list
                        placesResult = new ArrayList<>();
                        if (snapshot != null) {
                            Log.i(TAG, "readPlacesByCategory(): snapshot not null");
                            Log.i(TAG, "readPlacesByCategory(): snapshot size = " + snapshot.size());

                            for (QueryDocumentSnapshot queryDocumentSnapshot : snapshot) {
                                //initializing location result list
                                ArrayList<LocationModel> locationsResult = new ArrayList<>();
                                //initializing images result list
                                ArrayList<String> imagesResult = new ArrayList<>();

                                //reading place data from document
                                HashMap<String, Object> data = (HashMap<String, Object>) queryDocumentSnapshot.getData();
                                //reading locations data from document
                                ArrayList<Map<String, String>> locations = (ArrayList<Map<String, String>>) data.get(PlaceColumns.locationModels);
                                //reading images data from document
                                ArrayList<String> images = (ArrayList<String>) queryDocumentSnapshot.get(PlaceColumns.imagesURL);

                                Log.i(TAG, "readPlacesByCategory(): data size = " + data.size());
                                //extracting locations from result
                                extractLocations(locations, locationsResult);
                                //extracting images from result
                                extractImages(imagesResult, images);

                                //extracting place from result
                                PlaceModel placeModel = extractPlace(queryDocumentSnapshot, data, imagesResult, locationsResult);
                                //adding place result into places result list
                                placesResult.add(placeModel);

                                Log.i(TAG, "readPlacesByCategory(): places size = " + placesResult.size());

                            }
                        } else {
                            Log.i(TAG, "readPlacesByCategory(): snapshot is null");
                        }
                        //returning result to callback
                        returningPlacesByCategoryResult(callback, placesResult);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returningPlacesByCategoryResult(PlaceFirebaseListener callback, ArrayList<PlaceModel> placeModels) {
        try {
            //checking callback nullability
            if (callback != null) {
                //sending result over callback
                callback.onReadPlaceByCategory(placeModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractLocations(ArrayList<Map<String, String>> locations, ArrayList<LocationModel> locationModels) {
        try {
            //checking if result not null or empty
            if (locations != null && !locations.isEmpty()) {
                //looping over location from FireStore
                for (Map<String, String> location : locations) {
                    Log.i(TAG, "extractLocations(): locations size = " + location.size());
                    try {
                        //extracting location into location model
                        LocationModel locationModel = extractLocation(location);
                        //adding location into list
                        locationModels.add(locationModel);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extractImages(ArrayList<String> imageModels, ArrayList<String> images) {
        try {
            //checking if images not null or empty
            if (images != null && !images.isEmpty()) {
                Log.i(TAG, "extractImages(): images size = " + images.size());
                //looping over images from FireStore
                for (String image : images) {
                    try {
                        //adding image into list
                        imageModels.add(image);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                Log.i(TAG, "extractImages(): images size = 0 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PlaceModel extractPlace(QueryDocumentSnapshot queryDocumentSnapshot, HashMap<String, Object> data, ArrayList<String> imageModels, ArrayList<LocationModel> locationModels) {
        return new PlaceModel(
                queryDocumentSnapshot.getId(),
                (String) data.get(PlaceColumns.name),
                (String) data.get(PlaceColumns.category),
                (String) data.get(PlaceColumns.phoneNumber),
                (String) data.get(PlaceColumns.description),
                (String) data.get(PlaceColumns.facebookUrl),
                (String) data.get(PlaceColumns.twitterUrl),
                (String) data.get(PlaceColumns.instagramUrl),
                (String) data.get(PlaceColumns.websiteUrl),
                Integer.parseInt(String.valueOf(data.get(PlaceColumns.likesCount))),
                Integer.parseInt(String.valueOf(data.get(PlaceColumns.okayCount))),
                Integer.parseInt(String.valueOf(data.get(PlaceColumns.dislikesCount))),
                locationModels,
                imageModels
        );
    }

    private LocationModel extractLocation(Map<String, String> location) {
        return new LocationModel(
                location.get(PlaceColumns.id),
                location.get(PlaceColumns.placeID),
                location.get(PlaceColumns.country),
                location.get(PlaceColumns.city),
                location.get(PlaceColumns.street),
                Double.parseDouble(String.valueOf(location.get(PlaceColumns.latitude))),
                Double.parseDouble(String.valueOf(location.get(PlaceColumns.longitude))
                ));
    }

    public void requestToAddPlace(RequestModel placeModel, PlaceFirebaseListener listener) {
        db.collection(requests).add(placeModel).addOnCompleteListener(task -> {
            try {
                if (task.isSuccessful()) {
                    if (listener != null) {
                        listener.onRequestPlaceSuccess(true, null);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).addOnFailureListener(e -> {
            if (listener != null) {
                listener.onRequestPlaceSuccess(false, e);
            }
        }).addOnCanceledListener(() -> {
            if (listener != null) {
                listener.onRequestPlaceSuccess(false, null);
            }
        });
    }

    @Override
    public void onReadPlaceLocation(ArrayList<LocationModel> data, Throwable t) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFavoritePlaces(String favorite_places_child, String users_root, String UID) {
        try {
            if (db != null) {
                db.collection(users_root).document(user_document).collection(UID).document(favorite_places_child).collection(places_collection).addSnapshotListener((documentSnapshot, e) -> {
                    try {
                        ArrayList<PlaceModel> favoriteResult = new ArrayList<>();

                        if (documentSnapshot != null) {
                            for (QueryDocumentSnapshot snapshot : documentSnapshot) {

                                //initializing location result list
                                ArrayList<LocationModel> locationsResult = new ArrayList<>();
                                //initializing images result list
                                ArrayList<String> imagesResult = new ArrayList<>();

                                //reading place data from document
                                HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                                //reading locations data from document
                                ArrayList<Map<String, String>> locations = (ArrayList<Map<String, String>>) data.get(PlaceColumns.locationModels);
                                //reading images data from document
                                ArrayList<String> images = (ArrayList<String>) snapshot.get(PlaceColumns.imagesURL);

                                Log.i(TAG, "readPlacesByCategory(): data size = " + data.size());
                                //extracting locations from result
                                extractLocations(locations, locationsResult);
                                //extracting images from result
                                extractImages(imagesResult, images);

                                //extracting place from result
                                PlaceModel placeModel = extractPlace(snapshot, data, imagesResult, locationsResult);
                                //adding place result into places result list
                                favoriteResult.add(placeModel);
                            }
                            if (callback != null) {
                                callback.onReadFavoritePlaces(favoriteResult);
                            }
                        }

                        if (e != null) {
                            e.printStackTrace();
                            if (callback != null) {
                                callback.onReadFavoritePlaces(favoriteResult);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (callback != null) {
                            callback.onReadFavoritePlaces(null);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onReadFavoritePlaces(null);
            }
        }
    }

    public void saveFavoritePlace(String users_root, String user_document,String favorite_places_child, String UID, PlaceModel place) {
        try {

            Log.i(TAG, "saveFavoritePlace(): " + UID);

            db.collection(users_root).document(user_document).collection(UID).document(favorite_places_child).collection(places_collection).document(place.getId()).set(place).addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful()) {
                        if (callback != null) {
                            callback.onSaveFavoritePlaceIntoFirebase(true, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).addOnFailureListener(e -> {
                if (callback != null) {
                    callback.onSaveFavoritePlaceIntoFirebase(false, e);
                }
            }).addOnCanceledListener(() -> {
                if (callback != null) {
                    callback.onSaveFavoritePlaceIntoFirebase(false, null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFavoritePlace(String favorite_users, String favorite_places, String uid, PlaceModel place) {
        try {
            db.collection(favorite_users).document(user_document).collection(uid).document(favorite_places).collection(places_collection).document(place.getId()).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (callback != null) {
                        callback.onRemoveFavoritePlaceFromFirebase(true, null);
                    }
                } else if (task.isCanceled()) {
                    if (callback != null) {
                        callback.onRemoveFavoritePlaceFromFirebase(false, null);
                    }
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                if (callback != null) {
                    callback.onRemoveFavoritePlaceFromFirebase(false, e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
