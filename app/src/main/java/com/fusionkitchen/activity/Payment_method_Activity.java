package com.fusionkitchen.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.assist.AssistStructure;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.paymentgatway.appkey;
import com.fusionkitchen.model.wallet.wallet_walletbutton_model;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.CardOfferAdapter;
import com.fusionkitchen.adapter.CardPromoAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.cart.coupon_valid_model;
import com.fusionkitchen.model.cart.serviceDelCharge_model;
import com.fusionkitchen.model.checkout.chechoutvalidate_model;
import com.fusionkitchen.model.offer.offer_code_model;
import com.fusionkitchen.model.paymentmethod.order_payment_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment_method_Activity extends AppCompatActivity {

    private Context mContext = Payment_method_Activity.this;
    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;

    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;

    /*---------------------------XML ID Call----------------------------------------------------*/


    AppCompatButton button_place;

    String menupathurls;
    String pay_amount, order_types, orderpaymenupathurls;

    public static final String PaytypePREFERENCES = "paymentPrefs";

    private Dialog dialog;
    /*---------------------------Payment Type List----------------------------------------------------*/
    ListView payment_type_list;
    String[] paytypeArray = {"Credit or Debit Card", "Pay with Cash"};
    //, "Google Pay", "Fusion Wallet"

    LinearLayout card_linerhead, cash_linerhead, wallet_linerhead, gpay_linerhead;
    TextView card_name, cash_name, wallet_name, wallet_amount_symbol, wallet_amount_amount, gpay_name;
    ImageView card_img, cash_img, wallet_img, gpay_img;
    /*---------------------------View coupon List----------------------------------------------------*/
    RelativeLayout select_coupon_layout, amt_discount_rel1, amt_service_rel, amt_delivery_rel, bag_delivery_rel;
    CardView card_offer_item_view, card_offer_popup_view;
    ImageView txt_apply_coupon, txt_remove_coupon;
    TextView card_coupon_code, apply_coupon_edit;
    EditText editcoubon;
    RecyclerView recyclerviewOffers, recyclerviewpromo;
    String cardoffertitle, authKey, fullUrl, user_id;
    TextView service_total, delivery_charg_total, bag_charg_total, coupon_amt_dis, order_type, card_total, Sub_total, showamt;
    String payment_type_number, order_type_number, coupon_discount, coupon_type, coupon_Discription, cooking_insttruction;
    LinearLayout ok_offers;
    RelativeLayout no_offers, coupon_showing_layout, paymenttype_layout;
    TextView payment_titiel;
    /*-------------------------SharedPreferences--------------------------------*/
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    String menuurlpath;

    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;

    /*------------------Popup------------------*/
    GifImageView custom_loading_imageView;
    LinearLayout show_details_popup;
    TextView couponcode_popup, amountsave_popup;


    /*---------------------------Session Manager Class----------------------------------------------------*/
    // Session Manager Class
    //  SessionManager session;
    String bulkeyfullUrl, addressids, customerpostcode;

    CardOfferAdapter adapter;
    CardPromoAdapter adapterpromo;

    Double subamt, serviceamt, deliveryamt, cartamt, bagamt;
    double hidddenserviceamt, hidddenfkserviceamt, addhiddencharges;
    TextView text;
    ImageView services_info;
    TextView service_txt_info;

    String str_service_total, hiddencharges, str_delivery_charg_total, str_bag_charg_total;

    boolean show_discoutss;
    String token,apikey;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_payment_method);


        /*---------------------------hind acsnackbarPositiontionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        overridePendingTransition(R.anim.enter, R.anim.exit);

        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Payment_method_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Payment_method_Activity.ViewDialog alert = new Payment_method_Activity.ViewDialog();
            alert.showDialog(Payment_method_Activity.this);
        }


        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);


        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        Log.e("ideuser_ids", "" + user_id);


        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);

        menuurlpath = sharedpreferences.getString("menuurlpath", null);

        /*---------------------------Session Manager Class----------------------------------------------------*/
      /*  session = new SessionManager(getApplicationContext());
        session.checkLogin();*/


        /*---------------------------XML ID Call----------------------------------------------------*/


        button_place = findViewById(R.id.button_place);

        /*---------------------------View coupon List----------------------------------------------------*/
        select_coupon_layout = findViewById(R.id.select_coupon_layout);
        card_offer_item_view = findViewById(R.id.card_offer_item_view);
        txt_remove_coupon = findViewById(R.id.txt_remove_coupon);
        txt_apply_coupon = findViewById(R.id.txt_apply_coupon);
        card_coupon_code = findViewById(R.id.card_coupon_code);
        amt_discount_rel1 = findViewById(R.id.amt_discount_rel1);
        card_offer_popup_view = findViewById(R.id.card_offer_popup_view);

        recyclerviewOffers = findViewById(R.id.recyclerviewOffers);
        recyclerviewpromo = findViewById(R.id.recyclerviewpromo);

        apply_coupon_edit = findViewById(R.id.apply_coupon_edit);
        editcoubon = findViewById(R.id.editcoubon);

        no_offers = findViewById(R.id.no_offers);
        ok_offers = findViewById(R.id.ok_offers);
        coupon_showing_layout = findViewById(R.id.coupon_showing_layout);

        amt_service_rel = findViewById(R.id.amt_service_rel);
        amt_delivery_rel = findViewById(R.id.amt_delivery_rel);
        bag_delivery_rel = findViewById(R.id.bag_delivery_rel);
        service_total = findViewById(R.id.service_total);
        delivery_charg_total = findViewById(R.id.delivery_charg_total);
        bag_charg_total = findViewById(R.id.bag_charg_total);
        coupon_amt_dis = findViewById(R.id.coupon_amt_dis);
        order_type = findViewById(R.id.order_type);
        card_total = findViewById(R.id.card_total);
        Sub_total = findViewById(R.id.Sub_total);
        showamt = findViewById(R.id.showamt);
        //paymenttype_layout = findViewById(R.id.paymenttype_layout);
        payment_titiel = findViewById(R.id.payment_titiel);
        services_info = findViewById(R.id.services_info);
        service_txt_info = findViewById(R.id.service_txt_info);




        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
        Intent intent = getIntent();
        pay_amount = intent.getStringExtra("pay_amount");
        order_types = intent.getStringExtra("order_types");
        cooking_insttruction = intent.getStringExtra("cooking_insttruction");
        addressids = intent.getStringExtra("addressids");
        customerpostcode = intent.getStringExtra("customerpostcode");

       /* if (intent.getStringExtra("customerpostcode") == null) {
            customerpostcode = "";
        } else {
            customerpostcode = intent.getStringExtra("customerpostcode");
        }*/

        Log.e("customerpostcode", "" + customerpostcode);

        /*--------------Back----------------*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // startActivity(new Intent(getApplicationContext(), Add_to_Cart.class));

               /* Intent intentback = new Intent(Payment_method_Activity.this, Add_to_Cart.class);
                intentback.putExtra("cooking_insttructionback", cooking_insttruction);
                startActivity(intentback);*/
            }
        });


        services_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service_txt_info.getVisibility() == View.VISIBLE) {
                    service_txt_info.setVisibility(View.GONE);
                } else {
                    service_txt_info.setVisibility(View.VISIBLE);
                }
            }
        });

        order_type.setText(order_types);
        Sub_total.setText(pay_amount);
        card_total.setText(pay_amount);
        showamt.setText("Total £ " + pay_amount);

        if (order_types.equalsIgnoreCase("Delivery")) {
            order_type_number = "0";
        } else {
            order_type_number = "1";
        }

        amt_discount_rel1.setVisibility(View.GONE);

        coupon_Discription = "no";
        coupon_type = "no";
        coupon_discount = "no";

        /*---------------------------Payment Type List----------------------------------------------------*/

