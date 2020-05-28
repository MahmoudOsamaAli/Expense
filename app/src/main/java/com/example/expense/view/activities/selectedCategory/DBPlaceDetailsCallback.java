package com.example.expense.view.activities.selectedCategory;

public interface DBPlaceDetailsCallback {

    void onSaveFavoritePlace(boolean status, Throwable t);

    void onRemoveFavoritePlace(boolean status, Throwable t);

}
