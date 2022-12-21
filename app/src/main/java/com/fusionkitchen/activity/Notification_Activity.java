package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;

import com.fusionkitchen.app.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;

public class Notification_Activity extends AppCompatActivity {

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
    String user_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_notification);





        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);



        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Notification_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Notification_Activity.ViewDialog alert = new Notification_Activity.ViewDialog();
            alert.showDialog(Notification_Activity.this);

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
        dbHelper = new SQLDBHelper(Notification_Activity.this);
        getContactsCount();



        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));

        /*---------------------------XML ID Call----------------------------------------------------*/
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
                            Toast.makeText(Notification_Activity.this,"Your cart is Empty!",Toast.LENGTH_SHORT).show();
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


    }




    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Notification_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Notification_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Notification_Activity.this);
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
        MyApplication.getInstance().trackScreenView("Notification Activity");
    }
}
