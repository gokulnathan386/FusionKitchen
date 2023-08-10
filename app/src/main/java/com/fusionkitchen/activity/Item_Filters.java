package com.fusionkitchen.activity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.R;
import com.fusionkitchen.app.MyApplication;


public class Item_Filters extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    /*---------------------------Back Button Click----------------------------------------------------*/
    private ImageView back;
    private AppCompatButton ratingACB, deliveryACB, tablebookingACB, preorderACB, resetallfiltersACB, applyallfiltersACB;
    private SwitchCompat specialofferSW, collectionsSW, opennowSW, preorderSW;
    private TextView sortreset, filterreset, cuisinereset;
    private RecyclerView cusineRV;
    private String sortstring, filterstring = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_item_filters);





        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        overridePendingTransition(R.anim.enter, R.anim.exit);

        initview();

        startService(new Intent(getBaseContext(),MyService.class));
    }


    private void initview() {
        cusineRV = findViewById(R.id.cusion_list_view);
// -------------------------------------------inittextview----------------------------------------------
        sortreset = findViewById(R.id.reset_sort);
        filterreset = findViewById(R.id.reset_filters);
        cuisinereset = findViewById(R.id.reset_cusine);
// -------------------------initimageview---------------------------------------------------------
        back = findViewById(R.id.back);
//  ----------------------------Appcompactbuttonviewinit---------------------------------------------------
        ratingACB = findViewById(R.id.rating_ACB);
        deliveryACB = findViewById(R.id.delivery_ACB);
        tablebookingACB = findViewById(R.id.tablebooking_ACB);
        preorderACB = findViewById(R.id.preorder_ACB);
        applyallfiltersACB = findViewById(R.id.apply_filter_button);
        resetallfiltersACB = findViewById(R.id.reset_filter_button);
//  ----------------------------switchbuttonviewinit---------------------------------------------------
        specialofferSW = findViewById(R.id.specialofferSW);
        collectionsSW = findViewById(R.id.collectionSW);
        opennowSW = findViewById(R.id.opennowSW);
        preorderSW = findViewById(R.id.preorderSW);
//      --------------------switch CheckedChangeListener for all components---------------------------------------------
        specialofferSW.setOnCheckedChangeListener(this);
        collectionsSW.setOnCheckedChangeListener(this);
        opennowSW.setOnCheckedChangeListener(this);
        preorderSW.setOnCheckedChangeListener(this);
//   --------------------------CLick listener for all components---------------------------------------------
        back.setOnClickListener(this);
        ratingACB.setOnClickListener(this);
        deliveryACB.setOnClickListener(this);
        tablebookingACB.setOnClickListener(this);
        preorderACB.setOnClickListener(this);
        applyallfiltersACB.setOnClickListener(this);
        resetallfiltersACB.setOnClickListener(this);
        sortreset.setOnClickListener(this);
        cuisinereset.setOnClickListener(this);
        filterreset.setOnClickListener(this);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rating_ACB:
                changeonthemeofbutton(ratingACB);
                changeoffthemeofbutton(deliveryACB);
                changeoffthemeofbutton(tablebookingACB);
                changeoffthemeofbutton(preorderACB);
                sortstring = ratingACB.getText().toString();
                Log.v("sortstring", sortstring);
                break;
            case R.id.delivery_ACB:
                changeonthemeofbutton(deliveryACB);
                changeoffthemeofbutton(ratingACB);
                changeoffthemeofbutton(tablebookingACB);
                changeoffthemeofbutton(preorderACB);
                sortstring = deliveryACB.getText().toString();
                Log.v("sortstring", sortstring);
                break;
            case R.id.tablebooking_ACB:
                changeonthemeofbutton(tablebookingACB);
                changeoffthemeofbutton(deliveryACB);
                changeoffthemeofbutton(ratingACB);
                changeoffthemeofbutton(preorderACB);
                sortstring = tablebookingACB.getText().toString();
                Log.v("sortstring", sortstring);
                break;
            case R.id.preorder_ACB:
                changeonthemeofbutton(preorderACB);
                changeoffthemeofbutton(deliveryACB);
                changeoffthemeofbutton(tablebookingACB);
                changeoffthemeofbutton(ratingACB);
                sortstring = preorderACB.getText().toString();
                Log.v("sortstring", sortstring);
                break;
            case R.id.apply_filter_button:
                initwebservice();
                Log.v("swichcase", "applyallfilters_button");
                break;
            case R.id.reset_filter_button:
                Log.v("swichcase", "resetall_filters");
                break;
            case R.id.reset_sort:
                Log.v("swichcase", "reset_sort");
                break;
            case R.id.reset_cusine:
                Log.v("swichcase", "reset_cusine");
                break;
            case R.id.reset_filters:
                Log.v("swichcase", "reset_filters");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.specialofferSW:
                if (String.valueOf(isChecked).contains("true")) {
                    String specialofferstring = " Special Offer ";
                    filterstring = filterstring + specialofferstring;
                } else {
                    filterstring = filterstring.replace(",Special Offer", "");
                    filterstring = filterstring.replace(" Special Offer", "");

                }
                filterstring = filterstring.replace("  ", ",");
                Log.v("filterstring", filterstring);
                break;
            case R.id.collectionSW:
                String collectionst = " Collection ";
                if (String.valueOf(isChecked).contains("true")) {
                    filterstring = filterstring + collectionst;
                } else {
                    filterstring = filterstring.replace(",Collection", "");
                    filterstring = filterstring.replace("Collection", "");
                }
                filterstring = filterstring.replace("  ", ",");
                Log.v("filterstring", filterstring);
                break;
            case R.id.opennowSW:
                String opennowst = " Open Now ";
                if (String.valueOf(isChecked).contains("true")) {
                    filterstring = filterstring + opennowst;
                } else {
                    filterstring = filterstring.replace(",Open Now", "");
                    filterstring = filterstring.replace("Open Now", "");
                }
                filterstring = filterstring.replace("  ", ",");
                Log.v("filterstring", filterstring);
                break;
            case R.id.preorderSW:
                String preorder = " Pre Order ";
                if (String.valueOf(isChecked).contains("true")) {
                    filterstring = filterstring + preorder;
                } else {
                    filterstring = filterstring.replace(",Pre Order", "");
                    filterstring = filterstring.replace("Pre Order", "");
                }
                filterstring = filterstring.replace("  ", ",");
                Log.v("filterstring", String.valueOf(isChecked));
                break;
        }
    }

    //-------------------------------------implemement METHODS-----------------------------------------------
    public void onClickCalled(String anyValue) {
        // Call another acitivty here and pass some arguments to it.

    }

    private void changeonthemeofbutton(AppCompatButton appCompatButton) {
        appCompatButton.setBackgroundColor(appCompatButton.getContext().getResources().getColor(R.color.welcome_button_color));
        appCompatButton.setTextColor(Color.WHITE);
    }

    private void changeoffthemeofbutton(AppCompatButton appCompatButton) {
        appCompatButton.setBackgroundColor(appCompatButton.getContext().getResources().getColor(R.color.filter_buton_bg));
        appCompatButton.setTextColor(Color.BLACK);
    }

    private void initwebservice() {
        //API call set here
    }


    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Filter Activity");
    }
}
