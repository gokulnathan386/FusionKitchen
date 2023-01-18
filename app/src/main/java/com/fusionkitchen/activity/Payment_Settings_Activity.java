package com.fusionkitchen.activity;

import static com.fusionkitchen.DBHelper.SQLDBHelper.DATABASE_NAME;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.viewsavecardAdapter;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.Savecard.addnewsavecard_model;
import com.fusionkitchen.model.Savecard.getclientscSecret_model;
import com.fusionkitchen.model.Savecard.viewsavecard_details_model;
import com.fusionkitchen.model.cart.Cartitem;
import com.fusionkitchen.model.checkout.CheckloginModel;
import com.fusionkitchen.model.gpay.getgpayclientscSecret_model;
import com.fusionkitchen.model.myaccount.get_account_modal;
import com.fusionkitchen.model.paymentgatway.appkey;
import com.fusionkitchen.model.paymentgatway.completpay_model;
import com.fusionkitchen.model.paymentgatway.getclientSecret_model;
import com.fusionkitchen.model.wallet.wallet_getrefer_details;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
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
import com.stripe.android.model.PaymentMethodOptionsParams;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment_Settings_Activity extends AppCompatActivity {
    private Context mContext = Payment_Settings_Activity.this;
    //public static com.stripe.android.model.PaymentIntent.ConfirmationMethod Automatic;
    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;
    /*---------------------------XML ID Call----------------------------------------------------*/
    AppCompatButton amount_change_button, amount_pay_button, add_new_card;
    LinearLayout cash_item, card_item, wallet_item, savecard_item, gpay_item, amount_below_btoon;
    String pay_type, pay_amount, order_types, serviceamt, cooking_insttruction, addressids, hiddencharges, gpay_apikey;
    Dialog dialog;
    TextView showamt, wallet_showamt, walletaviableamt, gpay_showamt;
    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    private ListView listView;
    SQLDBHelper dbHelper;
    /*-------------------------SharedPreferences--------------------------------*/
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    String menuurlpath, fullUrl;
    String ResponseData;
    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, user_email, subtotal, coupon_discount, coupon_type, coupon_Discription;
    String jsonStr, login_type_id;
    /*----------------------card payment-----------------------*/
    String first_name, last_name, token, apikey, strcard_number, strcard_cvv, strcard_date, cardmonth, cardyear;
    EditText card_number, card_date, card_cvv;
    String sc_client_secret, metdpasfullUrl, intepasfullUrl, bulkeyfullUrl;
    int keyDel;
    private Stripe stripe;
    int cardmont, cardyears;
    Dialog imgdialog;
    String referid, refergroup, referstatus, refersecure, refertranfer, deliveryamt, bagamt;
    EditText card_name;
    NestedScrollView nestscro;
    TextView name_txt;
    double totalpayamt, hidddenfkserviceamt, removehiddencharges;
    String amt_total, walletaviableamtstring;
    RecyclerView view_save_card;
    String paybotton_type;
    /*---------------------------save_card_get_methodid----------------------------------------------------*/
    TextView save_title, opensavecard;
    String savecardmid, savecardexpmonth, savecardexpyear, savecardnumbers, sc_card_cvv, savecardholdername;

    CheckBox savecard_checkBox;
    int gpay_amount;
    String tempamount;
    /*---------------------Google Pay---------------------------*/
    String gpay_client_secret;
    GooglePayPaymentMethodLauncher googlePayLauncher;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_payment_seeting);


        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        overridePendingTransition(R.anim.enter, R.anim.exit);
        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Payment_Settings_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Payment_Settings_Activity.ViewDialog alert = new Payment_Settings_Activity.ViewDialog();
            alert.showDialog(Payment_Settings_Activity.this);
        }
        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        user_email = (slogin.getString("login_key_email", null));
        login_type_id = (slogin.getString("type_of_login", null));
        getaccountdetails(user_id, login_type_id);
        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
        Intent intent = getIntent();
        pay_type = intent.getStringExtra("pay_type");
        pay_amount = intent.getStringExtra("pay_amount");
        order_types = intent.getStringExtra("order_types");
        subtotal = intent.getStringExtra("subtotal");
        serviceamt = intent.getStringExtra("serviceamt");
        deliveryamt = intent.getStringExtra("deliveryamt");
        bagamt = intent.getStringExtra("bagamt");
        cooking_insttruction = intent.getStringExtra("cooking_insttruction");
        hiddencharges = intent.getStringExtra("hiddencharges");
        gpay_apikey = intent.getStringExtra("gpay_apikey");

        Log.d("public_key",gpay_apikey);



        if (intent.getStringExtra("coupon_Discription").equalsIgnoreCase("no")) {
            coupon_Discription = "";
        } else {
            coupon_Discription = intent.getStringExtra("coupon_Discription");
        }
        if (intent.getStringExtra("coupon_type").equalsIgnoreCase("no")) {
            coupon_type = "";
        } else {
            coupon_type = intent.getStringExtra("coupon_type");
        }
        if (intent.getStringExtra("coupon_discount").equalsIgnoreCase("no")) {
            coupon_discount = "0.00";
        } else {
            coupon_discount = intent.getStringExtra("coupon_discount");
        }
        addressids = intent.getStringExtra("addressids");
        if (intent.getStringExtra("wallet_amount") == null) {
            walletaviableamtstring = "0";
        } else {
            walletaviableamtstring = intent.getStringExtra("wallet_amount");
        }

        //  intent.putExtra("wallet_amount", wallet_amount_amount.getText().toString());
        // zero_contact = intent.getStringExtra("zero_contact");
      /*  if (intent.getStringExtra("serviceamt") == null) {
            serviceamt = "0.00";
        } else {
            serviceamt = intent.getStringExtra("serviceamt");
        }*/


        gpay_amount = (int) Math.round(Float.parseFloat(pay_amount));// Integer.parseInt(pay_amount);
        /*--------------remove FK service charge------------------*/
        totalpayamt = Double.parseDouble(pay_amount);


        if (pay_amount.contains(".")) {
            tempamount = pay_amount.replace(".", "");
            Log.e("intamount5", "" + Integer.parseInt(tempamount));
        } else {
            tempamount = pay_amount;
        }

       /* Log.e("intamount1", "" + gpay_amount);
        Log.e("intamount2", "" + pay_amount);
        Log.e("intamount3", "" + totalpayamt);
        Log.e("intamount4", "" + (int) totalpayamt * 100);*/


        hidddenfkserviceamt = Double.parseDouble(hiddencharges);
        removehiddencharges = (totalpayamt - hidddenfkserviceamt);
        amt_total = (String.format("%.2f", removehiddencharges));
        Log.e("getvalues1", "" + pay_type);
        Log.e("getvalues2", "" + pay_amount);
        Log.e("getvalues3", "" + order_types);
        Log.e("getvalues4", "" + subtotal);
        Log.e("getvalues5", "" + serviceamt);
        Log.e("getvalues6", "" + deliveryamt);
        Log.e("getvalues7", "" + bagamt);
        Log.e("getvalues8", "" + coupon_Discription);
        Log.e("getvalues9", "" + coupon_type);
        Log.e("getvalues10", "" + coupon_discount);
        Log.e("getvalues11", "" + addressids);
        Log.e("getvalues12", "" + cooking_insttruction);
        Log.e("getvalues13", "" + hiddencharges);
        Log.e("getvalues14", "" + amt_total);
        Log.e("getvalues15", "" + gpay_amount);


        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);
        menuurlpath = sharedpreferences.getString("menuurlpath", null);
        Log.e("menuurlpathcashpay", "" + menuurlpath);

        Log.e("menuurlpathcashpay", "" + sharedpreferences.getString("orderactivetag", null));
        Log.e("menuurlpathcashpay", "" + sharedpreferences.getString("ordertodattime", null));
        Log.e("menuurlpathcashpay", "" + sharedpreferences.getString("orderlaterdate", null));
        Log.e("menuurlpathcashpay", "" + sharedpreferences.getString("orderlatertime", null));






        /*---------------------------XML ID Call----------------------------------------------------*/
        cash_item = findViewById(R.id.cash_item);
        card_item = findViewById(R.id.card_item);
        wallet_item = findViewById(R.id.wallet_item);
        savecard_item = findViewById(R.id.savecard_item);
        gpay_item = findViewById(R.id.gpay_item);
        amount_below_btoon = findViewById(R.id.amount_below_btoon);
        amount_change_button = findViewById(R.id.amount_change_button);
        amount_pay_button = findViewById(R.id.amount_pay_button);
        showamt = findViewById(R.id.showamt);
        wallet_showamt = findViewById(R.id.wallet_showamt);
        walletaviableamt = findViewById(R.id.walletaviableamt);
        gpay_showamt = findViewById(R.id.gpay_showamt);
        opensavecard = findViewById(R.id.opensavecard);
        listView = findViewById(R.id.listView);
        /*-------------------Card Flow XML ID--------------------*/
        card_number = findViewById(R.id.card_number);
        card_date = findViewById(R.id.card_date);
        card_cvv = findViewById(R.id.card_cvv);
        card_cvv.setTransformationMethod(new Payment_Settings_Activity.AsteriskPasswordTransformationMethod());
        card_name = findViewById(R.id.card_name);
        nestscro = findViewById(R.id.nestscro);
        name_txt = findViewById(R.id.name_txt);
        view_save_card = findViewById(R.id.view_save_card);
        save_title = findViewById(R.id.save_title);
        add_new_card = findViewById(R.id.add_new_card);
        savecard_checkBox = findViewById(R.id.savecard_checkBox);


        card_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nestscro.scrollTo(0, 2000);
                    }
                }, 1000);
                return false;
            }
        });
        card_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nestscro.scrollTo(0, 2000);
                    }
                }, 1000);
                return false;
            }
        });

        //card_date.addTextChangedListener(mDateEntryWatcher);
        /*-------------------card date validation----------------*/
        card_date.addTextChangedListener(mDateEntryWatcher);
        /*-------------------card number validation----------------*/
        card_number.addTextChangedListener(new TextWatcher() {
            private boolean spaceDeleted;

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CharSequence charDeleted = s.subSequence(start, start + count);
                spaceDeleted = " ".equals(charDeleted.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                card_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
                card_number.removeTextChangedListener(this);
                int cursorPosition = card_number.getSelectionStart();
                String withSpaces = formatTextVisaMasterCard(editable);
                card_number.setText(withSpaces);
                card_number.setSelection(cursorPosition + (withSpaces.length() - editable.length()));
                if (spaceDeleted) {
                    card_number.setSelection(card_number.getSelectionStart() - 1);
                    spaceDeleted = false;
                }
                card_number.addTextChangedListener(this);
                if (card_number.getText().toString().length() == 19) {
                    card_date.requestFocus();
                }
            }
        });
             /*---------------------get securt key and publise key -----------------------------*/
        getpublisekey();


        PaymentConfiguration.init(Payment_Settings_Activity.this, gpay_apikey);

        stripe = new Stripe(
                Payment_Settings_Activity.this,
                Objects.requireNonNull(gpay_apikey)
        );

        Log.e("gpayapikey", "" + gpay_apikey);
        
        GooglePayPaymentMethodLauncher googlePayLauncher = new GooglePayPaymentMethodLauncher(

                Payment_Settings_Activity.this,
                new GooglePayPaymentMethodLauncher.Config(
                        GooglePayEnvironment.Production,
                        "US",
                        "Fusion Kitchen"
                ),
                Payment_Settings_Activity.this::onGooglePayReady,
                Payment_Settings_Activity.this::onGooglePayResult
        );


        card_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                       if(s.length() == 1 ){
                          amount_pay_button.setBackgroundResource(R.color.welcome_button_color);
                          amount_pay_button.setEnabled(true);
                      }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 1 ){
                    amount_pay_button.setBackgroundResource(R.color.welcome_button_color);
                    amount_pay_button.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        card_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 1 ){
                    amount_pay_button.setBackgroundResource(R.color.welcome_button_color);
                    amount_pay_button.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        card_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 1 ){
                    amount_pay_button.setBackgroundResource(R.color.welcome_button_color);
                    amount_pay_button.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        if (pay_type.equalsIgnoreCase("1")) {
            /*---------------------------card----------------------------------*/
            card_item.setVisibility(View.VISIBLE);
            cash_item.setVisibility(View.GONE);
            wallet_item.setVisibility(View.GONE);
            gpay_item.setVisibility(View.GONE);
            amount_below_btoon.setVisibility(View.VISIBLE);
            amount_change_button.setText("Amount Payable\nTotal £ " + pay_amount);
            paybotton_type = "1";

            amount_pay_button.setBackgroundResource(R.color.txt_two);
            amount_pay_button.setEnabled(false);
            /*-------------------View save Crd--------------------*/
            viewsavecard(user_id);
        } else if (pay_type.equalsIgnoreCase("0")) {
            /*---------------------------cash----------------------------------*/
            card_item.setVisibility(View.GONE);
            cash_item.setVisibility(View.VISIBLE);
            wallet_item.setVisibility(View.GONE);
            gpay_item.setVisibility(View.GONE);
            amount_below_btoon.setVisibility(View.VISIBLE);
            showamt.setText("Total £ " + pay_amount);
            amount_change_button.setText("Change Payment");
            paybotton_type = "0";
            /*---------------------------cash  Payment_Settings_Activity details-----------------------------------*/
            dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
            ArrayList<Cartitem> allContacts = dbHelper.listContacts();
            JSONArray jsonArray1 = new JSONArray();
            for (int i = 0; i < allContacts.size(); i++) {
                Log.e("addonlenth", "" + allContacts.get(i).getAddonnameid().length());
                JSONObject item1 = new JSONObject();
                String[] itemAddonNameIds = allContacts.get(i).getAddonnameid().split(",");
                ArrayList subItemArray = new ArrayList<String>();
                String[] itemAddonextaid = allContacts.get(i).getAddonextraid().split(",");
                if (allContacts.get(i).getAddonnameid().length() != 0) {
                    for (int x = 0; x < itemAddonNameIds.length; x++) {
                        JSONObject subInside = new JSONObject();
                        String itemAddonextaidData = "";
                        if (x < itemAddonextaid.length) {
                            itemAddonextaidData = itemAddonextaid[x];
                        } else {
                            itemAddonextaidData = "";
                        }
                        try {
                            subInside.put("id", itemAddonNameIds[x]);
                            subInside.put("qty", allContacts.get(i).getQty());
                            subInside.put("type", "addon");
                            subInside.put("ext", itemAddonextaidData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        subItemArray.add(subInside);
                    }
                }
                Log.e("itemaddonids", "" + subItemArray.toString());
                try {
                    item1.put("id", allContacts.get(i).getItemid());
                    item1.put("qty", allContacts.get(i).getQty());
                    item1.put("free", "0");
                    item1.put("offer", "");
                    item1.put("sub", new JSONArray(subItemArray));
                   // item1.put("special_instruction",allContacts.get(i).getSpecialinstruction());
                    if(allContacts.get(i).getSpecialinstruction() != null){
                        item1.put("special_instruction",allContacts.get(i).getSpecialinstruction());
                    }else{
                        item1.put("special_instruction","");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray1.put(item1);
            }
            Log.e("itemid", "" + jsonArray1);
            JSONObject promo_discount = new JSONObject();
            try {
                promo_discount.put("discount", coupon_discount);
                promo_discount.put("desc", coupon_Discription);
                promo_discount.put("type", coupon_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject studentsObj = new JSONObject();
            try {
                studentsObj.put("refno", "");//ok
                studentsObj.put("inst", cooking_insttruction);//ok
                studentsObj.put("bank", serviceamt);//ok
                studentsObj.put("bag", bagamt);//ok
                studentsObj.put("activetab", sharedpreferences.getString("orderactivetag", null));//ok
                if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {
                    studentsObj.put("time", "");//ok
                    studentsObj.put("date", "");//ok
                    studentsObj.put("date_string", "");//ok

                } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {
                    studentsObj.put("time", sharedpreferences.getString("ordertodattime", null));//ok
                    studentsObj.put("date", "");//ok
                    studentsObj.put("date_string",sharedpreferences.getString("todaytimestring", null));

                } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
                    studentsObj.put("time", sharedpreferences.getString("orderlatertime", null));//ok
                    studentsObj.put("date", sharedpreferences.getString("orderlaterdate", null));//ok
                    studentsObj.put("date_string",sharedpreferences.getString("latertimestring", null));
                }
                studentsObj.put("preorder_time", "0");//ok
                studentsObj.put("items", jsonArray1);//ok
                studentsObj.put("discount", "0");//ok
                studentsObj.put("coupon", "0");//ok
                studentsObj.put("delivery", deliveryamt);//ok
                studentsObj.put("cid", user_id);//ok
                studentsObj.put("loginmail", user_email);//ok
                studentsObj.put("typeoflogin", login_type_id);//ok
                studentsObj.put("otype", order_types);//ok
                studentsObj.put("deliveryaddress", addressids);
                studentsObj.put("ptype", pay_type);//ok
                studentsObj.put("subtotal", subtotal);//ok
                studentsObj.put("totalamount", amt_total);//ok
                studentsObj.put("zero_contact", "0");
                studentsObj.put("app_id", "0");
                studentsObj.put("promo_discount", promo_discount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonStr = studentsObj.toString();
            Log.e("Mark_JSON111", "" + jsonStr.toString());
        } else if (pay_type.equalsIgnoreCase("9")) {

            /* ------------------------Wallet--------------------------*/
            card_item.setVisibility(View.GONE);
            cash_item.setVisibility(View.GONE);
            wallet_item.setVisibility(View.VISIBLE);
            gpay_item.setVisibility(View.GONE);
            amount_below_btoon.setVisibility(View.VISIBLE);
            wallet_showamt.setText("Total £ " + pay_amount);
            walletaviableamt.setText("Available Balance £ " + walletaviableamtstring);
            amount_change_button.setText("Change Payment");
            paybotton_type = "9";

        } else if (pay_type.equalsIgnoreCase("8")) {
            /* ------------------------Google Pay--------------------------*/
            card_item.setVisibility(View.GONE);
            cash_item.setVisibility(View.GONE);
            wallet_item.setVisibility(View.GONE);
            gpay_item.setVisibility(View.VISIBLE);
            amount_below_btoon.setVisibility(View.VISIBLE);
            gpay_showamt.setText("Total £ " + pay_amount);
            amount_change_button.setText("Change Payment");
            paybotton_type = "8";
        }

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
        final Cursor cursor = dbHelper.getAllItem();
        String[] columns = new String[]{
                SQLDBHelper.ITEM_NAME,
                SQLDBHelper.ITEM_ADDON_NAME,
                SQLDBHelper.ITEM_Final_AMOUNT,
                SQLDBHelper.ITEM_ID
        };
        int[] widgets = new int[]{
                R.id.item_name,
                R.id.item_addon_name,
                R.id.item_total,
                R.id.item_id
        };
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.raw_item_info,
                cursor, columns, widgets, 0);
        listView = findViewById(R.id.listView);
        listView.setNestedScrollingEnabled(true);
        listView.setAdapter(cursorAdapter);


        amount_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paybotton_type.equalsIgnoreCase("0")) {
                    /*--------cash---------*/
                    cashpayment(jsonStr);
                    amount_pay_button.setClickable(false);
                    amount_pay_button.setEnabled(false);
                    amount_pay_button.setFocusable(false);
                    amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.filter_switch));
                } else if (paybotton_type.equalsIgnoreCase("1")) {
                    /*--------card---------*/
                    cartvalidate();
                } else if (paybotton_type.equalsIgnoreCase("9")) {
                    /*--------Wallet Payment---------*/
                    walletgetreferid();
                    amount_pay_button.setClickable(false);
                    amount_pay_button.setEnabled(false);
                    amount_pay_button.setFocusable(false);
                    amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.filter_switch));
                } else if (paybotton_type.equalsIgnoreCase("5")) {
                    /*--------Save card Payment---------*/
                    savecartvalidate();
                } else if (paybotton_type.equalsIgnoreCase("8")) {
                    /*--------GooglePay card Payment---------*/
                     googlePayLauncher.present("GBP", Integer.parseInt(tempamount));
                }
            }
        });
        amount_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        opensavecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_item.setVisibility(View.GONE);
                cash_item.setVisibility(View.GONE);
                wallet_item.setVisibility(View.GONE);
                amount_below_btoon.setVisibility(View.GONE);
                savecard_item.setVisibility(View.VISIBLE);
            }
        });
        add_new_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_item.setVisibility(View.VISIBLE);
                cash_item.setVisibility(View.GONE);
                wallet_item.setVisibility(View.GONE);
                amount_below_btoon.setVisibility(View.VISIBLE);
                savecard_item.setVisibility(View.GONE);
                savecard_checkBox.setVisibility(View.VISIBLE);
                savecard_checkBox.setChecked(true);
                card_number.setText("");
                card_date.setText("");
                card_cvv.setText("");
                card_number.setEnabled(true);
                card_date.setEnabled(true);
            }
        });
        /*---------------------------save_card_get_methodid----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(Save_card_get_details, new IntentFilter("Save_card_details"));

    }

    /*---------------------------Google Pay----------------------------*/
    private void onGooglePayReady(boolean isReady) {
        Log.e("pay_type", "" + pay_type);
        Log.e("pay_type", "" + isReady);
        if (pay_type.equalsIgnoreCase("8")) {
            amount_pay_button.setEnabled(true);
            Log.e("isReady", "" + isReady);
            if (isReady == false) {
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.filter_switch));
                Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Google Pay not available in your device.Try another payment method", Snackbar.LENGTH_LONG).show();
            } else {
                Log.e("pay_type", "" + pay_type);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
            }
        }
    }

    private void onGooglePayResult(@NotNull GooglePayPaymentMethodLauncher.Result result) {
        Log.e("paymentresult", "" + result);
        if (result instanceof GooglePayPaymentMethodLauncher.Result.Completed) {
            // Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
            loadingshow();
            // Payment details successfully captured.
            // Send the paymentMethodId to your server to finalize payment.
            final String paymentMethodId =
                    ((GooglePayPaymentMethodLauncher.Result.Completed) result).getPaymentMethod().id;
            Log.e("paymentMethodId", "" + paymentMethodId);

            Map<String, String> params = new HashMap<String, String>();
            params.put("total", pay_amount);
            params.put("app_id", "0");
            params.put("fname", first_name);
            params.put("lname", last_name);
            params.put("email", user_email);
            params.put("cid", user_id);
            params.put("payment_method_id", paymentMethodId);
            params.put("url", "demo2.fusionepos.co.uk");

            metdpasfullUrl = menuurlpath + "/gppaymentProcess";
            //, "Bearer " + token
            //ApiInterface apiService = ApiClient.getInstance().getClient2().create(ApiInterface.class);
            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            retrofit2.Call<getgpayclientscSecret_model> call = apiService.getgpaylientsecret(metdpasfullUrl, params);
            Log.e("gpaypass", "" + params);
            call.enqueue(new retrofit2.Callback<getgpayclientscSecret_model>() {
                @Override
                public void onResponse(Call<getgpayclientscSecret_model> call, Response<getgpayclientscSecret_model> response) {
                    int statusCode = response.code();
                    Log.e("statusCode", "" + statusCode);
                    if (statusCode == 200) {
                        hideloading();

                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            gpay_client_secret = response.body().getData().getClient_secret();
                            Log.e("gpay_client_secret", "" + gpay_client_secret);

                            //googlePayLauncher.presentForPaymentIntent(gpay_client_secret);
                            //  Googlepay_Activity.this.runOnUiThread(() -> stripe.handleNextActionForPayment(Googlepay_Activity.this, gpay_client_secret));

                            final ConfirmPaymentIntentParams params =
                                    ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                            paymentMethodId,//'{{PAYMENT_METHOD_ID}}'
                                            gpay_client_secret,
                                            null);
                            //  new PaymentMethodOptionsParams.Card(cvc)
                            stripe.confirmPayment(Payment_Settings_Activity.this, params);
                            Log.e("sc_params", "" + params);
                        }
                    } else {
                        hideloading();
                        Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Client secret id not found", Snackbar.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(retrofit2.Call<getgpayclientscSecret_model> call, Throwable t) {
                    hideloading();
                    Log.e("errorcode1", "" + t);
                    Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            });

        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Canceled) {
            // User cancelled the operation
              Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Failed) {
            // Operation failed; inspect `result.getError()` for the exception
              Log.e("paymentfails", "" + ((GooglePayPaymentMethodLauncher.Result.Failed) result).getError());
             Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

        }


    }
    /*---------------------------save_card_get_methodid----------------------------------------------------*/
    public BroadcastReceiver Save_card_get_details = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            savecardmid = intent.getStringExtra("savecardmid");
            savecardnumbers = intent.getStringExtra("savecardnumbers");
            savecardexpmonth = intent.getStringExtra("savecardexpmonth");
            savecardexpyear = intent.getStringExtra("savecardexpyear");
            savecardholdername = intent.getStringExtra("savecardholdername");

            paybotton_type = "5";
            card_item.setVisibility(View.VISIBLE);
            cash_item.setVisibility(View.GONE);
            wallet_item.setVisibility(View.GONE);
            amount_below_btoon.setVisibility(View.VISIBLE);
            savecard_item.setVisibility(View.GONE);
            savecard_checkBox.setVisibility(View.GONE);
            savecard_checkBox.setChecked(false);
            card_number.setEnabled(false);
            card_date.setEnabled(false);
            Log.e("savecardexpyear1", "" + savecardmid);
            Log.e("savecardexpyear2", "" + savecardexpmonth);
            Log.e("savecardexpyear3", "" + savecardexpyear);
            //   savecardexpyear.substring(Math.max(savecardexpyear.length() - 2, 0))
            card_number.setText("0000 0000 0000 " + savecardnumbers);
            card_name.setText(savecardholdername);
            card_date.setText(savecardexpmonth + "/" + savecardexpyear.substring(Math.max(savecardexpyear.length() - 2, 0)));
        }
    };

    /*------------------- View save Crd --------------------*/
    private void viewsavecard(String userid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", userid);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<viewsavecard_details_model> call = apiService.viewsavecard(params);
        Log.e("params", "" + params);
        call.enqueue(new Callback<viewsavecard_details_model>() {
            @Override
            public void onResponse(Call<viewsavecard_details_model> call, Response<viewsavecard_details_model> response) {
                //  response.headers().get("Set-Cookie");
                int statusCode = response.code();
                Log.e("dashbord", "" + statusCode);
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        if (response.body().getCount().equalsIgnoreCase("0")) {
                            opensavecard.setVisibility(View.GONE);
                        } else {
                            opensavecard.setVisibility(View.VISIBLE);
                            viewsavecardAdapter savecardadapter = new viewsavecardAdapter(mContext, response.body().getData());
                            view_save_card.setHasFixedSize(true);
                            view_save_card.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            view_save_card.setItemAnimator(new DefaultItemAnimator());
                            view_save_card.setAdapter(savecardadapter);
                        }
                    } else {
                        opensavecard.setVisibility(View.GONE);
                        // Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    opensavecard.setVisibility(View.GONE);
                    //  Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<viewsavecard_details_model> call, Throwable t) {
                opensavecard.setVisibility(View.GONE);
                // Log.e("dasboarderror", "location type : " + t);
                // Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /*---------------------------Thank you pay----------------------------------------------------*/
    public class ViewthankuDialog {
        public void shownothankuDialog(Activity activity) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.thank_you);
            TextView alertTextView = dialog.findViewById(R.id.text_dialog1);
            TextView alertTextViewtwo = dialog.findViewById(R.id.text_dialog);
            alertTextView.setText("Thank You !");
            alertTextViewtwo.setText("Your Payment was successful.\nNow you can Track your order");
            dialog.show();
        }
    }

    /*---------------------------check internet connection----------------------------------------------------*/
    public class ViewDialog {
        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Payment_Settings_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Payment_Settings_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_chk = new Internet_connection_checking(Payment_Settings_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        }
    }

    /*----------------------------------Cash payment button click to call API----------------------------*/
    private void cashpayment(String s) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        fullUrl = menuurlpath + "/orderprocess";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CheckloginModel> call = apiService.insertOrder(fullUrl, requestBody);
        Log.e("fullUrl_arrayrequest", "" + fullUrl + " " +  s);
        Log.e("arrayrequest", "" + s);
        call.enqueue(new Callback<CheckloginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<CheckloginModel> call, Response<CheckloginModel> response) {
                int statusCode = response.code();
                Log.e("statusCodecashpay", "" + statusCode);
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        Payment_Settings_Activity.ViewthankuDialog alert = new Payment_Settings_Activity.ViewthankuDialog();
                        alert.shownothankuDialog(Payment_Settings_Activity.this);
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                // mTextField.setText("done!");
                                //delete all db
                              /*  SQLDBHelper dbHelper;
                                dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                                dbHelper.deleteAll();*/
                                dialog.cancel();

                                SQLDBHelper dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                                SQLiteDatabase db = dbHelper.getReadableDatabase();

                                try {

                                    db.execSQL("DELETE FROM " + DATABASE_NAME);

                                } catch (SQLiteException e) {
                                    e.printStackTrace();
                                    Log.e("sqliteerror", "" + e);
                                }

                                /*----------------------------------------Start Remove All item cart  ------------------------------------------*/
                                dbHelper.deleteAll();
                                /*-----------------------------------------End Remove All item cart-------------------------------------------------*/

                                // startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
                                Intent intent = new Intent(Payment_Settings_Activity.this, Order_Status_Activity.class);
                                intent.putExtra("orderid", response.body().getData().getOrder_id());
                                intent.putExtra("orderpath", response.body().getData().getPath());
                                intent.putExtra("orderdate", response.body().getData().getOrderdate());
                                intent.putExtra("clientname", response.body().getData().getClientname());
                                intent.putExtra("clientid", response.body().getData().getClientid());
                                intent.putExtra("clientphonenumber", response.body().getData().getClientphonenumber());
                                startActivity(intent);
                            }
                        }.start();
                    } else {
                        amount_pay_button.setClickable(true);
                        amount_pay_button.setEnabled(true);
                        amount_pay_button.setFocusable(true);
                        amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                        Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    amount_pay_button.setClickable(true);
                    amount_pay_button.setEnabled(true);
                    amount_pay_button.setFocusable(true);
                    amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                    Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CheckloginModel> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                amount_pay_button.setClickable(true);
                amount_pay_button.setEnabled(true);
                amount_pay_button.setFocusable(true);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /* -----------------------------------------Card payment method actions------------------------------------------*/
    /*---------------------------get account details----------------------------------------------------*/
    //get api values
    private void getaccountdetails(final String cid, final String logintype) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", cid);
        params.put("typeoflogin", logintype);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<get_account_modal> call = apiService.getaccountdetails(params);
        call.enqueue(new Callback<get_account_modal>() {
            @Override
            public void onResponse(Call<get_account_modal> call, Response<get_account_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        if (response.body().getRESPONSE_CODE().equalsIgnoreCase("201")) {
                            first_name = response.body().getData().getFname();
                            last_name = response.body().getData().getLname();
                        } else if (response.body().getRESPONSE_CODE().equalsIgnoreCase("202")) {
                            first_name = response.body().getData().getSsologin().getFname();
                            last_name = response.body().getData().getSsologin().getLname();
                        } else if (response.body().getRESPONSE_CODE().equalsIgnoreCase("203")) {
                            first_name = response.body().getData().getSsologin().getFname();
                            last_name = response.body().getData().getSsologin().getLname();
                        }
                    } else {
                        Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<get_account_modal> call, Throwable t) {
                Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getpublisekey() {
        bulkeyfullUrl = menuurlpath + "/stripeAppId";
        Log.d("stripeappid",bulkeyfullUrl);
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
                    Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<appkey> call, Throwable t) {
                // loader.dismiss();
                Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /*---------------------card date validation---------------*/
    private TextWatcher mDateEntryWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length() == 2 && before == 0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                    isValid = false;
                    card_date.setText("");
                    card_date.setError("Expire date is invalid.");
                } else {
                    working += "/";
                    card_date.setText(working);
                    card_date.setSelection(working.length());
                }
            } else if (working.length() == 5 && before == 0) {
                String enteredYear = working.substring(3);
                Log.e("enteredYear", "" + enteredYear);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
                Log.e("currentYear", "" + currentYear);
                if (Integer.parseInt(enteredYear) < currentYear) {
                    isValid = false;
                    card_date.setError("Expire date is invalid.");
                }
            }
            if (working.length() == 5) {
                card_cvv.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    /*---------------------------Card validate ----------------------------------------------------*/
    private void cartvalidate() {
        if (TextUtils.isEmpty(card_number.getText().toString())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_number.getWindowToken(), 0);
            card_number.setError("Please fill out this field.");
            // Snackbar.make(this.findViewById(android.R.id.content), "Please enter your mobile number", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(card_name.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_name.getWindowToken(), 0);
            card_name.setError("Please fill out this field.");
        } else if (TextUtils.isEmpty(card_date.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_date.getWindowToken(), 0);
            card_date.setError("Please fill out this field.");
        } else if (TextUtils.isEmpty(card_cvv.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_cvv.getWindowToken(), 0);
            card_cvv.setError("Please fill out this field.");
        } else if (!TextUtils.isEmpty(card_number.getText().toString()) && !TextUtils.isEmpty(card_date.getText()) && !TextUtils.isEmpty(card_cvv.getText())) {
            strcard_number = card_number.getText().toString().replace(" ", "");
            strcard_cvv = card_cvv.getText().toString();
            strcard_date = card_date.getText().toString();
            StringTokenizer st = new StringTokenizer(strcard_date, "/");
            cardmonth = st.nextToken();
            cardyear = st.nextToken();
            cardmont = Integer.parseInt(cardmonth);
            cardyears = Integer.parseInt(cardyear);
            Log.e("card_number", "" + card_number.getText().toString().replace(" ", ""));
            Log.e("cardmonth", "" + cardmonth);
            Log.e("cardyear", "" + cardyear);
            pay();
        }
    }

    private void pay() {
       /* CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        PaymentMethodCreateParams params;
        params = cardInputWidget.getPaymentMethodCreateParams();*/
        CardInputWidget cardInputWidget = new CardInputWidget(this);
        cardInputWidget.setCardNumber(strcard_number);
        cardInputWidget.setCvcCode(strcard_cvv);
        cardInputWidget.setExpiryDate(Integer.parseInt(cardmonth), Integer.parseInt(cardyear));
        cardInputWidget.setPostalCodeRequired(false);
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        Log.e("cardparams", "" + params);
        if (params == null) {
            card_number.setText("");
            card_date.setText("");
            card_cvv.setText("");
            card_number.setError("Your card number is invalide");
            //Toast.makeText(getApplicationContext(), "error 1", Toast.LENGTH_LONG).show();
            return;
        } else {
            amount_pay_button.setClickable(false);
            amount_pay_button.setEnabled(false);
            amount_pay_button.setFocusable(false);
            amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.filter_switch));
            // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
            PaymentConfiguration.init(getApplicationContext(), apikey);//new version add this line version 17.0.0
            // stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());
            stripe = new Stripe(
                    getApplicationContext(),
                    Objects.requireNonNull(apikey)
            );
            stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                @Override
                public void onSuccess(@NonNull PaymentMethod result) {
                    // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                    pay(result.id);
                    Log.e("createmethodid", "" + result.id);
                    /*-----------------------Add new save card---------------*/
                    if (savecard_checkBox.isChecked()) {
                        addnewsavecard(user_id, strcard_number, cardmonth, cardyear);
                    }

                }

                @Override
                public void onError(@NonNull Exception e) {
                    amount_pay_button.setClickable(true);
                    amount_pay_button.setEnabled(true);
                    amount_pay_button.setFocusable(true);
                    amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                    showPopup("Transaction failed if any take your money will be refunded within 3 to 4 hrs.\\nT&amp;C apply");
                }
            });
        }
    }

    private void pay(@Nullable String paymentMethodId) {
        loadingshow();
        // ...continued in the next step

        Log.e("paymentMethodId", "" + paymentMethodId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("payment_method_id", paymentMethodId);
        params.put("total", pay_amount);
        params.put("fname", first_name);
        params.put("lname", last_name);
        params.put("email", user_email);
        params.put("app_id", "0");
        metdpasfullUrl = menuurlpath + "/stripepaymentProcess";


//, "Bearer " + token
        //ApiInterface apiService = ApiClient.getInstance().getClient2().create(ApiInterface.class);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        retrofit2.Call<getclientSecret_model> call = apiService.getclientSecret(metdpasfullUrl, params, "Bearer" + token);
        Log.e("paramsintentpass", "" + params + "" + metdpasfullUrl);
        call.enqueue(new retrofit2.Callback<getclientSecret_model>() {
            @Override
            public void onResponse(Call<getclientSecret_model> call, Response<getclientSecret_model> response) {
                int statusCode = response.code();
                Log.e("statusCode", "" + statusCode);
                if (statusCode == 200) {
                    hideloading();
                    Log.e("ststau", "" + response.body().getStatus());
                    Log.e("getD_secure", "" + response.body().getSecure().getD_secure());
                    String s = response.body().getSecure().getD_secure();
                    StringTokenizer st = new StringTokenizer(s, ":");
                    String community = st.nextToken();
                    String helpDesk = st.nextToken();
                    String secret = st.nextToken().replace("\"", "").replace("}", "").replace(" ", "");

                    Log.e("community", "" + secret);

                    Payment_Settings_Activity.this.runOnUiThread(() -> stripe.handleNextActionForPayment(Payment_Settings_Activity.this, secret));

                    Log.e("responseData", "" + response);
                } else {
                    hideloading();
                    // loader.dismiss();
                    // Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    amount_pay_button.setClickable(true);
                    amount_pay_button.setEnabled(true);
                    amount_pay_button.setFocusable(true);
                    amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                    showPopup("Transaction failed if any take your money will be refunded within 3 to 4 hrs.\\nT&amp;C apply");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<getclientSecret_model> call, Throwable t) {
                hideloading();
                Log.e("errorcode1", "" + t);
                // loader.dismiss();
                amount_pay_button.setClickable(true);
                amount_pay_button.setEnabled(true);
                amount_pay_button.setFocusable(true);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                showPopup("Transaction failed if any take your money will be refunded within 3 to 4 hrs.\\nT&amp;C apply");
                // Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "" + requestCode);
        Log.e("retysdata", "" + data);
        Log.e("resultCode", "" + resultCode);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new Payment_Settings_Activity.PaymentResultCallback(Payment_Settings_Activity.this));
        /// Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
    }

    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<Payment_Settings_Activity> activityRef;

        PaymentResultCallback(@NonNull Payment_Settings_Activity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final Payment_Settings_Activity activity = activityRef.get();
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
                //Toast.makeText(getApplicationContext(), "Succeeded", Toast.LENGTH_LONG).show();
               /* activity.runOnUiThread(() -> {
                    // Log.e("paymentintentidshow1", "" + paymentIntent.getId());
                    // activity.paymentcompleted(paymentIntent.getId());
                    // Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    // activity.displayAlert("Payment completed", gson.toJson(paymentIntent), true);
                    showPopup("We are unable to authenticate your payment method. Please choose a different payment method and try again.");
                });*/
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                Log.e("paymentIntenterror1", "" + paymentIntent.getLastPaymentError().getMessage());

                Log.e("paymentIntenterror2", "" + paymentIntent.getLastErrorMessage());

                Log.e("paymentIntenterror3", "" + paymentIntent);


                showPopup("Your card's security code is incorrect.");
            } else if (status == PaymentIntent.Status.RequiresConfirmation) {
                // After handling a required action on the client, the status of the PaymentIntent is
                // requires_confirmation. You must send the PaymentIntent ID to your backend
                // and confirm it to finalize the payment. This step enables your integration to
                // synchronously fulfill the order on your backend and return the fulfillment result
                // to your client.
                Log.e("paymentintentidshow2", "" + paymentIntent.getId());
                activity.paymentcompleted(paymentIntent.getId(), "false");
                // Toast.makeText(getApplicationContext(), "RequiresConfirmation", Toast.LENGTH_LONG).show();
            } else if (status == PaymentIntent.Status.RequiresAction) {
                // After handling a required action on the client, the status of the PaymentIntent is
                // requires_confirmation. You must send the PaymentIntent ID to your backend
                // and confirm it to finalize the payment. This step enables your integration to
                // synchronously fulfill the order on your backend and return the fulfillment result
                // to your client.
                showPopup("We are unable to authenticate your payment method. Please choose a different payment method and try again.");
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final Payment_Settings_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.e("errormsg", "" + e.toString());
            // Payment request failed – allow retrying using the same payment method
            activity.runOnUiThread(() -> {
                Log.e("errormsg", "" + e.toString());
                //activity.displayAlert("Error", e.toString(), false);
                showPopup(e.toString());
            });
        }
    }

    private void paymentcompleted(String intentid, String typestatus) {
        loadingshow();
        Map<String, String> paramspay = new HashMap<String, String>();
//typestatus is false to direct stripe payment type true to save card payment process
        if (typestatus.equalsIgnoreCase("false")) {
            paramspay.put("payment_intent_id", intentid);
        } else {
            paramspay.put("paymentIntent", intentid);
        }
        paramspay.put("total", pay_amount);
        paramspay.put("fname", first_name);
        paramspay.put("lname", last_name);
        paramspay.put("email", user_email);
        paramspay.put("app_id", "0");
        intepasfullUrl = menuurlpath + "/stripepaymentProcess";
        // ApiInterface apiService = ApiClient.getInstance().getClient2().create(ApiInterface.class);
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
                        //   Payment_Settings_Activity.this.displayAlert("Payment completed", "Payment completed", true);
                        referid = response.body().getRefno().getId();
                        refergroup = response.body().getRefno().getTransfer_group();
                        referstatus = response.body().getRefno().getStatus();
                        refersecure = response.body().getRefno().getD_secure();
                        refertranfer = response.body().getRefno().getTransfer();
                        Log.e("referid", "" + referid);
                        Log.e("refergroup", "" + refergroup);
                        Log.e("referstatus", "" + referstatus);
                        Log.e("refersecure", "" + refersecure);
                        Log.e("refertranfer", "" + refertranfer);
                        /*---------------------------card payment Payment_Settings_Activity details-----------------------------------*/
                        dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                        ArrayList<Cartitem> allContacts = dbHelper.listContacts();
                        JSONArray jsonArray1 = new JSONArray();
                        for (int i = 0; i < allContacts.size(); i++) {
                            Log.e("addonlenth", "" + allContacts.get(i).getAddonnameid().length());
                            JSONObject item1 = new JSONObject();
                            String[] itemAddonNameIds = allContacts.get(i).getAddonnameid().split(",");
                            ArrayList subItemArray = new ArrayList<String>();
                            String[] itemAddonextaid = allContacts.get(i).getAddonextraid().split(",");
                            if (allContacts.get(i).getAddonnameid().length() != 0) {
                                for (int x = 0; x < itemAddonNameIds.length; x++) {
                                    JSONObject subInside = new JSONObject();
                                    String itemAddonextaidData = "";
                                    if (x < itemAddonextaid.length) {
                                        itemAddonextaidData = itemAddonextaid[x];
                                    } else {
                                        itemAddonextaidData = "";
                                    }
                                    try {
                                        subInside.put("id", itemAddonNameIds[x]);
                                        subInside.put("qty", allContacts.get(i).getQty());
                                        subInside.put("type", "addon");
                                        subInside.put("ext", itemAddonextaidData);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    subItemArray.add(subInside);
                                }
                            }
                            Log.e("itemaddonids", "" + subItemArray.toString());
                            try {
                                item1.put("id", allContacts.get(i).getItemid());
                                item1.put("qty", allContacts.get(i).getQty());
                                item1.put("free", "0");
                                item1.put("offer", "");
                                item1.put("sub", new JSONArray(subItemArray));
                                //item1.put("special_instruction",allContacts.get(i).getSpecialinstruction());

                                if(allContacts.get(i).getSpecialinstruction() != null){
                                    item1.put("special_instruction",allContacts.get(i).getSpecialinstruction());
                                }else{
                                    item1.put("special_instruction","");
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray1.put(item1);
                        }
                        Log.e("itemid", "" + jsonArray1);
                        JSONObject promo_discount = new JSONObject();
                        try {
                            promo_discount.put("discount", coupon_discount);
                            promo_discount.put("desc", coupon_Discription);
                            promo_discount.put("type", coupon_type);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        JSONObject refernumber = new JSONObject();
                        try {
                            refernumber.put("id", referid);
                            refernumber.put("transfer", refertranfer);
                            refernumber.put("transfer_group", refergroup);
                            refernumber.put("status", referstatus);
                            refernumber.put("3d_secure", refersecure);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        JSONObject studentsObj = new JSONObject();
                        try {
                            studentsObj.put("refno", refernumber);//ok
                            studentsObj.put("inst", cooking_insttruction);//ok
                            studentsObj.put("bank", serviceamt);//ok
                            studentsObj.put("bag", bagamt);//ok
                            studentsObj.put("activetab", sharedpreferences.getString("orderactivetag", null));//ok
                            if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {
                                studentsObj.put("time", "");//ok
                                studentsObj.put("date", "");//ok
                                studentsObj.put("date_string", "");//ok

                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {
                                studentsObj.put("time", sharedpreferences.getString("ordertodattime", null));//ok
                                studentsObj.put("date", "");//ok
                                studentsObj.put("date_string",sharedpreferences.getString("todaytimestring", null));

                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
                                studentsObj.put("time", sharedpreferences.getString("orderlatertime", null));//ok
                                studentsObj.put("date", sharedpreferences.getString("orderlaterdate", null));//ok
                                studentsObj.put("date_string",sharedpreferences.getString("latertimestring", null));

                            }
                            studentsObj.put("preorder_time", "0");//ok
                            studentsObj.put("items", jsonArray1);//ok
                            studentsObj.put("discount", "0");//ok
                            studentsObj.put("coupon", "0");//ok
                            studentsObj.put("delivery", deliveryamt);//ok
                            studentsObj.put("cid", user_id);//ok
                            studentsObj.put("loginmail", user_email);//ok
                            studentsObj.put("typeoflogin", login_type_id);//ok
                            studentsObj.put("otype", order_types);//ok
                            studentsObj.put("deliveryaddress", addressids);
                            studentsObj.put("ptype", pay_type);//ok
                            studentsObj.put("subtotal", subtotal);//ok
                            studentsObj.put("totalamount", amt_total);//ok
                            studentsObj.put("zero_contact", "0");
                            studentsObj.put("app_id", "0");
                            studentsObj.put("promo_discount", promo_discount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonStr = studentsObj.toString();
                        Log.e("card_Mark_JSON", "" + jsonStr.toString());
                        cardpayment(jsonStr);
                    } else {
                        hideloading();
                        amount_pay_button.setClickable(true);
                        amount_pay_button.setEnabled(true);
                        amount_pay_button.setFocusable(true);
                        amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                        showPopup(response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<completpay_model> call, Throwable t) {
                hideloading();
                amount_pay_button.setClickable(true);
                amount_pay_button.setEnabled(true);
                amount_pay_button.setFocusable(true);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                showPopup("Transaction failed if any take your money will be refunded within 3 to 4 hrs.\\nT&amp;C apply");
                Log.e("errorcode1", "" + t);
                // loader.dismiss();
                //  Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /*----------------------------------Card payment button click to call API----------------------------*/
    private void cardpayment(String s) {
        loadingshow();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        fullUrl = menuurlpath + "/orderprocess";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CheckloginModel> call = apiService.insertOrder(fullUrl, requestBody);
        Log.e("fullUrl", "" + fullUrl);
        Log.e("arrayrequest", "" + s);
        call.enqueue(new Callback<CheckloginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<CheckloginModel> call, Response<CheckloginModel> response) {
                int statusCode = response.code();
                Log.e("statusCodecashpay", "" + statusCode);
                Log.e("card_payment---->", new Gson().toJson(response.body()));
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        Payment_Settings_Activity.ViewthankuDialog alert = new Payment_Settings_Activity.ViewthankuDialog();
                        alert.shownothankuDialog(Payment_Settings_Activity.this);
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                //dbHelper.deleteAll();
                            /*    SQLDBHelper dbHelper;
                                dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                                dbHelper.deleteAll();*/




                                 /* File data = Environment.getDataDirectory();
                                String currentDBPath = "/data/com.fusionkitchen/databases/" + DATABASE_NAME;
                                File currentDB = new File(data, currentDBPath);
                                boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);*/

                                SQLDBHelper dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                                SQLiteDatabase db = dbHelper.getReadableDatabase();

                                try {

                                    db.execSQL("DELETE FROM " + DATABASE_NAME);


                                } catch (SQLiteException e) {
                                    e.printStackTrace();
                                    Log.e("sqliteerror", "" + e);
                                }


                                //  dbHelper.insertItem("", "", "", "", "", "", "0", "", "", "", "");
                                dialog.cancel();
                                /*----------------------------------------Start Remove All item cart  ------------------------------------------*/
                                dbHelper.deleteAll();
                                /*-----------------------------------------End Remove All item cart-------------------------------------------------*/

                                Intent intent = new Intent(Payment_Settings_Activity.this, Order_Status_Activity.class);
                                intent.putExtra("orderid", response.body().getData().getOrder_id());
                                intent.putExtra("orderpath", response.body().getData().getPath());
                                intent.putExtra("orderdate", response.body().getData().getOrderdate());
                                intent.putExtra("clientname", response.body().getData().getClientname());
                                intent.putExtra("clientid", response.body().getData().getClientid());
                                intent.putExtra("clientphonenumber", response.body().getData().getClientphonenumber());
                                startActivity(intent);
                                // startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
                            }
                        }.start();
                    } else {
                        hideloading();
                        Log.d("error_else--","something");
                        Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Log.d("error_else--else","something");
                    Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CheckloginModel> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                amount_pay_button.setClickable(true);
                amount_pay_button.setEnabled(true);
                amount_pay_button.setFocusable(true);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                showPopup("Transaction failed if any take your money will be refunded within 3 to 4 hrs.\\nT&amp;C apply");
                Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*------------------------Wallet get refer number---------------------*/
    private void walletgetreferid() {
        loadingshow();
        Map<String, String> paramspay = new HashMap<String, String>();
        paramspay.put("total", pay_amount);
        paramspay.put("app_id", "0");
        paramspay.put("fname", first_name);
        paramspay.put("lname", last_name);
        paramspay.put("email", user_email);
        paramspay.put("pay_type", "9");
        intepasfullUrl = menuurlpath + "/stripepaymentProcess";
        // ApiInterface apiService = ApiClient.getInstance().getClient2().create(ApiInterface.class);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        retrofit2.Call<wallet_getrefer_details> call = apiService.walletgetrefer(intepasfullUrl, paramspay, "Bearer" + token);

        Log.e("walletgetrefer", "" + paramspay);
        call.enqueue(new retrofit2.Callback<wallet_getrefer_details>() {
            @Override
            public void onResponse(retrofit2.Call<wallet_getrefer_details> call, retrofit2.Response<wallet_getrefer_details> response) {
                int statusCode = response.code();
                Log.e("completedstatusCode", "" + statusCode);
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        //   Payment_Settings_Activity.this.displayAlert("Payment completed", "Payment completed", true);


                        refertranfer = response.body().getRefno().getTransfer();
                        refergroup = response.body().getRefno().getTransfer_group();
                        refersecure = response.body().getRefno().getD_secure();


                        Log.e("refergroup", "" + refergroup);
                        Log.e("refersecure", "" + refersecure);
                        Log.e("refertranfer", "" + refertranfer);



                        /*---------------------------wallet payment Payment_Settings_Activity details-----------------------------------*/

                        dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                        ArrayList<Cartitem> allContacts = dbHelper.listContacts();

                        JSONArray jsonArray1 = new JSONArray();

                        for (int i = 0; i < allContacts.size(); i++) {

                            Log.e("addonlenth", "" + allContacts.get(i).getAddonnameid().length());


                            JSONObject item1 = new JSONObject();
                            String[] itemAddonNameIds = allContacts.get(i).getAddonnameid().split(",");
                            ArrayList subItemArray = new ArrayList<String>();

                            String[] itemAddonextaid = allContacts.get(i).getAddonextraid().split(",");

                            if (allContacts.get(i).getAddonnameid().length() != 0) {
                                for (int x = 0; x < itemAddonNameIds.length; x++) {
                                    JSONObject subInside = new JSONObject();
                                    String itemAddonextaidData = "";
                                    if (x < itemAddonextaid.length) {
                                        itemAddonextaidData = itemAddonextaid[x];
                                    } else {
                                        itemAddonextaidData = "";
                                    }
                                    try {
                                        subInside.put("id", itemAddonNameIds[x]);
                                        subInside.put("qty", allContacts.get(i).getQty());
                                        subInside.put("type", "addon");
                                        subInside.put("ext", itemAddonextaidData);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    subItemArray.add(subInside);
                                }
                            }
                            Log.e("itemaddonids", "" + subItemArray.toString());


                            try {
                                item1.put("id", allContacts.get(i).getItemid());
                                item1.put("qty", allContacts.get(i).getQty());
                                item1.put("free", "0");
                                item1.put("offer", "");
                                item1.put("sub", new JSONArray(subItemArray));
                                //item1.put("special_instruction",allContacts.get(i).getSpecialinstruction());
                                if(allContacts.get(i).getSpecialinstruction() != null){
                                    item1.put("special_instruction",allContacts.get(i).getSpecialinstruction());
                                }else{
                                    item1.put("special_instruction","");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray1.put(item1);


                        }


                        Log.e("itemid", "" + jsonArray1);


                        JSONObject promo_discount = new JSONObject();
                        try {
                            promo_discount.put("discount", coupon_discount);
                            promo_discount.put("desc", coupon_Discription);
                            promo_discount.put("type", coupon_type);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        JSONObject refernumber = new JSONObject();
                        try {
                            refernumber.put("id", "");
                            refernumber.put("transfer", refertranfer);
                            refernumber.put("transfer_group", refergroup);
                            refernumber.put("status", "");
                            refernumber.put("3d_secure", refersecure);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        JSONObject studentsObj = new JSONObject();
                        try {


                            studentsObj.put("refno", refernumber);//ok
                            studentsObj.put("inst", cooking_insttruction);//ok
                            studentsObj.put("bank", serviceamt);//ok
                            studentsObj.put("bag", bagamt);//ok
                            studentsObj.put("activetab", sharedpreferences.getString("orderactivetag", null));//ok
                            if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {
                                studentsObj.put("time", "");//ok
                                studentsObj.put("date", "");//ok
                                studentsObj.put("date_string", "");//ok

                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {
                                studentsObj.put("time", sharedpreferences.getString("ordertodattime", null));//ok
                                studentsObj.put("date", "");//ok
                                studentsObj.put("date_string",sharedpreferences.getString("todaytimestring", null));


                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
                                studentsObj.put("time", sharedpreferences.getString("orderlatertime", null));//ok
                                studentsObj.put("date", sharedpreferences.getString("orderlaterdate", null));//ok
                                studentsObj.put("date_string",sharedpreferences.getString("latertimestring", null));
                            }

                            studentsObj.put("preorder_time", "0");//ok
                            studentsObj.put("items", jsonArray1);//ok
                            studentsObj.put("discount", "0");//ok
                            studentsObj.put("coupon", "0");//ok
                            studentsObj.put("delivery", deliveryamt);//ok
                            studentsObj.put("cid", user_id);//ok
                            studentsObj.put("loginmail", user_email);//ok
                            studentsObj.put("typeoflogin", login_type_id);//ok
                            studentsObj.put("otype", order_types);//ok
                            studentsObj.put("deliveryaddress", addressids);
                            studentsObj.put("ptype", pay_type);//ok
                            studentsObj.put("subtotal", subtotal);//ok
                            studentsObj.put("totalamount", amt_total);//ok
                            studentsObj.put("zero_contact", "0");
                            studentsObj.put("app_id", "0");
                            studentsObj.put("promo_discount", promo_discount);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonStr = studentsObj.toString();
                        Log.e("card_Mark_JSON", "" + jsonStr.toString());

                        wallet_checkout(jsonStr);

                    } else {
                        hideloading();
                        amount_pay_button.setClickable(true);
                        amount_pay_button.setEnabled(true);
                        amount_pay_button.setFocusable(true);
                        amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                        Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<wallet_getrefer_details> call, Throwable t) {
                hideloading();
                amount_pay_button.setClickable(true);
                amount_pay_button.setEnabled(true);
                amount_pay_button.setFocusable(true);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                Log.e("errorcode1", "" + t);
                // loader.dismiss();
                //  Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /*----------------------------------Wallet payment button click to call API----------------------------*/
    private void wallet_checkout(String orderval) {
        loadingshow();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), orderval);
        fullUrl = menuurlpath + "/orderprocess";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CheckloginModel> call = apiService.insertOrder(fullUrl, requestBody);
        Log.e("fullUrl", "" + fullUrl);
        Log.e("arrayrequest", "" + orderval);
        call.enqueue(new Callback<CheckloginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<CheckloginModel> call, Response<CheckloginModel> response) {
                int statusCode = response.code();
                Log.e("statusCodecashpay", "" + statusCode);
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        Payment_Settings_Activity.ViewthankuDialog alert = new Payment_Settings_Activity.ViewthankuDialog();
                        alert.shownothankuDialog(Payment_Settings_Activity.this);
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                // mTextField.setText("done!");
                                //delete all db
                               /* SQLDBHelper dbHelper;
                                dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                                dbHelper.deleteAll();*/


                                SQLDBHelper dbHelper = new SQLDBHelper(Payment_Settings_Activity.this);
                                SQLiteDatabase db = dbHelper.getReadableDatabase();

                                try {

                                    db.execSQL("DELETE FROM " + DATABASE_NAME);

                                } catch (SQLiteException e) {
                                    e.printStackTrace();
                                    Log.e("sqliteerror", "" + e);
                                }


                                dialog.cancel();
                                /*----------------------------------------Start Remove All item cart  ------------------------------------------*/
                                dbHelper.deleteAll();
                                /*-----------------------------------------End Remove All item cart-------------------------------------------------*/
                                Intent intent = new Intent(Payment_Settings_Activity.this, Order_Status_Activity.class);
                                intent.putExtra("orderid", response.body().getData().getOrder_id());
                                intent.putExtra("orderpath", response.body().getData().getPath());
                                intent.putExtra("orderdate", response.body().getData().getOrderdate());
                                intent.putExtra("clientname", response.body().getData().getClientname());
                                intent.putExtra("clientid", response.body().getData().getClientid());
                                intent.putExtra("clientphonenumber", response.body().getData().getClientphonenumber());
                                startActivity(intent);
                                // startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
                            }
                        }.start();
                    } else {
                        hideloading();
                        amount_pay_button.setClickable(true);
                        amount_pay_button.setEnabled(true);
                        amount_pay_button.setFocusable(true);
                        amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));

                        Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    amount_pay_button.setClickable(true);
                    amount_pay_button.setEnabled(true);
                    amount_pay_button.setFocusable(true);
                    amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));

                    Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CheckloginModel> call, Throwable t) {
                hideloading();
                Log.e("walletcheouterror", "" + t.toString());
                amount_pay_button.setClickable(true);
                amount_pay_button.setEnabled(true);
                amount_pay_button.setFocusable(true);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Something not right Please try again", Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void displayAlert(@NonNull String title, @NonNull String message, boolean restartDemo) {
        // ...omitted for brevity
        hideloading();
        //  Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
        Log.e("paytitle", "" + title);
        Log.e("paymessage", "" + message);
        Log.e("payrestartDemo", "" + restartDemo);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);

        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        card_number.setText("");
                        card_date.setText("");
                        card_cvv.setText("");
                        amount_pay_button.setClickable(true);
                        amount_pay_button.setEnabled(true);
                        amount_pay_button.setFocusable(true);
                        amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }


    /*------------------------------coupon applyed show popup--------------------------*/
    public void showPopup(String msging) {
        View popupView = LayoutInflater.from(Payment_Settings_Activity.this).inflate(R.layout.card_invalid_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        TextView item_names = popupView.findViewById(R.id.item_names);
        item_names.setText(msging);

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                card_number.setText("");
                card_date.setText("");
                card_cvv.setText("");
                amount_pay_button.setClickable(true);
                amount_pay_button.setEnabled(true);
                amount_pay_button.setFocusable(true);
                amount_pay_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.welcome_button_color));

                popupWindow.dismiss();


            }
        }.start();


        //   popupWindow.dismiss();


        popupWindow.showAsDropDown(popupView, 0, 0);
    }


    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {


            return new Payment_Settings_Activity.AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    /*-------------------card number validation----------------*/
    private String formatTextVisaMasterCard(CharSequence text) {
        StringBuilder formatted = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (Character.isDigit(text.charAt(i))) {
                if (count % 4 == 0 && count > 0)
                    formatted.append(" ");
                formatted.append(text.charAt(i));
                ++count;
            }
        }
        return formatted.toString();
    }



    /*------------------------------Save card Payment---------------------------*/

    private void savecartvalidate() {
        if (TextUtils.isEmpty(card_number.getText().toString())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_number.getWindowToken(), 0);
            card_number.setError("Please fill out this field.");
            // Snackbar.make(this.findViewById(android.R.id.content), "Please enter your mobile number", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(card_name.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_name.getWindowToken(), 0);
            card_name.setError("Please fill out this field.");
        } else if (TextUtils.isEmpty(card_date.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_date.getWindowToken(), 0);
            card_date.setError("Please fill out this field.");
        } else if (TextUtils.isEmpty(card_cvv.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_cvv.getWindowToken(), 0);
            card_cvv.setError("Please fill out this field.");
        } else if (!TextUtils.isEmpty(card_number.getText().toString()) && !TextUtils.isEmpty(card_date.getText()) && !TextUtils.isEmpty(card_cvv.getText())) {
            sc_card_cvv = card_cvv.getText().toString();

            PaymentConfiguration.init(getApplicationContext(), apikey);//new version add this line version 17.0.0
            // stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());
            stripe = new Stripe(
                    getApplicationContext(),
                    Objects.requireNonNull(apikey)
            );
            // stripe.createCvcUpdateToken(sc_card_cvv, new CvcUpdateResultCallback());

            loadingshow();
            Map<String, String> params = new HashMap<String, String>();
            params.put("total", pay_amount);
            params.put("fname", first_name);
            params.put("lname", last_name);
            params.put("email", user_email);
            params.put("app_id", "0");
            params.put("svuid", user_id);
            params.put("pmid", "1");
            params.put("token", "1");

            metdpasfullUrl = menuurlpath + "/svcardpayment";
            //, "Bearer " + token
            //ApiInterface apiService = ApiClient.getInstance().getClient2().create(ApiInterface.class);
            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            retrofit2.Call<getclientscSecret_model> call = apiService.getscclientsecret(metdpasfullUrl, params);
            Log.e("paramsintentpass", "" + params);
            call.enqueue(new retrofit2.Callback<getclientscSecret_model>() {
                @Override
                public void onResponse(Call<getclientscSecret_model> call, Response<getclientscSecret_model> response) {
                    int statusCode = response.code();
                    Log.e("statusCode", "" + statusCode);
                    if (statusCode == 200) {
                        hideloading();
                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            sc_client_secret = response.body().getData().getClient_secret();
                            Log.e("sc_client_secret", "" + sc_client_secret);
                            PaymentConfiguration.init(getApplicationContext(), apikey);//new version add this line version 17.0.0
                            // stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());
                            stripe = new Stripe(
                                    getApplicationContext(),
                                    Objects.requireNonNull(apikey)
                            );
                            Log.e("sc_apikey", "" + apikey);
                            confirmPaymentIntent(savecardmid, sc_client_secret, sc_card_cvv);
                        }
                    } else {
                        hideloading();
                        Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), "Client secret id not found", Snackbar.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(retrofit2.Call<getclientscSecret_model> call, Throwable t) {
                    hideloading();
                    Log.e("errorcode1", "" + t);
                    Snackbar.make(Payment_Settings_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            });


        }
    }

    private void confirmPaymentIntent(String scmid, String sc_clientSecret, String cvc) {
        final ConfirmPaymentIntentParams params =
                ConfirmPaymentIntentParams.createWithPaymentMethodId(
                        scmid,//'{{PAYMENT_METHOD_ID}}'
                        sc_clientSecret,
                        null,
                        new PaymentMethodOptionsParams.Card(cvc, null));
        //  new PaymentMethodOptionsParams.Card(cvc)
        stripe.confirmPayment(Payment_Settings_Activity.this, params);
        Log.e("sc_params", "" + params);
    }

    /*-----------------------Add new save card---------------*/
    private void addnewsavecard(String str_userid, String str_cardnum, String str_camonth, String str_cardyear) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", str_userid);
        params.put("card_number", str_cardnum);
        params.put("exp_month", str_camonth);
        params.put("exp_year", str_cardyear);
        params.put("name", card_name.getText().toString());

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<addnewsavecard_model> call = apiService.addnewsavecard(params);
        Log.d("paramsadd", "ok" + params);
        call.enqueue(new Callback<addnewsavecard_model>() {
            @Override
            public void onResponse(Call<addnewsavecard_model> call, Response<addnewsavecard_model> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {
//save card
                }
            }

            @Override
            public void onFailure(Call<addnewsavecard_model> call, Throwable t) {

            }
        });

    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {
        imgdialog = new Dialog(Payment_Settings_Activity.this);
        imgdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imgdialog.setCancelable(false);
        imgdialog.setContentView(R.layout.custom_loading_layout);

        ImageView gifImageView = imgdialog.findViewById(R.id.custom_loading_imageView);


        Glide.with(Payment_Settings_Activity.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);
        imgdialog.show();
    }

    public void hideloading() {
        imgdialog.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView("Payment Setting Activity");
    }
}


