package com.fusionkitchen.activity;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;

import com.fusionkitchen.adapter.EatListAdapter;
import com.fusionkitchen.adapter.PopularRestaurantsListAdapter;
import com.fusionkitchen.model.EatListPostelModel;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.post_code_modal;
import com.fusionkitchen.model.version_code_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Postcode_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    int bottonkey;


    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;

    /*---------------------------Version Update----------------------------------------------------*/
    AlertDialog.Builder builder;//alert box
    String apiversionname;

    /*---------------------------XML ID Call----------------------------------------------------*/
    String versionName, url, postcode_seperate, postcode_seperate_str, str_postcode_seperate_str, str_postcode_seperate;//app version set
    EditText post_code_edittext;
    LinearLayout post_code_check;
    Button btn_next;

    /*---------------------------Session Manager Class----------------------------------------------------*/
    // SessionManager session;

    /*---------------------------Navigation Menu----------------------------------------------------*/
    DrawerLayout drawer;
    TextView nav_header_name;
    ImageView nav_header_close;
    NavigationView navigationView;
    TextView txtversionname;

    private static final char space = ' ';

    //every 5 seconds API Call
    Handler myHandler = new Handler();
    Runnable runnable;
    int delay = 2000; // 5000 milliseconds == 5 second

    String post_code_new;
    Dialog dialog;

    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;

    /*--------------------------Login postcode save local------------------------*/
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";


    RelativeLayout layout1, layout2;

    /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";


    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;

    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*--------------Login details get SharedPreferences------------------*/

    String user_id;

    /*----------------Rateing APP-------------------*/
    String APP_TITLE = "Fusion Kitchen";// App Name
    private final static String APP_PNAME = "com.fusionkitchen";// Package Name
    CardView cads;

    /*--------------------------RecyclerView List view ------------------------*/

    RecyclerView eat_listview;
    private EatListAdapter eatListAdapter;
    private List<EatListPostelModel> eatListPostelModel = new ArrayList<>();

    /*----------------------------Most Popular RecyclerView List view-----------------*/

    RecyclerView Most_Popular_Listview;
    private PopularRestaurantsListAdapter popularRestaurantsListAdapter;
    private List<popular_restaurants_listmodel> popularlistmodule = new ArrayList<>();

    HttpUrl baseUrl;

    String menuurlpath;
    LinearLayout clear_list_layout;
    AppCompatButton go_back, remove_cart;
    TextView invalide_postcode;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcodenew);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(Postcode_Activity.this, R.color.txt_eight));

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        go_back = findViewById(R.id.go_back);
        remove_cart = findViewById(R.id.remove_cart);
        invalide_postcode = findViewById(R.id.invalide_postcode);
        /*---------------------------Toolbar----------------------------------------------------*/
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.enter, R.anim.exit);

        baseUrl  =ApiClient.getInstance().getClient().baseUrl();

        /*------------------------------RecyclerView List view ------------------------------*/

        eat_listview = findViewById(R.id.eat_listview);
        eatListAdapter = new EatListAdapter(eatListPostelModel,Postcode_Activity.this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        eat_listview.setLayoutManager(manager);
        eat_listview.setAdapter(eatListAdapter);
        EatListDataPrepare();

        /*------------------------------------------------------------*/

        Most_Popular_Listview  =  findViewById(R.id.Most_Popular_Listview);

        /*---------------------------Get App Version----------------------------------------------------*/
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Postcode_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            ViewNoNetDialog alert = new ViewNoNetDialog();
            alert.shownonetDialog(Postcode_Activity.this);
        } else {
            //version update pop up shoeing
            versionupdate();//versionName
        }


        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Postcode_Activity.this.MODE_PRIVATE);

        if (sharedpreferences.getString("menuurlpath", null) == null) {
            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
            editor_extra.putString("menuurlpath", "0");
            editor_extra.commit();

        }

        cads = findViewById(R.id.cads);

      /*  cads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), rate.class));
            }
        });*/

     /*   SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String channel = (shared.getString(keyChannel, ""));*/

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Postcode_Activity.this);
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
        //  bottomNav.getMenu().setGroupCheckable(0, false, true);
        //  bottomNav.getOrCreateBadge(R.id.home_card).setNumber(5);

        Log.e("cursor", "" + cursor);

       /* if (cursor != 0) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE); // gone
        }*/



        BottomNavigationItemView itemView = bottomNav.findViewById(R.id.home_card);
       /* View badge = LayoutInflater.from(Postcode_Activity.this).inflate(R.layout.notifcation_badge, bottomNav, false);
        TextView itemtextcut = badge.findViewById(R.id.badge_text_view);
        itemtextcut.setText(String.valueOf(cursor));
        itemView.addView(badge);*/

      /*  itemtextcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == post_code_edittext.getId()) {
                    startActivity(new Intent(getApplicationContext(), Add_to_Cart.class));
                }
            }
        });*/

        // bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().setGroupCheckable(0, false, true);

        bottomNav.getMenu().findItem(R.id.home_search).setVisible(false);
        bottomNav.getMenu().findItem(R.id.home_chat).setVisible(true);

        // bottomNav.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) Postcode_Activity.this);
      /*  bottomNav.setSelectedItemId(R.id.home_card);
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNav.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2); // number of menu from left
        new QBadgeView(Postcode_Activity.this).bindTarget(v).setBadgeNumber(cursor);*/

        final Handler handler = new Handler();
        final int delay = 2500;

        handler.postDelayed(new Runnable() {
            public void run() {
                if(bottonkey == 2){
                    bottomNav.getMenu().findItem(R.id.home_card).setChecked(true);
                }else{
                    bottomNav.getMenu().findItem(R.id.home_bottom).setChecked(true);
                }

                handler.postDelayed(this, delay);
            }
        }, delay);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_bottom:
                        // finish();
                        //startActivity(getIntent());
                        bottonkey = 1;
                        Intent intenthome = new Intent(getApplicationContext(), Postcode_Activity.class);
                        startActivity(intenthome);
                        break;
                    case R.id.home_chat:
                        Freshchat.showConversations(getApplicationContext());
                        bottonkey = 1;
                        break;
                  /*  case R.id.home_chat:
                        Intent Offer = new Intent(getApplicationContext(),Show_Offer_Activity.class);
                        startActivity(Offer);
                        break;*/


                    case R.id.home_card:
                        bottonkey = 2;
                        if (cursor != 0) {
                            Intent intentcard = new Intent(Postcode_Activity.this, Add_to_Cart.class);
                            startActivity(intentcard);
                            bottonkey = 1;
                        }


                        //  startActivity(new Intent(getApplicationContext(), Add_to_Cart.class));
                        break;
                    case R.id.home_account:
                        if (user_id != null && !user_id.isEmpty()) {
                            Intent intentaccount = new Intent(getApplicationContext(), MyAccount_Activity.class);
                            startActivity(intentaccount);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            intent.putExtra("activity_details", "postcode");
                            startActivity(intent);
                        }
                        bottonkey = 1;
                        break;

                }
                return true;
            }
        });

        /*---------------------------XML ID Call----------------------------------------------------*/
        post_code_edittext = findViewById(R.id.post_code_edittext);
        post_code_check = findViewById(R.id.post_code_check);
        btn_next = findViewById(R.id.btn_next);
        clear_list_layout = findViewById(R.id.clear_list_layout);
     //   layout1 = findViewById(R.id.layout1);
       // layout2 = findViewById(R.id.layout2);


          post_code_edittext.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  invalide_postcode.setVisibility(View.GONE);

              }
          });
        /*---------------------------Session Manager Class----------------------------------------------------*/
        // session = new SessionManager(getApplicationContext());


       /* post_code_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == post_code_edittext.getId()) {
                    post_code_edittext.setCursorVisible(true);
                }
            }
        });*/
        post_code_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check internet connection
                hideKeybaord(v);
                validateLogin();
            }
        });

        /*---------------------------post_code_check true or not----------------------------------------------------*/

        post_code_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                invalide_postcode.setVisibility(View.GONE);
                post_code_edittext.setHintTextColor(getResources().getColor(R.color.Invalid_color_code1));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

                invalide_postcode.setVisibility(View.GONE);
                post_code_edittext.setHintTextColor(getResources().getColor(R.color.Invalid_color_code1));
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                invalide_postcode.setVisibility(View.GONE);
                post_code_edittext.setHintTextColor(getResources().getColor(R.color.Invalid_color_code1));

                Log.e("lenth", "" + s.length());

                if (s.length() == 5) {
                    String zipCode = s.toString();
                    String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
                    System.out.println("The zip code is: " + zipCode);
                    System.out.println(String.format("Is the above zip code valid? " + zipCode.matches(regex)));
                    if (zipCode.matches(regex) == true) {
                        post_code_new = zipCode.substring(0, 2) + " " + zipCode.substring(2);
                        post_code_edittext.setText(post_code_new);
                        post_code_edittext.setSelection(post_code_edittext.getText().length());
                        Log.e("zip code", " The zip code is: " + zipCode);
                        Log.e("zip code", " Is the above zip code valid? " + zipCode.matches(regex));//.matches(regex));
                    }
                }
                if (s.length() == 6) {
                    String zipCode = s.toString();
                    String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
                    System.out.println("The zip code is: " + zipCode);
                    System.out.println(String.format("Is the above zip code valid? " + zipCode.matches(regex)));
                    if (zipCode.matches(regex) == true) {
                        post_code_new = zipCode.substring(0, 3) + " " + zipCode.substring(3);
                        post_code_edittext.setText(post_code_new);
                        post_code_edittext.setSelection(post_code_edittext.getText().length());
                        Log.e("zip code", " The zip code is: " + zipCode);
                        Log.e("zip code", " Is the above zip code valid? " + zipCode.matches(regex));//.matches(regex));
                    }
                }
                if (s.length() == 7) {
                    String zipCode = s.toString();
                    String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";

                    System.out.println("The zip code is: " + zipCode);
                    System.out.println(String.format("Is the above zip code valid? " + zipCode.matches(regex)));
                    if (zipCode.matches(regex) == true) {
                        post_code_new = zipCode.substring(0, 4) + " " + zipCode.substring(4);
                        post_code_edittext.setText(post_code_new);
                        post_code_edittext.setSelection(post_code_edittext.getText().length());
                        Log.e("zip code", " The zip code is: " + post_code_new);
                        Log.e("zip code", " The zip code is: " + zipCode);
                        Log.e("zip code", " Is the above zip code valid? " + zipCode.matches(regex));//.matches(regex));
                    }
                }
            }
        });


        /*---------------------------Navigation Menu----------------------------------------------------*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.getDrawerArrowDrawable().setBarThickness(0);
        toggle.getDrawerArrowDrawable().setBarLength(0);
        toggle.getDrawerArrowDrawable().setGapSize(0);


        //drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // int versionNumber = pinfo.versionCode;
        String versionName = pinfo.versionName;


        txtversionname = navigationView.findViewById(R.id.txtversionname);
        txtversionname.setText("V" + versionName + " " + "Live");

        View header = navigationView.getHeaderView(0);
        nav_header_close = header.findViewById(R.id.nav_header_close);

        navigationView.getMenu().getItem(10).setActionView(R.layout.menu_hide);
        navigationView.getMenu().getItem(11).setActionView(R.layout.menu_show);

        hideItem();



        nav_header_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // drawer.closeDrawer(Gravity.LEFT);
                drawer.closeDrawers();
            }
        });


        /*--------------Login store SharedPreferences------------------*/
        CheckLogin();

        /*---------------Load Popular Restaurants ------------------------*/

        PopularRestaurants();


        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message-menuurlpath"));


        /*---------------------------Post code Adapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mInvaild_postcode, new IntentFilter("Invaild_Postcode_activity"));


        /*---------------Remove cart popup--------------------*/
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_list_layout.setVisibility(View.GONE);
            }
        });


        remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_list_layout.setVisibility(View.GONE);
                SQLDBHelper dbHelper;
                dbHelper = new SQLDBHelper(Postcode_Activity.this);
                dbHelper.deleteAll();

                Intent intentthir = new Intent(Postcode_Activity.this, Item_Menu_Activity.class);
                intentthir.putExtra("menuurlpath", menuurlpath);
                intentthir.putExtra("reloadback", "4");
                startActivity(intentthir);
                finish();

            }
        });

        SharedPreferences sharedpreferences = getSharedPreferences("PREFS_MOREINFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("More_info", "Moreinfo-popup");
        editor.commit();


    }



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



               /* Dashboard_Activity.ViewwarningDialog alert = new Dashboard_Activity.ViewwarningDialog();
                alert.showwarningDialog(Dashboard_Activity.this);*/

                clear_list_layout.setVisibility(View.VISIBLE);

            } else {


                Intent intenttwo = new Intent(Postcode_Activity.this, Item_Menu_Activity.class);
                intenttwo.putExtra("menuurlpath", menuurlpath);
                intenttwo.putExtra("reloadback", "4");
                startActivity(intenttwo);
                finish();

            }


        }
    };




    /*---------------------------MenuItemAdapter item value get add button click----------------------------------------------------*/
    public BroadcastReceiver mInvaild_postcode = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String invaild_postcode = intent.getStringExtra("PostCode_Activity");

            post_code_edittext.setHintTextColor(getResources().getColor(R.color.Invalid_color_code));
            post_code_edittext.setFocusableInTouchMode(true);
            post_code_edittext.requestFocus();
            post_code_edittext.clearFocus();

        }
    };


    private void PopularRestaurants() {

        loadingshow();

        popularRestaurantsListAdapter = new PopularRestaurantsListAdapter(popularlistmodule,Postcode_Activity.this);
        RecyclerView.LayoutManager manager4 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        Most_Popular_Listview.setLayoutManager(manager4);
        Most_Popular_Listview.setAdapter(popularRestaurantsListAdapter);
        //https://www.api.fusionkitchen.co.uk/loadPopularRestaurants
          StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,baseUrl+"loadPopularRestaurants",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hideloading();
                            JSONObject jsonobject = new JSONObject(response);
                            JSONObject getdata =jsonobject.getJSONObject("data");
                            JSONArray most_popular_list = getdata.getJSONArray("popular_restaurants");

                              for (int i = 0; i < most_popular_list.length(); i++) {
                                 JSONObject object = most_popular_list.getJSONObject(i);
                                    popular_restaurants_listmodel popularlist = new popular_restaurants_listmodel(
                                            object.getString("name"),
                                            object.getString("area"),
                                            object.getString("rating_average"),
                                            object.getString("takeawaystatus"),
                                            object.getString("discount"),
                                            object.getString("image_url"),
                                            object.getString("postcode"),
                                            object.getString("address_location"),
                                            object.getString("menupageurl"),
                                            object.getString("lat"),
                                            object.getString("lang")
                                    );
                                  popularlistmodule.add(popularlist);
                                }

                            popularRestaurantsListAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
                            e.printStackTrace();
                            hideloading();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        hideloading();
                    }
                }){


           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",userid);
                return params;
            }*/
        };

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);


    }

    private void EatListDataPrepare() {
            EatListPostelModel data = new EatListPostelModel(R.drawable.pizza, "1");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.burger, "2");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.biryanai, "3");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.curry, "4");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.southindian, "5");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.drinks, "6");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.breakfast, "7");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.curry, "8");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.kebab, "9");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.cake, "10");
        eatListPostelModel.add(data);
        data = new EatListPostelModel(R.drawable.fishchips, "11");
        eatListPostelModel.add(data);
    }

    /*--------------Login store SharedPreferences------------------*/
    public void CheckLogin() {
        if (slogin == null)
            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);

           String login_status = slogin.getString("login_key_status", "");



