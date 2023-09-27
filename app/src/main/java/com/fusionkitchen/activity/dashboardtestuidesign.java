package com.fusionkitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fusionkitchen.R;
import com.fusionkitchen.adapter.DashboardBannerAutoScrollAdapter;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class dashboardtestuidesign extends AppCompatActivity {

    private ViewPager2 viewPager;
    private DashboardBannerAutoScrollAdapter dashboardBannerAutoScrollAdapter;
    private List<String> imageUrls;
    private int currentPage = 0;
    LinearLayout currentLocationDetails,searchRestaurantCuisine,searchIconCusion;
    LinearLayout filterListCategory;
    Dialog currentlocationpopup,filtercategoryList,preOrderPopUp;
    RecyclerView cusionListView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardtestuidesign);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(dashboardtestuidesign.this, R.color.status_bar_color));



        viewPager = findViewById(R.id.viewPager);
        currentLocationDetails = findViewById(R.id.currentLocationDetails);
        searchRestaurantCuisine = findViewById(R.id.searchRestaurantCuisine);
        searchIconCusion = findViewById(R.id.searchIconCusion);
        cusionListView = findViewById(R.id.cusionListView);
        filterListCategory = findViewById(R.id.filterListCategory);

        filterListCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filtercategoryList= new Dialog(dashboardtestuidesign.this);
                filtercategoryList.requestWindowFeature(Window.FEATURE_NO_TITLE);
                filtercategoryList.setContentView(R.layout.filter_category_list);



                filtercategoryList.show();
                filtercategoryList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                filtercategoryList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                filtercategoryList.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                filtercategoryList.getWindow().setGravity(Gravity.BOTTOM);

            }
        });

        searchIconCusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchIconCusion.getVisibility() == View.VISIBLE){

                    searchIconCusion.setVisibility(View.GONE);
                    cusionListView.setVisibility(View.GONE);
                    searchRestaurantCuisine.setVisibility(View.VISIBLE);

                }
            }
        });


        currentLocationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 preOrderPopup();
                // changePostCodeLocation();

            }
        });


        imageUrls = new ArrayList<>();

        imageUrls.add("https://fusion-crm.s3.eu-west-2.amazonaws.com/partnerapp/banner/16910563045337.png");
        imageUrls.add("https://fusion-crm.s3.eu-west-2.amazonaws.com/partnerapp/banner/16910563045337.png");
        imageUrls.add("https://fusion-crm.s3.eu-west-2.amazonaws.com/partnerapp/banner/16910568692303.png");
        // Add more image URLs as needed

        dashboardBannerAutoScrollAdapter = new DashboardBannerAutoScrollAdapter(this, imageUrls);
        viewPager.setAdapter(dashboardBannerAutoScrollAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == imageUrls.size()) {
                            currentPage = 0;
                        }
                        viewPager.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, 3000, 3000);



    }

    private void preOrderPopup() {

        preOrderPopUp = new Dialog(dashboardtestuidesign.this);
        preOrderPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        preOrderPopUp.setContentView(R.layout.pre_order_design);


            TextView changeAddressTxt = preOrderPopUp.findViewById(R.id.changeAddressTxt);
            RadioGroup deliveryPickupRadioGroup = preOrderPopUp.findViewById(R.id.deliveryPickupRadioGroup);
            RadioButton radioDelivery = preOrderPopUp.findViewById(R.id.radioDelivery);
            RadioButton radioPickUp = preOrderPopUp.findViewById(R.id.radioPickUp);
            TextView confirmBtn = preOrderPopUp.findViewById(R.id.confirmBtn);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
                    for (int i = 0; i < 7; i++) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.add(Calendar.DATE, i);
                        String day = sdf.format(calendar.getTime());
                        Log.d("kjfnbkfnvkjfnvkdfvk", day);
                    }
                }
            });

            deliveryPickupRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioDelivery:

                        Drawable radiodeliverychecked = getResources().getDrawable(R.drawable.fi_preorder_delivery_icon);
                        radiodeliverychecked.setBounds(0, 0, 40, 40);
                        radioDelivery.setCompoundDrawables(radiodeliverychecked, null, null, null);


                        Drawable radiopickunchecked = getResources().getDrawable(R.drawable.fi_pickup_icon);
                        radiopickunchecked.setBounds(0, 0, 40, 40);
                        radioPickUp.setCompoundDrawables(radiopickunchecked, null, null, null);

                        break;

                    case R.id.radioPickUp:

                        Drawable radiopickchecked = getResources().getDrawable(R.drawable.pickup_checked_icon);
                        radiopickchecked.setBounds(0, 0, 40, 40);
                        radioPickUp.setCompoundDrawables(radiopickchecked, null, null, null);


                        Drawable radiodeliveryunchecked = getResources().getDrawable(R.drawable.bike_unchecked_icon);
                        radiodeliveryunchecked.setBounds(0, 0, 40, 40);
                        radioDelivery.setCompoundDrawables(radiodeliveryunchecked, null, null, null);

                        break;
                }
            }
        });

            changeAddressTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePostCodeLocation();
                }
            });


        preOrderPopUp.show();
        preOrderPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        preOrderPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        preOrderPopUp.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        preOrderPopUp.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void changePostCodeLocation() {

        currentlocationpopup= new Dialog(dashboardtestuidesign.this);
        currentlocationpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        currentlocationpopup.setContentView(R.layout.current_location_popup);

        LinearLayout addAddressCurrentLocation = currentlocationpopup.findViewById(R.id.addAddressCurrentLocation);
        LinearLayout manual_search_icon = currentlocationpopup.findViewById(R.id.manual_search_icon);
        addAddressCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(manual_search_icon.getVisibility() == View.VISIBLE){
                    manual_search_icon.setVisibility(View.GONE);
                }else{
                    manual_search_icon.setVisibility(View.VISIBLE);
                }

            }
        });


        currentlocationpopup.show();
        currentlocationpopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        currentlocationpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        currentlocationpopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        currentlocationpopup.getWindow().setGravity(Gravity.CENTER);


    }
}