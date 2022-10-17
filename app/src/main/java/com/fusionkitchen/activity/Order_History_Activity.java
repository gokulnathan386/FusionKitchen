package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
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
import com.fusionkitchen.adapter.MenuserachcatAdapter;
import com.fusionkitchen.adapter.OrderHistoryListAdapter;
import com.fusionkitchen.adapter.OrderstatusListAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.loginsignup.login_model;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.model.orderstatus.orderstatus_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

public class Order_History_Activity extends AppCompatActivity {

    private Context mContext = Order_History_Activity.this;
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

    /*---------------------------XML ID Call----------------------------------------------------*/
    AppCompatButton start_order;
    RelativeLayout no_order;
    LinearLayout order_list;
    RecyclerView myorderList;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, user_email;

    TextView more_view;
    int itemnum = 1;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_orderhistory);

        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);

        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Order_History_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            Order_History_Activity.ViewDialog alert = new Order_History_Activity.ViewDialog();
            alert.showDialog(Order_History_Activity.this);
        }

        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(Order_History_Activity.this);
        getContactsCount();

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        user_email = (slogin.getString("login_key_email", null));
        Log.e("user_ids", "" + user_id);
        Log.e("user_email", "" + user_email);

        /*--------------Order list get API------------------*/
        orderhistory(user_id, user_email);

        /*---------------------------XML ID Call----------------------------------------------------*/
        start_order = findViewById(R.id.start_order);
        no_order = findViewById(R.id.no_order);
        order_list = findViewById(R.id.order_list);
        myorderList = findViewById(R.id.myorderList);
        more_view = findViewById(R.id.more_view);

        start_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));
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


        bottomNav = findViewById(R.id.bottom_navigation);


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




    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Order_History_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Order_History_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int_chk = new Internet_connection_checking(Order_History_Activity.this);
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

    /*--------------Order list get API------------------*/

    /*  ---------------------------get api URL first time get type values----------------------------------------------------*/
    private void orderhistory(String userid, String user_email) {
        loadingshow();
        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("gmail", user_email);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<ordhistorys_list_model> call = apiService.orderhistoryslist(params);
        Log.e("ur_id", "" + params);
        call.enqueue(new Callback<ordhistorys_list_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ordhistorys_list_model> call, Response<ordhistorys_list_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        hideloading();
                        no_order.setVisibility(View.GONE);
                        order_list.setVisibility(View.VISIBLE);
                        List<ordhistorys_list_model.reorder_details> orderhistory = (response.body().getReorder_details());
                        OrderHistoryListAdapter menuitemadapter = new OrderHistoryListAdapter(mContext, (List<ordhistorys_list_model.reorder_details>) orderhistory, itemnum);
                        myorderList.setHasFixedSize(true);
                        myorderList.setLayoutManager(new LinearLayoutManager(Order_History_Activity.this));
                        myorderList.setAdapter(menuitemadapter);
                       /* myorderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);

                                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    Log.d("-----", "end");

                                    Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();

                                }
                            }
                        });*/

                        Log.e("itemcount", "" + orderhistory.size());
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
                                    List<ordhistorys_list_model.reorder_details> orderhistory = (response.body().getReorder_details());
                                    OrderHistoryListAdapter menuitemadapter = new OrderHistoryListAdapter(mContext, (List<ordhistorys_list_model.reorder_details>) orderhistory, itemnum);
                                    myorderList.setHasFixedSize(true);
                                    myorderList.setLayoutManager(new LinearLayoutManager(Order_History_Activity.this));
                                    myorderList.setAdapter(menuitemadapter);
                                    myorderList.scrollToPosition(cont);

                                }
                            }
                        });
                    } else {
                        hideloading();
                        no_order.setVisibility(View.VISIBLE);
                        order_list.setVisibility(View.GONE);
                        // Snackbar.make(Order_History_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    no_order.setVisibility(View.VISIBLE);
                    order_list.setVisibility(View.GONE);
                    Snackbar.make(Order_History_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ordhistorys_list_model> call, Throwable t) {
                hideloading();
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Order_History_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(Order_History_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loading_layout);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(Order_History_Activity.this)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideloading() {
        dialog.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Order History Activity");
    }


}
