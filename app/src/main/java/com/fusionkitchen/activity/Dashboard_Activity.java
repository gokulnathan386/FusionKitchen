package com.fusionkitchen.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import com.fusionkitchen.adapter.DashboardListViewAdapter;
import com.fusionkitchen.adapter.DashboardMostPopularAdapter;
import com.fusionkitchen.adapter.EatListAdapter;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.DashboardListViewModel;
import com.fusionkitchen.model.DashboardMostPopularModel;
import com.fusionkitchen.model.EatListPostelModel;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.DashboardSearchResultList;
import com.fusionkitchen.adapter.DashboardSearchclientList;
import com.fusionkitchen.check_internet.Internet_connection_checking;

import com.fusionkitchen.model.home_model.location_type_modal;
import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.model.home_model.serachgetshop_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lincoln on 05/01/21.
 * Post code api url and php session id store process
 */
public class Dashboard_Activity extends AppCompatActivity {

    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    HttpUrl baseUrl;

    /*---------------------------Session Manager Class----------------------------------------------------*/
    // Session Manager Class
    //  SessionManager session;

    /*---------------------------XML ID Call----------------------------------------------------*/
    TextView url_postcode, notakeaway, notakeawayone;
    LinearLayout notakeway_layout;
    String fullUrl;
    RecyclerView restaurants_view, search_restaurants_view;
    LinearLayout change_postcode, show_offer_page, filter_layout, clear_list_layout,linearLayout_backbtn;
    private Context mContext = Dashboard_Activity.this;
    CardView card_view_search_client;
    EditText editTextSearchclient;
    ImageView search_colse;
    AppCompatButton go_back, remove_cart;
    String clientworking;

    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*---------------------------loaderviewlibrary----------------------------------------------------*/
    private ShimmerFrameLayout mShimmerViewContainer;

    /*---------------------------NO TAKEAWAYS----------------------------------------------------*/
    String[] separated;

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;
    Dialog warningdialog;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, menuurlpath;

    SharedPreferences sharedpreferences,morepopup;
    public static final String MyPREFERENCES = "MyPrefs_extra";

    /*--------------------------Login postcode save local------------------------*/
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    String key_postcode, key_lat, key_lon, key_area;

    /*--------------------------------RecyclerView-----------------------------------*/
    RecyclerView Enter_your_RecyclerView,Most_Popular_Listview;
    private DashboardListViewAdapter dashboardListViewAdapter;
    private List<DashboardListViewModel> dashboardListViewModels  = new ArrayList<>();

    /*--------------------------------RecyclerView-----------------------------------*/

