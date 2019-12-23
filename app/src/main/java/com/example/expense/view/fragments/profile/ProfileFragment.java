package com.example.expense.view.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.expense.R;
import com.example.expense.view.activities.signInUp.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "ProfileFragment";

    @BindView(R.id.user_profile_photo)
    ImageView profileImage;
    @BindView(R.id.user_profile_name)
    TextView profileName;
    @BindView(R.id.user_profile_email)
    TextView profileMail;
    @BindView(R.id.profile_phone_number)
    TextView profilePhoneNumber;
    @BindView(R.id.profile_country)
    TextView profileCountry;
    @BindView(R.id.change_photo)
    CircleImageView changePhoto;
    @BindView(R.id.profile_content_layout)
    ConstraintLayout profileContentLayout;
    @BindView(R.id.log_in_button)
    Button logInButton;
    @BindView(R.id.register_layout)
    ConstraintLayout registerLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        try {
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            FirebaseUser user = mAuth.getCurrentUser();
//            String name = null;
//            String email = null;
//            String gender = null;
//            if (user != null) {
//                name = user.getDisplayName();
//                email = user.getEmail();
//                gender = user.getPhoneNumber();
//                profileName.setText(name);
//                profileMail.setText(email);
//
//            } else {
//                profileName.setText("");
//                profileMail.setText("");
//            }
//            if (user != null && user.getPhotoUrl() != null)
//                Picasso.get().load(user.getPhotoUrl().toString()).into(profileImage);
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                registerLayout.setVisibility(View.VISIBLE);
                profileContentLayout.setVisibility(View.GONE);
            }else{
                registerLayout.setVisibility(View.GONE);
                profileContentLayout.setVisibility(View.VISIBLE);
            }
            logInButton.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getContext() , SignInActivity.class));
    }
}