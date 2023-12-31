package com.fusionkitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Add_to_Cart;
import com.fusionkitchen.model.cart.coupon_valid_model;
import com.fusionkitchen.model.offer.offer_list_model_details;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MenupromoAdapter extends RecyclerView.Adapter<MenupromoAdapter.ViewHolder> {
    private offer_list_model_details.discountcode[] listdata;
    private offer_list_model_details.promocode[] promocode;
    private  offer_list_model_details.commoncoupon[] commoncoupon;

    private Context mContext;
    Dialog offer_popup,Coupen_popup;
    String cooking_insttructionback;
    private SQLDBHelper dbHelper;
    int cursor;
    int Client_id;
    String menuurlpath;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    String fullUrl;

   // public int[] mColors = {R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two};//int or string

    // RecyclerView recyclerView;
    public MenupromoAdapter(Context mContext, List<offer_list_model_details.discountcode> listdata, List<offer_list_model_details.promocode> promocode,
                            List<offer_list_model_details.commoncoupon> commoncoupon,String cooking_insttructionback,int client_id,String menuurlpath) {
        this.listdata = listdata.toArray(new offer_list_model_details.discountcode[0]);
        this.promocode = promocode.toArray(new offer_list_model_details.promocode[0]);
        this.commoncoupon = commoncoupon.toArray(new offer_list_model_details.commoncoupon[0]);
        this.mContext = mContext;
        this.cooking_insttructionback = cooking_insttructionback;
        this.Client_id =client_id;
        this.menuurlpath = menuurlpath;

    }

    @Override
    public MenupromoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_offer_details, parent, false);
        MenupromoAdapter.ViewHolder viewHolder = new MenupromoAdapter.ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MenupromoAdapter.ViewHolder holder, int position) {

        final offer_list_model_details.promocode myListData = promocode[position];

        Log.d("Offercodesize-->","" + promocode.length);

        dbHelper = new SQLDBHelper(mContext);
        getContactsCount();

        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);


        if (promocode[position].getType().equalsIgnoreCase("0")) {
            holder.offer_title.setText("GET " + promocode[position].getDiscount() + " % OFF");
            holder.offer_decs.setText("Use Code " +promocode[position].getFree());
        } else {
            holder.offer_title.setText("GET £ " + promocode[position].getDiscount() + " OFF");
            holder.offer_decs.setText("Use Code " +promocode[position].getFree());
        }


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                offer_popup  = new Dialog(view.getContext());
                offer_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                offer_popup.setContentView(R.layout.offer_popup_design);

                TextView details_offers_show = offer_popup.findViewById(R.id.details_offers_show);
                TextView TextView_Offer  = offer_popup.findViewById(R.id.TextView_Offer);
                LinearLayout description = offer_popup.findViewById(R.id.description);
                TextView offer_code_textview = offer_popup.findViewById(R.id.offer_code_textview);
                TextView view_offer_details  = offer_popup.findViewById(R.id.view_offer_details);
                AppCompatButton offer_promo_btn = offer_popup.findViewById(R.id.offer_promo_btn);
                TextView offer_apply_textview = offer_popup.findViewById(R.id.offer_apply_textview);
                EditText offerapplied_edittext = offer_popup.findViewById(R.id.offerapplied_edittext);


                offer_apply_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getContactsCount();

                        ArrayList<String> get_amt_count = dbHelper.gettotalamt();

                        if (cursor != 0) {
                            String sub_amount = get_amt_count.get(0);

                            couponcodevalidate(menuurlpath,Client_id,sharedpreferences.getString("ordermodetype", null),"1",offerapplied_edittext.getText().toString(),
                                    sub_amount,offer_popup,sharedpreferences.getString("asaptodaylaterstring", null));
                        }else{
                            Toast.makeText(mContext,"Please Add the one Item",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                offer_promo_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getContactsCount();

                        ArrayList<String> get_amt_count = dbHelper.gettotalamt();

                        if (cursor != 0) {
                            String sub_amount = get_amt_count.get(0);
                            couponcodevalidate(menuurlpath,Client_id,sharedpreferences.getString("ordermodetype", null),
                                    "1",promocode[position].getFree(),
                                   sub_amount,offer_popup,sharedpreferences.getString("asaptodaylaterstring", null));
                        }else{

                            Toast.makeText(mContext,"Please Add the one Item",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

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

                TextView_Offer.setText(promocode[position].getDiscount()+"% OFF");

                offer_code_textview.setText(promocode[position].getFree());

                details_offers_show.setText("Promo Code applicable on all orders." + "\n"
                        + "Offer will be applicable on your first order only." + "\n"
                        + "Offer is valid only for this particular restaurant/takeaway." + "\n"
                        + "Apply the promo code at the checkout." + "\n"
                        + "Other T & C may also apply.");


                offer_popup.show();
                offer_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                offer_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                offer_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                offer_popup.getWindow().setGravity(Gravity.BOTTOM);

            }
        });


    }

    private void couponcodevalidate(String menuurlpath, int client_id, String ordermodetype, String payment_mode,
                                     String use_code, String sub_amount, Dialog offer_popup, String dtatstring) {


        offer_popup.dismiss();


        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", String.valueOf(client_id));
        params.put("ordermode", ordermodetype);
        params.put("paymenttype", payment_mode);
        params.put("code", use_code);
        params.put("subtotal", sub_amount);
        params.put("order_time",dtatstring);


        Log.d("coupencode","User->"+client_id + "ordermode->"+  ordermodetype +"paymenttype->"+ payment_mode + "code->"+use_code +"subtotsl->"+sub_amount);

        fullUrl = menuurlpath + "/menu/couponAPI";

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<coupon_valid_model> call = apiService.getcouponvalid(fullUrl, params);

        Log.e("paramsval", "" + params);

        call.enqueue(new Callback<coupon_valid_model>() {
            @Override
            public void onResponse(Call<coupon_valid_model> call, Response<coupon_valid_model> response) {
                int statusCode = response.code();


                if (statusCode == 200) {

                    // Log.e("Success======", new Gson().toJson(response.body()));
                    String offer_msg =  response.body().getMsg();

                    if (response.body().getStatus().equalsIgnoreCase("true")) {


                        String discount =  response.body().getDiscount();
                        String code =  response.body().getCode();
                        String discription =  response.body().getDiscription();
                        String type  = response.body().getType();
                        String total =  response.body().getTotal();

                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Offer_applied",MODE_PRIVATE);
                        SharedPreferences.Editor offerEdit = sharedPreferences.edit();
                        offerEdit.putString("offer_total_amount", total);
                        offerEdit.putString("offer_discount", discount);
                        offerEdit.putString("offer_code", code);
                        offerEdit.putString("offer_applied","1");
                        offerEdit.commit();


                        Intent intent = new Intent("total_count_Update");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                        Coupen_popup = new Dialog(mContext);
                        Coupen_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Coupen_popup.setContentView(R.layout.coupon_popup_design);
                        ImageView imageView = Coupen_popup.findViewById(R.id.imageview);
                        TextView   add_more_button_textview = Coupen_popup.findViewById(R.id.add_more_button_textview);
                        Glide.with(mContext).load(R.drawable.offer_gif).into(imageView);
                        TextView offer_amount_mins  = Coupen_popup.findViewById(R.id.offer_amount_mins);
                        TextView applied_code = Coupen_popup.findViewById(R.id.applied_code);
                        TextView check_out_btn = Coupen_popup.findViewById(R.id.check_out_btn);

                        offer_amount_mins.setText("£"+ discount);
                        applied_code.setText(code + " Applied");

                        add_more_button_textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Coupen_popup.dismiss();
                            }
                        });

                        check_out_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (cursor != 0) {

                                    Intent intent = new Intent(mContext, Add_to_Cart.class);
                                    intent.putExtra("cooking_insttruction", cooking_insttructionback);
                                    mContext.startActivity(intent);
                                }



                            }
                        });

                        Coupen_popup.show();
                        Coupen_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        Coupen_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Coupen_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        Coupen_popup.getWindow().setGravity(Gravity.BOTTOM);

                    }else{

                        Toast.makeText(mContext, offer_msg, Toast.LENGTH_LONG).show();
                    }

                } else {

                    Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<coupon_valid_model> call, Throwable t) {

                Log.e("Tro", "" + t);

            }

        });



    }

    private int getContactsCount() {
        cursor = dbHelper.numberOfRows();
        Log.e("c", "" + cursor);
        return cursor;
    }


    @Override
    public int getItemCount() {
        Log.d("Menupromocod0e","" + promocode.length);
        return promocode.length;
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