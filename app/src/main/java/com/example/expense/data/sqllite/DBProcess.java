package com.example.expense.data.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.expense.configs.App;
import com.example.expense.pojo.Model.LocationModel;
import com.example.expense.pojo.Model.PlaceModel;
import com.example.expense.view.activities.Home.DBHomeCallback;
import com.example.expense.view.activities.selectedCategory.DBPlaceDetailsCallback;
import com.example.expense.view.fragments.favorites.DBFavoritePlaceCallback;

import java.util.ArrayList;

public class DBProcess {

    private String TAG = "DBProcess";
    private Context c;
    private App mAppContext;

    public DBProcess(Context context) {
        this.c = context;
        this.mAppContext = (App) this.c.getApplicationContext();

    }

    public boolean deleteTable(String tableName) {
        return ((getTableCount(tableName) <= 0)) ||
                (mAppContext.dbConnect().delete(tableName, null, null) > 0);/* No. of deleted rows*/
    }

    private int getTableCount(String tableName) {

        String countLbl = "COUNT";
        String sqlStm = "SELECT COUNT(*)  AS " + countLbl + " FROM " + tableName;
        try {
            Cursor cursor = mAppContext.dbConnect().rawQuery(sqlStm, null);
            int tableCount = 0;
            if (cursor != null) {
                cursor.moveToFirst();
                tableCount = cursor.getInt(cursor.getColumnIndex(countLbl));
                cursor.close();
            }
            return tableCount;
        } catch (Exception ex) {
            ex.getMessage();
            return 0;
        }
    }

