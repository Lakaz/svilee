package com.mobiwarez.laki.seapp.ui.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.evernote.android.job.JobManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobiwarez.laki.seapp.DashBoardActivity;
import com.mobiwarez.laki.seapp.R;
import com.mobiwarez.laki.seapp.prefs.GetUserData;
import com.mobiwarez.laki.seapp.prefs.UserInfo;
import com.mobiwarez.laki.seapp.remotebackend.AppEngineBackend;

import com.example.laki.myapplication.backend.myApi.MyApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{


    private static final String TAG = "Face book";
    private Button loginButton, createAccountButton, googleBtn, facebookBtn;
    private Button signInBtn;
    private CoordinatorLayout parent;
    //private ContentLoginBinding contentLoginBinding;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    //CallbackManager callbackManager;
    //private static final int RC_SIGN_IN = 9001;
    //private LoginButton fLoginButton;

    private static final String UNCHANGED_CONFIG_VALUE = "CHANGE-ME";
    //private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String GOOGLE_TOS_URL = "https://s3.amazonaws.com/sachestaticweb/terms.html";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    //private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google.com/policies/privacy/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://s3.amazonaws.com/sachestaticweb/licence.htm";
    private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google.com/terms/analytics/#7_privacy";
    private static final int RC_SIGN_IN = 100;

    private ProgressDialog progressDialog;

    private CoordinatorLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");
        setSupportActionBar(toolbar);

        JobManager.create(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting ready...");
        progressDialog.setIndeterminate(true);


        root = findViewById(R.id.main_log_in);
        mAuth = FirebaseAuth.getInstance();
        signInBtn = findViewById(R.id.login_button);
        signInBtn.setOnClickListener(view -> startSingInActivity());


    }


    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(root, errorMessageRes, Snackbar.LENGTH_LONG).show();
        Snackbar.make(root, "errorMessageRes", Snackbar.LENGTH_LONG).show();
    }


    private void updateUI(FirebaseUser user) {


        if (user != null) {

            if (GetUserData.getUserToken(this).equals("no_token")) {

            }

            else {
                if (GetUserData.getStatusOfToken(this)) {

                }

                else {
                    registerToken(GetUserData.getUserToken(this), user.getUid());
                }
            }



            //if (user.getEmail() != null )

            String photoUrl = null;
            if(user.getPhotoUrl() != null) {
                photoUrl = user.getPhotoUrl().toString();
            }

            UserInfo.saveUserInfo(user.getEmail(), user.getDisplayName(), photoUrl, user.getUid(), this);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            gotoMain();
        }

        else {
            //gotoMain();
            startSingInActivity();
        }

    }

    private void registerToken(final String token, final String email) {
        final SignInActivity.RegisterTokenTask registerTokenTask = new SignInActivity.RegisterTokenTask();

        registerTokenTask.execute(email, token);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if ( registerTokenTask.getStatus() == AsyncTask.Status.RUNNING ) {
                registerTokenTask.cancel(true);
                //SubmitTokeJob.scheduleSubmitTokenJob(email, token);
            }
        }, 7000 );
    }


    private class RegisterTokenTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            MyApi myApi = AppEngineBackend.getInstance();

            String email = strings[0];
            String token = strings[1];

            try {
                myApi.serveRegistrationToken(email, token).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }

            //UserInfo.markToken(true, LoginActivity.this);
        }
    }


    private void startSingInActivity() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.mineTheme)
                        .setLogo(getSelectedLogo())
                        .setAvailableProviders(getSelectedProviders())
                        .setTosUrl(getSelectedTosUrl())
                        .setPrivacyPolicyUrl(GOOGLE_PRIVACY_POLICY_URL)
                        .setIsSmartLockEnabled(true)
                        .setAllowNewEmailAccounts(true)
                        .build(),
                RC_SIGN_IN);
    }

/*
    private String getSelectedPrivacyPolicyUrl() {
        //if (mUseGooglePrivacyPolicy.isChecked()) {
            return GOOGLE_PRIVACY_POLICY_URL;
        //}

        //return FIREBASE_PRIVACY_POLICY_URL;
    }
*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startSignedInActivity(response);
            finish();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }

    private void startSignedInActivity(IdpResponse response) {

        Intent intent = new Intent(this, DashBoardActivity.class);
        startActivity(intent);
    }


    private String getSelectedTosUrl() {
        return GOOGLE_TOS_URL;
    }

    //@MainThread
    private String getSelectedPrivacyPolicyUrl() {
        return GOOGLE_PRIVACY_POLICY_URL;
    }

    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();


        selectedProviders.add(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                        .setPermissions(getGooglePermissions())
                        .build());

        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        return selectedProviders;
    }


    private List<String> getGooglePermissions() {
        List<String> result = new ArrayList<>();
/*
        if (mGoogleScopeYoutubeData.isChecked()) {
            result.add("https://www.googleapis.com/auth/youtube.readonly");
        }
        if (mGoogleScopeDriveFile.isChecked()) {
            result.add(Scopes.DRIVE_FILE);
        }
*/
        return result;
    }


    private List<String> getFacebookPermissions() {
        List<String> result = new ArrayList<>();
        //if (mFacebookScopeFriends.isChecked()) {
        result.add("user_friends");
        //}
        //if (mFacebookScopePhotos.isChecked()) {
        result.add("user_photos");
        //}
        return result;
    }


    private int getSelectedLogo() {
        return R.drawable.ic_babbysilo_logo;//.NO_LOGO;
    }

    private void gotoMain() {

        Intent mainIntent = new Intent(this, DashBoardActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect. Check your internet connection", Toast.LENGTH_SHORT).show();
    }


}
