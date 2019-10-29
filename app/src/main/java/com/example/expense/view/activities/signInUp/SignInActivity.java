package com.example.expense.view.activities.signInUp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.expense.R;
import com.example.expense.Utilities.AppUtils;
import com.example.expense.view.activities.Home.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;

    @BindView(R.id.rellay1)
    LinearLayout rellay1;

    @BindView(R.id.progerss_bar)
    ProgressBar progressBar;

    @BindView(R.id.rellay2)
    LinearLayout rellay2;

    @BindView(R.id.log_in_button)
    Button logInButton;

    @BindView(R.id.txt_sign_up)
    TextView signUpTxt;

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.fb_login_button)
    Button fbLogIn;

    @BindView(R.id.tv_back)
    TextView backTV;

    @BindView(R.id.sign_google_button)
    Button mGoogleButton;

    @BindView(R.id.login_email)
    EditText mEmailLoginET;

    @BindView(R.id.login_password)
    EditText mPasswordLoginET;

    @BindView(R.id.first_last_name_et)
    EditText mFirstNameLastNameEditText;

    @BindView(R.id.email_et)
    EditText mEmailEditText;

    @BindView(R.id.password_et)
    EditText mPasswordEditText;

    @BindView(R.id.confirm_password_et)
    EditText mConfirmPasswordEditText;

    @BindView(R.id.normal_sign_button)
    Button mNormalSignUp;

    Handler handler = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.GONE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.GONE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_sign_in);
            ButterKnife.bind(this);
            handler.postDelayed(runnable1, 3000);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            mAuth = FirebaseAuth.getInstance();
            logInButton.setOnClickListener(this);
            signUpTxt.setOnClickListener(this);
            backTV.setOnClickListener(this);
            mGoogleButton.setOnClickListener(this);
            fbLogIn.setOnClickListener(this);
            mNormalSignUp.setOnClickListener(this);

            mConfirmPasswordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (mPasswordEditText.getText() == null || mPasswordEditText.getText().toString().isEmpty()) {
                            mConfirmPasswordEditText.setText("");
                            mPasswordEditText.setError(getString(R.string.required));
                        } else {
                            String password = mPasswordEditText.getText().toString().trim();
                            String confirmPassword = s.toString();

                            if (password.matches(confirmPassword)) {
                                mConfirmPasswordEditText.setTextColor(getResources().getColor(R.color.progress_start));
                                mPasswordEditText.setTextColor(getResources().getColor(R.color.progress_start));
                            } else if (password.length() > confirmPassword.length()) {
                                mPasswordEditText.setTextColor(getResources().getColor(R.color.text_color));
                                mConfirmPasswordEditText.setTextColor(getResources().getColor(R.color.text_color));
                            } else if (password.length() < confirmPassword.length()) {
                                mConfirmPasswordEditText.setTextColor(getResources().getColor(R.color.red));
                            } else if (password.length() == confirmPassword.length() && !password.matches(confirmPassword)) {
                                mConfirmPasswordEditText.setTextColor(getResources().getColor(R.color.red));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSignWithGoogle() {

        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSignInWithFB() {
        try {
            mCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleAccessToken(loginResult.getAccessToken(), null);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAccessToken(AccessToken token, GoogleSignInAccount acct) {
        try {
            Log.d(TAG, "handleAccessToken:" + token);
            if (acct == null) {
                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                getCredential(credential);
            } else {
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                getCredential(credential);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCredential(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.i(TAG, "GoogleSignIn onSuccess(): is called");
                    }
                }).addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        Log.i(TAG, "GoogleSignIn onComplete(): is called");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "GoogleSignIn onFailure(): is called");
                        e.printStackTrace();
                    }
                });

                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    handleAccessToken(null, account);
                } else {
                    Toast.makeText(this, "account is null", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
            Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
            Toast.makeText(this, "you are logged in", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "sign in please", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_in_button:
//                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                startNormalLogin();
                break;
            case R.id.txt_sign_up:
                handler.postDelayed(runnable2, 0);
                break;
            case R.id.tv_back:
                handler.postDelayed(runnable3, 0);
                break;
            case R.id.sign_google_button:
                startSignWithGoogle();
            case R.id.fb_login_button:
                startSignInWithFB();
            case R.id.normal_sign_button:
                startNormalSignUp();
        }
    }

    private void startNormalLogin() {
        try {

            if (mEmailLoginET.getText() == null || mEmailLoginET.getText().toString().isEmpty()) {
                mEmailLoginET.setError(getString(R.string.required));
            } else if (mPasswordLoginET.getText() == null || mPasswordLoginET.getText().toString().isEmpty()) {
                mPasswordLoginET.setError(getString(R.string.required));
            } else {
                MaterialDialog mProgressDlg = AppUtils.showProgressDialog(this, getString(R.string.loading));

                String email = mEmailLoginET.getText().toString().trim();
                String password = mPasswordLoginET.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (mProgressDlg != null)
                                    mProgressDlg.dismiss();

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startNormalSignUp() {
        try {

            if (mFirstNameLastNameEditText.getText() == null || mFirstNameLastNameEditText.getText().toString().isEmpty()) {
                mFirstNameLastNameEditText.setError(getString(R.string.required));
            } else if (mEmailEditText.getText() == null || mEmailEditText.getText().toString().isEmpty()) {
                mEmailEditText.setError(getString(R.string.required));
            } else if (mPasswordEditText.getText() == null || mPasswordEditText.getText().toString().isEmpty()) {
                mPasswordEditText.setError(getString(R.string.required));
            } else if (mConfirmPasswordEditText.getText() == null || mConfirmPasswordEditText.getText().toString().isEmpty()) {
                mConfirmPasswordEditText.setError(getString(R.string.required));
            } else {
                MaterialDialog mProgressDlg = AppUtils.showProgressDialog(this, getString(R.string.loading));

                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (mProgressDlg != null)
                                    mProgressDlg.dismiss();

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI(FirebaseUser user) {

        try {
            if (user != null) {
                //TODO save user data into shared preferences

                // Start home activity
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
