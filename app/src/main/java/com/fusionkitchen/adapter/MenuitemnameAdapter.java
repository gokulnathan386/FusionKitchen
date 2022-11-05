package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.text.Html.fromHtml;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fusionkitchen.activity.Postcode_Activity;
import com.fusionkitchen.model.AdapterListData;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.fusionkitchen.model.modeoforder.getlatertime_model;
import com.fusionkitchen.model.modeoforder.modeof_order_popup_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Html.fromHtml;
import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_LONG;
import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Integer.parseInt;

public class MenuitemnameAdapter extends RecyclerView.Adapter<MenuitemnameAdapter.ViewHolder> {

    private static Context mContext;
    private final menu_item_sub_model.categoryall.subcat sub;
    private final menu_item_sub_model.categoryall listdatum;

    private menu_item_sub_model.categoryall.subcat.items[] items;
    private Dialog dialog;

    private String menuurlpath, fullUrl;
    String selectedlaterdateItem,item_user_add_name;
    int clickable = 0,count;
    Dialog item_view,repeatpopup ;
    int length;
    HttpUrl baseUrl;
    String id_item,item_name,item_image,item_description,item_price;
    String item_count_increment_decrement;



    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    private SQLDBHelper dbHelper;

    private static long mLastClickTime = 0;
    String removeqty,removefinalamt;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";

    ArrayList<String> arr = new ArrayList<String>();

    String updateqty;
    String updatefinalamt;
    String ITEM_ADDON_NAME1;
    int cursor;
    String metdpasfullUrl,mlaterdatefullUrl;
    private Dialog dialog_loading;
    String todaytimestr, laterdatestr, latertimestr, activetagstr, laterdate;
    String order_mode,todaytimestring;
    String addonitemtype,latertimestring;

    // RecyclerView recyclerView;
    public MenuitemnameAdapter(Context mContext, List<menu_item_sub_model.categoryall.subcat.items> items, String menuurlpath, menu_item_sub_model.categoryall.subcat sub, menu_item_sub_model.categoryall listdatum) {

        this.items = items.toArray(new menu_item_sub_model.categoryall.subcat.items[0]);
        this.mContext = mContext;
        this.menuurlpath = menuurlpath;
        this.sub = sub;
        this.listdatum = listdatum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_item_deatils, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        baseUrl  =ApiClient.getInstance().getClient().baseUrl();

        holder.menu_item_name.setText(items[position].getName());
        holder.menu_item_desc.setText(fromHtml(items[position].getDescription().replaceAll("Â", ""), Html.FROM_HTML_MODE_COMPACT));
        holder.menu_item_amout.setText("£ " + items[position].getPrice());



        /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);
        Log.e("otypeaction", "" + sharedpreferences.getString("orderactivetag", null));

        if(items[position].getImage().equalsIgnoreCase("")){
            holder.layout_logo.setVisibility(GONE);

        }else{
            Picasso.get()
                    .load("https://fusionbucket.co.uk/img/menu/" + items[position].getImage())
                    .placeholder(R.drawable.hederlocoplaceimg)
                    .error(R.drawable.hederlocoplaceimg)
                    .into(holder.menu_item_image);
        }


        // Log.e("sixg", "" + orderhistory.size().getName());
        /*---------------------------Sql Lite DataBase----------------------------------------------------*/

            dbHelper = new SQLDBHelper(mContext);
            getContactsCount();


         //   Log.d("Availableitme",items[position].getAvailableTime());

         if(items[position].getAvailableTime().equalsIgnoreCase("")){
            holder.menu_item_add.setVisibility(View.VISIBLE);
            holder.textview_avaliable_time.setVisibility(GONE);
            }else{
                holder.textview_avaliable_time.setText(items[position].getAvailableTime());
                holder.menu_item_add.setVisibility(GONE);
            }


             if(items[position].getBestseller().equalsIgnoreCase("true")){
                 holder.bestseller_musttry_textview.setText("Best Seller");
                 holder.bestseller_musttry_drawable.setBackgroundResource(R.drawable.star_blue);
                 holder.seller_header.setBackgroundColor(Color.parseColor("#E6F3FE"));
             }else if(items[position].getMusttry().equalsIgnoreCase("true")){
                 holder.bestseller_musttry_textview.setText("Must Try");
                 holder.seller_header.setBackgroundColor(Color.parseColor("#FFF2F3"));
                 holder.bestseller_musttry_textview.setTextColor(Color.parseColor("#E0467C"));
                 holder.bestseller_musttry_drawable.setBackgroundResource(R.drawable.thumbs_up_icon);
             }else{
                 holder.seller_header.setVisibility(GONE);
                 holder.menu_item_list.setBackgroundResource(R.drawable.item_bg_border2);
             }


