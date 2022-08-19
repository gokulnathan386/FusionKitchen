package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.orderstatus.orderstatus_model;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.CartitemlistAdapter;
import com.fusionkitchen.adapter.GetcartaddressListAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.address.addAddress_mode;
import com.fusionkitchen.model.address.getaddAddress_mode;
import com.fusionkitchen.model.address.getaddressforpostcode_modal;
import com.fusionkitchen.model.cart.Cartitem;
import com.fusionkitchen.model.cart.subcategory_printer_model;
import com.fusionkitchen.model.checkout.chechoutvalidate_model;
import com.fusionkitchen.model.menu_model.collDelivery_model;
import com.fusionkitchen.model.myaccount.get_account_modal;
import com.fusionkitchen.model.myaccount.update_account_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class Add_to_Cart extends AppCompatActivity {

    private Context mContext = Add_to_Cart.this;
    private Dialog dialog;
    private long mLastClickTime = 0;

    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;

    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;

    /*---------------------------XML ID Call----------------------------------------------------*/
    EditText cooking_insttruction;
    String cardoffertitle, cardofferdesc, ItemId, ItemupdateId, Itemupdateqty, Itemupdateamt, Itemsubtotal, menuurlpaths;
    CardView edit_address_popup, address_item_view, delivery_time_popup;
    TextView offer_copy, card_offer_titels, card_offer_dess, service_txt, service_total, change_address, address_titels, cus_address;
    AppCompatButton update_time;
    ImageView signin;
    TimePicker simpleTimePicker;
    ImageView add_address;
    TextView Sub_total, card_total;
    Double subamt, serviceamt, cartamt;
    String totalcardamt;
    ImageView address_add_popup_close;
    RelativeLayout no_signin, process_item, no_address, address_view, order_type_layout, add_new_address;


    LinearLayout extr_amt_rel_layout;

    Animation animationdown, animationup;
    boolean collection_btn = false;
    boolean delivery_btn = false;
    String authKey, fullUrl, fullUrladdon;
    TextView apply_coupon_edit;
    EditText editcoubon;

    EditText edit_postcode, edit_doorno, edit_town, edit_street, cusomer_mobile;
    String first_name, last_name;
    AppCompatButton edit_home_button, edit_office_button, edit_other_button, edit_save_address, add_save_new_address;


    boolean extra_amt_btn = true;
    RelativeLayout collection_order, delivery_order;
    ImageView checked_colloection, checked_delivery;


    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    SQLDBHelper dbHelper;
    SimpleCursorAdapter cursorAdapter;


    ArrayList arrayList;
    RecyclerView recyclerView;

    RecyclerView contactView, recyclerview_address;
    AppCompatButton add_more_button, processto_pay_button, delivery_add_more_button, delivery_pay_button;
    RelativeLayout mobilenumber;
    TextView number_add;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    String menuurlpath, zero_contact, subcatfullurl, subcategory_printers;
    CheckBox zero_checkBox;
    String but_order_type;
    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;

    /*--------------Order Type Spinner------------------*/

    /*--------------Login details get SharedPreferences------------------*/

    String user_id, login_type_id, address_type, str_postcode_seperate, str_postcode_seperate_str, addresstitle, addressviewopen, addressname, addressids, customerpostcode;
    String addressid, addresscid, addressno, addressaddress1, addressaddress2, addresspcode, addressgtype;


    /*---------------------------Session Manager Class----------------------------------------------------*/
    // Session Manager Class
    // SessionManager session;

    String post_code_new, nospace, cooking_insttructionback, nospacecheckpost;
    int keyDel;


    String strItemName, straddonid, strcategoryname, strsubcategoryname, strmenuurlpath;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_to_card);




        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);


        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Add_to_Cart.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(Add_to_Cart.this);
        }

        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        login_type_id = (slogin.getString("type_of_login", null));
        Log.e("ideuser_ids", "" + user_id);

        /*---------------------------Session Manager Class----------------------------------------------------*/
        /*session = new SessionManager(getApplicationContext());
        session.checkLogin();
*/



// sharedpreferences.getString("ordermodetype", null); //0 delivery 1 collection
        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
/*        Intent intent = getIntent();
        pay_type = intent.getStringExtra("pay_type");*/


        /*---------------------------Get Payment type using SharedPreferences ----------------------------------------------------*/
       /* SharedPreferences paymenttypesharedpreferences = getSharedPreferences(PaytypePREFERENCES, MODE_PRIVATE);
        pay_type = (paymenttypesharedpreferences.getString("pay_type", null));*/

        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);

        menuurlpath = sharedpreferences.getString("menuurlpath", null);
        Log.e("addtocarturl", "" + menuurlpath);

        // collDelivery(menuurlpath);

        but_order_type = sharedpreferences.getString("ordermodetype", null);

