package com.fusionkitchen.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.OrderHistoryListAdapter;
import com.fusionkitchen.adapter.OrderHistoryitemListAdapter;
import com.fusionkitchen.adapter.OrderstatusitemListAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.order_history.order_details_list_show;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.model.orderstatus.ordertracking_details_model;
import com.fusionkitchen.model.orderstatus.ordertracking_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;

public class Order_Status_Activity extends AppCompatActivity implements OnMapReadyCallback {



    private Context mContext = Order_Status_Activity.this;
    EditText custom_edittxt;
    String radio_selectedValue,stuart_delivery_;
    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*-----------------------------Google Map----------------------------*/
    private GoogleMap mMap;
    MarkerOptions origin, destination;
    Dialog ordershare_popup;

    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    private static final int REQUEST_CODE = 101;


    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;


    String fname;
    String pickup_lat,pickup_long,dropoff_lat,dropoff_long,driver_lat,driver_long;
    /*---------------------------XML ID Call----------------------------------------------------*/

    View view_order_placed, view_order_confirmed, view_order_processed, view_order_pickup, con_divider, ready_divider, placed_divider;
    ImageView img_orderconfirmed, orderprocessed, orderpickup, orderplaced,down_arrow,up_arrow;
    TextView textorderpickup, text_confirmed, textorderprocessed, textorderplaced,total_item_count;

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
    final int delay = 5000; // 1000 milliseconds == 1 second

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id;
    String delivery_status,delivery_status_name;
    RadioGroup tip_button_view;
    TextView rest_name,name_phoneno,sub_amt_stuart,stuart_order_tracking_share_btn;
    boolean check_again_btn = true;
    boolean check_confirmDialog_btn = true;
    boolean check_delivedDialog_btn = true;
    boolean check_reject_btn = true;

    AppCompatButton chat_client, call_client;
    String phone, gmail;
    LinearLayout botton_top;
    CardView botton_top_vis,restaurants_mobile_no;
    LottieAnimationView wait_confirm_icon;
    ImageView item_order_details;
    TextView tip_btn,custom_tip_textview,stuart_textview,tracking_txt;
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

