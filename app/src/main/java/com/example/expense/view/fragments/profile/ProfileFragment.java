package com.example.expense.view.fragments.profile;

import android.content.Intent;
import android.net.Uri;
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
    @BindView(R.id.account_name)
    TextView profileName;
    @BindView(R.id.mail)
    TextView profileMail;
    @BindView(R.id.phone_number)
    TextView profilePhoneNumber;
    @BindView(R.id.country_text)
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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                registerLayout.setVisibility(View.VISIBLE);
                profileContentLayout.setVisibility(View.GONE);
            }else{
                registerLayout.setVisibility(View.GONE);
                profileContentLayout.setVisibility(View.VISIBLE);
                if (user != null) {
                    String name = user.getDisplayName();
                    if(name != null) profileName.setText(name);
                    else profileName.setText("");

                    String email = user.getEmail();
                    if(email != null) profileMail.setText(email);
                    else profileMail.setText("");

                    String phoneNumber = user.getPhoneNumber();
                    if(phoneNumber != null) profilePhoneNumber.setText(phoneNumber);
                    else profilePhoneNumber.setText("");

                    Uri photoUrl = user.getPhotoUrl();
                    if(photoUrl != null) Picasso.get().load(photoUrl.toString()).into(profileImage);
                    else Picasso.get().load(R.drawable.empty_profile_image).into(profileImage);
                }
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