/*

        CheckLogin();
        subcategory_printer();
*/

        Log.e("but_order_type", "" + but_order_type);

        /*---------------------------XML ID Call----------------------------------------------------*/
        cooking_insttruction = findViewById(R.id.cooking_insttruction);
        address_item_view = findViewById(R.id.address_item_view);
        edit_address_popup = findViewById(R.id.edit_address_popup);
        delivery_time_popup = findViewById(R.id.delivery_time_popup);


        // offer_copy = findViewById(R.id.offer_copy);


        // card_offer_titels = findViewById(R.id.card_offer_titels);
        //card_offer_dess = findViewById(R.id.card_offer_dess);
        processto_pay_button = findViewById(R.id.processto_pay_button);
        delivery_pay_button = findViewById(R.id.delivery_pay_button);

        address_add_popup_close = findViewById(R.id.address_add_popup_close);
       /* collection_btn = findViewById(R.id.collection_btn);
        delivery_btn = findViewById(R.id.delivery_btn);*/

        extr_amt_rel_layout = findViewById(R.id.extr_amt_rel_layout);
        service_txt = findViewById(R.id.service_txt);
        service_total = findViewById(R.id.service_total);

        signin = findViewById(R.id.signin);

        simpleTimePicker = findViewById(R.id.simpleTimePicker);


        update_time = findViewById(R.id.update_time);
        recyclerview_address = findViewById(R.id.recyclerview_address);

        edit_postcode = findViewById(R.id.edit_postcode);
        edit_doorno = findViewById(R.id.edit_doorno);
        edit_town = findViewById(R.id.edit_town);
        edit_street = findViewById(R.id.edit_street);
        edit_home_button = findViewById(R.id.edit_home_button);
        edit_office_button = findViewById(R.id.edit_office_button);
        edit_other_button = findViewById(R.id.edit_other_button);
        edit_save_address = findViewById(R.id.edit_save_address);
        add_save_new_address = findViewById(R.id.add_save_new_address);

        apply_coupon_edit = findViewById(R.id.apply_coupon_edit);
        editcoubon = findViewById(R.id.editcoubon);

        cusomer_mobile = findViewById(R.id.cusomer_mobile);
        number_add = findViewById(R.id.number_add);
        mobilenumber = findViewById(R.id.mobilenumber);
        address_type = "0";
        zero_contact = "0";


        Log.e("address_type", "" + address_type);
        add_address = findViewById(R.id.add_address);//no address add new

        Sub_total = findViewById(R.id.Sub_total);
        //  card_total = findViewById(R.id.card_total);

        add_more_button = findViewById(R.id.add_more_button);
        delivery_add_more_button = findViewById(R.id.delivery_add_more_button);


        no_signin = findViewById(R.id.no_signin);
        process_item = findViewById(R.id.process_item);
        no_address = findViewById(R.id.no_address);
        address_view = findViewById(R.id.address_view);


        collection_order = findViewById(R.id.collection_order);
        delivery_order = findViewById(R.id.delivery_order);


        checked_colloection = findViewById(R.id.checked_colloection);
        checked_delivery = findViewById(R.id.checked_delivery);


        animationdown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animationup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        order_type_layout = findViewById(R.id.order_type_layout);


        //  zero_checkBox = findViewById(R.id.zero_checkBox);
        /*zero_checkBox.setButtonDrawable(null);
        zero_checkBox.setBackgroundResource(R.drawable.custom_checkbox);*/

        change_address = findViewById(R.id.change_address);
        address_titels = findViewById(R.id.address_titels);
        cus_address = findViewById(R.id.cus_address);


        add_new_address = findViewById(R.id.add_new_address);


        number_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                updateaccountdetails(cusomer_mobile.getText().toString(), user_id, first_name, last_name);

            }
        });


        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentthir = new Intent(Add_to_Cart.this, Item_Menu_Activity.class);
                intentthir.putExtra("menuurlpath", menuurlpath);
                intentthir.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
                startActivity(intentthir);

            }
        });


        /*---------------------------Intent Value Get URL Path----------------------------------------------------*/
        Intent intent = getIntent();




        if (intent.getStringExtra("cooking_insttruction") == null) {
            cooking_insttruction.setText("");
        } else {
            cooking_insttructionback = intent.getStringExtra("cooking_insttruction");
            cooking_insttruction.setText(cooking_insttructionback);
        }

        /*---------------------------post_code_check true or not----------------------------------------------------*/

        edit_postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.e("lenth", "" + s.length());

                edit_postcode.setOnKeyListener(new View.OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            keyDel = 1;
                        if (s.length() == 0) {
                            keyDel = 0;
                        }
                        return false;


                    }
                });


                if (keyDel == 0) {

                    nospace = s.toString().replace(" ", "");

                    Log.e("nospace", "" + nospace.length());

                    if (nospace.length() == 5) {

                        String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";

                        if (nospace.matches(regex) == true) {

                            Log.e("zipmatches", "" + nospace.matches(regex));

                            validatezip(nospace);

                        }
                    }
                    if (nospace.length() == 6) {

                        String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";

                        if (nospace.matches(regex) == true) {
                            Log.e("zipmatches", "" + nospace.matches(regex));
                            validatezip(nospace);
                        }
                    }
                    if (nospace.length() == 7) {
                        String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
                        if (nospace.matches(regex) == true) {
                            Log.e("zipmatches", "" + nospace.matches(regex));
                            validatezip(nospace);
                        }
                    }
                } else {
                    keyDel = 0;
                }

            }
        });

        /*---------------------new address add button click-----------------------*/
        add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                address_item_view.setVisibility(View.GONE);
                edit_address_popup.setVisibility(View.VISIBLE);
                edit_save_address.setVisibility(View.GONE);//update address button
                add_save_new_address.setVisibility(View.VISIBLE);//add new address

                edit_postcode.setText("");
                edit_doorno.setText("");
                edit_town.setText("");
                edit_street.setText("");
                address_type = "0";


                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));


            }
        });

        address_item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_postcode.setText("");
                edit_doorno.setText("");
                edit_town.setText("");
                edit_street.setText("");
                address_item_view.setVisibility(View.GONE);
            }
        });

        /*zero_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CompoundButton) view).isChecked()) {
                    // Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_LONG).show();
                    zero_contact = "1";
                } else {
                    //  Toast.makeText(getApplicationContext(), "Un-Checked", Toast.LENGTH_LONG).show();
                    zero_contact = "0";
                }
            }
        });*/

