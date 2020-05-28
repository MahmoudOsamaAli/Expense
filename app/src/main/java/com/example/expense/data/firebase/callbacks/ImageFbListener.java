package com.example.expense.data.firebase.callbacks;

public interface ImageFbListener {

    void onAddImageSuccess(String url, int position);

    void onAddImageFailure(Throwable t);
}
