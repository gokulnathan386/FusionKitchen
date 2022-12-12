package com.fusionkitchen.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.loginsignup.login_model;
import com.fusionkitchen.model.loginsignup.signup_model;
import com.fusionkitchen.model.social_signup_model;
import com.fusionkitchen.model.version_code_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Context mContext = Login_Activity.this;
    //postcode
    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;


    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String activity_details, cooking_insttructionback;
    Dialog dialog;


    /*-------------------view signin signup layout---------------*/
    View signinlayout, signuplayout, mContentView;
    AppCompatTextView signup_link;
    RelativeLayout signup, signup_welcome;
    LinearLayout signup_page1, signup_page2;
    AppCompatButton signup_next;
    int mCount = 0;
    AppCompatEditText mRgFirstName, mRgLastname, mRgPhonenumber, mRgUsermail, mRgPwd, mRgConfPwd;
    String fname, lname, phonenumber, usermail, pwd, cpwd;

    ImageView signin_back;


    /*--------------Login Button-----------------*/

    AppCompatButton sigin_button, signin_link;
    AppCompatEditText user_email, user_password;

    /*----------------------forgot_password-------------------------*/
    TextView forgot_password;


    /*--------------SSO LOGIN ENABLE-----------------*/
    TableLayout sso_login, sso_signup;


    /*---------------------------G-mail Login----------------------------------------------------*/
    private static final String TAG = Login_Activity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    ImageButton gmail_login_layout, gmail_signup_layout;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    /*---------------------------FaceBook Login----------------------------------------------------*/
    LoginButton loginButton;
    CallbackManager callbackManager;
    ImageButton facebook_login_layout, facebook_signup_layout;
    String fb_first_name, fb_last_name, fb_id, fb_email;
    AlertDialog.Builder builder;


    /*---------------------------------Sqlite database ------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_login);




        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);





        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Login_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Login_Activity.ViewDialog alert = new Login_Activity.ViewDialog();
            alert.showDialog(Login_Activity.this);
        }


        signinlayout = findViewById(R.id.signinlayout);
        signuplayout = findViewById(R.id.signuplayout);
        signup_link = findViewById(R.id.signup_link);


        signup = findViewById(R.id.signup);
        signup_welcome = findViewById(R.id.signup_welcome);
        signup_page1 = findViewById(R.id.signup_page1);
        signup_page2 = findViewById(R.id.signup_page2);
        signup_next = findViewById(R.id.signup_next);


        mRgFirstName = findViewById(R.id.mRgFirstName);
        mRgLastname = findViewById(R.id.mRgLastname);
        mRgPhonenumber = findViewById(R.id.mRgPhonenumber);
        mRgUsermail = findViewById(R.id.mRgUsermail);
        mRgPwd = findViewById(R.id.mRgPwd);
        mRgConfPwd = findViewById(R.id.mRgConfPwd);
        mContentView = findViewById(R.id.content_layout);
        signin_back = findViewById(R.id.signin_back);


        signin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();


            }
        });



        /*-------------------Sign-up next button color change--------------*/

        /*-------------------First namee--------------*/

        mRgFirstName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (mRgLastname.getText().toString().length() != 0 && mRgPhonenumber.getText().toString().length() != 0) {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                    } else {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                    }
                } else {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                }

            }
        });
        /*-------------------Lastname namee--------------*/
        mRgLastname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (mRgFirstName.getText().toString().length() != 0 && mRgPhonenumber.getText().toString().length() != 0) {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                    } else {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                    }
                } else {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                }

            }
        });

        /*-------------------Phone Number--------------*/
        mRgPhonenumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (mRgFirstName.getText().toString().length() != 0 && mRgLastname.getText().toString().length() != 0) {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                    } else {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                    }
                } else {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                }

            }
        });

        /*-------------------E-mail--------------*/
        mRgUsermail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (mRgPwd.getText().toString().length() != 0 && mRgConfPwd.getText().toString().length() != 0) {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                    } else {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                    }
                } else {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                }

            }
        });
        /*-------------------Password--------------*/
        mRgPwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (mRgUsermail.getText().toString().length() != 0 && mRgConfPwd.getText().toString().length() != 0) {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                    } else {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                    }
                } else {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                }

            }
        });

        /*-------------------Confirm password--------------*/
        mRgConfPwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    if (mRgUsermail.getText().toString().length() != 0 && mRgPwd.getText().toString().length() != 0) {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                    } else {
                        signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                    }
                } else {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                }

            }
        });




        /*----------------------forgot_password-------------------------*/
        forgot_password = findViewById(R.id.forgot_password);


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Forgot_Password_Activity.class));
            }
        });


        signup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signinlayout.setVisibility(View.GONE);
                signuplayout.setVisibility(View.VISIBLE);
                mCount = 1;

                signup.setVisibility(View.VISIBLE);
                signup_welcome.setVisibility(View.GONE);
                signup_page1.setVisibility(View.VISIBLE);

                mRgFirstName.getText().clear();
                mRgLastname.getText().clear();
                mRgPhonenumber.getText().clear();
                mRgUsermail.getText().clear();
                mRgPwd.getText().clear();
                mRgConfPwd.getText().clear();

                ssoenabledisabaleregister();

            }
        });

        signup_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fname = mRgFirstName.getText().toString().trim();
                lname = mRgLastname.getText().toString().trim();
                phonenumber = mRgPhonenumber.getText().toString();
                usermail = mRgUsermail.getText().toString();
                pwd = mRgPwd.getText().toString();
                cpwd = mRgConfPwd.getText().toString();

                if (mCount == 1) {

                    if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(phonenumber)) {


                        if (phonenumber.length() == 11) {
                            signup_page1.setVisibility(View.GONE);
                            signup_page2.setVisibility(View.VISIBLE);
                            mCount++;

                            if (mRgUsermail.getText().toString().length() != 0 && mRgPwd.getText().toString().length() != 0 && mRgConfPwd.getText().toString().length() != 0) {
                                signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                            } else {
                                signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                            }
                        } else {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgPhonenumber.getWindowToken(), 0);
                            Utility.Showsnackbar(mContentView, "Please Enter valid Phone Number");
                        }


                    } else {

                        if (TextUtils.isEmpty(fname)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgFirstName.getWindowToken(), 0);
                            mRgFirstName.setError("Please fill out this field.");
                            Utility.Showsnackbar(mContentView, "Enter the First Name");
                        } else if (TextUtils.isEmpty(lname)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgLastname.getWindowToken(), 0);
                            mRgLastname.setError("Please fill out this field.");
                            Utility.Showsnackbar(mContentView, "Enter the Last Namer");
                        } else {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgPhonenumber.getWindowToken(), 0);
                            mRgPhonenumber.setError("Please fill out this field.");
                            Utility.Showsnackbar(mContentView, "Enter the Phone number");
                        }


                    }


                } else if (mCount == 2) {

                    if (!TextUtils.isEmpty(usermail) && !TextUtils.isEmpty(pwd)
                            && !TextUtils.isEmpty(cpwd)) {
                        if (!Utility.isVaildEmail(usermail)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgUsermail.getWindowToken(), 0);
                            mRgUsermail.setError("Please fill out this field.");
                            Utility.Showsnackbar(mContentView, "Please Enter valid Email Address");
                        } else if (pwd.equals(cpwd)) {

                            SignupAction(fname, lname, phonenumber, usermail, pwd, cpwd);
                            /*mCount++;
                            signup.setVisibility(View.GONE);
                            signup_welcome.setVisibility(View.VISIBLE);
*/
                            //SignupAction(fname, lname, phonenumber, usermail, pwd, cpwd);
                            //API call
                               /* User user = new User();
                                user.email = usermail;
                                user.name = fname;
                                user.lname = lname;
                                user.password = pwd;
                                user.phonenumber = phonenumber;
                                insertuser(user);*/


                        } else {
                            Utility.Showsnackbar(mContentView, "Confirm Password should be same.");
                        }

                    } else {
                        if (TextUtils.isEmpty(usermail)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgUsermail.getWindowToken(), 0);
                            mRgUsermail.setError("Please fill out this field.");
                            Utility.Showsnackbar(mContentView, "Enter the Email address");
                        } else if (TextUtils.isEmpty(pwd)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgPwd.getWindowToken(), 0);
                            mRgPwd.setError("Please fill out this field.");
                            Utility.Showsnackbar(mContentView, "Enter the Password");
                        } else {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mRgConfPwd.getWindowToken(), 0);
                            mRgConfPwd.setError("Please fill out this field.");
                            Utility.Showsnackbar(mContentView, "Enter the Confirm Passwordr");
                        }


                    }
                }
            }
        });


        /*--------------SSO LOGIN ENABLE-----------------*/

        sso_login = findViewById(R.id.sso_login);
        sso_signup = findViewById(R.id.sso_signup);
        ssoenabledisabalelogin();


        /*--------------Login Button-----------------*/

        sigin_button = findViewById(R.id.sigin_button);
        signin_link = findViewById(R.id.signin_link);

        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);


        sigin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });


        signin_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signinlayout.setVisibility(View.VISIBLE);
                signuplayout.setVisibility(View.GONE);
                mCount = 0;
            }
        });


        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
        Intent intent = getIntent();
        activity_details = intent.getStringExtra("activity_details");

        if (intent.getStringExtra("cooking_insttruction") == null) {
            cooking_insttructionback = "";
        } else {
            cooking_insttructionback = intent.getStringExtra("cooking_insttruction");

        }


        Log.e("newactivity_details", "" + activity_details);




        /*---------------------------G-mail Login----------------------------------------------------*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(Login_Activity.this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        gmail_login_layout = findViewById(R.id.gmail_login_layout);
        gmail_signup_layout = findViewById(R.id.gmail_signup_layout);

        gmail_login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });

        gmail_signup_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });



        /*---------------------------FaceBook Login----------------------------------------------------*/
        facebook_login_layout = findViewById(R.id.facebook_login_layout);
        facebook_signup_layout = findViewById(R.id.facebook_signup_layout);
        loginButton = findViewById(R.id.login_button);

        facebook_login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.performClick();

            }
        });

        facebook_signup_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.performClick();

            }
        });

        builder = new AlertDialog.Builder(this);
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
            Picasso.get().load(String.valueOf(Profile.getCurrentProfile()));
            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());

            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken());
        }


        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    // txtUsername.setText(null);
                    // txtEmail.setText(null);
                    //imageView.setImageResource(0);
                    //   Toast.makeText(getApplicationContext(), "User Logged Out.", Toast.LENGTH_LONG).show();
                }
            }
        };
        fbTracker.startTracking();

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

                if (!loggedOut) {
                    // Picasso.with(MainActivity.this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
                    // Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());

                    //Using Graph API
                    getUserProfile(AccessToken.getCurrentAccessToken());


                    Log.e("TAG", "Username is: " + AccessToken.getCurrentAccessToken().toString());
                }

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

            }
        });
    }

    /*--------------SSO LOGIN ENABLE-----------------*/
    private void ssoenabledisabalelogin() {

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<version_code_modal> call = apiService.getversion();
        call.enqueue(new Callback<version_code_modal>() {
            @Override
            public void onResponse(Call<version_code_modal> call, Response<version_code_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        if (response.body().getVersion().get(0).getSociallogin().equalsIgnoreCase("enable")) {
                            sso_login.setVisibility(View.VISIBLE);
                        } else {
                            sso_login.setVisibility(View.GONE);
                        }


                    }
                } else {

                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<version_code_modal> call, Throwable t) {

                Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }

    private void ssoenabledisabaleregister() {

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<version_code_modal> call = apiService.getversion();
        call.enqueue(new Callback<version_code_modal>() {
            @Override
            public void onResponse(Call<version_code_modal> call, Response<version_code_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        if (response.body().getVersion().get(0).getSociallogin().equalsIgnoreCase("enable")) {
                            sso_signup.setVisibility(View.VISIBLE);
                        } else {
                            sso_signup.setVisibility(View.GONE);
                        }


                    }
                } else {

                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<version_code_modal> call, Throwable t) {

                Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }

    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Login_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Login_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }


                }
            });
            alertDialog.show();

        }
    }


    /*-----------------------LoginAction---------------------*/


    /*---------------------------Login Button----------------------------------------------------*/
    /*Check Login Details Hear...!*/
    private void validateLogin() {
        if (TextUtils.isEmpty(user_email.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(user_email.getWindowToken(), 0);
            user_email.setError("Please fill out this field.");
            Utility.Showsnackbar(mContentView, "Enter the E-mail address");
            // Snackbar.make(this.findViewById(android.R.id.content), "Please enter your mobile number", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(user_password.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(user_password.getWindowToken(), 0);
            Utility.Showsnackbar(mContentView, "Enter the Password");
        } else if (!Utility.isVaildEmail(user_email.getText().toString())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(user_email.getWindowToken(), 0);
            user_email.setError("Invalid email address");
            Utility.Showsnackbar(mContentView, "Invalid email address");
        } else {
            LoginAction(user_email.getText().toString(), user_password.getText().toString());
        }

    }


    /*---------------------------Login Button----------------------------------------------------*/
    private void LoginAction(String useremail, String userpass) {

        loadingshow();

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", useremail);
        params.put("password", userpass);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<login_model> call = apiService.logininterfas(params);
        Log.e("loginparams", "" + params);
        call.enqueue(new Callback<login_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<login_model> call, Response<login_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();

                Log.e("loginstatuscode", "" + statusCode);
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        hideloading();
                        /*--------------Login store SharedPreferences------------------*/
                        if (slogin == null)
                            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
                        sloginEditor = slogin.edit();
                        sloginEditor.putString("login_key_status", "true");
                        sloginEditor.putString("login_key_cid", response.body().getUserdetails().getCid());
                        sloginEditor.putString("login_key_vcode", response.body().getUserdetails().getVcode());
                        sloginEditor.putString("type_of_login", response.body().getUserdetails().getType_of_login());
                        sloginEditor.putString("login_key_email", response.body().getUserdetails().getEmail());
                        sloginEditor.putString("login_key_fname",response.body().getUserdetails().getFname());
                        sloginEditor.putString("login_key_phone",response.body().getUserdetails().getPhone());
                        sloginEditor.commit();


                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();


                        //Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                        if (activity_details.equalsIgnoreCase("pcode")) {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("cart")) {
                            Intent intent = new Intent(Login_Activity.this, Add_to_Cart.class);
                            intent.putExtra("cooking_insttruction", cooking_insttructionback);
                            startActivity(intent);
                        } else if (activity_details.equalsIgnoreCase("address")) {
                            startActivity(new Intent(getApplicationContext(), Address_Book_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("myaccount")) {
                            startActivity(new Intent(getApplicationContext(), MyAccount_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("postcode")) {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("myfavourite")) {
                            startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
                        } else if(activity_details.equalsIgnoreCase("item_menu_activity")){

                            Intent intent = getIntent();
                            String menuurlpath = intent.getStringExtra("menuurlpath");

                            Intent item_menu_page = new Intent(getApplicationContext(), Item_Menu_Activity.class);
                            item_menu_page.putExtra("menuurlpath",menuurlpath);
                            startActivity(item_menu_page);


                        }else {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        }


                    } else {
                        hideloading();
                        Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<login_model> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*-----------------------Sign Up Action---------------------*/
    private void SignupAction(String userfirstname, String userlastname, String usermobile, String useremail, String userpass, String userconpass) {
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", userfirstname);
        params.put("last_name", userlastname);
        params.put("mobile", usermobile);
        params.put("reg_email", useremail);
        params.put("reg_password", userpass);
        params.put("conf_password", userconpass);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<signup_model> call = apiService.signupinterfas(params);

        call.enqueue(new Callback<signup_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<signup_model> call, Response<signup_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("1")) {


                        mCount++;
                        signup.setVisibility(View.GONE);
                        signup_welcome.setVisibility(View.VISIBLE);


                        Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<signup_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }







    /*---------------------------G-mail Login----------------------------------------------------*/

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {


            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());


            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();



            GmailSignupAction(personGivenName, personFamilyName, personId, personEmail, "1");


            Log.e(TAG, "display name: " + personName + "\n" + "GivenName: " + personGivenName + "\n" + "FamilyName: " + personFamilyName + "\n" + "Email: " + personEmail + "\n" + "ID: " + personId);
            //txtName.setText(personName);
            // txtEmail.setText(personFamilyName);


            // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //  updateUI(false);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        //
/*        if (requestCode == PHONE_LOGIN_CODE || requestCode == EMAIL_LOGIN_CODE){
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null){
                alertServices.showToast(result.getError().getErrorType().getMessage());
            }else if (result.wasCancelled()){
                alertServices.showToast("Login Cancelled");
            }else if (result.getAccessToken() != null){
                FacebookAccountKitService.shared.getCurrentAccountHolder(new FacebookAccountKitService.GetCurrentAccountListener() {
                    @Override
                    public void onSuccess(String accountHolder) {
                        alertServices.showAlert(CustomerLoginActivity.this, "Verified Number/Email", accountHolder);

                            Here Send this account Holder to Server. If User exists, get profile. If user not exists add the user...

                    }

                    @Override
                    public void onError(String errorMsg) {
                        alertServices.showToast(errorMsg);
                    }
                });
            }
        }*/

        //


    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //  hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", "onConnectionFailed:" + connectionResult);
    }


    /*-----------------------Gmail Sign Up Action---------------------*/

    private void GmailSignupAction(String userfirstname, String userlastname, String userid, String useremail, String typelogin) {
        loadingshow();
        Map<String, String> params = new HashMap<String, String>();


        if (userlastname == null) {
            userlastname = ".";
        }
        params.put("fname", userfirstname);
        params.put("lname", userlastname);
        params.put("id", userid);
        params.put("email", useremail);
        params.put("type_of_login", typelogin);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<social_signup_model> call = apiService.gmailsignupinterfas(params);


        Log.e("GmailSignupActionpares", "" + params);


        call.enqueue(new Callback<social_signup_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<social_signup_model> call, Response<social_signup_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("1")) {

                        /*--------------Login store SharedPreferences------------------*/
                        if (slogin == null)
                            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
                        sloginEditor = slogin.edit();
                        sloginEditor.putString("login_key_status", "true");
                        sloginEditor.putString("login_key_cid", response.body().getUserdetails().getCid());
                        sloginEditor.putString("login_key_vcode", response.body().getUserdetails().getVcode());
                        sloginEditor.putString("type_of_login", response.body().getUserdetails().getType_of_login());
                        sloginEditor.putString("login_key_email", response.body().getUserdetails().getEmail());
                        sloginEditor.putString("login_key_fname",response.body().getUserdetails().getfname());
                        sloginEditor.putString("login_key_phone",response.body().getUserdetails().getphone());
                        sloginEditor.commit();

                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                        signOut();
                        if (activity_details.equalsIgnoreCase("pcode")) {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("cart")) {
                            Intent intent = new Intent(Login_Activity.this, Add_to_Cart.class);
                            intent.putExtra("cooking_insttruction", cooking_insttructionback);
                            startActivity(intent);
                        } else if (activity_details.equalsIgnoreCase("address")) {
                            startActivity(new Intent(getApplicationContext(), Address_Book_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("myaccount")) {
                            startActivity(new Intent(getApplicationContext(), MyAccount_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("postcode")) {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("myfavourite")) {
                            startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        }
                    } else {
                        hideloading();
                        Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    hideloading();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<social_signup_model> call, Throwable t) {
                hideloading();
                Log.e("gmailerror", "" + t.toString());
                Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }
        });
    }

    /*---------------------------G-mail Logout----------------------------------------------------*/
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // updateUI(false);
                    }
                });
    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Login_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.custom_loading_layout);


        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(Login_Activity.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideloading() {
        dialog.dismiss();
    }


    /*---------------------------FaceBook Login----------------------------------------------------*/
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("object", object.toString());

                        try {
                            fb_first_name = object.getString("first_name");
                            fb_last_name = object.getString("last_name");

                            if(object.has("email")){
                                fb_email = object.getString("email");
                            }else{
                                fb_email ="";
                            }
                           // fb_email = object.getString("email");
                            fb_id = object.getString("id");




                            FacebookSignupAction(fb_first_name, fb_last_name, fb_id, fb_email, "2");


                            Log.e("Fcaebookdetails1", "" + "First Name: " + fb_first_name + "\n" + "\nLast Name: " + fb_last_name + "\n" + fb_email + "\n" + "ID" + fb_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Fcaebookdetails2", "" + e);

                            LoginManager.getInstance().logOut();

                            builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                            //Setting message manually and performing action on button click

                            builder.setMessage("Please Login Your Account By Other Options.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("LOGIN COULD NOT PROCESS! ");
                            alert.show();


                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void FacebookSignupAction(String userfirstname, String userlastname, String userid, String useremail, String typelogin) {
        loadingshow();
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();


        params.put("fname", userfirstname);
        params.put("lname", userlastname);
        params.put("id", userid);
        params.put("email", useremail);
        params.put("type_of_login", typelogin);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<social_signup_model> call = apiService.gmailsignupinterfas(params);

        call.enqueue(new Callback<social_signup_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<social_signup_model> call, Response<social_signup_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        /*--------------Login store SharedPreferences------------------*/
                        if (slogin == null)
                            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
                        sloginEditor = slogin.edit();
                        sloginEditor.putString("login_key_status", "true");
                        sloginEditor.putString("login_key_cid", response.body().getUserdetails().getCid());
                        sloginEditor.putString("login_key_vcode", response.body().getUserdetails().getVcode());
                        sloginEditor.putString("type_of_login", response.body().getUserdetails().getType_of_login());
                        sloginEditor.putString("login_key_email", response.body().getUserdetails().getEmail());
                        sloginEditor.putString("login_key_fname",response.body().getUserdetails().getfname());
                        sloginEditor.putString("login_key_phone",response.body().getUserdetails().getphone());
                        sloginEditor.commit();
                        LoginManager.getInstance().logOut();
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                        if (activity_details.equalsIgnoreCase("pcode")) {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("cart")) {
                            Intent intent = new Intent(Login_Activity.this, Add_to_Cart.class);
                            intent.putExtra("cooking_insttruction", cooking_insttructionback);
                            startActivity(intent);
                        } else if (activity_details.equalsIgnoreCase("address")) {
                            startActivity(new Intent(getApplicationContext(), Address_Book_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("myaccount")) {
                            startActivity(new Intent(getApplicationContext(), MyAccount_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("postcode")) {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        } else if (activity_details.equalsIgnoreCase("myfavourite")) {
                            startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                        }
                    } else {
                        hideloading();
                        Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    hideloading();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<social_signup_model> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void ShowHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {
            if (mRgPwd.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.invisible);
                //Show Password
                mRgPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.visibility);
                //Hide Password
                mRgPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } else if (view.getId() == R.id.show_conpass_btn) {
            if (mRgConfPwd.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.invisible);
                //Show Password
                mRgConfPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.visibility);
                //Hide Password
                mRgConfPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }


    /*------------------On resume--------------*/

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Login Activity");
    }


    @Override
    public void onBackPressed() {

        Log.e("mCount", "" + mCount);
        if (signuplayout.getVisibility() == View.VISIBLE) {
            if (mCount == 1) {
                signinlayout.setVisibility(View.VISIBLE);
                signuplayout.setVisibility(View.GONE);
                mCount = 0;
            } else if (mCount == 2) {
                mCount = 1;
                signup_page1.setVisibility(View.VISIBLE);
                signup_page2.setVisibility(View.GONE);


                if (mRgFirstName.getText().toString().length() != 0 && mRgLastname.getText().toString().length() != 0 && mRgPhonenumber.getText().toString().length() != 0) {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextok));
                } else {
                    signup_next.setBackgroundTintList(ContextCompat.getColorStateList(Login_Activity.this, R.color.btn_nextnotok));
                }

            } else if (mCount == 3) {
                signinlayout.setVisibility(View.VISIBLE);
                signuplayout.setVisibility(View.GONE);
                mCount = 0;
            }
        } else {
            finish();
        }
    }
}