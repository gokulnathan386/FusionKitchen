package com.fusionkitchen.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.model.address.getaddressforpostcode_modal;
import com.fusionkitchen.model.cart.Cartitem;
import com.fusionkitchen.model.gpay.getgpayclientscSecret_model;
import com.fusionkitchen.model.gpay.getgpaystuartpayment_model;
import com.fusionkitchen.model.orderstatus.submitfeedback_model;
import com.fusionkitchen.model.paymentgatway.appkey;
import com.fusionkitchen.model.paymentgatway.completpay_model;
import com.fusionkitchen.model.paymentgatway.getclientSecret_model;
import com.fusionkitchen.model.updatestuartaddress_modal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.fusionkitchen.R;
import com.fusionkitchen.adapter.OrderstatusitemListAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.orderstatus.ordertracking_details_model;
import com.fusionkitchen.model.orderstatus.ordertracking_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.googlepaylauncher.GooglePayEnvironment;
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.jetbrains.annotations.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;

public class Order_Status_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private Context mContext = Order_Status_Activity.this;
    EditText custom_edittxt,card_number;
    String radio_selectedValue="£3",stuart_delivery_;
    int rest_rating,food_rating;
    int gpay_amount;
    TextView rider_name;
    String order_expected_time;
    String pay_descval,Stuartdrivename,Stuartdrivenumber;
    TextView delivery_coll_tip_desc,delivery_coll_tip_txt;
    Boolean stuart_delivery_status = false;
    SupportMapFragment mapFragment;
    LinearLayout orderlist_data,total_item,stuart_collection_layout,review_screen_layout,OrderDetails_layout;
    TextView orderlist_discount,confirm_txt_desc,tip_u_delivery,collect_txt_msg,collection_txtmsg;
    String phone_number,dno,add1,add2,post_code,msg,E_mail;
    CardView deliver_tip,tip_module,driver_details;
    RadioButton selectedRadioButton;
    private Stripe stripe;
    String bulkeyfullUrl,apikey,token,metdpasfullUrl,stripe_reponse_amount,intepasfullUrl,gpay_apikey;
    String gpay_client_secret;
    View order_details_view;
    CardView drive_phone_btn;

    private static final int REQUEST_READ_CONTACTS_PERMISSION = 0;
    private static final int REQUEST_CONTACT = 1;
    GooglePayPaymentMethodLauncher googlePayLauncher;



    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;
    EditText comments_txt,city_stuart;
    TextView tip_txt;

    /*-----------------------------Google Map----------------------------*/
    private GoogleMap mMap;
   // private ZoomControls mZoomControls;
    MarkerOptions origin, destination,midpoint;
    Dialog ordershare_popup;
    String  tip_pay_first_name ,tip_pay_last_name,pay_amt1;


    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_DRIVER = 125;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 102;
    private static final int PICK_CONTACT = 104;

    String _ccNumber = "";

    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;


    String fname;
    String pickup_lat,pickup_long,dropoff_lat,dropoff_long,driver_lat,driver_long;
    /*---------------------------XML ID Call----------------------------------------------------*/

    View view_order_placed, view_order_confirmed, view_order_processed, view_order_pickup, con_divider, ready_divider, placed_divider;
    ImageView img_orderconfirmed, orderprocessed, orderpickup, orderplaced,down_arrow,up_arrow;
    TextView textorderpickup, text_confirmed, textorderprocessed, textorderplaced,total_item_count,total_itemlist;

    AppCompatTextView order_date, order_id, total_amt, Delivery_Collection_time,user_delivery_address;

    ConstraintLayout tracking_layout, bill_layout;
    AppCompatTextView view_bill, hide_bill;
    TextView listview_total, sub_amt, service_amt, coupon_amt, delivery_amt, bag_amt,chg_address,alternative_number;
    RelativeLayout servicel_layout, delivery_layout, coupon_layout, bag_layout;

    RecyclerView myorderList;
    /*---------------------------dialog show----------------------------------------------------*/

    Dialog waitingdialog, confirmdialog, rejectdialog, delieveddialog;


    /* --------------Get Intent------------------*/

    String orderid, orderpath, orderdate, clientname, txtotype, clientid, clientphonenumber, clsstype;

    String otype, statusshow, customername,rest_phone_no;


    final Handler handler = new Handler();
    Runnable runnable;
    final int delay = 3000; // 1000 milliseconds == 1 second

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id;
    String delivery_status,delivery_status_name;
    RadioGroup tip_button_view;
    TextView rest_name,name_phoneno,sub_amt_stuart,stuart_order_tracking_share_btn,subtotal_item;


    AppCompatButton chat_client, call_client;
    String phone, gmail;
    LinearLayout botton_top;
    CardView botton_top_vis,restaurants_mobile_no,submit_review_btn;
    LottieAnimationView wait_confirm_icon,collection_anim;
    ImageView item_order_details;
    TextView tip_btn,custom_tip_textview,stuart_textview,tracking_txt,header_txt_status;
    TextView change_add_btn;
    Dialog  dialog;
    EditText post_code_txtview,House_doorno_txt,street_stuart;




    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_order_status);

        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);

        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Order_Status_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(Order_Status_Activity.this);

        }

        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        fname = (slogin.getString("login_key_fname", null));
        gmail = (slogin.getString("login_key_email", null));
        phone = (slogin.getString("login_key_phone", null));

        /*---------------------------Fresh Chat----------------------------------------------------*/

        FreshchatConfig config = new FreshchatConfig("67d078d8-604f-44c7-8807-c8a96810af62", "0604e381-8106-48e8-95bf-bc74bc8893fe");
        config.setDomain("msdk.in.freshchat.com");
        config.setCameraCaptureEnabled(true);
        config.setGallerySelectionEnabled(true);
        config.setResponseExpectationEnabled(true);
        Freshchat.getInstance(getApplicationContext()).init(config);

        /*---------------------------XML ID Call----------------------------------------------------*/

        view_order_placed = findViewById(R.id.view_order_placed);
        view_order_confirmed = findViewById(R.id.view_order_confirmed);
        view_order_processed = findViewById(R.id.view_order_processed);
        view_order_pickup = findViewById(R.id.view_order_pickup);

        placed_divider = findViewById(R.id.placed_divider);
        con_divider = findViewById(R.id.con_divider);
        ready_divider = findViewById(R.id.ready_divider);
        textorderpickup = findViewById(R.id.textorderpickup);

        text_confirmed = findViewById(R.id.text_confirmed);
        textorderprocessed = findViewById(R.id.textorderprocessed);
        textorderplaced = findViewById(R.id.textorderplaced);
        img_orderconfirmed = findViewById(R.id.img_orderconfirmed);

        orderplaced = findViewById(R.id.orderplaced);
        orderprocessed = findViewById(R.id.orderprocessed);
        orderpickup = findViewById(R.id.orderpickup);
        order_date = findViewById(R.id.order_date);

        order_id = findViewById(R.id.order_id);
        total_amt = findViewById(R.id.total_amt);
        myorderList = findViewById(R.id.myorderList);
        listview_total = findViewById(R.id.listview_total);

        sub_amt = findViewById(R.id.sub_amt);
        service_amt = findViewById(R.id.service_amt);
        coupon_amt = findViewById(R.id.coupon_amt);
        delivery_amt = findViewById(R.id.delivery_amt);

        bag_amt = findViewById(R.id.bag_amt);
        servicel_layout = findViewById(R.id.servicel_layout);
        delivery_layout = findViewById(R.id.delivery_layout);
        coupon_layout = findViewById(R.id.coupon_layout);

        bag_layout = findViewById(R.id.bag_layout);
        chat_client = findViewById(R.id.chat_client);
        call_client = findViewById(R.id.call_client);
        custom_edittxt = findViewById(R.id.custom_edittxt);
        tip_button_view = findViewById(R.id.tip_button_view);

        Delivery_Collection_time = findViewById(R.id.Delivery_Collection_time);
        tip_btn = findViewById(R.id.tip_btn);
        custom_tip_textview = findViewById(R.id.custom_tip_textview);

        bottomNav = findViewById(R.id.bottom_navigation);
        /*------------------------------------Google Map -------------------------*/

        botton_top =  findViewById(R.id.botton_top);
        botton_top_vis = findViewById(R.id.botton_top_vis);
        down_arrow = findViewById(R.id.down_arrow);
        up_arrow = findViewById(R.id.up_arrow);
        wait_confirm_icon = (LottieAnimationView) findViewById(R.id.wait_confirm_icon);
        item_order_details = findViewById(R.id.item_order_details);
        rest_name =  findViewById(R.id.rest_name);
        restaurants_mobile_no = findViewById(R.id.restaurants_mobile_no);
        user_delivery_address = findViewById(R.id.user_delivery_address);
        name_phoneno = findViewById(R.id.name_phoneno);
        chg_address = findViewById(R.id.chg_address);
        alternative_number = findViewById(R.id.alternative_number);
        total_item_count = findViewById(R.id.total_item_count);
        sub_amt_stuart = findViewById(R.id.sub_amt_stuart);

        stuart_order_tracking_share_btn = findViewById(R.id.stuart_order_tracking_share_btn);
        stuart_textview = findViewById(R.id.stuart_textview);
        tracking_txt = findViewById(R.id.tracking_txt);
        comments_txt = findViewById(R.id.comments_txt);
        submit_review_btn =findViewById(R.id.submit_review_btn);
        header_txt_status = findViewById(R.id.header_txt_status);

        orderlist_data = findViewById(R.id.orderlist_data);
        total_item = findViewById(R.id.total_item);
        total_itemlist = findViewById(R.id.total_itemlist);
        subtotal_item = findViewById(R.id.subtotal_item);
        orderlist_discount = findViewById(R.id.orderlist_discount);
        deliver_tip = findViewById(R.id.deliver_tip);
        confirm_txt_desc = findViewById(R.id.confirm_txt_desc);
        tip_u_delivery = findViewById(R.id.tip_u_delivery);
        stuart_collection_layout = findViewById(R.id.stuart_collection_layout);
        collection_anim = findViewById(R.id.collection_anim);
        collect_txt_msg = findViewById(R.id.collect_txt_msg);
        collection_txtmsg = findViewById(R.id.collection_txtmsg);
        tip_txt = findViewById(R.id.tip_txt);
        tip_module = findViewById(R.id.tip_module);
        driver_details = findViewById(R.id.driver_details);
        review_screen_layout = findViewById(R.id.review_screen_layout);
        OrderDetails_layout = findViewById(R.id.OrderDetails_layout);
        order_details_view = findViewById(R.id.order_details_view);
        delivery_coll_tip_desc = findViewById(R.id.delivery_coll_tip_desc);
        delivery_coll_tip_txt = findViewById(R.id.delivery_coll_tip_txt);
        rider_name = findViewById(R.id.rider_name);
        drive_phone_btn = findViewById(R.id.drive_phone_btn);



        total_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderlist_data.getVisibility() == View.VISIBLE) {
                    orderlist_data.setVisibility(View.GONE);
                } else {
                    orderlist_data.setVisibility(View.VISIBLE);
                }
            }
        });
     //   mZoomControls = findViewById(R.id.zoomControls);

       /* mZoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        mZoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });*/

        item_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Orderdetailshare();
            }
        });

       RatingBar foodquality_ratingBar = (RatingBar)findViewById(R.id.foodquality_ratingBar);
       RatingBar restuarant_service = (RatingBar)findViewById(R.id.restuarant_service);

        restuarant_service.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                 rest_rating = Math.round(rating);

          }

        });

        foodquality_ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                 food_rating = Math.round(rating);
            }

        });

        custom_tip_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip_button_view.setVisibility(View.GONE);
                custom_edittxt.setVisibility(View.VISIBLE);
                tip_btn.setText("Pay Rider Tip");
                radio_selectedValue = null;
            }
        });

        custom_edittxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() != 0) {
                    tip_btn.setText("Pay Rider Tip of £"+cs);
                    tip_u_delivery.setText("£" + cs);

                    Drawable drawable = getResources().getDrawable(R.drawable.pound_icon);
                    custom_edittxt.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);

                }else{
                    tip_btn.setText("Pay Rider Tip");
                    tip_u_delivery.setText("£0.00");
                    custom_edittxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);

                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_bottom:
                        Intent intenthome = new Intent(getApplicationContext(), Postcode_Activity.class);
                        startActivity(intenthome);
                        finish();
                        break;
                    case R.id.homeOffer:
                        if (user_id != null && !user_id.isEmpty()) {
                            Intent offer_list = new Intent(getApplicationContext(), Show_Offer_Activity.class);
                            startActivity(offer_list);
                        } /*else {
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            intent.putExtra("activity_details", "myaccount");
                            startActivity(intent);
                        }*/
                        break;
                    case R.id.my_order:
                        Toast.makeText(getApplicationContext(), "My Order", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home_account:
                        if (user_id != null && !user_id.isEmpty()) {
                            Intent intentcard = new Intent(getApplicationContext(), MyAccount_Activity.class);
                            startActivity(intentcard);
                        } /*else {
                            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                            intent.putExtra("activity_details", "myaccount");
                            startActivity(intent);
                        }*/
                        break;
                }
                return true;
            }
        });


        submit_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (foodquality_ratingBar.getRating() == 0) {
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Please rate this item.", Snackbar.LENGTH_LONG).show();
                }else if(restuarant_service.getRating() == 0){
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Please rate this item.", Snackbar.LENGTH_LONG).show();
                }else if(comments_txt.getText().toString().equalsIgnoreCase("")) {
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Please fill out comments field.", Snackbar.LENGTH_LONG).show();
                } else {
                    submitfeedback(user_id, orderid, clientid,comments_txt.getText().toString(),fname,gmail,phone); // Feedback API calling Method
                }
            }
        });


