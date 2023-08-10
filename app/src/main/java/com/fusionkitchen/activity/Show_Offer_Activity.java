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
import android.os.SystemClock;
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
import com.fusionkitchen.adapter.DashboardListViewAdapter;
import com.fusionkitchen.adapter.DashboardSearchResultList;
import com.fusionkitchen.adapter.DashboardSearchResultListOffer;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.DashboardListViewModel;
import com.fusionkitchen.model.home_model.location_type_modal;
import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Show_Offer_Activity extends AppCompatActivity {


    private Context mContext = Show_Offer_Activity.this;
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
    LinearLayout no_offers, list_layout, clear_list_layout;
    RecyclerView myofferlist;
    AppCompatButton go_back, remove_cart;


    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, menuurlpath;
    Dialog dialog;
    /*--------------------------Login postcode save local------------------------*/
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    String key_postcode, key_lat, key_lon, key_area;

    /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";




    /*----------------------------------Recyclerview List -----------------------------------------------------------*/

    RecyclerView Enter_your_RecyclerView;
    private DashboardListViewAdapter dashboardListViewAdapter;
    private List<DashboardListViewModel> dashboardListViewModels  = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_show_offer);


        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);



        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Show_Offer_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Show_Offer_Activity.ViewDialog alert = new Show_Offer_Activity.ViewDialog();
            alert.showDialog(Show_Offer_Activity.this);

        }


        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Show_Offer_Activity.this, Dashboard_Activity.class);
                startActivity(intent);
            }
        });

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Show_Offer_Activity.this);
        getContactsCount();

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);


        if (slogin.getString("login_key_cid", null) != null) {
            user_id = (slogin.getString("login_key_cid", null));
        } else {
            user_id = "0";
        }


        /*---------------------------XML ID Call----------------------------------------------------*/

        browse_more = findViewById(R.id.browse_more);

        no_offers = findViewById(R.id.no_offers);
        list_layout = findViewById(R.id.list_layout);
        Enter_your_RecyclerView = findViewById(R.id.Enter_your_RecyclerView);
        myofferlist = findViewById(R.id.myofferlist);
        clear_list_layout = findViewById(R.id.clear_list_layout);

        go_back = findViewById(R.id.go_back);
        remove_cart = findViewById(R.id.remove_cart);





        browse_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentthirnolist = new Intent(Show_Offer_Activity.this, Dashboard_Activity.class);
                startActivity(intentthirnolist);
            }
        });


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                no_offers.setVisibility(View.GONE);
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
                dbHelper = new SQLDBHelper(Show_Offer_Activity.this);
                dbHelper.deleteAll();


                Intent intentthir = new Intent(Show_Offer_Activity.this, Item_Menu_Activity.class);
                intentthir.putExtra("menuurlpath", menuurlpath);
                intentthir.putExtra("reloadback", "3");
                startActivity(intentthir);


            }
        });
        /*--------------------------Login postcode save local------------------------*/
        sharedptcode = getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);

        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Show_Offer_Activity.this.MODE_PRIVATE);


        key_postcode = (sharedptcode.getString("KEY_postcode", null));
        key_lat = (sharedptcode.getString("KEY_lat", null));
        key_lon = (sharedptcode.getString("KEY_lon", null));
        key_area = (sharedptcode.getString("KEY_area", null));


        Log.e("localstore1", "" + key_postcode);
        Log.e("localstore2", "" + key_lat);
        Log.e("localstore3", "" + key_lon);
        Log.e("localstore4", "" + key_area);

        getofferlist(key_postcode, key_lat, key_lon, "0", key_area, user_id);

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
                         Intent intent_Offer = new Intent(getApplicationContext(),Show_Offer_Activity.class);
                         startActivity(intent_Offer);
                         finish();
                     //   Freshchat.showConversations(getApplicationContext());
                        break;
                    case R.id.home_card:
                        /*Intent intentreview = new Intent(getApplicationContext(), Review.class);
                        startActivity(intentreview);*/
                        // Toast.makeText(getApplicationContext(), "Card", Toast.LENGTH_SHORT).show();

                        if (cursor != 0) {
                            Intent intentcard = new Intent(getApplicationContext(), Add_to_Cart.class);
                            startActivity(intentcard);
                        }else{
                            Toast.makeText(Show_Offer_Activity.this,"Your cart is Empty!",Toast.LENGTH_SHORT).show();
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
            getofferlist(key_postcode, key_lat, key_lon, "0", key_area, user_id);

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
                no_offers.setVisibility(View.GONE);
                list_layout.setVisibility(View.VISIBLE);
                clear_list_layout.setVisibility(View.VISIBLE);

            } else {
                Intent intenttwo = new Intent(Show_Offer_Activity.this, Item_Menu_Activity.class);
                intenttwo.putExtra("menuurlpath", menuurlpath);
                intenttwo.putExtra("reloadback", "3");
                startActivity(intenttwo);
            }

        }
    };

    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Show_Offer_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Show_Offer_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Show_Offer_Activity.this);
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


    /*---------------------------get favourtelist values----------------------------------------------------*/
    private void getofferlist(String str_key_postcode, String str_key_lat, String str_lng, String otype, String str_area, String str_userid) {
        loadingshow();

        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", str_key_postcode);
        params.put("lat", str_key_lat);
        params.put("lng", str_lng);
        params.put("order_type", otype);
        params.put("area", str_area);
        params.put("user_id", str_userid);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<location_type_modal> call = apiService.getofferlist(params);

        Log.e("params", "" + params);
        call.enqueue(new Callback<location_type_modal>() {
            @Override
            public void onResponse(Call<location_type_modal> call, Response<location_type_modal> response) {
                //  response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {

                        no_offers.setVisibility(View.GONE);
                        list_layout.setVisibility(View.VISIBLE);
                        clear_list_layout.setVisibility(View.GONE);


                        /*-------------------------------------Start RecyclerView Listview-------------------------------*/

                        dashboardListViewAdapter = new DashboardListViewAdapter(dashboardListViewModels,Show_Offer_Activity.this);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
                        Enter_your_RecyclerView.setLayoutManager(manager);
                        Enter_your_RecyclerView.setAdapter(dashboardListViewAdapter);
                        DashboardDataList();


                        /*-------------------------------------End RecyclerView Listview-------------------------------*/

                        List<location_type_sub_modal.clients> jobdetails = (response.body().getClientinfo().getClients());
                        DashboardSearchResultListOffer adapter = new DashboardSearchResultListOffer(mContext, (List<location_type_sub_modal.clients>) jobdetails);
                        myofferlist.setHasFixedSize(true);
                        myofferlist.setLayoutManager(new LinearLayoutManager(Show_Offer_Activity.this));
                        myofferlist.setAdapter(adapter);

                       // Offerlistpopup();



                    } else {
                        no_offers.setVisibility(View.VISIBLE);
                        list_layout.setVisibility(View.GONE);
                        clear_list_layout.setVisibility(View.GONE);
                        // Snackbar.make(Favourite_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    no_offers.setVisibility(View.VISIBLE);
                    list_layout.setVisibility(View.GONE);
                    clear_list_layout.setVisibility(View.GONE);
                    Snackbar.make(Show_Offer_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<location_type_modal> call, Throwable t) {
                hideloading();
                no_offers.setVisibility(View.VISIBLE);
                list_layout.setVisibility(View.GONE);
                clear_list_layout.setVisibility(View.GONE);
                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Show_Offer_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }

        });

    }

    private void Offerlistpopup() {

        final BottomSheetDialog bt=new BottomSheetDialog(mContext,R.style.BottomSheetDialogTheme);
        View  view = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_sheet, null);

        AppCompatButton btn_offer  = view.findViewById(R.id.btn_offer);
        EditText EditText_Search_View = view.findViewById(R.id.EditText_Search_View);

        btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText_Search_View.getText().toString().trim();
                //([A-PR-UWYZ](?:[A-HK-Y][0-9](?:[0-9]|[ABEHMNPRV-Y])?|[0-9]([0-9]|[A-HJKPSTUW])?)) ?([0-9][ABD-HJLNP-UW-Z]{2})
               /* String emailPattern = "^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)$";
                if (EditText_Search_View.getText().toString().trim().matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }*/
                bt.dismiss();
            }
        });

        bt.setContentView(view);
        bt.setCanceledOnTouchOutside(false);
        bt.show();
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


    /*-------------------Loading Images------------------*/
    public void loadingshow() {
        dialog = new Dialog(Show_Offer_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);
        LottieAnimationView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        gifImageView.setAnimation(R.raw.newloader);
        gifImageView.playAnimation();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideloading() {
        dialog.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Show Offer Activity");
    }
}
