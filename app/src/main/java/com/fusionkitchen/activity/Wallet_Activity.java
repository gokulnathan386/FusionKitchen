package com.fusionkitchen.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.DashboardSearchResultList;
import com.fusionkitchen.adapter.wallethistoryadapter;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.home_model.location_type_modal;
import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.model.wallet.get_wallet_amount;
import com.fusionkitchen.model.wallet.wallet_transaction_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wallet_Activity extends AppCompatActivity {

    /*-------------------Back Button Click--------------------*/
    RelativeLayout clikback;
    /*-------------------set wallet amount--------------------*/
    TextView wallet_amt;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    String user_id;
    /*--------------Wallet history show------------------*/
    RecyclerView wallethistoryshow;
    TextView wallet_no_history;
    /*-------------------see all wallet history--------------------*/
    TextView wallet_see_all, wallet_hide_all;
    CardView cv_one_login;
    RelativeLayout rl_two_login;
    private long mLastClickTime = 0;

    /*-------------------Loading Show--------------------*/
    private Dialog dialog;

    /*-------------------On click for layout--------------------*/
    LinearLayout terms_layout, save_card_layout;
    ImageView wallet_notification;

    private Context mContext = Wallet_Activity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_view_wallet);


        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);


        clikback = findViewById(R.id.clikback);
        wallet_amt = findViewById(R.id.wallet_amt);
        wallethistoryshow = findViewById(R.id.wallethistoryshow);
        wallet_see_all = findViewById(R.id.wallet_see_all);
        wallet_hide_all = findViewById(R.id.wallet_hide_all);

        cv_one_login = findViewById(R.id.cv_one_login);
        rl_two_login = findViewById(R.id.rl_two_login);
        wallet_no_history = findViewById(R.id.wallet_no_history);
        terms_layout = findViewById(R.id.terms_layout);
        save_card_layout = findViewById(R.id.save_card_layout);
        wallet_notification = findViewById(R.id.wallet_notification);


        /*-------------------On click for layout--------------------*/

        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intenthome = new Intent(getApplicationContext(), Wallet_terms_Activity.class);
                startActivity(intenthome);
            }
        });

        save_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intenthome = new Intent(getApplicationContext(), Show_Save_Card_Activity.class);
                startActivity(intenthome);
            }
        });


        wallet_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intenthome = new Intent(getApplicationContext(), Wallet_Notification_Activity.class);
                startActivity(intenthome);
            }
        });


        /*-------------------Back Button Click--------------------*/
        clikback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intenthome = new Intent(getApplicationContext(), Postcode_Activity.class);
                startActivity(intenthome);
            }
        });


        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        Log.e("user_walletpage_ids", "" + user_id);

        /*-------------------set wallet amount--------------------*/
        getwalletamount(user_id);
        /*--------------Wallet history show------------------*/
        getwallethistory(user_id, "1");
        /*-------------------see all wallet history--------------------*/

        wallet_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallet_hide_all.setVisibility(View.VISIBLE);
                wallet_see_all.setVisibility(View.GONE);

                cv_one_login.setVisibility(View.GONE);
                rl_two_login.setVisibility(View.GONE);


                getwallethistory(user_id, "2");


            }
        });
        wallet_hide_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallet_hide_all.setVisibility(View.GONE);
                wallet_see_all.setVisibility(View.VISIBLE);
                cv_one_login.setVisibility(View.VISIBLE);
                rl_two_login.setVisibility(View.VISIBLE);
                getwallethistory(user_id, "1");


            }
        });
    }


    /*-------------------set wallet amount--------------------*/
    private void getwalletamount(String userid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", userid);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<get_wallet_amount> call = apiService.getwalletamt(params);


        Log.e("params", "" + params);
        call.enqueue(new Callback<get_wallet_amount>() {
            @Override
            public void onResponse(Call<get_wallet_amount> call, Response<get_wallet_amount> response) {
                //  response.headers().get("Set-Cookie");
                int statusCode = response.code();

                Log.e("dashbord", "" + statusCode);

                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        wallet_amt.setText("£ " + response.body().getData().getAmount());


                    } else {
                        wallet_amt.setText("£ 00.00");
                        Snackbar.make(Wallet_Activity.this.findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    wallet_amt.setText("£ 00.00");
                    Snackbar.make(Wallet_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<get_wallet_amount> call, Throwable t) {

                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Wallet_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }

        });

    }

    /*--------------Wallet history show------------------*/
    private void getwallethistory(String userid, String listviewlenth) {
        loadingshow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", userid);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<wallet_transaction_model> call = apiService.wallettransaction(params);
        Log.e("params", "" + params);
        call.enqueue(new Callback<wallet_transaction_model>() {
            @Override
            public void onResponse(Call<wallet_transaction_model> call, Response<wallet_transaction_model> response) {
                //  response.headers().get("Set-Cookie");
                int statusCode = response.code();
                Log.e("dashbord", "" + statusCode);
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        hideloading();
                        wallet_no_history.setVisibility(View.GONE);
                        wallethistoryshow.setVisibility(View.VISIBLE);
                        List<wallet_transaction_model.tranferdetails> wallethistory = (response.body().getData());
                        wallethistoryadapter adapter = new wallethistoryadapter(mContext,(List<wallet_transaction_model.tranferdetails>) wallethistory, listviewlenth);
                        wallethistoryshow.setHasFixedSize(true);
                        wallethistoryshow.setLayoutManager(new LinearLayoutManager(Wallet_Activity.this));
                        wallethistoryshow.setAdapter(adapter);
                    } else {
                        hideloading();
                        wallet_no_history.setVisibility(View.VISIBLE);
                        wallethistoryshow.setVisibility(View.GONE);
                        Snackbar.make(Wallet_Activity.this.findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    wallet_no_history.setVisibility(View.VISIBLE);
                    wallethistoryshow.setVisibility(View.GONE);
                    Snackbar.make(Wallet_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<wallet_transaction_model> call, Throwable t) {
                hideloading();
                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Wallet_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });

    }


    /*-------------------Loading Show------------------*/
    public void loadingshow() {
        dialog = new Dialog(Wallet_Activity.this);
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
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            Intent intent = new Intent(Wallet_Activity.this, Postcode_Activity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Wallet Activity");
    }
}
