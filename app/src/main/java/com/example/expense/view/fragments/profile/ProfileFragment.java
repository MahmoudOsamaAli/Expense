package com.example.expense.view.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.expense.R;
import com.example.expense.pojo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_mail)
    TextView profileMail;
    @BindView(R.id.profile_phone_number)
    TextView profilePhoneNumber;
    @BindView(R.id.profile_country)
    TextView profileCountry;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this , view);
        init();
    }

    private void init() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String name = null;
        String email = null;
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            profileName.setText(name);
            profileMail.setText(email);
        }else{
            profileName.setText("");
            profileMail.setText("");
        }
        profileImage.setImageResource(R.drawable.restaurant);
    }
}
