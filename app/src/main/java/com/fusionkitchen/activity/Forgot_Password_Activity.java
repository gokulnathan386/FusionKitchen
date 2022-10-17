package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;


import com.fusionkitchen.app.MyApplication;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.loginsignup.forgot_password_model;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot_Password_Activity extends AppCompatActivity {



    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;


    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;

    /*---------------------------XML ID Call----------------------------------------------------*/
    AppCompatEditText user_email;
    AppCompatButton reset_password, login;
    LinearLayout get_email, get_login;
    String emailPattern;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_forgotpass);



        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);


        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Forgot_Password_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Forgot_Password_Activity.ViewDialog alert = new Forgot_Password_Activity.ViewDialog();
            alert.showDialog(Forgot_Password_Activity.this);

        }


        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        /*---------------------------XML ID Call----------------------------------------------------*/
        user_email = findViewById(R.id.user_email);
        reset_password = findViewById(R.id.reset_password);
        login = findViewById(R.id.login);
        get_email = findViewById(R.id.get_email);
        get_login = findViewById(R.id.get_login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validategmail();


            }
        });
    }


    /*---------------------------validate Login----------------------------------------------------*/
    /*Check Login Details Hear...!*/
    private void validategmail() {

        if (TextUtils.isEmpty(user_email.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(user_email.getWindowToken(), 0);
            user_email.setHint("Please fill out this field");
            user_email.setHintTextColor(getResources().getColor(R.color.forgot_error));

        } else if (!user_email.getText().toString().matches(emailPattern)) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(user_email.getWindowToken(), 0);
            user_email.getText().clear();
            user_email.setHint("Enter a valid email id");
            user_email.setHintTextColor(getResources().getColor(R.color.forgot_error));

        } else {

            forgotpass(user_email.getText().toString());
        }

    }



    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Forgot_Password_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_Password_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Forgot_Password_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }


                }
            });
            alertDialog.show();

        }
    }


    /*  ---------------------------get api URL first time get type values----------------------------------------------------*/
    private void forgotpass(String usergmail) {

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", usergmail);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<forgot_password_model> call = apiService.forgotpassword(params);

        call.enqueue(new Callback<forgot_password_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<forgot_password_model> call, Response<forgot_password_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        get_email.setVisibility(View.GONE);
                        get_login.setVisibility(View.VISIBLE);
                        Snackbar.make(Forgot_Password_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();

                    } else {
                        get_email.setVisibility(View.VISIBLE);
                        get_login.setVisibility(View.GONE);

                        Snackbar.make(Forgot_Password_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {

                    Snackbar.make(Forgot_Password_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<forgot_password_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Forgot_Password_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Forgot Password Activity");
    }


}
