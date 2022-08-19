package com.fusionkitchen.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;

import com.fusionkitchen.app.MyApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;


import com.fusionkitchen.adapter.DashboardSearchResultList;
import com.fusionkitchen.adapter.MoreinfoopenhrsAdapter;
import com.fusionkitchen.adapter.ReviewDetailsAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;


import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.model.moreinfo.about_us_model;
import com.fusionkitchen.model.moreinfo.review_list_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//implements OnMapReadyCallback
// implements OnMapReadyCallback
public class Moreinfo_Activity extends AppCompatActivity implements OnMapReadyCallback {


    private Context mContext = Moreinfo_Activity.this;
    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;


    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*---------------------------Back Button Click----------------------------------------------------*/
    CardView back_card_view1;


    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;
    Dialog dialog;

    /*---------------------------XML ID Call----------------------------------------------------*/

    LinearLayout about_layout, opentime_layout, review_layout;
    AppCompatButton menu, aboutus, opentime, review;
    TextView shop_name, shop_address;
    ImageView direction, shop_gb_images;

    /*--------------------Location Map----------------------*/

    Location currentLocation;
    String currentLocation1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private Location location;
    Marker marker1;


    /*---------------------------Session Manager Class----------------------------------------------------*/
    // Session Manager Class
    // SessionManager session;
    String authKey, fullUrl;

    /*-------------------------SharedPreferences--------------------------------*/
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    String menuurlpath, order_types, str_client_name, str_tv_desc;
    TextView clent_name, clent_submenu, no_review;
    RecyclerView recyclerview_review;

    /*--------------------Open hrs-----------------------*/
    RecyclerView open_hrs_review;
    /*--------------------About us-----------------------*/
    TextView about_deatils, cusines_details;

    /*---------------------------map-------------------------------*/
    /*AddressResultReceiver mResultReceiver;
    int fetchType = Constants.USE_ADDRESS_LOCATION;
    private static final String TAG = "Moreinfo_Activity";

    double latvalue, lonvalue;

    LatLng latLng;*/

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id;
    String strsgetAddress1, strsAddress2, strscity, strsstate, strspincd;

    /*---------------------Google map------------------------*/

    private GoogleMap mMap;
    LatLng p1 = null;
    double p2;
    double p3;

