package com.fusionkitchen.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;

import com.fusionkitchen.adapter.PopularRestaurantsListAdapter;
import com.fusionkitchen.adapter.Slider_adapter;
import com.fusionkitchen.model.EatListPostelModel;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.HomeFetch_Detail_Model;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;


import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.post_code_modal;
import com.fusionkitchen.model.version_code_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//public class Postcode_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
public class Postcode_Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int TIME_INTERVAL = 2000;
    LinearLayout referToFriend,homePageTxt,profileDetails,favouriteNav;
    LinearLayout myOrderNav,upComingOrder,myAccountNav,walletNavIcon;
    LinearLayout notificationNav,perksNav,fkPlusNav,addressListNav;
    LinearLayout helpNav,rateApp,aboutNav,allergyInfoNav;
    LinearLayout termsConditionNav,termsOfUse,privacyPolicyNav,deteleAccountNav,logoutNav;
    LinearLayout loginNav, moreHideView;

    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    private static final int REQUEST_CHECK_SETTINGS = 1001;

    private long mBackPressed;
    int bottonkey;
    Dialog offerPopup,comeingSoon;
    Bitmap myBitmap;

    private List<String> listItems;
    private ViewPager page;
    private TabLayout tabLayout;

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
    TextView nav_header_name,homeTxt;
    ImageView nav_header_close,frontImageViewLayout;
    NavigationView navigationView;
    RelativeLayout homePageLayout;
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
    SharedPreferences sharedptcode, offer_splash;
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
    Boolean offervalue;

    /*--------------------------RecyclerView List view ------------------------*/


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
    // SliderView sliderView;
    ViewPager mViewPager;
    LinearLayout myCurrentLocation;
    private ShimmerFrameLayout mShimmerViewContainer;

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 100;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcodenew);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(Postcode_Activity.this, R.color.status_bar_color));



        /*---------------------------silder start---------------------*/


        page = findViewById(R.id.my_pager);
        tabLayout = findViewById(R.id.my_tablayout);

        /*---------------------------silder End---------------------*/


        go_back = findViewById(R.id.go_back);
        remove_cart = findViewById(R.id.remove_cart);
        invalide_postcode = findViewById(R.id.invalide_postcode);
        myCurrentLocation = findViewById(R.id.myCurrentLocation);
        /*---------------------------Toolbar----------------------------------------------------*/
         Toolbar toolbar = findViewById(R.id.toolbar);
         frontImageViewLayout = findViewById(R.id.frontImageViewLayout);
         homeTxt = findViewById(R.id.homeTxt);
         homePageLayout = findViewById(R.id.homePageLayout);


        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.enter, R.anim.exit);

        baseUrl = ApiClient.getInstance().getClient().baseUrl();

        /*------------------------------RecyclerView List view ------------------------------*/


      //  EatListDataPrepare();

        /*------------------------------------------------------------*/

        Most_Popular_Listview = findViewById(R.id.Most_Popular_Listview);
        logoutNav = findViewById(R.id.logoutNav);


        offer_splash = getSharedPreferences("Offer_splash_popup", MODE_PRIVATE);
        offervalue = offer_splash.getBoolean("offervalue", true);

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
            //  versionupdate();//versionName
        }

        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Postcode_Activity.this.MODE_PRIVATE);

        if (sharedpreferences.getString("menuurlpath", null) == null) {
            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
            editor_extra.putString("menuurlpath", "0");
            editor_extra.commit();

        }

        cads = findViewById(R.id.cads);

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


        Log.e("cursor", "" + cursor);

        BottomNavigationItemView itemView = bottomNav.findViewById(R.id.home_card);


        bottomNav.getMenu().setGroupCheckable(0, false, true);

        bottomNav.getMenu().findItem(R.id.home_search).setVisible(false);
        bottomNav.getMenu().findItem(R.id.home_chat).setVisible(true);

        final Handler handler = new Handler();
        final int delay = 2500;

        handler.postDelayed(new Runnable() {
            public void run() {
                if (bottonkey == 2) {
                    bottomNav.getMenu().findItem(R.id.home_card).setChecked(true);
                } else {
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
                        } else {
                            Toast.makeText(Postcode_Activity.this, "Your cart is Empty!", Toast.LENGTH_SHORT).show();
                        }

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


        post_code_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalide_postcode.setVisibility(View.GONE);

            }
        });

        post_code_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        LinearLayout accountProfile = (LinearLayout) findViewById(R.id.accountProfile);
        accountProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.getDrawerArrowDrawable().setBarThickness(0);
        toggle.getDrawerArrowDrawable().setBarLength(0);
        toggle.getDrawerArrowDrawable().setGapSize(0);

        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pinfo.versionName;


        txtversionname = navigationView.findViewById(R.id.txtversionname);
        txtversionname.setText("Version : "+versionName + " " + "FK 2.0");

        View header = navigationView.getHeaderView(0);
        moreHideView = header.findViewById(R.id.moreHideView);
        LinearLayout hideLayoutTxt = header.findViewById(R.id.hideLayoutTxt);
        TextView textIcon = header.findViewById(R.id.textIcon);
        referToFriend = header.findViewById(R.id.referToFriend);
        homePageTxt = header.findViewById(R.id.homePageTxt);
        profileDetails = header.findViewById(R.id.profileDetails);
        favouriteNav = header.findViewById(R.id.favouriteNav);
        myOrderNav = header.findViewById(R.id.myOrderNav);
        upComingOrder = header.findViewById(R.id.upComingOrder);
        myAccountNav = header.findViewById(R.id.myAccountNav);
        walletNavIcon = header.findViewById(R.id.walletNavIcon);
        notificationNav = header.findViewById(R.id.notificationNav);
        perksNav = header.findViewById(R.id.perksNav);
        fkPlusNav = header.findViewById(R.id.fkPlusNav);
        addressListNav = header.findViewById(R.id.addressListNav);
        helpNav = header.findViewById(R.id.helpNav);
        rateApp = header.findViewById(R.id.rateApp);

        aboutNav = header.findViewById(R.id.aboutNav);
        allergyInfoNav = header.findViewById(R.id.allergyInfoNav);
        termsConditionNav = header.findViewById(R.id.termsConditionNav);
        termsOfUse = header.findViewById(R.id.termsOfUse);
        privacyPolicyNav = header.findViewById(R.id.privacyPolicyNav);
        deteleAccountNav = header.findViewById(R.id.deteleAccountNav);
        loginNav = header.findViewById(R.id.loginNav);


        referToFriend.setOnClickListener(this);
        homePageTxt.setOnClickListener(this);
        profileDetails.setOnClickListener(this);
        favouriteNav.setOnClickListener(this);
        myOrderNav.setOnClickListener(this);
        upComingOrder.setOnClickListener(this);
        myAccountNav.setOnClickListener(this);
        walletNavIcon.setOnClickListener(this);
        notificationNav.setOnClickListener(this);
        perksNav.setOnClickListener(this);
        fkPlusNav.setOnClickListener(this);
        addressListNav.setOnClickListener(this);
        helpNav.setOnClickListener(this);
        rateApp.setOnClickListener(this);

        aboutNav.setOnClickListener(this);
        allergyInfoNav.setOnClickListener(this);
        termsConditionNav.setOnClickListener(this);
        termsOfUse.setOnClickListener(this);
        privacyPolicyNav.setOnClickListener(this);
        deteleAccountNav.setOnClickListener(this);
        logoutNav.setOnClickListener(this);
        loginNav.setOnClickListener(this);

        moreHideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hideLayoutTxt.getVisibility() == View.VISIBLE){
                    hideLayoutTxt.setVisibility(View.GONE);
                    textIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_download, 0, 0, 0);
                }else{
                    hideLayoutTxt.setVisibility(View.VISIBLE);
                    textIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_arrow_nav, 0, 0, 0);
                }

            }
        });

        /*--------------Login store SharedPreferences------------------*/
        CheckLogin();
        /*---------------Load Popular Restaurants ------------------------*/

       //  PopularRestaurants();
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        fetchDetails();


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

        getdetailshareurl();


        /*-------------------------------Start Auto In Update Google---------------------------*/
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() ==
                        UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {  // FLEXIBLE

                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE,
                                Postcode_Activity.this, RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        /*-------------------------------End Auto In Update Google---------------------------*/

        startService(new Intent(getBaseContext(), MyService.class));


        myCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLastLocation();

            }
        });

    }

    private void fetchDetails() {

            Map<String, String> params = new HashMap<String, String>();
            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            Call<HomeFetch_Detail_Model> call = apiService.getHomePage(params);

            call.enqueue(new Callback<HomeFetch_Detail_Model>() {
                @Override
                public void onResponse(Call<HomeFetch_Detail_Model> call, Response<HomeFetch_Detail_Model> response) {
                    int statusCode = response.code();

                    Log.e("fetchDetail", new Gson().toJson(response.body()));

                    if (statusCode == 200) {


                        if (response.body().getSTATUS() == true) {

                             response.body().getRESPONSE().getBackGroundImage();

                             Glide.with(Postcode_Activity.this)
                                    .load(response.body().getRESPONSE().getFrontImage())
                                    .into(frontImageViewLayout);

                            String textColorPost = response.body().getRESPONSE().getPostTextColor();
                            String textColorLogan = response.body().getRESPONSE().getLoganTextColor();
                            String descriptionPost = response.body().getRESPONSE().getPostDescribtion();
                            String sloganDescription = response.body().getRESPONSE().getSloganDescribtion();

                            String styledText = "<font color=\"" + textColorPost + "\">" + descriptionPost + "</font>" + "<font color=\"" + textColorLogan + "\">" + sloganDescription + "</font>";
                            homeTxt.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

                             mShimmerViewContainer.stopShimmerAnimation();
                             mShimmerViewContainer.setVisibility(View.GONE);
                             homePageLayout.setVisibility(View.VISIBLE);

                        }


                    } else {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        homePageLayout.setVisibility(View.VISIBLE);
                        Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<HomeFetch_Detail_Model> call, Throwable t) {

                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    homePageLayout.setVisibility(View.VISIBLE);
                    Log.e("askjdnkasjdnkdnd", "" + t);
                    Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }

            });

        }


    private void getLastLocation(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> task =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.d("asbjdvhgsavdhvsadhgvc", "Location Enable");
                    if (fusedLocationProviderClient == null) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Postcode_Activity.this);
                    }

                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        try {
                                            Geocoder geocoder = new Geocoder(Postcode_Activity.this, Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            Log.d("LocationAddress", "Lattitude --->" + addresses.get(0).getLatitude());
                                            Log.d("LocationAddress", "Longitude --->" + addresses.get(0).getLongitude());
                                            Log.d("LocationAddress", "Address --->" +addresses.get(0).getAddressLine(0));
                                            Log.d("LocationAddress", "City --->" + addresses.get(0).getLocality());
                                            Log.d("LocationAddress", "Country --->" + addresses.get(0).getCountryName());
                                            Log.d("LocationAddress", "Postcode --->" +addresses.get(0).getPostalCode());
                                            postcodecheck(""+addresses.get(0).getPostalCode());


                                        } catch (IOException e) {
                                            Log.d("LocationExceptionHandle", "Lattitude --->" + e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(Postcode_Activity.this, "Location not available.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("LocationError", "Error getting location: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            });


                }
            });

            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(Postcode_Activity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied, and there's no way to fix the issue
                            break;
                    }
                }
            });


            /*------------End Location Update------------------*/

        } else {
            askPermission();
        }


    }

    private void askPermission() {

        ActivityCompat.requestPermissions(Postcode_Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);


    }


    /*------------------------------  start Atuo In update-------------------*/


    private InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        showCompletedUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.d("gokulnathan", "" + state.installStatus());
                    }
                }
            };

    @Override
    protected void onStop() {
        if (mAppUpdateManager != null)
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    private void showCompletedUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);


        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAppUpdateManager.completeUpdate();

            }
        });

        snackbar.show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RC_APP_UPDATE && resultCode != RESULT_OK) {

            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();

        }

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
              //  Toast.makeText(this, "User enabled location, start location updates", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enable GPS Location", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }

    /*------------------------------  End Atuo In update-------------------*/


    private void getdetailshareurl() {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        if (deepLink != null) {

                            String Order_tracking_share = String.valueOf(deepLink);

                            String path_share_status = Order_tracking_share.substring(Order_tracking_share.indexOf("_order_status") + 14);

                            if (path_share_status.equalsIgnoreCase("true")) {

                                String Order_tracking_status = String.valueOf(deepLink);
                                String order_id = Order_tracking_status.substring(Order_tracking_status.indexOf("_order_id=") + 10);

                                String Order_ids = order_id;
                                String[] Order_id_split = Order_ids.split("&_order_path=");
                                String Order_id_track = Order_id_split[0];  // Order_Id

                                String Order_path = Order_id_split[1];
                                String[] Order_path_split = Order_path.split("&_orderdate=");
                                String Order_path_track = Order_path_split[0]; // Order_path

                                String Order_date = Order_path_split[1];
                                String[] Order_date_split = Order_date.split("&_clientname=");
                                String Order_date_track = Order_date_split[0]; // Order_date


                                String Order_client_name = Order_date_split[1];
                                String[] Order_client_name_split = Order_client_name.split("&_clientid=");
                                String Order_Client_track = Order_client_name_split[0]; // Order_client_Name

                                String Order_client_id = Order_client_name_split[1];
                                String[] Order_client_id_split = Order_client_id.split("&_clientphonenumber=");
                                String Order_Client_id_track = Order_client_id_split[0]; // Order_client_Id

                                String Order_client_Number = Order_client_id_split[1];
                                String[] Order_client_Number_split = Order_client_Number.split("&_gpay_apikey=");
                                String Order_client_Number_track = Order_client_Number_split[0]; // Order_client_phone_no

                                String Order_Gpay_sec = Order_client_Number_split[1];
                                String[] Order_Gpay_split = Order_Gpay_sec.split("&_orderpostcode=");
                                String Order_Gpay = Order_Gpay_split[0];  // API Sec Key

                                String Order_postcode_sec = Order_Gpay_split[1];
                                String[] Order_postcode_split = Order_postcode_sec.split("&_orderarea=");
                                String Order_postCode = Order_postcode_split[0];  // Order Tracking Postcode

                                String Order_area_sec = Order_postcode_split[1];
                                String[] Order_area_sec_split = Order_area_sec.split("&_orderaddress=");
                                String Order_area = Order_area_sec_split[0];  // Order Tracking Area

                                String Order_addresss_sec = Order_area_sec_split[1];
                                String[] Order_address_sec_split = Order_addresss_sec.split("&_orderlat=");
                                String Order_address = Order_address_sec_split[0];  // Order Tracking Address

                                String Order_lat_sec = Order_address_sec_split[1];
                                String[] Order_lat_sec_split = Order_lat_sec.split("&_orderlon=");
                                String Order_lat = Order_lat_sec_split[0];  // Order Tracking lat

                                String Order_log_sec = Order_lat_sec_split[1];
                                String[] Order_log_sec_split = Order_log_sec.split("&_order_status=");
                                String Order_log = Order_log_sec_split[0];  // Order Tracking log


                                Log.d("sdjcbjscbjsdcbjsbcscb", " " + Order_postCode);
                                Log.d("sdjcbjscbjsdcbjsbcscb", " " + Order_area);
                                Log.d("sdjcbjscbjsdcbjsbcscb", " " + Order_address);
                                Log.d("sdjcbjscbjsdcbjsbcscb", " " + Order_lat);
                                Log.d("sdjcbjscbjsdcbjsbcscb", " " + Order_log);


                                String Order_data = Order_date_track.replaceAll("\\+", " ");
                                String Order_client_res_name = Order_Client_track.replaceAll("\\+", " ");

                                Intent intent = new Intent(Postcode_Activity.this, Order_Status_Activity.class);
                                intent.putExtra("orderid", Order_id_track);
                                intent.putExtra("orderpath", Order_path_track);
                                intent.putExtra("orderdate", Order_data);
                                intent.putExtra("clientname", Order_client_res_name);
                                intent.putExtra("clientid", Order_Client_id_track);
                                intent.putExtra("clientphonenumber", Order_client_Number_track);
                                intent.putExtra("gpay_apikey", Order_Gpay);
                                /*----------------------Order Tracking--------------------*/
                                intent.putExtra("order_postcode", Order_postCode);
                                intent.putExtra("order_area", Order_area);
                                intent.putExtra("order_address", Order_address);
                                intent.putExtra("order_lat", Order_lat);
                                intent.putExtra("order_lon", Order_log);

                                startActivity(intent);


                            } else {

                                String menu_url_path_share = String.valueOf(deepLink);

                                menu_url_path_share = menu_url_path_share.substring(menu_url_path_share.indexOf("_menuurl=") + 9);
                                String postcode_ = menu_url_path_share.substring(menu_url_path_share.indexOf("_postcode=") + 10);
                                String postcode_share = postcode_.replaceAll("\\+", "");
                                String key_area_share = menu_url_path_share.substring(menu_url_path_share.indexOf("_keyarea=") + 9);
                                String key_address_ = menu_url_path_share.substring(menu_url_path_share.indexOf("_address=") + 9);
                                String key_lat_ = menu_url_path_share.substring(menu_url_path_share.indexOf("_lat=") + 5);

                                String share_menu = menu_url_path_share;
                                String[] share_menu_split = share_menu.split("&_postcode=");
                                String Menu_Url = share_menu_split[0];

                                String share_post_code = postcode_;
                                String[] share_post_code_split = share_post_code.split("&_keyarea=");
                                String share_post_code_remove = share_post_code_split[0];
                                String menu_share_postcode = share_post_code_remove.replaceAll("\\+", " ");

                                String share_key_area = key_area_share;
                                String[] share_key_area_split = share_key_area.split("&_address=");
                                String share_key_area_remove = share_key_area_split[0];
                                String menu_share_area = share_key_area_remove.replaceAll("\\+", " ");

                                String share_key_address = key_address_;
                                String[] share_key_address_split = share_key_address.split("&_lat=");
                                String share_key_address_remove = share_key_address_split[0];
                                String menu_share_address = share_key_address_remove.replaceAll("\\+", " ");

                                String share_key_lat = key_lat_;
                                String[] share_key_lat_split = share_key_lat.split("&_lng=");
                                String lat_menu_share = share_key_lat_split[0];
                                String log_menu_share = share_key_lat_split[1];

                                SharedPreferences sharedptcode;
                                sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);
                                SharedPreferences.Editor getmenudata = sharedptcode.edit();
                                getmenudata.putString("KEY_posturl", "/location/" + Menu_Url);
                                getmenudata.putString("KEY_postcode", menu_share_postcode);
                                getmenudata.putString("KEY_area", menu_share_area);
                                getmenudata.putString("KEY_address", menu_share_address);
                                getmenudata.putString("KEY_lat", lat_menu_share);
                                getmenudata.putString("KEY_lon", log_menu_share);
                                getmenudata.commit();

                                Intent share_redirect = new Intent(Postcode_Activity.this, Item_Menu_Activity.class);
                                share_redirect.putExtra("menuurlpath", Menu_Url);
                                share_redirect.putExtra("reloadback", "5");
                                startActivity(share_redirect);

                            }


                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("share_get_details", "getDynamicLink:onFailure", e);
                    }
                });
    }


    /*---------------------------MenuItemAdapter item value get add button click----------------------------------------------------*/
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            menuurlpath = intent.getStringExtra("menuurlpath");

            getContactsCount();

            Log.e("dasbcursor", "" + cursor);
            Log.e("sharedpreferences1", "" + menuurlpath);
            Log.e("sharedpreferences2", "" + sharedpreferences.getString("menuurlpath", null));


            if (!sharedpreferences.getString("menuurlpath", null).equalsIgnoreCase(menuurlpath) && cursor != 0) {

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
        popularRestaurantsListAdapter = new PopularRestaurantsListAdapter(popularlistmodule, Postcode_Activity.this);
        RecyclerView.LayoutManager manager4 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        Most_Popular_Listview.setLayoutManager(manager4);
        Most_Popular_Listview.setAdapter(popularRestaurantsListAdapter);

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, baseUrl + "loadPopularRestaurants",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Load_popular_Restaur", response.toString());

                        try {
                            hideloading();
                            JSONObject jsonobject = new JSONObject(response);


                            JSONObject getdata = jsonobject.getJSONObject("data");

                            JSONArray most_popular_list = getdata.getJSONArray("popular_restaurants");
                            JSONArray banner_image = getdata.getJSONArray("banner_image");

                            JSONObject Offervalue = getdata.getJSONObject("offer_pop");
                            String offer_status = Offervalue.getString("status");
                            if (offer_status.equalsIgnoreCase("true")) {
                                String offer_image = Offervalue.getString("offer_pop_image");
                                OfferPopup(offer_image);
                            }

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

                            listItems = new ArrayList<>();
                            for (int K = 0; K < banner_image.length(); K++) {
                                listItems.add(String.valueOf(banner_image.get(K)));
                            }

                            Slider_adapter itemsPager_adapter = new Slider_adapter(Postcode_Activity.this, listItems);
                            page.setAdapter(itemsPager_adapter);

                            java.util.Timer timer = new java.util.Timer();
                            timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);
                            tabLayout.setupWithViewPager(page, true);

                            Log.d("Banner_Image", " " + banner_image.length());
                            if (banner_image.length() == 1) {
                                tabLayout.setVisibility(View.GONE);
                                // tabLayout.setVisibility(View.VISIBLE);
                            } else {
                                tabLayout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
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
                }) {

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

    private void OfferPopup(String offer_image) {

        offerPopup = new Dialog(Postcode_Activity.this);
        offerPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        offerPopup.setContentView(R.layout.home_offer_popup);
        ImageView offerimage = offerPopup.findViewById(R.id.offer_image);
        ImageView close_popup = offerPopup.findViewById(R.id.close_popup);
        Button discard_card = offerPopup.findViewById(R.id.discard_card);

        Glide.with(this)
                .load(offer_image)
                .into(offerimage);

        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerPopup.dismiss();
            }
        });

        discard_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerPopup.dismiss();
            }
        });

        if (offervalue == true) {
            offerPopup.show();
            SharedPreferences.Editor splash_popup = offer_splash.edit();
            splash_popup.putBoolean("offervalue", false);
            splash_popup.commit();
        }

        offerPopup.setCancelable(false);
        offerPopup.setCanceledOnTouchOutside(false);
        offerPopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        offerPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        offerPopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        offerPopup.getWindow().setGravity(Gravity.CENTER);

    }

    /*--------------Login store SharedPreferences------------------*/
    public void CheckLogin() {
        if (slogin == null)
            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);

        String login_status = slogin.getString("login_key_status", "");


        if (login_status.equalsIgnoreCase("true")) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE); // gone
        }

        if (login_status.equalsIgnoreCase("true")) {


            profileDetails.setVisibility(View.VISIBLE);
            myOrderNav.setVisibility(View.VISIBLE);
            upComingOrder.setVisibility(View.VISIBLE);
            myAccountNav.setVisibility(View.VISIBLE);
            perksNav.setVisibility(View.VISIBLE);
            fkPlusNav.setVisibility(View.VISIBLE);
            addressListNav.setVisibility(View.VISIBLE);
            helpNav.setVisibility(View.VISIBLE);
            rateApp.setVisibility(View.VISIBLE);
            logoutNav.setVisibility(View.VISIBLE);
            deteleAccountNav.setVisibility(View.VISIBLE);
            loginNav.setVisibility(View.GONE);





        } else {
//login not Successfully
            deteleAccountNav.setVisibility(View.GONE);
            profileDetails.setVisibility(View.GONE);
            myOrderNav.setVisibility(View.GONE);
            upComingOrder.setVisibility(View.GONE);
            myAccountNav.setVisibility(View.GONE);
            perksNav.setVisibility(View.GONE);
            fkPlusNav.setVisibility(View.GONE);
            addressListNav.setVisibility(View.GONE);
            helpNav.setVisibility(View.GONE);
            rateApp.setVisibility(View.GONE);
            loginNav.setVisibility(View.VISIBLE);
            logoutNav.setVisibility(View.GONE);


        }

    }

    /*---------------------------validate Login----------------------------------------------------*/
    /*Check Login Details Hear...!*/
    private void validateLogin() {
        if (TextUtils.isEmpty(post_code_edittext.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(post_code_edittext.getWindowToken(), 0);
          //  post_code_edittext.setError("Please Enter Your Postcode");
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

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    showPopup(url);

                                }
                            }, 200);
                        }


                    }
                } else {

                    Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<version_code_modal> call, Throwable t) {

                Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }


        });

    }

    /*---------------------------Submit Post Code Check----------------------------------------------------*/
    //get api values
    private void postcodecheck(final String post_code) {
        loadingshow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", post_code);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<post_code_modal> call = apiService.getpostcode(params);
        Log.e("paramsskjbnkjsdncksdc", " " + params);
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

                        Intent intent = new Intent(Postcode_Activity.this, Dashboard_Activity.class);
                        /*--------------------------Login postcode save local------------------------*/
                        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);

                        Log.d("hgdfhfvahsfvasfvfaf", " " + response.body().getUrl());

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
                        invalide_postcode.setVisibility(View.VISIBLE);
                    }


                } else {
                    hideloading();
                    Snackbar.make(Postcode_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<post_code_modal> call, Throwable t) {

                Log.e("Tro", "" + t);
                hideloading();
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

    @Override
    public void onClick(View v) {

        if (v == referToFriend){

            if(user_id != null && !user_id.isEmpty()){
                ComingSoon();
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }
            drawer.close();

        }else if(v == homePageTxt){
            drawer.close();
        }else if(v == profileDetails){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), MyAccount_Activity.class));
        }else if(v == favouriteNav){

            if(user_id != null && !user_id.isEmpty()){
                startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }

            drawer.close();

        }else if(v == myOrderNav){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Order_History_Activity.class));
        }else if(v == upComingOrder){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
        }else if(v == myAccountNav){
            drawer.close();
            ComingSoon();
        }else if(v == walletNavIcon){

            if(user_id != null && !user_id.isEmpty()){
                startActivity(new Intent(getApplicationContext(), Wallet_Activity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }

            drawer.close();

        }else if(v == notificationNav){


            if(user_id != null && !user_id.isEmpty()){
                startActivity(new Intent(getApplicationContext(), Notification_Activity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }
            drawer.close();

        }else if(v == perksNav){
            drawer.close();
            ComingSoon();
        }else if(v == fkPlusNav){
            drawer.close();
            ComingSoon();
        }else if(v == addressListNav){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Address_Book_Activity.class));
        }else if(v == helpNav){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Help_Activity.class));
        }else if(v == rateApp){
            drawer.close();
            showRateDialog(Postcode_Activity.this);
            // startActivity(new Intent(getApplicationContext(), Review_Activity.class));
        }else if(v == aboutNav){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Aboutus_Activity.class));
         }else if(v == allergyInfoNav){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Allergy_Activity.class));
        }else if(v == termsConditionNav){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Terms_Conditions_Activity.class));
        }else if(v == termsOfUse){
            drawer.close();
            ComingSoon();
        }else if(v == privacyPolicyNav){
            drawer.close();
            startActivity(new Intent(getApplicationContext(), Privacy_Policy_Activity.class));
        }else if(v == deteleAccountNav){
            drawer.close();
            ComingSoon();
        }else if(v == logoutNav){

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


            drawer.close();

        }else if(v == loginNav){

            drawer.close();
            startActivity(new Intent(getApplicationContext(), Login_Activity.class));


        }


    }

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
/*    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawers();
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            intent.putExtra("activity_details", "pcode");
            startActivity(intent);
            drawer.closeDrawers();
        } else if (id == R.id.nav_myaccount) {
            startActivity(new Intent(getApplicationContext(), MyAccount_Activity.class));
            drawer.closeDrawers();
        } else if (id == R.id.nav_favorite) {
            startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
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
            *//*----------------Rateing APP-------------------*//*


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

        return true;
    }*/

    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Postcode_Activity.this);
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

    public void showRateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Fusion Kitchen")
                .setMessage("If you enjoy using Fusion Kitchen app, please take a moment to rate it. Thanks for your support!")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.fusionkitchen")));
                            //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.fusionkitchen")));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.fusionkitchen")));
                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }


    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent backbtn = new Intent(Intent.ACTION_MAIN);
            backbtn.addCategory(Intent.CATEGORY_HOME);
            backbtn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backbtn);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();

    }


    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            Postcode_Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (page.getCurrentItem() < listItems.size() - 1) {
                        page.setCurrentItem(page.getCurrentItem() + 1);
                    } else
                        page.setCurrentItem(0);
                }
            });
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Postcode Activity");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                getLastLocation();

            }else {

                if (isGPSEnabled()) {  // Check GPS Location Enable Or Disable

                    Dialog deletepopup = new Dialog(Postcode_Activity.this);
                    deletepopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deletepopup.setContentView(R.layout.permission_popup);

                    AppCompatButton deletedBtn = deletepopup.findViewById(R.id.deletedBtn);
                    AppCompatButton cancelBtn = deletepopup.findViewById(R.id.cancelBtn);
                    TextView textHeadline = deletepopup.findViewById(R.id.textHeadline);
                    textHeadline.setText("Do you want to Enable App Location Permission?");

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletepopup.dismiss();
                        }
                    });

                    deletedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            deletepopup.dismiss();

                        }
                    });

                    deletepopup.getWindow().setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    deletepopup.show();
                    deletepopup.setCancelable(false);
                    deletepopup.setCanceledOnTouchOutside(false);
                    deletepopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    deletepopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    deletepopup.getWindow().setGravity(Gravity.CENTER);






                }else{
                    Dialog deletepopup = new Dialog(Postcode_Activity.this);
                    deletepopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deletepopup.setContentView(R.layout.permission_popup);

                    AppCompatButton deletedBtn = deletepopup.findViewById(R.id.deletedBtn);
                    AppCompatButton cancelBtn = deletepopup.findViewById(R.id.cancelBtn);
                    TextView textHeadline = deletepopup.findViewById(R.id.textHeadline);
                    textHeadline.setText("Do you want to Enable App Location Permission?");

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletepopup.dismiss();
                        }
                    });

                    deletedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Intent app_intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(app_intent);
                            Toast.makeText(Postcode_Activity.this,"Please enable GPS location",Toast.LENGTH_SHORT).show();
                            deletepopup.dismiss();

                        }
                    });

                    deletepopup.getWindow().setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    deletepopup.show();
                    deletepopup.setCancelable(false);
                    deletepopup.setCanceledOnTouchOutside(false);
                    deletepopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    deletepopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    deletepopup.getWindow().setGravity(Gravity.CENTER);

                      }

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void ComingSoon(){
        comeingSoon = new Dialog(Postcode_Activity.this);
        comeingSoon.requestWindowFeature(Window.FEATURE_NO_TITLE);
        comeingSoon.setContentView(R.layout.comingsoon);

        ImageView close= comeingSoon.findViewById(R.id.closepopup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comeingSoon.dismiss();
            }
        });


        comeingSoon.show();

        comeingSoon.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        comeingSoon.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //heart_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        comeingSoon.getWindow().setGravity(Gravity.CENTER);

    }


}

