package com.fusionkitchen.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;

import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;


import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.fusionkitchen.adapter.MenuListViewAdapter;
import com.fusionkitchen.adapter.MoreinfoopenhrsAdapter;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.AdapterListData;
import com.fusionkitchen.model.ExamData;
import com.fusionkitchen.model.cart.Cartitem;
import com.fusionkitchen.model.menu_model.Menu_Page_listmodel;
import com.fusionkitchen.model.menu_model.collDelivery_model;
import com.fusionkitchen.model.modeoforder.getlatertime_model;
import com.fusionkitchen.model.modeoforder.modeof_order_popup_model;
import com.fusionkitchen.model.moreinfo.about_us_model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.maps.OnMapReadyCallback;


import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.MenuAddonItemAdapter;
import com.fusionkitchen.adapter.MenuItemAdapter;
import com.fusionkitchen.adapter.MenuOfferAdapter;
import com.fusionkitchen.adapter.MenupromoAdapter;
import com.fusionkitchen.adapter.MenuserachcatAdapter;
import com.fusionkitchen.adapter.SearchItemAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.addon.menu_addons_model;
import com.fusionkitchen.model.menu_model.final_addon_add_model;
import com.fusionkitchen.model.menu_model.menu_item_model;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;
import com.fusionkitchen.model.menu_model.search_menu_item_model;
import com.fusionkitchen.model.offer.offer_code_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;
import static androidx.recyclerview.widget.RecyclerView.*;
import static java.lang.Integer.parseInt;


public class Item_Menu_Activity extends AppCompatActivity implements OnMapReadyCallback{

    String updateqty,description;
    String updatefinalamt;
    String item_price_amt;
    TextView delivery_collection_textview,cooking_time_textview;
    TextView restaurants_status;
    private static List<about_us_model.aboutdetails.openinghours> jobdetails6 = new ArrayList<>();

    public Context mContext = Item_Menu_Activity.this;
    private Dialog dialog,dialog_loading;
    long pressedTime;
    TextView menu_list_view;
    LinearLayout menu_page_pop_up;
    Dialog menulistpopup;
    MenuListViewAdapter menuListViewAdapter;
    CardView search_box_cardview;
    HttpUrl baseUrl;
    Dialog dialog_order_mode_popup;
    Dialog info_popup,heart_popup,Coupen_popup;
    TextView  shop_name,shop_address;
    String strsgetAddress1,strsAddress2,strscity, strsstate, strspincd;
    GoogleMap mMap;
    LatLng p1 = null;
    double p2;
    double p3;
    Boolean check_favourite_boolean;

    String menu_favourite_path;
    int favourite_client;

    List<menu_item_sub_model.categoryall> jobdetails2;


    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    TextView total_amount_textview;
    double amtfloat = 0.00;

    /*---------------------------Session Manager Class----------------------------------------------------*/
    // Session Manager Class
    //  SessionManager session;

    /*---------------------------addon extra add shared prferances----------------------------------------------------*/
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    // public static final String MyPREFERENCESMENU = "MyPrefs_mrnuurl";

    /*---------------------------Intent Value Get URL Path ----------------------------------------------------*/
    String menuurlpath, searchfullUrl, reloadback;

    /*---------------------------XML ID Call----------------------------------------------------*/
    String authKey, fullUrl, addonlimit, nextaid, btnnext, arrayaddonextraidsingle, item_price, item_total_amt;
    LoaderTextView menu_clientname, menu_tvdesc, clent_rating,  min_order_amt,miles_textview,delicery_collection_time;
    TextView addon_item_name, addon_item_addonname, item_amt, btnClear, collection_tex, delivery_tex;
    CardView back_card_view1, delivery_card_view2, collection_card_view3, delivery_only_view2, collection_only_view3, menu_view_card_view, menu_addon_item_view;
    TextView top_back_card_view;
    CardView top_card_search_view,heart_icon,shareicon,info_icon_btn,repeat_popup;
    LinearLayout header_img, collect_lener, delivery_lener, min_lener, bottom_nav, addon_extra_liner;
    RelativeLayout add_to_cart_layout;
    LinearLayout search_layout;
    RecyclerView recyclerviewOffers, recyclerviewpromo, recyclerviewitem, recyclerviewmenuitem, addon_item_view, searchrecyclerviewitem;
    GridView selected_addon_item_view;
    String[] values;
    menu_addons_model.dataval.selecteditemlist[] listItem;
    ImageButton close_addon;
    AppCompatButton addon_extra1, addon_extra2, addon_extra3, addon_extra4, addon_extra5, addon_extra6, addon_extra7;
    AppCompatButton Checkout, proceed_button;
    double sum;
    RelativeLayout relativ_moreinfo;
    // String strno, strless, strhalf, stron, strwith, stronburger, stronchips;
    CardView top_card_view, search_back;
    TextView total_item;
    AppBarLayout appbar;
    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*---------------------------Fab show and hind view----------------------------------------------------*/
    ExtendedFloatingActionButton mAddFab;
    Boolean isAllFabsVisible;
    NestedScrollView nsv;
    //  ScrollView nsv;
    ExtendedFloatingActionButton mfab_close;
    LinearLayout search_listview_header;

    /*---------------------------loaderviewlibrary----------------------------------------------------*/
    ShimmerFrameLayout mShimmerViewContainer;
    StringBuffer stringName_str, addonextraitemstringName_str;
    String addonextrastringName = "";
    String addonextraitemstringName = "";
    String categoryname;
    String subcategoryname;
    String ItemName;
    String addonid;
    String addonitemid;
    String addonidprice;
    String addonitemtype,todaytimestring,latertimestring;
    String addonitemposition;
    String addonitemarrayextraData;
    String btnnextfir;
    String arrayextranameData;
    String arrayaddonitemid;
    EditText editTextSearch, top_search_editTextSearch, top_editTextSearch;
    String addonextrafirsr = "";
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ArrayList<String> aidlist;
    String str_listItems = "";
    String item_qty = "1";

    int checkval;

    // ArrayAdapter<ArrayList<String>> listItemsaddon;
    ArrayList<String> listItemsids;
    ArrayAdapter<String> adapterids;
    String itemidsstr, addonscessfullUrl;

    ArrayList<String> arrayextranameDataadd;
    ArrayList<Integer> arrayextranameDataaddsize;

    ArrayList<Integer> listItemsidssize;
    ArrayList<String> arrayaddonitemidadd;

    ArrayList<Integer> arrayaddonextraidsingleaddsize;
    ArrayList<String> arrayaddonextraidsingleadd;


    ArrayList<String> item_priceadd;
    ArrayList<Integer> item_pricesize;


    boolean item_price_btn;

    String backbutclknum;


    ArrayList<String> listItemsexraids;
    ArrayAdapter<String> adapterexraids;
    String itemexradsstr, addsingleextra;

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    private SQLDBHelper dbHelper;
    int personID;
    int cursor;
    Cursor totalitemamut;

    Animation animationdown, animationup;


    Dialog repeatpopup;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id;

    /*------------------------Offer XML ID-----------------------------*/
    RelativeLayout offer_rel;
    CardView menu_offer_details_view;
    AppCompatButton confirm_code, confirm_copy_button;
    TextView details_offers_show;
    ImageView offer_back, search_colse_top, search_colse_bottom,restaurants_image;
    String promocode, itempossion, onlinecode, onlinedate, onlinesymbol, onlinepaytypess, onlinety, cooking_insttructionback;

    private long mLastClickTime = 0;

    /*--------------------------Login postcode save local------------------------*/
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    String key_postcode, key_address, key_area;

    /*------------------------------Add to cart to get item details------------------------------*/
    Intent intentitemdetails;

    /*--------------------------order_mode_popup_view---------------------*/
    String metdpasfullUrl;
    String order_mode;
    TextView delivery_one_tex;
    String todaytimestr, laterdatestr, latertimestr, activetagstr, laterdate;
    String selectedtodaytimeItem, selectedlaterdateItem, selectedlatertimeItem;
    String  mlaterdatefullUrl;
    ImageView bikeimgonlydelivery;
    /*   CardView mode_view2;*/

    TextView mode_view2;
    RecyclerView menu_item_list_view;
    String key_lat,key_lon;

    int pastVisibleItem, visibleItemCount, totalItemCount, currentPage = 1, mPage = 1;
    private boolean loading = true;


    /*------------------------------------------------------Menu Page List----------------------------------------*/
    private List<Menu_Page_listmodel> menu_page_listmodel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_item_menu);

        overridePendingTransition(R.anim.enter, R.anim.exit);

        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Item_Menu_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(Item_Menu_Activity.this);
        }

        /*------------------shared preferences Value-------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  sharedpreferences = getSharedPreferences(MyPREFERENCESMENU, Context.MODE_PRIVATE);

        baseUrl  =ApiClient.getInstance().getClient().baseUrl();


        /*---------------------------ExtendedFloatingActionButton----------------------------------------------------*/
        mAddFab = findViewById(R.id.add_fab);
        mfab_close = findViewById(R.id.fab_close);
        top_card_view = findViewById(R.id.top_card_view);

        /*--------------------------Login postcode save local------------------------*/
        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);

        key_postcode = (sharedptcode.getString("KEY_postcode", null));
        key_area = (sharedptcode.getString("KEY_area", null));
        key_address = (sharedptcode.getString("KEY_address", null));
        key_lat = (sharedptcode.getString("KEY_lat", null));
        key_lon = (sharedptcode.getString("KEY_lon", null));


        Log.e("localstore6", "" + key_postcode);
        Log.e("localstore7", "" + key_area);
        Log.e("localstore8", "" + key_address);

        Log.e("otypelog", "" + sharedpreferences.getString("ordermodetype", null));


        /*-----------------Fab-----------------*/
        appbar = findViewById(R.id.appbar);

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                int ScrollRange =   scrollRange + verticalOffset;

                if(20 < ScrollRange){
                    top_card_view.setVisibility(View.INVISIBLE);
                }else{
                    top_card_view.setVisibility(View.VISIBLE);
                }

            }
        });



        mAddFab.shrink();
        nsv = findViewById(R.id.nsv);

        nsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                nsv.fullScroll(View.FOCUS_UP);
            }
        }, 400);

        Log.e("nextapi1", "" + nextaid);
        Log.e("nextapi2", "" + btnnextfir);
        Log.e("nextapi3", "" + addonlimit);


        /*---------------------------Fab show and hind view----------------------------------------------------*/

        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                Log.e("scrolviw1", "scrollX: " + scrollX);
                Log.e("scrolviw2", "scrollY: " + scrollY);
                Log.e("scrolviw3", "oldScrollX: " + oldScrollX);
                Log.e("scrolviw4", "oldScrollY: " + oldScrollY);
/*
                if (39 < oldScrollY) {   //400
                     top_card_view.setVisibility(View.VISIBLE);
                } else {
                     top_card_view.setVisibility(View.INVISIBLE);
                }*/

                if (scrollY > oldScrollY) {
                    mAddFab.extend();
                } else {
                    mAddFab.shrink();
                }

            }
        });


        mAddFab.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_view_card_view.setVisibility(View.VISIBLE);
                        mfab_close.setVisibility(View.VISIBLE);
                        mAddFab.setVisibility(View.GONE);
                        mfab_close.extend();
                    }
                });

        mfab_close.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mfab_close.shrink();
                        menu_view_card_view.setVisibility(View.GONE);
                        mfab_close.setVisibility(View.GONE);
                        mAddFab.setVisibility(View.VISIBLE);
                    }
                });

        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
        Intent intent = getIntent();
        menuurlpath = intent.getStringExtra("menuurlpath");


        if (intent.getStringExtra("reloadback") == null) {
            reloadback = "0";
        } else {
            reloadback = intent.getStringExtra("reloadback");

        }

        Log.d("reloadback",""+intent.getStringExtra("reloadback"));
        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
        Intent intentcoocking = getIntent();
        if (intentcoocking.getStringExtra("cooking_insttruction") == null) {
            cooking_insttructionback = "";
        } else {
            cooking_insttructionback = intentcoocking.getStringExtra("cooking_insttruction");

        }
        Log.e("itemmenuurlvs", "" + menuurlpath);

        /*---------------------------Sql Lite DataBase get  item count----------------------------------------------------*/

        dbHelper = new SQLDBHelper(Item_Menu_Activity.this);
        getContactsCount();



        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
        editor_extra.putString("menuurlpath", menuurlpath);
        editor_extra.commit();


        Log.e("itemmenuurlpath", "" + menuurlpath);
        Log.e("itemmenuurlpathgetshare", "" + sharedpreferences.getString("menuurlpath", null));

        /*---------------------------Session Manager Class----------------------------------------------------*/
/*        session = new SessionManager(getApplicationContext());
       session.checkLogin();*/

        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));

        /*---------------------------XML ID Call----------------------------------------------------*/
        menu_clientname = findViewById(R.id.menu_clientname);
        back_card_view1 = findViewById(R.id.back_card_view1);
        top_back_card_view = findViewById(R.id.top_back_card_view);
        header_img = findViewById(R.id.header_img);
        menu_tvdesc = findViewById(R.id.menu_tvdesc);
        clent_rating = findViewById(R.id.clent_rating);
        min_order_amt = findViewById(R.id.min_order_amt);
        collect_lener = findViewById(R.id.collect_lener);
        delivery_lener = findViewById(R.id.delivery_lener);
        min_lener = findViewById(R.id.min_lener);
        recyclerviewOffers = findViewById(R.id.recyclerviewOffers);
        recyclerviewpromo = findViewById(R.id.recyclerviewpromo);
        recyclerviewitem = findViewById(R.id.recyclerviewitem);
        ViewCompat.setNestedScrollingEnabled(recyclerviewOffers, false);
        recyclerviewOffers.setNestedScrollingEnabled(false);


        restaurants_status = findViewById(R.id.restaurants_status);
        delicery_collection_time = findViewById(R.id.delicery_collection_time);

        menu_view_card_view = findViewById(R.id.menu_view_card_view);
        recyclerviewmenuitem = findViewById(R.id.recyclerviewmenuitem);
        menu_addon_item_view = findViewById(R.id.menu_addon_item_view);
        bottom_nav = findViewById(R.id.bottom_nav);
        add_to_cart_layout = findViewById(R.id.add_to_cart_layout);
        close_addon = findViewById(R.id.close_addon);
        addon_item_name = findViewById(R.id.addon_item_name);
        addon_item_addonname = findViewById(R.id.addon_item_addonname);
        addon_extra1 = findViewById(R.id.addon_extra1);
        addon_extra2 = findViewById(R.id.addon_extra2);
        addon_extra3 = findViewById(R.id.addon_extra3);
        addon_extra4 = findViewById(R.id.addon_extra4);
        addon_extra5 = findViewById(R.id.addon_extra5);
        addon_extra6 = findViewById(R.id.addon_extra6);
        addon_extra7 = findViewById(R.id.addon_extra7);
        editTextSearch = findViewById(R.id.editTextSearch);
        item_amt = findViewById(R.id.item_amt);
        addon_extra_liner = findViewById(R.id.addon_extra_liner);

        Checkout = findViewById(R.id.Checkout);
        btnClear = findViewById(R.id.btnClear);
        info_icon_btn = findViewById(R.id.info_icon);


        addon_item_view = findViewById(R.id.addon_item_view);
        selected_addon_item_view = findViewById(R.id.selected_addon_item_view);
        miles_textview = findViewById(R.id. miles_textview);

        selected_addon_item_view.setVisibility(View.GONE);
        animationdown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animationup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        relativ_moreinfo = findViewById(R.id.relativ_moreinfo);
        search_layout = findViewById(R.id.search_layout);
        searchrecyclerviewitem = findViewById(R.id.searchrecyclerviewitem);
        top_editTextSearch = findViewById(R.id.top_editTextSearch);
        search_colse_top = findViewById(R.id.search_colse_top);
        search_colse_bottom = findViewById(R.id.search_colse_bottom);
        menu_list_view = findViewById(R.id.menu_list_view);
        menu_page_pop_up = findViewById(R.id.menu_page_pop_up);
        heart_icon = findViewById(R.id.heart_icon);
        shareicon = findViewById(R.id.shareicon);
      //  repeat_popup = findViewById(R.id.repeat_popup);


        total_amount_textview = findViewById(R.id.total_amount_textview);
        search_box_cardview = findViewById(R.id.search_box_cardview);

        cooking_time_textview = findViewById(R.id.cooking_time_textview);

        delivery_collection_textview = findViewById(R.id.delivery_collection_textview);


        restaurants_image  = findViewById(R.id.restaurants_image);

        delivery_one_tex = findViewById(R.id.delivery_one_tex);
        bikeimgonlydelivery = findViewById(R.id.bikeimgonlydelivery);
        mode_view2 = findViewById(R.id.mode_view2);

        search_listview_header = findViewById(R.id.search_listview_header);
        search_listview_header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search_box_cardview.setVisibility(View.VISIBLE);
            }
        });



        heart_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                 HeartIcon();


            }
        });

        info_icon_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                info_popup.show();

            }
        });



        if(user_id.equalsIgnoreCase("")){

            heart_icon.setVisibility(GONE);

        }else{

            heart_icon.setVisibility(VISIBLE);

            SharedPreferences getclient_id = getSharedPreferences("favourite_store_data", MODE_PRIVATE);
            menu_favourite_path = getclient_id.getString("menuurlpath", null);
            favourite_client = getclient_id.getInt("client_id", 0);
            showFavourite(user_id,favourite_client);


        }

