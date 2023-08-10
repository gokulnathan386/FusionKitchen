package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.fusionkitchen.adapter.DashboardSearchResultList;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.home_model.location_type_modal;
import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favourite_Activity extends AppCompatActivity {

    private Context mContext = Favourite_Activity.this;
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

    /*---------------------------XML ID Call----------------------------------------------------*/
    AppCompatButton browse_more;
    LinearLayout no_favourtie, list_layout, clear_list_layout;
    RecyclerView myfavouritelist;
    AppCompatButton go_back, remove_cart;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, menuurlpath;

    /*--------------------------Login postcode save local------------------------*/
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    String key_postcode, key_lat, key_lon, key_area;
    Dialog dialog;

    /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    Dialog warningdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_favourite);


        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);



        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Favourite_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Favourite_Activity.ViewDialog alert = new Favourite_Activity.ViewDialog();
            alert.showDialog(Favourite_Activity.this);

        }


        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Favourite_Activity.this, Dashboard_Activity.class);
                startActivity(intent);


            }
        });

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Favourite_Activity.this);
        getContactsCount();


        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));


        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Favourite_Activity.this.MODE_PRIVATE);



        /*---------------------------XML ID Call----------------------------------------------------*/
        browse_more = findViewById(R.id.browse_more);

        no_favourtie = findViewById(R.id.no_favourtie);
        list_layout = findViewById(R.id.list_layout);
        myfavouritelist = findViewById(R.id.myfavouritelist);
        clear_list_layout = findViewById(R.id.clear_list_layout);


        go_back = findViewById(R.id.go_back);
        remove_cart = findViewById(R.id.remove_cart);

        browse_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentthirnolist = new Intent(Favourite_Activity.this, Dashboard_Activity.class);
                startActivity(intentthirnolist);
            }
        });


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                no_favourtie.setVisibility(View.GONE);
                list_layout.setVisibility(View.VISIBLE);
                clear_list_layout.setVisibility(View.GONE);


            }
        });
        remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_list_layout.setVisibility(View.GONE);
                //delete all db
                SQLDBHelper dbHelper;
                dbHelper = new SQLDBHelper(Favourite_Activity.this);
                dbHelper.deleteAll();


                Intent intentthir = new Intent(Favourite_Activity.this, Item_Menu_Activity.class);
                intentthir.putExtra("menuurlpath", menuurlpath);
                intentthir.putExtra("reloadback", "2");
                startActivity(intentthir);
            }
        });



        /*--------------------------Login postcode save local------------------------*/
        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);


        key_postcode = (sharedptcode.getString("KEY_postcode", null));
        key_lat = (sharedptcode.getString("KEY_lat", null));
        key_lon = (sharedptcode.getString("KEY_lon", null));
        key_area = (sharedptcode.getString("KEY_area", null));


        Log.e("localstore1", "" + key_postcode);
        Log.e("localstore2", "" + key_lat);
        Log.e("localstore3", "" + key_lon);
        Log.e("localstore4", "" + key_area);



        /*---------------check User login or not----------------------*/
        if (user_id != null && !user_id.isEmpty()) {
            //Call API
            getfavouritelist(key_postcode, key_lat, key_lon, "0", key_area, user_id);
        } else {
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            intent.putExtra("activity_details", "myfavourite");
            startActivity(intent);
        }

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
                            Toast.makeText(Favourite_Activity.this,"Your cart is Empty!",Toast.LENGTH_SHORT).show();
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

        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message-menuurlpath"));


        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverreload, new IntentFilter("custom-message-reloadlist"));

        startService(new Intent(getBaseContext(),MyService.class));
    }


    public BroadcastReceiver mMessageReceiverreload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String listname = intent.getStringExtra("reloadlist");
            getfavouritelist(key_postcode, key_lat, key_lon, "0", key_area, user_id);

        }
    };


    /*---------------------------MenuItemAdapter item value get add button click----------------------------------------------------*/
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            menuurlpath = intent.getStringExtra("menuurlpath");

            getContactsCount();

            Log.e("dasbcursor", "" + cursor);
            Log.e("sharedpreferences1", "" + menuurlpath);
            Log.e("sharedpreferences2", "" + sharedpreferences.getString("menuurlpath", null));


            if (!sharedpreferences.getString("menuurlpath", null).equalsIgnoreCase(menuurlpath) && cursor != 0) {

               /* ViewwarningDialog alert = new ViewwarningDialog();
                alert.showwarningDialog(Favourite_Activity.this);*/

                no_favourtie.setVisibility(View.GONE);
                list_layout.setVisibility(View.VISIBLE);
                clear_list_layout.setVisibility(View.VISIBLE);


            } else {
                Intent intenttwo = new Intent(Favourite_Activity.this, Item_Menu_Activity.class);
                intenttwo.putExtra("menuurlpath", menuurlpath);
                intenttwo.putExtra("reloadback", "2");
                startActivity(intenttwo);
            }


        }
    };

    /*---------------------------get favourtelist values----------------------------------------------------*/
    private void getfavouritelist(String str_key_postcode, String str_key_lat, String str_lng, String otype, String str_area, String str_userid) {
        loadingshow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", str_key_postcode);
        params.put("lat", str_key_lat);
        params.put("lng", str_lng);
        params.put("order_type", otype);
        params.put("area", str_area);
        params.put("user_id", str_userid);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<location_type_modal> call = apiService.getfavorite(params);

        Log.e("params", "" + params);
        call.enqueue(new Callback<location_type_modal>() {
            @Override
            public void onResponse(Call<location_type_modal> call, Response<location_type_modal> response) {
                //  response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {

                        no_favourtie.setVisibility(View.GONE);
                        list_layout.setVisibility(View.VISIBLE);
                        clear_list_layout.setVisibility(View.GONE);

                        List<location_type_sub_modal.clients> jobdetails = (response.body().getClientinfo().getClients());
                        DashboardSearchResultList adapter = new DashboardSearchResultList(mContext, (List<location_type_sub_modal.clients>) jobdetails);
                        myfavouritelist.setHasFixedSize(true);
                        myfavouritelist.setLayoutManager(new LinearLayoutManager(Favourite_Activity.this));
                        myfavouritelist.setAdapter(adapter);
                    } else {
                        no_favourtie.setVisibility(View.VISIBLE);
                        list_layout.setVisibility(View.GONE);
                        clear_list_layout.setVisibility(View.GONE);
                        // Snackbar.make(Favourite_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    no_favourtie.setVisibility(View.VISIBLE);
                    list_layout.setVisibility(View.GONE);
                    clear_list_layout.setVisibility(View.GONE);
                    Snackbar.make(Favourite_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<location_type_modal> call, Throwable t) {
                hideloading();
                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Favourite_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }

        });

    }

    /*---------------------------check internet connection----------------------------------------------------*/
    public class ViewDialog {
        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Favourite_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Favourite_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_chk = new Internet_connection_checking(Favourite_Activity.this);
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

    /*-------------------Loading Images------------------*/
    public void loadingshow() {
        dialog = new Dialog(Favourite_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);
        LottieAnimationView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        gifImageView.setAnimation(R.raw.newloader);
        gifImageView.playAnimation();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void hideloading() {
        dialog.dismiss();
    }


    /*---------------------------Warning dialog show----------------------------------------------------*/
  /*  public class ViewwarningDialog {
        public void showwarningDialog(Activity activity) {
            warningdialog = new Dialog(activity, R.style.MaterialDialogSheet);
            warningdialog.setContentView(R.layout.warning_cart_remove);

            AppCompatButton go_back = warningdialog.findViewById(R.id.go_back);
            AppCompatButton remove_cart = warningdialog.findViewById(R.id.remove_cart);

            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    warningdialog.dismiss();
                }
            });
            remove_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete all db
                    SQLDBHelper dbHelper;
                    dbHelper = new SQLDBHelper(Favourite_Activity.this);
                    dbHelper.deleteAll();
                    warningdialog.dismiss();

                    Intent intentthir = new Intent(Favourite_Activity.this, Item_Menu_Activity.class);
                    intentthir.putExtra("menuurlpath", menuurlpath);
                    startActivity(intentthir);

                  *//*  SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                    editor_extra.putString("menuurlpath", menuurlpath);
                    editor_extra.putString("ordermodetype", "0");
                    editor_extra.commit();

                    menugetitem(menuurlpath, "0");//menu item call api*//*
                    // startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });
            warningdialog.show();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Favourite Activity");
    }

}
