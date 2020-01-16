package com.example.expense.data.firebase.callbacks;

public interface UsersFirebaseListener {


    void onSaveNewUserIntoFirestore(boolean status, Throwable t);

    void onUpdateUserInfo(boolean status,Throwable t);

}