    /*--------------------------Login postcode save local------------------------*/
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    String key_postcode, key_address, key_area;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_morinfo);



        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);



        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Moreinfo_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Moreinfo_Activity.ViewDialog alert = new Moreinfo_Activity.ViewDialog();
            alert.showDialog(Moreinfo_Activity.this);

        }


        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back_card_view1 = findViewById(R.id.back_card_view1);
        back_card_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences("PREFS_MOREINFO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("More_info", "MoreInfo");
                editor.commit();
                finish();

            }
        });

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));


        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
        Intent intent = getIntent();
        order_types = intent.getStringExtra("order_types");
        str_client_name = intent.getStringExtra("client_name");
        str_tv_desc = intent.getStringExtra("tv_desc");

        Log.e("moreorder_types", "" + order_types);

        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);

        menuurlpath = sharedpreferences.getString("menuurlpath", null);

        /*---------------------------Session Manager Class----------------------------------------------------*/
       /* session = new SessionManager(getApplicationContext());
        session.checkLogin();*/


        /*--------------------------Login postcode save local------------------------*/
        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);


        key_postcode = (sharedptcode.getString("KEY_postcode", null));
        key_area = (sharedptcode.getString("KEY_area", null));
        key_address = (sharedptcode.getString("KEY_address", null));


        //  HashMap<String, String> user = session.getUserDetails();
        /*-------------------------About US-------------------------------*/

        aboutusshop(order_types, key_postcode, key_area, key_address, menuurlpath);



        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Moreinfo_Activity.this);
        getContactsCount();





        /*---------------------------XML ID Call----------------------------------------------------*/
        menu = findViewById(R.id.menu);
        aboutus = findViewById(R.id.aboutus);
        opentime = findViewById(R.id.opentime);
        review = findViewById(R.id.review);

        about_layout = findViewById(R.id.about_layout);
        opentime_layout = findViewById(R.id.opentime_layout);
        review_layout = findViewById(R.id.review_layout);


        shop_name = findViewById(R.id.shop_name);
        shop_address = findViewById(R.id.shop_address);
        direction = findViewById(R.id.direction);
        shop_gb_images = findViewById(R.id.shop_gb_images);
        recyclerview_review = findViewById(R.id.recyclerview_review);
        no_review = findViewById(R.id.no_review);

        clent_name = findViewById(R.id.clent_name);
        clent_submenu = findViewById(R.id.clent_submenu);


       /* clent_name.setText(str_client_name);
        clent_submenu.setText(str_tv_desc.replace(",", " | "));*/

        /*---------------------------On Click----------------------------------------------------*/
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences("PREFS_MOREINFO", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("More_info", "MoreInfo");
                editor.commit();
                finish();
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                about_layout.setVisibility(View.VISIBLE);
                opentime_layout.setVisibility(View.GONE);
                review_layout.setVisibility(View.GONE);

                menu.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                menu.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                aboutus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                aboutus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));

                opentime.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                opentime.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                review.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                review.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));


            }
        });
        opentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                about_layout.setVisibility(View.GONE);
                opentime_layout.setVisibility(View.VISIBLE);
                review_layout.setVisibility(View.GONE);


                menu.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                menu.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                aboutus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                aboutus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                opentime.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                opentime.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));

                review.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                review.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));


            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_layout.setVisibility(View.GONE);
                opentime_layout.setVisibility(View.GONE);
                review_layout.setVisibility(View.VISIBLE);

                menu.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                menu.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                aboutus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                aboutus.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                opentime.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                opentime.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                review.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                review.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));


                review_details(order_types, key_postcode, key_area, key_address);

            }
        });

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
                        Intent intenthome = new Intent(getApplicationContext(), Moreinfo_Activity.class);
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


        /*--------------------Location Map----------------------*/

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Moreinfo_Activity.this);
        //fetchLocation();


        /*--------------------Open hrs-----------------------*/
        open_hrs_review = findViewById(R.id.open_hrs_review);

        /*--------------------About us-----------------------*/
        about_deatils = findViewById(R.id.about_deatils);
        cusines_details = findViewById(R.id.cusines_details);


    }


    /*-------------------------About US-------------------------------*/
    private void aboutusshop(String order_types, String postcode, String area, String address, String path) {
        // get user data from session

        Map<String, String> params = new HashMap<String, String>();
        params.put("ordermode", order_types);
        params.put("postcode", postcode);
        params.put("area", area);
        params.put("address_location", address);

        fullUrl = path + "/about";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<about_us_model> call = apiService.getaboutus(fullUrl, params);
        call.enqueue(new Callback<about_us_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<about_us_model> call, Response<about_us_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {

                        about_deatils.setText(response.body().getAbout().getAboutus().replace("\n", ""));

                        if (response.body().getAbout().getCuisines().size() == 0) {
                            cusines_details.setText("Data not Found");
                        } else if (response.body().getAbout().getCuisines().size() == 1) {
                            cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName());
                            clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName());
                        } else if (response.body().getAbout().getCuisines().size() == 2) {
                            cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName() + response.body().getAbout().getCuisines().get(1).getName());
                            clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(1).getName());
                        } else if (response.body().getAbout().getCuisines().size() == 3) {
                            cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName() + response.body().getAbout().getCuisines().get(1).getName() + response.body().getAbout().getCuisines().get(2).getName());
                            clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(1).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(2).getName());
                        } else {
                            cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName() + response.body().getAbout().getCuisines().get(1).getName() + response.body().getAbout().getCuisines().get(2).getName() + response.body().getAbout().getCuisines().get(3).getName());
                            clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(1).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(2).getName());
                        }

                        clent_name.setText(response.body().getAbout().getGooglemaps().getName());
                        shop_name.setText(response.body().getAbout().getGooglemaps().getName());


                        if (response.body().getAbout().getGooglemaps().getAddress1() != null && !response.body().getAbout().getGooglemaps().getAddress1().isEmpty()) {

                            strsgetAddress1 = response.body().getAbout().getGooglemaps().getAddress1();
                        } else {
                            strsgetAddress1 = "";
                        }

                        if (response.body().getAbout().getGooglemaps().getAddress2() != null && !response.body().getAbout().getGooglemaps().getAddress2().isEmpty()) {
                            strsAddress2 = response.body().getAbout().getGooglemaps().getAddress2();
                        } else {
                            strsAddress2 = "";
                        }


                        if (response.body().getAbout().getGooglemaps().getCity() != null && !response.body().getAbout().getGooglemaps().getCity().isEmpty()) {

                            strscity = response.body().getAbout().getGooglemaps().getCity();
                        } else {
                            strscity = "";
                        }

                        if (response.body().getAbout().getGooglemaps().getState() != null && !response.body().getAbout().getGooglemaps().getState().isEmpty()) {
                            strsstate = response.body().getAbout().getGooglemaps().getState();
                        } else {
                            strsstate = "";
                        }

                        if (response.body().getAbout().getGooglemaps().getPincode() != null && !response.body().getAbout().getGooglemaps().getPincode().isEmpty()) {
                            strspincd = response.body().getAbout().getGooglemaps().getPincode();
                        } else {
                            strspincd = "";
                        }

                        shop_address.setText(strsgetAddress1.replace(",", "") + " " + strsAddress2.replace(",", "") + " " + strscity.replace(",", "") + " " + strsstate.replace(",", "") + " " + strspincd.replace(",", ""));


                        /*---------------------Google Map------------------------*/

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(Moreinfo_Activity.this);

                        getLocationFromAddress(Moreinfo_Activity.this, shop_address.getText().toString());



                      /*  Picasso.with(Moreinfo_Activity.this)
                                .load(response.body().getAbout().getGooglemaps().getBg())
                                .placeholder(R.drawable.headerplaceholder)
                                .error(R.drawable.headerplaceholder)
                                .into(shop_gb_images);*/


                        List<about_us_model.aboutdetails.openinghours> jobdetails = (response.body().getAbout().getOpeninghours());
                        MoreinfoopenhrsAdapter adapter = new MoreinfoopenhrsAdapter(mContext, jobdetails);
                        open_hrs_review.setHasFixedSize(true);
                        open_hrs_review.setLayoutManager(new LinearLayoutManager(Moreinfo_Activity.this));
                        open_hrs_review.setAdapter(adapter);






                      /*  fetchType = Constants.USE_ADDRESS_NAME;
                        mResultReceiver = new AddressResultReceiver(null);

                        Log.e("Latitude4", "Address: " + shop_address.getText().toString());

                        Intent intent = new Intent(Moreinfo_Activity.this, GeocodeAddressIntentService.class);
                        intent.putExtra(Constants.RECEIVER, mResultReceiver);
                        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
                        intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, shop_address.getText().toString());
                        Log.e(TAG, "Starting Service");
                        startService(intent);*/


                    } else {
                    }
                } else {

                    Snackbar.make(Moreinfo_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<about_us_model> call, Throwable t) {
                Snackbar.make(Moreinfo_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }


        });

    }


   /* class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        latvalue = address.getLatitude();
                        lonvalue = address.getLongitude();
                        Log.e("Latitude1", "Latitude: " + latvalue);
                        Log.e("Latitude2", "Longitude: " + lonvalue);


                        new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fetchLocation();
                            }
                        }.start();


                        // onMapReady(latvalue,lonvalue);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Latitude3", "Latitude: " + resultData.getString(Constants.RESULT_DATA_KEY));

                    }
                });
            }
        }
    }*/


    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Moreinfo_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Moreinfo_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Moreinfo_Activity.this);
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


    /*--------------------Location Map----------------------*/


    /*private void fetchLocation() {


        if (ActivityCompat.checkSelfPermission(

                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;


        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    direction.setVisibility(View.VISIBLE);
                    currentLocation = location;
                    // Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Moreinfo_Activity.this);


                }
            }
        });


    }*/

 /*   @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        double latss = Double.parseDouble(String.valueOf(latvalue));
        double lngss = Double.parseDouble(String.valueOf(lonvalue));
        // 13.078111003136573, 80.24059104303754
        // 53.256940,-2.125097
        latLng = new LatLng(latss, lngss);
        Log.e("Latitude5", "Longitude: " + latLng);
        int height = 130;
        int width = 130;
       *//* BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);*//*
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(shop_name.getText().toString());//.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
       // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latss,lngss), 15.5f), 4000, null);

        //   shop_name.setText("ok");
        //   shop_address.setText("ok");

        // 53.256940,-2.125097  //uk
        //13.097768, 80.185483 //india

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Uri gmmIntentUri = Uri.parse("google.navigation:q=latLng&avoid=tf");
                Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", latss, lngss));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

    }*/


    /*  ---------------------------get api URL type values----------------------------------------------------*/
    private void review_details(String ordertype, String str_key_postcode, String str_key_area, String str_key_address) {
        loadingshow();
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();

        // HashMap<String, String> user = session.getUserDetails();


        params.put("ordermode", ordertype);
        params.put("postcode", str_key_postcode);
        params.put("area", str_key_area);
        params.put("address_location", str_key_address);


        fullUrl = menuurlpath + "/review";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<review_list_model> call = apiService.reviewlist(fullUrl, params);

        call.enqueue(new Callback<review_list_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<review_list_model> call, Response<review_list_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        hideloading();

                        List<review_list_model.review_list> jobdetails = (response.body().getReview());
                        ReviewDetailsAdapter adapter = new ReviewDetailsAdapter(mContext, (List<review_list_model.review_list>) jobdetails);
                        recyclerview_review.setHasFixedSize(true);
                        recyclerview_review.setLayoutManager(new LinearLayoutManager(Moreinfo_Activity.this));
                        recyclerview_review.setAdapter(adapter);


                        if (adapter.getItemCount() == 0) {
                            no_review.setVisibility(View.VISIBLE);
                            recyclerview_review.setVisibility(View.GONE);
                        } else {
                            no_review.setVisibility(View.GONE);
                            recyclerview_review.setVisibility(View.VISIBLE);
                        }


                    } else {
                        hideloading();
                        Snackbar.make(Moreinfo_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Snackbar.make(Moreinfo_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<review_list_model> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Moreinfo_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Moreinfo_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loading_layout);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(Moreinfo_Activity.this)
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


    //permissions
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    new CountDownTimer(2000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            fetchLocation();
                        }
                    }.start();


                } else {
                    direction.setVisibility(View.GONE);
                    Snackbar.make(Moreinfo_Activity.this.findViewById(android.R.id.content), "Please allow the location to view the direction of the takeaway", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }*/




    /* ------------------------Google Map--------------------------*/


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;


        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            p2 = location.getLatitude();
            p3 = location.getLongitude();
            Log.e("getLatitude", "" + location.getLatitude());
            Log.e("getLongitude", "" + location.getLongitude());
            direction.setVisibility(View.VISIBLE);

            direction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  Uri gmmIntentUri = Uri.parse("google.navigation:q=latLng&avoid=tf");
                    Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", p2, p3));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                }
            });


        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device.
     * This method will only be triggered once the user has installed
     * Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(p2, p3);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        mMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("Tutorialspoint.com"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p2, p3), 17.0f));
    }
    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("MoreInfo Activity");
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedpreferences = getSharedPreferences("PREFS_MOREINFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("More_info", "MoreInfo");
        editor.commit();
        finish();
    }
}