//Liner layout
        card_linerhead = findViewById(R.id.card_linerhead);
        cash_linerhead = findViewById(R.id.cash_linerhead);
        wallet_linerhead = findViewById(R.id.wallet_linerhead);
        gpay_linerhead = findViewById(R.id.gpay_linerhead);
//Textview
        card_name = findViewById(R.id.card_name);
        cash_name = findViewById(R.id.cash_name);
        wallet_name = findViewById(R.id.wallet_name);
        wallet_amount_symbol = findViewById(R.id.wallet_amount_symbol);
        wallet_amount_amount = findViewById(R.id.wallet_amount_amount);
        gpay_name = findViewById(R.id.gpay_name);
//images
        card_img = findViewById(R.id.card_img);
        cash_img = findViewById(R.id.cash_img);
        wallet_img = findViewById(R.id.wallet_img);
        gpay_img = findViewById(R.id.gpay_img);


        orderpayment(order_type_number);//check cash and card api
        //

/*---------------------get securt key and publise key -----------------------------*/
        getpublisekey();


        card_linerhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                card_img.setVisibility(View.VISIBLE);

                cash_linerhead.setBackgroundColor(Color.TRANSPARENT);
                cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                cash_img.setVisibility(View.INVISIBLE);

                wallet_linerhead.setBackgroundColor(Color.TRANSPARENT);
                wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_img.setVisibility(View.INVISIBLE);

                gpay_linerhead.setBackgroundColor(Color.TRANSPARENT);
                gpay_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                gpay_img.setVisibility(View.INVISIBLE);

                payment_type_number = "1";
                //Remove coponcode

          /*    txt_apply_coupon.setVisibility(View.VISIBLE);
                txt_remove_coupon.setVisibility(View.GONE);
                card_coupon_code.setText("");

                select_coupon_layout.setClickable(true);
                amt_discount_rel1.setVisibility(View.GONE);

                card_total.setText(pay_amount);
                serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                coupon_Discription = "no";
                coupon_type = "no";
                coupon_discount = "no";
                menu_offer(menuurlpath, payment_type_number, order_type_number);
*/

            }
        });
        cash_linerhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                card_linerhead.setBackgroundColor(Color.TRANSPARENT);
                card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                card_img.setVisibility(View.INVISIBLE);

                cash_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                cash_img.setVisibility(View.VISIBLE);

                wallet_linerhead.setBackgroundColor(Color.TRANSPARENT);
                wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_img.setVisibility(View.INVISIBLE);

                gpay_linerhead.setBackgroundColor(Color.TRANSPARENT);
                gpay_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                gpay_img.setVisibility(View.INVISIBLE);

                payment_type_number = "0";

                //Remove coupon code
             /*   txt_apply_coupon.setVisibility(View.VISIBLE);
                txt_remove_coupon.setVisibility(View.GONE);
                card_coupon_code.setText("");

                select_coupon_layout.setClickable(true);
                amt_discount_rel1.setVisibility(View.GONE);

                card_total.setText(pay_amount);
                serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                coupon_Discription = "no";
                coupon_type = "no";
                coupon_discount = "no";
                menu_offer(menuurlpath, payment_type_number, order_type_number);*/

            }
        });


        wallet_linerhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_linerhead.setBackgroundColor(Color.TRANSPARENT);
                card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                card_img.setVisibility(View.INVISIBLE);

                cash_linerhead.setBackgroundColor(Color.TRANSPARENT);
                cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                cash_img.setVisibility(View.INVISIBLE);

                wallet_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                wallet_img.setVisibility(View.VISIBLE);

                gpay_linerhead.setBackgroundColor(Color.TRANSPARENT);
                gpay_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                gpay_img.setVisibility(View.INVISIBLE);

                payment_type_number = "9";

                //Remove coponcode
               /* txt_apply_coupon.setVisibility(View.VISIBLE);
                txt_remove_coupon.setVisibility(View.GONE);
                card_coupon_code.setText("");

                select_coupon_layout.setClickable(true);
                amt_discount_rel1.setVisibility(View.GONE);

                card_total.setText(pay_amount);
                serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                coupon_Discription = "no";
                coupon_type = "no";
                coupon_discount = "no";
                menu_offer(menuurlpath, payment_type_number, order_type_number);*/

            }
        });


        gpay_linerhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card_linerhead.setBackgroundColor(Color.TRANSPARENT);
                card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                card_img.setVisibility(View.INVISIBLE);

                cash_linerhead.setBackgroundColor(Color.TRANSPARENT);
                cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                cash_img.setVisibility(View.INVISIBLE);

                wallet_linerhead.setBackgroundColor(Color.TRANSPARENT);
                wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                wallet_img.setVisibility(View.INVISIBLE);

                gpay_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                gpay_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                gpay_img.setVisibility(View.VISIBLE);


                payment_type_number = "8";

                //Remove coponcode
           /*     txt_apply_coupon.setVisibility(View.VISIBLE);
                txt_remove_coupon.setVisibility(View.GONE);
                card_coupon_code.setText("");

                select_coupon_layout.setClickable(true);
                amt_discount_rel1.setVisibility(View.GONE);

                card_total.setText(pay_amount);
                serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                coupon_Discription = "no";
                coupon_type = "no";
                coupon_discount = "no";
                menu_offer(menuurlpath, payment_type_number, order_type_number);
*/
            }
        });



       /* payment_type_list = findViewById(R.id.payment_type_list);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.raw_payment_method, R.id.client_offertwo, paytypeArray);
        payment_type_list.setNestedScrollingEnabled(false);
        Log.e("paytypeArray", "" + paytypeArray);
        payment_type_list.setAdapter(adapter);
        payment_type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                for (int i = 0; i < payment_type_list.getChildCount(); i++) {
                    if (position == i) {
                        payment_type_list.getChildAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                        text = (TextView) payment_type_list.getChildAt(i).findViewById(R.id.client_offertwo);
                        text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        ImageView chk = payment_type_list.getChildAt(i).findViewById(R.id.checkedimg);
                        chk.setVisibility(View.VISIBLE);
                        paymenttypestr = text.getText().toString();
                        if (paymenttypestr.equalsIgnoreCase("Credit or Debit Card")) {
                            payment_type_number = "1";
                        } else if (paymenttypestr.equalsIgnoreCase("Pay with Cash")) {
                            payment_type_number = "0";
                        } else if (paymenttypestr.equalsIgnoreCase("Google Pay")) {
                            payment_type_number = "2";
                        } else if (paymenttypestr.equalsIgnoreCase("Fusion Wallet")) {
                            payment_type_number = "2";
                        } else {
                            payment_type_number = "2";
                        }
                        //Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_LONG).show();
                    } else {
                        payment_type_list.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                        text = (TextView) payment_type_list.getChildAt(i).findViewById(R.id.client_offertwo);
                        text.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                        ImageView chk = payment_type_list.getChildAt(i).findViewById(R.id.checkedimg);
                        chk.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });*/




        /*---------------------------View coupon List----------------------------------------------------*/
        select_coupon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transalate_anim);
                card_offer_item_view.startAnimation(bottomUp);
                card_offer_item_view.setVisibility(View.VISIBLE);
                editcoubon.setText("");
                /*---------------------------Offer RecyclerView ----------------------------------------------------*/
                menu_offer(menuurlpath, payment_type_number, order_type_number);
            }
        });
        /* ---------------------------remove coupon button click ----------------------------------------------------*/
        txt_remove_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_apply_coupon.setVisibility(View.VISIBLE);
                txt_remove_coupon.setVisibility(View.GONE);
                card_coupon_code.setText("");

                select_coupon_layout.setClickable(true);
                amt_discount_rel1.setVisibility(View.GONE);

                card_total.setText(pay_amount);
                serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                coupon_Discription = "no";
                coupon_type = "no";
                coupon_discount = "no";
            }
        });

        card_offer_item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                card_offer_item_view.setVisibility(View.GONE);
            }
        });


        apply_coupon_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardoffertitle = editcoubon.getText().toString();
                couponcodevalidate(menuurlpath, user_id, order_type_number, payment_type_number, cardoffertitle, Sub_total.getText().toString());

            }
        });

        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("card_offer-message"));


        // serviceDelCharge(menuurlpath, but_order_type);


        button_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // pay_amount,order_type_number

                //   show_discoutss
                if (payment_type_number.equalsIgnoreCase("")) {
                    Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), "Please select payment mode", Snackbar.LENGTH_LONG).show();
                } else {


                    if (show_discoutss == true) {

                        if (coupon_discount.equalsIgnoreCase("no")) {
                            View popupView = LayoutInflater.from(Payment_method_Activity.this).inflate(R.layout.coupon_not_popup, null);
                            final PopupWindow popupWindowno = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                            AppCompatButton check_out_last = popupView.findViewById(R.id.check_out_last);
                            AppCompatButton add_offer_new = popupView.findViewById(R.id.add_offer_new);


                            ImageView popup_close = popupView.findViewById(R.id.popup_close);


                            popup_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindowno.dismiss();

                                }
                            });
                            //   str_service_total, str_delivery_charg_total, str_bag_charg_total

                            check_out_last.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    popupWindowno.dismiss();
                                    Intent intent = new Intent(Payment_method_Activity.this, Payment_Settings_Activity.class);
                                    intent.putExtra("pay_type", payment_type_number);//payment type
                                    intent.putExtra("pay_amount", card_total.getText().toString());//amount
                                    intent.putExtra("order_types", order_type_number);//collection delivery type
                                    intent.putExtra("subtotal", pay_amount);//collection delivery type
                                    intent.putExtra("serviceamt", str_service_total);//collection delivery type
                                    intent.putExtra("deliveryamt", str_delivery_charg_total);//collection delivery type
                                    intent.putExtra("bagamt", str_bag_charg_total);//collection delivery type
                                    intent.putExtra("cooking_insttruction", cooking_insttruction);
                                    intent.putExtra("coupon_Discription", coupon_Discription);
                                    intent.putExtra("coupon_type", coupon_type);
                                    intent.putExtra("coupon_discount", coupon_discount);
                                    intent.putExtra("addressids", addressids);
                                    intent.putExtra("hiddencharges", hiddencharges);
                                    intent.putExtra("wallet_amount", wallet_amount_amount.getText().toString());
                                    intent.putExtra("gpay_apikey", apikey);
                                    startActivity(intent);

                                }
                            });


                            add_offer_new.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    popupWindowno.dismiss();

                                    Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transalate_anim);

                                    card_offer_item_view.startAnimation(bottomUp);
                                    card_offer_item_view.setVisibility(View.VISIBLE);

                                    editcoubon.setText("");

                                    /*---------------------------Offer RecyclerView ----------------------------------------------------*/
                                    menu_offer(menuurlpath, payment_type_number, order_type_number);
                                }
                            });


                            popupWindowno.showAsDropDown(popupView, 0, 0);

                        } else {
                            Intent intent = new Intent(Payment_method_Activity.this, Payment_Settings_Activity.class);
                            intent.putExtra("pay_type", payment_type_number);//payment type
                            intent.putExtra("pay_amount", card_total.getText().toString());//amount
                            intent.putExtra("order_types", order_type_number);//collection delivery type
                            intent.putExtra("subtotal", pay_amount);//collection delivery type
                            intent.putExtra("serviceamt", str_service_total);//collection delivery type
                            intent.putExtra("deliveryamt", str_delivery_charg_total);//collection delivery type
                            intent.putExtra("bagamt", str_bag_charg_total);//collection delivery type
                            intent.putExtra("cooking_insttruction", cooking_insttruction);
                            intent.putExtra("coupon_Discription", coupon_Discription);
                            intent.putExtra("coupon_type", coupon_type);
                            intent.putExtra("coupon_discount", coupon_discount);
                            intent.putExtra("addressids", addressids);
                            intent.putExtra("hiddencharges", hiddencharges);
                            intent.putExtra("wallet_amount", wallet_amount_amount.getText().toString());
                            intent.putExtra("gpay_apikey", apikey);
                            startActivity(intent);

                        }

                    } else {

                        Intent intent = new Intent(Payment_method_Activity.this, Payment_Settings_Activity.class);
                        intent.putExtra("pay_type", payment_type_number);//payment type
                        intent.putExtra("pay_amount", card_total.getText().toString());//amount
                        intent.putExtra("order_types", order_type_number);//collection delivery type
                        intent.putExtra("subtotal", pay_amount);//collection delivery type
                        intent.putExtra("serviceamt", str_service_total);//collection delivery type
                        intent.putExtra("deliveryamt", str_delivery_charg_total);//collection delivery type
                        intent.putExtra("bagamt", str_bag_charg_total);//collection delivery type
                        intent.putExtra("cooking_insttruction", cooking_insttruction);
                        intent.putExtra("coupon_Discription", coupon_Discription);
                        intent.putExtra("coupon_type", coupon_type);
                        intent.putExtra("coupon_discount", coupon_discount);
                        intent.putExtra("addressids", addressids);
                        intent.putExtra("hiddencharges", hiddencharges);
                        intent.putExtra("wallet_amount", wallet_amount_amount.getText().toString());
                        intent.putExtra("gpay_apikey", apikey);
                        startActivity(intent);
                    }
                }
            }
        });

        button_place.setClickable(false);
    }

    /*---------------------------Payment type showing----------------------------------------------------*/
    private void orderpayment(String strordermode) {
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("ordermode", strordermode);

        orderpaymenupathurls = menuurlpath + "/orderpayment";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<order_payment_model> call = apiService.orderpayment(orderpaymenupathurls, params);

        Log.e("paramscart", "" + params);
        call.enqueue(new Callback<order_payment_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<order_payment_model> call, Response<order_payment_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        // paymenttype_layout.setVisibility(View.VISIBLE);

                        Log.e("Card1", "" + response.body().getData().getCard());
                        Log.e("Card2", "" + response.body().getData().getCash());


//Card & cash Aviable to show below
                        if (response.body().getData().getCard().equalsIgnoreCase("1") && response.body().getData().getCash().equalsIgnoreCase("0")) {
                            payment_type_number = "1";
                            serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                            menu_offer(menuurlpath, payment_type_number, order_type_number);

                            card_linerhead.setVisibility(View.VISIBLE);
                            cash_linerhead.setVisibility(View.VISIBLE);
                            payment_titiel.setVisibility(View.VISIBLE);

                            card_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                            card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                            card_img.setVisibility(View.VISIBLE);

                            cash_linerhead.setBackgroundColor(Color.TRANSPARENT);
                            cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            cash_img.setVisibility(View.INVISIBLE);

                            wallet_linerhead.setBackgroundColor(Color.TRANSPARENT);
                            wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_img.setVisibility(View.INVISIBLE);
//check Google Pay available or not
                            if (response.body().getData().getGooglepay().equalsIgnoreCase("8")) {
                                gpay_linerhead.setVisibility(View.VISIBLE);
                            } else {
                                gpay_linerhead.setVisibility(View.GONE);
                            }

//No Card only cash available to show below
                        } else if (response.body().getData().getCard().equalsIgnoreCase("") && response.body().getData().getCash().equalsIgnoreCase("0")) {
                            payment_type_number = "0";
                            serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                            menu_offer(menuurlpath, payment_type_number, order_type_number);

                            card_linerhead.setVisibility(View.GONE);
                            cash_linerhead.setVisibility(View.VISIBLE);
                            payment_titiel.setVisibility(View.VISIBLE);

                            card_linerhead.setBackgroundColor(Color.TRANSPARENT);
                            card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            card_img.setVisibility(View.INVISIBLE);

                            cash_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                            cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                            cash_img.setVisibility(View.VISIBLE);

                            wallet_linerhead.setBackgroundColor(Color.TRANSPARENT);
                            wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_img.setVisibility(View.INVISIBLE);
                            //check Google Pay available or not
                            if (response.body().getData().getGooglepay().equalsIgnoreCase("8")) {
                                gpay_linerhead.setVisibility(View.VISIBLE);
                            } else {
                                gpay_linerhead.setVisibility(View.GONE);
                            }
//No cash only card available to show below
                        } else if (response.body().getData().getCard().equalsIgnoreCase("1") && response.body().getData().getCash().equalsIgnoreCase("")) {
                            payment_type_number = "1";
                            serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                            menu_offer(menuurlpath, payment_type_number, order_type_number);

                            card_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                            card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                            card_img.setVisibility(View.VISIBLE);

                            card_linerhead.setVisibility(View.VISIBLE);
                            cash_linerhead.setVisibility(View.GONE);
                            payment_titiel.setVisibility(View.VISIBLE);

                            cash_linerhead.setBackgroundColor(Color.TRANSPARENT);
                            cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            cash_img.setVisibility(View.INVISIBLE);

                            wallet_linerhead.setBackgroundColor(Color.TRANSPARENT);
                            wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                            wallet_img.setVisibility(View.INVISIBLE);
                            //check Google Pay available or not
                            if (response.body().getData().getGooglepay().equalsIgnoreCase("8")) {
                                gpay_linerhead.setVisibility(View.VISIBLE);
                            } else {
                                gpay_linerhead.setVisibility(View.GONE);
                            }

                        } else if (response.body().getData().getCard().equalsIgnoreCase("") && response.body().getData().getCash().equalsIgnoreCase("")) {


//check Google Pay available or not
                            if (response.body().getData().getGooglepay().equalsIgnoreCase("8")) {
                                gpay_linerhead.setVisibility(View.VISIBLE);
                                payment_titiel.setVisibility(View.VISIBLE);
                                card_linerhead.setVisibility(View.GONE);
                                cash_linerhead.setVisibility(View.GONE);

                                payment_type_number = "8";
                                serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);
                                menu_offer(menuurlpath, payment_type_number, order_type_number);

                                gpay_linerhead.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_blu_col));
                                gpay_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                gpay_img.setVisibility(View.VISIBLE);

                                card_linerhead.setBackgroundColor(Color.TRANSPARENT);
                                card_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                                card_img.setVisibility(View.INVISIBLE);

                                cash_linerhead.setBackgroundColor(Color.TRANSPARENT);
                                cash_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                                cash_img.setVisibility(View.INVISIBLE);

                                wallet_linerhead.setBackgroundColor(Color.TRANSPARENT);
                                wallet_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                                wallet_amount_symbol.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                                wallet_amount_amount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                                wallet_img.setVisibility(View.INVISIBLE);

                            } else {
                                card_linerhead.setVisibility(View.GONE);
                                cash_linerhead.setVisibility(View.GONE);
                                payment_titiel.setVisibility(View.GONE);
                                gpay_linerhead.setVisibility(View.GONE);
                                wallet_linerhead.setVisibility(View.GONE);
                                payment_type_number = "";
                                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), "Please select payment mode", Snackbar.LENGTH_LONG).show();
                            }
                        }

                    } else {
                        card_linerhead.setVisibility(View.GONE);
                        cash_linerhead.setVisibility(View.GONE);
                        payment_titiel.setVisibility(View.GONE);
                        gpay_linerhead.setVisibility(View.GONE);
                        wallet_linerhead.setVisibility(View.GONE);
                        // paymenttype_layout.setVisibility(View.GONE);
                        Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                    }
                } else {
                    card_linerhead.setVisibility(View.GONE);
                    cash_linerhead.setVisibility(View.GONE);
                    payment_titiel.setVisibility(View.GONE);
                    gpay_linerhead.setVisibility(View.GONE);
                    wallet_linerhead.setVisibility(View.GONE);
                    // paymenttype_layout.setVisibility(View.GONE);
                    Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<order_payment_model> call, Throwable t) {
                card_linerhead.setVisibility(View.GONE);
                cash_linerhead.setVisibility(View.GONE);
                gpay_linerhead.setVisibility(View.GONE);
                payment_titiel.setVisibility(View.GONE);
                wallet_linerhead.setVisibility(View.GONE);
                // paymenttype_layout.setVisibility(View.GONE);
                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*---------------------------MenuItemAdapter item value get add button click----------------------------------------------------*/
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            cardoffertitle = intent.getStringExtra("cardoffertitle");
            couponcodevalidate(menuurlpath, user_id, order_type_number, payment_type_number, cardoffertitle, Sub_total.getText().toString());
        }
    };


    /*---------------------------check internet connection----------------------------------------------------*/
    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Payment_method_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Payment_method_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Payment_method_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }


                }
            });
            alertDialog.show();

        }
    }


    /*---------------------------Offer RecyclerView ----------------------------------------------------*/
    private void menu_offer(String menuurlpath, String paymentmode, String ordermodeoffer) {


        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("payment_mode", paymentmode);
        params.put("ordermode", ordermodeoffer);


        menupathurls = menuurlpath + "/apipayment";


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<offer_code_model> call = apiService.offershowmethod(menupathurls, params);

        Log.e("paramscart", "" + params);
        Log.e("paramscart", "" + menupathurls);
        call.enqueue(new Callback<offer_code_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<offer_code_model> call, Response<offer_code_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();

                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        coupon_showing_layout.setVisibility(View.VISIBLE);
                        no_offers.setVisibility(View.GONE);
                        ok_offers.setVisibility(View.VISIBLE);
                        show_discoutss = true;

                        adapter = new CardOfferAdapter(mContext, response.body().getDiscount_list().getDiscountcode());
                        recyclerviewOffers.setHasFixedSize(true);
                        recyclerviewOffers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        recyclerviewOffers.setItemAnimator(new DefaultItemAnimator());
                        recyclerviewOffers.setAdapter(adapter);

                        if (adapter.getItemCount() == 0) {
                            recyclerviewOffers.setVisibility(View.GONE);
                        } else {

                            recyclerviewOffers.setVisibility(View.VISIBLE);

                        }

                        Log.e("recyclerviewOffers1", "" + response.body().getDiscount_list().getDiscountcode().size());
                        adapterpromo = new CardPromoAdapter(mContext, response.body().getDiscount_list().getPromocode());
                        recyclerviewpromo.setHasFixedSize(true);
                        recyclerviewpromo.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        recyclerviewpromo.setItemAnimator(new DefaultItemAnimator());
                        recyclerviewpromo.setAdapter(adapterpromo);

                        if (adapterpromo.getItemCount() == 0) {
                            recyclerviewpromo.setVisibility(View.GONE);
                        } else {
                            recyclerviewpromo.setVisibility(View.VISIBLE);

                        }

                        Log.e("recyclerviewOffers2", "" + adapterpromo.getItemCount());


                    } else {

                        show_discoutss = false;
                        coupon_showing_layout.setVisibility(View.GONE);
                        no_offers.setVisibility(View.VISIBLE);
                        ok_offers.setVisibility(View.GONE);
                        // Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), "Discount not available today", Snackbar.LENGTH_LONG).show();
                    }
                } else {

                    Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<offer_code_model> call, Throwable t) {

                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }



    /*  ------------------------------COUPON AMOUNT IN CART PAGE CALCULATION-------------------------*/

    private void couponcodevalidate(final String menuurlpath, String usercid, String ordermode, String paymenttype, String code, String subtotal) {

        //final ProgressDialog loader = ProgressDialog.show(Add_to_Cart.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", usercid);
        params.put("ordermode", ordermode);
        params.put("paymenttype", paymenttype);
        params.put("code", code);
        params.put("subtotal", subtotal);


        Log.d("coupencode","User->"+usercid + "ordermode->"+  ordermode +"paymenttype->"+ paymenttype + "code->"+code +"subtotsl->"+subtotal);


       /* HashMap<String, String> user = session.getUserDetails();
        authKey = user.get(SessionManager.KEY_phpsessid);*/
        fullUrl = menuurlpath + "/menu/couponAPI";

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<coupon_valid_model> call = apiService.getcouponvalid(fullUrl, params);

        Log.e("paramsval", "" + params);
        call.enqueue(new Callback<coupon_valid_model>() {
            @Override
            public void onResponse(Call<coupon_valid_model> call, Response<coupon_valid_model> response) {
                int statusCode = response.code();

                if (statusCode == 200) {

                    // loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        //  Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), response.body().getDiscount(), Snackbar.LENGTH_LONG).show();


                        card_coupon_code.setText(cardoffertitle);
                        card_offer_item_view.setVisibility(View.GONE);

                        txt_apply_coupon.setVisibility(View.GONE);
                        txt_remove_coupon.setVisibility(View.VISIBLE);

                        select_coupon_layout.setClickable(false);
                        amt_discount_rel1.setVisibility(View.VISIBLE);


                        card_total.setText(response.body().getTotal());
                        coupon_amt_dis.setText(response.body().getDiscount());


                        serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);

                        Log.d("servercharge",""+menuurlpath);
                        Log.d("servercharge",""+order_type_number);
                        Log.d("servercharge",""+Sub_total.getText().toString());
                        Log.d("servercharge",""+payment_type_number);
                        Log.d("servercharge",""+customerpostcode);


                        coupon_Discription = response.body().getDiscription();
                        coupon_type = response.body().getType();
                        coupon_discount = response.body().getDiscount();

                        Log.e("servicetotal", "" + coupon_discount);

                        Log.e("card_total", "" + card_total.getText().toString());

                     /*   final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);

                        showPopup(response.body().getDiscount(), card_coupon_code.getText().toString());

*/
                        card_offer_popup_view.setVisibility(View.VISIBLE);
                        custom_loading_imageView = findViewById(R.id.custom_loading_imageView);
                        couponcode_popup = findViewById(R.id.couponcode_popup);
                        amountsave_popup = findViewById(R.id.amountsave_popup);


                        couponcode_popup.setText("' " + card_coupon_code.getText().toString() + " applied" + " '");
                        amountsave_popup.setText("You saved £ " + response.body().getDiscount());

                        custom_loading_imageView.setVisibility(View.VISIBLE);


                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                custom_loading_imageView.setVisibility(View.GONE);
                                new CountDownTimer(1000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        card_offer_popup_view.setVisibility(View.GONE);
                                    }
                                }.start();
                            }
                        }.start();


                    } else {
                        //  Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                        card_offer_item_view.setVisibility(View.GONE);
                        final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
                        final Snackbar snackbar = Snackbar.make(coordinatorLayoutView, "", 5000);
                        View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
                        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                        snackbarLayout.setPadding(0, 0, 0, 0);
                        TextView textView1 = customSnackView.findViewById(R.id.textView1);
                        textView1.setText(response.body().getMsg().replaceAll("\\<.*?\\>", ""));
                        snackbarLayout.addView(customSnackView, 0);
                        snackbar.show();
                    }
                } else {
                    // loader.dismiss();
                    Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<coupon_valid_model> call, Throwable t) {

                Log.e("Tro", "" + t);
                // loader.dismiss();
                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }


    /*  ------------------------------serviceDelCharge CALCULATION-------------------------*/
    private void serviceDelCharge(final String menuurlpath, String ordermode, String subtotals, String paytype, String strcustomerpostcode) {

        //final ProgressDialog loader = ProgressDialog.show(Add_to_Cart.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        // HashMap<String, String> user = session.getUserDetails();
        params.put("ordertype", ordermode);
        params.put("postcode", strcustomerpostcode);
        params.put("subtotal", subtotals);
        params.put("payment_type", paytype);

        fullUrl = menuurlpath + "/menu/serviceDelCharge";

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<serviceDelCharge_model> call = apiService.getserviceDelCharge(fullUrl, params);

        Log.e("paramsval", "" + params);
        Log.e("fullUrl", "" + fullUrl);
        call.enqueue(new Callback<serviceDelCharge_model>() {
            @Override
            public void onResponse(Call<serviceDelCharge_model> call, Response<serviceDelCharge_model> response) {
                int statusCode = response.code();

                if (statusCode == 200) {
                    // loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        /*-------------------Service Charge-------------------*/
                        hidddenserviceamt = Double.parseDouble(response.body().getData().getService_charge());
                        hidddenfkserviceamt = Double.parseDouble(response.body().getData().getFk_servicecharge());
                        addhiddencharges = (hidddenserviceamt + hidddenfkserviceamt);
                        service_total.setText(String.format("%.2f", addhiddencharges));

                        str_service_total = response.body().getData().getService_charge();
                        hiddencharges = response.body().getData().getFk_servicecharge();


                        Log.e("servicescharge1", "" + response.body().getData().getService_charge());
                        Log.e("servicescharge2", "" + response.body().getData().getFk_servicecharge());
                        Log.e("servicescharge3", "" + service_total.getText().toString());


                        if (service_total.getText().toString().equalsIgnoreCase("0.00") || service_total.getText().toString().equalsIgnoreCase("") || service_total.getText().toString().isEmpty()) {
                            amt_service_rel.setVisibility(View.GONE);
                        } else {
                            amt_service_rel.setVisibility(View.VISIBLE);
                        }

                       /* if (response.body().getData().getService_charge().equalsIgnoreCase("0.00") || response.body().getData().getService_charge().equalsIgnoreCase("") || response.body().getData().getService_charge().isEmpty()) {
                            amt_service_rel.setVisibility(View.GONE);
                          *//*  service_total.setText("0.00");
                            str_service_total = "0.00";
                            hiddencharges = "0.00";*//*


                        } else {
                            amt_service_rel.setVisibility(View.VISIBLE);

                            hidddenserviceamt = Double.parseDouble(response.body().getData().getService_charge());
                            hidddenfkserviceamt = Double.parseDouble(response.body().getData().getFk_servicecharge());

                            addhiddencharges = (hidddenserviceamt + hidddenfkserviceamt);


                            service_total.setText(String.format("%.2f", addhiddencharges));
                            str_service_total = response.body().getData().getService_charge();
                            hiddencharges = response.body().getData().getFk_servicecharge();

                        }*/

                        if (response.body().getData().getDelivery_charge().equalsIgnoreCase("0.00") || response.body().getData().getDelivery_charge().equalsIgnoreCase("") || response.body().getData().getDelivery_charge().isEmpty()) {
                            amt_delivery_rel.setVisibility(View.GONE);
                            delivery_charg_total.setText("0.00");

                            str_delivery_charg_total = "0.00";
                        } else {
                            amt_delivery_rel.setVisibility(View.VISIBLE);
                            delivery_charg_total.setText(response.body().getData().getDelivery_charge());
                            str_delivery_charg_total = response.body().getData().getDelivery_charge();
                        }
                        if (response.body().getData().getSurcharge().equalsIgnoreCase("0.00") || response.body().getData().getSurcharge().equalsIgnoreCase("") || response.body().getData().getSurcharge().isEmpty()) {
                            bag_delivery_rel.setVisibility(View.GONE);
                            bag_charg_total.setText("0.00");
                            str_bag_charg_total = "0.00";
                        } else {
                            bag_delivery_rel.setVisibility(View.VISIBLE);
                            bag_charg_total.setText(response.body().getData().getSurcharge());
                            str_bag_charg_total = response.body().getData().getSurcharge();
                        }

                        Log.e("service_total", "" + service_total.getText().toString());
                        Log.e("delivery_charg_total", "" + delivery_charg_total.getText().toString());
                        Log.e("bag_charg_total", "" + bag_charg_total.getText().toString());


                        subamt = Double.parseDouble(card_total.getText().toString());
                        deliveryamt = Double.parseDouble(delivery_charg_total.getText().toString());
                        serviceamt = Double.parseDouble(service_total.getText().toString());
                        bagamt = Double.parseDouble(bag_charg_total.getText().toString());


                        cartamt = subamt + deliveryamt + serviceamt + bagamt;

                        Log.e("servicetota2", "" + subamt);
                        Log.e("servicetota3", "" + deliveryamt);
                        Log.e("servicetota4", "" + serviceamt);
                        Log.e("servicetota5", "" + bagamt);

                        Log.e("servicetotal", "" + cartamt);
                        card_total.setText(String.format("%.2f", cartamt));
                        showamt.setText("Total £ " + String.format("%.2f", cartamt));


                        walletpaymentbutonshow(user_id, String.valueOf(cartamt));

                        button_place.setBackgroundResource(R.color.welcome_button_color);
                        button_place.setClickable(true);

                    }
                } else {

                    // loader.dismiss();
                    Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<serviceDelCharge_model> call, Throwable t) {
                Log.e("Tro", "" + t);
                // loader.dismiss();
                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }


    /*  ------------------------------Wallet payment button show-------------------------*/
    private void walletpaymentbutonshow(final String str_userid, String str_totalamt) {
        loadingshow();
        //final ProgressDialog loader = ProgressDialog.show(Add_to_Cart.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        // HashMap<String, String> user = session.getUserDetails();
        params.put("cid", str_userid);
        params.put("order_amount", str_totalamt);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<wallet_walletbutton_model> call = apiService.walletbutton(params);

        Log.e("paramsval", "" + params);
        Log.e("fullUrl", "" + fullUrl);
        call.enqueue(new Callback<wallet_walletbutton_model>() {
            @Override
            public void onResponse(Call<wallet_walletbutton_model> call, Response<wallet_walletbutton_model> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        wallet_linerhead.setVisibility(View.VISIBLE);
                        wallet_amount_amount.setText(response.body().getData().getAmount());
                    } else {
                        wallet_linerhead.setVisibility(View.GONE);
                    }
                } else {
                    hideloading();
                    wallet_linerhead.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<wallet_walletbutton_model> call, Throwable t) {
                hideloading();
                wallet_linerhead.setVisibility(View.GONE);
                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }


    private void getpublisekey() {
        bulkeyfullUrl = menuurlpath + "/stripeAppId";

        Log.d("bulkeyfulurl",bulkeyfullUrl);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<appkey> call = apiService.stripepubliskey(bulkeyfullUrl);
        call.enqueue(new Callback<appkey>() {
            @Override
            public void onResponse(Call<appkey> call, Response<appkey> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    // loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        Log.e("api_id", "" + response.body().getData().getApi_id());
                        Log.e("securitykey", "" + response.body().getData().getSecuritykey());
                        token = response.body().getData().getSecuritykey();
                        apikey = response.body().getData().getApi_id();

                    }
                } else {
                    // loader.dismiss();
                    Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<appkey> call, Throwable t) {
                // loader.dismiss();
                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /*------------------------------coupon applyed show popup--------------------------*/
   /* public void showPopup(String discounts, String codes) {
        View popupView = LayoutInflater.from(Payment_method_Activity.this).inflate(R.layout.apply_coupon_diagao, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        GifImageView custom_loading_imageView = popupView.findViewById(R.id.custom_loading_imageView);
        LinearLayout show_details_popup = popupView.findViewById(R.id.show_details_popup);

        TextView couponcode_popup = popupView.findViewById(R.id.couponcode_popup);
        TextView amountsave_popup = popupView.findViewById(R.id.amountsave_popup);


        couponcode_popup.setText("' " + codes + " applied" + " '");
        amountsave_popup.setText("You saved £ " + discounts);

        show_details_popup.setVisibility(View.VISIBLE);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                custom_loading_imageView.setVisibility(View.GONE);
                //  show_details_popup.setVisibility(View.VISIBLE);
                // custom_loading_imageView.setVisibility(View.GONE);

                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        popupWindow.dismiss();

                    }
                }.start();
            }
        }.start();


        //   popupWindow.dismiss();


        popupWindow.showAsDropDown(popupView, 0, 0);
    }
*/
    /*-------------------Loading Images------------------*/
    public void loadingshow() {

       /* dialog = new Dialog(Payment_method_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loading_layout);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        *//*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        *//*
        // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(Payment_method_Activity.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);

        //...finally show it
        dialog.show();*/
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideloading() {
        //dialog.dismiss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (service_txt_info.getVisibility() == View.VISIBLE) {
            service_txt_info.setVisibility(View.GONE);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (service_txt_info.getVisibility() == View.VISIBLE) {
            service_txt_info.setVisibility(View.GONE);
        }
        return true;
    }*/

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Payment Method Activity");
    }
}
