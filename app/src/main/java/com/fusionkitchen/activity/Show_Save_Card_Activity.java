package com.fusionkitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.R;
import com.fusionkitchen.adapter.MenuOfferAdapter;
import com.fusionkitchen.adapter.viewcardAdapter;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.Savecard.viewsavecard_details_model;
import com.fusionkitchen.model.wallet.get_wallet_amount;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Show_Save_Card_Activity extends AppCompatActivity {


    public Context mContext = Show_Save_Card_Activity.this;
    /*-------------------Back Button Click--------------------*/
    RelativeLayout clikback;


    LinearLayout no_save_card;

    private long mLastClickTime = 0;

    RecyclerView view_save_card;
    TextView wallet_amt, save_title;
    AppCompatButton card_pay_button;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    String user_id;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_show_save_card);





        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);


        view_save_card = findViewById(R.id.view_save_card);
        wallet_amt = findViewById(R.id.wallet_amt);
        clikback = findViewById(R.id.clikback);
        wallet_amt = findViewById(R.id.wallet_amt);
        no_save_card = findViewById(R.id.no_save_card);
        save_title = findViewById(R.id.save_title);
        card_pay_button = findViewById(R.id.card_pay_button);





        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        Log.e("user_walletpage_ids", "" + user_id);



        /*-------------------Back Button Click--------------------*/
        clikback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intenthome = new Intent(getApplicationContext(), Wallet_Activity.class);
                startActivity(intenthome);
            }
        });

        card_pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intenthome = new Intent(getApplicationContext(), Add_new_savecard_Activity.class);
                startActivity(intenthome);
            }
        });

        /*-------------------set wallet amount--------------------*/
        getwalletamount(user_id);
        /*-------------------View save Crd--------------------*/
        viewsavecard(user_id);
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
                        Snackbar.make(Show_Save_Card_Activity.this.findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    wallet_amt.setText("£ 00.00");
                    Snackbar.make(Show_Save_Card_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<get_wallet_amount> call, Throwable t) {

                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Show_Save_Card_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }

        });

    }


    /*-------------------View save Crd--------------------*/
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
                            save_title.setVisibility(View.GONE);
                            view_save_card.setVisibility(View.GONE);
                            no_save_card.setVisibility(View.VISIBLE);


                        } else {
                            save_title.setVisibility(View.VISIBLE);
                            view_save_card.setVisibility(View.VISIBLE);
                            no_save_card.setVisibility(View.GONE);


                            viewcardAdapter cardadapter = new viewcardAdapter(mContext, response.body().getData());
                            view_save_card.setHasFixedSize(true);
                            view_save_card.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                            view_save_card.setItemAnimator(new DefaultItemAnimator());
                            view_save_card.setAdapter(cardadapter);
                        }


                    } else {
                        save_title.setVisibility(View.GONE);
                        view_save_card.setVisibility(View.GONE);
                        no_save_card.setVisibility(View.VISIBLE);
                        Snackbar.make(Show_Save_Card_Activity.this.findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    save_title.setVisibility(View.GONE);
                    view_save_card.setVisibility(View.GONE);
                    no_save_card.setVisibility(View.VISIBLE);
                    Snackbar.make(Show_Save_Card_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<viewsavecard_details_model> call, Throwable t) {
                save_title.setVisibility(View.GONE);
                view_save_card.setVisibility(View.GONE);
                no_save_card.setVisibility(View.VISIBLE);
                Log.e("dasboarderror", "location type : " + t);
                Snackbar.make(Show_Save_Card_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

            }

        });

    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            startActivity(new Intent(getApplicationContext(), Wallet_Activity.class));
            return true;
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("View Save Card Activity");
    }

}
