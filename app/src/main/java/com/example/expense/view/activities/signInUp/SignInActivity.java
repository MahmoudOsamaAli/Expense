package com.example.expense.view.activities.signInUp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expense.R;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
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
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        handler.postDelayed(runnable1, 3000);
        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        logInButton.setOnClickListener(this);
        signUpTxt.setOnClickListener(this);
        backTV.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        fbLogIn.setOnClickListener(this);
    }

    private void startSignWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void startSignInWithFB() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleAccessToken(loginResult.getAccessToken() , null);
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
    }

    private void handleAccessToken(AccessToken token , GoogleSignInAccount acct) {
        Log.d(TAG, "handleAccessToken:" + token);
        if (acct == null) {
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            getCredential(credential);
        }
        else {
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            getCredential(credential);
        }
    }
    private void getCredential(AuthCredential credential){
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
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    handleAccessToken(null, account);
                } else {
                    Toast.makeText(this, "account is null", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                finish();
                Toast.makeText(this, "you are logged in", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "sign in please", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_in_button:
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
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
        }
    }
}