/*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap finalMarker= Bitmap.createScaledBitmap(b, width, height, false);


        BitmapDrawable bitmapdraw1 = (BitmapDrawable)getResources().getDrawable(R.drawable.bikestuart);
        Bitmap b1=bitmapdraw1.getBitmap();
        Bitmap finalMarker1= Bitmap.createScaledBitmap(b1, width, height, false);



        Log.d("gokulnathan===="," " + pickup_lat);


        origin = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat),Double.parseDouble(pickup_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
        destination = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

        String url = getDirectionsUrl(origin.getPosition(), destination.getPosition());

        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(url);
*/


        botton_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int height = botton_top_vis.getHeight();

                if(height == 525){
                    ViewGroup.LayoutParams params = botton_top_vis.getLayoutParams();
                    params.height = 1250;
                    botton_top_vis.setLayoutParams(params);
                    up_arrow.setVisibility(View.VISIBLE);
                    down_arrow.setVisibility(View.GONE);

                }else if(height == 400){
                    ViewGroup.LayoutParams params = botton_top_vis.getLayoutParams();
                    params.height = 1250;
                    botton_top_vis.setLayoutParams(params);
                    up_arrow.setVisibility(View.VISIBLE);
                    down_arrow.setVisibility(View.GONE);
                }else{
                    ViewGroup.LayoutParams params = botton_top_vis.getLayoutParams();
                    params.height = 525;
                    botton_top_vis.setLayoutParams(params);
                    up_arrow.setVisibility(View.GONE);
                    down_arrow.setVisibility(View.VISIBLE);
                }
            }
        });

        /*--------------------------------------End Map-----------------------*/
        chat_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Freshchat.showConversations(getApplicationContext());
            }
        });

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        orderpath = intent.getStringExtra("orderpath");
        orderdate = intent.getStringExtra("orderdate");
        clientname = intent.getStringExtra("clientname");
        clientid = intent.getStringExtra("clientid");
        clientphonenumber = intent.getStringExtra("clientphonenumber");
        gpay_apikey = intent.getStringExtra("gpay_apikey");


        getpublisekey(orderpath);

        Log.e("itemvalue1", "" + orderid);
        Log.e("itemvalue2", "" + orderpath);
        Log.e("itemvalue3", "" + orderdate);
        Log.e("itemvalue4", "" + clientname);
        Log.e("itemvalue5", "" + clientid);
        Log.e("itemvalue6", "" + clientphonenumber);


        //   order_date.setText(orderdate);

        order_id.setText("Order Id: " + orderid);

       // getodertrackingdeatils(orderid, orderpath);

         getstuarttracking(orderid, orderpath);

        handler.postDelayed(new Runnable() {
            public void run() {
               // getodertrackingdeatils(orderid, orderpath);
                getstuarttracking(orderid, orderpath);
                handler.postDelayed(this, delay);
            }
        }, delay);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
            }
        });

       /* String orderStatus = intent.getStringExtra("orderStatus");
        getOrderStatus(orderStatus);*/

        tracking_layout = findViewById(R.id.tracking_layout);
        bill_layout = findViewById(R.id.bill_layout);

        view_bill = findViewById(R.id.view_bill);
        hide_bill = findViewById(R.id.hide_bill);

        view_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_bill.setVisibility(View.GONE);
                hide_bill.setVisibility(View.VISIBLE);
                tracking_layout.setVisibility(View.GONE);
                bill_layout.setVisibility(View.VISIBLE);
                handler.removeMessages(0);
            }
        });
        hide_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_bill.setVisibility(View.VISIBLE);
                hide_bill.setVisibility(View.GONE);
                tracking_layout.setVisibility(View.VISIBLE);
                bill_layout.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    public void run() {
                       // getodertrackingdeatils(orderid, orderpath);
                        getstuarttracking(orderid, orderpath);
                        handler.postDelayed(this, delay);
                    }
                }, delay);
            }
        });

        call_client.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                /*if (ContextCompat.checkSelfPermission(Order_Status_Activity.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+44 " + clientphonenumber));
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }*/
            }
        });

        tip_button_view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                selectedRadioButton = findViewById(checkedId);
                radio_selectedValue = selectedRadioButton.getText().toString();
                tip_btn.setText("Pay Rider Tip of " +radio_selectedValue);
                tip_u_delivery.setText(radio_selectedValue);
            }
        });




        restaurants_mobile_no.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
               /* Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + rest_phone_no));
                startActivity(dialIntent);*/

                if (ContextCompat.checkSelfPermission(Order_Status_Activity.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+44 " + rest_phone_no));
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }

            }
        });

        chg_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Changeaddress();
            }
        });

        alternative_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent act_ = new Intent(Order_Status_Activity.this,MyAccount_Activity.class);
                startActivity(act_);*/
                PickContactList();
            }
        });


        drive_phone_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Order_Status_Activity.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+44 " + Stuartdrivenumber));
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 125);
                }

            }
        });


        deliver_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((custom_edittxt.getText().toString().trim() != null && !custom_edittxt.getText().toString().isEmpty())) {

                    Log.d("scbsbcbscbschjbc","" + tip_btn.getText().toString());

                    String currentString = tip_btn.getText().toString();
                    String[] separated = currentString.split("£");
                    Deliverypay(separated[1]);

                }else if(radio_selectedValue != null && !radio_selectedValue.isEmpty()){

                    String currentString = tip_btn.getText().toString();
                    String[] separated = currentString.split("£");
                    Deliverypay(separated[1]);

                }else{

                  Toast.makeText(Order_Status_Activity.this,"Please enter amount",Toast.LENGTH_SHORT).show();

                }


            }
        });

            PaymentConfiguration.init(Order_Status_Activity.this, gpay_apikey);

            stripe = new Stripe(
                    Order_Status_Activity.this,
                    Objects.requireNonNull(gpay_apikey)
            );

            Log.e("gpayapikey", "" + gpay_apikey);

            googlePayLauncher = new GooglePayPaymentMethodLauncher(

                    Order_Status_Activity.this,
                    new GooglePayPaymentMethodLauncher.Config(
                            GooglePayEnvironment.Test,
                            "US",
                            "Fusion Kitchen"
                    ),
                    Order_Status_Activity.this::onGooglePayReady,
                    Order_Status_Activity.this::onGooglePayResult
            );

    }




    private void onGooglePayReady(boolean isReady) {
           Log.e("pay_type", "" + isReady);

            if (isReady == false) {
                   Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Google Pay not available in your device.Try another payment method", Snackbar.LENGTH_LONG).show();
            } else {
                 // Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Success", Snackbar.LENGTH_LONG).show();
            }

    }

    private void onGooglePayResult(@NotNull GooglePayPaymentMethodLauncher.Result result) {

        loadingshow();

        Log.e("paymentresult", "" + result);
        if (result instanceof GooglePayPaymentMethodLauncher.Result.Completed) {

            final String paymentMethodId =
                    ((GooglePayPaymentMethodLauncher.Result.Completed) result).getPaymentMethod().id;
            Log.e("paymentMethodId", "" + paymentMethodId);

            Map<String, String> params = new HashMap<String, String>();
            params.put("drivertips", String.valueOf(gpay_amount));
            params.put("app_id", "0");
            params.put("fname", tip_pay_first_name);
            params.put("lname", tip_pay_last_name);
            params.put("email", E_mail);
            params.put("cid", user_id);
            params.put("payment_method_id", paymentMethodId);
            params.put("url", "demo2.fusionepos.co.uk");

            metdpasfullUrl = orderpath + "/gpayProcessDriverTips";
            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            retrofit2.Call<getgpaystuartpayment_model> call = apiService.getstuartpayment(metdpasfullUrl, params);
            Log.e("gpaypass", "" + params + "sdjvdjsv" + metdpasfullUrl );
            call.enqueue(new retrofit2.Callback<getgpaystuartpayment_model>() {
                @Override
                public void onResponse(Call<getgpaystuartpayment_model> call, Response<getgpaystuartpayment_model> response) {
                    int statusCode = response.code();
                    Log.e("statusCode", "" + statusCode);
                    if (statusCode == 200) {
                        hideloading();

                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                              gpay_client_secret = response.body().getData().getClient_secret();
                              Log.e("gpay_client_secret", "" + gpay_client_secret);

                            final ConfirmPaymentIntentParams params =
                                    ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                            paymentMethodId,//'{{PAYMENT_METHOD_ID}}'
                                            gpay_client_secret,
                                            null);
                            stripe.confirmPayment(Order_Status_Activity.this, params);
                            Log.e("sc_params", " " + params);
                        }
                    } else {
                        hideloading();
                        Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Client secret id not found", Snackbar.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(retrofit2.Call<getgpaystuartpayment_model> call, Throwable t) {
                    hideloading();
                    Log.e("errorcode1", "" + t);
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            });

        }
        else if (result instanceof GooglePayPaymentMethodLauncher.Result.Canceled) {
            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Failed) {
            Log.e("paymentfails", "" + ((GooglePayPaymentMethodLauncher.Result.Failed) result).getError());
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

        }


    }


    private void getpublisekey(String menuurlpath) {
        bulkeyfullUrl = menuurlpath + "/stripeAppId";
        Log.d("stripeappid",bulkeyfullUrl);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<appkey> call = apiService.stripepubliskey(bulkeyfullUrl);
        call.enqueue(new Callback<appkey>() {
            @Override
            public void onResponse(Call<appkey> call, Response<appkey> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        Log.e("api_id", "" + response.body().getData().getApi_id());
                        Log.e("securitykey", "" + response.body().getData().getSecuritykey());
                        token = response.body().getData().getSecuritykey();
                        apikey = response.body().getData().getApi_id();

                    }
                } else {
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<appkey> call, Throwable t) {
                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void PickContactList() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }


    }


    private void Deliverypay(String pay_amt) {

        Dialog deliverytip = new Dialog(mContext);
        deliverytip.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deliverytip.setContentView(R.layout.delivery_tips_pop_up);

        card_number = deliverytip.findViewById(R.id.card_number);
        EditText  exp_date = deliverytip.findViewById(R.id.exp_date);
        EditText exp_month = deliverytip.findViewById(R.id.exp_month);
        LinearLayout card_no_layout = deliverytip.findViewById(R.id.card_no_layout);
        LinearLayout btn_credit_debit = deliverytip.findViewById(R.id.btn_credit_debit);
        LinearLayout payment_type = deliverytip.findViewById(R.id.payment_type);
        TextView card_pop_up_btn = deliverytip.findViewById(R.id.card_pop_up_btn);
        TextView pay_stuart_card = deliverytip.findViewById(R.id.pay_stuart_card);
        EditText card_cvv_no = deliverytip.findViewById(R.id.card_cvv_no);
        LinearLayout stuart_gpay = deliverytip.findViewById(R.id.stuart_gpay);


       //  pay_amt
        pay_stuart_card.setText("Pay £" + pay_amt);

        pay_amt1 = pay_amt;


        btn_credit_debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_no_layout.setVisibility(View.VISIBLE);
                payment_type.setVisibility(View.GONE);
            }
        });

        card_pop_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliverytip.dismiss();
            }
        });

        card_number.addTextChangedListener(creditCardNumberWatcher);



        pay_stuart_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tip_cardno = card_number.getText().toString().trim();
                String tip_expdate = exp_date.getText().toString().trim();
                String tip_month = exp_month.getText().toString().trim();
                String tip_cardccv = card_cvv_no.getText().toString().trim();


                CardInputWidget cardInputWidget = new CardInputWidget(Order_Status_Activity.this);
                cardInputWidget.setCardNumber(tip_cardno);
                cardInputWidget.setCvcCode(tip_cardccv);
                cardInputWidget.setExpiryDate(Integer.parseInt(tip_expdate), Integer.parseInt(tip_month));
                cardInputWidget.setPostalCodeRequired(false);
                PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

                PaymentConfiguration.init(getApplicationContext(), apikey);//new version add this line version 17.0.0
                   stripe = new Stripe(
                        getApplicationContext(),
                        Objects.requireNonNull(apikey)
                );
                stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                    @Override
                    public void onSuccess(PaymentMethod result) {
                        deliverytip.dismiss();
                        loadingshow();
                        Cardpay(result.id,pay_amt);
                        Log.e("createmethodid", "" + result.id);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                 }
            });


        stuart_gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deliverytip.dismiss();
                gpay_amount = (int) Math.round(Float.parseFloat(pay_amt1));
             /*   float hh = Float.parseFloat(pay_amt1);
                float  toatlamt = (hh* 100);
                Log.d("Round_point_tester"," " + Long.valueOf(String.format("%d", (long) toatlamt)) );
              */
                googlePayLauncher.present("GBP", gpay_amount);
            }
        });


        deliverytip.show();
        deliverytip.setCancelable(false);
        deliverytip.setCanceledOnTouchOutside(false);
        deliverytip.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deliverytip.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deliverytip.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deliverytip.getWindow().setGravity(Gravity.BOTTOM);

 }

    private void Cardpay(@Nullable String paymentMethodId,String tippayamt) {
      //  loadingshow();

        Log.e("paymentMethodId", "" + paymentMethodId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("payment_method_id", paymentMethodId);
        params.put("total", tippayamt);
        params.put("fname", tip_pay_first_name);
        params.put("lname", tip_pay_last_name);
        params.put("email", E_mail);
        params.put("app_id", "0");
        metdpasfullUrl = orderpath + "/stripepaymentProcess";
       // metdpasfullUrl = orderpath + "/stripeProcessDriverTips";

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        retrofit2.Call<getclientSecret_model> call = apiService.getclientSecret(metdpasfullUrl, params, "Bearer" + token);
        Log.e("paramsintentpass", "" + params + "" + metdpasfullUrl);
        call.enqueue(new retrofit2.Callback<getclientSecret_model>() {
            @Override
            public void onResponse(Call<getclientSecret_model> call, Response<getclientSecret_model> response) {
                int statusCode = response.code();

                Log.e("statusCode", "" + statusCode);
                Log.d("Paymentissuetest", new Gson().toJson(response.body()));
                if (statusCode == 200) {

                    Log.e("ststau", "" + response.body().getStatus());
                    Log.e("getD_secure", "" + response.body().getSecure().getD_secure());
                    Log.e("gokulnathantest", "" + response.body().getstripe_amount());

                    stripe_reponse_amount = response.body().getstripe_amount();

                    String s = response.body().getSecure().getD_secure();
                    StringTokenizer st = new StringTokenizer(s, ":");
                    String community = st.nextToken();
                    String helpDesk = st.nextToken();
                    String secret = st.nextToken().replace("\"", "").replace("}", "").replace(" ", "");

                    Log.e("community", "" + secret);

                    Order_Status_Activity.this.runOnUiThread(() -> stripe.handleNextActionForPayment(Order_Status_Activity.this, secret));

                    hideloading();

                    Log.e("responseData", "" + response);
                } else {
                    hideloading();
                  }
            }

            @Override
            public void onFailure(retrofit2.Call<getclientSecret_model> call, Throwable t) {
                hideloading();
                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<Order_Status_Activity> activityRef;

        PaymentResultCallback(Order_Status_Activity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final Order_Status_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            Log.e("paymentstatus", "" + status);
            Log.e("paymentIntent", "" + paymentIntent);
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Log.e("paymentintentidshow5", "" + paymentIntent.getId());
                activity.paymentcompleted(paymentIntent.getId(), "true");

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                Log.e("paymentIntenterror1", "" + paymentIntent.getLastPaymentError().getMessage());

                Log.e("paymentIntenterror2", "" + paymentIntent.getLastErrorMessage());

                Log.e("paymentIntenterror3", "" + paymentIntent);


                //showPopup("Your card's security code is incorrect.");
            } else if (status == PaymentIntent.Status.RequiresConfirmation) {

                Log.e("paymentintentidshow2", "" + paymentIntent.getId());
                activity.paymentcompleted(paymentIntent.getId(), "false");

            } else if (status == PaymentIntent.Status.RequiresAction) {

              //  showPopup("We are unable to authenticate your payment method. Please choose a different payment method and try again.");
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final Order_Status_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.e("errormsg", "" + e.toString());

            activity.runOnUiThread(() -> {
                Log.e("errormsg", "" + e.toString());
               // showPopup(e.toString());
            });
        }
    }

    private void paymentcompleted(String intentid, String typestatus) {
        loadingshow();
        Map<String, String> paramspay = new HashMap<String, String>();
        if (typestatus.equalsIgnoreCase("false")) {
            paramspay.put("payment_intent_id", intentid);
        } else {
            paramspay.put("paymentIntent", intentid);
        }

        paramspay.put("drivertips", pay_amt1);
        paramspay.put("fname", tip_pay_first_name);
        paramspay.put("lname", tip_pay_last_name);
        paramspay.put("email", E_mail);
        paramspay.put("app_id", "0");
        intepasfullUrl = orderpath + "/stripeProcessDriverTips";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        retrofit2.Call<completpay_model> call = apiService.completpay(intepasfullUrl, paramspay, "Bearer" + token);

        Log.e("paramspay", "" + paramspay);
        call.enqueue(new retrofit2.Callback<completpay_model>() {
            @Override
            public void onResponse(retrofit2.Call<completpay_model> call, retrofit2.Response<completpay_model> response) {
                int statusCode = response.code();
                Log.e("completedstatusCode", "" + statusCode);
                if (statusCode == 200) {
                     hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        PaymentPop(1,pay_amt1);
                    }else{
                        PaymentPop(0,"");
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<completpay_model> call, Throwable t) {
                hideloading();
                Log.e("errorcode1", "" + t);
                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void PaymentPop(int payment_succes_fail,String amt) {

        Dialog payment_statuspopup = new Dialog(this);
        payment_statuspopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        payment_statuspopup.setContentView(R.layout.payment_status_layout);
        AppCompatButton track_now_btn = payment_statuspopup.findViewById(R.id.track_now_btn);
        LottieAnimationView reject_appect = payment_statuspopup.findViewById(R.id.reject_appect);
        TextView pay_txt = payment_statuspopup.findViewById(R.id.pay_txt);
        TextView pay_desc = payment_statuspopup.findViewById(R.id.pay_desc);

         pay_descval = "Payment for Rider Tip of £ " + amt + " is successfully received";


        if(payment_succes_fail == 1){
            reject_appect.setAnimation(R.raw.orderaccepted);
            reject_appect.playAnimation();
            pay_txt.setText("Payment Success");
            pay_desc.setText(pay_descval);
            track_now_btn.setText("Track Order");
        }else{
            reject_appect.setAnimation(R.raw.paymentfailed);
            reject_appect.playAnimation();
            pay_txt.setText("Payment Failed");
            pay_desc.setText("Due to a technical issue, your transaction \nfailed. Please try again");
            track_now_btn.setText("Make Payment");
        }

        track_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_statuspopup.dismiss();
            }
        });

        payment_statuspopup.show();
        payment_statuspopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        payment_statuspopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        payment_statuspopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        payment_statuspopup.getWindow().setGravity(Gravity.BOTTOM);

    }

    private TextWatcher creditCardNumberWatcher = new TextWatcher() {
        private static final char space = ' ';

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (count > 0 && (start + count) % 5 == 0) {
                final char space = ' ';
                final char c = s.charAt(start + count - 1);
                if (space == c) {
                    return;
                }
                StringBuilder sb = new StringBuilder(s);
                sb.insert(start + count - 1, space);
                card_number.setText(sb.toString());
                card_number.setSelection(sb.length());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void Changeaddress() {

        Dialog chnage_add = new Dialog(mContext);
        chnage_add.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chnage_add.setContentView(R.layout.stuart_change_address);

         change_add_btn = chnage_add.findViewById(R.id.change_add_btn);
         post_code_txtview = chnage_add.findViewById(R.id.post_code_txtview);
         House_doorno_txt = chnage_add.findViewById(R.id.House_doorno_txt);
         street_stuart = chnage_add.findViewById(R.id.street_stuart);
         city_stuart = chnage_add.findViewById(R.id.city_stuart);

         post_code_txtview.setText(""+post_code);
         House_doorno_txt.setText(""+dno);
         street_stuart.setText(""+add1);
         city_stuart.setText(""+add2);




        change_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(post_code_txtview.getText())) {
                    post_code_txtview.setBackgroundDrawable(ContextCompat.getDrawable(Order_Status_Activity.this, R.drawable.stuart_change_add_error));
                    post_code_txtview.requestFocus();
                } else if (TextUtils.isEmpty(House_doorno_txt.getText())) {
                    House_doorno_txt.setBackgroundDrawable(ContextCompat.getDrawable(Order_Status_Activity.this, R.drawable.stuart_change_add_error));
                    House_doorno_txt.requestFocus();
                } else if (TextUtils.isEmpty(street_stuart.getText())) {
                    street_stuart.setBackgroundDrawable(ContextCompat.getDrawable(Order_Status_Activity.this, R.drawable.stuart_change_add_error));
                    street_stuart.requestFocus();
                } else if (TextUtils.isEmpty(city_stuart.getText())) {
                    city_stuart.setBackgroundDrawable(ContextCompat.getDrawable(Order_Status_Activity.this, R.drawable.stuart_change_add_error));
                    city_stuart.requestFocus();
                } else{
                    Userdetails(orderpath,
                            House_doorno_txt.getText().toString().trim(),street_stuart.getText().toString().trim(),
                            city_stuart.getText().toString().trim(),phone_number);

                    chnage_add.dismiss();
                }

            }
         });


        chnage_add.show();
        chnage_add.setCancelable(true);
        chnage_add.setCanceledOnTouchOutside(true);
        chnage_add.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        chnage_add.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chnage_add.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        chnage_add.getWindow().setGravity(Gravity.BOTTOM);

    }




    private void Userdetails(String menu_path, String house_no, String street, String city,String user_phone) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("path", menu_path);
        params.put("orderdetails", orderid);
        params.put("phone", user_phone);
        params.put("add1", street);
        params.put("add2", city);
        params.put("dno", house_no);

        Log.d("sfcscscgscgdcgsdchsfcsfu"," " + params);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<updatestuartaddress_modal> call = apiService.updatestuartaddress(params);

        call.enqueue(new Callback<updatestuartaddress_modal>() {
            @Override
            public void onResponse(Call<updatestuartaddress_modal> call, Response<updatestuartaddress_modal> response) {

                int statusCode = response.code();

                if (statusCode == 200) {


                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        msg = response.body().getData();

                        Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content),msg, Snackbar.LENGTH_LONG).show();

                    }else{
                        Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content),msg, Snackbar.LENGTH_LONG).show();

                    }

                } else {

                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<updatestuartaddress_modal> call, Throwable t) {

                Log.e("Tro", "" + t);

                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });
    }

    private void Orderdetailshare() {

        ordershare_popup = new Dialog(this);
        ordershare_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ordershare_popup.setContentView(R.layout.order_detials);
        AppCompatButton share_now_btn = ordershare_popup.findViewById(R.id.share_now_btn);
        share_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ordershare_popup.dismiss();

                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.fusionkitchen.co.uk/help?_order_id=" + orderid +
                                         "&_order_path=" +orderpath + "&_orderdate=" + orderdate +
                                "&_clientname=" + clientname + "&_clientid="+clientid +"&_clientphonenumber="+clientphonenumber +
                                "&_gpay_apikey="+gpay_apikey

                        ))
                        .setDomainUriPrefix("https://fusionkitchen.page.link")

                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        .buildDynamicLink();
                Uri dynamicLinkUri = dynamicLink.getUri();
                Log.e("main", "  Long refer "+ dynamicLink.getUri());


                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(Uri.parse(""+dynamicLinkUri))  // manually
                        .buildShortDynamicLink()
                        .addOnCompleteListener(Order_Status_Activity.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    Log.e("share_link_item_pop_up ", "short link "+ shortLink.toString());
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                    intent.setType("text/plain");
                                    startActivity(intent);
                                } else {
                                    Log.e("main", " error "+task.getException() );
                                }
                            }
                        });
            }
        });


        ordershare_popup.show();
        ordershare_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ordershare_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ordershare_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        ordershare_popup.getWindow().setGravity(Gravity.BOTTOM);

    }




    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Order_Status_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Order_Status_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Order_Status_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }

                }
            });
            alertDialog.show();

        }
    }

    private void getstuarttracking(String orderid, String orderpath) {

        Map<String, String> params = new HashMap<String, String>();
       /* params.put("orderdetails", "2328");
        params.put("path", "restaurant-demo-2-if28threefield-house-sk11");*/
        params.put("orderdetails", orderid);
        params.put("path", orderpath);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<ordertracking_model> call = apiService.stuartordertracking(params);
        Log.e("ur_id_", "" + params);
        call.enqueue(new Callback<ordertracking_model>() {

            @Override
            public void onResponse(Call<ordertracking_model> call, Response<ordertracking_model> response) {
                int statusCode = response.code();

                Log.e("Order_tracking_design", new Gson().toJson(response.body()));

                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        String order_status  = response.body().getOrdertracking().getOrder().getOrder().getStatus();

                        rest_name.setText(response.body().getOrdertracking().getOrder().getrest().getName());

                        rest_phone_no = response.body().getOrdertracking().getOrder().getrest().getmobile();

                        String fname = response.body().getOrdertracking().getOrder().getUser().getFname();

                        String lname = response.body().getOrdertracking().getOrder().getUser().getLname();

                         dno = response.body().getOrdertracking().getOrder().getUser().getdno();

                         add1 = response.body().getOrdertracking().getOrder().getUser().getadd1();

                         add2 = response.body().getOrdertracking().getOrder().getUser().getadd2();

                         post_code = response.body().getOrdertracking().getOrder().getUser().getpostcode();

                         phone_number = response.body().getOrdertracking().getOrder().getUser().getPhone();

                         E_mail = response.body().getOrdertracking().getOrder().getUser().getemail();

                         user_delivery_address.setText(dno + "," + add1+ "," +add2+","+post_code);

                         name_phoneno.setText(fname+ " "+lname + "," +phone_number);

                        tip_pay_first_name = fname;
                        tip_pay_last_name = lname;

                        String item_total_count = response.body().getOrdertracking().getOrder().getOrder().getorderCount();

                        String item_total_amt = response.body().getOrdertracking().getOrder().getOrder().getTotal();

                        total_item_count.setText(item_total_count +" Items");

                        sub_amt_stuart.setText("£"+item_total_amt);


                         pickup_lat = response.body().getOrdertracking().getOrder().getOrder().getpickup_latitude();
                         pickup_long = response.body().getOrdertracking().getOrder().getOrder().getpickup_longitude();

                         dropoff_lat = response.body().getOrdertracking().getOrder().getOrder().getdropoff_latitude();
                         dropoff_long = response.body().getOrdertracking().getOrder().getOrder().getdropoff_longitude();

                         driver_lat = response.body().getOrdertracking().getOrder().getOrder().getdriver_latitude();
                         driver_long = response.body().getOrdertracking().getOrder().getOrder().getdriver_longitude();

                         stuart_delivery_ = response.body().getOrdertracking().getOrder().getOrder().getdelivery_status();

                         delivery_status = response.body().getOrdertracking().getOrder().getOrder().getdelivery_status();

                         delivery_status_name = response.body().getOrdertracking().getOrder().getOrder().getdelivery_status_name();

                         order_expected_time = response.body().getOrdertracking().getOrder().getOrder().getorder_expected_time();

                         Stuartdrivename = response.body().getOrdertracking().getOrder().getOrder().getdriver_name();

                         Stuartdrivenumber = response.body().getOrdertracking().getOrder().getOrder().getdriver_number();

                         if(Stuartdrivename != null && !Stuartdrivename.isEmpty()){
                             rider_name.setText(Stuartdrivename);
                         }


                         String Stuart_enable_disable = response.body().getOrdertracking().getOrder().getOrder().getstuart_status();

                       if(Stuart_enable_disable.equalsIgnoreCase("true")){

                           stuart_collection_layout.setVisibility(View.GONE);
                           botton_top_vis.setVisibility(View.VISIBLE);
                           tracking_txt.setVisibility(View.VISIBLE);
                           OrderDetails_layout.setVisibility(View.VISIBLE);
                           order_details_view.setVisibility(View.VISIBLE);

                           mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                   .findFragmentById(R.id.map);
                           mapFragment.getMapAsync((OnMapReadyCallback) Order_Status_Activity.this);


                           int height = 50;
                           int width = 50;
                           BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                           Bitmap b=bitmapdraw.getBitmap();
                           Bitmap finalMarker= Bitmap.createScaledBitmap(b, width, height, false);


                           BitmapDrawable bitmapdraw1 = (BitmapDrawable)getResources().getDrawable(R.drawable.bikestuart);
                           Bitmap b1=bitmapdraw1.getBitmap();
                           Bitmap finalMarker1= Bitmap.createScaledBitmap(b1, width, height, false);

                           BitmapDrawable bitmapdraw2 = (BitmapDrawable)getResources().getDrawable(R.drawable.gokul);
                           Bitmap b5=bitmapdraw2.getBitmap();
                           Bitmap finalMarker2= Bitmap.createScaledBitmap(b5, width, height, false);


                           if(delivery_status.equalsIgnoreCase("0")){

                               wait_confirm_icon.setAnimation(R.raw.foodloading);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               confirm_txt_desc.setText("The restaurant will confirm your order soon");

                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {


                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }

                           }else if(delivery_status.equalsIgnoreCase("1")){

                               wait_confirm_icon.setAnimation(R.raw.orderconfirmed);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               confirm_txt_desc.setText("Thanks for your order, You will see the updates along the way");


                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                           && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }


                           }else if(delivery_status.equalsIgnoreCase("2")){

                                wait_confirm_icon.setAnimation(R.raw.ordercancel);
                                wait_confirm_icon.playAnimation();
                                stuart_textview.setText(delivery_status_name);
                                header_txt_status.setText(order_expected_time);

                                confirm_txt_desc.setText("your Order Scheduled will at Delivery Collection at ----- time");

                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");
                               }

                           }else if(delivery_status.equalsIgnoreCase("3")){

                               wait_confirm_icon.setAnimation(R.raw.orderprepare);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);

                               confirm_txt_desc.setText("Your food being prepared in a safe & Hygienic cooking environment");

                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }


                           }else if(delivery_status.equalsIgnoreCase("4")){

                               wait_confirm_icon.setAnimation(R.raw.deliverypickup);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);

                               confirm_txt_desc.setText("Driver ready to pick up your order from Pizza nova");

                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }

                           }else if(delivery_status.equalsIgnoreCase("5")){

                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);

                               confirm_txt_desc.setText("Driver ready to pick up your order from Pizza nova");


                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");
                               }

                           }else if(delivery_status.equalsIgnoreCase("6")){

                               wait_confirm_icon.setAnimation(R.raw.deliverypickup);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");
                               }

                           }else if(delivery_status.equalsIgnoreCase("7")){

                               wait_confirm_icon.setAnimation(R.raw.deliverypickup);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               confirm_txt_desc.setText("Keep a deep lookout, driver is just a couple of minutes away");
                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");
                               }

                           }else if(delivery_status.equalsIgnoreCase("8")){

                               wait_confirm_icon.setAnimation(R.raw.deliverypickup);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");


                                   handler.postDelayed(new Runnable() {
                                       public void run() {
                                           review_screen_layout.setVisibility(View.VISIBLE);
                                           handler.postDelayed(this, delay);
                                       }
                                   }, delay);

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                                   handler.postDelayed(new Runnable() {
                                       public void run() {
                                           review_screen_layout.setVisibility(View.VISIBLE);
                                           handler.postDelayed(this, delay);
                                       }
                                   }, delay);

                               }

                           }else if(delivery_status.equalsIgnoreCase("9")){

                               wait_confirm_icon.setAnimation(R.raw.ordercancel);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);

                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }

                           }else if(delivery_status.equalsIgnoreCase("10")){

                           wait_confirm_icon.setAnimation(R.raw.ordercancel);
                           wait_confirm_icon.playAnimation();
                           stuart_textview.setText(delivery_status_name);
                           header_txt_status.setText(order_expected_time);

                             confirm_txt_desc.setText("We regret to inform you that your order has been rejected");

                               if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                       && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()
                               ) {

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                   midpoint = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_long))).title("Bike Or Car").snippet("Move Point").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");

                               }else if(pickup_lat !=null && pickup_long !=null
                                       && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                       && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                                   origin = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Delivery Address").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker2));
                                   destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Restaurants Address").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));

                                   tip_txt.setVisibility(View.VISIBLE);
                                   tip_module.setVisibility(View.VISIBLE);
                                   driver_details.setVisibility(View.VISIBLE);
                                   delivery_coll_tip_txt.setText("Tip your delivery partner");
                                   delivery_coll_tip_desc.setText("Tip will go directly to your \ndelivery partner");
                               }

                            }else{
                               stuart_textview.setText(" ");
                               header_txt_status.setText(order_expected_time);

                           }



                           if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                                   && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                                   && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                              String url = getDirectionsUrl(origin.getPosition(),midpoint.getPosition(),destination.getPosition());

                              DownloadTask downloadTask = new DownloadTask();

                              downloadTask.execute(url);

                          }else if(pickup_lat !=null && pickup_long !=null
                                   && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                                   && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                               String url = getDirectionsUrltwo(origin.getPosition(),destination.getPosition());

                               DownloadTask downloadTask = new DownloadTask();

                               downloadTask.execute(url);
                           }


                       }else{


                           tracking_txt.setVisibility(View.GONE);
                           botton_top_vis.setVisibility(View.GONE);
                           driver_details.setVisibility(View.GONE);
                           OrderDetails_layout.setVisibility(View.GONE);
                           order_details_view.setVisibility(View.GONE);

                           if(delivery_status.equalsIgnoreCase("0")){

                               collection_anim.setAnimation(R.raw.foodloading);
                               collection_anim.playAnimation();
                               collect_txt_msg.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               collection_txtmsg.setText("The restaurant will confirm your\norder soon");
                               stuart_collection_layout.setVisibility(View.VISIBLE);
                               stuart_delivery_status = true;
                               tip_module.setVisibility(View.GONE);
                               tip_txt.setVisibility(View.GONE);
                               delivery_coll_tip_txt.setText("Tips to Thank");
                               delivery_coll_tip_desc.setText("Tip will go directly \nto takeaway");

                           }else if(delivery_status.equalsIgnoreCase("1")){

                               collection_anim.setAnimation(R.raw.orderprepare);
                               collection_anim.playAnimation();
                               collect_txt_msg.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               collection_txtmsg.setText("Your food is being prepared \n& can be picked up soon!");
                               stuart_collection_layout.setVisibility(View.VISIBLE);
                               stuart_delivery_status = true;
                               tip_module.setVisibility(View.VISIBLE);
                               tip_txt.setVisibility(View.VISIBLE);
                               delivery_coll_tip_txt.setText("Tips to Thank");
                               delivery_coll_tip_desc.setText("Tip will go directly \nto takeaway");

                           }else if(delivery_status.equalsIgnoreCase("2")){

                               collection_anim.setAnimation(R.raw.ordercancel);
                               collection_anim.playAnimation();
                               collect_txt_msg.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               collection_txtmsg.setText("We regret to inform you that \nyour order has been rejected");
                               stuart_collection_layout.setVisibility(View.VISIBLE);
                               stuart_delivery_status = true;
                               tip_module.setVisibility(View.VISIBLE);
                               tip_txt.setVisibility(View.VISIBLE);
                               delivery_coll_tip_txt.setText("Tips to Thank");
                               delivery_coll_tip_desc.setText("Tip will go directly \nto takeaway");

                           }else if(delivery_status.equalsIgnoreCase("3")){

                               collection_anim.setAnimation(R.raw.yourorderready);
                               collection_anim.playAnimation();
                               collect_txt_msg.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               collection_txtmsg.setText("Your Order Is Ready & Can Be \nCollected Now!");
                               tip_module.setVisibility(View.VISIBLE);
                               tip_txt.setVisibility(View.VISIBLE);
                               delivery_coll_tip_txt.setText("Tips to Thank");
                               delivery_coll_tip_desc.setText("Tip will go directly \nto takeaway");

                               if(stuart_delivery_status == true){
                                   stuart_delivery_status = false;
                                   stuart_collection_layout.setVisibility(View.VISIBLE);
                               }else{
                                   stuart_collection_layout.setVisibility(View.GONE);
                               }


                               handler.postDelayed(new Runnable() {
                                   public void run() {
                                       stuart_delivery_status = false;
                                       stuart_collection_layout.setVisibility(View.GONE);
                                       review_screen_layout.setVisibility(View.VISIBLE);
                                       handler.postDelayed(this, delay);
                                   }
                               }, delay);


                           }else if(delivery_status.equalsIgnoreCase("9")){

                               collection_anim.setAnimation(R.raw.ordercancel);
                               collection_anim.playAnimation();
                               collect_txt_msg.setText(delivery_status_name);
                               header_txt_status.setText(order_expected_time);
                               collection_txtmsg.setText("We regret to inform you that \nyour order has been Cancel");
                               stuart_collection_layout.setVisibility(View.VISIBLE);
                               tip_module.setVisibility(View.VISIBLE);
                               tip_txt.setVisibility(View.VISIBLE);
                               delivery_coll_tip_txt.setText("Tips to Thank");
                               delivery_coll_tip_desc.setText("Tip will go directly \nto takeaway");
                               stuart_delivery_status = true;

                           }else{

                               collect_txt_msg.setText("");
                               header_txt_status.setText("");

                           }
                       }


                        List<ordertracking_details_model.item> orderhistory = (response.body().getOrdertracking().getOrder().getItem());
                        OrderstatusitemListAdapter menuitemnameadapter = new OrderstatusitemListAdapter(mContext, (List<ordertracking_details_model.item>) orderhistory);
                        myorderList.setHasFixedSize(true);
                        myorderList.setLayoutManager(new LinearLayoutManager(Order_Status_Activity.this));
                        myorderList.setAdapter(menuitemnameadapter);


                        subtotal_item.setText("£" + response.body().getOrdertracking().getOrder().getOrder().getSub_total());

                        orderlist_discount.setText("£" + response.body().getOrdertracking().getOrder().getOrder().getDiscount());

                        total_itemlist.setText("£" + response.body().getOrdertracking().getOrder().getOrder().getTotal());

                    }


                } else {
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ordertracking_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }
        });

    }

    /*  ---------------------------get api URL first time get type values----------------------------------------------------*/
    /*private void getodertrackingdeatils(String orderiding, String orderpathing) {

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
      *//*  params.put("orderdetails", orderiding);
        params.put("path", orderpathing);*//*

        params.put("orderdetails","1908");
        params.put("path","restaurant-demo-2-if28threefield-house-sk11");


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<ordertracking_model> call = apiService.ordertracking(params);
        Log.e("ur_id", "" + params);
        call.enqueue(new Callback<ordertracking_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ordertracking_model> call, Response<ordertracking_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();


                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        customername = response.body().getOrdertracking().getOrder().getUser().getFname();
                        total_amt.setText("Total Bill: £ " + response.body().getOrdertracking().getOrder().getOrder().getTotal());
                        listview_total.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getTotal());

                        order_date.setText(response.body().getOrdertracking().getOrder().getOrder().get_order_date());
                        Delivery_Collection_time.setText(response.body().getOrdertracking().getOrder().getOrder().get_order_dateword());
                        Log.d("order_date", response.body().getOrdertracking().getOrder().getOrder().get_order_date());


                        Log.d("delivery_collection_time", response.body().getOrdertracking().getOrder().getOrder().get_order_dateword());

                        sub_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getSub_total());
                        if (response.body().getOrdertracking().getOrder().getOrder().getBank().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getBank().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getBank().equalsIgnoreCase("0")) {
                            servicel_layout.setVisibility(View.GONE);
                        } else {
                            servicel_layout.setVisibility(View.VISIBLE);
                            service_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getBank());
                        }
                        if (response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0")) {
                            delivery_layout.setVisibility(View.GONE);
                        } else {
                            delivery_layout.setVisibility(View.VISIBLE);
                            delivery_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge());
                        }
                        if (response.body().getOrdertracking().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0")) {
                            coupon_layout.setVisibility(View.GONE);
                        } else {
                            coupon_layout.setVisibility(View.VISIBLE);
                            coupon_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getPromo_discount());
                        }
                        if (response.body().getOrdertracking().getOrder().getOrder().getBagage().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getBagage().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getBagage().equalsIgnoreCase("0")) {
                            bag_layout.setVisibility(View.GONE);
                        } else {
                            bag_layout.setVisibility(View.VISIBLE);
                            bag_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getBagage());
                        }


                        List<ordertracking_details_model.item> orderhistory = (response.body().getOrdertracking().getOrder().getItem());
                        OrderstatusitemListAdapter menuitemnameadapter = new OrderstatusitemListAdapter(mContext, (List<ordertracking_details_model.item>) orderhistory);
                        myorderList.setHasFixedSize(true);
                        myorderList.setLayoutManager(new LinearLayoutManager(Order_Status_Activity.this));
                        myorderList.setAdapter(menuitemnameadapter);


                        otype = response.body().getOrdertracking().getOrder().getOrder().getOtype();
                        statusshow = response.body().getOrdertracking().getOrder().getOrder().getStatus();


                        if (otype.equalsIgnoreCase("0")) {
                            orderprocessed.setImageResource(R.drawable.deliverybike);
                            textorderpickup.setText("Order Delivered");

                            txtotype = "Delivery";
                        } else {
                            orderprocessed.setImageResource(R.drawable.takeawaybag);
                            textorderpickup.setText("Order Collected");
                            txtotype = "Collection";
                        }


                        if (otype.equalsIgnoreCase("0")) {
//Delivery
                            if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("0")) {
                                getOrderStatus("0");
                                //Order recived
                                if (check_again_btn == true) {
                                    check_again_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewwaitingDialog alert = new ViewwaitingDialog();
                                        alert.shownowaitingDialog(Order_Status_Activity.this);
                                    }
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("1") && response.body().getOrdertracking().getOrder().getOrder().getDrivertracking().equalsIgnoreCase("false")) {

                                //   handler.removeCallbacksAndMessages(null);
                                getOrderStatus("1");
                                if (check_confirmDialog_btn == true) {
                                    check_confirmDialog_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewconfirmDialog alert = new ViewconfirmDialog();
                                        alert.showconfirmDialog(Order_Status_Activity.this);
                                    }
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("1") && response.body().getOrdertracking().getOrder().getOrder().getDrivertracking().equalsIgnoreCase("true")) {
                                //  handler.removeCallbacksAndMessages(null);
                                //order accepted
                                getOrderStatus("2");
                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("3")) {
                                // handler.removeCallbacksAndMessages(null);
                                //Order Delived
                                getOrderStatus("3");

                                if (check_delivedDialog_btn == true) {
                                    check_delivedDialog_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewdelivedDialog alert = new ViewdelivedDialog();
                                        alert.showdelivedDialog(Order_Status_Activity.this);
                                    }
                                }

                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("2")) {
                                // handler.removeCallbacksAndMessages(null);
                                //Order rejected


                                getOrderStatus("0");
                                if (check_reject_btn == true) {
                                    check_reject_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewrejectDialog alert = new ViewrejectDialog();
                                        alert.showrejectDialog(Order_Status_Activity.this);
                                    }
                                }

                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            }

                        } else {


                            if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("0")) {
                                getOrderStatus("0");
                                //Order recived
                                if (check_again_btn == true) {
                                    check_again_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewwaitingDialog alert = new ViewwaitingDialog();
                                        alert.shownowaitingDialog(Order_Status_Activity.this);
                                    }
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("1")) {
                                //   handler.removeCallbacksAndMessages(null);
                                //order accepted
                                getOrderStatus("1");
                                if (check_confirmDialog_btn == true) {
                                    check_confirmDialog_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewconfirmDialog alert = new ViewconfirmDialog();
                                        alert.showconfirmDialog(Order_Status_Activity.this);
                                    }
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("3")) {
                                //  handler.removeCallbacksAndMessages(null);
                                getOrderStatus("3");
                                //Order Delived
                                if (check_delivedDialog_btn == true) {
                                    check_delivedDialog_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewdelivedDialog alert = new ViewdelivedDialog();
                                        alert.showdelivedDialog(Order_Status_Activity.this);
                                    }

                                    if (confirmdialog != null && confirmdialog.isShowing()) {
                                        confirmdialog.cancel();
                                    }
                                    if (waitingdialog != null && waitingdialog.isShowing()) {
                                        waitingdialog.cancel();
                                    }
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("2")) {
                                // handler.removeCallbacksAndMessages(null);
                                getOrderStatus("0");
                                //Order rejected
                                if (check_reject_btn == true) {
                                    check_reject_btn = false;
                                    if (response.body().getOrdertracking().getOrder().getOrder().getorderfeedback().equalsIgnoreCase("0")) {
                                        ViewrejectDialog alert = new ViewrejectDialog();
                                        alert.showrejectDialog(Order_Status_Activity.this);
                                    }
                                }

                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            }


                        }


                    }
                } else {
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ordertracking_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }*/


    /*--------------------Tracking id pass-----------------------*/
    private void getOrderStatus(String orderStatus) {
        if (orderStatus.equals("0")) {
            float alfa = (float) 0.5;
            setStatus(alfa);

        } else if (orderStatus.equals("1")) {
            float alfa = (float) 1;
            setStatus1(alfa);


        } else if (orderStatus.equals("2")) {
            float alfa = (float) 1;
            setStatus2(alfa);


        } else if (orderStatus.equals("3")) {
            float alfa = (float) 1;
            setStatus3(alfa);
        }
    }

    //  alfa - disable  myf --enabale
    private void setStatus(float alfa) {
        float myf = (float) 0.5;


        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));

        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));


        img_orderconfirmed.setAlpha(alfa);
        orderprocessed.setAlpha(alfa);
        orderpickup.setAlpha(alfa);
        orderplaced.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);
        textorderplaced.setAlpha(alfa);

        placed_divider.setAlpha(alfa);


    }

    private void setStatus1(float alfa) {
        float myf = (float) 0.5;

        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));

        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));

        orderplaced.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        orderprocessed.setAlpha(myf);
        orderpickup.setAlpha(myf);

        textorderplaced.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(myf);
        textorderpickup.setAlpha(myf);


        //   view_order_pickup.setAlpha(myf);


    }

    private void setStatus2(float alfa) {
        float myf = (float) 0.5;

        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));

        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));

        orderplaced.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        orderpickup.setAlpha(myf);

        textorderplaced.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);
        orderprocessed.setAlpha(alfa);
        textorderpickup.setAlpha(myf);


    }

    private void setStatus3(float alfa) {
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));


        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderplaced.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        orderpickup.setAlpha(alfa);

        textorderplaced.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);
        orderprocessed.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);


    }


    /*---------------------------Waiting dialog show----------------------------------------------------*/
    public class ViewwaitingDialog {
        public void shownowaitingDialog(Activity activity) {


            waitingdialog = new Dialog(activity, R.style.MaterialDialogSheet);
            // waitingdialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            waitingdialog.setContentView(R.layout.order_status_waiting);

            TextView waiting_txtone_popup = waitingdialog.findViewById(R.id.waiting_txtone_popup);
            TextView waiting_txttwo_popup = waitingdialog.findViewById(R.id.waiting_txttwo_popup);
            TextView pleasewait = waitingdialog.findViewById(R.id.pleasewait);


            waiting_txtone_popup.setText(clientname + " has received your order for " + txtotype);
            waiting_txttwo_popup.setText("Awaiting confirmation from the restaurant.\n You will receive a confirmation mail regarding your " + txtotype + " order status soon.");
            pleasewait.setText("ORDER RECEIVED!");

            ImageView waiting_img = waitingdialog.findViewById(R.id.waiting_img);
            // ImageView waiting_popup_close = waitingdialog.findViewById(R.id.waiting_popup_close);

            Glide.with(Order_Status_Activity.this)
                    .load(R.drawable.waiting_loader)
                    .placeholder(R.drawable.waiting_loader)
                    .into(waiting_img);

/*

            waiting_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    waitingdialog.cancel();

                }
            });
*/


            waitingdialog.show();
        }
    }

    /*---------------------------Confirmation dialog show----------------------------------------------------*/
    public class ViewconfirmDialog {
        public void showconfirmDialog(Activity activity) {
            confirmdialog = new Dialog(activity, R.style.MaterialDialogSheet);
            confirmdialog.setContentView(R.layout.order_status_confirm);
            TextView contitel = confirmdialog.findViewById(R.id.contitel);
            TextView confirm_txtone_popup = confirmdialog.findViewById(R.id.confirm_txtone_popup);
            TextView confirm_txttwo_popup = confirmdialog.findViewById(R.id.confirm_txttwo_popup);

            AppCompatButton confirm_view_status = confirmdialog.findViewById(R.id.confirm_view_status);

            AppCompatButton confirm_view_feedback = confirmdialog.findViewById(R.id.confirm_view_feedback);

            ImageView waiting_img = confirmdialog.findViewById(R.id.waiting_img);

            contitel.setText("ORDER CONFIRMED!");
            confirm_txtone_popup.setText(clientname + " has confirmed your order ");
            confirm_txttwo_popup.setText("Yummy Food Can be Collected soon. Thank You For ordering From " + clientname + ".\n Happy Ordering, Happy Eating");
            //  txtotype
            Glide.with(Order_Status_Activity.this)
                    .load(R.drawable.confirmed)
                    .placeholder(R.drawable.confirmed)
                    .into(waiting_img);

            confirm_view_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmdialog.cancel();

                    Intent intent = new Intent(mContext, Feedback_Activity.class);
                    intent.putExtra("orderid", orderid);
                    intent.putExtra("clientid", clientid);
                    intent.putExtra("Fname", fname);
                    intent.putExtra("gmail", gmail);
                    intent.putExtra("phone", phone);


                    startActivity(intent);

                    if (waitingdialog != null && waitingdialog.isShowing()) {
                        waitingdialog.cancel();
                    }

                    if (confirmdialog != null && confirmdialog.isShowing()) {
                        confirmdialog.cancel();
                    }


                }
            });


            confirm_view_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    confirmdialog.cancel();

                    if (waitingdialog != null && waitingdialog.isShowing()) {
                        waitingdialog.cancel();
                    }
                    if (confirmdialog != null && confirmdialog.isShowing()) {
                        confirmdialog.cancel();
                    }

                }
            });

            new CountDownTimer(4000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {


                    waiting_img.setImageResource(R.drawable.popconfirmed);


                }
            }.start();


            confirmdialog.show();
        }
    }


    /*---------------------------Reject dialog show----------------------------------------------------*/
    public class ViewrejectDialog {
        public void showrejectDialog(Activity activity) {
            rejectdialog = new Dialog(activity, R.style.MaterialDialogSheet);
            rejectdialog.setContentView(R.layout.order_status_reject);


            TextView reject_txtone_popup = rejectdialog.findViewById(R.id.reject_txtone_popup);
            TextView reject_txttwo_popup = rejectdialog.findViewById(R.id.reject_txttwo_popup);
            TextView orderrejetirtel = rejectdialog.findViewById(R.id.orderrejetirtel);
            ImageView reject_popup_close = rejectdialog.findViewById(R.id.reject_popup_close);


            orderrejetirtel.setText("ORDER REJECTED!");
            reject_txtone_popup.setText("Sorry! Unfortunately your order has been rejected by " + clientname);
            reject_txttwo_popup.setText("If your order is paid, you will receive the refund within next 3-5 working days. Please call the takeaway at" + clientphonenumber + " to know more.");


            Button reject_order_again = rejectdialog.findViewById(R.id.reject_order_again);

            reject_order_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });

            reject_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectdialog.cancel();
                    //  startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });

            rejectdialog.show();
        }
    }

    /*---------------------------Delived dialog show----------------------------------------------------*/
    public class ViewdelivedDialog {
        public void showdelivedDialog(Activity activity) {
            delieveddialog = new Dialog(activity, R.style.MaterialDialogSheet);
            delieveddialog.setContentView(R.layout.order_status_delived);


            TextView delieved_txtone_popup = delieveddialog.findViewById(R.id.delieved_txtone_popup);

            delieved_txtone_popup.setText("Hello " + customername + ", Your order was delivered successfully");

            AppCompatButton delived_order_again = delieveddialog.findViewById(R.id.delived_order_again);
            AppCompatButton delived_view_recept = delieveddialog.findViewById(R.id.delived_view_recept);

            ImageView delieved_popup_close = delieveddialog.findViewById(R.id.delieved_popup_close);


            delived_order_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });
            delived_view_recept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    delieveddialog.cancel();
                    view_bill.setVisibility(View.GONE);
                    hide_bill.setVisibility(View.VISIBLE);

                    tracking_layout.setVisibility(View.GONE);
                    bill_layout.setVisibility(View.VISIBLE);

                }
            });

            delieved_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    delieveddialog.cancel();

                }
            });


            delieveddialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);

            startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
            return true;
        }
        return false;
    }

    //permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+44 " + rest_phone_no));
                    startActivity(callIntent);
                } else {

                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Please allow to call the takeaway", Snackbar.LENGTH_LONG).show();
                }

                break;

              case REQUEST_CODE_DRIVER:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:+44 " + Stuartdrivenumber));
                        startActivity(callIntent);
                    } else {

                        Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Please allow to call the takeaway", Snackbar.LENGTH_LONG).show();
                    }

                    break;


             case PERMISSIONS_REQUEST_READ_CONTACTS:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);
                    } else {
                        // Permission denied, disable the functionality that depends on this permission
                        Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Please allow to Contact List", Snackbar.LENGTH_LONG).show();
                    }

               break;

        }



    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        stripe.onPaymentResult(reqCode, data, new Order_Status_Activity.PaymentResultCallback(Order_Status_Activity.this));


        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        @SuppressLint("Range") String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                           @SuppressLint("Range") String  cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:"+cNumber);

                            Userdetails(orderpath,dno,add1,add2,cNumber);


                        }
                        //@SuppressLint("Range") String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    }
                }
                break;
        }
    }



    /*---------------------------------gokul--------------------*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

                   mMap = googleMap;

                if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                    && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                    && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                    mMap.addMarker(origin);
                    mMap.addMarker(midpoint);
                    mMap.addMarker(destination);

                }else if(pickup_lat !=null && pickup_long !=null
                        && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                        && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                    mMap.addMarker(origin);
                    mMap.addMarker(destination);

                }

                   try {

                       boolean success = mMap.setMapStyle(
                               MapStyleOptions.loadRawResourceStyle(
                                       this, R.raw.style_json));
                       if (!success) {
                           Log.e("Map_Order_Activity", "Style parsing failed.");
                       }else{
                           Log.e("Map_Order_Activity", "Style parsing success.");
                       }

                   } catch (Resources.NotFoundException e) {
                       Log.e("Map_Order_Activity", "Can't find style.", e);
                   }


                if(driver_lat !=null && driver_long !=null && pickup_lat !=null && pickup_long !=null
                        && dropoff_lat != null && dropoff_long != null && !driver_lat.isEmpty() && !driver_long.isEmpty()
                        && !pickup_lat.isEmpty() && !pickup_long.isEmpty() && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(midpoint.getPosition(), 16));

                }else if(pickup_lat !=null && pickup_long !=null
                        && dropoff_lat != null && dropoff_long != null && !pickup_lat.isEmpty() && !pickup_long.isEmpty()
                        && !dropoff_lat.isEmpty() && !dropoff_long.isEmpty()){

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination.getPosition(), 16));
                }


                // mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);


                for (int j = 0; j < path.size(); j++) {

                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);


                }


                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.parseColor("#2b70f7"));
                lineOptions.geodesic(true);


            }

// Drawing polyline in the Google Map for the i-th route

            if (points.size() != 0)
                mMap.addPolyline(lineOptions);

        }



    }



    private String getDirectionsUrl(LatLng origin,LatLng midpoint ,LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_midpoint = "midpoint=" + midpoint.latitude + "," + midpoint.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String mode = "mode=driving";

        String parameters = str_origin +"&"+str_midpoint +"&" + str_dest + "&" + mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDoG0FlQiIJX5MlCrEG_U3vHZmZDfEdww0";

        return url;
    }

    private String getDirectionsUrltwo(LatLng origin,LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        //setting transportation mode
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDoG0FlQiIJX5MlCrEG_U3vHZmZDfEdww0";


        Log.d("Google_url"," " + url);

        return url;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void submitfeedback(String struserid, String strorderid, String strclientid,String strcomment,String fname,String gmail,String phone) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", struserid);
        params.put("order_id", strorderid);
        params.put("client_id", strclientid);
        params.put("comments", strcomment);
        params.put("name",fname);
        params.put("email",gmail);
        params.put("phone",phone);
        params.put("foodquality", String.valueOf(food_rating));
        params.put("restuarant_service", String.valueOf(rest_rating));


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<submitfeedback_model> call = apiService.submitfeedback(params);

        Log.e("feedbackparametes", "" + params);

        call.enqueue(new Callback<submitfeedback_model>() {
            @Override
            public void onResponse(Call<submitfeedback_model> call, Response<submitfeedback_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        //finish();
                        Toast.makeText(getApplicationContext(), "Thank you for the feedback", Toast.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<submitfeedback_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }
        });
    }

    public void loadingshow() {

        dialog = new Dialog(Order_Status_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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



    @Override
    public void onBackPressed() {
      Intent back_btn = new Intent(this,Postcode_Activity.class);
      startActivity(back_btn);
      finish();
    }

}
