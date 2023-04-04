package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Dashboard_Activity;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.model.AdapterListData;
import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.addon.menu_addons_model;
import com.fusionkitchen.model.menu_model.search_menu_item_model;
import com.fusionkitchen.model.modeoforder.getlatertime_model;
import com.fusionkitchen.model.modeoforder.modeof_order_popup_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Html.fromHtml;
import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Integer.parseInt;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    private search_menu_item_model.data[] searchitemname;
    private static Context mContext;
    private String ordertypevalue, menuurlpath, fullUrl;
    private static long mLastClickTime = 0;
    private Dialog dialog;
    ArrayList<String> allContacts;
    int count,length;
    String remove_qty,remove_item_addon,remove_id;
    String item_user_add_name;

    Dialog repeatpopup;    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    private SQLDBHelper dbHelper;
    String todaytimestr, laterdatestr, latertimestr, activetagstr, laterdate;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    int cursor;
    String metdpasfullUrl,mlaterdatefullUrl,order_mode,todaytimestring;
    String selectedlaterdateItem,latertimestring;
    String updatefinalamt,updateqty;

    // RecyclerView recyclerView;
    public SearchItemAdapter(Context mContext, List<search_menu_item_model.data> searchitemname, String ordertypevalue, String menuurlpath) {

        this.mContext = mContext;
        this.searchitemname = searchitemname.toArray(new search_menu_item_model.data[0]);

        this.ordertypevalue = ordertypevalue;
        this.menuurlpath = menuurlpath;
    }


    @Override
    public SearchItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_search_item, parent, false);
        SearchItemAdapter.ViewHolder viewHolder = new SearchItemAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(SearchItemAdapter.ViewHolder holder, int position) {


        final search_menu_item_model.data student = searchitemname[position];//new add

        holder.item_name.setText(searchitemname[position].getName());//new add
        holder.item_price.setText("£ " + searchitemname[position].getPrice());

        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);



        /*---------------------------Sql Lite DataBase----------------------------------------------------*/

        dbHelper = new SQLDBHelper(mContext);


        allContacts = dbHelper.getitemlist();

        for (int k = 0; k<allContacts.size();k++){

            if(allContacts.get(k).equalsIgnoreCase(searchitemname[position].getId())){

                ArrayList<String> get_count = dbHelper.getuseridcount(searchitemname[position].getId());

                holder.menu_item_add.setVisibility(GONE);
                holder.increment_decrement_layout.setVisibility(View.VISIBLE);

                int count = get_count.get(0).length();

                if(count == 1){
                    holder.qty_textview_number.setText("0"+get_count.get(0));
                }else{
                    holder.qty_textview_number.setText(""+get_count.get(0));
                }

            }

        }

        holder.qty_decrease_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Decreasepriceqty(v,searchitemname[position].getId(),holder,0);
            }
        });

        holder.qty_increase_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addonitem(v,position,holder);
            }
        });


        LocalBroadcastManager.getInstance(mContext).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                ArrayList<String> get_count = dbHelper.getuseridcount(searchitemname[position].getId());

                try {
                    String count = String.valueOf(get_count.get(0).length());
                    if(count.equalsIgnoreCase("1")){
                        holder.qty_textview_number.setText("0"+get_count.get(0));
                    }else{
                        holder.qty_textview_number.setText(""+get_count.get(0));
                    }

                } catch(NullPointerException e) {
                    Log.d("Null_point_Expection","" + e);
                }
            }
        }, new IntentFilter("total_item_qty"));



        holder.menu_item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cursor != 0) {
                    addonitem(view,position,holder);
                } else {

                    if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("1")){
                        addonitem(view,position,holder);

                    }else if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("2")){

                        Intent intent = new Intent("loadPreOrderPop");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                       // Order_mode_popup(view,holder,position);

                    }else{
                        addonitem(view,position,holder);

                    }
                }

            }
        });
    }

    private void Decreasepriceqty(View v, String id, ViewHolder holder, int i) {


        count= parseInt(String.valueOf(holder.qty_textview_number.getText()));

        if (count == 1) {

            holder.qty_textview_number.setText("01");

            ArrayList<HashMap<String, String>> qtypice3 = dbHelper.getlastposition(id);

            for (int K = 0; K < qtypice3.size(); K++) {
                HashMap<String, String> hashmap = qtypice3.get(K);
                remove_qty =  hashmap.get("qty");
                remove_item_addon = hashmap.get("itemaddontotalamt");
                remove_id = hashmap.get("id");
            }

            int database_qty =  Math.round(Float.parseFloat(remove_qty));
            int qty  = database_qty - 1;
            String price  = remove_item_addon;
            float total_amt = Float.parseFloat(price) * qty;

            Boolean updatevalue = dbHelper.repeat_last_pop_up(Integer.parseInt(remove_id), qty, total_amt);

            Intent intent = new Intent("item_successfully_custom-message");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


            holder.menu_item_add.setVisibility(View.VISIBLE);
            holder.increment_decrement_layout.setVisibility(GONE);


            ArrayList<String> get_qty_count = dbHelper.getqtycount();

            if(get_qty_count.get(0).equalsIgnoreCase("0")){

                Intent cart_botton_layout = new Intent("total_count_layout_gone");
                cart_botton_layout.putExtra("item_id_sqlite",id);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(cart_botton_layout);

            }else{

                dbHelper.deleteItemRow(id);

            }

        } else {

            count -= 1;
            length = String.valueOf(count).length();


            if(length == 1){

                holder.qty_textview_number.setText("0" + count);


                ArrayList<HashMap<String, String>> qtypice3 = dbHelper.getlastposition(id);

                for (int K = 0; K < qtypice3.size(); K++) {
                    HashMap<String, String> hashmap = qtypice3.get(K);
                    remove_qty =  hashmap.get("qty");
                    remove_item_addon = hashmap.get("itemaddontotalamt");
                    remove_id = hashmap.get("id");
                }

                int database_qty =  Math.round(Float.parseFloat(remove_qty));
                int qty  = database_qty - 1;
                String price  = remove_item_addon;
                float total_amt = Float.parseFloat(price) * qty;

                if(remove_qty.equalsIgnoreCase("1")){
                    dbHelper.deleteItem(Integer.parseInt(remove_id));
                }else{
                    Boolean updatevalue = dbHelper.repeat_last_pop_up(Integer.parseInt(remove_id), qty, total_amt);
                }

                Intent intent = new Intent("item_successfully_custom-message");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }else{

                holder.qty_textview_number.setText("" + count);

                ArrayList<HashMap<String, String>> qtypice3 = dbHelper.getlastposition(id);

                for (int K = 0; K < qtypice3.size(); K++) {
                    HashMap<String, String> hashmap = qtypice3.get(K);
                    remove_qty =  hashmap.get("qty");
                    remove_item_addon = hashmap.get("itemaddontotalamt");
                    remove_id = hashmap.get("id");
                }

                int database_qty =  Math.round(Float.parseFloat(remove_qty));
                int qty  = database_qty - 1;
                String price  = remove_item_addon;
                float total_amt = Float.parseFloat(price) * qty;

                if(remove_qty.equalsIgnoreCase("1")){
                    dbHelper.deleteItem(Integer.parseInt(remove_id));
                }else{
                    Boolean updatevalue = dbHelper.repeat_last_pop_up(Integer.parseInt(remove_id), qty, total_amt);
                }

                Intent intent = new Intent("item_successfully_custom-message");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }

        }


    }


   /* private void Order_mode_popup(View view,ViewHolder holder, int position) {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pre_order);

        ShimmerFrameLayout shimmer_view_preorder = dialog.findViewById(R.id.shimmer_view_preorder);
        shimmer_view_preorder.startShimmerAnimation();

        CardView ordermode_popup_view  =dialog.findViewById(R.id.ordermode_popup_view);
        TextView colloetion_tattime = dialog.findViewById(R.id.colloetion_tattime);
        TextView delivery_tattime = dialog.findViewById(R.id.delivery_tattime);
        RelativeLayout delivery_but = dialog.findViewById(R.id.delivery_but);
        RelativeLayout collection_but = dialog.findViewById(R.id.collection_but);
        TextView delivery_txt = dialog.findViewById(R.id.delivery_txt);
        TextView colli_txt = dialog.findViewById(R.id.colli_txt);
        GifImageView loading_imageView1 = dialog.findViewById(R.id.loading_imageView1);
        GifImageView loading_imageView2 = dialog.findViewById(R.id.loading_imageView2);
        AppCompatButton update_mode = dialog.findViewById(R.id.update_mode);
        AppCompatButton bun_asap = dialog.findViewById(R.id.bun_asap);
        TextView sevenday_txt =dialog.findViewById(R.id.sevenday_txt);
        LinearLayout card_change = dialog.findViewById(R.id.card_change);
        LinearLayout today_time_layer = dialog.findViewById(R.id.today_time_layer);
        LinearLayout later_time_layer = dialog.findViewById(R.id.later_time_layer);
        AppCompatButton bun_later = dialog.findViewById(R.id.bun_later);
        AppCompatButton  bun_today = dialog.findViewById(R.id.bun_today);
        Spinner today_time = dialog.findViewById(R.id.today_time);
        Spinner later_time = dialog.findViewById(R.id.later_time);
        Spinner later_date = dialog.findViewById(R.id.later_date);
        RelativeLayout later_timing_layer =dialog.findViewById(R.id.later_timing_layer);





        Map<String, String> params = new HashMap<String, String>();


        metdpasfullUrl = menuurlpath + "/loadPreOrderPop";
        //ordermode_popup_view.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<modeof_order_popup_model> call = apiService.modeofordershow(metdpasfullUrl);
        call.enqueue(new Callback<modeof_order_popup_model>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<modeof_order_popup_model> call, Response<modeof_order_popup_model> response) {
                //response.headers().get("Set-Cookie");
                ordermode_popup_view.setVisibility(View.VISIBLE);
                int statusCode = response.code();

                if (statusCode == 200) {

                    takeway_closed  = response.body().getStatus();

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        //Cooking time set
                        colloetion_tattime.setText(response.body().getData().getCollection().getCooking_time());
                        delivery_tattime.setText(response.body().getData().getDelivery().getCooking_time());

                        menu_delivery_tattime = response.body().getData().getDelivery().getCooking_time();
                        menu_collection_tattime = response.body().getData().getCollection().getCooking_time();

                        if (!response.body().getData().getDelivery().getStatus().equalsIgnoreCase("0")) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    delivery_but.performClick();

                                }
                            }, 1000);



                        } else if (!response.body().getData().getCollection().getStatus().equalsIgnoreCase("0")) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    collection_but.performClick();


                                }
                            }, 1000);

                        }

                        //Top button click
                        delivery_but.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        update_mode.setVisibility(View.GONE);
                                        delivery_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_one));
                                        collection_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_two));
                                        delivery_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));
                                        delivery_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));
                                        colli_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));
                                        colloetion_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));

                                        loading_imageView1.setVisibility(View.VISIBLE);
                                        loading_imageView2.setVisibility(View.GONE);

                                        order_mode = "0";//Delivery

                                        if (response.body().getData().getDelivery().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                            sevenday_txt.setVisibility(View.GONE);
                                        } else {
                                           // sevenday_txt.setVisibility(View.VISIBLE);
                                            sevenday_txt.setText("Select a delivery time" + "\n" + " up to 7 days in advance");
                                        }

                                        if (response.body().getData().getDelivery().getStatus().equalsIgnoreCase("0")) {
                                            card_change.setVisibility(View.GONE);
                                            today_time_layer.setVisibility(View.GONE);
                                            later_time_layer.setVisibility(View.GONE);
                                            update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                            update_mode.setClickable(false);
                                            update_mode.setFocusable(false);
                                            update_mode.setEnabled(false);
                                            delivery_tattime.setText("Unavailable");
                                            menu_delivery_tattime = "Unavailable";
                                            update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                            update_mode.setText("Takeaway Closed for Delivery");
                                            update_mode.setVisibility(View.VISIBLE);
                                            sevenday_txt.setVisibility(View.VISIBLE);
                                            shimmer_view_preorder.stopShimmerAnimation();
                                            shimmer_view_preorder.setVisibility(View.GONE);


                                        } else {

                                            card_change.setVisibility(View.VISIBLE);
                                            delivery_tattime.setText(response.body().getData().getDelivery().getCooking_time());

                                            if (response.body().getData().getDelivery().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                if (response.body().getData().getDelivery().getToday().getStatus().equalsIgnoreCase("0")) {
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            bun_later.performClick();
                                                        }
                                                    }, 1000);
                                                } else {
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            bun_today.performClick();
                                                        }
                                                    }, 1000);
                                                }
                                            } else {

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        bun_asap.performClick();
                                                    }
                                                }, 1000);
                                            }
                                        }
                                    }
                                });


                        collection_but.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        update_mode.setVisibility(View.GONE);
                                        delivery_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_two));
                                        collection_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_one));
                                        delivery_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));
                                        delivery_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));
                                        colli_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));
                                        colloetion_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));

                                        loading_imageView1.setVisibility(View.GONE);
                                        loading_imageView2.setVisibility(View.VISIBLE);
                                        order_mode = "1";//Collection

                                        if (response.body().getData().getCollection().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                            sevenday_txt.setVisibility(View.GONE);

                                        } else {
                                           // sevenday_txt.setVisibility(View.VISIBLE);
                                            sevenday_txt.setText("Select a collection time" + "\n" + " up to 7 days in advance");
                                        }

                                        if (response.body().getData().getCollection().getStatus().equalsIgnoreCase("0")) {
                                            card_change.setVisibility(View.GONE);
                                            today_time_layer.setVisibility(View.GONE);
                                            later_time_layer.setVisibility(View.GONE);
                                            update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                            update_mode.setClickable(false);
                                            update_mode.setFocusable(false);
                                            update_mode.setEnabled(false);
                                            colloetion_tattime.setText("Unavailable");
                                            menu_collection_tattime = "Unavailable";
                                            update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                            update_mode.setText("Takeaway Closed for Collection");
                                            update_mode.setVisibility(View.VISIBLE);
                                            sevenday_txt.setVisibility(View.VISIBLE);
                                            shimmer_view_preorder.stopShimmerAnimation();
                                            shimmer_view_preorder.setVisibility(View.GONE);

                                        } else {

                                            card_change.setVisibility(View.VISIBLE);
                                            colloetion_tattime.setText(response.body().getData().getCollection().getCooking_time());
                                            menu_collection_tattime = response.body().getData().getCollection().getCooking_time();

                                            if (response.body().getData().getCollection().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                if (response.body().getData().getCollection().getToday().getStatus().equalsIgnoreCase("0")) {

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            bun_later.performClick();

                                                        }
                                                    }, 1000);

                                                } else {

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            bun_today.performClick();
                                                        }
                                                    }, 1000);


                                                }
                                            } else {

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        bun_asap.performClick();
                                                    }
                                                }, 1000);


                                            }

                                        }

                                    }
                                });


                        bun_asap.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        activetagstr = "1";


                                        todaytimestr = "";
                                        laterdatestr = "";
                                        latertimestr = "";


                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getAsap().getStatus().equalsIgnoreCase("0")) {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getAsap().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                                sevenday_txt.setVisibility(View.VISIBLE);
                                                card_change.setVisibility(View.VISIBLE);
                                                shimmer_view_preorder.stopShimmerAnimation();
                                                shimmer_view_preorder.setVisibility(View.GONE);


                                            } else {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                update_mode.setText("Deliver ASAP");
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                            }
                                        } else {
                                            if (response.body().getData().getCollection().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getAsap().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                update_mode.setText("Collection ASAP");
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                            }
                                        }

                                    }
                                });


                        bun_today.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        activetagstr = "2";
                                        laterdatestr = "";
                                        latertimestr = "";




                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getToday().getStatus().equalsIgnoreCase("0")) {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getToday().getMessage());


                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                            } else {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                // update_mode.setText("Deliver today at " + response.body().getData().getDelivery().getToday().getToday_time().get(0).getToday_time());


                                                today_time_layer.setVisibility(View.VISIBLE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                                ArrayList<AdapterListData> todaytimeitem = new ArrayList<AdapterListData>();

                                                for (int i = 0; i < response.body().getData().getDelivery().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem.add(new AdapterListData(
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time_string(),
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time(),
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).gettoday_label()

                                                    ));

                                                }

                                              *//*  String[] todaytimeitem = new String[response.body().getData().getDelivery().getToday().getToday_time().size()];

                                                for (int i = 0; i < response.body().getData().getDelivery().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem[i] = response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time();

                                                }*//*

                                              *//*  String[] todaytimeitem = new String[response.body().getData().getDelivery().getToday().getToday_time().size()];
                                                for (int i = 0; i < response.body().getData().getDelivery().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem[i] = response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time_string();

                                                }*//*

                                                ArrayAdapter<AdapterListData> todaytimeadapter;
                                                todaytimeadapter = new ArrayAdapter<AdapterListData>(mContext, android.R.layout.simple_list_item_1, todaytimeitem);
                                                //setting adapter to spinner
                                                today_time.setAdapter(todaytimeadapter);

                                                today_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        // selectedtodaytimeItem = parent.getItemAtPosition(position).toString();

                                                        AdapterListData  todaytime = (AdapterListData)parent.getItemAtPosition(position);

                                                        //Toast.makeText(getApplicationContext(),todaytime.today_time_string, Toast.LENGTH_LONG).show();

                                                        //   update_mode.setText("Deliver today at " + todaytime.today_time);

                                                        update_mode.setText("Deliver "+ todaytime.label +" at " + todaytime.today_time);

                                                        todaytimestr =todaytime.today_time;
                                                        todaytimestring = todaytime.today_time_string;



                                                    *//*    update_mode.setText("Deliver today at " + selectedtodaytimeItem);
                                                        todaytimestr = selectedtodaytimeItem;*//*
                                                        update_mode.setVisibility(View.VISIBLE);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });


                                            }
                                        } else {

                                            if (response.body().getData().getCollection().getToday().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getToday().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                //  update_mode.setText("Collection today at " + response.body().getData().getCollection().getToday().getToday_time().get(0).getToday_time());
                                                today_time_layer.setVisibility(View.VISIBLE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);


                                                ArrayList<AdapterListData> todaytimeitem = new ArrayList<AdapterListData>();

                                                for (int i = 0; i < response.body().getData().getDelivery().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem.add(new AdapterListData(
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time_string(),
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).getToday_time(),
                                                            response.body().getData().getDelivery().getToday().getToday_time().get(i).gettoday_label()
                                                    ));

                                                }


                                               *//* String[] todaytimeitem = new String[response.body().getData().getCollection().getToday().getToday_time().size()];
                                                for (int i = 0; i < response.body().getData().getCollection().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem[i] = response.body().getData().getCollection().getToday().getToday_time().get(i).getToday_time();
                                                }*//*

                                                ArrayAdapter<AdapterListData> todaytimeadapter;
                                                todaytimeadapter = new ArrayAdapter<AdapterListData>(mContext, android.R.layout.simple_list_item_1, todaytimeitem);
                                                //setting adapter to spinner
                                                today_time.setAdapter(todaytimeadapter);


                                                today_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        // selectedtodaytimeItem = parent.getItemAtPosition(position).toString();

                                                        AdapterListData  todaytime = (AdapterListData)parent.getItemAtPosition(position);
                                                        //  Toast.makeText(getApplicationContext(),todaytime.today_time_string, Toast.LENGTH_LONG).show();
                                                        update_mode.setText("Collection "+todaytime.label+" at " + todaytime.today_time);
                                                        todaytimestr = todaytime.today_time;
                                                        todaytimestring = todaytime.today_time_string;
                                                        update_mode.setVisibility(View.VISIBLE);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });


                                            }
                                        }


                                    }


                                });


                        bun_later.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        activetagstr = "3";
                                        todaytimestr = "";

                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getLater_array().getStatus().equalsIgnoreCase("0")) {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getLater_array().getMessage());
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                            } else {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.VISIBLE);
                                                update_mode.setVisibility(View.VISIBLE);

                                                String[] laterdateitem = new String[response.body().getData().getDelivery().getLater_array().getLater_date().size()];
                                                for (int i = 0; i < response.body().getData().getDelivery().getLater_array().getLater_date().size(); i++) {
                                                    //Storing names to string array
                                                    laterdateitem[i] = response.body().getData().getDelivery().getLater_array().getLater_date().get(i).getLater_date();
                                                }

                                                ArrayAdapter<String> laterdateadapter;
                                                laterdateadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, laterdateitem);
                                                //setting adapter to spinner
                                                later_date.setAdapter(laterdateadapter);


                                                later_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        selectedlaterdateItem = parent.getItemAtPosition(position).toString();
                                                        // Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();

                                                        // update_mode.setText("Deliver at " + selectedlaterdateItem);
                                                        laterdatestr = selectedlaterdateItem;
                                                        loadLatertime("0", selectedlaterdateItem);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });


                                            }
                                        } else {
                                            if (response.body().getData().getCollection().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getLater_array().getMessage());
                                                update_mode.setVisibility(View.VISIBLE);
                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.GONE);
                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.menu_txt_tr));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));


                                                today_time_layer.setVisibility(View.GONE);
                                                later_time_layer.setVisibility(View.VISIBLE);
                                                update_mode.setVisibility(View.VISIBLE);

                                                String[] laterdateitem = new String[response.body().getData().getCollection().getLater_array().getLater_date().size()];
                                                for (int i = 0; i < response.body().getData().getCollection().getLater_array().getLater_date().size(); i++) {
                                                    //Storing names to string array
                                                    laterdateitem[i] = response.body().getData().getCollection().getLater_array().getLater_date().get(i).getLater_date();
                                                }

                                                ArrayAdapter<String> laterdateadapter;
                                                laterdateadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, laterdateitem);
                                                //setting adapter to spinner
                                                later_date.setAdapter(laterdateadapter);


                                                later_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        selectedlaterdateItem = parent.getItemAtPosition(position).toString();
                                                        // Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();
                                                        laterdatestr = selectedlaterdateItem;
                                                        // update_mode.setText("Collection at " + selectedlaterdateItem);
                                                        loadLatertime("1", selectedlaterdateItem);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });
                                            }
                                        }

                                    }

                                    *//*---------------------------load Later Time----------------------------------------------------*//*
                                    private void loadLatertime(String ordermodeing, String laterdates) {

                                        // get user data from session
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("later_time", laterdates);
                                        params.put("order_type", ordermodeing);


                                        mlaterdatefullUrl = menuurlpath + "/loadLaterTime";


                                        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                                        Call<getlatertime_model> call = apiService.loadLaterTime(mlaterdatefullUrl, params);
                                        call.enqueue(new Callback<getlatertime_model>() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onResponse(Call<getlatertime_model> call, Response<getlatertime_model> response) {

                                                //response.headers().get("Set-Cookie");
                                                int statusCode = response.code();
                                                if (statusCode == 200) {
                                                    if (response.body().getData().getStatus().equalsIgnoreCase("true")) {

                                                        later_timing_layer.setVisibility(View.VISIBLE);


                                                        ArrayList<AdapterListData> latertimeitem = new ArrayList<AdapterListData>();

                                                        for (int i = 0; i <response.body().getData().getLater_time().size(); i++) {
                                                            //Storing names to string array

                                                            latertimeitem.add(new AdapterListData(
                                                                    response.body().getData().getLater_time().get(i).getLater_time_string(),
                                                                    response.body().getData().getLater_time().get(i).getLater_time(),""
                                                            ));

                                                        }



                                                      *//*
                                                        String[] latertimeitem = new String[response.body().getData().getLater_time().size()];
                                                        for (int i = 0; i < response.body().getData().getLater_time().size(); i++) {
                                                            //Storing names to string array
                                                            latertimeitem[i] = response.body().getData().getLater_time().get(i).getLater_time();
                                                        }*//*

                                                        //response.body().getData().getLater_time().get(i).getLater_time_string()

                                                        ArrayAdapter<AdapterListData> latertimeadapter;
                                                        latertimeadapter = new ArrayAdapter<AdapterListData>(mContext, android.R.layout.simple_list_item_1, latertimeitem);
                                                        //setting adapter to spinner
                                                        later_time.setAdapter(latertimeadapter);


                                                        later_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                                AdapterListData  latertime = (AdapterListData)parent.getItemAtPosition(position);


                                                              *//*  update_mode.setText("Collection today at " + todaytime.today_time);
                                                                todaytimestr = todaytime.today_time;
                                                                todaytimestring = todaytime.today_time_string;*//*

                                                                //  selectedlatertimeItem = parent.getItemAtPosition(position).toString();


                                                                if (ordermodeing.equalsIgnoreCase("0")) {
                                                                    update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                                    update_mode.setClickable(true);
                                                                    update_mode.setFocusable(true);
                                                                    update_mode.setEnabled(true);
                                                                    update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                                    update_mode.setText("Deliver " +laterdates+ " at " +latertime.today_time);
                                                                    update_mode.setVisibility(View.VISIBLE);
                                                                    latertimestr = latertime.today_time;
                                                                    latertimestring = latertime.today_time_string;

                                                                } else {
                                                                    update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                                    update_mode.setClickable(true);
                                                                    update_mode.setFocusable(true);
                                                                    update_mode.setEnabled(true);
                                                                    update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                                    update_mode.setText("Collection " +laterdates+ " at " + latertime.today_time);
                                                                    update_mode.setVisibility(View.VISIBLE);
                                                                    latertimestr = latertime.today_time;
                                                                    latertimestring = latertime.today_time_string;

                                                                }
                                                            } // to close the onItemSelected

                                                            public void onNothingSelected(AdapterView<?> parent) {
                                                            }
                                                        });
                                                    } else {
                                                        later_timing_layer.setVisibility(View.GONE);
                                                        update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                        update_mode.setClickable(false);
                                                        update_mode.setFocusable(false);
                                                        update_mode.setEnabled(false);
                                                        update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                        update_mode.setText("Later Unavailable");
                                                        update_mode.setVisibility(View.VISIBLE);

                                                    }
                                                }
                                            }


                                            @Override
                                            public void onFailure(Call<getlatertime_model> call, Throwable t) {

                                                //Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                                                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });


                        update_mode.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("2")){
                                            addonitem(view,position,holder);
                                            SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                            editor_extra.putString("pop_up_show","3");
                                            editor_extra.commit();
                                        }



                                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                        editor_extra.putString("ordermodetype", order_mode);
                                        editor_extra.putString("orderactivetag", activetagstr);
                                        editor_extra.putString("ordertodattime", todaytimestr);
                                        editor_extra.putString("orderlaterdate", laterdatestr);
                                        editor_extra.putString("orderlatertime", latertimestr);
                                        editor_extra.putString("todaytimestring",todaytimestring);
                                        editor_extra.putString("latertimestring",latertimestring);
                                        editor_extra.commit();

                                        //  menugetitem(menuurlpath, sharedpreferences.getString("ordermodetype", null), key_postcode, key_area, key_address);//menu item call api
                                        Log.e("order_mode_details1", "" + order_mode);
                                        Log.e("order_mode_details2", "" + activetagstr);
                                        Log.e("order_mode_details3", "" + todaytimestr);
                                        Log.e("order_mode_details4", "" + laterdatestr);
                                        Log.e("order_mode_details5", "" + latertimestr);

                                        dialog.dismiss();

                                    }
                                });

                    }else{
                        dialog.dismiss();

                        Dialog takeway_colse = new Dialog(mContext);
                        takeway_colse.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        takeway_colse.setContentView(R.layout.takeway_status);

                        TextView browse_menu_textview = takeway_colse.findViewById(R.id.browse_menu_textview);
                        TextView brower_others = takeway_colse.findViewById(R.id.brower_others);

                        browse_menu_textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                takeway_colse.dismiss();
                            }
                        });

                        brower_others.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mContext.startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                               // finish();
                            }
                        });


                        *//*Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                delivery_collection_textview.setText("Delivery");
                                cooking_time_textview.setText("Closed");
                                del_col_anim.setAnimation(R.raw.delivery);
                                del_col_anim.playAnimation();

                            }
                        }, 3000);*//*

                        takeway_colse.show();
                        takeway_colse.setCancelable(false);
                        takeway_colse.setCanceledOnTouchOutside(false);
                        takeway_colse.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        takeway_colse.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        takeway_colse.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        takeway_colse.getWindow().setGravity(Gravity.BOTTOM);
                    }


                } else {
                    //Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<modeof_order_popup_model> call, Throwable t) {
                //  Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });




        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }*/

    private int getContactsCount() {

        cursor = dbHelper.numberOfRows();

        Log.e("c", "" + cursor);
        //Log.e("totalitemamut12", "" + totalitemamut);
        return cursor;

    }

    private void addonitem(View view, int position, ViewHolder holder) {

                loadingshow();

                Map<String, String> params = new HashMap<String, String>();

           if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {
                    params.put("id",searchitemname[position].getId());
                    params.put("ordermode", sharedpreferences.getString("ordermodetype", null));
                    params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                    params.put("dates", "");
                    params.put("time", "");
                } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {
                    params.put("id", searchitemname[position].getId());
                    params.put("ordermode", sharedpreferences.getString("ordermodetype", null));
                    params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                    params.put("dates", "");
                    params.put("time", sharedpreferences.getString("ordertodattime", null));
                    params.put("date_string",sharedpreferences.getString("todaytimestring", null));
                } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
                    params.put("id", searchitemname[position].getId());
                    params.put("ordermode", sharedpreferences.getString("ordermodetype", null));
                    params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                    params.put("dates", sharedpreferences.getString("orderlaterdate", null));
                    params.put("time", sharedpreferences.getString("orderlatertime", null));
                    params.put("date_string",sharedpreferences.getString("latertimestring", null));
                }


                fullUrl = menuurlpath + "/menu" + "/itemadd";
                ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                Call<menu_addon_status_model> call = apiService.menuaddon(fullUrl, params);

                Log.e("params1", ": " + searchitemname[position].getId());
                Log.e("params2", ": " + ordertypevalue);
                Log.e("params3", ": " + fullUrl);
                Log.e("params4", ": " + params);


                call.enqueue(new Callback<menu_addon_status_model>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call<menu_addon_status_model> call, Response<menu_addon_status_model> response) {
                        //response.headers().get("Set-Cookie");

                        int statusCode = response.code();

                        if (statusCode == 200) {

                            Log.e("statusfor1", ": " + response.body().getStatus());
                            Log.e("statusfor2", ": " + response.body().getError_code());


                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                if (response.body().getError_code().equalsIgnoreCase("0")) {
   /*                                 hideloading();

                                    int  userList = dbHelper.GetUserByUserId(parseInt(searchitemname[position].getId()));

                                    if(userList == 0){


                                    if (dbHelper.insertItem(searchitemname[position].getName(), searchitemname[position].getId(), "",
                                            "", "", searchitemname[position].getPrice(), "1",
                                            searchitemname[position].getPrice(), searchitemname[position].getPrice(), "", "")) {
                                        Toast.makeText(mContext, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent("item_successfully_custom-message");

                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                    } else {
                                        Toast.makeText(mContext, "Could not Insert Item", Toast.LENGTH_SHORT).show();
                                    }

                                    }else{



                                        ArrayList<HashMap<String, String>> qtypice = dbHelper.Getqtyprice(parseInt(searchitemname[position].getId()));


                                        for (int i=0;i<qtypice.size();i++)
                                        {
                                            HashMap<String, String> hashmap= qtypice.get(i);

                                            updateqty = hashmap.get("qty");
                                            updatefinalamt = hashmap.get("itemfinalamt");
                                        }

                                        int database_qty =  Math.round(Float.parseFloat(updateqty));

                                        int qty  = database_qty + 1;

                                        String price = searchitemname[position].getPrice();

                                        float total_amt = Float.parseFloat(price) * qty;

                                        Boolean updatevalue  =  dbHelper.Updateqtyprice(parseInt(searchitemname[position].getId()),qty,total_amt);

                                        Toast.makeText(mContext, "Item Added Successfully", Toast.LENGTH_SHORT).show();


                                    }*/


                                    hideloading();


                                    int  userList = dbHelper.GetUserByUserId(parseInt(searchitemname[position].getId()));

                                    if(userList == 0){


                                        if (dbHelper.insertItem(searchitemname[position].getName(), searchitemname[position].getId(), "", "",
                                                "", searchitemname[position].getPrice(), "1", searchitemname[position].getPrice(),
                                                searchitemname[position].getPrice(), "", "")) {

                                            Log.d("SearchItemAdapter",searchitemname[position].getName()+ "\n" + searchitemname[position].getId()+ "\n" +
                                                    searchitemname[position].getPrice()
                                            );


                                            holder.menu_item_add.setVisibility(GONE);
                                            holder.increment_decrement_layout.setVisibility(View.VISIBLE);


                                            Intent intent = new Intent("bottom_btn_hidden");
                                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                            Customertoastmessage(view);


                                        } else {

                                            Toast.makeText(mContext, "Could not Insert Item", LENGTH_SHORT).show();

                                        }

                                    }else{

                                        count = Integer.parseInt(String.valueOf(holder.qty_textview_number.getText()));
                                        count++;
                                        length = String.valueOf(count).length();

                                        if (length == 1) {

                                            updateqtyandprice(view,searchitemname[position].getId(),searchitemname[position].getPrice());
                                            holder.qty_textview_number.setText("0" + count);


                                        }else{

                                            updateqtyandprice(view,searchitemname[position].getId(),searchitemname[position].getPrice());
                                            holder.qty_textview_number.setText("" + count);


                                        }


                                    }




                                }

                            } else {

                                if (response.body().getError_code().equalsIgnoreCase("300")) {
                                    hideloading();
                                    holder.showPopup(view, "Takeaway Closed", response.body().getError_message().getTakeawayclosed().getDbname(), "300");

                                } else if (response.body().getError_code().equalsIgnoreCase("101")) {
                                    hideloading();
                                    holder.showPopup(view, response.body().getError_message().getChooseMode().getPopupdelivery(), response.body().getError_message().getChooseMode().getOnlycollection(), "101");

                                } else if (response.body().getError_code().equalsIgnoreCase("200")) {
                                    hideloading();
                                    holder.showPopup(view, "Delivery Closed", response.body().getError_message().getMsg(), "200");
                                } else if (response.body().getError_code().equalsIgnoreCase("201")) {
                                    hideloading();
                                    holder.showPopup(view, "Delivery Closed", response.body().getError_message().getMsg(), "201");
                                } else if (response.body().getError_code().equalsIgnoreCase("102")) {
                                    //addon show
                                  /*  String ItemName = searchitemname[position].getId();
                                    Intent intent = new Intent("custom-message");
                                    intent.putExtra("item", ItemName);
                                    intent.putExtra("addonid", response.body().getAddonId());
                                    intent.putExtra("categoryname", "");
                                    intent.putExtra("addonchoosepopup","");
                                    intent.putExtra("subcategoryname", "");
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                    hideloading();*/

                                    hideloading();

                                    int check_user_data = dbHelper.GetUserByUserId(parseInt(searchitemname[position].getId()));

                                    if(check_user_data == 0){

                                        String ItemName = searchitemname[position].getId();
                                        Intent intent = new Intent("custom-message");
                                        intent.putExtra("item", ItemName);
                                        intent.putExtra("addonid", response.body().getAddonId());
                                        intent.putExtra("categoryname", "");
                                        intent.putExtra("addonchoosepopup","");
                                        intent.putExtra("subcategoryname", "");
                                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                        LocalBroadcastManager.getInstance(mContext).registerReceiver(new BroadcastReceiver() {
                                            @Override
                                            public void onReceive(Context context, Intent intent) {

                                                String itempossion = intent.getStringExtra("item_id_activity");
                                                holder.menu_item_add.setVisibility(GONE);
                                                holder.increment_decrement_layout.setVisibility(View.VISIBLE);

                                            }
                                        }, new IntentFilter("add_on_btn_enable_adapter"));

                                    }else {

                                        ArrayList<HashMap<String, String>> item_addon = dbHelper.GetUserdetails(parseInt(searchitemname[position].getId()));

                                        for (int i=0;i<item_addon.size();i++)
                                        {
                                            HashMap<String, String> hashmap= item_addon.get(i);
                                            item_user_add_name = hashmap.get("ITEM_ADDON_NAME");

                                        }
                                        repeatpopup = new Dialog(mContext);
                                        repeatpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        repeatpopup.setContentView(R.layout.repeat_popup_design);

                                        ImageView repeat_gif = repeatpopup.findViewById(R.id.repeat_gif);
                                        TextView repeat_popup_textview = repeatpopup.findViewById(R.id.repeat_popup_textview);
                                        TextView add_more_button_textview = repeatpopup.findViewById(R.id.add_more_button_textview);
                                        TextView item_description_textview = repeatpopup.findViewById(R.id.item_description_textview);
                                        item_description_textview.setText(item_user_add_name);

                                        repeat_popup_textview.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent btn_hide = new Intent("bottom_btn_hidden");
                                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(btn_hide);



                                                count = Integer.parseInt(String.valueOf(holder.qty_textview_number.getText()));
                                                count++;
                                                length = String.valueOf(count).length();

                                                if (length == 1) {
                                                    String ItemName = searchitemname[position].getId();
                                                    Intent intent = new Intent("custom-message");
                                                    intent.putExtra("item", ItemName);
                                                    intent.putExtra("addonid", response.body().getAddonId());
                                                    intent.putExtra("categoryname", "");
                                                    intent.putExtra("addonchoosepopup","");
                                                    intent.putExtra("subcategoryname", "");
                                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                                    repeatpopup.dismiss();
                                                    holder.qty_textview_number.setText("0" + count);



                                                } else {
                                                    String ItemName = searchitemname[position].getId();
                                                    Intent intent = new Intent("custom-message");
                                                    intent.putExtra("item", ItemName);
                                                    intent.putExtra("addonid", response.body().getAddonId());
                                                    intent.putExtra("categoryname", "");
                                                    intent.putExtra("addonchoosepopup","");
                                                    intent.putExtra("subcategoryname", "");
                                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                                    repeatpopup.dismiss();
                                                    holder.qty_textview_number.setText("" + count);

                                                }

                                                Customertoastmessage(view);

                                            }


                                        });

                                        add_more_button_textview.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                              /*  String ItemName = items[position].getId();
                                                Intent intent = new Intent("custom-message");
                                                intent.putExtra("item", ItemName);
                                                intent.putExtra("addonid", response.body().getAddonId());
                                                intent.putExtra("categoryname", listdatum.getName());
                                                intent.putExtra("subcategoryname", sub.getName());
                                                intent.putExtra("addonchoosepopup","addonpopup");
                                                intent.putExtra("item_price_amt", items[position].getPrice());
                                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)*/;

                                                String ItemName = searchitemname[position].getId();
                                                Intent intent = new Intent("custom-message");
                                                intent.putExtra("item", ItemName);
                                                intent.putExtra("addonid", response.body().getAddonId());
                                                intent.putExtra("categoryname", "");
                                                intent.putExtra("addonchoosepopup","");
                                                intent.putExtra("subcategoryname", "");
                                                intent.putExtra("addonchoosepopup","addonpopup");
                                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                                repeatpopup.dismiss();
                                            }
                                        });

                                        repeatpopup.show();
                                        repeatpopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                        repeatpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        repeatpopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                        repeatpopup.getWindow().setGravity(Gravity.BOTTOM);

                                    }




                                } else {
                                    hideloading();
                                    Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                }

                            }


                        } else {
                            hideloading();
                            Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<menu_addon_status_model> call, Throwable t) {
                        hideloading();
                        Log.e("menuThrowable", "" + t);
                        Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                });



    }

    private void updateqtyandprice(View view, String user_id, String item_price) {


        ArrayList<HashMap<String, String>> qtypice = dbHelper.Getqtyprice(parseInt(user_id));

        for (int i=0;i<qtypice.size();i++)
        {
            HashMap<String, String> hashmap= qtypice.get(i);

            updateqty = hashmap.get("qty");
            updatefinalamt = hashmap.get("itemaddontotalamt");
        }

        int database_qty =  Math.round(Float.parseFloat(updateqty));

        int qty  = database_qty + 1;

        String price  = updatefinalamt;

        float total_amt = Float.parseFloat(price) * qty;
        Boolean updatevalue  =  dbHelper.Updateqtyprice(parseInt(user_id),qty,total_amt);

        Intent intent = new Intent("bottom_btn_hidden");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Customertoastmessage(view);
    }

    private void Customertoastmessage(View view) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) view.findViewById(R.id.custom_toast_layout));
        TextView tv = (TextView) layout.findViewById(R.id.txtvw);
        tv.setText("Wow! Item added Successfully");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
                Intent intent = new Intent("item_successfully_custom-message");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        }, 1000);

    }

    @Override
    public int getItemCount() {
        return searchitemname.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView item_name, item_price,qty_textview_number;
        LinearLayout menu_item_add,increment_decrement_layout;
        TextView qty_decrease_textview,qty_increase_textview;

        public ViewHolder(View itemView) {
            super(itemView);


            this.item_name = itemView.findViewById(R.id.item_name);
            this.item_price = itemView.findViewById(R.id.item_price);
            this.menu_item_add = itemView.findViewById(R.id.menu_item_add);
            this.qty_textview_number = itemView.findViewById(R.id.qty_textview_number);
            this.increment_decrement_layout = itemView.findViewById(R.id.increment_decrement_layout);
            this.qty_decrease_textview = itemView.findViewById(R.id.qty_decrease_textview);
            this.qty_increase_textview = itemView.findViewById(R.id.qty_increase_textview);


        }


        public void showPopup(View view, String title, String msg, String codes) {
            View popupView = LayoutInflater.from(mContext).inflate(R.layout.addon_popup, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //  Button takaway_btn_dismiss = popupView.findViewById(R.id.takaway_btn_dismiss);


            //300 Takeaway Closed
            //101 Delivery Closed
            //200 Delivery Closed collection only
            //201 Delivery Closed collection only
  /*  Intent intent = new Intent(mContext, Item_Menu_Activity.class);
                intent.putExtra("menuurlpath", listdata[position].getMenuurlpath());
                mContext.startActivity(intent);*/

            AppCompatButton browse = popupView.findViewById(R.id.browse);
            AppCompatButton update = popupView.findViewById(R.id.update);

            TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);
            TextView takaway_status = popupView.findViewById(R.id.takaway_status);

            //  ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);


            takaway_status.setText(title);
            takaway_status_dec.setText(msg);

            if (codes.equalsIgnoreCase("300")) {

                update.setText("Cancel");

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        popupWindow.dismiss();


                    }
                });

            } else if (codes.equalsIgnoreCase("101")) {
                update.setText("Collection");


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        popupWindow.dismiss();

                        Intent intent = new Intent("collection_only");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


                    }
                });

            } else if (codes.equalsIgnoreCase("200")) {
                update.setText("Collection");


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        popupWindow.dismiss();

                        Intent intent = new Intent("collection_only");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


                    }
                });

            } else if (codes.equalsIgnoreCase("201")) {
                update.setText("Collection");


                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        popupWindow.dismiss();

                        Intent intent = new Intent("collection_only");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


                    }
                });

            }

            browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    popupWindow.dismiss();

                    Intent intent = new Intent(mContext, Dashboard_Activity.class);
                    mContext.startActivity(intent);


                }
            });


            popupWindow.showAsDropDown(popupView, 0, 0);
        }
    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(mContext);
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




}