package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;

import com.fusionkitchen.app.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.loginsignup.forgot_password_model;
import com.fusionkitchen.model.orderstatus.submitfeedback_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feedback_Activity extends AppCompatActivity {

    private Context mContext = Feedback_Activity.this;



    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;


    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;
    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id,gmail,phone;

    String feedback_type, orderid, clientid;
    EditText comments;
    AppCompatButton feedback_submit;
    String Fname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_feedback);



        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);



        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Feedback_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Feedback_Activity.ViewDialog alert = new Feedback_Activity.ViewDialog();
            alert.showDialog(Feedback_Activity.this);

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

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Feedback_Activity.this);
        getContactsCount();

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));

        /*---------------------------Fresh Chat----------------------------------------------------*/

        FreshchatConfig config = new FreshchatConfig("67d078d8-604f-44c7-8807-c8a96810af62", "0604e381-8106-48e8-95bf-bc74bc8893fe");
        config.setDomain("msdk.in.freshchat.com");
        config.setCameraCaptureEnabled(true);
        config.setGallerySelectionEnabled(true);
        config.setResponseExpectationEnabled(true);
        Freshchat.getInstance(getApplicationContext()).init(config);
        /*---------------------------BottomNavigationView----------------------------------------------------*/

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().setGroupCheckable(0, false, true);
        //  bottomNav.getOrCreateBadge(R.id.home_card).setNumber(5);

        if (cursor != 0) {
            bottomNav.getOrCreateBadge(R.id.home_card).setNumber(cursor);
        }
        bottomNav.getMenu().findItem(R.id.home_search).setVisible(false);
        bottomNav.getMenu().findItem(R.id.home_chat).setVisible(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_bottom:
                        // finish();
                        //startActivity(getIntent());
                        Intent intenthome = new Intent(getApplicationContext(), Postcode_Activity.class);
                        startActivity(intenthome);
                        break;
                    case R.id.home_chat:
                        Freshchat.showConversations(getApplicationContext());
                        break;
                    case R.id.home_card:
                        /*Intent intentreview = new Intent(getApplicationContext(), Review.class);
                        startActivity(intentreview);*/
                        // Toast.makeText(getApplicationContext(), "Card", Toast.LENGTH_SHORT).show();

                        if (cursor != 0) {
                            Intent intentcard = new Intent(getApplicationContext(), Add_to_Cart.class);
                            startActivity(intentcard);
                        }else{
                            Toast.makeText(Feedback_Activity.this,"Your cart is Empty!",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.home_account:
                        if (user_id != null && !user_id.isEmpty()) {
                            Intent intentcard = new Intent(getApplicationContext(), MyAccount_Activity.class);
                            startActivity(intentcard);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            intent.putExtra("activity_details", "myaccount");
                            startActivity(intent);
                        }
                        break;

                }
                return true;
            }
        });

        /*---------------------Get Intent value----------------------------*/

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        clientid = intent.getStringExtra("clientid");
        Fname = intent.getStringExtra("Fname");
        gmail = intent.getStringExtra("gmail");
        phone = intent.getStringExtra("phone");




        comments = findViewById(R.id.comments);
        feedback_submit = findViewById(R.id.feedback_submit);


        feedback_type = "Appreciation";
        feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comments.getText().toString().equalsIgnoreCase("")) {
                    Snackbar.make(Feedback_Activity.this.findViewById(android.R.id.content), "Please fill out comments field.", Snackbar.LENGTH_LONG).show();
                } else {
                    // Log.e("errorddd ", "" + feedback_type + "   " + comments.getText().toString());

                    submitfeedback(user_id, orderid, clientid, feedback_type, comments.getText().toString(),Fname,gmail,phone); // Feedback API calling Method
                   /* Toast.makeText(getApplicationContext(), "Thank you for the feedback", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Feedback_Activity.this,Order_Status_List_Activity.class);
                    startActivity(intent);
                    finish();*/
                }
            }
        });
        startService(new Intent(getBaseContext(),MyService.class));
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_ppreciation:
                if (checked) {

                    feedback_type = "Appreciation";

                    break;
                }
            case R.id.radio_oncern:
                if (checked) {
                    feedback_type = "Concern";

                    break;
                }
            case R.id.radio_suggestion:
                if (checked) {
                    feedback_type = "Suggestion";

                    break;
                }

            case R.id.radio_query:
                if (checked) {
                    feedback_type = "Query";

                    break;
                }
        }
    }


    /*  --------------------------Submit feedback----------------------------------------------------*/
    private void submitfeedback(String struserid, String strorderid, String strclientid, String strtype, String strcomment,String fname,String gmail,String phone) {

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", struserid);
        params.put("order_id", strorderid);
        params.put("client_id", strclientid);
        params.put("feedback", strtype);
        params.put("comments", strcomment);
        params.put("name",fname);
        params.put("email",gmail);
        params.put("phone",phone);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<submitfeedback_model> call = apiService.submitfeedback(params);

        Log.e("feedbackparametes", "" + params);

        call.enqueue(new Callback<submitfeedback_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<submitfeedback_model> call, Response<submitfeedback_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Thank you for the feedback", Toast.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(Feedback_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(Feedback_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<submitfeedback_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Feedback_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }
        });
    }





    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Feedback_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Feedback_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Feedback_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }


                }
            });
            alertDialog.show();

        }
    }
    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    public int getContactsCount() {
        cursor = dbHelper.numberOfRows();
        Log.e("tmpStr10", "" + cursor);
        return cursor;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Feedback Activity");
    }

}