/*
        recyclerviewitem.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int total = ((LinearLayoutManager)recyclerView.getLayoutManager()).getItemCount();
                int firstVisibleItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                Log.d("Item_Menu_Activity_scroll", String.valueOf(firstVisibleItemCount));

            }

            @Override
            public void onScrollStateChanged(RecyclerView r, int newState) {
                super.onScrollStateChanged(r, newState);

                int firstVisibleItemCount = ((LinearLayoutManager)r.getLayoutManager()).findFirstVisibleItemPosition();

            }

        });*/



        Log.d("backbtn",reloadback);





        btnClear.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Toast.makeText(getApplicationContext(), "Back Click", Toast.LENGTH_LONG).show();

                        //Item id remove last
                        aidlist.remove(aidlist.size() - 1);


                        item_priceadd.remove(item_priceadd.size() - 1);
                        // item_amt.setText("Total: £ " + (item_priceadd.get(item_priceadd.size() - 1)));

                        item_amt.setText(" £ " + (item_priceadd.get(item_priceadd.size() - 1)));

                        Log.e("clraeitem_priceadd2", "" + item_priceadd);
                        Log.e("backclick", "" + aidlist);
                        Log.e("backclick2", "" + aidlist.get(aidlist.size() - 1));
                        Log.e("arrayextranameData", "" + arrayextranameData);

// back butoon 1 to two time remove addon item without check box next
                        if (backbutclknum.equalsIgnoreCase("1")) {
                            // Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();

                            //addon item name
                            arrayextranameDataaddsize.add(arrayextranameDataadd.size());
                            arrayextranameDataadd.clear();

                            //addon item id name
                            listItemsidssize.add(arrayaddonitemidadd.size());
                            arrayaddonitemidadd.clear();

                            //addon item comas name
                            arrayaddonextraidsingleaddsize.add(arrayaddonextraidsingleadd.size());
                            arrayaddonextraidsingleadd.clear();


                            //addon amount add
                            // item_pricesize.add(item_priceadd.size());
                            //  item_priceadd.clear();


                            for (int i = 0; i < (arrayextranameDataaddsize.get(arrayextranameDataaddsize.size() - 1)); i++) {

                                //addon item name
                                listItems.remove(listItems.size() - 1);
                                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.raw_simple_list_item, R.id.selected_item, listItems);
                                selected_addon_item_view.setAdapter(adapter);
                                Log.e("addonadaptername", "" + adapter.toString());
                                Log.e("addonadapi", "" + i);
//addon item id name
                                listItemsids.remove(listItemsids.size() - 1);
                                adapterids.notifyDataSetChanged();
                                itemidsstr = listItemsids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();

//addon item comas name
                                listItemsexraids.remove(listItemsexraids.size() - 1);
                                adapterexraids.notifyDataSetChanged();
                                itemexradsstr = listItemsexraids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();


                            }

                            for (int i = 0; i < (arrayextranameDataaddsize.get(arrayextranameDataaddsize.size() - 2)); i++) {

                                //addon item name
                                listItems.remove(listItems.size() - 1);
                                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.raw_simple_list_item, R.id.selected_item, listItems);
                                selected_addon_item_view.setAdapter(adapter);
                                Log.e("addonadaptername", "" + adapter.toString());
                                Log.e("addonadapi", "" + i);

//addon item id name
                                listItemsids.remove(listItemsids.size() - 1);
                                adapterids.notifyDataSetChanged();
                                itemidsstr = listItemsids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();

                                //addon item comas name
                                listItemsexraids.remove(listItemsexraids.size() - 1);
                                adapterexraids.notifyDataSetChanged();
                                itemexradsstr = listItemsexraids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();


                            }
                            //addon item name
                            arrayextranameDataaddsize.remove(arrayextranameDataaddsize.size() - 1);
                            arrayextranameDataaddsize.remove(arrayextranameDataaddsize.size() - 1);

//addon item id name
                            listItemsidssize.remove(listItemsidssize.size() - 1);
                            listItemsidssize.remove(listItemsidssize.size() - 1);


                            arrayaddonextraidsingleaddsize.remove(arrayaddonextraidsingleaddsize.size() - 1);
                            arrayaddonextraidsingleaddsize.remove(arrayaddonextraidsingleaddsize.size() - 1);

                        } else {

                            //single time remove addon
                            //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                            for (int i = 0; i < (arrayextranameDataaddsize.get(arrayextranameDataaddsize.size() - 1)); i++) {
                                //addon item name
                                listItems.remove(listItems.size() - 1);
                                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.raw_simple_list_item, R.id.selected_item, listItems);
                                selected_addon_item_view.setAdapter(adapter);
                                Log.e("addonadaptername", "" + adapter.toString());
//addon item id name
                                listItemsids.remove(listItemsids.size() - 1);
                                adapterids.notifyDataSetChanged();
                                itemidsstr = listItemsids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();
//addon item comas name
                                listItemsexraids.remove(listItemsexraids.size() - 1);
                                adapterexraids.notifyDataSetChanged();
                                itemexradsstr = listItemsexraids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();


                            }
                            //addon item name
                            arrayextranameDataaddsize.remove(arrayextranameDataaddsize.size() - 1);

//addon item id name
                            listItemsidssize.remove(listItemsidssize.size() - 1);
//addon item comas name
                            arrayaddonextraidsingleaddsize.remove(arrayaddonextraidsingleaddsize.size() - 1);
                        }
                        addonitemfirstview(ItemName, aidlist.get(aidlist.size() - 1), addonitemid, addonitemarrayextraData, "2");
                        //listaddonid
                    }
                });


        editTextSearch.addTextChangedListener(mDateEntryWatcher);


        top_editTextSearch.addTextChangedListener(mDateEntryWatcher);


        mode_view2.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mode_view2.setEnabled(false);
                        Order_mode_popup();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mode_view2.setEnabled(true);
                            }
                        }, 8000);

                    }
                });

        search_colse_top.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        editTextSearch.setText("");
                        top_editTextSearch.setText("");
                        recyclerviewitem.setVisibility(View.VISIBLE);
                        search_layout.setVisibility(View.GONE);
                        mAddFab.setVisibility(View.VISIBLE);
                    }
                });
        search_colse_bottom.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        editTextSearch.setText("");
                        top_editTextSearch.setText("");
                        recyclerviewitem.setVisibility(View.VISIBLE);
                        search_layout.setVisibility(View.GONE);
                        mAddFab.setVisibility(View.VISIBLE);
                    }
                });


        //  recyclerviewitem
        //  search_layout
        /*------------------------Offer XML ID-----------------------------*/

        offer_rel = findViewById(R.id.offer_rel);
        offer_back = findViewById(R.id.offer_back);
        menu_offer_details_view = findViewById(R.id.menu_offer_details_view);
        confirm_code = findViewById(R.id.confirm_code);
        confirm_copy_button = findViewById(R.id.confirm_copy_button);
        details_offers_show = findViewById(R.id.details_offers_show);
        confirm_code.setTransformationMethod(null);

        offer_back.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menu_offer_details_view.setVisibility(View.GONE);
                    }
                });

        total_item = findViewById(R.id.total_item);
        proceed_button = findViewById(R.id.proceed_button);


        if (cursor != 0) {

            //total_item.setText(cursor + " Items");
            // total_item.setText(cursor+ "");

            ArrayList<String> get_qty_count = dbHelper.getqtycount();
            total_item.setText(get_qty_count.get(0) + "");
            Log.d("Cursor1 ", String.valueOf(get_qty_count.get(0) + ""));

            ArrayList<String> get_amt_count = dbHelper.gettotalamt();
            Double test = Double.valueOf(get_amt_count.get(0)+ "");
            total_amount_textview.setText(String.format("%.2f", amtfloat + Double.parseDouble(get_amt_count.get(0) + "")));
            Log.d("TotalAmount",get_amt_count.get(0)+ " ");

            collDelivery(menuurlpath);

        } else {
            add_to_cart_layout.setVisibility(View.INVISIBLE);
            menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api

            //Order_mode_popup();


            SharedPreferences sharedpreferences = getSharedPreferences("PREFS_MOREINFO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            Log.d("More_info",sharedpreferences.getString("More_info",null));

            if(sharedpreferences.getString("More_info",null).equalsIgnoreCase("MoreInfo")){
                // editor.putString("More_info", "Moreinfo-popup");
            }else{
                Order_mode_popup();
            }
            editor.commit();

        }

        proceed_button.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cursor != 0) {
                            Intent intent = new Intent(Item_Menu_Activity.this, Add_to_Cart.class);
                            intent.putExtra("cooking_insttruction", cooking_insttructionback);
                            startActivity(intent);
                        }
                    }
                });

        /*---------------------------OnClickListener----------------------------------------------------*/
        addon_extra1.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addonextrafirsr = "0";
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("addon_extra", "0");
                        editor_extra.commit();

                       /* addonextrastringName = addonextrastringName + "0,";
                        Toast.makeText(getApplicationContext(), addonextrastringName, Toast.LENGTH_LONG).show();*/

                        /*     addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        addon_extra1.setTextColor(Color.WHITE);*/
                        addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_style));
                        addon_extra1.setTextColor(Color.WHITE);


                        //  addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                        addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra2.setTextColor(addon_extra2.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                        addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra3.setTextColor(addon_extra3.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //  addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                        addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra4.setTextColor(addon_extra4.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                        addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra5.setTextColor(addon_extra5.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //   addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                        addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra6.setTextColor(addon_extra6.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                        addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra7.setTextColor(addon_extra7.getContext().getResources().getColor(R.color.No_Less_Text_Color));


                    }
                });
        addon_extra2.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addonextrafirsr = "1";
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("addon_extra", "1");
                        editor_extra.commit();
                       /* addonextrastringName = addonextrastringName + "1,";
                        Toast.makeText(getApplicationContext(), addonextrastringName, Toast.LENGTH_LONG).show();*/

                        // addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                        addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra1.setTextColor(addon_extra1.getContext().getResources().getColor(R.color.No_Less_Text_Color));

                        //  addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_style));
                        addon_extra2.setTextColor(Color.WHITE);

                        //  addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                        addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra3.setTextColor(addon_extra3.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                        addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra4.setTextColor(addon_extra4.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //   addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                        addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra5.setTextColor(addon_extra5.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                        addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra6.setTextColor(addon_extra6.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                        addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra7.setTextColor(addon_extra7.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                    }
                });
        addon_extra3.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addonextrafirsr = "2";
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("addon_extra", "2");
                        editor_extra.commit();
                       /* addonextrastringName = addonextrastringName + "2,";
                        Toast.makeText(getApplicationContext(), addonextrastringName, Toast.LENGTH_LONG).show();*/

                        // addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                        addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra1.setTextColor(addon_extra1.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                        addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra2.setTextColor(addon_extra2.getContext().getResources().getColor(R.color.No_Less_Text_Color));

                        //addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_style));
                        addon_extra3.setTextColor(Color.WHITE);

                        // addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                        addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra4.setTextColor(addon_extra4.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                        addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra5.setTextColor(addon_extra5.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                        addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra6.setTextColor(addon_extra6.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //  addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                        addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra7.setTextColor(addon_extra7.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                    }
                });
        addon_extra4.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addonextrafirsr = "3";
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("addon_extra", "3");
                        editor_extra.commit();
                       /* addonextrastringName = addonextrastringName + "3,";
                        Toast.makeText(getApplicationContext(), addonextrastringName, Toast.LENGTH_LONG).show();*/
                        //addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                        addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra1.setTextColor(addon_extra1.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //  addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                        addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra2.setTextColor(addon_extra2.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                        addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra3.setTextColor(addon_extra3.getContext().getResources().getColor(R.color.No_Less_Text_Color));

                        //   addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_style));
                        addon_extra4.setTextColor(Color.WHITE);

                        //addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                        addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra5.setTextColor(addon_extra5.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                        addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra6.setTextColor(addon_extra6.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                        addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra7.setTextColor(addon_extra7.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                    }
                });

        addon_extra5.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addonextrafirsr = "4";
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("addon_extra", "4");
                        editor_extra.commit();
                       /* addonextrastringName = addonextrastringName + "4,";
                        Toast.makeText(getApplicationContext(), addonextrastringName, Toast.LENGTH_LONG).show();*/
                        //addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                        addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra1.setTextColor(addon_extra1.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                        addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra2.setTextColor(addon_extra2.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                        addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra3.setTextColor(addon_extra3.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                        addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra4.setTextColor(addon_extra4.getContext().getResources().getColor(R.color.No_Less_Text_Color));

                        //addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_style));
                        addon_extra5.setTextColor(Color.WHITE);

                        //addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                        addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra6.setTextColor(addon_extra6.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                        addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra7.setTextColor(addon_extra7.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                    }
                });
        addon_extra6.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addonextrafirsr = "5";
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("addon_extra", "5");
                        editor_extra.commit();
                       /* addonextrastringName = addonextrastringName + "5,";
                        Toast.makeText(getApplicationContext(), addonextrastringName, Toast.LENGTH_LONG).show();*/
                        //addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                        addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra1.setTextColor(addon_extra1.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                        addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra2.setTextColor(addon_extra2.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                        addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra3.setTextColor(addon_extra3.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                        addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra4.setTextColor(addon_extra4.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                        addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra5.setTextColor(addon_extra5.getContext().getResources().getColor(R.color.No_Less_Text_Color));

                        //  addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_style));
                        addon_extra6.setTextColor(Color.WHITE);

                        // addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                        addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra7.setTextColor(addon_extra7.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                    }
                });
        addon_extra7.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addonextrafirsr = "6";
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("addon_extra", "6");
                        editor_extra.commit();
                       /* addonextrastringName = addonextrastringName + "6,";
                        Toast.makeText(getApplicationContext(), addonextrastringName, Toast.LENGTH_LONG).show();*/
                        //  addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                        addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra1.setTextColor(addon_extra1.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        //addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                        addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra2.setTextColor(addon_extra2.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                        addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra3.setTextColor(addon_extra3.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                        addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra4.setTextColor(addon_extra4.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                        addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra5.setTextColor(addon_extra5.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        // addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                        addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                        addon_extra6.setTextColor(addon_extra6.getContext().getResources().getColor(R.color.No_Less_Text_Color));

                        //addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.No_Less_Text_Color));
                        addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_style));
                        addon_extra7.setTextColor(Color.WHITE);
                    }
                });


        back_card_view1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
             /*   Intent intentorder = new Intent(getApplicationContext(), Dashboard_Activity.class);
                startActivity(intentorder);*/


                if (reloadback.equalsIgnoreCase("1")) {
                    startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                    finish();
                } else if (reloadback.equalsIgnoreCase("2")) {
                    startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
                    finish();
                } else if (reloadback.equalsIgnoreCase("3")) {
                    startActivity(new Intent(getApplicationContext(), Show_Offer_Activity.class));
                    finish();
                } else if(reloadback.equalsIgnoreCase("4")) {
                    // startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class))
                    startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                    finish();

                }else{
                    startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                    finish();
                }


                //finish();
            }
        });

        top_back_card_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (reloadback.equalsIgnoreCase("1")) {
                    startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                    finish();

                } else if (reloadback.equalsIgnoreCase("2")) {
                    startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
                    finish();

                } else if (reloadback.equalsIgnoreCase("3")) {
                    startActivity(new Intent(getApplicationContext(), Show_Offer_Activity.class));
                    finish();

                } else if (reloadback.equalsIgnoreCase("4")){

                    startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                    finish();


                } else {
                    //startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                    startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                    finish();

                }
                //finish();
            }
        });


        close_addon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_addon_item_view.setVisibility(View.GONE);
                mAddFab.setVisibility(View.VISIBLE);
                // bottom_nav.setVisibility(View.VISIBLE);
                add_to_cart_layout.setVisibility(View.VISIBLE);
                if (cursor != 0) {
                } else {
                    add_to_cart_layout.setVisibility(View.INVISIBLE);
                }
            }
        });

        Checkout.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        addonitemnext("0");
                        // Toast.makeText(getApplicationContext(), "Test - 1", Toast.LENGTH_LONG).show();