/*

        if (sharedpreferences.getString("ordermodetype", null).equalsIgnoreCase("0")) {
            checked_colloection.setImageResource(R.drawable.checked_light);
            checked_delivery.setImageResource(R.drawable.checked_blue);
            //order type set
            but_order_type = "0";
        } else if (sharedpreferences.getString("ordermodetype", null).equalsIgnoreCase("1")) {
            checked_colloection.setImageResource(R.drawable.checked_blue);
            checked_delivery.setImageResource(R.drawable.checked_light);
            //order type set
            but_order_type = "1";
        } else {
            checked_colloection.setImageResource(R.drawable.checked_light);
            checked_delivery.setImageResource(R.drawable.checked_blue);
        }
*/


      /*  collection_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checked_colloection.setImageResource(R.drawable.checked_blue);
                checked_delivery.setImageResource(R.drawable.checked_light);

                but_order_type = "1";
                CheckLogin();
                SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                editor_extra.putString("ordermodetype", "1");
                editor_extra.commit();
                subcategory_printer();

            }
        });

        delivery_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checked_colloection.setImageResource(R.drawable.checked_light);
                checked_delivery.setImageResource(R.drawable.checked_blue);

                but_order_type = "0";
                CheckLogin();
                SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                editor_extra.putString("ordermodetype", "0");
                editor_extra.commit();
                subcategory_printer();
            }
        });
*/

        /*delivery_card_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delivery_btn == false) {
                    delivery_btn = true;
                    delivery_card_view2.setVisibility(View.VISIBLE);
                    collection_card_view3.setVisibility(View.GONE);
                    order_type_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.box));
                    //collection_card_view3.startAnimation(animationup);
                    collection_card_view3.setCardBackgroundColor(ContextCompat.getColor(Add_to_Cart.this, R.color.ord_type));
                    delivery_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_more_24, 0);
                    collection_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_nomore_24, 0);
                    //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                    but_order_type = "1";
                    CheckLogin();
                } else {
                    delivery_btn = false;
                    collection_card_view3.setVisibility(View.VISIBLE);
                    delivery_card_view2.setVisibility(View.VISIBLE);
                    delivery_card_view2.startAnimation(animationdown);
                    order_type_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    collection_card_view3.setCardBackgroundColor(ContextCompat.getColor(Add_to_Cart.this, R.color.ord_type));
                    delivery_card_view2.setCardBackgroundColor(ContextCompat.getColor(Add_to_Cart.this, R.color.white));
                    delivery_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_nomore_24, 0);
                    collection_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_less_24, 0);

                    //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                }

            }
        });
        collection_card_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collection_btn == true) {
                    collection_btn = false;
                    delivery_card_view2.startAnimation(animationup);
                    delivery_card_view2.setVisibility(View.GONE);
                    collection_card_view3.setVisibility(View.VISIBLE);
                    collection_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_more_24, 0);
                    delivery_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_nomore_24, 0);
                    //Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();

                    but_order_type = "0";
                    CheckLogin();
                } else {
                    collection_btn = true;
                    delivery_card_view2.setVisibility(View.VISIBLE);
                    collection_card_view3.setVisibility(View.VISIBLE);
                    delivery_card_view2.startAnimation(animationdown);
                    //Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_LONG).show();
                    delivery_card_view2.setCardBackgroundColor(ContextCompat.getColor(Add_to_Cart.this, R.color.txt_blu_col));
                    collection_card_view3.setCardBackgroundColor(ContextCompat.getColor(Add_to_Cart.this, R.color.txt_blu_col));
                    collection_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_less_24, 0);
                    delivery_tex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_nomore_24, 0);

                }

            }
        });*/

      /*  if (pay_type.equalsIgnoreCase("2")) {

            service_txt.setVisibility(View.INVISIBLE);
            service_total.setVisibility(View.INVISIBLE);

        } else {
            service_txt.setVisibility(View.VISIBLE);
            service_total.setVisibility(View.VISIBLE);

        }*/

        /*---------------------------Update time picker ----------------------------------------------------*/
        update_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = simpleTimePicker.getHour();
                    minute = simpleTimePicker.getMinute();
                } else {
                    hour = simpleTimePicker.getCurrentHour();
                    minute = simpleTimePicker.getCurrentMinute();
                }
                if (hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                } else if (hour == 0) {
                    hour = hour + 12;
                    am_pm = "AM";
                } else if (hour == 12) {
                    am_pm = "PM";
                } else {
                    am_pm = "AM";
                }
                // delivery_time_popup.setVisibility(View.GONE);
                Intent intent = new Intent(Add_to_Cart.this, Payment_method_Activity.class);
                intent.putExtra("pay_amount", totalcardamt);
                intent.putExtra("order_types", "Delivery");
                intent.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
                intent.putExtra("addressids", addressids);
                intent.putExtra("customerpostcode", customerpostcode);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Selected Date: " + String.format("%02d:%02d", hour, minute) + " " + am_pm, Toast.LENGTH_LONG).show();
            }
        });
        /*---------------------------SignUp and SignIn ----------------------------------------------------*/
        signin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_to_Cart.this, Login_Activity.class);
                intent.putExtra("activity_details", "cart");
                intent.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
                startActivity(intent);
            }
        });

        /*---------------------------Add address button click no address----------------------------------------------------*/
        add_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_address_popup.setVisibility(View.VISIBLE);
                edit_save_address.setVisibility(View.GONE);//update address button
                add_save_new_address.setVisibility(View.VISIBLE);//add new address

                edit_postcode.setText("");
                edit_doorno.setText("");
                edit_town.setText("");
                edit_street.setText("");
                address_type = "0";

                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));


            }
        });
        add_save_new_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                add_save_new_address.setClickable(false);
                //  Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_LONG).show();
                add_address_validate();
                //  edit_address_popup.setVisibility(View.GONE);
                // Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_LONG).show();
                new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        add_save_new_address.setClickable(true);
                    }
                }.start();
            }
        });
        address_add_popup_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_postcode.setText("");
                edit_doorno.setText("");
                edit_town.setText("");
                edit_street.setText("");

                edit_address_popup.setVisibility(View.GONE);

                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

            }
        });
       /* edit_address_popup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                edit_address_popup.setVisibility(View.GONE);


            }
        });*/
        delivery_time_popup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delivery_time_popup.setVisibility(View.GONE);
            }
        });
        /*---------------------------Allergy Type ----------------------------------------------------*/

        cooking_insttruction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cooking_insttruction.setCursorVisible(true);
            }
        });

        /*---------------------------edit address type----------------------------------------------------*/

        edit_home_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_one));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_type));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));
                address_type = "1";

            }
        });

        /*---------------------------edit offer button click ----------------------------------------------------*/

        edit_office_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_one));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_type));


                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));
                address_type = "2";
            }
        });

        /*---------------------------edit other button click ----------------------------------------------------*/
        edit_other_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));


                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_one));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_type));
                address_type = "3";
            }
        });



        /*---------------------------Change collection and delivery buttons ----------------------------------------------------*/

       /* collection_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                but_order_type = "0";
                collection_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                collection_btn.setBackgroundResource(R.drawable.background_round_color);

                delivery_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                delivery_btn.setBackgroundResource(R.drawable.background_round_no_color);

                *//*--------------Login store SharedPreferences------------------*//*
                CheckLogin();
            }
        });

        delivery_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                but_order_type = "1";
                collection_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_ten));
                collection_btn.setBackgroundResource(R.drawable.background_round_no_color);

                delivery_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                delivery_btn.setBackgroundResource(R.drawable.background_round_color);
                *//*--------------Login store SharedPreferences------------------*//*
                CheckLogin();
            }
        });*/


        /*---------------------------Collection Button go to Payment page  ----------------------------------------------------*/
        processto_pay_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

               if (cusomer_mobile.getText().toString().isEmpty()) {
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), "Please enter your contact Number", Snackbar.LENGTH_LONG).show();
                } else {
                    checkoutvalidate(menuurlpath, totalcardamt, "1", "");

                    //  Toast.makeText(getApplicationContext(),"collection",Toast.LENGTH_LONG).show();
                }

            }
        });
        /*---------------------------delivery Button go to Payment page ----------------------------------------------------*/
        delivery_pay_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                // delivery_time_popup.setVisibility(View.VISIBLE);

                // delivery_time_popup.setVisibility(View.GONE);

                if (cusomer_mobile.getText().toString().isEmpty()) {
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), "Please enter your contact Number", Snackbar.LENGTH_LONG).show();
                } else {
                    checkoutvalidate(menuurlpath, totalcardamt, "0", customerpostcode);
                    //Toast.makeText(getApplicationContext(),"delivery",Toast.LENGTH_LONG).show();
                }
            }
        });

        /*---------------------------add more to go menu page button click ----------------------------------------------------*/
        add_more_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(Add_to_Cart.this, Item_Menu_Activity.class);
                 intent.putExtra("menuurlpath", sharedpreferences.getString("menuurlpath", null));
                 intent.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
                 startActivity(intent);
            }
        });
        delivery_add_more_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_to_Cart.this, Item_Menu_Activity.class);
                intent.putExtra("menuurlpath", sharedpreferences.getString("menuurlpath", null));
                intent.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
                startActivity(intent);
            }
        });

        /*---------------------------Change address----------------------------------------------------*/

        change_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                address_item_view.setVisibility(View.VISIBLE);
                get_address(user_id);
            }
        });

        /*---------------------------Offer RecyclerView ----------------------------------------------------*/

        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/

        LocalBroadcastManager.getInstance(this).registerReceiver(mitemidReceiver, new IntentFilter("delete-itemid"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mitemupdate, new IntentFilter("update_itemqty"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mitemupdatesubtotal, new IntentFilter("update_subtotal"));

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        /*---------------------------Sql Lite DataBase----------------------------------------------------*/

        dbHelper = new SQLDBHelper(Add_to_Cart.this);

        //  ArrayList<Cartitem> allContacts = dbHelper.listContacts();
        // ArrayList<Contacts> allContacts = (ArrayList<Contacts>) dbHelper.getAllItem();
        contactView = findViewById(R.id.myContactList);



        subcategory_printer();
/*

        dbHelper = new SQLDBHelper(Add_to_Cart.this);
      //  ArrayList<Cartitem> allContacts = dbHelper.listContacts();
        // ArrayList<Contacts> allContacts = (ArrayList<Contacts>) dbHelper.getAllItem();
        contactView = findViewById(R.id.myContactList);
        ArrayList<Cartitem> allContacts = dbHelper.listContacts();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Add_to_Cart.this);
        contactView.setLayoutManager(linearLayoutManager);
        Log.e("allContacts", "" + allContacts.size() + "one");
        if (allContacts.size() >= 4) {
            ViewGroup.LayoutParams params = contactView.getLayoutParams();
            params.height = 1200;
            contactView.setLayoutParams(params);
        } else {
            contactView.getLayoutParams().height = WRAP_CONTENT;
        }
        subcategory_printer();
       */
/* CartitemlistAdapter mAdapter = new CartitemlistAdapter(Add_to_Cart.this, allContacts, menuurlpath);
        contactView.setAdapter(mAdapter);*//*
        if (allContacts.size() == 0) {
            finish();
        }
*/
        /*--------------Login store SharedPreferences------------------*/
        //  CheckLogin();
        /*---------------------------addressAdapter item update----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageaddressReceiver, new IntentFilter("address_view-message"));

        /*---------------------------addressAdapter item update----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageupdateReceiver, new IntentFilter("address_update-message"));


        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessagecollectononly, new IntentFilter("collection_only"));

        /*---------------------------MenuItemAdapter item value get----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessagereaplastcollectononly, new IntentFilter("collection_only_repatlast"));

    }

    /*---------------------------collection only----------------------------------------------------*/
    public BroadcastReceiver mMessagecollectononly = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadingshow();
            strItemName = intent.getStringExtra("item");
            straddonid = intent.getStringExtra("addonid");
            strcategoryname = intent.getStringExtra("categoryname");
            strsubcategoryname = intent.getStringExtra("subcategoryname");
            strmenuurlpath = intent.getStringExtra("menuurlpath");

            Log.e("getdetailsforadpater", "" + strItemName + "\n" + straddonid + "\n" + strcategoryname + "\n" + strsubcategoryname + "\n" + strmenuurlpath);


          /*  checked_colloection.setImageResource(R.drawable.checked_blue);
            checked_delivery.setImageResource(R.drawable.checked_light);*/
            //order type set
            but_order_type = "1";
            CheckLogin();
            subcategory_printer();

            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
            editor_extra.putString("ordermodetype", "1");
            editor_extra.commit();


            Map<String, String> params = new HashMap<String, String>();
            params.put("id", strItemName);
            params.put("ordermode", but_order_type);


            fullUrladdon = strmenuurlpath + "/menu" + "/itemadd";
            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            Call<menu_addon_status_model> call = apiService.menuaddon(fullUrladdon, params);


            Log.e("addtocartparams4", ": " + params);
            Log.e("fullUrladdon", ": " + fullUrladdon);

            call.enqueue(new Callback<menu_addon_status_model>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<menu_addon_status_model> call, Response<menu_addon_status_model> response) {
                    //response.headers().get("Set-Cookie");

                    int statusCode = response.code();
                    if (statusCode == 200) {
                        hideloading();
                        Intent intentitemdetails = new Intent(Add_to_Cart.this, Item_Menu_Activity.class);
                        intentitemdetails.putExtra("item", strItemName);
                        intentitemdetails.putExtra("addonid", response.body().getAddonId());
                        intentitemdetails.putExtra("categoryname", strcategoryname);
                        intentitemdetails.putExtra("subcategoryname", strsubcategoryname);
                        intentitemdetails.putExtra("menuurlpath", strmenuurlpath);
                        startActivity(intentitemdetails);
                    } else {
                        hideloading();
                        Toast.makeText(Add_to_Cart.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<menu_addon_status_model> call, Throwable t) {
                    hideloading();
                    Log.e("menuThrowable", "" + t);
                    Toast.makeText(Add_to_Cart.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            });


        }
    };


    /*---------------------------collection only repeat last----------------------------------------------------*/
    public BroadcastReceiver mMessagereaplastcollectononly = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

           /* checked_colloection.setImageResource(R.drawable.checked_blue);
            checked_delivery.setImageResource(R.drawable.checked_light);*/
            //order type set
            but_order_type = "1";
            CheckLogin();
            subcategory_printer();

            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
            editor_extra.putString("ordermodetype", "1");
            editor_extra.commit();


        }
    };


    /*---------------------------get order mode coll delivery----------------------------------------------------*/
    private void collDelivery(String smenuurlpath) {
        fullUrl = smenuurlpath + "/collDelivery";
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<collDelivery_model> call = apiService.changecollDelivery(fullUrl);

        Log.e("cartfullUrl", "" + fullUrl);
        call.enqueue(new Callback<collDelivery_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<collDelivery_model> call, Response<collDelivery_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        if (response.body().getData().getDelivery().equalsIgnoreCase("0") && response.body().getData().getCollection().equalsIgnoreCase("1")) {
                            collection_order.setVisibility(View.VISIBLE);
                            delivery_order.setVisibility(View.VISIBLE);
                            if (sharedpreferences.getString("ordermodetype", null).equalsIgnoreCase("0")) {
                                checked_colloection.setImageResource(R.drawable.checked_light);
                                checked_delivery.setImageResource(R.drawable.checked_blue);
                                //order type set
                                but_order_type = "0";
                                CheckLogin();
                                subcategory_printer();
                            } else if (sharedpreferences.getString("ordermodetype", null).equalsIgnoreCase("1")) {
                                checked_colloection.setImageResource(R.drawable.checked_blue);
                                checked_delivery.setImageResource(R.drawable.checked_light);
                                //order type set
                                but_order_type = "1";
                                CheckLogin();
                                subcategory_printer();
                            }

                        } else if (response.body().getData().getDelivery().equalsIgnoreCase("") && response.body().getData().getCollection().equalsIgnoreCase("1")) {
                            collection_order.setVisibility(View.VISIBLE);
                            delivery_order.setVisibility(View.GONE);

                            checked_colloection.setImageResource(R.drawable.checked_blue);
                            checked_delivery.setImageResource(R.drawable.checked_light);

                            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                            editor_extra.putString("ordermodetype", "1");
                            editor_extra.commit();

                            but_order_type = "1";
                            CheckLogin();
                            subcategory_printer();
                        } else if (response.body().getData().getDelivery().equalsIgnoreCase("0") && response.body().getData().getCollection().equalsIgnoreCase("")) {
                            collection_order.setVisibility(View.GONE);
                            delivery_order.setVisibility(View.VISIBLE);

                            checked_colloection.setImageResource(R.drawable.checked_light);
                            checked_delivery.setImageResource(R.drawable.checked_blue);

                            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                            editor_extra.putString("ordermodetype", "0");
                            editor_extra.commit();

                            but_order_type = "0";
                            CheckLogin();
                            subcategory_printer();
                        } else {

                            collection_order.setVisibility(View.GONE);
                            delivery_order.setVisibility(View.GONE);
                        }


                    } else {
                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<collDelivery_model> call, Throwable t) {

                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*---------------------------update primery value----------------------------------------------------*/
    public BroadcastReceiver mMessageaddressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            addressviewopen = intent.getStringExtra("addressviewopen");
            addresstitle = intent.getStringExtra("addresstitle");
            addressname = intent.getStringExtra("addressname");
            addressids = intent.getStringExtra("addressid");
            customerpostcode = intent.getStringExtra("customerpostcode");


            address_titels.setText(addresstitle);
            cus_address.setText(addressname);

            //  Toast.makeText(getApplicationContext(), addressids, Toast.LENGTH_LONG).show();
            if (addressviewopen.equalsIgnoreCase("1")) {
                edit_postcode.setText("");
                edit_doorno.setText("");
                edit_town.setText("");
                edit_street.setText("");
                address_item_view.setVisibility(View.GONE);

            }
        }
    };


    /*---------------------------addressAdapter item delete----------------------------------------------------*/
    public BroadcastReceiver mMessageupdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            addressid = intent.getStringExtra("addressid");
            addresscid = intent.getStringExtra("addresscid");
            addressno = intent.getStringExtra("addressno");
            addressaddress1 = intent.getStringExtra("addressaddress1");
            addressaddress2 = intent.getStringExtra("addressaddress2");
            addresspcode = intent.getStringExtra("addresspcode");
            addressgtype = intent.getStringExtra("addressgtype");




            /*---------------------------Update Address layout show----------------------------------------------------*/

            edit_save_address.setVisibility(View.VISIBLE);//update address button
            add_save_new_address.setVisibility(View.GONE);//add address

            no_signin.setVisibility(View.GONE);
            process_item.setVisibility(View.GONE);
            no_address.setVisibility(View.GONE);
            //   address_view.setVisibility(View.GONE);
            edit_address_popup.setVisibility(View.VISIBLE);

            address_type = addressgtype;

            if (addressgtype.equalsIgnoreCase("1")) {
                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_one));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_type));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

            } else if (addressgtype.equalsIgnoreCase("2")) {
                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_one));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_type));


                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

            } else if (addressgtype.equalsIgnoreCase("3")) {
                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));


                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_one));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_type));

            } else {
                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

            }


            edit_postcode.setText(addresspcode);
            edit_doorno.setText(addressno);
            edit_town.setText(addressaddress2);
            edit_street.setText(addressaddress1);


            edit_save_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Toast.makeText(getApplicationContext(), "update", Toast.LENGTH_LONG).show();
                    edit_save_address.setClickable(false);
                    update_address_validate();

                    new CountDownTimer(5000, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            edit_save_address.setClickable(true);
                        }
                    }.start();

                }
            });


        }
    };

    private void validatezip(String zipcodesp) {

        str_postcode_seperate = zipcodesp;
        if (str_postcode_seperate.length() == 5) {
            str_postcode_seperate_str = str_postcode_seperate.substring(0, 2) + " " + str_postcode_seperate.substring(2);
        } else if (str_postcode_seperate.length() == 6) {
            str_postcode_seperate_str = str_postcode_seperate.substring(0, 3) + " " + str_postcode_seperate.substring(3);
        } else if (str_postcode_seperate.length() == 7) {
            str_postcode_seperate_str = str_postcode_seperate.substring(0, 4) + " " + str_postcode_seperate.substring(4);
        } else {
            str_postcode_seperate_str = str_postcode_seperate;
        }
        // edit_postcode.setText(str_postcode_seperate_str.toUpperCase());
        Log.e("edit_postcode", "" + str_postcode_seperate_str.toUpperCase());

        getaddressforpostcode(str_postcode_seperate_str.toUpperCase());

    }


    private void update_address_validate() {

        if (TextUtils.isEmpty(edit_postcode.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_postcode.getWindowToken(), 0);
            edit_postcode.setError("Please fill out this field.");
            // Snackbar.make(this.findViewById(android.R.id.content), "Please enter your mobile number", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(edit_doorno.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_doorno.getWindowToken(), 0);
            edit_doorno.setError("Please fill out this field.");

        } else if (TextUtils.isEmpty(edit_town.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_town.getWindowToken(), 0);
            edit_town.setError("Please fill out this field.");

        } else if (TextUtils.isEmpty(edit_street.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_street.getWindowToken(), 0);
            edit_street.setError("Please fill out this field.");

        } else if (address_type.equalsIgnoreCase("0")) {


            Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), "Please fill out address type field. ", Snackbar.LENGTH_LONG).show();


        } else {

         /*   str_postcode_seperate = edit_postcode.getText().toString().replace(" ", "");
            if (str_postcode_seperate.length() == 5) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 2) + " " + str_postcode_seperate.substring(2);
            } else if (str_postcode_seperate.length() == 6) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 3) + " " + str_postcode_seperate.substring(3);
            } else if (str_postcode_seperate.length() == 7) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 4) + " " + str_postcode_seperate.substring(4);
            } else {
                str_postcode_seperate_str = str_postcode_seperate;
            }
            edit_postcode.setText(str_postcode_seperate_str.toUpperCase());
            updatepostcodecheck(edit_postcode.getText().toString());*/
            //   update_new_address(addressid, edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);

            // add_address_validate()


            str_postcode_seperate = edit_postcode.getText().toString().replace(" ", "");
            if (str_postcode_seperate.length() == 5) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 2) + " " + str_postcode_seperate.substring(2);
            } else if (str_postcode_seperate.length() == 6) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 3) + " " + str_postcode_seperate.substring(3);
            } else if (str_postcode_seperate.length() == 7) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 4) + " " + str_postcode_seperate.substring(4);
            } else {
                str_postcode_seperate_str = str_postcode_seperate;
            }
            edit_postcode.setText(str_postcode_seperate_str.toUpperCase());


            nospacecheckpost = str_postcode_seperate_str.replace(" ", "");

            Log.e("nospace", "" + nospacecheckpost.length());

            String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
            Log.e("zipmatches", "" + nospacecheckpost.matches(regex));
            if (nospacecheckpost.matches(regex) == true) {

                // updatepostcodecheck(edit_postcode.getText().toString());

                update_new_address(addressid, edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);

            } else {


                edit_postcode.setText("");


                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), " Please enter a valid postcode ", Snackbar.LENGTH_LONG).show();

            }


        }

    }

    //get api values
   /* private void updatepostcodecheck(final String post_code) {

        //final ProgressDialog loader = ProgressDialog.show(Add_to_Cart.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", post_code);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<post_code_modal> call = apiService.getpostcode(params);
        call.enqueue(new Callback<post_code_modal>() {
            @Override
            public void onResponse(Call<post_code_modal> call, Response<post_code_modal> response) {
                int statusCode = response.code();
                *//*Get Login Good Response...*//*
                if (statusCode == 200) {

                    // loader.dismiss();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {


                        update_new_address(addressid, edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);
                    } else {
                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getPostcode(), Snackbar.LENGTH_LONG).show();
                    }

                } else {

                    // loader.dismiss();
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<post_code_modal> call, Throwable t) {

                Log.e("Tro", "" + t);

                // loader.dismiss();
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }*/


    /*  ---------------------------Update address values----------------------------------------------------*/
    private void update_new_address(String str_id, String str_doorno, String str_street, String str_town, String str_postcode, String str_status, String str_cid, String str_type) {


        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", str_id);
        params.put("no", str_doorno);
        params.put("address1", str_street);
        params.put("address2", str_town);
        params.put("postcode", str_postcode);
        params.put("status", str_status);
        params.put("cid", str_cid);
        params.put("type", str_type);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<addAddress_mode> call = apiService.updateaddress(params);

        call.enqueue(new Callback<addAddress_mode>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<addAddress_mode> call, Response<addAddress_mode> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        edit_postcode.setText("");
                        edit_doorno.setText("");
                        edit_town.setText("");
                        edit_street.setText("");


                        edit_address_popup.setVisibility(View.GONE);
                        get_address(user_id);

                        edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                        edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                        edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                        edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                        edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                        edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));


                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    } else {

                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {

                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<addAddress_mode> call, Throwable t) {

                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*--------------Login store SharedPreferences------------------*/
    //no_signin,process_item,no_address,address_view
    public void CheckLogin() {
        if (slogin == null)
            slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);

        String login_status = slogin.getString("login_key_status", "");
        Log.e("login_status", "" + login_status);

        if (login_status != null && !login_status.equals("")) {
//login Successfully
                   getaccountdetails(user_id, login_type_id);

// 0 - delivery  1 - collection
            if (but_order_type.equalsIgnoreCase("1")) {
                no_signin.setVisibility(View.GONE);
                process_item.setVisibility(View.VISIBLE);
                no_address.setVisibility(View.GONE);
                address_view.setVisibility(View.GONE);

            } else {
                get_address(user_id);
                no_signin.setVisibility(View.GONE);
                process_item.setVisibility(View.GONE);
                no_address.setVisibility(View.GONE);
                address_view.setVisibility(View.VISIBLE);

            }
        } else {
//login not Successfully
            mobilenumber.setVisibility(View.GONE);
            no_signin.setVisibility(View.VISIBLE);
            process_item.setVisibility(View.GONE);
            no_address.setVisibility(View.GONE);
            address_view.setVisibility(View.GONE);

        }
    }


    /*---------------------Delete item---------------------------*/
    public BroadcastReceiver mitemidReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            ItemId = intent.getStringExtra("ItemId");
            menuurlpaths = intent.getStringExtra("menuurlpath");

            Log.e("ItemId", "" + ItemId);

            dbHelper.deleteItem(Integer.parseInt(ItemId));

            subcategory_printer();


        }
    };
    /*------------------------------Update Item Amount and qty---------------------------*/
    public BroadcastReceiver mitemupdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            ItemupdateId = intent.getStringExtra("ItemId");
            Itemupdateqty = intent.getStringExtra("Itemqty");
            Itemupdateamt = intent.getStringExtra("Itemamount");

            Log.e("ItemId", "" + ItemId);

            dbHelper.updateItem(Integer.parseInt(ItemupdateId), Itemupdateqty, Itemupdateamt);

            subcategory_printer();

           /* if(dbHelper.updateItem(Integer.parseInt(ItemupdateId), Itemupdateqty,Itemupdateamt)) {
               // Toast.makeText(getApplicationContext(), "Person Update Successful", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(getApplicationContext(), "Person Update Failed", Toast.LENGTH_SHORT).show();
            }*/


        }
    };

    /*------------------------------Update Item sub total---------------------------*/
    public BroadcastReceiver mitemupdatesubtotal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Itemsubtotal = intent.getStringExtra("Itemsubtotal");

            Sub_total.setText("£ " + Itemsubtotal);

           /* subamt = Double.parseDouble(Itemsubtotal);
            serviceamt = Double.parseDouble(service_total.getText().toString());
            cartamt = subamt + serviceamt;
            card_total.setText("£ " + String.format("%.2f", cartamt));*/


            // card_total.setText("£ " + Itemsubtotal);

            processto_pay_button.setText("Pay £ " + Itemsubtotal);
            delivery_pay_button.setText("Pay £ " + Itemsubtotal);
            totalcardamt = Itemsubtotal;


        }
    };



    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Add_to_Cart.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Add_to_Cart.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Add_to_Cart.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }

                }
            });
            alertDialog.show();

        }
    }
    /*---------------------------------subcategory_printer--------------------------------------*/

    private void subcategory_printer() {


        subcatfullurl = menuurlpath + "/subcategory_printer";

        Log.e("subcatfullurl", "" + subcatfullurl);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<subcategory_printer_model> call = apiService.subcategoryprinter(subcatfullurl);
        call.enqueue(new Callback<subcategory_printer_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<subcategory_printer_model> call, Response<subcategory_printer_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {

                    CheckLogin();
                    //subcategory_printer();


                    subcategory_printers = response.body().getSubcategory();

                    Log.e("namesubcategory", "" + subcategory_printers);



                    /*---------------------------Sql Lite DataBase----------------------------------------------------*/


                    dbHelper = new SQLDBHelper(Add_to_Cart.this);

                    //  ArrayList<Cartitem> allContacts = dbHelper.listContacts();
                    // ArrayList<Contacts> allContacts = (ArrayList<Contacts>) dbHelper.getAllItem();

                    //   contactView = findViewById(R.id.myContactList);


                    ArrayList<Cartitem> allContacts = dbHelper.listContacts();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Add_to_Cart.this);
                    contactView.setLayoutManager(linearLayoutManager);
                    Log.e("allContacts", "" + allContacts.size() + "one");


                   /* for(int i = 0; i < allContacts.size(); i++) {
                        Log.d("allContacts",allContacts.get(i).getItemid());
                    }
*/


                    if (allContacts.size() >= 4) {
                        ViewGroup.LayoutParams params = contactView.getLayoutParams();
                        params.height = 1200;
                        contactView.setLayoutParams(params);
                    } else {
                        contactView.getLayoutParams().height = WRAP_CONTENT;
                    }

                    if (allContacts.size() == 0) {

                        SQLDBHelper dbHelper = new SQLDBHelper(Add_to_Cart.this);
                        SQLiteDatabase db = dbHelper.getReadableDatabase();

                        try {

                            db.execSQL("DELETE FROM " + dbHelper.DATABASE_NAME);

                        } catch (SQLiteException e) {
                            e.printStackTrace();
                            Log.e("sqliteerror", "" + e);
                        }

                        // back.performClick();
                        Intent intentdasboard = new Intent(Add_to_Cart.this, Item_Menu_Activity.class);
                        intentdasboard.putExtra("menuurlpath", menuurlpaths);
                        startActivity(intentdasboard);
                        Log.e("deletemenu", "" + menuurlpath);
                    }
                    CartitemlistAdapter mAdapter = new CartitemlistAdapter(mContext, allContacts, menuurlpath, subcategory_printers, but_order_type);
                    contactView.setAdapter(mAdapter);


                }
            }

            @Override
            public void onFailure(Call<subcategory_printer_model> call, Throwable t) {

                Toast.makeText(Add_to_Cart.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();

            }
        });


    }

    /*  ---------------------------get address values----------------------------------------------------*/
    private void get_address(String usereid) {

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", usereid);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<getaddAddress_mode> call = apiService.getaddAddress(params);

        call.enqueue(new Callback<getaddAddress_mode>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<getaddAddress_mode> call, Response<getaddAddress_mode> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        if (response.body().getMsg().equalsIgnoreCase("Data not Found")) {


                            no_signin.setVisibility(View.GONE);
                            process_item.setVisibility(View.GONE);
                            no_address.setVisibility(View.VISIBLE);
                            address_view.setVisibility(View.GONE);

                        } else {


                            List<getaddAddress_mode.userdetail> jobdetails = (response.body().getUserdetail());
                            GetcartaddressListAdapter adapter = new GetcartaddressListAdapter(mContext, (List<getaddAddress_mode.userdetail>) jobdetails);
                            recyclerview_address.setHasFixedSize(true);
                            recyclerview_address.setLayoutManager(new LinearLayoutManager(Add_to_Cart.this));
                            recyclerview_address.setAdapter(adapter);


                            no_signin.setVisibility(View.GONE);
                            process_item.setVisibility(View.GONE);
                            no_address.setVisibility(View.GONE);
                            address_view.setVisibility(View.VISIBLE);

                        }

                    } else {
                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();

                    }

                } else {

                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<getaddAddress_mode> call, Throwable t) {

                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*---------------------------validate address----------------------------------------------------*/
    /*Check Login Details Hear...!*/
    private void add_address_validate() {

        if (TextUtils.isEmpty(edit_postcode.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_postcode.getWindowToken(), 0);
            edit_postcode.setError("Please fill out this field.");
            // Snackbar.make(this.findViewById(android.R.id.content), "Please enter your mobile number", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(edit_doorno.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_doorno.getWindowToken(), 0);
            edit_doorno.setError("Please fill out this field.");

        } else if (TextUtils.isEmpty(edit_town.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_town.getWindowToken(), 0);
            edit_town.setError("Please fill out this field.");

        } else if (TextUtils.isEmpty(edit_street.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_street.getWindowToken(), 0);
            edit_street.setError("Please fill out this field.");

        } else if (address_type.equalsIgnoreCase("0")) {


            Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), "Please fill out address type field. ", Snackbar.LENGTH_LONG).show();


        } else {
         /*   str_postcode_seperate = edit_postcode.getText().toString().replace(" ", "");
            if (str_postcode_seperate.length() == 5) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 2) + " " + str_postcode_seperate.substring(2);
            } else if (str_postcode_seperate.length() == 6) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 3) + " " + str_postcode_seperate.substring(3);
            } else if (str_postcode_seperate.length() == 7) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 4) + " " + str_postcode_seperate.substring(4);
            } else {
                str_postcode_seperate_str = str_postcode_seperate;
            }
            edit_postcode.setText(str_postcode_seperate_str.toUpperCase());*/
            // postcodecheck(edit_postcode.getText().toString());

            //  add_new_address(edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);


            //   nospacecheckpost = edit_postcode.getText().toString().replace(" ", "");


            str_postcode_seperate = edit_postcode.getText().toString().replace(" ", "");
            if (str_postcode_seperate.length() == 5) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 2) + " " + str_postcode_seperate.substring(2);
            } else if (str_postcode_seperate.length() == 6) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 3) + " " + str_postcode_seperate.substring(3);
            } else if (str_postcode_seperate.length() == 7) {
                str_postcode_seperate_str = str_postcode_seperate.substring(0, 4) + " " + str_postcode_seperate.substring(4);
            } else {
                str_postcode_seperate_str = str_postcode_seperate;
            }
            edit_postcode.setText(str_postcode_seperate_str.toUpperCase());


            nospacecheckpost = str_postcode_seperate_str.replace(" ", "");


            String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
            Log.e("zipmatchesaddnew", "" + nospacecheckpost.matches(regex));
            if (nospacecheckpost.matches(regex) == true) {

                //  postcodecheck(edit_postcode.getText().toString());

                add_new_address(edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);

            } else {
                address_type = "0";
                edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                edit_postcode.setText("");
                edit_doorno.setText("");
                edit_town.setText("");
                edit_street.setText("");
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), " Please enter a valid postcode ", Snackbar.LENGTH_LONG).show();

            }


        }

    }

    /*---------------------------Submit Post Code Check----------------------------------------------------*/
    //get api values
    private void getaddressforpostcode(final String post_code) {

        //final ProgressDialog loader = ProgressDialog.show(Add_to_Cart.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", post_code);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<getaddressforpostcode_modal> call = apiService.getaddressforpostcode(params);

        Log.e("apippostcode", "" + params);
        call.enqueue(new Callback<getaddressforpostcode_modal>() {
            @Override
            public void onResponse(Call<getaddressforpostcode_modal> call, Response<getaddressforpostcode_modal> response) {
                int statusCode = response.code();
                /*Get Login Good Response...*/
                if (statusCode == 200) {

                    // loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        edit_street.setText(response.body().getAddress().getStreet());

                        edit_town.setText(response.body().getAddress().getTown());

                        edit_postcode.setText(post_code);
                        keyDel = 1;

                    }

                } else {

                    // loader.dismiss();
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<getaddressforpostcode_modal> call, Throwable t) {

                Log.e("Tro", "" + t);

                // loader.dismiss();
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }

    /*---------------------------Submit Post Code Check----------------------------------------------------*/
    //get api values
  /*  private void postcodecheck(final String post_code) {

        //final ProgressDialog loader = ProgressDialog.show(Add_to_Cart.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("postcode", post_code);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<post_code_modal> call = apiService.getpostcode(params);
        call.enqueue(new Callback<post_code_modal>() {
            @Override
            public void onResponse(Call<post_code_modal> call, Response<post_code_modal> response) {
                int statusCode = response.code();
                *//*Get Login Good Response...*//*
                if (statusCode == 200) {

                    // loader.dismiss();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {


                        add_new_address(edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);
                    } else {
                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getPostcode(), Snackbar.LENGTH_LONG).show();
                    }

                } else {

                    // loader.dismiss();
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<post_code_modal> call, Throwable t) {

                Log.e("Tro", "" + t);

                // loader.dismiss();
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }*/

    /*  ---------------------------Add address values----------------------------------------------------*/
    private void add_new_address(String str_doorno, String str_street, String str_town, String str_postcode, String str_status, String str_cid, String str_type) {


        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("no", str_doorno);
        params.put("address1", str_street);
        params.put("address2", str_town);
        params.put("postcode", str_postcode);
        params.put("status", str_status);
        params.put("cid", str_cid);
        params.put("type", str_type);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<addAddress_mode> call = apiService.addAddress(params);

        call.enqueue(new Callback<addAddress_mode>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<addAddress_mode> call, Response<addAddress_mode> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        edit_postcode.setText("");
                        edit_doorno.setText("");
                        edit_town.setText("");
                        edit_street.setText("");


                        get_address(user_id);
                        no_signin.setVisibility(View.GONE);
                        process_item.setVisibility(View.GONE);
                        no_address.setVisibility(View.GONE);
                        address_view.setVisibility(View.VISIBLE);

                        edit_address_popup.setVisibility(View.GONE);


                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    } else {

                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {

                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<addAddress_mode> call, Throwable t) {

                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*  ------------------------------checkoutvalidate-------------------------*/
    private void checkoutvalidate(final String menuurlpath, String subtotals, String ordermode, String cuspostcode) {
        //final ProgressDialog loader = ProgressDialog.show(Add_to_Cart.this, "", "Loading...", true);
        Map<String, String> params = new HashMap<String, String>();
        // HashMap<String, String> user = session.getUserDetails();


        if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {
            params.put("otype", ordermode);
            params.put("postcode", cuspostcode);
            params.put("subtotal", subtotals);
            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
            params.put("dates", "");
            params.put("time", "");
            params.put("date_string","");
        } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {
            params.put("otype", ordermode);
            params.put("postcode", cuspostcode);
            params.put("subtotal", subtotals);
            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
            params.put("dates", "");
            params.put("time", sharedpreferences.getString("ordertodattime", null));
            params.put("date_string",sharedpreferences.getString("todaytimestring", null));
        } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
            params.put("otype", ordermode);
            params.put("postcode", cuspostcode);
            params.put("subtotal", subtotals);
            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
            params.put("dates", sharedpreferences.getString("orderlaterdate", null));
            params.put("time", sharedpreferences.getString("orderlatertime", null));
            params.put("date_string",sharedpreferences.getString("latertimestring", null));
        }



        fullUrl = menuurlpath + "/menu/custom_details";


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<chechoutvalidate_model> call = apiService.chechoutvalidate(fullUrl, params);

        Log.e("paramsval1", "" + params);
        Log.e("paramsval2", "" + fullUrl);
        call.enqueue(new Callback<chechoutvalidate_model>() {
            @Override
            public void onResponse(Call<chechoutvalidate_model> call, Response<chechoutvalidate_model> response) {
                int statusCode = response.code();

                if (statusCode == 200) {

                    // loader.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        if (ordermode.equalsIgnoreCase("1")) {
                            Intent intent = new Intent(Add_to_Cart.this, Payment_method_Activity.class);
                            intent.putExtra("pay_amount", totalcardamt);
                            intent.putExtra("order_types", "Collection");
                            intent.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
                            intent.putExtra("addressids", addressids);
                            intent.putExtra("customerpostcode", "");
                            startActivity(intent);
                        } else {

                            Intent intent = new Intent(Add_to_Cart.this, Payment_method_Activity.class);
                            intent.putExtra("pay_amount", totalcardamt);
                            intent.putExtra("order_types", "Delivery");
                            intent.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
                            intent.putExtra("addressids", addressids);
                            intent.putExtra("customerpostcode", customerpostcode);



                            startActivity(intent);
                        }
                    } else {



                      /*  bShowSnackbar.performClick();
                        bShowSnackbar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {*/

                        // create an instance of the snackbar
                        final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
                        final Snackbar snackbar = Snackbar.make(coordinatorLayoutView, "", Snackbar.LENGTH_LONG);
                        View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
                        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                        snackbarLayout.setPadding(0, 0, 0, 0);
                        TextView textView1 = customSnackView.findViewById(R.id.textView1);
                        //   textView1.setText(response.body().getMsg());

                        if (response.body().getMsg() != null && !response.body().getMsg().isEmpty()) {

                            textView1.setText(Html.fromHtml(response.body().getMsg()).toString());


                        } else {
                            textView1.setText("INVALID POSTCODE");
                        }
                        snackbarLayout.addView(customSnackView, 0);

                        snackbar.show();
                         /*   }
                        });*/

                        //Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // loader.dismiss();
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<chechoutvalidate_model> call, Throwable t) {

                Log.e("Tro", "" + t);
                // loader.dismiss();
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }


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
                Log.d("responsemsg2", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        if (response.body().getRESPONSE_CODE().equalsIgnoreCase("201")) {
                            first_name = response.body().getData().getFname();
                            last_name = response.body().getData().getLname();
                            cusomer_mobile.setText(response.body().getData().getPhone());

                            if (cusomer_mobile.getText().toString().equalsIgnoreCase("") || cusomer_mobile.getText().toString().isEmpty() || cusomer_mobile.getText().toString() == null) {
                                mobilenumber.setVisibility(View.VISIBLE);
                            } else {
                                mobilenumber.setVisibility(View.GONE);
                            }




                        } else if (response.body().getRESPONSE_CODE().equalsIgnoreCase("202")) {
                            first_name = response.body().getData().getSsologin().getFname();
                            last_name = response.body().getData().getSsologin().getLname();
                            cusomer_mobile.setText(response.body().getData().getSsologin().getPhone());
                            if (cusomer_mobile.getText().toString().equalsIgnoreCase("") || cusomer_mobile.getText().toString().isEmpty() || cusomer_mobile.getText().toString() == null) {
                                mobilenumber.setVisibility(View.VISIBLE);
                            } else {
                                mobilenumber.setVisibility(View.GONE);
                            }


                        } else if (response.body().getRESPONSE_CODE().equalsIgnoreCase("203")) {
                            first_name = response.body().getData().getSsologin().getFname();
                            last_name = response.body().getData().getSsologin().getLname();
                            cusomer_mobile.setText(response.body().getData().getSsologin().getPhone());
                            if (cusomer_mobile.getText().toString().equalsIgnoreCase("") || cusomer_mobile.getText().toString().isEmpty() || cusomer_mobile.getText().toString() == null) {
                                mobilenumber.setVisibility(View.VISIBLE);
                            } else {
                                mobilenumber.setVisibility(View.GONE);
                            }


                        }


                    }
                } else {
                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<get_account_modal> call, Throwable t) {
                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }


    /*---------------------------update account details----------------------------------------------------*/
    //get api values
    private void updateaccountdetails(final String mobiles, String cids, String fnames, String lnames) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("name", fnames);
        params.put("phone", mobiles);
        params.put("lname", lnames);
        params.put("id", cids);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<update_account_modal> call = apiService.updateaccount(params);
        call.enqueue(new Callback<update_account_modal>() {
            @Override
            public void onResponse(Call<update_account_modal> call, Response<update_account_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        getaccountdetails(user_id, login_type_id);

                        Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), response.body().getData(), Snackbar.LENGTH_LONG).show();

                    }
                } else {

                    Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }


            @Override
            public void onFailure(Call<update_account_modal> call, Throwable t) {

                Snackbar.make(Add_to_Cart.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }


    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Add_to_Cart.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loading_layout);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(Add_to_Cart.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);

        //...finaly show it
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
            Intent intent = new Intent(Add_to_Cart.this, Item_Menu_Activity.class);
            intent.putExtra("menuurlpath", sharedpreferences.getString("menuurlpath", null));
            intent.putExtra("cooking_insttruction", cooking_insttruction.getText().toString());
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Add to Card Activity");
    }





}
