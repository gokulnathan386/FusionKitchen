package com.fusionkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fusionkitchen.R;
import com.fusionkitchen.app.MyApplication;


public class Wallet_Notification_Activity extends AppCompatActivity {


    /*-------------------Back Button Click--------------------*/
    RelativeLayout clikback;
    private long mLastClickTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_wallet_notification);





        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);


        clikback = findViewById(R.id.clikback);



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
        startService(new Intent(getBaseContext(),MyService.class));
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            Intent intent = new Intent(Wallet_Notification_Activity.this, Wallet_Activity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Postcode Activity");
    }

}