package com.fusionkitchen.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
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

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.fusionkitchen.adapter.Slider_adapter;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.Login.Login_mobile_email;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.fusionkitchen.model.loginsignup.login_model;
import com.fusionkitchen.model.loginsignup.signup_model;
import com.fusionkitchen.model.post_code_modal;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    TextView otp_timer,decs_txt;
    TextView desc_otp;
    Boolean resend = false;


    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String activity_details, cooking_insttructionback;
    Dialog dialog;
    LoginButton loginButton;
    LinearLayout four_otp,desc_resend_otp;



    /*-------------------view signin signup layout---------------*/
    RelativeLayout signup, signup_welcome;
    AppCompatButton otp_btn;
    int mCount = 0;



    ImageView signin_back;
    AppCompatButton sigin_button;


    /*--------------Login Button-----------------*/

    AppCompatEditText user_email, user_password;

    /*----------------------forgot_password-------------------------*/



    /*--------------SSO LOGIN ENABLE-----------------*/
    TableLayout sso_signup;


    /*---------------------------G-mail Login----------------------------------------------------*/
    private static final String TAG = Login_Activity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    ImageButton gmail_login_layout;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    /*---------------------------FaceBook Login----------------------------------------------------*/
    CallbackManager callbackManager;
    ImageButton facebook_login_layout;
    String fb_first_name, fb_last_name, fb_id, fb_email;
    AlertDialog.Builder builder;
    TextView text_terms_condition,resend_txt;
    EditText email_phone_edittxt,otp1,otp2,otp3,otp4;
    LinearLayout sso_login;


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

        text_terms_condition = findViewById(R.id.text_terms_condition);



        otp_timer = findViewById(R.id.otp_timer);



        sso_login = findViewById(R.id.sso_login);
        signin_back = findViewById(R.id.signin_back);

        sigin_button = findViewById(R.id.sigin_button);
        email_phone_edittxt  = findViewById(R.id.email_phone_edittxt);
        decs_txt = findViewById(R.id.decs_txt);
        four_otp = findViewById(R.id.four_otp);
        desc_otp = findViewById(R.id.desc_otp);
        desc_resend_otp = findViewById(R.id.desc_resend_otp);
        otp_btn = findViewById(R.id.otp_btn);
        resend_txt = findViewById(R.id.resend_txt);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otp1.getText().toString().trim().isEmpty() && otp2.getText().toString().trim().isEmpty()
                && otp3.getText().toString().trim().isEmpty() && otp4.getText().toString().trim().isEmpty()
                ){

                    otp1.requestFocus();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), "Please fill out this field.", Snackbar.LENGTH_LONG).show();
                    otp1.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    otp2.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    otp3.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    otp4.setBackground(getResources().getDrawable(R.drawable.otp_bg));

                }else if(otp1.getText().toString().trim().isEmpty()){
                    otp1.requestFocus();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), "Please fill out this field.", Snackbar.LENGTH_LONG).show();
                    otp1.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                }else if(otp2.getText().toString().trim().isEmpty()){
                    otp2.requestFocus();
                    otp2.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), "Please fill out this field.", Snackbar.LENGTH_LONG).show();
                }else if(otp3.getText().toString().trim().isEmpty()){
                    otp3.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    otp3.requestFocus();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), "Please fill out this field.", Snackbar.LENGTH_LONG).show();
                }else if(otp4.getText().toString().trim().isEmpty()){
                    otp4.requestFocus();
                    otp4.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), "Please fill out this field.", Snackbar.LENGTH_LONG).show();
                }else{

                    LoginAction(otp1.getText().toString().trim() + otp2.getText().toString().trim() +
                            otp3.getText().toString().trim() + otp4.getText().toString().trim() , email_phone_edittxt.getText().toString().trim());

                }

            }
        });


        signin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();


            }
        });


        text_terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this,Terms_Conditions_Activity.class);
                startActivity(intent);
            }
        });


        otp1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {

               if(s.length() == 1){
                   otp2.requestFocus();
               }

            }
        });


        otp2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(s.length() == 1){
                    otp1.requestFocus();
                }

            }

            public void afterTextChanged(Editable s) {

                if(s.length() == 1){
                    otp3.requestFocus();
                }

            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(s.length() == 1){
                    otp2.requestFocus();
                }

            }

            public void afterTextChanged(Editable s) {

                if(s.length() == 1){
                    otp4.requestFocus();
                }

            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(s.length() == 1){
                    otp3.requestFocus();
                }

            }

            public void afterTextChanged(Editable s) {

            }
        });

        email_phone_edittxt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(s.length() == 1){
                    Drawable img = email_phone_edittxt.getContext().getResources().getDrawable( R.drawable.ic_gmail_phone_icon );
                    email_phone_edittxt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    sigin_button.setBackground(getResources().getDrawable(R.drawable.gmail_phone_bg));
                    sigin_button.setTextColor(Color.parseColor("#909497"));

                }

            }

            public void afterTextChanged(Editable s) {

                if(s.length() > 0){
                    Drawable img = email_phone_edittxt.getContext().getResources().getDrawable( R.drawable.login_icon_green );
                    email_phone_edittxt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    sigin_button.setBackground(getResources().getDrawable(R.drawable.gmail_phone_bg1));
                    sigin_button.setTextColor(Color.parseColor("#FFFFFF"));
                }

            }
        });



        /*--------------SSO LOGIN ENABLE-----------------*/

        ssoenabledisabalelogin();


        /*--------------Login Button-----------------*/




        user_email = findViewById(R.id.user_email);


        sigin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
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

        gmail_login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });


        /*---------------------------FaceBook Login----------------------------------------------------*/
        facebook_login_layout = findViewById(R.id.facebook_login_layout);
        loginButton = findViewById(R.id.login_button);


        facebook_login_layout.setOnClickListener(new View.OnClickListener() {
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


        resend_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend = true;
                Sendotpphone(email_phone_edittxt.getText().toString().trim());
            }
        });


    }

    private void Checkotpvalidation(String txtotp,String txtgmail) {

        loadingshow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("otp_login", txtotp);
        params.put("user_name", txtgmail);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<Login_mobile_email> call = apiService.sendotpemailphone(params);
        call.enqueue(new Callback<Login_mobile_email>() {
            @Override
            public void onResponse(Call<Login_mobile_email> call, Response<Login_mobile_email> response) {
                int statusCode = response.code();

                Log.e("Login_Activity", new Gson().toJson(response.body()));

                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        if(resend == true){
                            Resendtimecount();
                            resend = false;
                        }else{

                            decs_txt.setVisibility(View.GONE);
                            email_phone_edittxt.setVisibility(View.GONE);
                            sigin_button.setVisibility(View.GONE);

                            four_otp.setVisibility(View.VISIBLE);
                            desc_otp.setVisibility(View.VISIBLE);
                            otp_timer.setVisibility(View.VISIBLE);
                            otp_btn.setVisibility(View.VISIBLE);

                            Resendtimecount();

                        }


                        // Snackbar.make(Login_Activity.this.findViewById(android.R.id.content),response.message(), Snackbar.LENGTH_LONG).show();


                    }

                } else {
                    hideloading();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Login_mobile_email> call, Throwable t) {

                Log.e("Tro", "" + t);
                hideloading();
                Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
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
                Log.d("responsemsg==", "ok" + statusCode + "SSO_LOGIN_ENABLE_DISABLE"+new Gson().toJson(response.body()));
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

        if(TextUtils.isEmpty(email_phone_edittxt.getText())){
            Snackbar.make(this.findViewById(android.R.id.content), "Please fill out this field.", Snackbar.LENGTH_LONG).show();
        }else{
            Sendotpphone(email_phone_edittxt.getText().toString().trim());
        }

    }

    private void Sendotpphone(String emailphone){

            loadingshow();
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_name", emailphone);
            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            Call<Login_mobile_email> call = apiService.sendotpemailphone(params);
            call.enqueue(new Callback<Login_mobile_email>() {
                @Override
                public void onResponse(Call<Login_mobile_email> call, Response<Login_mobile_email> response) {
                    int statusCode = response.code();

                    Log.e("Login_Activity", new Gson().toJson(response.body()));

                    if (statusCode == 200) {
                        hideloading();
                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            if(resend == true){
                                Resendtimecount();
                                resend = false;
                            }else{

                                decs_txt.setVisibility(View.GONE);
                                email_phone_edittxt.setVisibility(View.GONE);
                                sigin_button.setVisibility(View.GONE);

                                four_otp.setVisibility(View.VISIBLE);
                                desc_otp.setVisibility(View.VISIBLE);
                                otp_timer.setVisibility(View.VISIBLE);
                                otp_btn.setVisibility(View.VISIBLE);

                                Resendtimecount();

                            }


                           // Snackbar.make(Login_Activity.this.findViewById(android.R.id.content),response.message(), Snackbar.LENGTH_LONG).show();


                        }

                    } else {
                        hideloading();
                        Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<Login_mobile_email> call, Throwable t) {

                    Log.e("Tro", "" + t);
                    hideloading();
                    Snackbar.make(Login_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }

            });


    }

    private void Resendtimecount() {


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                long count = millisUntilFinished / 1000;

                String count_timer = String.valueOf(count);

                if(count_timer.length() == 2){
                    otp_timer.setText("00:"+millisUntilFinished / 1000 );
                }else{
                    otp_timer.setText("00:0"+millisUntilFinished / 1000 );
                }

            }

            public void onFinish() {
                otp_timer.setText("00:00");
                desc_resend_otp.setVisibility(View.VISIBLE);
            }

        }.start();


    }


    /*---------------------------Login Button----------------------------------------------------*/
    private void LoginAction(String txtotp, String txtgmail) {

       // Checkotpvalidation();

        Log.d("bjbjhdbvsbjbd"," " + txtotp + txtgmail);
        loadingshow();
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("otp_login", txtotp);
        params.put("user_name", txtgmail);


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

                Log.e("loginstatuscode", new Gson().toJson(response.body()));
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        hideloading();
                        /*--------------Login store SharedPreferences------------------*/
                        if (slogin == null)
                            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);

                        Log.d("mvjgdusjssgcugcscb"," " +response.body().getUserdetails().getCid() );
                        sloginEditor = slogin.edit();
                        sloginEditor.putString("login_key_status", "true");
                     //   sloginEditor.putString("login_key_cid", response.body().getUserdetails().getCid());
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


    /*------------------On resume--------------*/

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Login Activity");
    }


  /*    @Override
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

        Toast.makeText(Login_Activity.this,"Gokulnathan",Toast.LENGTH_SHORT).show();

    }

*/

}