        holder.menu_item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.menu_item_add.setEnabled(false);

                holder.increment_decrement_layout.setVisibility(View.VISIBLE);
                holder.menu_item_add.setVisibility(GONE);

                if (cursor != 0) {
                        addonitem(view,position,holder);
                } else {
                        if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("1")){
                            addonitem(view,position,holder);
                        }else if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("2")){

                            Order_mode_popup(view,holder,position);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    holder.menu_item_add.setEnabled(true);
                                }
                            }, 4000);
                        }else{
                            addonitem(view,position,holder);
                        }
                }

            }
        });

        holder.layout_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              loadingshow();
              Single_itemviewpup_up(items[position].getId(),menuurlpath,mContext);
            }
        });


            holder.menu_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingshow();
                    Single_itemviewpup_up(items[position].getId(),menuurlpath,mContext);

                }
            });



        holder.qty_decrease_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                count= parseInt(String.valueOf(holder.qty_textview_number.getText()));

                if (count == 1) {
                    Decreasepriceqty(view,items[position].getId());
                    holder.qty_textview_number.setText("01");
                } else {
                    count -= 1;
                    length = String.valueOf(count).length();
                    if(length == 1){
                        Decreasepriceqty(view,items[position].getId());
                        holder.qty_textview_number.setText("0" + count);
                    }else{
                        Decreasepriceqty(view,items[position].getId());
                        holder.qty_textview_number.setText("" + count);
                    }

                }

            }
        });

        holder.qty_increase_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 addonitem(v,position,holder);
            }
        });
    }

    private void Decreasepriceqty(View view, String id) {

        ArrayList<HashMap<String, String>> qtypice = dbHelper.Remoeveqtyprice(parseInt(id));

        for (int i=0;i<qtypice.size();i++)
        {
            HashMap<String, String> hashmap= qtypice.get(i);

            removeqty = hashmap.get("qty");
            removefinalamt = hashmap.get("itemaddontotalamt");
        }

        int database_qty =  Math.round(Float.parseFloat(removeqty));

        int qty  = database_qty - 1;

        //String price = item_price;

        String price  = removefinalamt;

        float total_amt = Float.parseFloat(price) * qty;
        Boolean updatevalue  =  dbHelper.Updateqtyprice(parseInt(id),qty,total_amt);

        Intent intent = new Intent("item_successfully_custom-message");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


    }


    private void Single_itemviewpup_up(String item_id,String menu_url,Context context) {

        item_view = new Dialog(mContext);
        item_view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        item_view.setContentView(R.layout.raw_menu_single_itemview);

        item_view.setCancelable(true);
        item_view.setCanceledOnTouchOutside(true);

        LinearLayout Add_your_comment = item_view.findViewById(R.id.Add_your_comment);
        EditText Enter_your_comments = item_view.findViewById(R.id.Enter_your_comments);
        TextView special_instruction =item_view.findViewById(R.id.special_instruction);
        CardView back_btn_popup = item_view.findViewById(R.id.back_btn_popup);
        LinearLayout plus_linearlayout = item_view.findViewById(R.id.plus_linearlayout);
        LinearLayout add_to_cart_btn = item_view.findViewById(R.id.add_to_cart_btn);
        LinearLayout plus_minus_symbol = item_view.findViewById(R.id.plus_minus_symbol);
        TextView Enter_your_plus_symbol = item_view.findViewById(R.id.Enter_your_plus_symbol);
        LinearLayout minus_symbol =item_view.findViewById(R.id.minus_symbol);
        LinearLayout plus_symbol = item_view.findViewById(R.id.plus_symbol);
        TextView textview_qty = item_view.findViewById(R.id.textview_qty);
        ImageView single_item_image = item_view.findViewById(R.id.single_item_image);
        TextView item_name_textview = item_view.findViewById(R.id.item_name_textview);
        TextView item_price_textview = item_view.findViewById(R.id.item_price_textview);
        TextView item_description_textview = item_view.findViewById(R.id.item_description_textview);
        TextView textview_avaliable_time = item_view.findViewById(R.id.textview_avaliable_time);

        add_to_cart_btn.getBackground().setColorFilter(Color.parseColor("#DEDDDF"), PorterDuff.Mode.SRC_ATOP);
        add_to_cart_btn.setClickable(false);



        //start Single Item API Integration
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,baseUrl+menu_url+"/getitemdetail",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                             JSONObject jsonobject = new JSONObject(response);
                             String check_status =  jsonobject.getString("status");

                             hideloading();

                             if(check_status.equalsIgnoreCase("true")){

                                 JSONObject single_item_data = jsonobject.getJSONObject("data");

                                 id_item =  single_item_data.getString("id");
                                 item_name = single_item_data.getString("name");
                                 item_image = single_item_data.getString("image");
                                 item_description = single_item_data.getString("description");
                                 item_price =  single_item_data.getString("price");

                                if(item_image.equalsIgnoreCase("")){

                                    single_item_image.setVisibility(GONE);
                                    back_btn_popup.setVisibility(GONE);

                                }else{

                                     Picasso.get()
                                    .load("https://fusionbucket.co.uk/img/menu/" + item_image)
                                    .placeholder(R.drawable.hederlocoplaceimg)
                                    .error(R.drawable.hederlocoplaceimg)
                                    .into(single_item_image);

                                }


                                    item_name_textview.setText(item_name);

                                    item_price_textview.setText("£ " +item_price);

                                    if(item_description.equalsIgnoreCase("")){

                                        item_description_textview.setVisibility(GONE);

                                    }else{

                                        item_description_textview.setText(item_description);

                                    }
                             }

                        }catch (JSONException e) {

                            hideloading();

                            e.printStackTrace();

                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                    }
                }){

           @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("item_id",item_id);
                return params;
            }
        };

        RequestQueue requestqueue = Volley.newRequestQueue(context);
        requestqueue.add(stringRequest);

        //End Single Item API Integration

        Enter_your_plus_symbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus_linearlayout.setVisibility(GONE);
                plus_minus_symbol.setVisibility(View.VISIBLE);
                add_to_cart_btn.getBackground().setColorFilter(Color.parseColor("#FF276CF6"), PorterDuff.Mode.SRC_ATOP);
                add_to_cart_btn.setClickable(true);
                clickable = 1;
            }
        });


        plus_symbol.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int count= Integer.parseInt(String.valueOf(textview_qty.getText()));
                count++;
                int length = String.valueOf(count).length();
                if(length == 1){

                    textview_qty.setText("0" + count);
                    item_count_increment_decrement = "0"+count;

                }else{

                    textview_qty.setText("" + count);
                    item_count_increment_decrement = ""+count;

                }

            }
        });

        minus_symbol.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int count= Integer.parseInt(String.valueOf(textview_qty.getText()));

                if (count == 1) {
                    textview_qty.setText("01");
                } else {
                    count -= 1;
                    int length = String.valueOf(count).length();
                    if(length == 1){

                        textview_qty.setText("0" + count);
                        item_count_increment_decrement = "0"+count;

                    }else{

                        textview_qty.setText("" + count);
                        item_count_increment_decrement = ""+count;

                    }

                }

            }
        });



        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickable == 0){
                    Toast.makeText(getApplicationContext(), "Btn click false", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Btn click true", Toast.LENGTH_SHORT).show();
                    clickable = 0;
                }

            }
        });

        back_btn_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_view.dismiss();
            }
        });

        Add_your_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Enter_your_comments.getVisibility() == GONE) {
                    Enter_your_comments.setVisibility(View.VISIBLE);
                    special_instruction.setText("- Special Instructions");
                } else {
                    Enter_your_comments.setVisibility(GONE);
                    special_instruction.setText("+ Special Instructions");
                }
            }
        });


        //  Glide.with(this).load(R.drawable.heartgif).into(favourite_image);

        item_view.show();
        item_view.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        item_view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        item_view.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        item_view.getWindow().setGravity(Gravity.BOTTOM);
    }


    private void Order_mode_popup(View view, ViewHolder holder, int position) {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pre_order);

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

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        //Cooking time set
                        colloetion_tattime.setText(response.body().getData().getCollection().getCooking_time());
                        delivery_tattime.setText(response.body().getData().getDelivery().getCooking_time());


                        if (!response.body().getData().getDelivery().getStatus().equalsIgnoreCase("0")) {
                            loading();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismissloading();
                                    delivery_but.performClick();

                                }
                            }, 1000);


                        } else if (!response.body().getData().getCollection().getStatus().equalsIgnoreCase("0")) {
                            loading();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismissloading();
                                    collection_but.performClick();

                                }
                            }, 1000);

                        } else {
                            //  ordermode_popup_view.setVisibility(View.GONE);
                            //mode_view2.setVisibility(View.VISIBLE);
                           // mAddFab.setVisibility(View.VISIBLE);
                        }

                        //Top button click
                        delivery_but.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loading();
                                        // Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                                        delivery_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_one));
                                        collection_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_two));
                                        delivery_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));
                                        delivery_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));
                                        colli_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));
                                        colloetion_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));

                                        loading_imageView1.setVisibility(View.VISIBLE);
                                        loading_imageView2.setVisibility(GONE);

                                        order_mode = "0";//Delivery

                                        if (response.body().getData().getDelivery().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                            sevenday_txt.setVisibility(GONE);
                                        } else {
                                            sevenday_txt.setVisibility(View.VISIBLE);
                                            sevenday_txt.setText("Select a delivery time" + "\n" + " up to 7 days in advance");
                                        }

                                        if (response.body().getData().getDelivery().getStatus().equalsIgnoreCase("0")) {
                                            card_change.setVisibility(GONE);
                                            today_time_layer.setVisibility(GONE);
                                            later_time_layer.setVisibility(GONE);
                                            update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                            update_mode.setClickable(false);
                                            update_mode.setFocusable(false);
                                            update_mode.setEnabled(false);
                                            delivery_tattime.setText("Unavailable");
                                            update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                            update_mode.setText("Takeaway Closed for Delivery");
                                            update_mode.setVisibility(View.VISIBLE);
                                            dismissloading();
                                        } else {

                                            card_change.setVisibility(View.VISIBLE);
                                            delivery_tattime.setText(response.body().getData().getDelivery().getCooking_time());
                                            dismissloading();
                                            if (response.body().getData().getDelivery().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                if (response.body().getData().getDelivery().getToday().getStatus().equalsIgnoreCase("0")) {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_later.performClick();
                                                        }
                                                    }, 1000);
                                                } else {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_today.performClick();
                                                        }
                                                    }, 1000);
                                                }
                                            } else {
                                                loading();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dismissloading();
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
                                        loading();
                                        delivery_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_two));
                                        collection_but.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.pre_mode_bg_one));
                                        delivery_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));
                                        delivery_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_two));
                                        colli_txt.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));
                                        colloetion_tattime.setTextColor(ContextCompat.getColor(mContext, R.color.pre_mode_txt_one));

                                        loading_imageView1.setVisibility(GONE);
                                        loading_imageView2.setVisibility(View.VISIBLE);
                                        order_mode = "1";//Collection

                                        if (response.body().getData().getCollection().getLater_array().getStatus().equalsIgnoreCase("0")) {
                                            sevenday_txt.setVisibility(GONE);

                                        } else {
                                            sevenday_txt.setVisibility(View.VISIBLE);
                                            sevenday_txt.setText("Select a collection time" + "\n" + " up to 7 days in advance");
                                        }

                                        if (response.body().getData().getCollection().getStatus().equalsIgnoreCase("0")) {
                                            card_change.setVisibility(GONE);
                                            today_time_layer.setVisibility(GONE);
                                            later_time_layer.setVisibility(GONE);
                                            update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                            update_mode.setClickable(false);
                                            update_mode.setFocusable(false);
                                            update_mode.setEnabled(false);
                                            colloetion_tattime.setText("Unavailable");
                                            update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                            update_mode.setText("Takeaway Closed for Collection");
                                            update_mode.setVisibility(View.VISIBLE);
                                            dismissloading();
                                        } else {
                                            dismissloading();
                                            card_change.setVisibility(View.VISIBLE);

                                            colloetion_tattime.setText(response.body().getData().getCollection().getCooking_time());
                                            if (response.body().getData().getCollection().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                if (response.body().getData().getCollection().getToday().getStatus().equalsIgnoreCase("0")) {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_later.performClick();

                                                        }
                                                    }, 1000);

                                                } else {
                                                    loading();
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dismissloading();
                                                            bun_today.performClick();
                                                        }
                                                    }, 1000);
                                                }
                                            } else {
                                                loading();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dismissloading();
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

                                        loading();

                                        activetagstr = "1";


                                        todaytimestr = "";
                                        laterdatestr = "";
                                        latertimestr = "";


                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getAsap().getStatus().equalsIgnoreCase("0")) {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getAsap().getMessage());
                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();

                                            } else {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                update_mode.setText("Deliver ASAP");
                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            }
                                        } else {
                                            if (response.body().getData().getCollection().getAsap().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getAsap().getMessage());
                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap_active);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                update_mode.setText("Collection ASAP");
                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            }
                                        }

                                    }
                                });


                        bun_today.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loading();
                                        activetagstr = "2";
                                        laterdatestr = "";
                                        latertimestr = "";




                                        if (order_mode.equalsIgnoreCase("0")) {
                                            if (response.body().getData().getDelivery().getToday().getStatus().equalsIgnoreCase("0")) {


                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getToday().getMessage());


                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();
                                            } else {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                // update_mode.setText("Deliver today at " + response.body().getData().getDelivery().getToday().getToday_time().get(0).getToday_time());


                                                today_time_layer.setVisibility(View.VISIBLE);
                                                later_time_layer.setVisibility(GONE);
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

                                                        if(todaytimestr.equalsIgnoreCase("Mid Night")){
                                                            update_mode.setText("Deliver "+ todaytime.label +" at 12:00" );
                                                        }

                                                    /*    update_mode.setText("Deliver today at " + selectedtodaytimeItem);
                                                        todaytimestr = selectedtodaytimeItem;*/
                                                        update_mode.setVisibility(View.VISIBLE);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });

                                                dismissloading();

                                            }
                                        } else {

                                            if (response.body().getData().getCollection().getToday().getStatus().equalsIgnoreCase("0")) {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getToday().getMessage());
                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);
                                                dismissloading();

                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today_active);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                bun_later.setBackgroundResource(R.drawable.background_later);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                //  update_mode.setText("Collection today at " + response.body().getData().getCollection().getToday().getToday_time().get(0).getToday_time());
                                                today_time_layer.setVisibility(View.VISIBLE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);


                                                ArrayList<AdapterListData> todaytimeitem = new ArrayList<AdapterListData>();

                                                for (int i = 0; i < response.body().getData().getCollection().getToday().getToday_time().size(); i++) {
                                                    //Storing names to string array
                                                    todaytimeitem.add(new AdapterListData(
                                                            response.body().getData().getCollection().getToday().getToday_time().get(i).getToday_time_string(),
                                                            response.body().getData().getCollection().getToday().getToday_time().get(i).getToday_time(),
                                                            response.body().getData().getCollection().getToday().getToday_time().get(i).gettoday_label()
                                                    ));

                                                }

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
                                                        if(todaytimestr.equalsIgnoreCase("Mid Night")){
                                                            update_mode.setText("Collection "+ todaytime.label +" at 12:00 " );
                                                        }
                                                        update_mode.setVisibility(View.VISIBLE);
                                                    } // to close the onItemSelected

                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });

                                                dismissloading();
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
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));


                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getDelivery().getLater_array().getMessage());
                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                                update_mode.setVisibility(View.VISIBLE);

                                            } else {

                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                                                today_time_layer.setVisibility(GONE);
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
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                update_mode.setClickable(false);
                                                update_mode.setFocusable(false);
                                                update_mode.setEnabled(false);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                update_mode.setText(response.body().getData().getCollection().getLater_array().getMessage());
                                                update_mode.setVisibility(View.VISIBLE);
                                                today_time_layer.setVisibility(GONE);
                                                later_time_layer.setVisibility(GONE);
                                            } else {
                                                bun_asap.setBackgroundResource(R.drawable.background_asap);
                                                bun_asap.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_today.setBackgroundResource(R.drawable.background_today);
                                                bun_today.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_per_order));
                                                bun_later.setBackgroundResource(R.drawable.background_later_active);
                                                bun_later.setTextColor(ContextCompat.getColor(mContext, R.color.white));

                                                update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.pre_mode_txt_one));
                                                update_mode.setClickable(true);
                                                update_mode.setFocusable(true);
                                                update_mode.setEnabled(true);
                                                update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.white));


                                                today_time_layer.setVisibility(GONE);
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

                                    /*---------------------------load Later Time----------------------------------------------------*/
                                    private void loadLatertime(String ordermodeing, String laterdates) {
                                        loading();
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


                                                        ArrayAdapter<AdapterListData> latertimeadapter;
                                                        latertimeadapter = new ArrayAdapter<AdapterListData>(mContext, android.R.layout.simple_list_item_1, latertimeitem);
                                                        //setting adapter to spinner
                                                        later_time.setAdapter(latertimeadapter);


                                                        later_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                                AdapterListData  latertime = (AdapterListData)parent.getItemAtPosition(position);


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
                                                                    dismissloading();
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
                                                                    dismissloading();
                                                                }
                                                            } // to close the onItemSelected

                                                            public void onNothingSelected(AdapterView<?> parent) {
                                                            }
                                                        });
                                                    } else {
                                                        later_timing_layer.setVisibility(GONE);
                                                        update_mode.setBackgroundColor(update_mode.getContext().getResources().getColor(R.color.modeofitem_disable));
                                                        update_mode.setClickable(false);
                                                        update_mode.setFocusable(false);
                                                        update_mode.setEnabled(false);
                                                        update_mode.setTextColor(ContextCompat.getColor(mContext, R.color.modeofitem_disable_txt));
                                                        update_mode.setText("Later Unavailable");
                                                        update_mode.setVisibility(View.VISIBLE);
                                                        dismissloading();
                                                    }
                                                } else {
                                                    dismissloading();
                                                   // Snackbar.make(mContext.dialog.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                                                }
                                            }


                                            @Override
                                            public void onFailure(Call<getlatertime_model> call, Throwable t) {
                                                dismissloading();
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

                                        holder.menu_item_add.setEnabled(true);

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

                    } else {
                        // dismissloading();
                        //  ordermode_popup_view.setVisibility(View.INVISIBLE);
                        //      mode_view2.setVisibility(View.INVISIBLE);
                      //  mAddFab.setVisibility(View.VISIBLE);


                     //   menugetitem(menuurlpath, "0", key_postcode, key_area, key_address);//menu item call api

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

    }

    /*-------------------Loading Images------------------*/
    public void loading() {

        dialog_loading = new Dialog(mContext);
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loading.setCancelable(false);
        dialog_loading.setContentView(R.layout.custom_loading);
        ImageView ImageViewgif = dialog_loading.findViewById(R.id.custom_searchloader_ImageView);
        Glide.with(mContext)
                .load(R.drawable.search_loader)
                .placeholder(R.drawable.search_loader)
                .centerCrop()
                .into(ImageViewgif);

        dialog_loading.show();
    }

    public void dismissloading() {
        dialog_loading.dismiss();
    }

    private int getContactsCount() {

        cursor = dbHelper.numberOfRows();

        Log.e("c", "" + cursor);
        return cursor;

    }

    private void addonitem(View view, int position, ViewHolder holder) {
        loadingshow();
        holder.menu_item_add.setEnabled(true);

        Map<String, String> params = new HashMap<String, String>();

        if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {
            params.put("id", items[position].getId());
            params.put("ordermode", sharedpreferences.getString("ordermodetype", null));
            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
            params.put("dates", "");
            params.put("time", "");
        } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {
            params.put("id", items[position].getId());
            params.put("ordermode", sharedpreferences.getString("ordermodetype", null));
            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
            params.put("dates", "");
            params.put("time", sharedpreferences.getString("ordertodattime", null));
            params.put("date_string",sharedpreferences.getString("todaytimestring", null));

        } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
            params.put("id", items[position].getId());
            params.put("ordermode", sharedpreferences.getString("ordermodetype", null));
            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
            params.put("dates", sharedpreferences.getString("orderlaterdate", null));
            params.put("time", sharedpreferences.getString("orderlatertime", null));
            params.put("date_string",sharedpreferences.getString("latertimestring", null));
        }

        fullUrl = menuurlpath + "/menu" + "/itemadd";

        Log.d("item_add_params-URL","Full URL "+ fullUrl+ "Params"  + params);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<menu_addon_status_model> call = apiService.menuaddon(fullUrl, params);

        Log.e("params1", ": " + items[position].getId());
        Log.e("params2", ": " + sharedpreferences.getString("orderactivetag", null));
        Log.e("params3", ": " + fullUrl);
        Log.e("params4", ": " + params);

             /*  Log.e("params","Time:" + sharedpreferences.getString("ordertodattime", null) +
                        "date_string :" + sharedpreferences.getString("todaytimestring", null) );*/


        call.enqueue(new Callback<menu_addon_status_model>() {
            @SuppressLint("LongLogTag")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<menu_addon_status_model> call, Response<menu_addon_status_model> response) {
                //response.headers().get("Set-Cookie");

                int statusCode = response.code();

                Log.d("response", String.valueOf(response));

                if (statusCode == 200) {

                    Log.e("statusfor1", ": " + response.body().getStatus());
                    Log.e("statusfor2", ": " + response.body().getError_code());

                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        if (response.body().getError_code().equalsIgnoreCase("0")) {
                            hideloading();


                            int  userList = dbHelper.GetUserByUserId(parseInt(items[position].getId()));

                            if(userList == 0){


                                if (dbHelper.insertItem(items[position].getName(), items[position].getId(), "", "",
                                        "", items[position].getPrice(), "1", items[position].getPrice(),
                                        items[position].getPrice(), listdatum.getName(), sub.getName())) {

                                    Customertoastmessage(view);

                                    Log.d("item_add_time5- MenuitemnameAdapter",items[position].getName()+ "\n" + items[position].getId()+ "\n" +
                                            items[position].getPrice()+""+listdatum.getName()+"\n"+sub.getName()
                                    );


                                    Intent intent = new Intent("item_successfully_custom-message");
                               //     intent.putExtra("Qty_count",dbHelper.getqtycount());
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);



                                } else {
                                    Toast.makeText(mContext, "Could not Insert Item", Toast.LENGTH_SHORT).show();
                                }

                            }else{

                                count = Integer.parseInt(String.valueOf(holder.qty_textview_number.getText()));
                                count++;
                                length = String.valueOf(count).length();

                                if (length == 1) {
                                    updateqtyandprice(view,items[position].getId(),items[position].getPrice());
                                    holder.qty_textview_number.setText("0" + count);
                                }else{
                                    updateqtyandprice(view,items[position].getId(),items[position].getPrice());
                                    holder.qty_textview_number.setText("" + count);
                                }


                            }


                        }

                    } else {


                        if (response.body().getError_code() == null) {
                            Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            holder.showPopup(view, "Takeaway Closed","", "");
                            hideloading();
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

                                hideloading();

                                int check_user_data = dbHelper.GetUserByUserId(parseInt(items[position].getId()));



                                if(check_user_data == 0){

                                    String ItemName = items[position].getId();
                                    Intent intent = new Intent("custom-message");
                                    intent.putExtra("item", ItemName);
                                    intent.putExtra("addonid", response.body().getAddonId());
                                    intent.putExtra("categoryname", listdatum.getName());
                                    intent.putExtra("subcategoryname", sub.getName());
                                    intent.putExtra("item_price_amt", items[position].getPrice());
                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                }else {



                                    ArrayList<HashMap<String, String>> item_addon = dbHelper.GetUserdetails(parseInt(items[position].getId()));

                                    for (int i=0;i<item_addon.size();i++)
                                    {
                                        HashMap<String, String> hashmap= item_addon.get(i);
                                       // updateqty = hashmap.get("qty");
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

                                            count = Integer.parseInt(String.valueOf(holder.qty_textview_number.getText()));
                                            count++;
                                            length = String.valueOf(count).length();



                                            if (length == 1) {

                                                String ItemName = items[position].getId();
                                                Intent intent = new Intent("custom-message");
                                                intent.putExtra("item", ItemName);
                                                intent.putExtra("addonid", response.body().getAddonId());
                                                intent.putExtra("categoryname", listdatum.getName());
                                                intent.putExtra("subcategoryname", sub.getName());
                                                intent.putExtra("item_price_amt", items[position].getPrice());
                                                intent.putExtra("item_description", items[position].getDescription());
                                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                                repeatpopup.dismiss();
                                                holder.qty_textview_number.setText("0" + count);

                                            } else {
                                                String ItemName = items[position].getId();
                                                Intent intent = new Intent("custom-message");
                                                intent.putExtra("item", ItemName);
                                                intent.putExtra("addonid", response.body().getAddonId());
                                                intent.putExtra("categoryname", listdatum.getName());
                                                intent.putExtra("subcategoryname", sub.getName());
                                                intent.putExtra("item_price_amt", items[position].getPrice());
                                                intent.putExtra("item_description", items[position].getDescription());
                                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                                repeatpopup.dismiss();
                                                holder.qty_textview_number.setText("" + count);
                                            }
                                        }


                                    });

                                    add_more_button_textview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
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
                                Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                hideloading();
                            }
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
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
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

        //String price = item_price;

        String price  = updatefinalamt;

        float total_amt = Float.parseFloat(price) * qty;
        Boolean updatevalue  =  dbHelper.Updateqtyprice(parseInt(user_id),qty,total_amt);

        Intent intent = new Intent("item_successfully_custom-message");
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
        toast.setDuration(LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }


    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menu_item_name, menu_item_desc, menu_item_amout;
        public ImageView menu_item_image,bestseller_musttry_drawable;
        CardView ordermode_popup_view,layout_logo;
        LinearLayout menu_item_add,increment_decrement_layout;
        TextView textview_avaliable_time,qty_increase_textview,qty_decrease_textview,qty_textview_number;
        TextView bestseller_musttry_textview;
        LinearLayout seller_header;
        RelativeLayout menu_item_list;

        public ViewHolder(View itemView) {
            super(itemView);
            this.menu_item_name = itemView.findViewById(R.id.menu_item_name);
            this.menu_item_desc = itemView.findViewById(R.id.menu_item_desc);
            this.menu_item_amout = itemView.findViewById(R.id.menu_item_amout);
            this.menu_item_add = itemView.findViewById(R.id.menu_item_add);
            this.menu_item_image = itemView.findViewById(R.id.menu_item_image);
            this.ordermode_popup_view = itemView.findViewById(R.id.ordermode_popup_view);
            this.layout_logo = itemView.findViewById(R.id.layout_logo);
            this.textview_avaliable_time = itemView.findViewById(R.id.textview_avaliable_time);
            this.qty_increase_textview = itemView.findViewById(R.id.qty_increase_textview);
            this.qty_decrease_textview = itemView.findViewById(R.id.qty_decrease_textview);
            this.qty_textview_number = itemView.findViewById(R.id.qty_textview_number);
            this.increment_decrement_layout = itemView.findViewById(R.id.increment_decrement_layout);
            this.bestseller_musttry_textview = itemView.findViewById(R.id.bestseller_musttry_textview);
            this.bestseller_musttry_drawable = itemView.findViewById(R.id.bestseller_musttry_drawable);
            this.seller_header = itemView.findViewById(R.id.seller_header);
            this.menu_item_list = itemView.findViewById(R.id.menu_item_list);


        }

        public void showPopup(View view, String title, String msg, String codes) {
            View popupView = LayoutInflater.from(mContext).inflate(R.layout.addon_popup, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //  Button takaway_btn_dismiss = popupView.findViewById(R.id.takaway_btn_dismiss);


            //300 Takeaway Closed
            //101 Delivery Closed
            //200 Delivery Closed collection only
            //201 Delivery Closed collection only
  /*            Intent intent = new Intent(mContext, Item_Menu_Activity.class);
                intent.putExtra("menuurlpath", listdata[position].getMenuurlpath());
                mContext.startActivity(intent);*/

            AppCompatButton browse = popupView.findViewById(R.id.browse);
            AppCompatButton update = popupView.findViewById(R.id.update);

            TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);
            TextView takaway_status = popupView.findViewById(R.id.takaway_status);

            //  ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);


            takaway_status.setText(title);
            takaway_status_dec.setText(Html.fromHtml(msg).toString());

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

            }else{

                update.setText("Cancel");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
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


                    Intent intent = new Intent("clasback");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


                    /*Intent intent = new Intent(mContext, Dashboard_Activity.class);
                    mContext.startActivity(intent);*/


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

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(mContext)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);
        dialog.show();
    }

    public void hideloading() {
        dialog.dismiss();
    }

}