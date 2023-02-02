package com.fusionkitchen.activity;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.airbnb.lottie.L;
import com.fusionkitchen.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class Splash_Activity extends AppCompatActivity {

    Handler handler;
    SharedPreferences offer_splash,slogin;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(Splash_Activity.this, R.color.white));


        offer_splash = getSharedPreferences("Offer_splash_popup", MODE_PRIVATE);
        SharedPreferences.Editor splash_popup = offer_splash.edit();
        splash_popup.putBoolean("offervalue",true);
        splash_popup.commit();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
                user_id = (slogin.getString("login_key_cid", null));

                Intent intent = new Intent(Splash_Activity.this, Welcome_Activity.class);
                startActivity(intent);
                finish();

             }
        }, 1800);

    }

}
