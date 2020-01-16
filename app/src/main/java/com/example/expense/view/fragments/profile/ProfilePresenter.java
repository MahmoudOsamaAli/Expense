package com.example.expense.view.fragments.profile;

import android.content.Context;

import com.example.expense.Utilities.PrefManager;
import com.example.expense.Utilities.UserColumns;
import com.example.expense.data.firebase.UsersFirebase;
import com.example.expense.data.firebase.callbacks.UsersFirebaseListener;

import java.util.HashMap;
import java.util.Map;

public class ProfilePresenter implements UsersFirebaseListener {

    private Context context;
    private ProfileView callback;
    private PrefManager mPrefManager;

    private UsersFirebase mUsersFirebase;

    public ProfilePresenter(Context context, ProfileView callback) {
        this.context = context;
        this.callback = callback;
        this.mPrefManager = new PrefManager(context);
        this.mUsersFirebase = new UsersFirebase(this);
    }

    void updateName(String name) {
        try {

            Map<String, Object> fields = new HashMap<>();

            fields.put(UserColumns.NAME, name);
            String uid = mPrefManager.readString(PrefManager.USER_ID);
            mPrefManager.saveString(PrefManager.USER_NAME, name);

            mUsersFirebase.updateUser(fields, uid);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void updatePhone(String phone) {
        try {

            Map<String, Object> fields = new HashMap<>();

            fields.put(UserColumns.PHONE, phone);
            String uid = mPrefManager.readString(PrefManager.USER_ID);
            mPrefManager.saveString(PrefManager.USER_PHONE, phone);

            mUsersFirebase.updateUser(fields, uid);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSaveNewUserIntoFirestore(boolean status, Throwable t) {
        // Do nothing
    }

    @Override
    public void onUpdateUserInfo(boolean status, Throwable t) {

        if (status) {
            callback.onProfileUpdated(true, null);
        } else {
            if (t != null) {
                callback.onProfileUpdated(false, t);
            } else {
                callback.onProfileUpdated(false, null);
            }
        }
    }
}