    private DashboardMostPopularAdapter dashboardMostPopularAdapter;
    private List<DashboardMostPopularModel> dashboardMostPopularModel  = new ArrayList<>();
    LinearLayout dynamic_and_static_listview;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_dashboardnew);


        baseUrl  =ApiClient.getInstance().getClient().baseUrl();


      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);

       /* ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0d353d"));
        actionBar.setBackgroundDrawable(colorDrawable);*/

        /*---------------------------Session Manager Class----------------------------------------------------*/
     /*   session = new SessionManager(getApplicationContext());
        session.checkLogin();*/
        // cookies = CookieManager.getInstance().getCookie("cookie");//"SK11-Macclesfield";//

        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Dashboard_Activity.this.MODE_PRIVATE);

        /*---------------------------XML ID Call----------------------------------------------------*/
        url_postcode = findViewById(R.id.url_postcode);
        restaurants_view = findViewById(R.id.restaurants_view);
        search_restaurants_view = findViewById(R.id.search_restaurants_view);
        notakeaway = findViewById(R.id.notakeaway);
        notakeawayone = findViewById(R.id.notakeawayone);
        notakeway_layout = findViewById(R.id.notakeway_layout);
        change_postcode = findViewById(R.id.change_postcode);
        show_offer_page = findViewById(R.id.show_offer_page);
        filter_layout = findViewById(R.id.filter_layout);
        card_view_search_client = findViewById(R.id.card_view_search_client);
        editTextSearchclient = findViewById(R.id.editTextSearchclient);
        search_colse = findViewById(R.id.search_colse);
        clear_list_layout = findViewById(R.id.clear_list_layout);
        go_back = findViewById(R.id.go_back);
        remove_cart = findViewById(R.id.remove_cart);
        linearLayout_backbtn = findViewById(R.id.linearLayout_backbtn);
        dynamic_and_static_listview = findViewById(R.id.dynamic_and_static_listview);

        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Dashboard_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(Dashboard_Activity.this);
        }

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Dashboard_Activity.this);
        getContactsCount();

        /*---------------Remove cart popup--------------------*/
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_list_layout.setVisibility(View.GONE);
            }
        });

        morepopup = getSharedPreferences("PREFS_MOREINFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = morepopup.edit();
        editor.putString("More_info", "Moreinfo-popup");
        editor.commit();


        remove_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_list_layout.setVisibility(View.GONE);
                SQLDBHelper dbHelper;
                dbHelper = new SQLDBHelper(Dashboard_Activity.this);
                dbHelper.deleteAll();

                Intent intentthir = new Intent(Dashboard_Activity.this, Item_Menu_Activity.class);
                intentthir.putExtra("menuurlpath", menuurlpath);
                intentthir.putExtra("reloadback", "1");
                startActivity(intentthir);
            }
        });
        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);

        if (slogin.getString("login_key_cid", null) != null) {
            user_id = (slogin.getString("login_key_cid", null));
        } else {
            user_id = "0";
        }
        /*---------------------------loaderviewlibrary----------------------------------------------------*/
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();

        //   ordertypecheck(0);
        /*---------------------------get api URL type values----------------------------------------------------*/

        // get user data from session
      /*  HashMap<String, String> user = session.getUserDetails();
        fullUrl = user.get(SessionManager.KEY_posturl);
        locationgetshop(user.get(SessionManager.KEY_postcode), user.get(SessionManager.KEY_lat), user.get(SessionManager.KEY_lon), "0", user.get(SessionManager.KEY_area), fullUrl);
*/

        /*--------------------------Login postcode save local------------------------*/
        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);
        key_postcode = (sharedptcode.getString("KEY_postcode", null));
        key_lat = (sharedptcode.getString("KEY_lat", null));
        key_lon = (sharedptcode.getString("KEY_lon", null));
        key_area = (sharedptcode.getString("KEY_area", null));
        fullUrl = (sharedptcode.getString("KEY_posturl", null));

        Log.e("localstore1", "" + key_postcode);
        Log.e("localstore2", "" + key_lat);
        Log.e("localstore3", "" + key_lon);
        Log.e("localstore4", "" + key_area);
        Log.e("localstore5", "" + fullUrl);

        /*-----------------get Client List---------------------*/
        locationgetshop(key_postcode, key_lat, key_lon, "0", key_area, fullUrl, user_id);

        // locationgetshop(user.get(SessionManager.KEY_postcode), user.get(SessionManager.KEY_lat), user.get(SessionManager.KEY_lon), "0", user.get(SessionManager.KEY_area), fullUrl);

        /*---------------------------BottomNavigationView----------------------------------------------------*/

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().setGroupCheckable(0, false, true);

        if (cursor != 0) {
            bottomNav.getOrCreateBadge(R.id.home_card).setNumber(cursor);
        }

       /* BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.home_card);
        badge.setNumber(cursor);
        badge.setBackgroundColor(R.color.app_color);*/
        bottomNav.getMenu().getItem(4).setIcon(R.drawable.menufavourite);
        bottomNav.getMenu().getItem(4).setTitle("Favourite");
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
                    case R.id.home_search:
                        card_view_search_client.setVisibility(View.VISIBLE);
                        editTextSearchclient.requestFocus();
                        //   bottomNav.getMenu().findItem(R.id.home_search).setChecked(true);
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
                            Intent intent = new Intent(getApplicationContext(), Favourite_Activity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            intent.putExtra("activity_details", "myfavourite");
                            startActivity(intent);
                        }
                      /*  Intent intent = new Intent(getApplicationContext(), Favourite_Activity.class);
                        startActivity(intent);*/
                        // item.setIcon(R.drawable.icon4);
                        /*if (user_id != null && !user_id.isEmpty()) {
                            Intent intentcard = new Intent(getApplicationContext(), MyAccount_Activity.class);
                            startActivity(intentcard);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            intent.putExtra("activity_details", "myaccount");
                            startActivity(intent);
                        }*/
                        break;
                }
                return true;
            }
        });

        /*-----------------------------Back Button---------------------*/
        linearLayout_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*--------------------------Seach Shop-----------------------------*/

        editTextSearchclient.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clientworking = s.toString();
                if (clientworking.length() >= 2) {
                    restaurants_view.setVisibility(View.GONE);
                    Enter_your_RecyclerView.setVisibility(View.GONE);
                   // Most_Popular_Listview.setVisibility(View.GONE);
                    dynamic_and_static_listview.setVisibility(View.GONE);
                    search_restaurants_view.setVisibility(View.VISIBLE);
                    serachgetshop(clientworking, key_lat, key_lon, key_area, key_postcode, user_id);
                    //
                    //
                    // (clientworking, user.get(SessionManager.KEY_lat), user.get(SessionManager.KEY_lon), user.get(SessionManager.KEY_area), user.get(SessionManager.KEY_postcode));

                } else if (clientworking.length() == 0) {
                    View view = Dashboard_Activity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    card_view_search_client.setVisibility(View.GONE);
                    restaurants_view.setVisibility(View.VISIBLE);
                    Enter_your_RecyclerView.setVisibility(View.VISIBLE);
                  //  Most_Popular_Listview.setVisibility(View.VISIBLE);
                    dynamic_and_static_listview.setVisibility(View.VISIBLE);
                    search_restaurants_view.setVisibility(View.GONE);
                    notakeway_layout.setVisibility(View.GONE);

                    finish();
                    startActivity(getIntent());

                } else if (clientworking.length() == 1) {
                    restaurants_view.setVisibility(View.VISIBLE);
                    Enter_your_RecyclerView.setVisibility(View.VISIBLE);
                    //Most_Popular_Listview.setVisibility(View.VISIBLE);
                    dynamic_and_static_listview.setVisibility(View.VISIBLE);
                    search_restaurants_view.setVisibility(View.GONE);
                    notakeway_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        search_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = Dashboard_Activity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                editTextSearchclient.setText("");
                card_view_search_client.setVisibility(View.GONE);
                restaurants_view.setVisibility(View.VISIBLE);
                search_restaurants_view.setVisibility(View.GONE);
                notakeway_layout.setVisibility(View.GONE);
                finish();
                startActivity(getIntent());
            }
        });

        /*---------------------------Change Postcode----------------------------------------------------*/

        change_postcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Postcode_Activity.class);
                startActivity(i);
                //  session.logoutUser();
            }
        });
        show_offer_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Show_Offer_Activity.class));
            }
        });

        filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Item_Filters.class));
            }
        });

        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message-menuurlpath"));

        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverreload, new IntentFilter("custom-message-reloadlist"));



  /*-----------------------------------------Recyclerview List--------------------------------------------*/

        Enter_your_RecyclerView = findViewById(R.id.Enter_your_RecyclerView);
        dashboardListViewAdapter = new DashboardListViewAdapter(dashboardListViewModels,Dashboard_Activity.this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        Enter_your_RecyclerView.setLayoutManager(manager);
        Enter_your_RecyclerView.setAdapter(dashboardListViewAdapter);
        DashboardDataList();


  /*----------------------------------RecyclerView List---------------------------------------------------*/
        Most_Popular_Listview = findViewById(R.id.Most_Popular_Listview);
        DashboardMostPopular();

    }

    private void DashboardMostPopular() {

        dashboardMostPopularAdapter = new DashboardMostPopularAdapter(dashboardMostPopularModel,Dashboard_Activity.this);
        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        Most_Popular_Listview.setLayoutManager(manager1);
        Most_Popular_Listview.setAdapter(dashboardMostPopularAdapter);

        //https://www.api.fusionkitchen.co.uk/loadPopularRestaurants

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,baseUrl+"loadPopularRestaurants",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONObject getdata =jsonobject.getJSONObject("data");
                            JSONArray most_popular_list = getdata.getJSONArray("popular_restaurants");



                           for (int i = 0; i < most_popular_list.length(); i++) {
                                JSONObject object = most_popular_list.getJSONObject(i);
                               DashboardMostPopularModel popularlist = new DashboardMostPopularModel(
                                            object.getString("name"),
                                            object.getString("area"),
                                            object.getString("rating_average"),
                                            object.getString("takeawaystatus"),
                                            object.getString("discount"),
                                            object.getString("image_url"),
                                            object.getString("postcode"),
                                            object.getString("address_location"),
                                            object.getString("menupageurl")

                                );
                               dashboardMostPopularModel.add(popularlist);
                            }

                            dashboardMostPopularAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
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

    private void DashboardDataList() {
        DashboardListViewModel data = new DashboardListViewModel(R.drawable.image1, "1");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image2, "2");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image3, "3");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image4, "4");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image5, "5");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image6, "6");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image7, "7");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image8, "8");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image9, "9");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image10, "10");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image11, "11");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image12, "12");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image13, "13");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image14, "14");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image15, "15");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image16, "16");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image17, "17");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image18, "18");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image19, "19");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image20, "20");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image21, "21");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image22, "22");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image23, "23");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image24, "24");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image25, "25");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image26, "26");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image27, "27");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image28, "28");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image29, "29");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image30, "30");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image31, "31");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image32, "32");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image33, "33");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image34, "34");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image35, "35");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image36, "36");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image37, "37");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image38, "38");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image39, "39");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image40, "40");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image41, "41");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image42, "42");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image43, "43");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image44, "44");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image45, "45");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image46, "46");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image47, "47");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image48, "48");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image49, "49");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image50, "50");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image51, "51");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image52, "52");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image53, "53");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image54, "54");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image55, "55");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image56, "56");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image57, "57");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image58, "58");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image59, "59");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image60, "60");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image61, "61");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image62, "62");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image63, "63");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image64, "64");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image65, "65");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image66, "66");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image67, "67");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image68, "68");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image69, "69");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image70, "70");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image71, "71");
        dashboardListViewModels.add(data);
        data = new DashboardListViewModel(R.drawable.image72, "72");
        dashboardListViewModels.add(data);


    }

    public BroadcastReceiver mMessageReceiverreload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (intent.getStringExtra("reloadlist").equalsIgnoreCase("1")) {
                locationgetshop(key_postcode, key_lat, key_lon, "0", key_area, fullUrl, user_id);
            } else {
                serachgetshop(clientworking, key_lat, key_lon, key_area, key_postcode, user_id);
            }
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




               /* Dashboard_Activity.ViewwarningDialog alert = new Dashboard_Activity.ViewwarningDialog();
                alert.showwarningDialog(Dashboard_Activity.this);*/

                clear_list_layout.setVisibility(View.VISIBLE);

            } else {


                Intent intenttwo = new Intent(Dashboard_Activity.this, Item_Menu_Activity.class);
                intenttwo.putExtra("menuurlpath", menuurlpath);
                intenttwo.putExtra("reloadback", "1");
                startActivity(intenttwo);

            }


        }
    };

    /*---------------------------get api URL type values----------------------------------------------------*/
    private void locationgetshop(String postcode, String lat, String lng, String otp, String area, String fullUrl, String struseris) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", postcode);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("order_type", otp);
        params.put("area", area);
        params.put("user_id", struseris);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<location_type_modal> call = apiService.getlocationshop(fullUrl, params);

        Log.e("fullUrl", "" + fullUrl);
        Log.e("params", "" + params);
        call.enqueue(new Callback<location_type_modal>() {
            @Override
            public void onResponse(Call<location_type_modal> call, Response<location_type_modal> response) {
                //  response.headers().get("Set-Cookie");
                int statusCode = response.code();


                Log.e("dashbord", "" + statusCode);

            //   Log.e("params", new Gson().toJson(response.body()));

                if (statusCode == 200) {
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        notakeway_layout.setVisibility(View.GONE);

                        url_postcode.setText(response.body().getClientinfo().getPostcode());
                        List<location_type_sub_modal.clients> jobdetails = (response.body().getClientinfo().getClients());
                        DashboardSearchResultList adapter = new DashboardSearchResultList(mContext, (List<location_type_sub_modal.clients>) jobdetails);
                        restaurants_view.setHasFixedSize(true);
                        restaurants_view.setLayoutManager(new LinearLayoutManager(Dashboard_Activity.this));
                        restaurants_view.setAdapter(adapter);


                        Log.e("itemcount", "" + adapter.getItemCount());


                    } else {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        if (response.body().getLocations() != null) {
                            Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), response.body().getLocations(), Snackbar.LENGTH_LONG).show();
                        } else {
                            url_postcode.setText(postcode);
                            notakeway_layout.setVisibility(View.VISIBLE);
                            separated = response.body().getMsg().split("We");
                            separated[0] = separated[0].trim();
                            separated[1] = separated[1].trim();
                            notakeaway.setText(separated[0]);
                            notakeawayone.setText("We " + separated[1]);
                            //  Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                } else {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                   Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<location_type_modal> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }

        });

    }

    /*---------------------------Search API Call-----------------------------*/
    private void serachgetshop(String serachkey, String lat, String lng, String area, String postcode, String struseris) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("search", serachkey);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("area", area);
        params.put("postcode", postcode);
        params.put("user_id", struseris);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<serachgetshop_modal> call = apiService.serachgetshop(params);

        Log.e("fullUrl", "" + fullUrl);
        Log.e("params", "" + params);
        call.enqueue(new Callback<serachgetshop_modal>() {
            @Override
            public void onResponse(Call<serachgetshop_modal> call, Response<serachgetshop_modal> response) {
                //  response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        notakeway_layout.setVisibility(View.GONE);

                        List<serachgetshop_modal.clientinfos> jobdetails = (response.body().getClientinfo());
                        DashboardSearchclientList searchclientadapter = new DashboardSearchclientList(mContext, jobdetails);
                        search_restaurants_view.setHasFixedSize(true);
                        search_restaurants_view.setLayoutManager(new LinearLayoutManager(Dashboard_Activity.this));
                        search_restaurants_view.setAdapter(searchclientadapter);


                        //  Log.e("itemcount", "" + adapter.getItemCount());


                    } else {

                        if (serachkey.length() >= 3) {
                            notakeway_layout.setVisibility(View.VISIBLE);
                            restaurants_view.setVisibility(View.GONE);
                            search_restaurants_view.setVisibility(View.GONE);
                        }
                    }
                } else {

                    Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<serachgetshop_modal> call, Throwable t) {

                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });

    }
    /* *//*---------------------------get api order type values----------------------------------------------------*//*
    private void ordertypecheck(Integer otp) {
        final ProgressDialog loader = ProgressDialog.show(Dashboard_Activity.this, "", "Loading...", true);
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("order_type", otp);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        authKey = user.get(SessionManager.KEY_phpsessid);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<order_type_modal> call = apiService.getordertype(authKey, params);
        call.enqueue(new Callback<order_type_modal>() {
            @Override
            public void onResponse(Call<order_type_modal> call, Response<order_type_modal> response) {
                response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    loader.dismiss();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        url.setText(response.body().getClientinfo().getPostcode());
                    } else {
                        if (response.body().getLocations() != null) {
                            Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), response.body().getLocations(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                } else {
                    loader.dismiss();
                    Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<order_type_modal> call, Throwable t) {
                loader.dismiss();
                Snackbar.make(Dashboard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });

    }
*/
    /*---------------------------check internet connection----------------------------------------------------*/


    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Dashboard_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Dashboard_Activity.this);
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

    /*---------------------------Warning dialog show----------------------------------------------------*/
   /* public class ViewwarningDialog {
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
                    dbHelper = new SQLDBHelper(Dashboard_Activity.this);
                    dbHelper.deleteAll();
                    warningdialog.dismiss();

                    Intent intentthir = new Intent(Dashboard_Activity.this, Item_Menu_Activity.class);
                    intentthir.putExtra("menuurlpath", menuurlpath);
                    startActivity(intentthir);


                    // startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });
            warningdialog.show();
        }
    }*/


    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);

            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
            return true;
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Client List Activity");
    }
}