/*
                        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(mMessageReceiver);
                        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(addonmMessageReceiver);
                        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(addonbtnnextid);
                        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(mitemsuccessMessageReceiver);
*/

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
                        Intent intenthome = new Intent(getApplicationContext(), Postcode_Activity.class);
                        startActivity(intenthome);
                        break;
                    case R.id.home_chat:
                        Freshchat.showConversations(getApplicationContext());
                        break;
                    case R.id.home_card:
                      /*  Intent intentreview = new Intent(getApplicationContext(), Review.class);
                        startActivity(intentreview);*/
                        // Toast.makeText(getApplicationContext(), "Card", Toast.LENGTH_SHORT).show();
                        if (cursor != 0) {
                            Intent intentcard = new Intent(getApplicationContext(), Add_to_Cart.class);
                            intentcard.putExtra("cooking_insttruction", cooking_insttructionback);
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


        relativ_moreinfo.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Item_Menu_Activity.this, Moreinfo_Activity.class);
                        intent.putExtra("order_types", sharedpreferences.getString("ordermodetype", null));//collection delivery type
                        intent.putExtra("client_name", menu_clientname.getText().toString());
                        intent.putExtra("tv_desc", menu_tvdesc.getText().toString());
                        startActivity(intent);
                    }
                });

        /*---------------------------Offer RecyclerView ----------------------------------------------------*/
        menu_offer(menuurlpath, "0", "0");

        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("custom-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(addonmMessageReceiver, new IntentFilter("addon_extra-message"));

        LocalBroadcastManager.getInstance(this).registerReceiver(addonbtnnextid, new IntentFilter("addon_btn-nextid"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mitemsuccessMessageReceiver, new IntentFilter("item_successfully_custom-message"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessagepromooff, new IntentFilter("custom-message-promooffer"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageonlineoff, new IntentFilter("custom-message-onlineoffer"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageitempossion, new IntentFilter("item_possion-message"));


        /*---------------------------loaderviewlibrary----------------------------------------------------*/
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();


        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessagecollectononly, new IntentFilter("collection_only"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mclasbackonly, new IntentFilter("clasback"));



        /*---------------------------Share function----------------------------------------------------*/
        shareicon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent =   new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
                String app_url = " https://play.google.com/store/apps/details?id=com.fusionkitchen";
                shareIntent.putExtra(Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });


        /*---------------------------Menu Item List View----------------------------------------------------*/
        menu_page_pop_up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menulistpopup();
                menulistpopup.show();
            }
        });


        Menulistpopup();
        Show_info_popup();

     /*  recyclerviewitem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.e("DY",""+dy);

                int firstVisibleItemCount = ((LinearLayoutManager)recyclerviewitem.getLayoutManager()).findFirstVisibleItemPosition();

                Log.e("recyclerviewitem","  "+firstVisibleItemCount);

            }
        });
*/

    }

    private void showFavourite(String user_id, int favourite_client) {



        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,baseUrl+"showFavourite",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {

                            JSONObject jsonobject = new JSONObject(response);

                            String favourite_check_status = jsonobject.getString("status");

                            if(favourite_check_status.equalsIgnoreCase("true")){

                                     check_favourite_boolean = jsonobject.getBoolean("favourite");

                                     if(check_favourite_boolean == true){
                                         //heart_icon.setBackgroundColor(Color.parseColor("#464747"));
                                         ((CardView) heart_icon).setCardBackgroundColor(Color.parseColor("#e0467c"));
                                     }

                            }else{

                                heart_icon.setBackgroundColor(Color.parseColor("#464747"));
                            }

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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("client_id", String.valueOf(favourite_client));
                return params;
            }
        };

        RequestQueue requestqueue = Volley.newRequestQueue(Item_Menu_Activity.this);
        requestqueue.add(stringRequest);



    }


    private void Show_info_popup() {

        info_popup = new Dialog(this);
        info_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);

        info_popup.setContentView(R.layout.info_design);



        shop_name = info_popup.findViewById(R.id.shop_name);
        shop_address = info_popup.findViewById(R.id.shop_address);
        RecyclerView open_hrs_review = info_popup.findViewById(R.id.open_hrs_review);
        ImageView  direction = info_popup.findViewById(R.id.direction);
        TextView textview_email_id = info_popup.findViewById(R.id.textview_email_id);

        /*------------------------------------Start open time And closed time----------------------------------*/
        // get user data from session


        Map<String, String> params = new HashMap<String, String>();
        params.put("ordermode", sharedpreferences.getString("ordermodetype", null));
        params.put("postcode", key_postcode);
        params.put("area", key_area);
        params.put("address_location", key_address);
        params.put("lat", key_lat);
        params.put("lng", key_lon);

        fullUrl = menuurlpath + "/about";

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<about_us_model> call = apiService.getaboutus(fullUrl, params);
        call.enqueue(new Callback<about_us_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<about_us_model> call, Response<about_us_model> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        //   about_deatils.setText(response.body().getAbout().getAboutus().replace("\n", ""));

                        textview_email_id.setText(response.body().getAbout().getAboutEmail());

                        if (response.body().getAbout().getCuisines().size() == 0) {
                            //    cusines_details.setText("Data not Found");
                        } else if (response.body().getAbout().getCuisines().size() == 1) {
                            //  cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName());
                            // clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName());
                        } else if (response.body().getAbout().getCuisines().size() == 2) {
                            //  cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName() + response.body().getAbout().getCuisines().get(1).getName());
                            // clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(1).getName());
                        } else if (response.body().getAbout().getCuisines().size() == 3) {
                            // cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName() + response.body().getAbout().getCuisines().get(1).getName() + response.body().getAbout().getCuisines().get(2).getName());
                            // clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(1).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(2).getName());
                        } else {
                            //  cusines_details.setText(response.body().getAbout().getCuisines().get(0).getName() + response.body().getAbout().getCuisines().get(1).getName() + response.body().getAbout().getCuisines().get(2).getName() + response.body().getAbout().getCuisines().get(3).getName());
                            // clent_submenu.setText(response.body().getAbout().getCuisines().get(0).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(1).getName().replace(",", " | ") + response.body().getAbout().getCuisines().get(2).getName());
                        }

                        //   clent_name.setText(response.body().getAbout().getGooglemaps().getName());


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
                        mapFragment.getMapAsync((OnMapReadyCallback) Item_Menu_Activity.this);

                        getLocationFromAddress(Item_Menu_Activity.this, shop_address.getText().toString(),info_popup);

                        jobdetails6 = (response.body().getAbout().getOpeninghours());


                        MoreinfoopenhrsAdapter adapter = new MoreinfoopenhrsAdapter(mContext,jobdetails6);
                        open_hrs_review.setHasFixedSize(true);
                        open_hrs_review.setLayoutManager(new LinearLayoutManager(Item_Menu_Activity.this));
                        open_hrs_review.setAdapter(adapter);

                    } else {

                    }
                } else {

                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }

            }


            @Override
            public void onFailure(Call<about_us_model> call, Throwable t) {
                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }


        });


        /*------------------------------------End open time And closed time----------------------------------*/

        //   info_popup.show();
        info_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        info_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        info_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        info_popup.getWindow().setGravity(Gravity.BOTTOM);


    }

    /* ------------------------Google Map--------------------------*/
    public LatLng getLocationFromAddress(Context context, String strAddress, Dialog info_popup) {

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
            info_popup.findViewById(R.id.direction).setVisibility(View.VISIBLE);

            info_popup.findViewById(R.id.direction).setOnClickListener(new View.OnClickListener() {
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



    private void HeartIcon() {

        heart_popup = new Dialog(this);
        heart_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        heart_popup.setContentView(R.layout.heart_popup_design);

        ImageView favourite_image = heart_popup.findViewById(R.id.favourite_image);
        AppCompatButton favourite_btn = heart_popup.findViewById(R.id.favourite_btn);

        Glide.with(this).load(R.drawable.heartgif).into(favourite_image);

        favourite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String clientId = String.valueOf(favourite_client);

                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,baseUrl+"addfavroitelist",
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {

                                    JSONObject jsonobject = new JSONObject(response);

                                    String check_status = jsonobject.getString("status");


                                    if(check_status.equalsIgnoreCase("true")){

                                        String check_msg = jsonobject.getString("data");

                                        Log.d("Favourite","" + check_msg);

                                        if(check_msg.equalsIgnoreCase("insert successfully")){

                                            ((CardView) heart_icon).setCardBackgroundColor(Color.parseColor("#e0467c"));

                                        }else{

                                            ((CardView) heart_icon).setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                                        }


                                    }


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


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("user_id",user_id);
                        params.put("path",menu_favourite_path);
                        params.put("client_id",clientId);
                        return params;
                    }
                };

                RequestQueue requestqueue = Volley.newRequestQueue(Item_Menu_Activity.this);
                requestqueue.add(stringRequest);

                heart_popup.dismiss();
            }
        });

        heart_popup.show();
        heart_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        heart_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        heart_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        heart_popup.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void Menulistpopup() {

        menulistpopup = new Dialog(this);
        menulistpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menulistpopup.setContentView(R.layout.menu_page_pop_up);

        menu_item_list_view = menulistpopup.findViewById(R.id.menu_item_list_view);


        Menulistview(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,menulistpopup,key_lat,key_lon);

        Log.d("Item_Menu_Activity",menuurlpath);
        Log.d("Item_Menu_Activity",""+sharedpreferences.getString("ordermodetype", null));
        Log.d("Item_Menu_Activity",key_postcode);
        Log.d("Item_Menu_Activity",key_area);
        Log.d("Item_Menu_Activity",key_address);

        //menulistpopup.show();
        menulistpopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        menulistpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        menulistpopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        menulistpopup.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void Menulistview(String menuurlpath, String ordermodetype, String key_postcode, String key_area, String key_address, Dialog menulistpopup, String menu_lat, String menu_lon) {

        String Menu_Url_Path =  menuurlpath +"/menu";

        menuListViewAdapter = new MenuListViewAdapter(menu_page_listmodel,Item_Menu_Activity.this,menulistpopup);
        LayoutManager manager4 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        menu_item_list_view.setLayoutManager(manager4);
        menu_item_list_view.setAdapter(menuListViewAdapter);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,baseUrl+Menu_Url_Path,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonobject = new JSONObject(response);
                            JSONObject getdata =jsonobject.getJSONObject("menu");
                            JSONArray menu_list = getdata.getJSONArray("searchcategory");

                            for (int i = 0; i < menu_list.length(); i++) {
                                JSONObject object = menu_list.getJSONObject(i);
                                Menu_Page_listmodel popularlist = new Menu_Page_listmodel(
                                        object.getString("name")

                                );
                                menu_page_listmodel.add(popularlist);

                            }

                            menuListViewAdapter.notifyDataSetChanged();


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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("ordermode",ordermodetype);
                params.put("postcode",key_postcode);
                params.put("area",key_area);
                params.put("address_location",key_address);
                params.put("lat",menu_lat);
                params.put("lng",menu_lon);
                return params;
            }
        };

        RequestQueue requestqueue = Volley.newRequestQueue(this);
        requestqueue.add(stringRequest);


    }


    /*---------------------------collection only----------------------------------------------------*/
    public BroadcastReceiver mclasbackonly = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (reloadback.equalsIgnoreCase("1")) {
                startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                finish();
            } else if (reloadback.equalsIgnoreCase("2")) {
                startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
                finish();
            } else if (reloadback.equalsIgnoreCase("3")) {
                startActivity(new Intent(getApplicationContext(), Show_Offer_Activity.class));
                finish();
            } else if(reloadback.equalsIgnoreCase("4")){

                //startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
                finish();

            }else{
                startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                finish();
            }
        }
    };





    private TextWatcher mDateEntryWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String words = s.toString();
            Log.e("wordlenth1", "" + s.toString());
            if (words.length() >= 3) {
                recyclerviewitem.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
                search_menu_item(words, sharedpreferences.getString("ordermodetype", null));
            } else if (words.length() == 0) {
                View view = Item_Menu_Activity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mAddFab.setVisibility(View.VISIBLE);
            } else {
                recyclerviewitem.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                mAddFab.setVisibility(View.GONE);
                mfab_close.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable == editTextSearch.getEditableText()) {
                top_editTextSearch.setText(editable);
                // DO STH
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    /*---------------------------collection only----------------------------------------------------*/
    public BroadcastReceiver mMessagecollectononly = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            //ordertypes(menuurlpath, ordertypevalue);//ordertype change api call
            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
            editor_extra.putString("ordermodetype", "1");
            editor_extra.commit();

            mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();

            menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api
            Log.e("ordertypevalue", "" + sharedpreferences.getString("ordermodetype", null));


        }
    };


    /*---------------------------Offer details shoe----------------------------------------------------*/
    public BroadcastReceiver mMessagepromooff = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            promocode = intent.getStringExtra("promocode");
            menu_offer_details_view.setVisibility(View.VISIBLE);
            confirm_code.setText(promocode);
            details_offers_show.setText("\u2022  Promo Code applicable on all orders." + "\n"
                    + "\u2022  Offer will be applicable on your first order only." + "\n"
                    + "\u2022  Offer is valid only for this particular restaurant/takeaway." + "\n"
                    + "\u2022  Apply the promo code at the checkout." + "\n"
                    + "\u2022  Other T & C may also apply.");
            confirm_copy_button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            menu_offer_details_view.setVisibility(View.GONE);
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("label", promocode);
                            clipboard.setPrimaryClip(clip);
                        }
                    });
        }
    };
    public BroadcastReceiver mMessageitempossion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            itempossion = intent.getStringExtra("itempossion");
            String item_postion_name = intent.getStringExtra("itempossionname");


            menu_list_view.setText(item_postion_name);
            Log.e("itempossion", "" + itempossion);


            if (itempossion == null) {
                final float y = recyclerviewitem.getChildAt(Integer.parseInt("1")).getY();
                nsv.post(new Runnable() {
                    @Override
                    public void run() {
                        nsv.fling(0);
                        nsv.smoothScrollTo(0, (int) y);

                    }
                });
            } else {
                final float y = recyclerviewitem.getChildAt(Integer.parseInt(itempossion)).getY();
                nsv.post(new Runnable() {
                    @Override
                    public void run() {
                        nsv.fling(0);
                        nsv.smoothScrollTo(0, (int) y);
                    }
                });
            }


            mfab_close.shrink();
            menu_view_card_view.setVisibility(View.GONE);
            mfab_close.setVisibility(View.GONE);
            mAddFab.setVisibility(View.VISIBLE);
        }
    };
    public BroadcastReceiver mMessageonlineoff = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            onlinecode = intent.getStringExtra("onlinecode");
            if (intent.getStringExtra("paymenttype").equalsIgnoreCase("0")) {
                onlinepaytypess = "Applicable only cash payments.";
            } else if (intent.getStringExtra("paymenttype").equalsIgnoreCase("1")) {
                onlinepaytypess = "Applicable only card payments.";
            } else {
                onlinepaytypess = "Applicable both cash and card payments.";
            }

            if (intent.getStringExtra("ordertyp").equalsIgnoreCase("0")) {
                onlinety = "This coupon code is applicable only for delivery orders.";
            } else if (intent.getStringExtra("ordertyp").equalsIgnoreCase("1")) {
                onlinety = "This coupon code is applicable only for collection orders.";
            } else {
                onlinety = "This coupon code is applicable for both delivery & collection orders.";
            }
            menu_offer_details_view.setVisibility(View.VISIBLE);
            confirm_code.setText(onlinecode);
            details_offers_show.setText("\u2022  Coupon code applicable only on orders above £" + intent.getStringExtra("mintype") + "\n"
                    + "\u2022  Offer is valid only for this particular takeaway/restaurant." + "\n"
                    + "\u2022  No maximum limit to apply the coupon code." + "\n"
                    + "\u2022  Apply the coupon code at the time of checkout." + "\n"
                    + "\u2022  Offer valid only till " + intent.getStringExtra("datetype") + "\n"
                    + "\u2022  " + onlinety + "\n"
                    + "\u2022  " + onlinepaytypess + "\n"
                    + "\u2022  Others T & C's may also apply.");

            confirm_copy_button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            menu_offer_details_view.setVisibility(View.GONE);
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("label", onlinecode);
                            clipboard.setPrimaryClip(clip);
                        }
                    });
        }
    };








    /*---------------------------get order mode coll delivery----------------------------------------------------*/
    private void collDelivery(String smenuurlpath) {
        fullUrl = smenuurlpath + "/collDelivery";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<collDelivery_model> call = apiService.changecollDelivery(fullUrl);
        call.enqueue(new Callback<collDelivery_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<collDelivery_model> call, Response<collDelivery_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                Log.e("itemstatusCode", "" + statusCode);
                if (statusCode == 200) {
                    Log.e("itemstatusrepos", "" + response.body().getStatus());
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        Log.e("itemstatusdell", "" + response.body().getData().getDelivery());
                        Log.e("itemstatuscoll", "" + response.body().getData().getCollection());

                        if (response.body().getData().getDelivery().equalsIgnoreCase("0") && response.body().getData().getCollection().equalsIgnoreCase("1")) {
                            if (sharedpreferences.getString("ordermodetype", null) == null) {


                                delivery_one_tex.setText("Delivery");
                                bikeimgonlydelivery.setImageResource(R.drawable.menu_delivery);
                                SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                editor_extra.putString("ordermodetype", "0");
                                editor_extra.commit();
                                menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api


                            } else {
                                if (sharedpreferences.getString("ordermodetype", null).equalsIgnoreCase("0")) {


                                    delivery_one_tex.setText("Delivery");
                                    bikeimgonlydelivery.setImageResource(R.drawable.menu_delivery);
                                    SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                    editor_extra.putString("ordermodetype", "0");
                                    editor_extra.commit();
                                    menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api


                                } else if (sharedpreferences.getString("ordermodetype", null).equalsIgnoreCase("1")) {


                                    delivery_one_tex.setText("Collection");
                                    bikeimgonlydelivery.setImageResource(R.drawable.menu_collection);
                                    SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                    editor_extra.putString("ordermodetype", "1");
                                    editor_extra.commit();
                                    menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api


                                }

                            }
                        } else if (response.body().getData().getDelivery().equalsIgnoreCase("") && response.body().getData().getCollection().equalsIgnoreCase("1")) {


                            delivery_one_tex.setText("Collection");
                            bikeimgonlydelivery.setImageResource(R.drawable.menu_collection);
                            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                            editor_extra.putString("ordermodetype", "1");
                            editor_extra.commit();
                            menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api


                        } else if (response.body().getData().getDelivery().equalsIgnoreCase("0") && response.body().getData().getCollection().equalsIgnoreCase("")) {


                            delivery_one_tex.setText("Delivery");
                            bikeimgonlydelivery.setImageResource(R.drawable.menu_delivery);
                            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                            editor_extra.putString("ordermodetype", "0");
                            editor_extra.commit();
                            menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api


                        } else if (response.body().getData().getDelivery().equalsIgnoreCase("") && response.body().getData().getCollection().equalsIgnoreCase("")) {

                            delivery_one_tex.setText("Delivery");
                            bikeimgonlydelivery.setImageResource(R.drawable.menu_delivery);
                            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                            editor_extra.putString("ordermodetype", "0");
                            editor_extra.commit();
                            menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api


                        }
                    } else {
                        Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<collDelivery_model> call, Throwable t) {
                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }




    public void Order_mode_popup() {

        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
        editor_extra.putString("pop_up_show","2");
        editor_extra.commit();

        dialog_order_mode_popup = new Dialog(this);
        dialog_order_mode_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_order_mode_popup.setContentView(R.layout.pre_order);


        CardView ordermode_popup_view  =dialog_order_mode_popup.findViewById(R.id.ordermode_popup_view);
        TextView colloetion_tattime = dialog_order_mode_popup.findViewById(R.id.colloetion_tattime);
        TextView delivery_tattime = dialog_order_mode_popup.findViewById(R.id.delivery_tattime);
        RelativeLayout delivery_but = dialog_order_mode_popup.findViewById(R.id.delivery_but);
        RelativeLayout collection_but = dialog_order_mode_popup.findViewById(R.id.collection_but);
        TextView delivery_txt = dialog_order_mode_popup.findViewById(R.id.delivery_txt);
        TextView colli_txt = dialog_order_mode_popup.findViewById(R.id.colli_txt);
        GifImageView loading_imageView1 = dialog_order_mode_popup.findViewById(R.id.loading_imageView1);
        GifImageView loading_imageView2 = dialog_order_mode_popup.findViewById(R.id.loading_imageView2);
        AppCompatButton update_mode = dialog_order_mode_popup.findViewById(R.id.update_mode);
        AppCompatButton bun_asap = dialog_order_mode_popup.findViewById(R.id.bun_asap);
        TextView sevenday_txt =dialog_order_mode_popup.findViewById(R.id.sevenday_txt);
        LinearLayout card_change = dialog_order_mode_popup.findViewById(R.id.card_change);
        LinearLayout today_time_layer = dialog_order_mode_popup.findViewById(R.id.today_time_layer);
        LinearLayout later_time_layer = dialog_order_mode_popup.findViewById(R.id.later_time_layer);
        AppCompatButton bun_later = dialog_order_mode_popup.findViewById(R.id.bun_later);
        AppCompatButton  bun_today = dialog_order_mode_popup.findViewById(R.id.bun_today);
        Spinner today_time = dialog_order_mode_popup.findViewById(R.id.today_time);
        Spinner later_time = dialog_order_mode_popup.findViewById(R.id.later_time);
        Spinner later_date = dialog_order_mode_popup.findViewById(R.id.later_date);
        RelativeLayout later_timing_layer =dialog_order_mode_popup.findViewById(R.id.later_timing_layer);

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();


        metdpasfullUrl = menuurlpath + "/loadPreOrderPop";

        Log.d("metdpasfullUrl",metdpasfullUrl);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<modeof_order_popup_model> call = apiService.modeofordershow(metdpasfullUrl);
        call.enqueue(new Callback<modeof_order_popup_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<modeof_order_popup_model> call, Response<modeof_order_popup_model> response) {
                //response.headers().get("Set-Cookie");
                ordermode_popup_view.setVisibility(View.VISIBLE);
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        mAddFab.setVisibility(View.GONE);

                        //Cooking time set
                        colloetion_tattime.setText(response.body().getData().getCollection().getCooking_time());
                        delivery_tattime.setText(response.body().getData().getDelivery().getCooking_time());


                        if (!response.body().getData().getDelivery().getStatus().equalsIgnoreCase("0")) {
                            loading();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismissloading();
                                    delivery_but.performClick();
                                }
                            }, 1000);



                        } else if (!response.body().getData().getCollection().getStatus().equalsIgnoreCase("0")) {
                            loading();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismissloading();
                                    collection_but.performClick();

                                }
                            }, 1000);

                        } else {

                            mAddFab.setVisibility(View.VISIBLE);
                        }


                        delivery_but.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loading();
                                        // Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                                        delivery_but.setBackgroundTintList(ContextCompat.getColorStateList(Item_Menu_Activity.this, R.color.pre_mode_bg_one));
                                        collection_but.setBackgroundTintList(ContextCompat.getColorStateList(Item_Menu_Activity.this, R.color.pre_mode_bg_two));
                                        delivery_txt.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_one));
                                        delivery_tattime.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_one));
                                        colli_txt.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_two));
                                        colloetion_tattime.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_two));

                                        loading_imageView1.setVisibility(View.VISIBLE);
                                        loading_imageView2.setVisibility(View.GONE);

                                        order_mode = "0";//Delivery

                                        if (response.body().getData().getDelivery().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                            sevenday_txt.setVisibility(View.GONE);
                                        } else {
                                            sevenday_txt.setVisibility(View.VISIBLE);
                                            sevenday_txt.setText("Select a delivery time" + "\n" + " up to 7 days in advance");
                                        }

                                        if (response.body().getData().getDelivery().getStatus().equalsIgnoreCase("0")) {

                                            card_change.setVisibility(View.GONE);
                                            today_time_layer.setVisibility(View.GONE);
                                            later_time_layer.setVisibility(View.GONE);
                                            update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                            update_mode.setClickable(false);
                                            update_mode.setFocusable(false);
                                            update_mode.setEnabled(false);
                                            delivery_tattime.setText("Unavailable");
                                            update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                            update_mode.setText("Takeaway Closed for Delivery");
                                            update_mode.setVisibility(View.VISIBLE);
                                            dismissloading();

                                        } else {

                                            card_change.setVisibility(View.VISIBLE);
                                            delivery_tattime.setText(response.body().getData().getDelivery().getCooking_time());
                                            dismissloading();
                                            if (response.body().getData().getDelivery().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                if (response.body().getData().getDelivery().getToday().getStatus().equalsIgnoreCase("0")) {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_later.performClick();
                                                        }
                                                    }, 1000);
                                                } else {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_today.performClick();
                                                        }
                                                    }, 1000);
                                                }
                                            } else {
                                                loading();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dismissloading();
                                                        bun_asap.performClick();
                                                    }
                                                }, 1000);
                                            }
                                        }

                                    }
                                });


                        collection_but.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loading();
                                        delivery_but.setBackgroundTintList(ContextCompat.getColorStateList(Item_Menu_Activity.this, R.color.pre_mode_bg_two));
                                        collection_but.setBackgroundTintList(ContextCompat.getColorStateList(Item_Menu_Activity.this, R.color.pre_mode_bg_one));
                                        delivery_txt.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_two));
                                        delivery_tattime.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_two));
                                        colli_txt.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_one));
                                        colloetion_tattime.setTextColor(ContextCompat.getColor(Item_Menu_Activity.this, R.color.pre_mode_txt_one));

                                        loading_imageView1.setVisibility(View.GONE);
                                        loading_imageView2.setVisibility(View.VISIBLE);
                                        order_mode = "1";//Collection

                                        if (response.body().getData().getCollection().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                            sevenday_txt.setVisibility(View.GONE);

                                        } else {
                                            sevenday_txt.setVisibility(View.VISIBLE);
                                            sevenday_txt.setText("Select a collection time" + "\n" + " up to 7 days in advance");
                                        }

                                        if (response.body().getData().getCollection().getStatus().equalsIgnoreCase("0")) {
                                            card_change.setVisibility(View.GONE);
                                            today_time_layer.setVisibility(View.GONE);
                                            later_time_layer.setVisibility(View.GONE);
                                            update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                            update_mode.setClickable(false);
                                            update_mode.setFocusable(false);
                                            update_mode.setEnabled(false);
                                            colloetion_tattime.setText("Unavailable");
                                            update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                            update_mode.setText("Takeaway Closed for Collection");
                                            update_mode.setVisibility(View.VISIBLE);
                                            dismissloading();
                                        } else {
                                            dismissloading();
                                            card_change.setVisibility(View.VISIBLE);

                                            colloetion_tattime.setText(response.body().getData().getCollection().getCooking_time());
                                            if (response.body().getData().getCollection().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                if (response.body().getData().getCollection().getToday().getStatus().equalsIgnoreCase("0")) {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_later.performClick();

                                                        }
                                                    }, 1000);

                                                } else {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_today.performClick();
                                                        }
                                                    }, 1000);


                                                }
                                            } else {
                                                loading();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dismissloading();
                                                        bun_asap.performClick();
                                                    }
                                                }, 1000);


                                            }

                                        }

                                    }
                                });


                        bun_asap.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        loading();

                                        activetagstr = "1";


                                        todaytimestr = "";
                                        laterdatestr = "";
                                        latertimestr = "";


                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getAsap().getStatus().equalsIgnoreCase("0")) {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getAsap().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();

                                            } else {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                update_mode.setText("Deliver ASAP");
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            }
                                        } else {
                                            if (response.body().getData().getCollection().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getAsap().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                update_mode.setText("Collection ASAP");
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            }
                                        }

                                    }
                                });


                        bun_today.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loading();
                                        activetagstr = "2";
                                        laterdatestr = "";
                                        latertimestr = "";



                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getToday().getStatus().equalsIgnoreCase("0")) {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getToday().getMessage());


                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            } else {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                // update_mode.setText("Deliver today at " + response.body().getData().getDelivery().getToday().getToday_time().get(0).getToday_time());


                                                today_time_layer.setVisibility(View.VISIBLE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                                ArrayList<AdapterListData> todaytimeitem = new ArrayList<AdapterListData>();

                                                for (int i = 0; i < response.body().getData().getDelivery().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem.add(new AdapterListData(
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time_string(),
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time(),
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).gettoday_label()

                                                    ));

                                                }


                                                ArrayAdapter<AdapterListData> todaytimeadapter;
                                                todaytimeadapter = new ArrayAdapter<AdapterListData>(getApplicationContext(), android.R.layout.simple_list_item_1, todaytimeitem);
                                                //setting adapter to spinner
                                                today_time.setAdapter(todaytimeadapter);

                                                today_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        // selectedtodaytimeItem = parent.getItemAtPosition(position).toString();

                                                        AdapterListData  todaytime = (AdapterListData)parent.getItemAtPosition(position);

                                                        //Toast.makeText(getApplicationContext(),todaytime.today_time_string, Toast.LENGTH_LONG).show();

                                                        //   update_mode.setText("Deliver today at " + todaytime.today_time);




                                                        update_mode.setText("Deliver "+ todaytime.label +" at " + todaytime.today_time);

                                                        todaytimestr =todaytime.today_time;
                                                        todaytimestring = todaytime.today_time_string;

                                                        if(todaytimestr.equalsIgnoreCase("Mid Night")){
                                                            update_mode.setText("Deliver "+ todaytime.label +" at 12:00" );
                                                        }




                                                    /*    update_mode.setText("Deliver today at " + selectedtodaytimeItem);
                                                        todaytimestr = selectedtodaytimeItem;*/
                                                        update_mode.setVisibility(View.VISIBLE);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });

                                                dismissloading();

                                            }
                                        } else {


                                            if (response.body().getData().getCollection().getToday().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getToday().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                update_mode.setText("Takeaway Closed for Collection");
                                                dismissloading();

                                            } else {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                //  update_mode.setText("Collection today at " + response.body().getData().getCollection().getToday().getToday_time().get(0).getToday_time());
                                                today_time_layer.setVisibility(View.VISIBLE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);


                                                ArrayList<AdapterListData> todaytimeitem = new ArrayList<AdapterListData>();



                                                for (int i = 0; i < response.body().getData().getCollection().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem.add(new AdapterListData(
                                                            response.body().getData().getCollection().getToday().getToday_time().get(i).getToday_time_string(),
                                                            response.body().getData().getCollection().getToday().getToday_time().get(i).getToday_time(),
                                                            response.body().getData().getCollection().getToday().getToday_time().get(i).gettoday_label()
                                                    ));

                                                }
                                                ArrayAdapter<AdapterListData> todaytimeadapter;
                                                todaytimeadapter = new ArrayAdapter<AdapterListData>(getApplicationContext(), android.R.layout.simple_list_item_1, todaytimeitem);
                                                //setting adapter to spinner
                                                today_time.setAdapter(todaytimeadapter);


                                                today_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        // selectedtodaytimeItem = parent.getItemAtPosition(position).toString();

                                                        AdapterListData  todaytime = (AdapterListData)parent.getItemAtPosition(position);
                                                        //  Toast.makeText(getApplicationContext(),todaytime.today_time_string, Toast.LENGTH_LONG).show();
                                                        update_mode.setText("Collection "+todaytime.label+" at " + todaytime.today_time);
                                                        todaytimestr = todaytime.today_time;
                                                        todaytimestring = todaytime.today_time_string;

                                                        if(todaytimestr.equalsIgnoreCase("Mid Night")){
                                                            update_mode.setText("Collection "+ todaytime.label +" at 12:00 " );
                                                        }

                                                        update_mode.setVisibility(View.VISIBLE);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });

                                                dismissloading();
                                            }
                                        }

                                    }

                                });


                        bun_later.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        activetagstr = "3";
                                        todaytimestr = "";

                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getLater_array().getStatus().equalsIgnoreCase("0")) {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getLater_array().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                            } else {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.VISIBLE);
                                                update_mode.setVisibility(View.VISIBLE);

                                                String[] laterdateitem = new String[response.body().getData().getDelivery().getLater_array().getLater_date().size()];
                                                for (int i = 0; i < response.body().getData().getDelivery().getLater_array().getLater_date().size(); i++) {
                                                    //Storing names to string array
                                                    laterdateitem[i] = response.body().getData().getDelivery().getLater_array().getLater_date().get(i).getLater_date();
                                                }

                                                ArrayAdapter<String> laterdateadapter;
                                                laterdateadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, laterdateitem);
                                                //setting adapter to spinner
                                                later_date.setAdapter(laterdateadapter);


                                                later_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        selectedlaterdateItem = parent.getItemAtPosition(position).toString();
                                                        // Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();

                                                        // update_mode.setText("Deliver at " + selectedlaterdateItem);
                                                        laterdatestr = selectedlaterdateItem;
                                                        loadLatertime("0", selectedlaterdateItem);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });


                                            }
                                        } else {
                                            if (response.body().getData().getCollection().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getLater_array().getMessage());
                                                update_mode.setVisibility(View.VISIBLE);
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));


                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.VISIBLE);
                                                update_mode.setVisibility(View.VISIBLE);

                                                String[] laterdateitem = new String[response.body().getData().getCollection().getLater_array().getLater_date().size()];
                                                for (int i = 0; i < response.body().getData().getCollection().getLater_array().getLater_date().size(); i++) {
                                                    //Storing names to string array
                                                    laterdateitem[i] = response.body().getData().getCollection().getLater_array().getLater_date().get(i).getLater_date();
                                                }

                                                ArrayAdapter<String> laterdateadapter;
                                                laterdateadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, laterdateitem);
                                                //setting adapter to spinner
                                                later_date.setAdapter(laterdateadapter);


                                                later_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        selectedlaterdateItem = parent.getItemAtPosition(position).toString();
                                                        // Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();
                                                        laterdatestr = selectedlaterdateItem;
                                                        // update_mode.setText("Collection at " + selectedlaterdateItem);
                                                        loadLatertime("1", selectedlaterdateItem);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });
                                            }
                                        }

                                    }

                                    /*---------------------------load Later Time----------------------------------------------------*/
                                    private void loadLatertime(String ordermodeing, String laterdates) {
                                        loading();
                                        // get user data from session
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("later_time", laterdates);
                                        params.put("order_type", ordermodeing);


                                        mlaterdatefullUrl = menuurlpath + "/loadLaterTime";


                                        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                                        Call<getlatertime_model> call = apiService.loadLaterTime(mlaterdatefullUrl, params);
                                        call.enqueue(new Callback<getlatertime_model>() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onResponse(Call<getlatertime_model> call, Response<getlatertime_model> response) {

                                                //response.headers().get("Set-Cookie");
                                                int statusCode = response.code();
                                                if (statusCode == 200) {
                                                    if (response.body().getData().getStatus().equalsIgnoreCase("true")) {

                                                        later_timing_layer.setVisibility(View.VISIBLE);


                                                        ArrayList<AdapterListData> latertimeitem = new ArrayList<AdapterListData>();

                                                        for (int i = 0; i <response.body().getData().getLater_time().size(); i++) {
                                                            //Storing names to string array

                                                            latertimeitem.add(new AdapterListData(
                                                                    response.body().getData().getLater_time().get(i).getLater_time_string(),
                                                                    response.body().getData().getLater_time().get(i).getLater_time(),""
                                                            ));

                                                        }



                                                      /*
                                                        String[] latertimeitem = new String[response.body().getData().getLater_time().size()];
                                                        for (int i = 0; i < response.body().getData().getLater_time().size(); i++) {
                                                            //Storing names to string array
                                                            latertimeitem[i] = response.body().getData().getLater_time().get(i).getLater_time();
                                                        }*/

                                                        //response.body().getData().getLater_time().get(i).getLater_time_string()

                                                        ArrayAdapter<AdapterListData> latertimeadapter;
                                                        latertimeadapter = new ArrayAdapter<AdapterListData>(getApplicationContext(), android.R.layout.simple_list_item_1, latertimeitem);
                                                        //setting adapter to spinner
                                                        later_time.setAdapter(latertimeadapter);


                                                        later_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                                AdapterListData  latertime = (AdapterListData)parent.getItemAtPosition(position);


                                                              /*  update_mode.setText("Collection today at " + todaytime.today_time);
                                                                todaytimestr = todaytime.today_time;
                                                                todaytimestring = todaytime.today_time_string;*/

                                                                //  selectedlatertimeItem = parent.getItemAtPosition(position).toString();


                                                                if (ordermodeing.equalsIgnoreCase("0")) {
                                                                    update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                                    update_mode.setClickable(true);
                                                                    update_mode.setFocusable(true);
                                                                    update_mode.setEnabled(true);
                                                                    update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                                    update_mode.setText("Deliver " +laterdates+ " at " +latertime.today_time);
                                                                    update_mode.setVisibility(View.VISIBLE);
                                                                    latertimestr = latertime.today_time;
                                                                    latertimestring = latertime.today_time_string;
                                                                    dismissloading();
                                                                } else {
                                                                    update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                                    update_mode.setClickable(true);
                                                                    update_mode.setFocusable(true);
                                                                    update_mode.setEnabled(true);
                                                                    update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                                                    update_mode.setText("Collection " +laterdates+ " at " + latertime.today_time);
                                                                    update_mode.setVisibility(View.VISIBLE);
                                                                    latertimestr = latertime.today_time;
                                                                    latertimestring = latertime.today_time_string;
                                                                    dismissloading();
                                                                }
                                                            } // to close the onItemSelected

                                                            public void onNothingSelected(AdapterView<?> parent) {
                                                            }
                                                        });
                                                    } else {
                                                        later_timing_layer.setVisibility(View.GONE);
                                                        update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                        update_mode.setClickable(false);
                                                        update_mode.setFocusable(false);
                                                        update_mode.setEnabled(false);
                                                        update_mode.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.modeofitem_disable_txt));
                                                        update_mode.setText("Later Unavailable");
                                                        update_mode.setVisibility(View.VISIBLE);
                                                        dismissloading();
                                                    }
                                                } else {
                                                    dismissloading();
                                                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                                                }
                                            }


                                            @Override
                                            public void onFailure(Call<getlatertime_model> call, Throwable t) {
                                                dismissloading();
                                                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                                                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });


                        update_mode.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                        editor_extra.putString("ordermodetype", order_mode);
                                        editor_extra.putString("orderactivetag", activetagstr);
                                        editor_extra.putString("ordertodattime", todaytimestr);
                                        editor_extra.putString("orderlaterdate", laterdatestr);
                                        editor_extra.putString("orderlatertime", latertimestr);
                                        editor_extra.putString("todaytimestring",todaytimestring);
                                        editor_extra.putString("latertimestring",latertimestring);
                                        editor_extra.putString("pop_up_show","1");
                                        editor_extra.commit();




                                        if (order_mode.equalsIgnoreCase("0")) {
                                            delivery_one_tex.setText("Delivery");
                                            bikeimgonlydelivery.setImageResource(R.drawable.menu_delivery);

                                        /*    delivery_collection_textview.setText("Delivery");
                                            cooking_time_textview.setText(response.body().getData().getDelivery().getCooking_time());*/

                                            mAddFab.setVisibility(View.VISIBLE);

                                        } else {
                                            delivery_one_tex.setText("Collection");
                                            bikeimgonlydelivery.setImageResource(R.drawable.menu_collection);

                                   /*         delivery_collection_textview.setText("Collection");
                                            cooking_time_textview.setText(response.body().getData().getCollection().getCooking_time());*/

                                            mAddFab.setVisibility(View.VISIBLE);
                                        }

                                        menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api
                                        Log.e("order_mode_details1", "" + order_mode);
                                        Log.e("order_mode_details2", "" + activetagstr);
                                        Log.e("order_mode_details3", "" + todaytimestr);
                                        Log.e("order_mode_details4", "" + laterdatestr);
                                        Log.e("order_mode_details5", "" + latertimestr);




                                        mode_view2.setEnabled(true);

                                        dialog_order_mode_popup.dismiss();
                                    }
                                });

                    } else {
                        // dismissloading();
                        //  ordermode_popup_view.setVisibility(View.INVISIBLE);
                        //      mode_view2.setVisibility(View.INVISIBLE);
                        mAddFab.setVisibility(View.VISIBLE);


                        menugetitem(menuurlpath, "0", key_postcode, key_area, key_address,key_lat,key_lon);//menu item call api

                    }


                } else {
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<modeof_order_popup_model> call, Throwable t) {

                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }
        });

        dialog_order_mode_popup.show();
        dialog_order_mode_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_order_mode_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_order_mode_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog_order_mode_popup.getWindow().setGravity(Gravity.BOTTOM);
    }

    /*---------------------------Offer RecyclerView ----------------------------------------------------*/
    private void menu_offer(String menuurlpath, String paymentmode, String ordermodeoffer) {
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("path", menuurlpath);
        params.put("payment_mode", paymentmode);
        params.put("ordermode", ordermodeoffer);

        Log.d("menu_offer", String.valueOf(params));

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<offer_code_model> call = apiService.offershow(params);
        call.enqueue(new Callback<offer_code_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<offer_code_model> call, Response<offer_code_model> response) {
                //response.headers().get("Set-Cookie");

                int statusCode = response.code();

                Log.e("menu_offer", new Gson().toJson(response.body()));

                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

//Online Offer
                        MenuOfferAdapter adapter = new MenuOfferAdapter(mContext, response.body().getDiscount_list().getDiscountcode(), response.body().getDiscount_list().getPromocode());
                        recyclerviewOffers.setHasFixedSize(true);
                        recyclerviewOffers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerviewOffers.setItemAnimator(new DefaultItemAnimator());
                        recyclerviewOffers.setAdapter(adapter);

//Promo Code
                        MenupromoAdapter adapterpromo = new MenupromoAdapter(mContext, response.body().getDiscount_list().getDiscountcode(), response.body().getDiscount_list().getPromocode());
                        recyclerviewpromo.setHasFixedSize(true);
                        recyclerviewpromo.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                        recyclerviewpromo.setItemAnimator(new DefaultItemAnimator());
                        recyclerviewpromo.setAdapter(adapterpromo);


                    }
                } else {
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<offer_code_model> call, Throwable t) {

                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*---------------------Get Item in menu----------------------------*/
    private void menugetitem(String menuurlpath, String menuparamesint, String str_key_postcode, String str_key_area, String str_key_address,String latvalue,String lonvalue) {
        // get user data from session

        if(menuparamesint==null){
            menuparamesint = "0";
        }


        Map<String, String> params = new HashMap<String, String>();

        //   HashMap<String, String> user = session.getUserDetails();

        params.put("ordermode", menuparamesint);
        params.put("postcode", str_key_postcode);
        params.put("area", str_key_area);
        params.put("address_location", str_key_address);
        params.put("lat", latvalue);
        params.put("lng", lonvalue);

        fullUrl = menuurlpath + "/menu";

        Log.e("numbers", "" + params + " " + fullUrl);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<menu_item_model> call = apiService.getmenuitem(fullUrl, params);
        call.enqueue(new Callback<menu_item_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<menu_item_model> call, Response<menu_item_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

//array value set textview

                        menu_clientname.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        menu_tvdesc.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        miles_textview.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        delicery_collection_time.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        min_order_amt.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;

                        menu_clientname.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        menu_tvdesc.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;


                        //client name
                        List<menu_item_model.all_topbanner> clientdetails = (response.body().getTopbanner());
                        menu_item_model.all_topbanner[] listdata = clientdetails.toArray(new menu_item_model.all_topbanner[0]);

                        menu_clientname.setText(listdata[0].getClientName());
                        miles_textview.setText(listdata[0].getmiles() + " Miles");

                        restaurants_status.setText(listdata[0].gettakeawaystatus());


                        if (listdata[0].gettakeawaystatus().equalsIgnoreCase("Closed")) {

                            restaurants_status.setText("CLOSED");
                            restaurants_status.setBackgroundResource(R.drawable.close_background);

                        } else if (listdata[0].gettakeawaystatus().equalsIgnoreCase("Preorder")) {

                            restaurants_status.setText("PRE-ORDER");
                            restaurants_status.setBackgroundResource(R.drawable.preorder_background);

                        } else if (listdata[0].gettakeawaystatus().equalsIgnoreCase("Order Now")) {

                            restaurants_status.setText("Order Now");
                            restaurants_status.setBackgroundResource(R.drawable.open_background);

                        } else {

                            restaurants_status.setText("CLOSED");
                            restaurants_status.setBackgroundResource(R.drawable.close_background);

                        }

                        delicery_collection_time.setText("20-30 min");

                        Picasso.get()
                                .load(listdata[0].getClienImage())
                                .placeholder(R.drawable.hederlocoplaceimg)
                                .error(R.drawable.hederlocoplaceimg)
                                .into(restaurants_image);

//client item
                        if (listdata[0].getCuisinename().size() == 0) {
                            menu_tvdesc.setVisibility(View.INVISIBLE);
                        } else if (listdata[0].getCuisinename().size() == 1) {
                            menu_tvdesc.setText(listdata[0].getCuisinename().get(0).getCuisine());
                        } else if (listdata[0].getCuisinename().size() == 2) {
                            menu_tvdesc.setText(listdata[0].getCuisinename().get(0).getCuisine() + " , " + listdata[0].getCuisinename().get(1).getCuisine());
                        } else if (listdata[0].getCuisinename().size() == 3) {
                            menu_tvdesc.setText(listdata[0].getCuisinename().get(0).getCuisine() + " , " + listdata[0].getCuisinename().get(1).getCuisine() + " , " + listdata[0].getCuisinename().get(2).getCuisine());
                        } else {
                            menu_tvdesc.setText(listdata[0].getCuisinename().get(0).getCuisine() + " , " + listdata[0].getCuisinename().get(1).getCuisine() + " , " + listdata[0].getCuisinename().get(2).getCuisine());
                        }


                        //   tvdesc
                        clent_rating.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        clent_rating.setText(listdata[0].getReviews_count());// item view list

                        jobdetails2 = (response.body().getMenu().getCategoryall());




                        MenuItemAdapter itemadapter = new MenuItemAdapter(mContext, (List<menu_item_sub_model.categoryall>) jobdetails2, menuurlpath,recyclerviewitem);
                        recyclerviewitem.setHasFixedSize(true);
                        recyclerviewitem.setLayoutManager(new LinearLayoutManager(Item_Menu_Activity.this));
                        // recyclerviewitem.getLayoutManager().scrollToPosition(2);
                        recyclerviewitem.setAdapter(itemadapter);

                       // ViewCompat.setNestedScrollingEnabled(recyclerviewitem, true);


                        List<menu_item_sub_model.searchcategory> menuitemdetails = (response.body().getMenu().getSearchcategory());
                        MenuserachcatAdapter menuitemadapter = new MenuserachcatAdapter(mContext, (List<menu_item_sub_model.searchcategory>) menuitemdetails);
                        recyclerviewmenuitem.setHasFixedSize(true);
                        recyclerviewmenuitem.setLayoutManager(new LinearLayoutManager(Item_Menu_Activity.this));
                        recyclerviewmenuitem.setAdapter(menuitemadapter);



                        /*------------------------------Add to cart to get item details------------------------------*/
                        intentitemdetails = getIntent();
                        if (intentitemdetails.getStringExtra("addonid") == null) {

                        } else {
                            //   cooking_insttructionback = intentitemdetails.getStringExtra("cooking_insttruction");
                            itemdetails();
                        }

                        
                    } else {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<menu_item_model> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
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


    /*---------------------------check internet connection----------------------------------------------------*/


    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Item_Menu_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Item_Menu_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_chk = new Internet_connection_checking(Item_Menu_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();

        }
    }

    /*---------------------------No addon direct item add-----------------------------*/
    public BroadcastReceiver mitemsuccessMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getContactsCount();
            menu_addon_item_view.setVisibility(View.GONE);
            mAddFab.setVisibility(View.VISIBLE);
            //bottom_nav.setVisibility(View.VISIBLE);
            add_to_cart_layout.setVisibility(View.VISIBLE);
            bottomNav.getOrCreateBadge(R.id.home_card).setNumber(cursor);
            //  total_item.setText(cursor + " Items");
            //  total_item.setText(cursor + "");

            ArrayList<String> get_qty_count = dbHelper.getqtycount();
            total_item.setText(get_qty_count.get(0) + "");
            Log.d("Cursor2 ", String.valueOf(get_qty_count.get(0) + ""));


            ArrayList<String> get_amt_count = dbHelper.gettotalamt();
            total_amount_textview.setText(String.format("%.2f", amtfloat + Double.parseDouble(get_amt_count.get(0) + "")));
            Log.d("TotalAmount",get_amt_count.get(0)+ " ");
        }
    };

    private void itemdetails() {
        // Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
        // Get extra data included in the Intent
        ItemName = intentitemdetails.getStringExtra("item");
        addonid = intentitemdetails.getStringExtra("addonid");
        categoryname = intentitemdetails.getStringExtra("categoryname");
        subcategoryname = intentitemdetails.getStringExtra("subcategoryname");

        addonitemarrayextraData = "";
        addonitemid = "";

        selected_addon_item_view.setVisibility(View.GONE);
        item_price_btn = true;

        Log.e("addbuttonclick1", "" + ItemName);
        Log.e("addbuttonclick2", "" + addonid);
        Log.e("addbuttonclick3", "" + categoryname);
        Log.e("addbuttonclick4", "" + subcategoryname);


        //add adddon id in array
        aidlist = new ArrayList<String>();
        aidlist.clear();


        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
        editor_extra.putString("addon_extra", "");
        editor_extra.commit();


        //Item Name
        listItems = new ArrayList<String>();
        listItems.clear();//.removeAll(Collections.singleton(arrayextranameData));
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.raw_simple_list_item, R.id.selected_item, listItems);
        selected_addon_item_view.setAdapter(adapter);

        arrayextranameDataadd = new ArrayList<String>();
        arrayextranameDataadd.clear();
        arrayextranameDataaddsize = new ArrayList<Integer>();
        arrayextranameDataaddsize.clear();


        listItemsidssize = new ArrayList<Integer>();
        listItemsidssize.clear();
        arrayaddonitemidadd = new ArrayList<>();
        arrayaddonitemidadd.clear();
        arrayaddonextraidsingleaddsize = new ArrayList<Integer>();
        arrayaddonextraidsingleaddsize.clear();
        arrayaddonextraidsingleadd = new ArrayList<>();
        arrayaddonextraidsingleadd.clear();

        //Item Id
        listItemsids = new ArrayList<String>();
        listItemsids.clear();//.removeAll(Collections.singleton(arrayaddonitemid));
        adapterids = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listItemsids);


        //Item Extra Addon
        listItemsexraids = new ArrayList<String>();
        listItemsexraids.clear();//.removeAll(Collections.singleton(arrayaddonitemid));
        adapterexraids = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listItemsexraids);
        //    Toast.makeText(Item_Menu_Activity.this, ItemName, Toast.LENGTH_SHORT).show();


        item_priceadd = new ArrayList<String>();
        item_priceadd.clear();

        item_pricesize = new ArrayList<>();
        item_pricesize.clear();


        addonitemfirstview(ItemName, addonid, "", "", "1");


    }

    /*---------------------------MenuItemAdapter item value get add button click----------------------------------------------------*/
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            ItemName = intent.getStringExtra("item");//Item ID
            addonid = intent.getStringExtra("addonid");//first addonId
            categoryname = intent.getStringExtra("categoryname");
            item_price_amt = intent.getStringExtra("item_price_amt");
            subcategoryname = intent.getStringExtra("subcategoryname");
            description = intent.getStringExtra("item_description");



            Log.d("Item_Amount_Test_Activity","Item_id----->" + ItemName + "Addon_id--->"+ addonid +
                                                                        "CategoryName----->"+categoryname+"itemPrice---->"+item_price_amt
                    + "subcategoryname--------->"+subcategoryname + " description--------->" + description);

            int userList = dbHelper.GetUserByUserId(parseInt(ItemName));

           // ArrayList<HashMap<String, String>> qtypice = dbHelper.Getqtyprice(parseInt(ItemName));



            if(userList == 0){


                addonitemarrayextraData = "";
                addonitemid = "";
                selected_addon_item_view.setVisibility(View.GONE);

                item_price_btn = true;


                Log.e("nextapi1", "" + nextaid);
                Log.e("nextapi2", "" + btnnextfir);
                Log.e("nextapi3", "" + addonlimit);

                Log.e("addbuttonclick1", "" + ItemName);
                Log.e("addbuttonclick2", "" + addonid);
                Log.e("addbuttonclick3", "" + categoryname);
                Log.e("addbuttonclick4", "" + subcategoryname);


                SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                editor_extra.putString("addon_extra", "");
                editor_extra.commit();


//add adddon id in array

                aidlist = new ArrayList<String>();
                aidlist.clear();


                //Item Name
                listItems = new ArrayList<String>();
                listItems.clear();//.removeAll(Collections.singleton(arrayextranameData));
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.raw_simple_list_item, R.id.selected_item, listItems);
                selected_addon_item_view.setAdapter(adapter);


                arrayextranameDataadd = new ArrayList<String>();
                arrayextranameDataadd.clear();
                arrayextranameDataaddsize = new ArrayList<Integer>();
                arrayextranameDataaddsize.clear();


                listItemsidssize = new ArrayList<Integer>();
                listItemsidssize.clear();
                arrayaddonitemidadd = new ArrayList<>();
                arrayaddonitemidadd.clear();


                arrayaddonextraidsingleaddsize = new ArrayList<Integer>();
                arrayaddonextraidsingleaddsize.clear();
                arrayaddonextraidsingleadd = new ArrayList<>();
                arrayaddonextraidsingleadd.clear();

                //Item Id
                listItemsids = new ArrayList<String>();
                listItemsids.clear();//.removeAll(Collections.singleton(arrayaddonitemid));
                adapterids = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listItemsids);

                //Item Extra Addon
                listItemsexraids = new ArrayList<String>();
                listItemsexraids.clear();//.removeAll(Collections.singleton(arrayaddonitemid));
                adapterexraids = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, listItemsexraids);


                item_priceadd = new ArrayList<String>();
                item_priceadd.clear();

                item_pricesize = new ArrayList<>();
                item_pricesize.clear();
                //    Toast.makeText(Item_Menu_Activity.this, ItemName, Toast.LENGTH_SHORT).show();

                addonitemfirstview(ItemName, addonid, "", "", "1");


            }else {

                ArrayList<HashMap<String, String>> qtypice = dbHelper.Getqtypriceaddon(parseInt(ItemName));

                for (int i=0;i<qtypice.size();i++)
                {
                    HashMap<String, String> hashmap= qtypice.get(i);
                    updateqty = hashmap.get("qty");
                    updatefinalamt = hashmap.get("itemaddontotalamt");
                }

                int database_qty =  Math.round(Float.parseFloat(updateqty));


                int qty  = database_qty + 1;

                //String price =item_price_amt;
                 String price1 = updatefinalamt;

                 Log.d("Item_Price_Amout"," " + price1);

                float total_amt = Float.parseFloat(price1) * qty;

                Boolean updatevalue  =  dbHelper.Updateqtyprice(parseInt(ItemName),qty,total_amt);

            }

        }
    };
    /*---------------------------MenuItemAdapter item value get addon button click----------------------------------------------------*/
    public BroadcastReceiver addonbtnnextid = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            btnnextfir = intent.getStringExtra("addonbtnnext");
            Log.e("menugetlimt1", "" + addonlimit);
            Log.e("menugetlimt2", "" + nextaid);
            Log.e("menugetlimt3", "" + btnnextfir);
            if (nextaid == null) {
                addonitemfirstview(ItemName, addonid, "", "", "1");
            } else {
                if (nextaid.equalsIgnoreCase("0") && btnnextfir.equalsIgnoreCase("0")) {
                    // Checkout.setText("PROCESS");
                    Checkout.setText("Proceed");
                } else {
                    //Checkout.setText("NEXT");
                    Checkout.setText("Proceed");
                }
                if (addonlimit.equalsIgnoreCase("1")) {
                    Checkout.setEnabled(false);
                    Checkout.setBackgroundColor(Checkout.getContext().getResources().getColor(R.color.line_col));
                    Checkout.setTextColor(Checkout.getContext().getResources().getColor(R.color.menu_arrow));
                } else {
                    Checkout.setEnabled(true);
                    Checkout.setBackgroundColor(Checkout.getContext().getResources().getColor(R.color.menu_text_bg));
                    Checkout.setTextColor(Checkout.getContext().getResources().getColor(R.color.white));
                }
            }
        }
    };
    /*---------------------------MenuItemAdapter item value get addon extra button click----------------------------------------------------*/
    public BroadcastReceiver addonmMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get extra data included in the Intent
            addonitemid = intent.getStringExtra("addonitemid");
            addonidprice = intent.getStringExtra("addonidprice");
            addonitemtype = intent.getStringExtra("addonitemtype");
            //addonitemposition = intent.getStringExtra("addonidposion");
            addonitemarrayextraData = intent.getStringExtra("arrayextraData");
            arrayextranameData = intent.getStringExtra("arrayextranameData");
            arrayaddonitemid = intent.getStringExtra("arrayaddonitemid");
            btnnextfir = intent.getStringExtra("addonbtnnext");
            arrayaddonextraidsingle = intent.getStringExtra("arrayaddonextraidsingle");
            Log.e("menugetlimt33", "" + btnnext);

            // item_add_time5
            Log.e("addonextramessage1", "" + addonitemid);
            Log.e("addonextramessage2", "" + addonidprice);
            Log.e("addonextramessage3", "" + addonitemtype);
            Log.e("addonextramessage4", "" + addonitemarrayextraData);
            Log.e("addonextramessage5", "" + arrayextranameData);
            Log.e("addonextramessage6", "" + arrayaddonitemid);
            Log.e("addonextramessage7", "" + btnnext);
            Log.e("addonextramessage8", "" + arrayaddonextraidsingle);


            Log.e("addonextramessage40", "" + listItemsexraids);
            Log.e("addonextramessage41", "" + itemexradsstr);


            if (arrayaddonextraidsingle.equalsIgnoreCase("0")) {
                addsingleextra = "No";
            } else if (arrayaddonextraidsingle.equalsIgnoreCase("1")) {
                addsingleextra = "Less";
            } else if (arrayaddonextraidsingle.equalsIgnoreCase("2")) {
                addsingleextra = "Half";
            } else if (arrayaddonextraidsingle.equalsIgnoreCase("3")) {
                addsingleextra = "On";
            } else if (arrayaddonextraidsingle.equalsIgnoreCase("4")) {
                addsingleextra = "With";
            } else if (arrayaddonextraidsingle.equalsIgnoreCase("5")) {
                addsingleextra = "On Burger";
            } else if (arrayaddonextraidsingle.equalsIgnoreCase("6")) {
                addsingleextra = "On Chips";
            } else {
                addsingleextra = "";
            }

            selected_addon_item_view.setVisibility(View.VISIBLE);
            Log.e("arrayaddonitemid", "" + arrayaddonitemid);
            Log.e("arrayaddonextraidsingle", "" + arrayaddonextraidsingle);

            Log.e("itemides1", "" + addonlimit);
            Log.e("itemides2", "" + nextaid);
            Log.e("itemides3", "" + btnnext);
            Log.e("getlimt4", "" + addonlimit);
            Log.e("getlimt5", "" + nextaid);
            Log.e("getlimt6", "" + btnnext);
            Log.e("getlimt7", "" + arrayextranameData);
            Log.e("getlimt8", "" + addonitemtype);
            Log.e("addonitemid4", "" + addonitemid);
            Log.e("addonitemid5", "" + addonitemarrayextraData);

            Log.e("addonval", "" + addonitemid);
            // Log.e("addonva2", "" + addonextrastringName);
            Log.e("addonva3", "" + addonidprice);

            //amount calucluate
            if (addonitemtype.equalsIgnoreCase("0")) {  //add amount

                listItems.add(addsingleextra + " " + arrayextranameData);
                adapter.notifyDataSetChanged();

                listItemsids.add(arrayaddonitemid);
                adapterids.notifyDataSetChanged();
                itemidsstr = listItemsids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();

                listItemsexraids.add(arrayaddonextraidsingle);
                adapterexraids.notifyDataSetChanged();
                itemexradsstr = listItemsexraids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();


                Log.e("nameaddonitem1", "" + addsingleextra);
                Log.e("nameaddonitem2", "" + arrayextranameData);
                Log.e("nameaddonitem3", "" + listItems);


//arrayextranameData
  /*              if(listItems.size()!=0){
                    listItemsaddon.add(listItems);
                    Log.e("listItemsaddon", "" + listItemsaddon);
                }
*/

                arrayextranameDataadd.add(arrayextranameData);
                arrayaddonitemidadd.add(arrayaddonitemid);    // addons id store in database
                arrayaddonextraidsingleadd.add(arrayaddonextraidsingle);

                Log.d("add_on_popup",arrayextranameDataadd +  "----> " +arrayaddonitemidadd+ "-----> " + arrayaddonextraidsingleadd);

                Log.e("firsttime1", "" + arrayextranameData);
                Log.e("firsttime2", "" + arrayextranameDataadd);
                Log.e("firsttime3", "" + listItems);

//check box check or not view
                checkval = checkval + 1;
                Log.e("checkval", "" + checkval);

                if (checkval == 0) {
                    backbutclknum = "2";
                } else {
                    backbutclknum = "1";
                }

                Log.e("arrayaddonextraidsingle", "" + arrayaddonextraidsingle);

                if (!arrayaddonextraidsingle.equalsIgnoreCase("0")) {
                    String currentString = item_amt.getText().toString();
                    String[] separated = currentString.split("£");
                    separated[0] = separated[0].trim();
                    separated[1] = separated[1].trim();

                    double num1 = Double.parseDouble(separated[1]);
                    double num2 = Double.parseDouble(addonidprice);//addonidprice
                    // add both number and store it to sum
                    sum = num1 + num2;

                    Log.e("addtotal1", "" + separated[1]);
                    Log.e("addtotal2", "" + addonidprice);
                    Log.e("addtotal3", "" + sum);


                    //  item_amt.setText("Total: £ " + String.format("%.2f", sum));

                    item_amt.setText(" £ " + String.format("%.2f", sum));

                    /*String[] separateditemprice = item_amt.getText().toString().split("£");
                    separateditemprice[0] = separateditemprice[0].trim();
                    separateditemprice[1] = separateditemprice[1].trim();
                    item_price = separateditemprice[1];

                    item_priceadd.add(item_price);
*/
                    Log.e("item_price2", "" + separated[1]);
                    Log.e("item_price3", "" + addonidprice);
                    Log.e("item_price4", "" + item_amt.getText().toString());
                    // Log.e("item_price5", "" + separateditemprice[1]);
                    Log.e("item_price6", "" + item_price);
                    Log.e("item_price7", "" + item_priceadd);

                }
            } else if (addonitemtype.equalsIgnoreCase("1")) {//remove amount
               /* listItemsids.remove(addonitemid);
                adapterids.notifyDataSetChanged();*/


                //check box check or not view
                checkval = checkval - 1;
                if (checkval == 0) {
                    backbutclknum = "2";
                } else {
                    backbutclknum = "1";
                }

                listItems.remove(addsingleextra + " " + arrayextranameData);
                adapter.notifyDataSetChanged();


                listItemsids.remove(arrayaddonitemid);
                adapterids.notifyDataSetChanged();
                itemidsstr = listItemsids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();

                listItemsexraids.remove(arrayaddonextraidsingle);
                adapterexraids.notifyDataSetChanged();
                itemexradsstr = listItemsexraids.toString().replace("[", "").replace("]", "").replace(" ", "").trim();


                arrayextranameDataadd.remove(arrayextranameData);
                arrayaddonitemidadd.remove(arrayaddonitemid);
                arrayaddonextraidsingleadd.remove(arrayaddonextraidsingle);


                if (!arrayaddonextraidsingle.equalsIgnoreCase("0")) {
                    String currentString = item_amt.getText().toString();
                    String[] separated = currentString.split("£");
                    separated[0] = separated[0].trim();
                    separated[1] = separated[1].trim();

                    double num1 = Double.parseDouble(separated[1]);
                    double num2 = Double.parseDouble(addonidprice);//addonidprice
                    // add both number and store it to sum
                    sum = num1 - num2;
                    //   item_amt.setText("Total: £ " + String.format("%.2f", sum));

                    item_amt.setText(" £ " + String.format("%.2f", sum));


                }

            }
            // Toast.makeText(Item_Menu_Activity.this, ItemName, Toast.LENGTH_SHORT).show();
            addonitemnext("1");

        }
    };

    //  listItemsexraids
    private void addonitemnext(String btontype) {

        Log.e("addonlimitnext", "" + addonlimit);
        if (addonlimit == null) {
            // addonitemview(ItemName, nextaid, addonitemid, addonitemarrayextraData);
            addonitemfirstview(ItemName, nextaid, addonitemid, addonitemarrayextraData, "1");
        } else {

            if (addonlimit.equalsIgnoreCase("1")) {

                Log.e("additem1", "" + addonitemid);
                Log.e("additem2", "" + listItemsids);
                Log.e("additem3", "" + itemidsstr);


                if (nextaid.equalsIgnoreCase("0")) {
                    //btnnextfir
                    if (btnnextfir.equalsIgnoreCase("0")) {
                        //Error show removed me 10-08-2021


                        str_listItems = listItems.toString().replace("[", "").replace("]", "").replace("  ", "").replace(", ", ",").trim();//addon item extrtra call

                        item_total_amt = String.format("%.2f", sum);

                        //  persistPerson();
                        if (itemidsstr == null || itemexradsstr == null) {
                            addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, "", "", item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);
                        } else {
                            addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, itemidsstr, itemexradsstr, item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);
                        }
                        //addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, itemidsstr, itemexradsstr, item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);
                        Log.e("item_add_time1", "" + "1");//Item addon Name
                        Log.e("idvalue1", "" + itemexradsstr); // addon extra id
                        Log.e("idvalue2", "" + itemidsstr);//ok addon extra item id
                        Log.e("idvalue3", "" + ItemName);//ok addon item id
                        Log.e("idvalue4-addonitemnextif", "" + listItemsids);//array
                        Log.e("idvalue5", "" + addon_item_name.getText().toString());//Item name
                        Log.e("idvalue6", "" + String.format("%.2f", sum));//total amt
                        Log.e("idvalue7", "" + item_price);//Item amt
                        Log.e("idvalue8", "" + "1");//Item qty
                        Log.e("idvalue9", "" + str_listItems);//Item qty
                    } else {
                        //nextaid
                        // addonitemview(ItemName, btnnextfir, addonitemid, addonitemarrayextraData);
                        addonitemfirstview(ItemName, btnnextfir, addonitemid, addonitemarrayextraData, "1");
                    }
                } else {
                    // addonitemview(ItemName, nextaid, addonitemid, addonitemarrayextraData);
                    addonitemfirstview(ItemName, nextaid, addonitemid, addonitemarrayextraData, "1");
                }
            } else {
                //select limt 0
                if (btontype.equalsIgnoreCase("0")) {
                    if (nextaid.equalsIgnoreCase("0")) {
                        if (btnnextfir.equalsIgnoreCase("0")) {
                            // str_listItems = Arrays.toString(new ArrayList[]{listItems});
                            str_listItems = listItems.toString().replace("[", "").replace("]", "").replace("  ", "").replace(", ", ",").trim();//addon item extrtra call
                            item_total_amt = String.format("%.2f", sum);
                            //limt 1 and next + btn 0 to call add to call method
                            // Toast.makeText(getApplicationContext(), listItemsids.toString(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_LONG).show();

                            //  persistPerson();
                            if (itemidsstr == null || itemexradsstr == null) {
                                addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, "", "", item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);
                            } else {
                                addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, itemidsstr, itemexradsstr, item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);
                            }
                            //  addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, itemidsstr, itemexradsstr, item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);


                            Log.e("item_add_time2", "" + "2");//Item addon Name
                            Log.e("idvalue1", "" + itemexradsstr);//addon extra id
                            Log.e("idvalue2", "" + itemidsstr);//addon extra item id
                            Log.e("idvalue3", "" + ItemName);//ok addon item id
                            Log.e("idvalue4-addonitemnextelse", "" + listItemsids);//array

                            Log.e("idvalue5", "" + addon_item_name.getText().toString());//Item Name
                            Log.e("idvalue6", "" + String.format("%.2f", sum));//total amt
                            Log.e("idvalue7", "" + item_price);//Item amt
                            Log.e("idvalue8", "" + "1");//Item qty
                            Log.e("idvalue9", "" + str_listItems);//Item qty


                        } else {
                            //  addonitemview(ItemName, btnnext, addonitemid, addonitemarrayextraData);
                            addonitemfirstview(ItemName, btnnextfir, addonitemid, addonitemarrayextraData, "1");
                        }
                    } else {
                        //  addonitemview(ItemName, nextaid, addonitemid, addonitemarrayextraData);
                        addonitemfirstview(ItemName, nextaid, addonitemid, addonitemarrayextraData, "1");

                    }
                } else {

              /*  adapter.add(addonitemarrayextraData);
                 adapter.notifyDataSetChanged();*/
                    //Log.e("ITEM_LIST",""+ITEM_LIST.toString());
                    //select limt 0 0

                    // addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                    addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                    addon_extra1.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                    // addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                    addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                    addon_extra2.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                    //addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                    addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                    addon_extra3.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                    //   addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                    addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                    addon_extra4.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                    // addon_extra4.setTextColor(getResources().getColor(R.color.menu_arrow));
                    //addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                    addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                    addon_extra5.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                    //addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                    addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                    addon_extra6.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                    //addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                    addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                    addon_extra7.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));

                    Checkout.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                        return;
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime();


                                    if (nextaid.equalsIgnoreCase("0")) {
                                        if (btnnextfir.equalsIgnoreCase("0")) {
                                            str_listItems = listItems.toString().replace("[", "").replace("]", "").replace("  ", "").replace(", ", ",").trim();//addon item extrtra call
                                            item_total_amt = String.format("%.2f", sum);
                                            //  Toast.makeText(getApplicationContext(), listItemsids.toString(), Toast.LENGTH_LONG).show();
                                            //limt 1 and next + btn 0 to call add to call method
                                            //Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_LONG).show();

                                            //  persistPerson();
                                            if (itemidsstr == null || itemexradsstr == null) {
                                                addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, "", "", item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);
                                            } else {
                                                addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, itemidsstr, itemexradsstr, item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);
                                            }
                                            //   addon_sucess_add(addon_item_name.getText().toString(), ItemName, str_listItems, itemidsstr, itemexradsstr, item_price, "1", item_total_amt, sharedpreferences.getString("ordermodetype", null), categoryname, subcategoryname);

                                            Log.e("item_add_time3", "" + "3");//Item addon Name
                                            Log.e("idvalue1", "" + itemexradsstr); // addon extra id
                                            Log.e("idvalue2", "" + itemidsstr);//ok addon extra item id
                                            Log.e("idvalue3", "" + ItemName);//ok addon item id
                                            Log.e("idvalue4-0", "" + listItemsids);//array
                                            Log.e("idvalue5", "" + addon_item_name.getText().toString());//Item name
                                            Log.e("idvalue6", "" + String.format("%.2f", sum));//total amt
                                            Log.e("idvalue7", "" + item_price);//Item amt
                                            Log.e("idvalue8", "" + "1");//Item qty
                                            Log.e("idvalue9", "" + str_listItems);//Item qty

                                        } else {
                                            addonitemfirstview(ItemName, btnnextfir, addonitemid, addonitemarrayextraData, "1");
                                            //  addonitemview(ItemName, btnnext, addonitemid, addonitemarrayextraData);

                                        }
                                    } else {
                                        //  addonitemview(ItemName, nextaid, addonitemid, addonitemarrayextraData);

                                        addonitemfirstview(ItemName, nextaid, addonitemid, addonitemarrayextraData, "1");
                                    }
                                }
                            });
                }
            }
        }
    }


    /*  ---------------------------get api URL first time get type values----------------------------------------------------*/
    private void addonitemfirstview(String ItemName, String addonids, String addonitemid, String addonitemarrayextraData, String backnum) {
        // get user data from session

        if (dialog != null && dialog.isShowing()) {
            //Leave Empty here or your way
            hideloading();
        } else {
            loadingshow();
        }


        Map<String, String> params = new HashMap<String, String>();
        params.put("id", ItemName);
        params.put("aid", addonids);
        params.put("items", addonitemid);
        params.put("extra", addonitemarrayextraData);

        if (backnum.equalsIgnoreCase("1")) {
            aidlist.add(addonids);
        }
        if (aidlist.size() == 1) {
            btnClear.setVisibility(View.INVISIBLE);
            listItems.clear();
            selected_addon_item_view.setVisibility(View.GONE);
        } else {
            btnClear.setVisibility(View.VISIBLE);
        }

        Log.e("listaddonidsize", "" + aidlist.size());
        Log.e("listaddonid", "" + aidlist);
        Log.e("listItemssizes", "" + listItems.size());

        backbutclknum = "2";


        checkval = 0;

        Log.e("item_ps1", "" + item_priceadd);

        Log.e("arrayexDataaddsize1", "" + arrayextranameDataaddsize.size());
        Log.e("arrayexDataaddsize3", "" + (arrayextranameDataaddsize.size() - 1));
        Log.e("arrayexDataaddsize", "" + arrayextranameDataaddsize);


        Log.e("addonextramessage91", "" + listItemsids);
        Log.e("addonextramessage92", "" + itemidsstr);
        Log.e("addonextramessage93", "" + listItemsexraids);
        Log.e("addonextramessage94", "" + itemexradsstr);

        fullUrl = menuurlpath + "/menu" + "/addons";

        Log.d("params+gokul",fullUrl + "----->" +params);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<menu_addons_model> call = apiService.addonslist(fullUrl, params);
        Log.e("for1", ": " + ItemName);
        Log.e("for2", ": " + addonids);
        Log.e("for3", ": " + fullUrl);
        // Log.e("for4", ": " + authKey);
        Log.e("for5", ": " + params);
        call.enqueue(new Callback<menu_addons_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<menu_addons_model> call, Response<menu_addons_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                Log.e("addoncode1", ": " + statusCode);

                if (statusCode == 200) {
                    Log.e("addoncode2", ": " + response.body().getSTATUS());
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        hideloading();
                        menu_addon_item_view.setVisibility(View.VISIBLE);
                        mAddFab.setVisibility(View.GONE);
                        //bottom_nav.setVisibility(View.GONE);
                        add_to_cart_layout.setVisibility(View.GONE);

                        addon_item_name.setText(response.body().getDATA().getOrderitemname());
                        addon_item_addonname.setText(response.body().getDATA().getAddondescription());


                        if (item_price_btn == true) {
                            item_price_btn = false;

                            //  item_amt.setText(response.body().getDATA().getOrderitemprice());
                            String[] separateditemprice = response.body().getDATA().getOrderitemprice().split("£");
                            separateditemprice[0] = separateditemprice[0].trim();
                            separateditemprice[1] = separateditemprice[1].trim();
                            item_price = separateditemprice[1];
                            sum = Double.parseDouble(separateditemprice[1]);

                            item_amt.setText(" £ "+separateditemprice[1]);

                            Log.e("item_price1", ": " + item_price);


                        }

                        if (backnum.equalsIgnoreCase("1")) {
                            arrayextranameDataaddsize.add(arrayextranameDataadd.size());
                            arrayextranameDataadd.clear();

                            listItemsidssize.add(arrayaddonitemidadd.size());
                            arrayaddonitemidadd.clear();
                            Log.e("listItemsidssize", "" + listItemsidssize);

                            arrayaddonextraidsingleaddsize.add(arrayaddonextraidsingleadd.size());
                            arrayaddonextraidsingleadd.clear();

           /* item_pricesize.add(item_priceadd.size());
            item_priceadd.clear();
            Log.e("item_priceadd2", "" + item_priceadd);
            Log.e("item_priceadd3", "" + item_pricesize);*/


                            String[] separateditemprice = item_amt.getText().toString().split("£");
                            separateditemprice[0] = separateditemprice[0].trim();
                            separateditemprice[1] = separateditemprice[1].trim();
                            item_price = separateditemprice[1];

                            item_priceadd.add(item_price);

                            Log.e("item_priceaddds1", "" + item_priceadd);

                        }


                        addonlimit = response.body().getDATA().getAddonlimit();
                        nextaid = response.body().getDATA().getNextaid();

                        Log.e("responesids1", "" + addonlimit);
                        Log.e("responesids2", "" + nextaid);
                        Log.e("responesids3", "" + item_amt.getText().toString());
//itemides


                    /*    if (addonlimit.equalsIgnoreCase("1")) {

                            Checkout.setClickable(false);
                        } else {
                            Checkout.setClickable(true);
                        }*/

                        Log.e("getAddonextra", "" + response.body().getDATA().getAddonextra().toString().length());

                        if (response.body().getDATA().getAddonextra().getNoid() == null) {
                            addon_extra1.setVisibility(View.GONE);
                        } else {
                            addon_extra1.setText("No");
                            addon_extra1.setVisibility(View.VISIBLE);
                            //addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                            //addon_extra1.setTextColor(getResources().getColor(R.color.menu_arrow));
                            addon_extra1.setBackground(getResources().getDrawable(R.drawable.button_default));
                            addon_extra1.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                        }
                        if (response.body().getDATA().getAddonextra().getLessid() == null) {
                            addon_extra2.setVisibility(View.GONE);
                        } else {
                            addon_extra2.setText("Less");
                            addon_extra2.setVisibility(View.VISIBLE);
                         /*   addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                            addon_extra2.setTextColor(getResources().getColor(R.color.menu_arrow));*/
                            //addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                            addon_extra2.setBackground(getResources().getDrawable(R.drawable.button_default));
                            addon_extra2.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));

                        }
                        if (response.body().getDATA().getAddonextra().getHalfid() == null) {
                            addon_extra3.setVisibility(View.GONE);
                        } else {
                            addon_extra3.setText("Half");
                            addon_extra3.setVisibility(View.VISIBLE);
                         /*   addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                            addon_extra3.setTextColor(getResources().getColor(R.color.menu_arrow));*/
                            addon_extra3.setBackground(getResources().getDrawable(R.drawable.button_default));
                            addon_extra3.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                        }
                        if (response.body().getDATA().getAddonextra().getOnid() == null) {
                            addon_extra4.setVisibility(View.GONE);
                        } else {
                            addon_extra4.setText("On");
                            addon_extra4.setVisibility(View.VISIBLE);
                         /*   addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                            addon_extra4.setTextColor(getResources().getColor(R.color.menu_arrow));*/
                            addon_extra4.setBackground(getResources().getDrawable(R.drawable.button_default));
                            addon_extra4.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                        }
                        if (response.body().getDATA().getAddonextra().getWithid() == null) {
                            addon_extra5.setVisibility(View.GONE);
                        } else {
                            addon_extra5.setText("With");
                            addon_extra5.setVisibility(View.VISIBLE);
                          /*  addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                            addon_extra5.setTextColor(getResources().getColor(R.color.menu_arrow));*/
                            addon_extra5.setBackground(getResources().getDrawable(R.drawable.button_default));
                            addon_extra5.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                        }
                        if (response.body().getDATA().getAddonextra().getOnBurgerid() == null) {
                            addon_extra6.setVisibility(View.GONE);
                        } else {
                            addon_extra6.setText("On Burger");
                            addon_extra6.setVisibility(View.VISIBLE);
                           /* addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                            addon_extra6.setTextColor(getResources().getColor(R.color.menu_arrow));*/
                            addon_extra6.setBackground(getResources().getDrawable(R.drawable.button_default));
                            addon_extra6.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                        }
                        if (response.body().getDATA().getAddonextra().getOnChipsid() == null) {
                            addon_extra7.setVisibility(View.GONE);
                        } else {
                            addon_extra7.setText("On Chips");
                            addon_extra7.setVisibility(View.VISIBLE);
                        /*    addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                            addon_extra7.setTextColor(getResources().getColor(R.color.menu_arrow));*/
                            addon_extra7.setBackground(getResources().getDrawable(R.drawable.button_default));
                            addon_extra7.setTextColor(getResources().getColor(R.color.No_Less_Text_Color));
                        }


                        List<menu_addons_model.dataval.addonitemslist> menuaddonitemdetails = (response.body().getDATA().getAddonitems());
                        MenuAddonItemAdapter menuaddonitemadapter = new MenuAddonItemAdapter(mContext, (List<menu_addons_model.dataval.addonitemslist>) menuaddonitemdetails);
                        addon_item_view.setHasFixedSize(true);
                    /*    addon_item_view.destroyDrawingCache();
                        menuaddonitemadapter.notifyDataSetChanged();*/
                        addon_item_view.setLayoutManager(new LinearLayoutManager(Item_Menu_Activity.this));
                        addon_item_view.setAdapter(menuaddonitemadapter);


                        Log.e("menuaddonitnth", "" + menuaddonitemadapter.toString().length());

                        //  listItem = response.body().getDATA().getSelecteditem().toArray(new menu_addons_model.dataval.selecteditemlist[0]);

//                        Log.e("listItemlenth", "" + listItem.length);
                      /*  if (listItem.length == 0) {
                            selected_addon_item_view.setVisibility(View.GONE);
                        } else {
                            selected_addon_item_view.setVisibility(View.VISIBLE);
                            values = new String[listItem.length];
                            for (int i = 0; i < listItem.length; i++) {
                                values[i] = listItem[i].getItems();
                            }
                            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.raw_simple_list_item, R.id.selected_item, values);
                            selected_addon_item_view.setAdapter(adapter);
                        }*/


                    } else {
                        hideloading();
                        Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<menu_addons_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                // Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                hideloading();
            }
        });
    }

    /*  ---------------------------get api URL type values----------------------------------------------------*/
    private void addonitemview(String ItemName, String addonids, String addonitemid, String addonitemarrayextraData) {
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", ItemName);
        params.put("aid", addonids);
        params.put("items", addonitemid);
        params.put("extra", addonitemarrayextraData);

        /*HashMap<String, String> user = session.getUserDetails();
        authKey = user.get(SessionManager.KEY_phpsessid);*/
        fullUrl = menuurlpath + "/menu" + "/addons";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<menu_addons_model> call = apiService.addonslist(fullUrl, params);
        Log.e("for1", ": " + ItemName);
        Log.e("for2", ": " + addonids);
        Log.e("for3", ": " + fullUrl);
        // Log.e("for4", ": " + authKey);
        Log.e("for5", ": " + params);
        call.enqueue(new Callback<menu_addons_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<menu_addons_model> call, Response<menu_addons_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                Log.e("rscode", ": " + response.code());
                if (statusCode == 200) {
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {
                        menu_addon_item_view.setVisibility(View.VISIBLE);
                        mAddFab.setVisibility(View.GONE);
                        // bottom_nav.setVisibility(View.GONE);
                        add_to_cart_layout.setVisibility(View.GONE);

                        addon_item_name.setText(response.body().getDATA().getOrderitemname());
                        addon_item_addonname.setText(response.body().getDATA().getAddondescription());
                        //  item_amt.setText(response.body().getDATA().getOrderitemprice());

                        addonlimit = response.body().getDATA().getAddonlimit();
                        nextaid = response.body().getDATA().getNextaid();

                        Log.e("itemides71", "" + addonlimit);
                        //itemides
                    /*if (addonlimit.equalsIgnoreCase("1")) {
                            Checkout.setClickable(false);
                        } else {
                            Checkout.setClickable(true);
                        }*/
                        Log.e("getAddonextra", "" + response.body().getDATA().getAddonextra().toString().length());
                        if (response.body().getDATA().getAddonextra().getNoid() == null) {
                            addon_extra1.setVisibility(View.GONE);
                        } else {
                            addon_extra1.setText("No");
                            addon_extra1.setVisibility(View.VISIBLE);
                            addon_extra1.setBackgroundColor(addon_extra1.getContext().getResources().getColor(R.color.box));
                            addon_extra1.setTextColor(getResources().getColor(R.color.menu_arrow));
                        }
                        if (response.body().getDATA().getAddonextra().getLessid() == null) {
                            addon_extra2.setVisibility(View.GONE);
                        } else {
                            addon_extra2.setText("Less");
                            addon_extra2.setVisibility(View.VISIBLE);
                            addon_extra2.setBackgroundColor(addon_extra2.getContext().getResources().getColor(R.color.box));
                            addon_extra2.setTextColor(getResources().getColor(R.color.menu_arrow));
                        }
                        if (response.body().getDATA().getAddonextra().getHalfid() == null) {
                            addon_extra3.setVisibility(View.GONE);
                        } else {
                            addon_extra3.setText("Half");
                            addon_extra3.setVisibility(View.VISIBLE);
                            addon_extra3.setBackgroundColor(addon_extra3.getContext().getResources().getColor(R.color.box));
                            addon_extra3.setTextColor(getResources().getColor(R.color.menu_arrow));
                        }
                        if (response.body().getDATA().getAddonextra().getOnid() == null) {
                            addon_extra4.setVisibility(View.GONE);
                        } else {
                            addon_extra4.setText("On");
                            addon_extra4.setVisibility(View.VISIBLE);
                            addon_extra4.setBackgroundColor(addon_extra4.getContext().getResources().getColor(R.color.box));
                            addon_extra4.setTextColor(getResources().getColor(R.color.menu_arrow));
                        }
                        if (response.body().getDATA().getAddonextra().getWithid() == null) {
                            addon_extra5.setVisibility(View.GONE);
                        } else {
                            addon_extra5.setText("With");
                            addon_extra5.setVisibility(View.VISIBLE);
                            addon_extra5.setBackgroundColor(addon_extra5.getContext().getResources().getColor(R.color.box));
                            addon_extra5.setTextColor(getResources().getColor(R.color.menu_arrow));
                        }
                        if (response.body().getDATA().getAddonextra().getOnBurgerid() == null) {
                            addon_extra6.setVisibility(View.GONE);
                        } else {
                            addon_extra6.setText("On Burger");
                            addon_extra6.setVisibility(View.VISIBLE);
                            addon_extra6.setBackgroundColor(addon_extra6.getContext().getResources().getColor(R.color.box));
                            addon_extra6.setTextColor(getResources().getColor(R.color.menu_arrow));
                        }
                        if (response.body().getDATA().getAddonextra().getOnChipsid() == null) {
                            addon_extra7.setVisibility(View.GONE);
                        } else {
                            addon_extra7.setText("On Chips");
                            addon_extra7.setVisibility(View.VISIBLE);
                            addon_extra7.setBackgroundColor(addon_extra7.getContext().getResources().getColor(R.color.box));
                            addon_extra7.setTextColor(getResources().getColor(R.color.menu_arrow));
                        }
                        List<menu_addons_model.dataval.addonitemslist> menuaddonitemdetails = (response.body().getDATA().getAddonitems());
                        MenuAddonItemAdapter menuaddonitemadapter = new MenuAddonItemAdapter(mContext, menuaddonitemdetails);
                        addon_item_view.setHasFixedSize(true);
                        addon_item_view.setLayoutManager(new LinearLayoutManager(Item_Menu_Activity.this));
                        /*addon_item_view.destroyDrawingCache();
                        menuaddonitemadapter.notifyDataSetChanged();*/
                        addon_item_view.setAdapter(menuaddonitemadapter);
                        Log.e("menuaddonitnth", "" + menuaddonitemadapter.toString().length());
                        //   listItem = response.body().getDATA().getSelecteditem().toArray(new menu_addons_model.dataval.selecteditemlist[0]);
                        //  Log.e("listItemlenth", "" + listItem.length);
                      /*  if (listItem.length == 0) {
                            selected_addon_item_view.setVisibility(View.GONE);
                        } else {
                            selected_addon_item_view.setVisibility(View.VISIBLE);
                            values = new String[listItem.length];
                            for (int i = 0; i < listItem.length; i++) {
                                values[i] = listItem[i].getItems();
                            }
                            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.raw_simple_list_item, R.id.selected_item, values);
                            selected_addon_item_view.setAdapter(adapter);
                        }*/
                    } else {
                        Log.e("erocode", "" + "1");
                        Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("erocode", "" + "2");
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<menu_addons_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Log.e("erocode", "" + "3");
                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*----------------------------Search menu----------------------------*/
    private void search_menu_item(String words, String ordertype) {
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", words);
        params.put("ordermode", ordertype);

       /* HashMap<String, String> user = session.getUserDetails();
        authKey = user.get(SessionManager.KEY_phpsessid);*/
        searchfullUrl = menuurlpath + "/menu" + "/searchAPI";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<search_menu_item_model> call = apiService.search_menu_item(searchfullUrl, params);
        Log.e("searchapikey", "" + params);
        call.enqueue(new Callback<search_menu_item_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<search_menu_item_model> call, Response<search_menu_item_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                Log.e("rscode", ": " + response.code());
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        searchrecyclerviewitem.setVisibility(View.VISIBLE);

                        List<search_menu_item_model.data> searchitemname = (response.body().getData());
                        SearchItemAdapter searchitemadapter = new SearchItemAdapter(mContext, (List<search_menu_item_model.data>) searchitemname, sharedpreferences.getString("ordermodetype", null), menuurlpath);
                        searchrecyclerviewitem.setHasFixedSize(true);
                        searchrecyclerviewitem.setLayoutManager(new LinearLayoutManager(Item_Menu_Activity.this));
                        searchrecyclerviewitem.setAdapter(searchitemadapter);


                    } else {
                        //  searchitemname.clear();

                        recyclerviewitem.setVisibility(View.VISIBLE);
                        search_layout.setVisibility(View.GONE);

                        // Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<search_menu_item_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Log.e("erocode", "" + "3");
                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*-----------------------Final Addon suucess--------------------------------*/

    private void addon_sucess_add(String str_addon_item_name, String str_ItemName, String str_str_listItems,
                                  String str_itemidsstr, String str_itemexradsstr, String str_item_price, String strqtys,
                                  String str_item_total_amt, String str_ordertype, String str_categoryname, String str_subcategoryname) {

        // get user data from session
        Log.e("str_itemidsstr", "" + str_itemidsstr);
        Log.e("str_itemexradsstr", "" + str_itemexradsstr);
        Map<String, String> params = new HashMap<String, String>();
        params.put("ids", str_itemidsstr);
        params.put("iid", str_ItemName);
        params.put("ext", str_itemexradsstr);
        params.put("ordermode", str_ordertype);


       /* HashMap<String, String> user = session.getUserDetails();
        authKey = user.get(SessionManager.KEY_phpsessid);*/
        addonscessfullUrl = menuurlpath + "/menu" + "/itemaddaddon";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<final_addon_add_model> call = apiService.final_addon_add(addonscessfullUrl, params);
        Log.e("addon_last_params", "" + params);
        call.enqueue(new Callback<final_addon_add_model>() {
            @SuppressLint("LongLogTag")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<final_addon_add_model> call, Response<final_addon_add_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                Log.e("addon_latcode", ": " + response.code());
                if (statusCode == 200) {
                    Log.e("addon_latcode", ": " + response.body().getStatus());
                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        Log.e("addon_latcode", ": " + response.body().getError_code());
                        Log.e("addon_latcode", ": " + response.body().getError_message());


                        if (dbHelper.insertItem(str_addon_item_name, str_ItemName, str_str_listItems,
                                str_itemidsstr, str_itemexradsstr, str_item_price, strqtys, str_item_total_amt, str_item_total_amt,
                                str_categoryname, str_subcategoryname)) {


                            Log.e("nextapi1", "" + nextaid);
                            Log.e("nextapi2", "" + btnnextfir);
                            Log.e("nextapi3", "" + addonlimit);


                            Log.e("item_add_time5-Item_Menu_Activity", "\n"+" " + str_addon_item_name + "\n" + "-------->"+str_ItemName + "\n" + str_str_listItems +
                                    "\n" +"----->"+ str_itemidsstr + "\n" + str_itemexradsstr + "\n" + str_item_price + "\n" + strqtys + "\n" + " -----> "+ str_item_total_amt + "\n" + "--->"
                                    + str_item_total_amt + "\n"
                                    + str_categoryname + "\n" + str_subcategoryname);

                            // Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show();
                            Customertoastmessage();

                            Log.e("item_add_time4", "" + "4");//Item addon Name

                            getContactsCount();

                            menu_addon_item_view.setVisibility(View.GONE);
                            mAddFab.setVisibility(View.VISIBLE);
                            //bottom_nav.setVisibility(View.VISIBLE);
                            add_to_cart_layout.setVisibility(View.VISIBLE);
                            // bottomNav.getOrCreateBadge(R.id.home_card).setNumber(cursor);
                            ArrayList<String> get_qty_count = dbHelper.getqtycount();
                            total_item.setText(get_qty_count.get(0) + "");
                            // total_item.setText(cursor + "");
                            Log.d("Cursor3", String.valueOf(get_qty_count.get(0) + ""));
                            // total_item.setText(cursor + " Items");

                            ArrayList<String> get_amt_count = dbHelper.gettotalamt();
                            total_amount_textview.setText(String.format("%.2f", amtfloat + Double.parseDouble(get_amt_count.get(0) + "")));
                            Log.d("TotalAmount",get_amt_count.get(0)+ " ");

                            new CountDownTimer(2000, 1000) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    getContactsCount();
                                    add_to_cart_layout.setVisibility(View.VISIBLE);
                                    ArrayList<String> get_qty_count = dbHelper.getqtycount();
                                    total_item.setText(get_qty_count.get(0) + "");
                                    //   total_item.setText(cursor + "");
                                    //     Log.d("Cursor4", String.valueOf(get_qty_count.get(0) + ""));

                                    ArrayList<String> get_amt_count = dbHelper.gettotalamt();
                                    Log.d("TotalAmount",get_amt_count.get(0)+ " ");
                                    total_amount_textview.setText(String.format("%.2f", amtfloat + Double.parseDouble(get_amt_count.get(0) + "")));
                                    //  total_item.setText(cursor + " Items");
                                }
                            }.start();


                            // dbHelper.sel DISTINCT select_list FROM table;
                        } else {
                            Toast.makeText(getApplicationContext(), "Could not Insert Item", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<final_addon_add_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Log.e("erocode", "" + "3");
                Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Customertoastmessage() {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView tv = (TextView) layout.findViewById(R.id.txtvw);
        tv.setText("Wow! Item added Successfully");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    public int getContactsCount() {
        cursor = dbHelper.numberOfRows();

        Log.e("c", "" + cursor);
        //Log.e("totalitemamut12", "" + totalitemamut);
        return cursor;
    }
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(addonmMessageReceiver);
        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(addonbtnnextid);
        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(mitemsuccessMessageReceiver);
        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(mMessagepromooff);
        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(mMessageonlineoff);
        LocalBroadcastManager.getInstance(Item_Menu_Activity.this).unregisterReceiver(mMessageitempossion);
    }


    /*-------------------Loading Images------------------*/
    public void loadingshow() {
        dialog = new Dialog(Item_Menu_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(Item_Menu_Activity.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);

        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideloading() {
        dialog.dismiss();
    }


    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            // startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));

            if (reloadback.equalsIgnoreCase("1")) {
                startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
            } else if (reloadback.equalsIgnoreCase("2")) {
                startActivity(new Intent(getApplicationContext(), Favourite_Activity.class));
            } else if (reloadback.equalsIgnoreCase("3")) {
                startActivity(new Intent(getApplicationContext(), Show_Offer_Activity.class));
            } else if(reloadback.equalsIgnoreCase("4")) {
                // startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

            }else{
                startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
            }
            return true;
        }
        return false;
    }

   /* @Override
    public void onResume() {
        super.onResume();
       Toast.makeText(getApplicationContext(),"Resume",Toast.LENGTH_LONG).show();
        finish();
        startActivity(getIntent());
    }*/

    @Override
    public void onRestart() {
        super.onRestart();

        // do some stuff here
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Menu Activity");
    }
    /*-------------------Loading Images------------------*/
    public void loading() {

        dialog_loading = new Dialog(Item_Menu_Activity.this);
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loading.setCancelable(false);
        dialog_loading.setContentView(R.layout.custom_loading);


        ImageView ImageViewgif = dialog_loading.findViewById(R.id.custom_searchloader_ImageView);


        Glide.with(Item_Menu_Activity.this)
                .load(R.drawable.search_loader)
                .placeholder(R.drawable.search_loader)
                .centerCrop()
                .into(ImageViewgif);


        dialog_loading.show();
    }
    public void dismissloading() {
        dialog_loading.dismiss();
    }


}





