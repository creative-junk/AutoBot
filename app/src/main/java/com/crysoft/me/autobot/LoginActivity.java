package com.crysoft.me.autobot;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "FullScreenLogin";
    private static final int FACEBOOK_AUTH =1;
    private static final int GOOGLE_AUTH = 2;
    private static final int RC_SIGN_IN = 9001;
    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

    private ParseUser parseUser;
    private GoogleApiClient mGoogleApiClient;

    private Button btnFbLogin;
    private Button btnGoogleLogin;
    private Button btnSignIn;
    private Button btnSignup;
    private ImageView ivClose;
    private ProgressDialog mProgressDialog;

    private String email, firstName, lastName, name, timezone, gender, imageURL;
    private boolean isVerified;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
         /*   mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //   mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        btnFbLogin = (Button) findViewById(R.id.btnFbLogin);
        btnGoogleLogin = (Button) findViewById(R.id.btnGoogleLogin);


        //SIGN IN WITH FACEBOOK

        btnFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, mPermissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user == null) {
                            Log.d(TAG, "Shoot, User Cancelled the Facebook Login");
                        } else if (user.isNew()) {
                            Log.d(TAG, "User Signed Up and Logged in through Facebook");
                            getUserDetailsFromFB();
                        } else {
                            Log.d(TAG, "User Logged in through Facebook");
                            getUserDetailsFromParse();
                        }

                    }
                });
            }
        });


        //SIGN IN WITH GOOGLE
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });



        mVisible = true;
       /* mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);*/


        // Set up the user interaction to manually show or hide the system UI.
       /* mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
       /* mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);*/
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void googleSignIn() {

        Intent signinIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signinIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Facebook Login
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

        //Google Login
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    /*  @Override
      protected void onStart() {
          super.onStart();
          OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
          if (opr.isDone()){
              //If the user's credentials are cached, they will be available immediately at this point
              Log.d(TAG,"Got cached Sign in");
              GoogleSignInResult result = opr.get();
              handleGoogleSignInResult(result);

          }else {
              //The user has not previously signed in on this device or the session has expired.Single sign-on will occur here
              showProgressDialog();
              opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                  @Override
                  public void onResult(@NonNull GoogleSignInResult result) {
                      hideProgressDialog();
                      handleGoogleSignInResult(result);
                  }
              });
          }
      }*/
    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Logging you in...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleGoogleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //Signed in Successfully
            GoogleSignInAccount account = result.getSignInAccount();
            Log.i("Login Result", "User Successfully Logged In");
            email=account.getEmail();
            firstName=account.getGivenName();
            lastName=account.getFamilyName();
            isVerified=true;
            //get the 50X50 profile pic they send us
            final String pictureURL = account.getPhotoUrl().toString();

            new ProfilePicAsync(pictureURL,2).execute();

        } else {
            //Sign in failed or user signed out
            Log.i("Login Result", "Failed");
        }
    }

    private void getUserDetailsFromFB() {
        //Spanish dude gave us this after hours of hitting the wall. sweet -> https://disqus.com/by/dominiquecanlas/
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,picture,first_name,last_name,gender,timezone,verified");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        //Handle the result here
                        try {
                            email = response.getJSONObject().getString("email");
                            name = response.getJSONObject().getString("name");
                            timezone = response.getJSONObject().getString("timezone");
                            firstName = response.getJSONObject().getString("first_name");
                            lastName = response.getJSONObject().getString("last_name");
                            gender = response.getJSONObject().getString("gender");
                            isVerified = response.getJSONObject().getBoolean("verified");

                            JSONObject picture = response.getJSONObject().getJSONObject("picture");
                            JSONObject data = picture.getJSONObject("data");

                            //get the 50X50 profile pic they send us
                            String pictureURL = data.getString("url");

                            new ProfilePicAsync(pictureURL,1).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }
    private void getUserDetailsFromParse() {
        parseUser = ParseUser.getCurrentUser();
        //Fetch profile photo
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fisrtName",parseUser.getString("first_name"));
        editor.putString("lastName",parseUser.getString("last_name"));
        editor.putString("email",parseUser.getEmail());
        editor.apply();

        Toast.makeText(this, "Welcome back " + parseUser.getString("first_name"), Toast.LENGTH_SHORT).show();
        updateUI();
    }
    private void saveNewGoogleUser(final Bitmap bitmap){
        parseUser = new ParseUser();
        parseUser.setEmail(email);
        parseUser.setUsername(email);
        parseUser.put("first_name", firstName);
        parseUser.put("last_name", lastName);
        parseUser.put("emailVerified", isVerified);
        parseUser.put("isUser", true);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                updateProfilePic(bitmap);
            }
        });
        //Save the Profile Photo as well
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            byte[] data = bos.toByteArray();
            String thumbname = parseUser.getUsername().replaceAll("\\s+", "");
            final ParseFile profilePhoto = new ParseFile(thumbname + "_thumb.jpg", data);
            profilePhoto.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    parseUser.put("profile_pic", profilePhoto);
                    //Now we save the user details
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            updateUI();
                        }
                    });
                }
            });
        }
    }
    private void saveNewFacebookUser(Bitmap bitmap) {
        parseUser = ParseUser.getCurrentUser();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fisrtName",firstName);
        editor.putString("lastName",lastName);
        editor.putString("email",email);
        editor.apply();


        parseUser.setEmail(email);
        parseUser.setUsername(email);
        parseUser.put("first_name", firstName);
        parseUser.put("last_name", lastName);
        parseUser.put("gender", gender);
        parseUser.put("timezone", timezone);
        parseUser.put("emailVerified", isVerified);
        parseUser.put("isUser", true);

        //Save the Profile Photo as well
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            byte[] data = bos.toByteArray();
            String thumbname = parseUser.getUsername().replaceAll("\\s+", "");
            final ParseFile profilePhoto = new ParseFile(thumbname + "_thumb.jpg", data);
            profilePhoto.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    parseUser.put("profile_pic", profilePhoto);
                    //Now we save the user details
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            updateUI();
                        }
                    });
                }
            });
        }


    }
    private void updateUI(){
        Intent i = new Intent(this,MainHome.class);
        this.startActivity(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class ProfilePicAsync extends AsyncTask<String, String, String> {
        public Bitmap bitmap;
        String url;
        int authMode;

        public ProfilePicAsync(String url,int authMode) {
            this.url = url;
            this.authMode=authMode;
        }

        @Override
        protected String doInBackground(String... params) {
            //Fetching data from URI  and storing in Bitmap
            bitmap = DownloadImageBitmap(url);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (authMode==1) {
                saveNewFacebookUser(bitmap);
            }else if(authMode==2){
                saveNewGoogleUser(bitmap);
            }
        }
    }
    private void updateProfilePic(Bitmap bitmap){
        //Save the Profile Photo as well
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            byte[] data = bos.toByteArray();
            String thumbname = parseUser.getUsername().replaceAll("\\s+", "");
            final ParseFile profilePhoto = new ParseFile(thumbname + "_thumb.jpg", data);
            profilePhoto.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    parseUser.put("profile_pic", profilePhoto);
                    //Now we save the user details
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                        }
                    });
                }
            });
        }
    }
    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection connection = aURL.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Image Download", "Error getting Bitmap", e);
        }
        return bm;
    }

}
