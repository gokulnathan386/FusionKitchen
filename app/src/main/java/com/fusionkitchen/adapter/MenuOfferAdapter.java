package com.fusionkitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.model.menu_model.menu_offer_model;
import com.fusionkitchen.model.offer.offer_list_model_details;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;

public class MenuOfferAdapter extends RecyclerView.Adapter<MenuOfferAdapter.ViewHolder> {
    private offer_list_model_details.discountcode[] listdata;
    private offer_list_model_details.promocode[] promocode;
    private Context mContext;
    String onlinepaytypess,onlinety;
    Dialog offer_popup,Coupen_popup;

   // public int[] mColors = {R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two};//int or string

    // RecyclerView recyclerView;
    public MenuOfferAdapter(Context mContext, List<offer_list_model_details.discountcode> listdata, List<offer_list_model_details.promocode> promocode) {
        this.listdata = listdata.toArray(new offer_list_model_details.discountcode[0]);
        this.promocode = promocode.toArray(new offer_list_model_details.promocode[0]);
        this.mContext = mContext;

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

        final offer_list_model_details.discountcode myListData = listdata[position];

       // holder.relativeLayout.setBackgroundResource((mColors[position % 20]));

        if (listdata[position].getType().equalsIgnoreCase("0")) {
           /* holder.offer_title.setText("GET " + listdata[position].getDiscount() + " % OFF");
            holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());*/
            holder.offer_title.setText(listdata[position].getDiscount() + " % OFF");
            holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());
        } else {
            /*holder.offer_title.setText("GET £ " + listdata[position].getDiscount() + " OFF");
            holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());*/
            holder.offer_title.setText(listdata[position].getDiscount() + " OFF");
            holder.offer_decs.setText("On Orders above £ " + listdata[position].getMin_order());
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

                AppCompatButton offer_promo_btn = offer_popup.findViewById(R.id.offer_promo_btn);
                offer_promo_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        offer_popup.dismiss();

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