/*        if (login_status != null && !login_status.equals("")) {
//login Successfully
          //  layout1.setVisibility(View.INVISIBLE);
          //  layout2.setVisibility(View.VISIBLE);



            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_myaccount).setVisible(true);

            navigationView.getMenu().findItem(R.id.nav_orderhistory).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_orderstatus).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_address).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_wallet).setVisible(true);


        } else {
//login not Successfully
          //  layout1.setVisibility(View.VISIBLE);
           // layout2.setVisibility(View.INVISIBLE);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_myaccount).setVisible(false);


            navigationView.getMenu().findItem(R.id.nav_orderhistory).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_orderstatus).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_address).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_wallet).setVisible(false);


        }*/


        if (login_status.equalsIgnoreCase("true")) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE); // gone
        }

        if (login_status.equalsIgnoreCase("true")) {

            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_myaccount).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_orderhistory).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_orderstatus).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_address).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_wallet).setVisible(true);


        } else {
//login not Successfully
            //  layout1.setVisibility(View.VISIBLE);
            // layout2.setVisibility(View.INVISIBLE);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_myaccount).setVisible(false);


            navigationView.getMenu().findItem(R.id.nav_orderhistory).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_orderstatus).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_address).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_wallet).setVisible(false);


        }

    }

    /*---------------------------validate Login----------------------------------------------------*/
    /*Check Login Details Hear...!*/
    private void validateLogin() {
        if (TextUtils.isEmpty(post_code_edittext.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(post_code_edittext.getWindowToken(), 0);
            post_code_edittext.setError("Please Enter Your Postcode");
            Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), "Please Enter Your Postcode", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            str_postcode_seperate = post_code_edittext.getText().toString().replace(" ", "");
            if (str_postcode_seperate.length() == 5) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 2) + " " + str_postcode_seperate.substring(2);
            } else if (str_postcode_seperate.length() == 6) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 3) + " " + str_postcode_seperate.substring(3);
            } else if (str_postcode_seperate.length() == 7) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 4) + " " + str_postcode_seperate.substring(4);
            } else {
                str_postcode_seperate_str = str_postcode_seperate;
            }
            post_code_edittext.setText(str_postcode_seperate_str.toUpperCase());
            post_code_edittext.setSelection(post_code_edittext.getText().length());
            postcodecheck(post_code_edittext.getText().toString());
        }
    }

    /*---------------------------Version Update----------------------------------------------------*/
    //get api values
    private void versionupdate() {

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<version_code_modal> call = apiService.getversion();
        call.enqueue(new Callback<version_code_modal>() {
            @Override
            public void onResponse(Call<version_code_modal> call, Response<version_code_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                Log.d("responsemsg1", "ok" + response);
                /*Get Login Good Response...*/
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                         apiversionname = response.body().getVersion().get(0).getVersion();
                        if (!versionName.equalsIgnoreCase(apiversionname)) {
                            url = response.body().getVersion().get(0).getApp_url();
                           /* Postcode_Activity.ViewDialog alert = new Postcode_Activity.ViewDialog();
                            alert.showDialog(Postcode_Activity.this, url);*/
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                     showPopup(url);
                                     //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                }
                            }, 200);
                        }


                     /*   url = response.body().getUrl();
                        Postcode_Activity.ViewDialog alert = new Postcode_Activity.ViewDialog();
                        alert.showDialog(Postcode_Activity.this, "Update Available", "A new version of the app is available.\n Would you like to update them now?", url);
               */

                    }
                } else {

                    Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<version_code_modal> call, Throwable t) {

                Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });

    }

    /*---------------------------Submit Post Code Check----------------------------------------------------*/
    //get api values
    private void postcodecheck(final String post_code) {
        loadingshow();
        //final ProgressDialog loader = ProgressDialog.show(Postcode_Activity.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", post_code);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<post_code_modal> call = apiService.getpostcode(params);
        call.enqueue(new Callback<post_code_modal>() {
            @Override
            public void onResponse(Call<post_code_modal> call, Response<post_code_modal> response) {
                int statusCode = response.code();

                Log.e("Postcode_Activity", new Gson().toJson(response.body()));

                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    hideloading();
                    // loader.dismiss();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {

                        //  session.createLoginSession(response.body().getUrl(), response.headers().get("Set-Cookie"), response.body().getArea(), response.body().getFullpostcode(), response.body().getLocation().getLat(), response.body().getLocation().getLng(), response.body().getAddress());
                        Intent intent = new Intent(Postcode_Activity.this, Dashboard_Activity.class);
                        /*--------------------------Login postcode save local------------------------*/
                        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);

                        SharedPreferences.Editor editorpostcode = sharedptcode.edit();
                        editorpostcode.putString("KEY_posturl", response.body().getUrl());
                        editorpostcode.putString("KEY_area", response.body().getArea());
                        editorpostcode.putString("KEY_postcode", response.body().getFullpostcode());
                        editorpostcode.putString("KEY_lat", response.body().getLocation().getLat());
                        editorpostcode.putString("KEY_lon", response.body().getLocation().getLng());
                        editorpostcode.putString("KEY_address", response.body().getAddress());
                        editorpostcode.commit();
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } else {
                        //Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), response.body().getPostcode(), Snackbar.LENGTH_LONG).show();
                        invalide_postcode.setVisibility(View.VISIBLE);
                    }


                } else {
                    hideloading();
                    // loader.dismiss();
                    Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<post_code_modal> call, Throwable t) {

                Log.e("Tro", "" + t);
                hideloading();
                // loader.dismiss();
                Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }

    public void showPopup(String Playstoreurl) {
        View popupView = LayoutInflater.from(Postcode_Activity.this).inflate(R.layout.dialog_version_update, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        AppCompatButton notnow = popupView.findViewById(R.id.notnow);
        AppCompatButton update = popupView.findViewById(R.id.update);

        GifImageView custom_loading_imageView = popupView.findViewById(R.id.custom_loading_imageView);

        Animation imganim = AnimationUtils.loadAnimation(Postcode_Activity.this, R.anim.downtoup);
        custom_loading_imageView.startAnimation(imganim);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Playstoreurl)));
                popupWindow.dismiss();
            }
        });
        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        popupWindow.showAsDropDown(popupView, 0, 0);
    }


    /*---------------------------Version Update----------------------------------------------------*/
  /*  public class ViewDialog {
        public void showDialog(Activity activity, String url) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_version_update);

            AppCompatButton notnow = dialog.findViewById(R.id.notnow);
            AppCompatButton update = dialog.findViewById(R.id.update);

            ImageView custom_loading_imageView = dialog.findViewById(R.id.custom_loading_imageView);

         *//*   Animation imganim = AnimationUtils.loadAnimation(Postcode_Activity.this, R.anim.enter);
            custom_loading_imageView.startAnimation(imganim);
*//*

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    dialog.dismiss();
                }
            });
            notnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }*/


    /*---------------------------check internet connection----------------------------------------------------*/
    public class ViewNoNetDialog {
        public void shownonetDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Postcode_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Postcode_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_chk = new Internet_connection_checking(Postcode_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {
                        alertDialog.dismiss();
                    }

                }
            });
            alertDialog.show();

        }
    }


    /*---------------------------Hide Keybaord----------------------------------------------------*/
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }


    /*---------------------------Navigation Menu----------------------------------------------------*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // drawer.closeDrawers();
            drawer.closeDrawers();
        } else if (id == R.id.nav_login) {


           /* Intent intent = new Intent(getApplicationContext(), New_Login.class);
            intent.putExtra("activity_details", "pcode");
            startActivity(intent);*/


            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            intent.putExtra("activity_details", "pcode");
            startActivity(intent);
            drawer.closeDrawers();
        } else if (id == R.id.nav_myaccount) {
            startActivity(new Intent(getApplicationContext(), MyAccount_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_address) {
            startActivity(new Intent(getApplicationContext(), Address_Book_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_wallet) {
            startActivity(new Intent(getApplicationContext(), Wallet_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(getApplicationContext(), Notification_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_orderhistory) {
            startActivity(new Intent(getApplicationContext(), Order_History_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_orderstatus) {
            startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_more_hide) {
            hideItem();

        } else if (id == R.id.nav_more_show) {
            showItem();

        } else if (id == R.id.nav_aboutus) {
            startActivity(new Intent(getApplicationContext(), Aboutus_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_rateapp) {
            drawer.closeDrawers();

            showRateDialog(Postcode_Activity.this);
            /*----------------Rateing APP-------------------*/


            // startActivity(new Intent(getApplicationContext(), Review_Activity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_termsconditions) {
            startActivity(new Intent(getApplicationContext(), Terms_Conditions_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_privacy) {
            startActivity(new Intent(getApplicationContext(), Privacy_Policy_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_allergy) {
            startActivity(new Intent(getApplicationContext(), Allergy_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_logout) {

            try {
                if (slogin == null)
                    slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);

                sloginEditor = slogin.edit();
                sloginEditor.putString("login_key_status", "");
                sloginEditor.putString("login_key_cid", "");
                sloginEditor.putString("login_key_vcode", "");
                sloginEditor.commit();
                startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                drawer.closeDrawers();

            } catch (Exception ex) {
                Toast.makeText(Postcode_Activity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            }

        }
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/

        return true;
    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Postcode_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        Glide.with(Postcode_Activity.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);
        dialog.show();
    }

    public void hideloading() {
        dialog.dismiss();
    }


    private void hideItem() {

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_aboutus).setVisible(false);
        nav_Menu.findItem(R.id.nav_termsconditions).setVisible(false);
        nav_Menu.findItem(R.id.nav_privacy).setVisible(false);
        nav_Menu.findItem(R.id.nav_allergy).setVisible(false);
        nav_Menu.findItem(R.id.nav_more_hide).setVisible(false);
        nav_Menu.findItem(R.id.nav_more_show).setVisible(true);

    }

    private void showItem() {

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_aboutus).setVisible(true);
        nav_Menu.findItem(R.id.nav_termsconditions).setVisible(true);
        nav_Menu.findItem(R.id.nav_privacy).setVisible(true);
        nav_Menu.findItem(R.id.nav_allergy).setVisible(true);
        nav_Menu.findItem(R.id.nav_more_hide).setVisible(true);
        nav_Menu.findItem(R.id.nav_more_show).setVisible(false);

    }

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    public int getContactsCount() {
        cursor = dbHelper.numberOfRows();
        Log.e("tmpStr10", "" + cursor);
        return cursor;
    }


/*    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);

            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
            return true;
        }
        return false;
    }*/

    public void showRateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Fusion Kitchen")
                .setMessage("If you enjoy using Fusion Kitchen app, please take a moment to rate it. Thanks for your support!")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.fusionkitchen")));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.fusionkitchen")));
                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Postcode Activity");

    }

    public void onBackPressed(){




        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            Intent backbtn = new Intent(Intent.ACTION_MAIN);
            backbtn.addCategory(Intent.CATEGORY_HOME);
            backbtn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backbtn);
            return;
        }
        else {
            Toast.makeText(getBaseContext(), "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        }

         mBackPressed = System.currentTimeMillis();

       /*Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);*/

    }

}

