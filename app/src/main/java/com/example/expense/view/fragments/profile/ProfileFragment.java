package com.example.expense.view.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.expense.R;
import com.example.expense.Utilities.AppUtils;
import com.example.expense.Utilities.PrefManager;
import com.example.expense.view.activities.signInUp.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener, ProfileView {

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

    @BindView(R.id.edit_name)
    ImageView mEditName;

    @BindView(R.id.edit_phone_number)
    ImageView mEditPhone;

    @BindView(R.id.change_photo)
    CircleImageView changePhoto;

    @BindView(R.id.profile_content_layout)
    ConstraintLayout profileContentLayout;

    @BindView(R.id.log_in_button)
    Button logInButton;

    @BindView(R.id.register_layout)
    ConstraintLayout registerLayout;


    //-------------  Edit Field Dialog  -------------
//    @BindView(R.id.edit_profile_dialog_field_title)
    TextView mFieldTitleTV;

    //    @BindView(R.id.edit_profile_dialog_field_et)
    EditText mFieldET;

    //    @BindView(R.id.edit_profile_dialog_field_max_ems_tv)
    TextView mFieldMaxEmsTV;

    //    @BindView(R.id.edit_profile_dialog_save_tv)
    TextView mSaveTV;

    //    @BindView(R.id.edit_profile_dialog_cancel_tv)
    TextView mCancelTV;

    private ProfilePresenter presenter;

    private Context mCurrent;

    private String name,email,phoneNumber;

    private PrefManager mPrefManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            ButterKnife.bind(this, view);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            mCurrent = getContext();
            mPrefManager = new PrefManager(mCurrent);
            presenter = new ProfilePresenter(mCurrent, this);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                registerLayout.setVisibility(View.VISIBLE);
                profileContentLayout.setVisibility(View.GONE);
            } else {
                registerLayout.setVisibility(View.GONE);
                profileContentLayout.setVisibility(View.VISIBLE);

                name = mPrefManager.readString(PrefManager.USER_NAME);
                phoneNumber = mPrefManager.readString(PrefManager.USER_PHONE);
                email = mPrefManager.readString(PrefManager.USER_EMAIL);

                if (user != null) {
//                     name = user.getDisplayName();
                    if (name != null) profileName.setText(name);
                    else profileName.setText("");

                     email = user.getEmail();
                    if (email != null) profileMail.setText(email);
                    else profileMail.setText("");

//                    phoneNumber = user.getPhoneNumber();
                    if (phoneNumber != null) profilePhoneNumber.setText(phoneNumber);
                    else profilePhoneNumber.setText("");

                    Uri photoUrl = user.getPhotoUrl();
                    if (photoUrl != null)
                        Picasso.get().load(photoUrl.toString()).into(profileImage);
                    else Picasso.get().load(R.drawable.empty_profile_image).into(profileImage);
                }
            }
            logInButton.setOnClickListener(this);

            mEditName.setOnClickListener(this);
            mEditPhone.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.log_in_button) {
            mPrefManager.saveString(PrefManager.LOGIN_FROM,"Profile");
            startActivity(new Intent(getContext(), SignInActivity.class));
        } else if (view.equals(mEditName)) {
            handleEditName(view);
        } else if (view.equals(mEditPhone)) {
            handleEditPhone(view);
        }
    }

    private void handleEditName(View v) {
        try {
            if (getContext() != null) {
                MaterialDialog dlg = AppUtils.showAlertDialogWithCustomView(getContext(), R.layout.edit_profile_phone_number);
                mFieldTitleTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_field_title);
                mFieldET = (EditText) dlg.findViewById(R.id.edit_profile_dialog_field_et);
                mFieldMaxEmsTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_field_max_ems_tv);
                mSaveTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_save_tv);
                mCancelTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_cancel_tv);

                mFieldTitleTV.setText(getString(R.string.enter_your).concat(getString(R.string.your_name)));

                mFieldET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String size = String.valueOf(20 - s.length());
                        mFieldMaxEmsTV.setText(size);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                mFieldET.setText(name);

                mSaveTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFieldET.getText() == null || mFieldET.getText().toString().isEmpty()) {
                            AppUtils.showToast(getContext(), getString(R.string.name_connot_be_empty));
                        } else if (!profileName.getText().toString().matches(mFieldET.getText().toString())) {
                            if (dlg.isShowing())
                                dlg.dismiss();
                            handleNameUpdate(mFieldET.getText().toString());
                        }
                    }
                });

                mCancelTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dlg.isShowing())
                            dlg.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

    private void handleEditPhone(View v) {
        try {
            if (getContext() != null) {
                MaterialDialog dlg = AppUtils.showAlertDialogWithCustomView(getContext(), R.layout.edit_profile_phone_number);
                mFieldTitleTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_field_title);
                mFieldET = (EditText) dlg.findViewById(R.id.edit_profile_dialog_field_et);
                mFieldMaxEmsTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_field_max_ems_tv);
                mSaveTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_save_tv);
                mCancelTV = (TextView) dlg.findViewById(R.id.edit_profile_dialog_cancel_tv);

                mFieldTitleTV.setText(getString(R.string.enter_your).concat(" " +getString(R.string.your_phone_number)));

                mFieldET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String size = String.valueOf(20 - s.length());
                        mFieldMaxEmsTV.setText(size);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                mFieldET.setText(phoneNumber);

                mSaveTV.setOnClickListener(v1 -> {
                    if (mFieldET.getText() == null || mFieldET.getText().toString().isEmpty()) {
                        AppUtils.showToast(getContext(), getString(R.string.name_connot_be_empty));
                    } else if ((profilePhoneNumber.getText() == null || profilePhoneNumber.getText().toString().isEmpty()) ||
                            (profilePhoneNumber.getText() != null && !profileName.getText().toString().matches(mFieldET.getText().toString()))) {

                        if (dlg.isShowing())
                            dlg.dismiss();
                        handlePhoneUpdate(mFieldET.getText().toString());
                    }
                });

                mCancelTV.setOnClickListener(v12 -> {
                    if (dlg.isShowing())
                        dlg.dismiss();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNameUpdate(String name) {
        try {
            profileName.setText(name);
            if (presenter != null) {
                presenter.updateName(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePhoneUpdate(String phone) {
        try {
            profilePhoneNumber.setText(phone);
            if (presenter != null) {
                presenter.updatePhone(phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProfileUpdated(boolean status, Throwable t) {

    }
}