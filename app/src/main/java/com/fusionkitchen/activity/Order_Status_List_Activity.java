package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
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
import com.fusionkitchen.adapter.OrderHistoryListAdapter;
import com.fusionkitchen.adapter.OrderstatusListAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.model.orderstatus.orderstatus_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Order_Status_List_Activity extends AppCompatActivity {

    private Context mContext = Order_Status_List_Activity.this;


    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;
    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;
    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;
    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, user_email;
    RelativeLayout no_order;
    LinearLayout order_list;
    RecyclerView orderstatus;
    Dialog dialog;
    TextView more_view;

    AppCompatButton start_order;
    int itemnum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_order_status_list);




        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);

        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Order_Status_List_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Order_Status_List_Activity.ViewDialog alert = new Order_Status_List_Activity.ViewDialog();
            alert.showDialog(Order_Status_List_Activity.this);
        }

        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
            }
        });

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Order_Status_List_Activity.this);
        getContactsCount();







       /* Log.e("dbsize", "" + dbHelper.listContacts().size());
        if (dbHelper.listContacts().get(0).getQty().equalsIgnoreCase("0")) {
            dbHelper.deleteItem(Integer.parseInt(dbHelper.listContacts().get(0).getId()));
        }*/


        /*---------------------------XML ID Call----------------------------------------------------*/

        no_order = findViewById(R.id.no_order);
        order_list = findViewById(R.id.order_list);
        orderstatus = findViewById(R.id.orderstatus);

        more_view = findViewById(R.id.more_view);
        start_order = findViewById(R.id.start_order);

        start_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
            }
        });

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        user_email = (slogin.getString("login_key_email", null));

        orderstatus(user_id, user_email);
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
        bottomNav = findViewById(R.id.bottom_navigation);
        // bottomNav.getMenu().setGroupCheckable(0, false, true);

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
                            Toast.makeText(Order_Status_List_Activity.this,"Your cart is Empty!",Toast.LENGTH_SHORT).show();
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
    }

    /*------------------------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {
        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Order_Status_List_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Order_Status_List_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_chk = new Internet_connection_checking(Order_Status_List_Activity.this);
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


   /* public void OrderPlaced(View view) {
        //startActivity(new Intent(Order_Status_List_Activity.this,TrackActivity.class));
        String orderStatus = "0";
        Intent intent = new Intent(Order_Status_List_Activity.this, Order_Status_Activity.class);
        intent.putExtra("orderStatus", orderStatus);
        startActivity(intent);
    }

    public void OrderConfirmed(View view) {
        String orderStatus = "1";
        Intent intent = new Intent(Order_Status_List_Activity.this, Order_Status_Activity.class);
        intent.putExtra("orderStatus", orderStatus);
        startActivity(intent);
    }

    public void OrderProcessed(View view) {
        String orderStatus = "2";
        Intent intent = new Intent(Order_Status_List_Activity.this, Order_Status_Activity.class);
        intent.putExtra("orderStatus", orderStatus);
        startActivity(intent);
    }

    public void OrderPickup(View view) {
        String orderStatus = "3";
        Intent intent = new Intent(Order_Status_List_Activity.this, Order_Status_Activity.class);
        intent.putExtra("orderStatus", orderStatus);
        startActivity(intent);
    }*/


    /*  ---------------------------get api URL first time get type values----------------------------------------------------*/
    private void orderstatus(String userid, String user_email) {
        loadingshow();
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("gmail", user_email);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<orderstatus_model> call = apiService.orderstatus(params);
        Log.e("ur_id", "" + params);
        call.enqueue(new Callback<orderstatus_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<orderstatus_model> call, Response<orderstatus_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();

                Log.e("statuslistone", "" + statusCode);


                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        no_order.setVisibility(View.GONE);
                        order_list.setVisibility(View.VISIBLE);

                        List<orderstatus_model.orderstatus> orderhistory = (response.body().getOrderstatus());
                        OrderstatusListAdapter menuitemadapter = new OrderstatusListAdapter(mContext, (List<orderstatus_model.orderstatus>) orderhistory, itemnum);
                        orderstatus.setHasFixedSize(true);
                        orderstatus.setLayoutManager(new LinearLayoutManager(Order_Status_List_Activity.this));
                        orderstatus.setAdapter(menuitemadapter);

                        if (orderhistory.size() > 10) {
                            more_view.setVisibility(View.VISIBLE);
                        } else {
                            more_view.setVisibility(View.GONE);
                        }

                        more_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("menuitemadapter", "" + orderhistory.size());
                                int cont = itemnum * 10;
                                if (itemnum * 10 < orderhistory.size()) {
                                    itemnum = itemnum + 1;

                                    List<orderstatus_model.orderstatus> orderhistory = (response.body().getOrderstatus());
                                    OrderstatusListAdapter menuitemadapter = new OrderstatusListAdapter(mContext, (List<orderstatus_model.orderstatus>) orderhistory, itemnum);
                                    orderstatus.setHasFixedSize(true);
                                    orderstatus.setLayoutManager(new LinearLayoutManager(Order_Status_List_Activity.this));
                                    orderstatus.setAdapter(menuitemadapter);
                                    orderstatus.scrollToPosition(cont);

                                }
                            }
                        });
                    } else {
                        hideloading();
                        no_order.setVisibility(View.VISIBLE);
                        order_list.setVisibility(View.GONE);
                        Snackbar.make(Order_Status_List_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Snackbar.make(Order_Status_List_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<orderstatus_model> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Order_Status_List_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Order_Status_List_Activity.this);
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
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Order Status List Activity");
    }
}
