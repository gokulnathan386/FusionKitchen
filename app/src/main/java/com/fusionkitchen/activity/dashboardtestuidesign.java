package com.fusionkitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.fusionkitchen.R;
import com.fusionkitchen.adapter.DashboardBannerAutoScrollAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class dashboardtestuidesign extends AppCompatActivity {

    private ViewPager2 viewPager;
    private DashboardBannerAutoScrollAdapter dashboardBannerAutoScrollAdapter;
    private List<String> imageUrls;
    private int currentPage = 0;
    LinearLayout currentLocationDetails,searchRestaurantCuisine,searchIconCusion;
    Dialog currentlocationpopup;
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
}