package com.fusionkitchen.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.adapter.OrderHistoryListAdapter;
import com.fusionkitchen.adapter.OrderHistoryitemListAdapter;
import com.fusionkitchen.adapter.OrderstatusitemListAdapter;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.order_history.order_details_list_show;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.model.orderstatus.ordertracking_details_model;
import com.fusionkitchen.model.orderstatus.ordertracking_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;

public class Order_Status_Activity extends AppCompatActivity {

    private Context mContext = Order_Status_Activity.this;


    /*---------------------------check internet connection----------------------------------------------------*/

    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    private static final int REQUEST_CODE = 101;


    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;




    /*---------------------------XML ID Call----------------------------------------------------*/

    View view_order_placed, view_order_confirmed, view_order_processed, view_order_pickup, con_divider, ready_divider, placed_divider;
    ImageView img_orderconfirmed, orderprocessed, orderpickup, orderplaced;
    TextView textorderpickup, text_confirmed, textorderprocessed, textorderplaced;

    AppCompatTextView order_date, order_id, total_amt;

    ConstraintLayout tracking_layout, bill_layout;
    AppCompatTextView view_bill, hide_bill;
    TextView listview_total, sub_amt, service_amt, coupon_amt, delivery_amt, bag_amt;
    RelativeLayout servicel_layout, delivery_layout, coupon_layout, bag_layout;

    RecyclerView myorderList;
    /*---------------------------dialog show----------------------------------------------------*/

    Dialog waitingdialog, confirmdialog, rejectdialog, delieveddialog;


    /* --------------Get Intent------------------*/

    String orderid, orderpath, orderdate, clientname, txtotype, clientid, clientphonenumber, clsstype;

    String otype, statusshow, customername;


    final Handler handler = new Handler();
    Runnable runnable;
    final int delay = 5000; // 1000 milliseconds == 1 second

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id;

    boolean check_again_btn = true;
    boolean check_confirmDialog_btn = true;
    boolean check_delivedDialog_btn = true;
    boolean check_reject_btn = true;

