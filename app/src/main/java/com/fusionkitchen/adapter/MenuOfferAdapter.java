package com.fusionkitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.activity.Payment_method_Activity;
import com.fusionkitchen.activity.Postcode_Activity;
import com.fusionkitchen.model.cart.coupon_valid_model;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.R;
import com.fusionkitchen.model.menu_model.menu_offer_model;
import com.fusionkitchen.model.offer.offer_list_model_details;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuOfferAdapter extends RecyclerView.Adapter<MenuOfferAdapter.ViewHolder> {
    private offer_list_model_details.discountcode[] listdata;
    private offer_list_model_details.promocode[] promocode;
    private  offer_list_model_details.commoncoupon[] commoncoupon;
    private Context mContext;
    String onlinepaytypess,onlinety;
    Dialog offer_popup,Coupen_popup;
    String cooking_insttructionback;
    private SQLDBHelper dbHelper;
    int cursor;
    int Client_id;
    String menuurlpath;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";



    // public int[] mColors = {R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two};//int or string

    // RecyclerView recyclerView;
    public MenuOfferAdapter(Context mContext, List<offer_list_model_details.discountcode> listdata, List<offer_list_model_details.promocode> promocode,
                            List<offer_list_model_details.commoncoupon> commoncoupon,String cooking_insttructionback,int client_id,String menuurlpath) {
        this.listdata = listdata.toArray(new offer_list_model_details.discountcode[0]);
        this.promocode = promocode.toArray(new offer_list_model_details.promocode[0]);
        this.commoncoupon = commoncoupon.toArray(new offer_list_model_details.commoncoupon[0]);
        this.mContext = mContext;
        this.cooking_insttructionback = cooking_insttructionback;
        this.Client_id = client_id;
        this.menuurlpath = menuurlpath;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_offer_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {




        dbHelper = new SQLDBHelper(mContext);
        getContactsCount();

        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);




       // holder.relativeLayout.setBackgroundResource((mColors[position % 20]));

        if (listdata[position].getType().equalsIgnoreCase("0")) {
           /* holder.offer_title.setText("GET " + listdata[position].getDiscount() + " % OFF");
            holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());*/
            holder.offer_title.setText(listdata[position].getDiscount() + " % OFF");
           // holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());

            holder.offer_decs.setText("Use Code " + listdata[position].getFree());
        } else {
            /*holder.offer_title.setText("GET £ " + listdata[position].getDiscount() + " OFF");
            holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());*/
            holder.offer_title.setText(listdata[position].getDiscount() + " OFF");
          //  holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());

            holder.offer_decs.setText("Use Code " + listdata[position].getFree());
        }


      /*  holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent("custom-message-onlineoffer");
                intent.putExtra("onlinecode", listdata[position].getFree());
                intent.putExtra("symbolstype", listdata[position].getType());
                intent.putExtra("paymenttype", listdata[position].getPayment_details());
                intent.putExtra("mintype", listdata[position].getMin_order());
                intent.putExtra("ordertyp", listdata[position].getOrder_type());
                intent.putExtra("datetype", listdata[position].getValid_date());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


            }
        });*/


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                offer_popup  = new Dialog(view.getContext());
                offer_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                offer_popup.setContentView(R.layout.offer_popup_design);

                TextView TextView_Offer  = offer_popup.findViewById(R.id.TextView_Offer);
                TextView details_offers_show = offer_popup.findViewById(R.id.details_offers_show);
                // AppCompatButton confirm_code = offer_popup.findViewById(R.id.confirm_code);
                TextView view_offer_details  = offer_popup.findViewById(R.id.view_offer_details);
                LinearLayout description = offer_popup.findViewById(R.id.description);
                TextView offer_code_textview = offer_popup.findViewById(R.id.offer_code_textview);

                AppCompatButton offer_promo_btn = offer_popup.findViewById(R.id.offer_promo_btn);
                offer_promo_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        offer_popup.dismiss();


                        ArrayList<String> get_amt_count = dbHelper.gettotalamt();

                        Log.d("get_amt_count---->"," " + get_amt_count);

                        if (cursor != 0) {
                            //couponcodevalidate(menuurlpath,Client_id,sharedpreferences.getString("ordermodetype", null),"1",listdata[position].getFree(), subtotal);
                        }else{
                            Toast.makeText(mContext,"Please Add Value",Toast.LENGTH_SHORT).show();
                        }





                        Coupen_popup = new Dialog(mContext);
                        Coupen_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Coupen_popup.setContentView(R.layout.coupon_popup_design);
                        ImageView imageView = Coupen_popup.findViewById(R.id.imageview);
                        TextView   add_more_button_textview = Coupen_popup.findViewById(R.id.add_more_button_textview);
                        Glide.with(mContext).load(R.drawable.offer_gif).into(imageView);

                        add_more_button_textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Coupen_popup.dismiss();
                            }
                        });



                        Coupen_popup.show();
                        Coupen_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        Coupen_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Coupen_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        Coupen_popup.getWindow().setGravity(Gravity.BOTTOM);


                    }
                });

                TextView_Offer.setText(listdata[position].getDiscount()+"% OFF");


                offer_code_textview.setText(listdata[position].getFree());


                view_offer_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (description.getVisibility() == View.GONE) {
                            description.setVisibility(View.VISIBLE);
                            view_offer_details.setText("Hide Details");
                        } else {
                            description.setVisibility(View.GONE);
                            view_offer_details.setText("View Details");
                        }
                    }
                });

                if (listdata[position].getPayment_details().equalsIgnoreCase("0")) {
                    onlinepaytypess = "Applicable only cash payments.";
                } else if (listdata[position].getPayment_details().equalsIgnoreCase("1")) {
                    onlinepaytypess = "Applicable only card payments.";
                } else {
                    onlinepaytypess = "Applicable both cash and card payments.";
                }

                if (listdata[position].getOrder_type().equalsIgnoreCase("0")) {
                    onlinety = "This coupon code is applicable only for delivery orders.";
                } else if (listdata[position].getOrder_type().equalsIgnoreCase("1")) {
                    onlinety = "This coupon code is applicable only for collection orders.";
                } else {
                    onlinety = "This coupon code is applicable for both delivery & collection orders.";
                }
                //  menu_offer_details_view.setVisibility(View.VISIBLE);

                //     confirm_code.setText(listdata[position].getFree());

                details_offers_show.setText("Coupon code applicable only on orders above £" + listdata[position].getMin_order() + "\n"
                        + "Offer is valid only for this particular takeaway/restaurant." + "\n"
                        + "No maximum limit to apply the coupon code." + "\n"
                        + "Apply the coupon code at the time of checkout." + "\n"
                        + "Offer valid only till " + listdata[position].getValid_date() + "\n"
                        + onlinety + "\n"
                        + onlinepaytypess + "\n"
                        + "Others T & C's may also apply.");

            /*    confirm_copy_button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                menu_offer_details_view.setVisibility(View.GONE);
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", onlinecode);
                                clipboard.setPrimaryClip(clip);
                            }
                        });*/

                //Glide.with(this).load(R.drawable.delivery_in_cycle).into(favourite_image);
                offer_popup.show();
                offer_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                offer_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                offer_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                offer_popup.getWindow().setGravity(Gravity.BOTTOM);


            }
        });



    }

    private void couponcodevalidate() {

/*
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", usercid);
        params.put("ordermode", ordermode);
        params.put("paymenttype", paymenttype);
        params.put("code", code);
        params.put("subtotal", subtotal);


        Log.d("coupencode","User->"+usercid + "ordermode->"+  ordermode +"paymenttype->"+ paymenttype + "code->"+code +"subtotsl->"+subtotal);


        fullUrl = menuurlpath + "/menu/couponAPI";

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<coupon_valid_model> call = apiService.getcouponvalid(fullUrl, params);

        Log.e("paramsval", "" + params);
        call.enqueue(new Callback<coupon_valid_model>() {
            @Override
            public void onResponse(Call<coupon_valid_model> call, Response<coupon_valid_model> response) {
                int statusCode = response.code();

                if (statusCode == 200) {


                    if (response.body().getStatus().equalsIgnoreCase("true")) {

*/
/*
                        card_coupon_code.setText(cardoffertitle);
                        card_offer_item_view.setVisibility(View.GONE);

                        txt_apply_coupon.setVisibility(View.GONE);
                        txt_remove_coupon.setVisibility(View.VISIBLE);

                        select_coupon_layout.setClickable(false);
                        amt_discount_rel1.setVisibility(View.VISIBLE);


                        card_total.setText(response.body().getTotal());
                        coupon_amt_dis.setText(response.body().getDiscount());


                        serviceDelCharge(menuurlpath, order_type_number, Sub_total.getText().toString(), payment_type_number, customerpostcode);

                        Log.d("servercharge",""+menuurlpath);
                        Log.d("servercharge",""+order_type_number);
                        Log.d("servercharge",""+Sub_total.getText().toString());
                        Log.d("servercharge",""+payment_type_number);
                        Log.d("servercharge",""+customerpostcode);


                        coupon_Discription = response.body().getDiscription();
                        coupon_type = response.body().getType();
                        coupon_discount = response.body().getDiscount();

                        Log.e("servicetotal", "" + coupon_discount);

                        Log.e("card_total", "" + card_total.getText().toString());


                        card_offer_popup_view.setVisibility(View.VISIBLE);
                        custom_loading_imageView = findViewById(R.id.custom_loading_imageView);
                        couponcode_popup = findViewById(R.id.couponcode_popup);
                        amountsave_popup = findViewById(R.id.amountsave_popup);


                        couponcode_popup.setText("' " + card_coupon_code.getText().toString() + " applied" + " '");
                        amountsave_popup.setText("You saved £ " + response.body().getDiscount());

                        custom_loading_imageView.setVisibility(View.VISIBLE);*//*



                      */
/*  new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                custom_loading_imageView.setVisibility(View.GONE);
                                new CountDownTimer(1000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        card_offer_popup_view.setVisibility(View.GONE);
                                    }
                                }.start();
                            }
                        }.start();
*//*


                    } else {
                        card_offer_item_view.setVisibility(View.GONE);
                        final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
                        final Snackbar snackbar = Snackbar.make(coordinatorLayoutView, "", 5000);
                        View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
                        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
                        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                        snackbarLayout.setPadding(0, 0, 0, 0);
                        TextView textView1 = customSnackView.findViewById(R.id.textView1);
                        textView1.setText(response.body().getMsg().replaceAll("\\<.*?\\>", ""));
                        snackbarLayout.addView(customSnackView, 0);
                        snackbar.show();
                    }
                } else {
                    // loader.dismiss();
                    Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<coupon_valid_model> call, Throwable t) {

                Log.e("Tro", "" + t);
                // loader.dismiss();
                Snackbar.make(Payment_method_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
            }

        });
*/

    }





    private int getContactsCount() {

        cursor = dbHelper.numberOfRows();
        Log.e("c", "" + cursor);
        return cursor;

    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView offer_title;
        public TextView offer_decs;
        public LinearLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.offer_title = (TextView) itemView.findViewById(R.id.offer_title);
            this.offer_decs = (TextView) itemView.findViewById(R.id.offer_decs);
            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}