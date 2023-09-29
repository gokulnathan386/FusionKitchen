package com.fusionkitchen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fusionkitchen.R;
import com.fusionkitchen.adapter.DashboardBannerAutoScrollAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Dashboard_List_Activity extends AppCompatActivity {

    private ViewPager2 viewPager;
    TextView postCodeAddress;
    FusedLocationProviderClient fusedLocationProviderClient;
    private DashboardBannerAutoScrollAdapter dashboardBannerAutoScrollAdapter;
    private List<String> imageUrls;
    private int currentPage = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private final static int REQUEST_CODE = 100;
    LinearLayout currentLocationDetails,searchRestaurantCuisine,searchIconCusion;
    LinearLayout filterListCategory;
    Dialog currentlocationpopup,filtercategoryList,preOrderPopUp;
    RecyclerView cusionListView,mostPopularLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_list);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(Dashboard_List_Activity.this, R.color.status_bar_color));

        viewPager = findViewById(R.id.viewPager);
        currentLocationDetails = findViewById(R.id.currentLocationDetails);
        searchRestaurantCuisine = findViewById(R.id.searchRestaurantCuisine);
        searchIconCusion = findViewById(R.id.searchIconCusion);
        cusionListView = findViewById(R.id.cusionListView);
        filterListCategory = findViewById(R.id.filterListCategory);
        mostPopularLayout = findViewById(R.id.mostPopularLayout);


        filterListCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filtercategoryList= new Dialog(Dashboard_List_Activity.this);
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

        preOrderPopUp = new Dialog(Dashboard_List_Activity.this);
        preOrderPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        preOrderPopUp.setContentView(R.layout.pre_order_design);

        TextView changeAddressTxt = preOrderPopUp.findViewById(R.id.changeAddressTxt);
        RadioGroup deliveryPickupRadioGroup = preOrderPopUp.findViewById(R.id.deliveryPickupRadioGroup);
        RadioButton radioDelivery = preOrderPopUp.findViewById(R.id.radioDelivery);
        RadioButton radioPickUp = preOrderPopUp.findViewById(R.id.radioPickUp);
        TextView confirmBtn = preOrderPopUp.findViewById(R.id.confirmBtn);
        Spinner sevenDays =preOrderPopUp.findViewById(R.id.sevenDays);
        Spinner hoursMin =preOrderPopUp.findViewById(R.id.hoursMin);
        postCodeAddress =preOrderPopUp.findViewById(R.id.postCodeAddress);

        LinearLayout amPmTxt = preOrderPopUp.findViewById(R.id.amPmTxt);
        TextView sessionAMPM = preOrderPopUp.findViewById(R.id.sessionAMPM);
        amPmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionAMPM.getText().toString().equalsIgnoreCase("AM")){
                    sessionAMPM.setText("PM");
                }else{
                    sessionAMPM.setText("AM");
                }

            }
        });



        ArrayList<String> dateDay = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        for (int i = 0; i < 7; i++) {

            if(i==0){
                dateDay.add("Today");
            }else if(i==1){
                dateDay.add("Tomorrow");
            }else{
                Calendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, i);
                String day = sdf.format(calendar.getTime());
                dateDay.add(day);
            }


        }



            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.my_selected_item, dateDay);
            dataAdapter.setDropDownViewResource(R.layout.my_dropdown_item);

            sevenDays.setAdapter(dataAdapter);
            sevenDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item_position = String.valueOf(id);
                    int positonInt = Integer.valueOf(item_position);
                    String hrs = dateDay.get(positonInt);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        ArrayList<String> hoursmin = new ArrayList<String>();
        //hours.add("hrs");
        for(int i=1;i<=12;i++){

            int num = 15;
            for(int j = 1; j <= 4; j++) {

                if(i<=9){

                    if(j==4){

                        int addplusone = i + 1;

                        if(addplusone == 10){
                            hoursmin.add(addplusone +" : " +"00");
                        }else{
                            hoursmin.add("0"+ addplusone +" : " +"00");
                        }

                    }else{

                        hoursmin.add("0"+ i +" : " +num * j);

                    }

                }else{

                    if(j==4){

                        int addplusone = i + 1;

                        if(addplusone == 13){
                            hoursmin.add(0, "01"+" : " + "00");
                        }else{
                            hoursmin.add(addplusone +" : " + "00");
                        }

                    }else{

                        hoursmin.add(i +" : " + num * j);
                    }

                }

            }

        }


        ArrayAdapter<String> hoursMinAdapter = new ArrayAdapter<String>(this,R.layout.my_selected_item, hoursmin);
        hoursMinAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        hoursMin.setAdapter(hoursMinAdapter);
        hoursMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_position = String.valueOf(id);
                int positonInt = Integer.valueOf(item_position);
                String im = hoursmin.get(positonInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

        currentlocationpopup= new Dialog(Dashboard_List_Activity.this);
        currentlocationpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        currentlocationpopup.setContentView(R.layout.current_location_popup);

        LinearLayout addAddressCurrentLocation = currentlocationpopup.findViewById(R.id.addAddressCurrentLocation);
        LinearLayout manual_search_icon = currentlocationpopup.findViewById(R.id.manual_search_icon);
        LinearLayout myCurrentLocation = currentlocationpopup.findViewById(R.id.myCurrentLocation);
        EditText manuallyEnterPostCode = currentlocationpopup.findViewById(R.id.manuallyEnterPostCode);
        LinearLayout post_code_check = currentlocationpopup.findViewById(R.id.post_code_check);

        post_code_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCodeAddress.setText(manuallyEnterPostCode.getText().toString());
                currentlocationpopup.dismiss();
            }
        });


        myCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

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

    private void getLastLocation(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> task =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.d("asbjdvhgsavdhvsadhgvc", "Location Enable");
                    if (fusedLocationProviderClient == null) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Dashboard_List_Activity.this);
                    }

                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        try {
                                            Geocoder geocoder = new Geocoder(Dashboard_List_Activity.this, Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            Log.d("LocationAddress", "Lattitude --->" + addresses.get(0).getLatitude());
                                            Log.d("LocationAddress", "Longitude --->" + addresses.get(0).getLongitude());
                                            Log.d("LocationAddress", "Address --->" + addresses.get(0).getAddressLine(0));
                                            Log.d("LocationAddress", "City --->" + addresses.get(0).getLocality());
                                            Log.d("LocationAddress", "Country --->" + addresses.get(0).getCountryName());
                                            Log.d("LocationAddress", "Postcode --->" +addresses.get(0).getPostalCode());
                                            postCodeAddress.setText(addresses.get(0).getPostalCode());
                                            currentlocationpopup.dismiss();


                                        } catch (IOException e) {
                                            Log.d("LocationExceptionHandle", "Lattitude --->" + e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(Dashboard_List_Activity.this, "Location not available.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("LocationError", "Error getting location: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            });


                }
            });

            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(Dashboard_List_Activity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied, and there's no way to fix the issue
                            break;
                    }
                }
            });


            /*------------End Location Update------------------*/

        } else {
            askPermission();
        }


    }

    private void askPermission() {

        ActivityCompat.requestPermissions(Dashboard_List_Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getLastLocation();

            }else {

                if (isGPSEnabled()) {  // Check GPS Location Enable Or Disable

                    Dialog deletepopup = new Dialog(Dashboard_List_Activity.this);
                    deletepopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deletepopup.setContentView(R.layout.permission_popup);

                    AppCompatButton deletedBtn = deletepopup.findViewById(R.id.deletedBtn);
                    AppCompatButton cancelBtn = deletepopup.findViewById(R.id.cancelBtn);
                    TextView textHeadline = deletepopup.findViewById(R.id.textHeadline);
                    textHeadline.setText("Do you want to Enable App Location Permission?");

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletepopup.dismiss();
                        }
                    });

                    deletedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            deletepopup.dismiss();

                        }
                    });

                    deletepopup.getWindow().setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    deletepopup.show();
                    deletepopup.setCancelable(false);
                    deletepopup.setCanceledOnTouchOutside(false);
                    deletepopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    deletepopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    deletepopup.getWindow().setGravity(Gravity.CENTER);






                }else{
                    Dialog deletepopup = new Dialog(Dashboard_List_Activity.this);
                    deletepopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deletepopup.setContentView(R.layout.permission_popup);

                    AppCompatButton deletedBtn = deletepopup.findViewById(R.id.deletedBtn);
                    AppCompatButton cancelBtn = deletepopup.findViewById(R.id.cancelBtn);
                    TextView textHeadline = deletepopup.findViewById(R.id.textHeadline);
                    textHeadline.setText("Do you want to Enable App Location Permission?");

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletepopup.dismiss();
                        }
                    });

                    deletedBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Intent app_intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(app_intent);
                            Toast.makeText(Dashboard_List_Activity.this,"Please enable GPS location",Toast.LENGTH_SHORT).show();
                            deletepopup.dismiss();

                        }
                    });

                    deletepopup.getWindow().setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    deletepopup.show();
                    deletepopup.setCancelable(false);
                    deletepopup.setCanceledOnTouchOutside(false);
                    deletepopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    deletepopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    deletepopup.getWindow().setGravity(Gravity.CENTER);

                }

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


}