    private void insertLocation(LocationModel locationModel, String placeID) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConfig.LocationsTable.COLUMN_PLACE_ID, placeID);
            cv.put(DBConfig.LocationsTable.COLUMN_COUNTRY, locationModel.getCountry());
            cv.put(DBConfig.LocationsTable.COLUMN_CITY, locationModel.getCity());
            cv.put(DBConfig.LocationsTable.COLUMN_STREET, locationModel.getStreet());
            cv.put(DBConfig.LocationsTable.COLUMN_LATITUDE, locationModel.getLatitude());
            cv.put(DBConfig.LocationsTable.COLUMN_LONGITUDE, locationModel.getLongitude());

            mAppContext.dbConnect().insert(DBConfig.LocationsTable.TABLE_NAME, DBConfig.LocationsTable.COLUMN_ID, cv);
            mAppContext.dbDisconnect();

        } catch (Exception e) {
            e.printStackTrace();
            mAppContext.dbDisconnect();
        }
    }

    private void insertImage(String imageModel, String placeID) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConfig.ImagesTable.COLUMN_PLACE_ID, placeID);
            cv.put(DBConfig.ImagesTable.COLUMN_URL, imageModel);

            long result = mAppContext.dbConnect().insert(DBConfig.ImagesTable.TABLE_NAME, null, cv);
            Log.i(TAG, "insertImage(): result = " + result);
            mAppContext.dbDisconnect();
        } catch (Exception e) {
            e.printStackTrace();
            mAppContext.dbDisconnect();
        }
    }

    public void insertPlace(ArrayList<PlaceModel> placeModels) {

        try {
            deleteTable(DBConfig.PlacesTable.TABLE_NAME);
            deleteTable(DBConfig.ImagesTable.TABLE_NAME);
            deleteTable(DBConfig.LocationsTable.TABLE_NAME);

            for (PlaceModel placeModel : placeModels) {
                ContentValues cv = new ContentValues();

                cv.put(DBConfig.PlacesTable.COLUMN_ID, placeModel.getId());
                cv.put(DBConfig.PlacesTable.COLUMN_Name, placeModel.getName());
                cv.put(DBConfig.PlacesTable.COLUMN_CATEGORY, placeModel.getCategory());
                cv.put(DBConfig.PlacesTable.COLUMN_DESCRIPTION, placeModel.getDescription());
                cv.put(DBConfig.PlacesTable.COLUMN_PHONE_NUMBER, placeModel.getPhoneNumber());
                cv.put(DBConfig.PlacesTable.COLUMN_FACEBOOK_URL, placeModel.getFacebookUrl());
                cv.put(DBConfig.PlacesTable.COLUMN_TWITTER_URL, placeModel.getTwitterUrl());
                cv.put(DBConfig.PlacesTable.COLUMN_WEBSITE_URL, placeModel.getWebsiteUrl());
                cv.put(DBConfig.PlacesTable.COLUMN_LIKES_COUNT, placeModel.getLikesCount());
                cv.put(DBConfig.PlacesTable.COLUMN_OKAY_COUNT, placeModel.getOkayCount());
                cv.put(DBConfig.PlacesTable.COLUMN_DISLIKES_COUNT, placeModel.getDislikesCount());

                mAppContext.dbConnect().insert(DBConfig.PlacesTable.TABLE_NAME, null, cv);
                mAppContext.dbDisconnect();

                for (LocationModel locationModel : placeModel.getLocationModels()) {
                    insertLocation(locationModel, placeModel.getId());
                }

                for (String imageModel : placeModel.getImagesURL()) {
                    insertImage(imageModel, placeModel.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mAppContext.dbDisconnect();
        }
    }

    public void saveFavorite(PlaceModel place, Object callback) {
        try {
            ContentValues cv = new ContentValues();

            cv.put(DBConfig.FavoritePlacesTable.COLUMN_ID, place.getId());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_Name, place.getName());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_CATEGORY, place.getCategory());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_DESCRIPTION, place.getDescription());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_PHONE_NUMBER, place.getPhoneNumber());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_FACEBOOK_URL, place.getFacebookUrl());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_INSTAGRAM_URL, place.getFacebookUrl());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_TWITTER_URL, place.getTwitterUrl());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_WEBSITE_URL, place.getWebsiteUrl());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_LIKES_COUNT, place.getLikesCount());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_OKAY_COUNT, place.getOkayCount());
            cv.put(DBConfig.FavoritePlacesTable.COLUMN_DISLIKES_COUNT, place.getDislikesCount());

            mAppContext.dbConnect().insert(DBConfig.FavoritePlacesTable.TABLE_NAME, null, cv);
            mAppContext.dbDisconnect();

            if (callback instanceof DBPlaceDetailsCallback) {
                ((DBPlaceDetailsCallback) callback).onSaveFavoritePlace(true, null);
            } else if (callback instanceof DBHomeCallback) {
                ((DBHomeCallback) callback).onSaveFavorites(true, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback instanceof DBPlaceDetailsCallback) {
                ((DBPlaceDetailsCallback) callback).onSaveFavoritePlace(true, e);
            } else if (callback instanceof DBHomeCallback) {
                ((DBHomeCallback) callback).onSaveFavorites(true, e);
            }
        }
    }

    public void removeFavorite(PlaceModel place, DBPlaceDetailsCallback callback) {
        try {

            mAppContext.dbConnect().delete(DBConfig.FavoritePlacesTable.TABLE_NAME, DBConfig.FavoritePlacesTable.COLUMN_ID + " =?", new String[]{place.getId()});
            mAppContext.dbDisconnect();

            callback.onRemoveFavoritePlace(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onRemoveFavoritePlace(false, e);
        }
    }

    public void readFavorites(DBFavoritePlaceCallback callback) {

        try {
            ArrayList<PlaceModel> places = new ArrayList<>();
            String query = "SELECT * FROM " + DBConfig.FavoritePlacesTable.TABLE_NAME;

            Cursor cursor = mAppContext.dbConnect().rawQuery(query, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        try {
                            String id = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_ID));
                            String name = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_Name));
                            String category = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_CATEGORY));
                            String description = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_DESCRIPTION));
                            String phone = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_PHONE_NUMBER));
                            String facebook = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_FACEBOOK_URL));
                            String twitter = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_TWITTER_URL));
                            String instagram = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_INSTAGRAM_URL));
                            String website = cursor.getString(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_WEBSITE_URL));
                            int likes = cursor.getInt(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_LIKES_COUNT));
                            int okay = cursor.getInt(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_OKAY_COUNT));
                            int dislikes = cursor.getInt(cursor.getColumnIndex(DBConfig.FavoritePlacesTable.COLUMN_DISLIKES_COUNT));

                            PlaceModel place = new PlaceModel();
                            place.setId(id);
                            place.setName(name);
                            place.setCategory(category);
                            place.setDescription(description);
                            place.setPhoneNumber(phone);
                            place.setFacebookUrl(facebook);
                            place.setTwitterUrl(twitter);
                            place.setInstagramUrl(instagram);
                            place.setWebsiteUrl(website);
                            place.setLikesCount(likes);
                            place.setOkayCount(okay);
                            place.setDislikesCount(dislikes);
                            places.add(place);

                            cursor.moveToNext();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    cursor.close();

                    if (!places.isEmpty()) {

                        for (PlaceModel place : places) {
                            place.setImagesURL(readImages(place.getId()));
                        }

                        for (PlaceModel place : places) {
                            place.setLocationModels(readLocationModels(place.getId()));
                        }
                    }

                    callback.readFavorites(places);
                } else {
                    callback.readFavorites(null);
                }

                if (!cursor.isClosed()) {
                    cursor.close();
                }
            } else {
                callback.readFavorites(null);
            }


        } catch (Exception e) {
            e.printStackTrace();
            callback.readFavorites(null);
        }
    }

    private ArrayList<LocationModel> readLocationModels(String placeId) {
        ArrayList<LocationModel> locationModels = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + DBConfig.LocationsTable.TABLE_NAME + " WHERE " + DBConfig.LocationsTable.COLUMN_PLACE_ID + " = '" + placeId + "'";

            Cursor cursor = mAppContext.dbConnect().rawQuery(query, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        String city = cursor.getString(cursor.getColumnIndex(DBConfig.LocationsTable.COLUMN_CITY));
                        String country = cursor.getString(cursor.getColumnIndex(DBConfig.LocationsTable.COLUMN_COUNTRY));
                        String street = cursor.getString(cursor.getColumnIndex(DBConfig.LocationsTable.COLUMN_STREET));
                        double latitude = cursor.getDouble(cursor.getColumnIndex(DBConfig.LocationsTable.COLUMN_LATITUDE));
                        double longitude = cursor.getDouble(cursor.getColumnIndex(DBConfig.LocationsTable.COLUMN_LONGITUDE));

                        LocationModel location = new LocationModel();
                        location.setCity(city);
                        location.setCountry(country);
                        location.setStreet(street);
                        location.setLatitude(latitude);
                        location.setLongitude(longitude);

                        locationModels.add(location);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationModels;
    }

    private ArrayList<String> readImages(String id) {
        ArrayList<String> images = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + DBConfig.ImagesTable.TABLE_NAME + " WHERE " + DBConfig.ImagesTable.COLUMN_PLACE_ID + " = '" + id + "'";

            Cursor cursor = mAppContext.dbConnect().rawQuery(query, null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        String url = cursor.getString(cursor.getColumnIndex(DBConfig.ImagesTable.COLUMN_URL));
                        images.add(url);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }
}