        item_order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Orderdetailshare();
            }
        });

       RatingBar ratingBar=(RatingBar)findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(getApplicationContext(),Float.toString(rating),Toast.LENGTH_LONG).show();
            }

        });

        custom_tip_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tip_button_view.setVisibility(View.GONE);
                custom_edittxt.setVisibility(View.VISIBLE);
                tip_btn.setText("Pay Rider Tip");
            }
        });

        custom_edittxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() != 0) {
                    tip_btn.setText("Pay Rider Tip of £"+cs);

                    Drawable drawable = getResources().getDrawable(R.drawable.pound_icon);
                    custom_edittxt.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);

                }else{
                    tip_btn.setText("Pay Rider Tip");
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

        /*---------------------------Waiting dialog show----------------------------------------------------*/
      /*  Order_Status_Activity.ViewwaitingDialog alert = new Order_Status_Activity.ViewwaitingDialog();
        alert.shownowaitingDialog(Order_Status_Activity.this);*/

        /*---------------------------Confirmation dialog show----------------------------------------------------*/
       /* Order_Status_Activity.ViewconfirmDialog alert = new Order_Status_Activity.ViewconfirmDialog();
        alert.showconfirmDialog(Order_Status_Activity.this);
*/
        /*---------------------------Reject dialog show----------------------------------------------------*/
        /*Order_Status_Activity.ViewrejectDialog alert = new Order_Status_Activity.ViewrejectDialog();
        alert.showrejectDialog(Order_Status_Activity.this);*/


        /*---------------------------delived dialog show----------------------------------------------------*/
       /* Order_Status_Activity.ViewdelivedDialog alert = new Order_Status_Activity.ViewdelivedDialog();
        alert.showdelivedDialog(Order_Status_Activity.this);*/

        tip_button_view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton selectedRadioButton = findViewById(checkedId);
                radio_selectedValue = selectedRadioButton.getText().toString();
                tip_btn.setText("Pay Rider Tip of " +radio_selectedValue);
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
                Intent act_ = new Intent(Order_Status_Activity.this,MyAccount_Activity.class);
                startActivity(act_);
            }
        });

        alternative_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act_ = new Intent(Order_Status_Activity.this,MyAccount_Activity.class);
                startActivity(act_);
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

           /*    // ordershare_popup.dismiss();
                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.fusionkitchen.co.uk/help?_menuurl=" + menuurlpath +
                                "&_postcode=" +key_postcode +
                                "&_keyarea=" +key_area +
                                "&_address=" + key_address +
                                "&_lat="+ key_lat +
                                "&_lng=" + key_lon

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
                        });*/
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
     /*   params.put("orderdetails", "1907");
        params.put("path", "restaurant-demo-2-if28threefield-house-sk11");*/

        params.put("orderdetails", orderid);
        params.put("path", orderpath);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<ordertracking_model> call = apiService.stuartordertracking(params);
        Log.e("ur_id", "" + params);
        call.enqueue(new Callback<ordertracking_model>() {

            @Override
            public void onResponse(Call<ordertracking_model> call, Response<ordertracking_model> response) {
                int statusCode = response.code();


                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        String order_status  = response.body().getOrdertracking().getOrder().getOrder().getStatus();

                        rest_name.setText(response.body().getOrdertracking().getOrder().getrest().getName());

                        rest_phone_no = response.body().getOrdertracking().getOrder().getrest().getmobile();

                        String fname = response.body().getOrdertracking().getOrder().getUser().getFname();

                        String lname = response.body().getOrdertracking().getOrder().getUser().getLname();

                        String dno = response.body().getOrdertracking().getOrder().getUser().getdno();

                        String add1 = response.body().getOrdertracking().getOrder().getUser().getadd1();

                        String add2 = response.body().getOrdertracking().getOrder().getUser().getadd2();

                        String post_code = response.body().getOrdertracking().getOrder().getUser().getpostcode();

                        String phone_number = response.body().getOrdertracking().getOrder().getUser().getPhone();

                        user_delivery_address.setText(dno + "," + add1+ "," +add2+","+post_code);

                        name_phoneno.setText(fname+ " "+lname + "," +phone_number);


                        String item_total_count = response.body().getOrdertracking().getOrder().getOrder().getorderCount();

                        String item_sub_amt = response.body().getOrdertracking().getOrder().getOrder().getSub_total();

                        total_item_count.setText(item_total_count);

                        sub_amt_stuart.setText("£"+item_sub_amt);


                         pickup_lat = response.body().getOrdertracking().getOrder().getOrder().getpickup_latitude();
                         pickup_long = response.body().getOrdertracking().getOrder().getOrder().getpickup_longitude();

                         dropoff_lat = response.body().getOrdertracking().getOrder().getOrder().getdropoff_latitude();
                         dropoff_long = response.body().getOrdertracking().getOrder().getOrder().getdropoff_longitude();

                         driver_lat = response.body().getOrdertracking().getOrder().getOrder().getdriver_latitude();
                         driver_long = response.body().getOrdertracking().getOrder().getOrder().getdriver_longitude();


                        stuart_delivery_ = response.body().getOrdertracking().getOrder().getOrder().getdelivery_status();

                         delivery_status = response.body().getOrdertracking().getOrder().getOrder().getdelivery_status();

                         delivery_status_name = response.body().getOrdertracking().getOrder().getOrder().getdelivery_status_name();


                       String Stuart_enable_disable = response.body().getOrdertracking().getOrder().getOrder().getstuart_status();

                       if(Stuart_enable_disable.equalsIgnoreCase("true")){

                           botton_top_vis.setVisibility(View.VISIBLE);
                           tracking_txt.setVisibility(View.VISIBLE);

                           SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                   .findFragmentById(R.id.map);
                           mapFragment.getMapAsync((OnMapReadyCallback) Order_Status_Activity.this);

                           int height = 100;
                           int width = 100;
                           BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.restaurant);
                           Bitmap b=bitmapdraw.getBitmap();
                           Bitmap finalMarker= Bitmap.createScaledBitmap(b, width, height, false);


                           BitmapDrawable bitmapdraw1 = (BitmapDrawable)getResources().getDrawable(R.drawable.bikestuart);
                           Bitmap b1=bitmapdraw1.getBitmap();
                           Bitmap finalMarker1= Bitmap.createScaledBitmap(b1, width, height, false);



                           if(delivery_status.equalsIgnoreCase("0")){
                               wait_confirm_icon.setAnimation(R.raw.foodloading);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);

                           }else if(delivery_status.equalsIgnoreCase("1")){

                               wait_confirm_icon.setAnimation(R.raw.orderconfirmed);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);

                               origin = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat),Double.parseDouble(driver_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                               destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));


                           }else if(delivery_status.equalsIgnoreCase("2")){

                                wait_confirm_icon.setAnimation(R.raw.delivery);
                                wait_confirm_icon.playAnimation();
                                stuart_textview.setText(delivery_status_name);

                                origin = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat),Double.parseDouble(driver_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                                destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                           }else if(delivery_status.equalsIgnoreCase("3")){

                               wait_confirm_icon.setAnimation(R.raw.orderprepare);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               origin = new MarkerOptions().position(new LatLng(Double.parseDouble(driver_lat),Double.parseDouble(driver_lat))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                               destination = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat), Double.parseDouble(pickup_lat))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                           }else if(delivery_status.equalsIgnoreCase("4")){

                               wait_confirm_icon.setAnimation(R.raw.deliverypickup);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);

                               origin = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat),Double.parseDouble(pickup_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                               destination = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                           }else if(delivery_status.equalsIgnoreCase("5")){

                               stuart_textview.setText(delivery_status_name);
                               origin = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat),Double.parseDouble(pickup_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                               destination = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                           }else if(delivery_status.equalsIgnoreCase("6")){

                               wait_confirm_icon.setAnimation(R.raw.deliverypickup);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               origin = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat),Double.parseDouble(pickup_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                               destination = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                           }else if(delivery_status.equalsIgnoreCase("7")){

                               wait_confirm_icon.setAnimation(R.raw.deliverypickup);
                               wait_confirm_icon.playAnimation();
                               stuart_textview.setText(delivery_status_name);
                               origin = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat),Double.parseDouble(pickup_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                               destination = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                           }else if(delivery_status.equalsIgnoreCase("8")){

                               stuart_textview.setText(delivery_status_name);
                               origin = new MarkerOptions().position(new LatLng(Double.parseDouble(pickup_lat),Double.parseDouble(pickup_long))).title("GokulNathan Start").snippet("origin").icon(BitmapDescriptorFactory.fromBitmap(finalMarker));
                               destination = new MarkerOptions().position(new LatLng(Double.parseDouble(dropoff_lat), Double.parseDouble(dropoff_long))).title("Gokulnathan End").snippet("destination").icon(BitmapDescriptorFactory.fromBitmap(finalMarker1));

                           }else{
                               stuart_textview.setText(" ");
                           }


                           String url = getDirectionsUrl(origin.getPosition(), destination.getPosition());

                           DownloadTask downloadTask = new DownloadTask();

                           downloadTask.execute(url);


                       }else{

                           tracking_txt.setVisibility(View.GONE);
                           botton_top_vis.setVisibility(View.GONE);

                           if(delivery_status.equalsIgnoreCase("0")){

                               stuart_textview.setText(delivery_status_name);

                           }else if(delivery_status.equalsIgnoreCase("1")){

                               stuart_textview.setText(delivery_status_name);

                           }else if(delivery_status.equalsIgnoreCase("2")){

                               stuart_textview.setText(delivery_status_name);

                           }else if(delivery_status.equalsIgnoreCase("3")){

                               stuart_textview.setText(delivery_status_name);

                           }else{

                               stuart_textview.setText("");

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

            }
        });

    }

    /*  ---------------------------get api URL first time get type values----------------------------------------------------*/
    private void getodertrackingdeatils(String orderiding, String orderpathing) {

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
      /*  params.put("orderdetails", orderiding);
        params.put("path", orderpathing);*/

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
    }


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
        }
    }


/*---------------------------------hoku--------------------*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(origin);
        mMap.addMarker(destination);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin.getPosition(), 12));

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
                lineOptions.width(8);
                lineOptions.color(Color.parseColor("#2b70f7"));
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route

            if (points.size() != 0)
                mMap.addPolyline(lineOptions);
        }
    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {

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
        //String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "YOUR_API_KEY";
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

}
