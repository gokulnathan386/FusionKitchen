package com.fusionkitchen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fusionkitchen.R;
import com.fusionkitchen.InternetConnection.NetworkUtils;
import com.fusionkitchen.adapter.DashboardBannerAutoScrollAdapter;

import com.fusionkitchen.adapter.FetchFilterDetails;
import com.fusionkitchen.adapter.FetchFilterOfferDetails;
import com.fusionkitchen.adapter.FilterFetchResturtantsDetails;
import com.fusionkitchen.adapter.LocationfetchDetailsRest;
import com.fusionkitchen.adapter.MostPopularRestListAdapter;
import com.fusionkitchen.adapter.RecommendedRestListAdapter;
import com.fusionkitchen.model.FilterFetchDetails;
import com.fusionkitchen.model.dashboard.FetchFilterListModel;
import com.fusionkitchen.model.dashboard.location_fetch_details;

import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardListActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    SharedPreferences sharedptcode;
    String key_postcode;
    LinearLayout referToFriend,homePageTxt,profileDetails,favouriteNav,locationIcon;
    LinearLayout myOrderNav,upComingOrder,myAccountNav,walletNavIcon;
    LinearLayout notificationNav,perksNav,fkPlusNav,addressListNav;
    LinearLayout helpNav,rateApp,aboutNav,allergyInfoNav;
    LinearLayout termsConditionNav,termsOfUse,privacyPolicyNav,deteleAccountNav,logoutNav;
    LinearLayout loginNav, moreHideView,filterListBtn;
    LinearLayout noRestaurantsAvailable,listFilterRating,listOffers;
    DrawerLayout drawerLayout;
    SharedPreferences slogin;
    String orderTimeMin,orderDate;
    SharedPreferences.Editor sloginEditor;
    int pre_order_delivery_collection = 0;
    String user_id;
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;
    Dialog comeingSoon;
    TextView txtversionname;
    RelativeLayout internetConnection;
    public NestedScrollView getAllRestListView;
    String dateFormate,time24HourPreOrder;
    public ShimmerFrameLayout mShimmerViewContainer,shimmerFilterSearchIcon;
    ArrayList<Integer> arrayListDemo = new ArrayList<Integer>();


    private ViewPager2 viewPager;
    TextView postCodeAddress;
    FusedLocationProviderClient fusedLocationProviderClient;
    private DashboardBannerAutoScrollAdapter dashboardBannerAutoScrollAdapter;
    private List<String> imageUrls;
    String orderTime;
    private int currentPage = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private final static int REQUEST_CODE = 100;
    LinearLayout currentLocationDetails,searchRestaurantCuisine,searchIconCusion;
    LinearLayout filterListCategory;
    Dialog currentlocationpopup,filtercategoryList,preOrderPopUp;
    RecyclerView mostPopularLayout,cusinesListLayout,recommendRestList,filterList,filterOfferList;
    public RecyclerView filterLayoutDesign;
    LinearLayout cusionListView,loadingShimmer,allFilterCategoryPopUpShow;

    List<location_fetch_details.showRestaurantist> restaurantList;
    List<location_fetch_details.restaurantList> vipRestaurants = new ArrayList<>();
    List<location_fetch_details.restaurantList> nonVipRestaurants = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_list);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(DashboardListActivity.this, R.color.status_bar_color));

      //  Session Store date postcode
        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);
        key_postcode = (sharedptcode.getString("KEY_postcode", null));
       /* key_lat = (sharedptcode.getString("KEY_lat", null));
        key_lon = (sharedptcode.getString("KEY_lon", null));
        key_area = (sharedptcode.getString("KEY_area", null));
        fullUrl = (sharedptcode.getString("KEY_posturl", null));*/






        //  Session Store date Login details
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));



       /*-------------------start Internet connection is available or Not-----------------*/
        internetConnection = findViewById(R.id.internetConnection);

        Button retry = findViewById(R.id.retry);
        allFilterCategoryPopUpShow = findViewById(R.id.allFilterCategoryPopUpShow);
        filterLayoutDesign = findViewById(R.id.filterLayoutDesign);
        arrayListDemo.clear();  // Filter List view Clear


        /*-------------------End Internet connection is available or Not-----------------*/


        TimeZone tz = TimeZone.getTimeZone("Europe/London");   // Current Time and date // 24 hours
        Calendar c = Calendar.getInstance(tz);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        orderTime = year +"-" +month+"-"+day+ " " + hours+":"+min;

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        shimmerFilterSearchIcon = findViewById(R.id.shimmerFilterSearchIcon);
        loadingShimmer = findViewById(R.id.loadingShimmer);
        getAllRestListView = findViewById(R.id.getAllRestListView);
        noRestaurantsAvailable = findViewById(R.id.noRestaurantsAvailable);
        listFilterRating = findViewById(R.id.listFilterRating);
        listOffers = findViewById(R.id.listOffers);






        /*-------------------NavigationView Start-------------------------*/

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        LinearLayout accountProfile = findViewById(R.id.profileSlider);
        cusinesListLayout = findViewById(R.id.cusinesListLayout);
        recommendRestList = findViewById(R.id.recommendRestList);

        accountProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


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
        logoutNav = findViewById(R.id.logoutNav);

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

        /*-------------------NavigationView End-------------------------*/

        viewPager = findViewById(R.id.viewPager);
        currentLocationDetails = findViewById(R.id.currentLocationDetails);
        locationIcon = findViewById(R.id.locationIcon);
        searchRestaurantCuisine = findViewById(R.id.searchRestaurantCuisine);
        searchIconCusion = findViewById(R.id.searchIconCusion);
        cusionListView = findViewById(R.id.cusionListView);
        filterListCategory = findViewById(R.id.filterListCategory);
        mostPopularLayout = findViewById(R.id.mostPopularLayout);




        searchIconCusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchIconCusion.getVisibility() == View.VISIBLE){

                    searchIconCusion.setVisibility(View.GONE);
                    cusionListView.setVisibility(View.GONE);
                    searchRestaurantCuisine.setVisibility(View.VISIBLE);

                }
            }
        });

        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preOrderPopup();

            }
        });

        currentLocationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 preOrderPopup();

            }
        });




        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        locationgetshop();


      /* Start Filter List UI Design*/


        filtercategoryList= new Dialog(DashboardListActivity.this);
        filtercategoryList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filtercategoryList.setContentView(R.layout.filter_category_list);

        filterList = filtercategoryList.findViewById(R.id.filterList);
        filterOfferList = filtercategoryList.findViewById(R.id.filterOfferList);
        filterListBtn = filtercategoryList.findViewById(R.id.filterListBtn);


        filteList(filtercategoryList,filterList,filterOfferList);



        filtercategoryList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        filtercategoryList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filtercategoryList.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        filtercategoryList.getWindow().setGravity(Gravity.BOTTOM);

        filterListCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtercategoryList.show();

            }
        });

        filterListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!arrayListDemo.isEmpty() && arrayListDemo != null){
                    getAllRestListView.setVisibility(View.GONE);

                    filterLayoutDesign.setVisibility(View.GONE);
                    shimmerFilterSearchIcon.setVisibility(View.VISIBLE);
                    shimmerFilterSearchIcon.startShimmerAnimation();
                    getFilterListView(0,"MultiChooseCuisines", null,"");
                    filtercategoryList.dismiss();
                }else{
                    Toast.makeText(DashboardListActivity.this, "Please choose any one filter", Toast.LENGTH_SHORT).show();
                }
            }
        });



        /* End Filter List UI Design*/



        if (NetworkUtils.isNetworkAvailable(this)) {
            internetConnection.setVisibility(View.GONE);
        } else {
            internetConnection.setVisibility(View.VISIBLE);
            loadingShimmer.setVisibility(View.GONE);
            mShimmerViewContainer.setVisibility(View.GONE);
            mShimmerViewContainer.stopShimmerAnimation();
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isNetworkAvailable(DashboardListActivity.this)) {
                    internetConnection.setVisibility(View.GONE);
                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.startShimmerAnimation();
                    locationgetshop();
                }
            }
        });

        allFilterCategoryPopUpShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allFilterCategoryPopUpShow.getBackground().setTint(getResources().getColor(R.color.ListSelector));
                listFilterRating.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));
                listOffers.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));

                filtercategoryList.show();

            }
        });

        listFilterRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allFilterCategoryPopUpShow.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));
                listFilterRating.getBackground().setTint(getResources().getColor(R.color.ListSelector));
                listOffers.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));


                getAllRestListView.setVisibility(View.GONE);

                filterLayoutDesign.setVisibility(View.GONE);
                shimmerFilterSearchIcon.setVisibility(View.VISIBLE);
                shimmerFilterSearchIcon.startShimmerAnimation();

                getFilterListView(3,"MultiChooseFilter", null,"");
            }
        });

        listOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allFilterCategoryPopUpShow.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));
                listFilterRating.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));
                listOffers.getBackground().setTint(getResources().getColor(R.color.ListSelector));

                getAllRestListView.setVisibility(View.GONE);

                filterLayoutDesign.setVisibility(View.GONE);
                shimmerFilterSearchIcon.setVisibility(View.VISIBLE);
                shimmerFilterSearchIcon.startShimmerAnimation();

                getFilterListView(1,"MultiChooseFilter", null,"");
            }
        });

    }

    private void filteList(Dialog filtercategoryList, RecyclerView filterList, RecyclerView filterOfferList) {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("post_code", key_postcode);
            jsonObj.put("order_mode", pre_order_delivery_collection);
            jsonObj.put("order_time", orderTime);
            jsonObj.put("customer_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, String.valueOf(jsonObj));
        ApiInterface apiService = ApiClient.getInstance().getClientt().create(ApiInterface.class);
        Call<FetchFilterListModel> call = apiService.getFilterList(requestBody);

        Log.d("Filter-params->", " " + jsonObj);

        call.enqueue(new Callback<FetchFilterListModel>() {
            @Override
            public void onResponse(Call<FetchFilterListModel> call, Response<FetchFilterListModel> response) {
                int statusCode = response.code();

                Log.d("List_Page_API", new Gson().toJson(response.body()));

                if (statusCode == 200) {

                    if (response.body().getStatus() == true) {


                        FetchFilterOfferDetails filterOfferadapter = new FetchFilterOfferDetails(DashboardListActivity.this,response.body().getData().getOffer(),filtercategoryList);
                        filterOfferList.setHasFixedSize(true);
                        filterOfferList.setLayoutManager(new LinearLayoutManager(DashboardListActivity.this,LinearLayoutManager.VERTICAL, false));
                        filterOfferList.setAdapter(filterOfferadapter);


                        FetchFilterDetails filteradapter = new FetchFilterDetails(DashboardListActivity.this,response.body().getData().getGetAllActiveCuisine());
                        filterList.setHasFixedSize(true);
                        filterList.setLayoutManager(new LinearLayoutManager(DashboardListActivity.this,LinearLayoutManager.VERTICAL, false));
                        filterList.setAdapter(filteradapter);


                    }
                }
            }


            @Override
            public void onFailure(Call<FetchFilterListModel> call, Throwable t) {

                Log.d("Filter List", "" + t);
            }

        });




    }

    private void locationgetshop() {

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("post_code", key_postcode);
            jsonObj.put("order_mode", pre_order_delivery_collection);
            jsonObj.put("order_time", orderTime);
            jsonObj.put("customer_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, String.valueOf(jsonObj));
        ApiInterface apiService = ApiClient.getInstance().getClientt().create(ApiInterface.class);
        Call<location_fetch_details> call = apiService.getlocationfetchdetails(requestBody);

        Log.d("adjbvdbvjdbv", " " + jsonObj);

        call.enqueue(new Callback<location_fetch_details>() {
            @Override
            public void onResponse(Call<location_fetch_details> call, Response<location_fetch_details> response) {
                int statusCode = response.code();

                Log.d("List_Page_API", new Gson().toJson(response.body()));

                if (statusCode == 200) {

                    if (response.body().getSTATUS() == true) {

                        LocationfetchDetailsRest adapter = new LocationfetchDetailsRest(DashboardListActivity.this, response.body().getDate().getGetAllActiveCuisine());
                        cusinesListLayout.setHasFixedSize(true);
                        cusinesListLayout.setLayoutManager(new LinearLayoutManager(DashboardListActivity.this,LinearLayoutManager.HORIZONTAL, false));
                        cusinesListLayout.setAdapter(adapter);


                        dashboardBannerAutoScrollAdapter = new DashboardBannerAutoScrollAdapter(DashboardListActivity.this, response.body().getDate().getOfferBannerDetails());
                        viewPager.setAdapter(dashboardBannerAutoScrollAdapter);

                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (currentPage == response.body().getDate().getOfferBannerDetails().size()) {
                                            currentPage = 0;
                                        }
                                        viewPager.setCurrentItem(currentPage++, true);
                                    }
                                });
                            }
                        }, 3000, 3000);

                         for (int i = 0; i<response.body().getDate().getRestaurantList().size() ; i++){

                             if (response.body().getDate().getRestaurantList().get(i).getRestaurant().getVip()==true){

                                 vipRestaurants.add(response.body().getDate().getRestaurantList().get(i).getRestaurant());

                             }else{

                                 nonVipRestaurants.add(response.body().getDate().getRestaurantList().get(i).getRestaurant());

                             }

                         }

                        RecommendedRestListAdapter recommendList = new RecommendedRestListAdapter(DashboardListActivity.this, nonVipRestaurants);
                        recommendRestList.setHasFixedSize(true);
                        recommendRestList.setLayoutManager(new LinearLayoutManager(DashboardListActivity.this,LinearLayoutManager.HORIZONTAL, false));
                        recommendRestList.setAdapter(recommendList);

                        MostPopularRestListAdapter mostPopularList = new MostPopularRestListAdapter(DashboardListActivity.this, nonVipRestaurants);
                        mostPopularLayout.setHasFixedSize(true);
                        mostPopularLayout.setLayoutManager(new LinearLayoutManager(DashboardListActivity.this,LinearLayoutManager.VERTICAL, false));
                        mostPopularLayout.setAdapter(mostPopularList);

                        loadingShimmer.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.setVisibility(View.GONE);
                        mShimmerViewContainer.stopShimmerAnimation();

                    }
                }
            }


            @Override
            public void onFailure(Call<location_fetch_details> call, Throwable t) {

                Log.d("dasboarderror", "location type : " + t);
            }

        });



    }


    public void getFilterListView(int adapterfilterid, String paramsChoose, ArrayList<Integer> listviewcuisine,String bannerid) {


        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("post_code", key_postcode);
            jsonObj.put("order_mode", pre_order_delivery_collection);
            jsonObj.put("order_time", orderTime);
            jsonObj.put("customer_id", user_id);

            if(paramsChoose.equalsIgnoreCase("MultiChooseCuisines")){
                jsonObj.put("cuisines", arrayListDemo);
            }else if(paramsChoose.equalsIgnoreCase("MultiChooseFilter")){
                jsonObj.put("filter", adapterfilterid);
            }else if(paramsChoose.equalsIgnoreCase("ListPageCuisines")){
                jsonObj.put("cuisines", listviewcuisine);
            }else if(paramsChoose.equalsIgnoreCase("BannerFilter")){
                jsonObj.put("offerBanner", bannerid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, String.valueOf(jsonObj));
        ApiInterface apiService = ApiClient.getInstance().getClientt().create(ApiInterface.class);
        Call<FilterFetchDetails> call = apiService.getFetchFilterList(requestBody);

        Log.d("fileterapireponse", " " + jsonObj);

        call.enqueue(new Callback<FilterFetchDetails>() {
            @Override
            public void onResponse(Call<FilterFetchDetails> call, Response<FilterFetchDetails> response) {
                int statusCode = response.code();

                Log.d("Get-Fetch-Filter", new Gson().toJson(response.body()));

                if (statusCode == 200) {

                    if (response.body().getStatus() == true) {

                        Log.d("FilterListSize","" + response.body().getData().getRestaurantList().size());

                        if(response.body().getData().getRestaurantList().size() != 0){

                            FilterFetchResturtantsDetails filterFetchResturtantsDetails = new FilterFetchResturtantsDetails(DashboardListActivity.this, response.body().getData().getRestaurantList());
                            filterLayoutDesign.setHasFixedSize(true);
                            filterLayoutDesign.setLayoutManager(new LinearLayoutManager(DashboardListActivity.this,LinearLayoutManager.VERTICAL, false));
                            filterLayoutDesign.setAdapter(filterFetchResturtantsDetails);
                            filterLayoutDesign.setVisibility(View.VISIBLE);
                            shimmerFilterSearchIcon.setVisibility(View.GONE);
                            mShimmerViewContainer.stopShimmerAnimation();

                        }else{
                            Log.d("errorelse","" + response.body().getData().getRestaurantList().size());
                            getAllRestListView.setVisibility(View.GONE);
                            recommendRestList.setVisibility(View.GONE);
                            noRestaurantsAvailable.setVisibility(View.VISIBLE);

                        }

                    }else{
                        Log.d("elsemnsc","" + response.body().getData().getRestaurantList().size());
                        getAllRestListView.setVisibility(View.GONE);
                        recommendRestList.setVisibility(View.GONE);
                        noRestaurantsAvailable.setVisibility(View.VISIBLE);

                    }

                }
            }


            @Override
            public void onFailure(Call<FilterFetchDetails> call, Throwable t) {

                Log.d("dasboarderror", "location type : " + t);
                Log.d("sdjbsjcbjsdsd","dvbjdfvmdnfmdnfv");

                getAllRestListView.setVisibility(View.GONE);
                recommendRestList.setVisibility(View.GONE);
                noRestaurantsAvailable.setVisibility(View.VISIBLE);
            }

        });



    }


    private void preOrderPopup() {

        preOrderPopUp = new Dialog(DashboardListActivity.this);
        preOrderPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        preOrderPopUp.setContentView(R.layout.pre_order_design);

        TextView changeAddressTxt = preOrderPopUp.findViewById(R.id.changeAddressTxt);
        RadioGroup deliveryPickupRadioGroup = preOrderPopUp.findViewById(R.id.deliveryPickupRadioGroup);
        RadioButton radioDelivery = preOrderPopUp.findViewById(R.id.radioDelivery);
        RadioButton radioPickUp = preOrderPopUp.findViewById(R.id.radioPickUp);
        TextView confirmBtn = preOrderPopUp.findViewById(R.id.confirmBtn);
        Spinner sevenDays =preOrderPopUp.findViewById(R.id.sevenDays);
        Spinner hoursMin =preOrderPopUp.findViewById(R.id.hoursMin);
        postCodeAddress =preOrderPopUp.findViewById(R.id.postCodeAddress);

        LinearLayout amPmTxt = preOrderPopUp.findViewById(R.id.amPmTxt);
        TextView sessionAMPM = preOrderPopUp.findViewById(R.id.sessionAMPM);
        amPmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionAMPM.getText().toString().equalsIgnoreCase("AM")){
                    sessionAMPM.setText("PM");
                }else{
                    sessionAMPM.setText("AM");
                }

            }
        });



        ArrayList<String> dateDay = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        for (int i = 0; i < 7; i++) {

            if(i==0){
                dateDay.add("Today");
            }else if(i==1){
                dateDay.add("Tomorrow");
            }else{
                Calendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, i);
                String day = sdf.format(calendar.getTime());
                dateDay.add(day);
            }


        }



            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.my_selected_item, dateDay);
            dataAdapter.setDropDownViewResource(R.layout.my_dropdown_item);

            sevenDays.setAdapter(dataAdapter);
            sevenDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item_position = String.valueOf(id);
                    int positonInt = Integer.valueOf(item_position);
                     orderDate = dateDay.get(positonInt);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        ArrayList<String> hoursmin = new ArrayList<String>();
        //hours.add("hrs");
        for(int i=1;i<=12;i++){

            int num = 15;
            for(int j = 1; j <= 4; j++) {

                if(i<=9){

                    if(j==4){

                        int addplusone = i + 1;

                        if(addplusone == 10){
                            hoursmin.add(addplusone +" : " +"00");
                        }else{
                            hoursmin.add("0"+ addplusone +" : " +"00");
                        }

                    }else{

                        hoursmin.add("0"+ i +" : " +num * j);

                    }

                }else{

                    if(j==4){

                        int addplusone = i + 1;

                        if(addplusone == 13){
                            hoursmin.add(0, "01"+" : " + "00");
                        }else{
                            hoursmin.add(addplusone +" : " + "00");
                        }

                    }else{

                        hoursmin.add(i +" : " + num * j);
                    }

                }

            }

        }


        ArrayAdapter<String> hoursMinAdapter = new ArrayAdapter<String>(this,R.layout.my_selected_item, hoursmin);
        hoursMinAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        hoursMin.setAdapter(hoursMinAdapter);
        hoursMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_position = String.valueOf(id);
                int positonInt = Integer.valueOf(item_position);
                orderTimeMin = hoursmin.get(positonInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String inputDateStr = orderDate;
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    try {
                        Date inputDate = inputFormat.parse(inputDateStr);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateFormate = outputFormat.format(inputDate);

                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }


                    String time12HourPreOrder = orderTimeMin + " " + sessionAMPM.getText().toString();

                    SimpleDateFormat inputFormat12HourPreOrder = new SimpleDateFormat("hh : mm a");
                    SimpleDateFormat outputFormat24HourPreOrder = new SimpleDateFormat("HH:mm");

                    try {
                        Date date12HourPreOrder = inputFormat12HourPreOrder.parse(time12HourPreOrder);
                        time24HourPreOrder = outputFormat24HourPreOrder.format(date12HourPreOrder);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Log.d("xkjcjdbdbcbdfjbjdbjdf"," " + dateFormate + " " + time24HourPreOrder);




                    SharedPreferences.Editor getmenudata = sharedptcode.edit();
                //   getmenudata.putString("KEY_posturl", "/location/" + Menu_Url);
                     getmenudata.putString("KEY_postcode",postCodeAddress.getText().toString());
                /*  getmenudata.putString("KEY_area", menu_share_area);
                    getmenudata.putString("KEY_address", menu_share_address);
                    getmenudata.putString("KEY_lat", lat_menu_share);
                    getmenudata.putString("KEY_lon", log_menu_share);*/
                    getmenudata.commit();

                    key_postcode = postCodeAddress.getText().toString();
                    orderTime = dateFormate + " " + time24HourPreOrder;

                   // mShimmerViewContainer.setVisibility(View.VISIBLE);
                  //  mShimmerViewContainer.startShimmerAnimation();
                    getAllRestListView.setVisibility(View.GONE);

                    filterLayoutDesign.setVisibility(View.GONE);
                    shimmerFilterSearchIcon.setVisibility(View.VISIBLE);
                    shimmerFilterSearchIcon.startShimmerAnimation();
                    getFilterListView(0,"PreOrderFilter", null,"");

                    preOrderPopUp.dismiss();
                }
            });

            deliveryPickupRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioDelivery:

                        Drawable radiodeliverychecked = getResources().getDrawable(R.drawable.fi_preorder_delivery_icon);
                        radiodeliverychecked.setBounds(0, 0, 40, 40);
                        radioDelivery.setCompoundDrawables(radiodeliverychecked, null, null, null);


                        Drawable radiopickunchecked = getResources().getDrawable(R.drawable.fi_pickup_icon);
                        radiopickunchecked.setBounds(0, 0, 40, 40);
                        radioPickUp.setCompoundDrawables(radiopickunchecked, null, null, null);

                        pre_order_delivery_collection = 0;

                        break;

                    case R.id.radioPickUp:

                        Drawable radiopickchecked = getResources().getDrawable(R.drawable.pickup_checked_icon);
                        radiopickchecked.setBounds(0, 0, 40, 40);
                        radioPickUp.setCompoundDrawables(radiopickchecked, null, null, null);


                        Drawable radiodeliveryunchecked = getResources().getDrawable(R.drawable.bike_unchecked_icon);
                        radiodeliveryunchecked.setBounds(0, 0, 40, 40);
                        radioDelivery.setCompoundDrawables(radiodeliveryunchecked, null, null, null);

                        pre_order_delivery_collection = 1;

                        break;
                }


            }
        });

        changeAddressTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePostCodeLocation();
            }
        });


        preOrderPopUp.show();
        preOrderPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        preOrderPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        preOrderPopUp.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        preOrderPopUp.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void changePostCodeLocation() {

        currentlocationpopup= new Dialog(DashboardListActivity.this);
        currentlocationpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        currentlocationpopup.setContentView(R.layout.current_location_popup);

        LinearLayout addAddressCurrentLocation = currentlocationpopup.findViewById(R.id.addAddressCurrentLocation);
        LinearLayout manual_search_icon = currentlocationpopup.findViewById(R.id.manual_search_icon);
        LinearLayout myCurrentLocation = currentlocationpopup.findViewById(R.id.myCurrentLocation);
        EditText manuallyEnterPostCode = currentlocationpopup.findViewById(R.id.manuallyEnterPostCode);
        LinearLayout post_code_check = currentlocationpopup.findViewById(R.id.post_code_check);

        post_code_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCodeAddress.setText(manuallyEnterPostCode.getText().toString());
                currentlocationpopup.dismiss();
            }
        });


        myCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        addAddressCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(manual_search_icon.getVisibility() == View.VISIBLE){
                    manual_search_icon.setVisibility(View.GONE);
                }else{
                    manual_search_icon.setVisibility(View.VISIBLE);
                }

            }
        });


        currentlocationpopup.show();
        currentlocationpopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        currentlocationpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        currentlocationpopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        currentlocationpopup.getWindow().setGravity(Gravity.CENTER);

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
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DashboardListActivity.this);
                    }

                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        try {
                                            Geocoder geocoder = new Geocoder(DashboardListActivity.this, Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            Log.d("LocationAddress", "Lattitude --->" + addresses.get(0).getLatitude());
                                            Log.d("LocationAddress", "Longitude --->" + addresses.get(0).getLongitude());
                                            Log.d("LocationAddress", "Address --->" + addresses.get(0).getAddressLine(0));
                                            Log.d("LocationAddress", "City --->" + addresses.get(0).getLocality());
                                            Log.d("LocationAddress", "Country --->" + addresses.get(0).getCountryName());
                                            Log.d("LocationAddress", "Postcode --->" +addresses.get(0).getPostalCode());
                                            postCodeAddress.setText(addresses.get(0).getPostalCode());
                                            currentlocationpopup.dismiss();


                                        } catch (IOException e) {
                                            Log.d("LocationExceptionHandle", "Lattitude --->" + e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(DashboardListActivity.this, "Location not available.", Toast.LENGTH_SHORT).show();
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
                                resolvable.startResolutionForResult(DashboardListActivity.this, REQUEST_CHECK_SETTINGS);
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

        ActivityCompat.requestPermissions(DashboardListActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getLastLocation();

            }else {

                if (isGPSEnabled()) {  // Check GPS Location Enable Or Disable

                    Dialog deletepopup = new Dialog(DashboardListActivity.this);
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
                    Dialog deletepopup = new Dialog(DashboardListActivity.this);
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
                            Toast.makeText(DashboardListActivity.this,"Please enable GPS location",Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {

        if (v == referToFriend){

            if(user_id != null && !user_id.isEmpty()){
                ComingSoon();
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }
            drawerLayout.close();

        }else if(v == homePageTxt){
            drawerLayout.close();
        }else if(v == profileDetails){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), MyAccount_Activity.class));
        }else if(v == favouriteNav){

            if(user_id != null && !user_id.isEmpty()){
                startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }

            drawerLayout.close();

        }else if(v == myOrderNav){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Order_History_Activity.class));
        }else if(v == upComingOrder){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
        }else if(v == myAccountNav){
            drawerLayout.close();
            ComingSoon();
        }else if(v == walletNavIcon){

            if(user_id != null && !user_id.isEmpty()){
                startActivity(new Intent(getApplicationContext(), Wallet_Activity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }

            drawerLayout.close();

        }else if(v == notificationNav){


            if(user_id != null && !user_id.isEmpty()){
                startActivity(new Intent(getApplicationContext(), Notification_Activity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }
            drawerLayout.close();

        }else if(v == perksNav){
            drawerLayout.close();
            ComingSoon();
        }else if(v == fkPlusNav){
            drawerLayout.close();
            ComingSoon();
        }else if(v == addressListNav){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Address_Book_Activity.class));
        }else if(v == helpNav){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Help_Activity.class));
        }else if(v == rateApp){
            drawerLayout.close();
            showRateDialog(DashboardListActivity.this);
            // startActivity(new Intent(getApplicationContext(), Review_Activity.class));
        }else if(v == aboutNav){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Aboutus_Activity.class));
        }else if(v == allergyInfoNav){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Allergy_Activity.class));
        }else if(v == termsConditionNav){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Terms_Conditions_Activity.class));
        }else if(v == termsOfUse){
            drawerLayout.close();
            ComingSoon();
        }else if(v == privacyPolicyNav){
            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Privacy_Policy_Activity.class));
        }else if(v == deteleAccountNav){
            drawerLayout.close();
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
                drawerLayout.closeDrawers();

            } catch (Exception ex) {
                Toast.makeText(DashboardListActivity.this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            }


            drawerLayout.close();

        }else if(v == loginNav){

            drawerLayout.close();
            startActivity(new Intent(getApplicationContext(), Login_Activity.class));


        }


    }

    public void ComingSoon(){

        comeingSoon = new Dialog(DashboardListActivity.this);
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
        comeingSoon.getWindow().setGravity(Gravity.CENTER);

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
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.fusionkitchen")));
                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }

    public void CheckLogin() {
        if (slogin == null)
            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);

          String login_status = slogin.getString("login_key_status", "");

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


    public void FilterCheckBoxAdapter(String id,String str) {

        if(str.equalsIgnoreCase("add")){
            arrayListDemo.add(Integer.valueOf(id));
        }else{
            arrayListDemo.remove(Integer.valueOf(id));
        }

    }

    public void onBackPressed() {


       if (filterLayoutDesign.getVisibility() == View.VISIBLE) {

           filterLayoutDesign.setVisibility(View.GONE);
           getAllRestListView.setVisibility(View.VISIBLE);
           allFilterCategoryPopUpShow.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));
           listFilterRating.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));
           listOffers.getBackground().setTint(getResources().getColor(R.color.ListUnSelector));

       }else{

           if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
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

       }

    }




}