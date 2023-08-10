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
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.facebook.AccessTokenTracker;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.fusionkitchen.app.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.GetaddressListAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.address.addAddress_mode;
import com.fusionkitchen.model.address.getaddAddress_mode;
import com.fusionkitchen.model.address.deleteaddress_mode;
import com.fusionkitchen.model.address.getaddressforpostcode_modal;
import com.fusionkitchen.model.post_code_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Address_Book_Activity extends AppCompatActivity {


    private Context mContext = Address_Book_Activity.this;
    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;

    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;

    /*---------------------------XML ID Call----------------------------------------------------*/
    EditText edit_postcode, edit_doorno, edit_town, edit_street;
    AppCompatButton edit_home_button, edit_office_button, edit_other_button;
    AppCompatButton add_new, new_save, update_save;
    LinearLayout address_book, address_add, no_address;
    String address_type, str_postcode_seperate, str_postcode_seperate_str;
    RecyclerView recyclerview_address;
    String addressid, addresscid, addressno, addressaddress1, addressaddress2, addresspcode, addressgtype;
    Dialog addressdeletedialog;

    Dialog dialog, deletedialog, primerdialog, adddialog, updatedialog;

    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, nospace, nospacecheckpost;
    int keyDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_addressbook);


        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);



        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Address_Book_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Address_Book_Activity.ViewDialog alert = new Address_Book_Activity.ViewDialog();
            alert.showDialog(Address_Book_Activity.this);
        }


        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Address_Book_Activity.this);
        getContactsCount();


        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish();

                Intent intent = new Intent(Address_Book_Activity.this, Postcode_Activity.class);

                startActivity(intent);
            }
        });

        /*---------------------------XML ID Call----------------------------------------------------*/
        edit_postcode = findViewById(R.id.edit_postcode);
        edit_doorno = findViewById(R.id.edit_doorno);
        edit_town = findViewById(R.id.edit_town);
        edit_street = findViewById(R.id.edit_street);


        edit_home_button = findViewById(R.id.edit_home_button);
        edit_office_button = findViewById(R.id.edit_office_button);
        edit_other_button = findViewById(R.id.edit_other_button);


        add_new = findViewById(R.id.add_new);
        new_save = findViewById(R.id.new_save);
        update_save = findViewById(R.id.update_save);
        address_book = findViewById(R.id.address_book);
        address_add = findViewById(R.id.address_add);
        no_address = findViewById(R.id.no_address);


        recyclerview_address = findViewById(R.id.recyclerview_address);



        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));


        Log.e("addressusercid", "" + user_id);


        if (user_id != null && !user_id.isEmpty()) {
            get_address(user_id);

        } else {
            address_book.setVisibility(View.GONE);
            address_add.setVisibility(View.GONE);
            no_address.setVisibility(View.VISIBLE);
            add_new.setVisibility(View.VISIBLE);
            new_save.setVisibility(View.GONE);
            update_save.setVisibility(View.GONE);
        }


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
                        Log.e("zipmatches", "" + nospace.matches(regex));
                        if (nospace.matches(regex) == true) {

                            validatezip(nospace);

                        }
                    }
                    if (nospace.length() == 6) {

                        String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
                        Log.e("zipmatches", "" + nospace.matches(regex));
                        if (nospace.matches(regex) == true) {

                            validatezip(nospace);
                        }
                    }
                    if (nospace.length() == 7) {

                        String regex = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";

                        Log.e("zipmatches", "" + nospace.matches(regex));
                        if (nospace.matches(regex) == true) {


                            validatezip(nospace);

                        }
                    }
                } else {
                    keyDel = 0;
                }

            }
        });

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ideuser_ids", "" + user_id);

                if (user_id != null && !user_id.isEmpty()) {

                    address_book.setVisibility(View.GONE);
                    address_add.setVisibility(View.VISIBLE);

                    add_new.setVisibility(View.GONE);
                    new_save.setVisibility(View.VISIBLE);
                    update_save.setVisibility(View.GONE);

                    no_address.setVisibility(View.GONE);

                    edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.txt_one));
                    edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_type));

                    edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                    edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                    edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                    edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                    address_type = "1";

                  /*  address_type = "0";
                    edit_home_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                    edit_home_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                    edit_office_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                    edit_office_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));

                    edit_other_button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_address_notxt));
                    edit_other_button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.card_no_delivery));*/

                    edit_postcode.setText("");
                    edit_doorno.setText("");
                    edit_town.setText("");
                    edit_street.setText("");

                } else {

                    Intent intent = new Intent(Address_Book_Activity.this, Login_Activity.class);
                    intent.putExtra("activity_details", "address");
                    startActivity(intent);


                }

            }
        });

    /*    add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ideuser_ids", "" + user_id);

                if (user_id != null && !user_id.isEmpty()) {

                    address_book.setVisibility(View.GONE);
                    address_add.setVisibility(View.VISIBLE);

                    add_new.setVisibility(View.GONE);
                    new_save.setVisibility(View.VISIBLE);
                    update_save.setVisibility(View.GONE);

                    no_address.setVisibility(View.GONE);

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

                } else {

                    Intent intent = new Intent(Address_Book_Activity.this, Login_Activity.class);
                    intent.putExtra("activity_details", "address");
                    startActivity(intent);


                }

            }
        });*/

        new_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                add_address_validate();

               /* address_book.setVisibility(View.VISIBLE);
                address_add.setVisibility(View.GONE);
                add_new.setVisibility(View.VISIBLE);
                new_save.setVisibility(View.GONE);*/

            }
        });






        /*---------------------------edit address type----------------------------------------------------*/

        edit_home_button.setOnClickListener(new View.OnClickListener() {
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

        edit_office_button.setOnClickListener(new View.OnClickListener() {
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
        edit_other_button.setOnClickListener(new View.OnClickListener() {
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
                        /*Intent intentreview = new Intent(getApplicationContext(), Review.class);
                        startActivity(intentreview);*/
                        // Toast.makeText(getApplicationContext(), "Card", Toast.LENGTH_SHORT).show();

                        if (cursor != 0) {
                            Intent intentcard = new Intent(getApplicationContext(), Add_to_Cart.class);
                            startActivity(intentcard);
                        }else{
                            Toast.makeText(Address_Book_Activity.this,"your cart is Empty!",Toast.LENGTH_SHORT).show();
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










        /* ---------------------add address save------------------*/

    /*    Address_Book_Activity.deleteDialog alert = new Address_Book_Activity.deleteDialog();
        alert.showdeleteDialog(Address_Book_Activity.this);*/




        /*---------------------------addressAdapter item delete----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessagedeletReceiver, new IntentFilter("address_delete-message"));


        /*---------------------------addressAdapter item update----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageupdateReceiver, new IntentFilter("address_update-message"));

        /*---------------------------update primery value----------------------------------------------------*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageupdateprimeryvalue, new IntentFilter("update_primery-message"));

        startService(new Intent(getBaseContext(),MyService.class));
    }


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

    private void getaddressforpostcode(final String post_code) {

        //final ProgressDialog loader = ProgressDialog.show(Address_Book_Activity.this, "", "Loading...", true);
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
                    Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<getaddressforpostcode_modal> call, Throwable t) {

                Log.e("Tro", "" + t);

                // loader.dismiss();
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }

    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Address_Book_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Address_Book_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int_chk = new Internet_connection_checking(Address_Book_Activity.this);
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


            Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), "Please fill out address type field. ", Snackbar.LENGTH_LONG).show();


        } else {
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
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), " Please enter a valid postcode ", Snackbar.LENGTH_LONG).show();

            }


        }

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


            Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), "Please fill out address type field. ", Snackbar.LENGTH_LONG).show();


        } else {
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


                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), " Please enter a valid postcode ", Snackbar.LENGTH_LONG).show();

            }


        }

    }


    /*---------------------------Submit Post Code Check----------------------------------------------------*/
    //get api values
  /*  private void postcodecheck(final String post_code) {
        loadingshow();
        //final ProgressDialog loader = ProgressDialog.show(Address_Book_Activity.this, "", "Loading...", true);
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
                    hideloading();
                    // loader.dismiss();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {


                        add_new_address(edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);
                    } else {
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getPostcode(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    hideloading();
                    // loader.dismiss();
                    Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<post_code_modal> call, Throwable t) {

                Log.e("Tro", "" + t);
                hideloading();
                // loader.dismiss();
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

}*/

    //get api values
  /*  private void updatepostcodecheck(final String post_code) {
        loadingshow();
        //final ProgressDialog loader = ProgressDialog.show(Address_Book_Activity.this, "", "Loading...", true);
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
                    hideloading();
                    // loader.dismiss();
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {


                        update_new_address(addressid, edit_doorno.getText().toString(), edit_street.getText().toString(), edit_town.getText().toString(), edit_postcode.getText().toString(), "1", user_id, address_type);
                    } else {
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getPostcode(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    hideloading();
                    // loader.dismiss();
                    Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<post_code_modal> call, Throwable t) {

                Log.e("Tro", "" + t);
                hideloading();
                // loader.dismiss();
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }*/


    /*  ---------------------------Add address values----------------------------------------------------*/
    private void add_new_address(String str_doorno, String str_street, String str_town, String str_postcode, String str_status, String str_cid, String str_type) {

        loadingshow();

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
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        /* ---------------------add address save------------------*/
                        Address_Book_Activity.addDialog alert = new Address_Book_Activity.addDialog();
                        alert.showaddDialog(Address_Book_Activity.this);

                        new CountDownTimer(3000, 1000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                adddialog.cancel();
                            }
                        }.start();


                        get_address(user_id);
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    } else {
                        hideloading();
                        get_address(user_id);
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    hideloading();
                    Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<addAddress_mode> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*  ---------------------------Update address values----------------------------------------------------*/
    private void update_new_address(String str_id, String str_doorno, String str_street, String str_town, String str_postcode, String str_status, String str_cid, String str_type) {

        loadingshow();

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
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        /* ---------------------add address save------------------*/


                        Address_Book_Activity.ViewthankuDialog alert = new Address_Book_Activity.ViewthankuDialog();
                        alert.shownothankuDialog(Address_Book_Activity.this);

                        new CountDownTimer(3000, 1000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                updatedialog.cancel();
                            }
                        }.start();


                        get_address(user_id);
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    } else {
                        hideloading();
                        get_address(user_id);
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    hideloading();
                    Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<addAddress_mode> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*---------------------------addressAdapter item delete----------------------------------------------------*/
    public BroadcastReceiver mMessagedeletReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            addressid = intent.getStringExtra("addressid");
            addresscid = intent.getStringExtra("addresscid");



            /*---------------------------delete address dialog show----------------------------------------------------*/
            Address_Book_Activity.ViewdeteDialog alert = new Address_Book_Activity.ViewdeteDialog();
            alert.showdeleteDialog(Address_Book_Activity.this);


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


            address_book.setVisibility(View.GONE);
            address_add.setVisibility(View.VISIBLE);

            add_new.setVisibility(View.GONE);
            new_save.setVisibility(View.GONE);
            update_save.setVisibility(View.VISIBLE);
            no_address.setVisibility(View.GONE);


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


            update_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    update_address_validate();


                }
            });


        }
    };


    /*---------------------------update primery value----------------------------------------------------*/
    public BroadcastReceiver mMessageupdateprimeryvalue = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            get_address(user_id);
            /*---------------------primer address save------------------*/
            Address_Book_Activity.primerDialog alert = new Address_Book_Activity.primerDialog();
            alert.showprimerDialogDialog();

            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    primerdialog.cancel();
                }
            }.start();


        }
    };


    /*---------------------------Delived dialog show----------------------------------------------------*/
    public class ViewdeteDialog {
        public void showdeleteDialog(Activity activity) {
            addressdeletedialog = new Dialog(activity, R.style.MaterialDialogSheet);
            addressdeletedialog.setContentView(R.layout.deleted_address);


            AppCompatButton cancel_delete_button = addressdeletedialog.findViewById(R.id.cancel_delete_button);
            AppCompatButton confirm_delete_button = addressdeletedialog.findViewById(R.id.confirm_delete_button);

            ImageView delieved_popup_close = addressdeletedialog.findViewById(R.id.delieved_popup_close);


            cancel_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });
            confirm_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteaddress(addressid, addresscid);
                    addressdeletedialog.cancel();

                }
            });

            cancel_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addressdeletedialog.cancel();

                }
            });


            addressdeletedialog.show();
        }
    }


    /*  ---------------------------Delete address values----------------------------------------------------*/
    private void deleteaddress(String str_addressid, String str_addresscid) {

        loadingshow();

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", str_addressid);
        params.put("cid", str_addresscid);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<deleteaddress_mode> call = apiService.deleteaddress(params);

        call.enqueue(new Callback<deleteaddress_mode>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<deleteaddress_mode> call, Response<deleteaddress_mode> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        get_address(user_id);
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    } else {
                        hideloading();
                        Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    hideloading();
                    Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<deleteaddress_mode> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*  ---------------------------get address values----------------------------------------------------*/
    private void get_address(String usereid) {

        loadingshow();

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", usereid);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<getaddAddress_mode> call = apiService.getaddAddress(params);

        Log.e("addresslogin", "" + params);

        call.enqueue(new Callback<getaddAddress_mode>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<getaddAddress_mode> call, Response<getaddAddress_mode> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();


                if (statusCode == 200) {

                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        if (response.body().getMsg().equalsIgnoreCase("Data not Found")) {
                            address_book.setVisibility(View.GONE);
                            address_add.setVisibility(View.GONE);
                            no_address.setVisibility(View.VISIBLE);
                            add_new.setVisibility(View.VISIBLE);
                            new_save.setVisibility(View.GONE);
                            update_save.setVisibility(View.GONE);
                        } else {

                            address_book.setVisibility(View.VISIBLE);
                            address_add.setVisibility(View.GONE);
                            add_new.setVisibility(View.VISIBLE);
                            new_save.setVisibility(View.GONE);
                            no_address.setVisibility(View.GONE);
                            update_save.setVisibility(View.GONE);

                            List<getaddAddress_mode.userdetail> jobdetails = (response.body().getUserdetail());
                            GetaddressListAdapter adapter = new GetaddressListAdapter(mContext, (List<getaddAddress_mode.userdetail>) jobdetails);
                            recyclerview_address.setHasFixedSize(true);
                            recyclerview_address.setLayoutManager(new LinearLayoutManager(Address_Book_Activity.this));
                            recyclerview_address.setAdapter(adapter);


                        }

                    } else {
                        hideloading();

                        if (response.body().getMsg().equalsIgnoreCase("Invalid user details")) {
                            address_book.setVisibility(View.GONE);
                            no_address.setVisibility(View.VISIBLE);
                            Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                        }


                    }

                } else {
                    hideloading();
                    Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<getaddAddress_mode> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Address_Book_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*---------------------------primer set address----------------------------------------------------*/


    public class primerDialog {
        public void showprimerDialogDialog() {
            primerdialog = new Dialog(Address_Book_Activity.this);
            primerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            primerdialog.setCancelable(false);
            primerdialog.setContentView(R.layout.thank_you);

            TextView alertTextView = primerdialog.findViewById(R.id.text_dialog1);
            TextView alertTextViewtwo = primerdialog.findViewById(R.id.text_dialog);
            alertTextView.setText("Primary Address");
            alertTextViewtwo.setText("Your primary address has been updated successfully.");
            primerdialog.show();
        }
    }

    /* ---------------------add address save------------------*/

    public class addDialog {
        public void showaddDialog(Activity activity) {
            adddialog = new Dialog(activity);
            adddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            adddialog.setCancelable(false);
            adddialog.setContentView(R.layout.thank_you);

            TextView alertTextView = adddialog.findViewById(R.id.text_dialog1);
            TextView alertTextViewtwo = adddialog.findViewById(R.id.text_dialog);
            alertTextView.setText("New Address");
            alertTextViewtwo.setText("Your new address has been added successfully.");
            adddialog.show();
        }
    }

    /*---------------------------Edit address----------------------------------------------------*/
    public class ViewthankuDialog {
        public void shownothankuDialog(Activity activity) {
            updatedialog = new Dialog(activity);
            updatedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            updatedialog.setCancelable(false);
            updatedialog.setContentView(R.layout.thank_you);

            TextView alertTextView = updatedialog.findViewById(R.id.text_dialog1);
            TextView alertTextViewtwo = updatedialog.findViewById(R.id.text_dialog);
            alertTextView.setText("Edit Address");
            alertTextViewtwo.setText("Your address has been updated successfully.");
            updatedialog.show();
        }

    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Address_Book_Activity.this);
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

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Address Book Activity");
    }
}