    AppCompatButton chat_client, call_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_order_status);

        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);

        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(Order_Status_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            ViewDialog alert = new ViewDialog();
            alert.showDialog(Order_Status_Activity.this);

        }

        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);





        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        /*---------------------------Fresh Chat----------------------------------------------------*/

        FreshchatConfig config = new FreshchatConfig("67d078d8-604f-44c7-8807-c8a96810af62", "0604e381-8106-48e8-95bf-bc74bc8893fe");
        config.setDomain("msdk.in.freshchat.com");
        config.setCameraCaptureEnabled(true);
        config.setGallerySelectionEnabled(true);
        config.setResponseExpectationEnabled(true);
        Freshchat.getInstance(getApplicationContext()).init(config);



        /*---------------------------XML ID Call----------------------------------------------------*/

        view_order_placed = findViewById(R.id.view_order_placed);
        view_order_confirmed = findViewById(R.id.view_order_confirmed);
        view_order_processed = findViewById(R.id.view_order_processed);
        view_order_pickup = findViewById(R.id.view_order_pickup);

        placed_divider = findViewById(R.id.placed_divider);
        con_divider = findViewById(R.id.con_divider);
        ready_divider = findViewById(R.id.ready_divider);
        textorderpickup = findViewById(R.id.textorderpickup);

        text_confirmed = findViewById(R.id.text_confirmed);
        textorderprocessed = findViewById(R.id.textorderprocessed);
        textorderplaced = findViewById(R.id.textorderplaced);
        img_orderconfirmed = findViewById(R.id.img_orderconfirmed);

        orderplaced = findViewById(R.id.orderplaced);
        orderprocessed = findViewById(R.id.orderprocessed);
        orderpickup = findViewById(R.id.orderpickup);
        order_date = findViewById(R.id.order_date);

        order_id = findViewById(R.id.order_id);
        total_amt = findViewById(R.id.total_amt);
        myorderList = findViewById(R.id.myorderList);
        listview_total = findViewById(R.id.listview_total);

        sub_amt = findViewById(R.id.sub_amt);
        service_amt = findViewById(R.id.service_amt);
        coupon_amt = findViewById(R.id.coupon_amt);
        delivery_amt = findViewById(R.id.delivery_amt);

        bag_amt = findViewById(R.id.bag_amt);
        servicel_layout = findViewById(R.id.servicel_layout);
        delivery_layout = findViewById(R.id.delivery_layout);
        coupon_layout = findViewById(R.id.coupon_layout);

        bag_layout = findViewById(R.id.bag_layout);
        chat_client = findViewById(R.id.chat_client);
        call_client = findViewById(R.id.call_client);


        chat_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Freshchat.showConversations(getApplicationContext());
            }
        });

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        orderpath = intent.getStringExtra("orderpath");
        orderdate = intent.getStringExtra("orderdate");
        clientname = intent.getStringExtra("clientname");
        clientid = intent.getStringExtra("clientid");
        clientphonenumber = intent.getStringExtra("clientphonenumber");


       /* if (clsstype.equalsIgnoreCase("1")) {
            dbHelper.deleteAll();
          //  Order_Status_Activity.this.deleteDatabase("SQLiteExample.db");
            for (int i = 0; i < dbHelper.listContacts().size(); i++) {
                Log.e("dbHelper1", "" + dbHelper.listContacts().size());
                Log.e("dbHelper3", "" + Integer.parseInt(dbHelper.listContacts().get(i).getId()));
                dbHelper.deleteItem(Integer.parseInt(dbHelper.listContacts().get(i).getId()));

            }

        }
*/





        Log.e("itemvalue1", "" + orderid);
        Log.e("itemvalue2", "" + orderpath);
        Log.e("itemvalue3", "" + orderdate);
        Log.e("itemvalue4", "" + clientname);
        Log.e("itemvalue5", "" + clientid);
        Log.e("itemvalue6", "" + clientphonenumber);


     //   order_date.setText(orderdate);

        order_id.setText("Order Id: " + orderid);

        getodertrackingdeatils(orderid, orderpath);


        handler.postDelayed(new Runnable() {
            public void run() {
                getodertrackingdeatils(orderid, orderpath);
                handler.postDelayed(this, delay);
            }
        }, delay);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);

                startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));

            }
        });

       /* String orderStatus = intent.getStringExtra("orderStatus");
        getOrderStatus(orderStatus);*/

        tracking_layout = findViewById(R.id.tracking_layout);
        bill_layout = findViewById(R.id.bill_layout);

        view_bill = findViewById(R.id.view_bill);
        hide_bill = findViewById(R.id.hide_bill);

        view_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_bill.setVisibility(View.GONE);
                hide_bill.setVisibility(View.VISIBLE);
                tracking_layout.setVisibility(View.GONE);
                bill_layout.setVisibility(View.VISIBLE);
                handler.removeMessages(0);
            }
        });
        hide_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_bill.setVisibility(View.VISIBLE);
                hide_bill.setVisibility(View.GONE);
                tracking_layout.setVisibility(View.VISIBLE);
                bill_layout.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getodertrackingdeatils(orderid, orderpath);
                        handler.postDelayed(this, delay);
                    }
                }, delay);
            }
        });

        call_client.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Order_Status_Activity.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+44 " + clientphonenumber));
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });

        /*---------------------------Waiting dialog show----------------------------------------------------*/
      /*  Order_Status_Activity.ViewwaitingDialog alert = new Order_Status_Activity.ViewwaitingDialog();
        alert.shownowaitingDialog(Order_Status_Activity.this);*/

        /*---------------------------Confirmation dialog show----------------------------------------------------*/
       /* Order_Status_Activity.ViewconfirmDialog alert = new Order_Status_Activity.ViewconfirmDialog();
        alert.showconfirmDialog(Order_Status_Activity.this);
*/
        /*---------------------------Reject dialog show----------------------------------------------------*/
        /*Order_Status_Activity.ViewrejectDialog alert = new Order_Status_Activity.ViewrejectDialog();
        alert.showrejectDialog(Order_Status_Activity.this);*/


        /*---------------------------delived dialog show----------------------------------------------------*/
       /* Order_Status_Activity.ViewdelivedDialog alert = new Order_Status_Activity.ViewdelivedDialog();
        alert.showdelivedDialog(Order_Status_Activity.this);*/


    }




    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = Order_Status_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Order_Status_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(Order_Status_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }


                }
            });
            alertDialog.show();

        }
    }


    /*  ---------------------------get api URL first time get type values----------------------------------------------------*/
    private void getodertrackingdeatils(String orderiding, String orderpathing) {

        // get user data from session
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderdetails", orderiding);
        params.put("path", orderpathing);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<ordertracking_model> call = apiService.ordertracking(params);
        Log.e("ur_id", "" + params);
        call.enqueue(new Callback<ordertracking_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ordertracking_model> call, Response<ordertracking_model> response) {
                //response.headers().get("Set-Cookie");
                int statusCode = response.code();

                Log.e("statusstatusCode", "" + statusCode);

                if (statusCode == 200) {

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        customername = response.body().getOrdertracking().getOrder().getUser().getFname();
                        total_amt.setText("Total Bill: £ " + response.body().getOrdertracking().getOrder().getOrder().getTotal());
                        listview_total.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getTotal());

                        order_date.setText(response.body().getOrdertracking().getOrder().getOrder().get_order_date());

                        Log.d("order_date",response.body().getOrdertracking().getOrder().getOrder().get_order_date());

                        sub_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getSub_total());
                        if (response.body().getOrdertracking().getOrder().getOrder().getBank().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getBank().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getBank().equalsIgnoreCase("0")) {
                            servicel_layout.setVisibility(View.GONE);
                        } else {
                            servicel_layout.setVisibility(View.VISIBLE);
                            service_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getBank());
                        }
                        if (response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0")) {
                            delivery_layout.setVisibility(View.GONE);
                        } else {
                            delivery_layout.setVisibility(View.VISIBLE);
                            delivery_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getDelivery_charge());
                        }
                        if (response.body().getOrdertracking().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0")) {
                            coupon_layout.setVisibility(View.GONE);
                        } else {
                            coupon_layout.setVisibility(View.VISIBLE);
                            coupon_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getPromo_discount());
                        }
                        if (response.body().getOrdertracking().getOrder().getOrder().getBagage().equalsIgnoreCase("0.00") || response.body().getOrdertracking().getOrder().getOrder().getBagage().equalsIgnoreCase("0.0") || response.body().getOrdertracking().getOrder().getOrder().getBagage().equalsIgnoreCase("0")) {
                            bag_layout.setVisibility(View.GONE);
                        } else {
                            bag_layout.setVisibility(View.VISIBLE);
                            bag_amt.setText("£ " + response.body().getOrdertracking().getOrder().getOrder().getBagage());
                        }


                        List<ordertracking_details_model.item> orderhistory = (response.body().getOrdertracking().getOrder().getItem());
                        OrderstatusitemListAdapter menuitemnameadapter = new OrderstatusitemListAdapter(mContext, (List<ordertracking_details_model.item>) orderhistory);
                        myorderList.setHasFixedSize(true);
                        myorderList.setLayoutManager(new LinearLayoutManager(Order_Status_Activity.this));
                        myorderList.setAdapter(menuitemnameadapter);


                        otype = response.body().getOrdertracking().getOrder().getOrder().getOtype();
                        statusshow = response.body().getOrdertracking().getOrder().getOrder().getStatus();



                        if (otype.equalsIgnoreCase("0")) {
                            orderprocessed.setImageResource(R.drawable.deliverybike);
                            textorderpickup.setText("Order Delivered");

                            txtotype = "Delivery";
                        } else {
                            orderprocessed.setImageResource(R.drawable.takeawaybag);
                            textorderpickup.setText("Order Collected");
                            txtotype = "Collection";
                        }


                        if (otype.equalsIgnoreCase("0")) {
//Delivery
                            if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("0")) {
                                getOrderStatus("0");
                                //Order recived
                                if (check_again_btn == true) {
                                    check_again_btn = false;
                                    ViewwaitingDialog alert = new ViewwaitingDialog();
                                    alert.shownowaitingDialog(Order_Status_Activity.this);
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("1") && response.body().getOrdertracking().getOrder().getOrder().getDrivertracking().equalsIgnoreCase("false")) {

                                //   handler.removeCallbacksAndMessages(null);
                                getOrderStatus("1");
                                if (check_confirmDialog_btn == true) {
                                    check_confirmDialog_btn = false;
                                    ViewconfirmDialog alert = new ViewconfirmDialog();
                                    alert.showconfirmDialog(Order_Status_Activity.this);
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("1") && response.body().getOrdertracking().getOrder().getOrder().getDrivertracking().equalsIgnoreCase("true")) {
                                //  handler.removeCallbacksAndMessages(null);
                                //order accepted
                                getOrderStatus("2");
                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("3")) {
                                // handler.removeCallbacksAndMessages(null);
                                //Order Delived
                                getOrderStatus("3");

                                if (check_delivedDialog_btn == true) {
                                    check_delivedDialog_btn = false;

                                    ViewdelivedDialog alert = new ViewdelivedDialog();
                                    alert.showdelivedDialog(Order_Status_Activity.this);
                                }

                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("2")) {
                                // handler.removeCallbacksAndMessages(null);
                                //Order rejected


                                getOrderStatus("0");
                                if (check_reject_btn == true) {
                                    check_reject_btn = false;
                                    ViewrejectDialog alert = new ViewrejectDialog();
                                    alert.showrejectDialog(Order_Status_Activity.this);
                                }

                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            }

                        } else {


                            if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("0")) {
                                getOrderStatus("0");
                                //Order recived
                                if (check_again_btn == true) {
                                    check_again_btn = false;
                                    ViewwaitingDialog alert = new ViewwaitingDialog();
                                    alert.shownowaitingDialog(Order_Status_Activity.this);
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("1")) {
                                //   handler.removeCallbacksAndMessages(null);
                                //order accepted
                                getOrderStatus("1");
                                if (check_confirmDialog_btn == true) {
                                    check_confirmDialog_btn = false;
                                    ViewconfirmDialog alert = new ViewconfirmDialog();
                                    alert.showconfirmDialog(Order_Status_Activity.this);
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("3")) {
                                //  handler.removeCallbacksAndMessages(null);
                                getOrderStatus("3");
                                //Order Delived
                                if (check_delivedDialog_btn == true) {
                                    check_delivedDialog_btn = false;
                                    ViewdelivedDialog alert = new ViewdelivedDialog();
                                    alert.showdelivedDialog(Order_Status_Activity.this);

                                    if (confirmdialog != null && confirmdialog.isShowing()) {
                                        confirmdialog.cancel();
                                    }
                                    if (waitingdialog != null && waitingdialog.isShowing()) {
                                        waitingdialog.cancel();
                                    }
                                }
                            } else if (response.body().getOrdertracking().getOrder().getOrder().getStatus().equalsIgnoreCase("2")) {
                                // handler.removeCallbacksAndMessages(null);
                                getOrderStatus("0");
                                //Order rejected
                                if (check_reject_btn == true) {
                                    check_reject_btn = false;
                                    ViewrejectDialog alert = new ViewrejectDialog();
                                    alert.showrejectDialog(Order_Status_Activity.this);
                                }

                                if (confirmdialog != null && confirmdialog.isShowing()) {
                                    confirmdialog.cancel();
                                }
                                if (waitingdialog != null && waitingdialog.isShowing()) {
                                    waitingdialog.cancel();
                                }
                            }


                        }


                    }
                } else {

                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ordertracking_model> call, Throwable t) {
                Log.e("bugcode", "" + t.toString());
                Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*--------------------Tracking id pass-----------------------*/
    private void getOrderStatus(String orderStatus) {
        if (orderStatus.equals("0")) {
            float alfa = (float) 0.5;
            setStatus(alfa);

        } else if (orderStatus.equals("1")) {
            float alfa = (float) 1;
            setStatus1(alfa);


        } else if (orderStatus.equals("2")) {
            float alfa = (float) 1;
            setStatus2(alfa);


        } else if (orderStatus.equals("3")) {
            float alfa = (float) 1;
            setStatus3(alfa);
        }
    }

    //  alfa - disable  myf --enabale
    private void setStatus(float alfa) {
        float myf = (float) 0.5;


        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));

        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));


        img_orderconfirmed.setAlpha(alfa);
        orderprocessed.setAlpha(alfa);
        orderpickup.setAlpha(alfa);
        orderplaced.setAlpha(alfa);

        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);
        textorderplaced.setAlpha(alfa);

        placed_divider.setAlpha(alfa);


    }

    private void setStatus1(float alfa) {
        float myf = (float) 0.5;

        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));

        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));

        orderplaced.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        orderprocessed.setAlpha(myf);
        orderpickup.setAlpha(myf);

        textorderplaced.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(myf);
        textorderpickup.setAlpha(myf);


        //   view_order_pickup.setAlpha(myf);


    }

    private void setStatus2(float alfa) {
        float myf = (float) 0.5;

        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_completed_dotted));

        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_current));

        orderplaced.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        orderpickup.setAlpha(myf);

        textorderplaced.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);
        orderprocessed.setAlpha(alfa);
        textorderpickup.setAlpha(myf);


    }

    private void setStatus3(float alfa) {
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.view_current_dotted));


        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderplaced.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        orderpickup.setAlpha(alfa);

        textorderplaced.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);
        orderprocessed.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);


    }


    /*---------------------------Waiting dialog show----------------------------------------------------*/
    public class ViewwaitingDialog {
        public void shownowaitingDialog(Activity activity) {


            waitingdialog = new Dialog(activity, R.style.MaterialDialogSheet);
            // waitingdialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            waitingdialog.setContentView(R.layout.order_status_waiting);

            TextView waiting_txtone_popup = waitingdialog.findViewById(R.id.waiting_txtone_popup);
            TextView waiting_txttwo_popup = waitingdialog.findViewById(R.id.waiting_txttwo_popup);
            TextView pleasewait = waitingdialog.findViewById(R.id.pleasewait);


            waiting_txtone_popup.setText(clientname + " has received your order for " + txtotype);
            waiting_txttwo_popup.setText("Awaiting confirmation from the restaurant.\n You will receive a confirmation mail regarding your " + txtotype + " order status soon.");
            pleasewait.setText("ORDER RECEIVED!");

            ImageView waiting_img = waitingdialog.findViewById(R.id.waiting_img);
            // ImageView waiting_popup_close = waitingdialog.findViewById(R.id.waiting_popup_close);

            Glide.with(Order_Status_Activity.this)
                    .load(R.drawable.waiting_loader)
                    .placeholder(R.drawable.waiting_loader)
                    .into(waiting_img);

/*

            waiting_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    waitingdialog.cancel();

                }
            });
*/


            waitingdialog.show();
        }
    }

    /*---------------------------Confirmation dialog show----------------------------------------------------*/
    public class ViewconfirmDialog {
        public void showconfirmDialog(Activity activity) {
            confirmdialog = new Dialog(activity, R.style.MaterialDialogSheet);
            confirmdialog.setContentView(R.layout.order_status_confirm);
            TextView contitel = confirmdialog.findViewById(R.id.contitel);
            TextView confirm_txtone_popup = confirmdialog.findViewById(R.id.confirm_txtone_popup);
            TextView confirm_txttwo_popup = confirmdialog.findViewById(R.id.confirm_txttwo_popup);

            AppCompatButton confirm_view_status = confirmdialog.findViewById(R.id.confirm_view_status);

            AppCompatButton confirm_view_feedback = confirmdialog.findViewById(R.id.confirm_view_feedback);

            ImageView waiting_img = confirmdialog.findViewById(R.id.waiting_img);

            contitel.setText("ORDER CONFIRMED!");
            confirm_txtone_popup.setText(clientname + " has confirmed your order ");
            confirm_txttwo_popup.setText("Yummy Food Can be Collected soon. Thank You For ordering From " + clientname + ".\n Happy Ordering, Happy Eating");
            //  txtotype
            Glide.with(Order_Status_Activity.this)
                    .load(R.drawable.confirmed)
                    .placeholder(R.drawable.confirmed)
                    .into(waiting_img);

            confirm_view_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmdialog.cancel();

                    Intent intent = new Intent(mContext, Feedback_Activity.class);
                    intent.putExtra("orderid", orderid);
                    intent.putExtra("clientid", clientid);

                    startActivity(intent);

                    if (waitingdialog != null && waitingdialog.isShowing()) {
                        waitingdialog.cancel();
                    }

                    if (confirmdialog != null && confirmdialog.isShowing()) {
                        confirmdialog.cancel();
                    }


                }
            });


            confirm_view_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    confirmdialog.cancel();

                    if (waitingdialog != null && waitingdialog.isShowing()) {
                        waitingdialog.cancel();
                    }
                    if (confirmdialog != null && confirmdialog.isShowing()) {
                        confirmdialog.cancel();
                    }

                }
            });

            new CountDownTimer(4000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {


                    waiting_img.setImageResource(R.drawable.popconfirmed);


                }
            }.start();


            confirmdialog.show();
        }
    }


    /*---------------------------Reject dialog show----------------------------------------------------*/
    public class ViewrejectDialog {
        public void showrejectDialog(Activity activity) {
            rejectdialog = new Dialog(activity, R.style.MaterialDialogSheet);
            rejectdialog.setContentView(R.layout.order_status_reject);


            TextView reject_txtone_popup = rejectdialog.findViewById(R.id.reject_txtone_popup);
            TextView reject_txttwo_popup = rejectdialog.findViewById(R.id.reject_txttwo_popup);
            TextView orderrejetirtel = rejectdialog.findViewById(R.id.orderrejetirtel);
            ImageView reject_popup_close = rejectdialog.findViewById(R.id.reject_popup_close);


            orderrejetirtel.setText("ORDER REJECTED!");
            reject_txtone_popup.setText("Sorry! Unfortunately your order has been rejected by " + clientname);
            reject_txttwo_popup.setText("If your order is paid, you will receive the refund within next 3-5 working days. Please call the takeaway at" + clientphonenumber + " to know more.");


            Button reject_order_again = rejectdialog.findViewById(R.id.reject_order_again);

            reject_order_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });

            reject_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectdialog.cancel();
                    //  startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });

            rejectdialog.show();
        }
    }

    /*---------------------------Delived dialog show----------------------------------------------------*/
    public class ViewdelivedDialog {
        public void showdelivedDialog(Activity activity) {
            delieveddialog = new Dialog(activity, R.style.MaterialDialogSheet);
            delieveddialog.setContentView(R.layout.order_status_delived);


            TextView delieved_txtone_popup = delieveddialog.findViewById(R.id.delieved_txtone_popup);

            delieved_txtone_popup.setText("Hello " + customername + ", Your order was delivered successfully");

            AppCompatButton delived_order_again = delieveddialog.findViewById(R.id.delived_order_again);
            AppCompatButton delived_view_recept = delieveddialog.findViewById(R.id.delived_view_recept);

            ImageView delieved_popup_close = delieveddialog.findViewById(R.id.delieved_popup_close);


            delived_order_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), Postcode_Activity.class));

                }
            });
            delived_view_recept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    delieveddialog.cancel();
                    view_bill.setVisibility(View.GONE);
                    hide_bill.setVisibility(View.VISIBLE);

                    tracking_layout.setVisibility(View.GONE);
                    bill_layout.setVisibility(View.VISIBLE);

                }
            });

            delieved_popup_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    delieveddialog.cancel();

                }
            });


            delieveddialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);

            startActivity(new Intent(getApplicationContext(), Order_Status_List_Activity.class));
            return true;
        }
        return false;
    }

    //permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+44 " + clientphonenumber));
                    startActivity(callIntent);
                } else {

                    Snackbar.make(Order_Status_Activity.this.findViewById(android.R.id.content), "Please allow to call the takeaway", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}
