package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.model.menu_model.menu_offer_model;
import com.fusionkitchen.model.offer.offer_list_model_details;

public class CardOfferAdapter extends RecyclerView.Adapter<CardOfferAdapter.ViewHolder> {

    private Context mContext;
    // private menu_offer_model[] listdata;
    private offer_list_model_details.discountcode[] listdata;
    String onlinepaytypess, onlinety;

    // RecyclerView recyclerView;
    public CardOfferAdapter(Context mContext, List<offer_list_model_details.discountcode> listdata) {
        this.mContext = mContext;
        this.listdata = listdata.toArray(new offer_list_model_details.discountcode[0]);
    }

    @Override
    public CardOfferAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_card_offer_details, parent, false);
        CardOfferAdapter.ViewHolder viewHolder = new CardOfferAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardOfferAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final offer_list_model_details.discountcode myListData = listdata[position];


        if (listdata[position].getType().equalsIgnoreCase("0")) {
            holder.cart_offer_title.setText("GET " + listdata[position].getDiscount() + " % OFF");
            holder.cart_offer_decs.setText("On Orders above £ " + listdata[position].getMin_order() + " | Use code " + listdata[position].getFree());
        } else {
            holder.cart_offer_title.setText("GET £ " + listdata[position].getDiscount() + " OFF");
            holder.cart_offer_decs.setText("On Orders above £ " + listdata[position].getMin_order() + " | Use code " + listdata[position].getFree());
        }

        holder.cart_offer_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cart_offer_copy.setClickable(false);
                holder.cart_offer_copy.setFocusable(false);
                holder.cart_offer_copy.setEnabled(false);
                Intent intent = new Intent("card_offer-message");
                intent.putExtra("cardoffertitle", listdata[position].getFree());
               /* intent.putExtra("cardoffertype", listdata[position].getType());
                intent.putExtra("cardofferdiscountamt", listdata[position].getDiscount());
                intent.putExtra("cardofferminorder", listdata[position].getMin_order());*/

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


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


        holder.details_offers_show.setText("\u2022  Coupon code applicable only on orders above £" + listdata[position].getMin_order() + "\n"
                + "\u2022  Offer is valid only for this particular takeaway/restaurant." + "\n"
                + "\u2022  No maximum limit to apply the coupon code." + "\n"
                + "\u2022  Apply the coupon code at the time of checkout." + "\n"
                + "\u2022  Offer valid only till " + listdata[position].getValid_date() + "\n"
                + "\u2022  " + onlinety + "\n"
                + "\u2022  " + onlinepaytypess + "\n"
                + "\u2022  Others T & C's may also apply.");

        holder.view_offer_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.view_offer_details.getText().toString().equalsIgnoreCase("View Details")) {
                    holder.offer_details_lay.setVisibility(View.VISIBLE);
                    holder.view_offer_details.setText("Hide Deatils");
                } else {
                    holder.view_offer_details.setText("View Details");
                    holder.offer_details_lay.setVisibility(View.GONE);

                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cart_offer_title;
        public TextView cart_offer_decs;
        public AppCompatButton cart_offer_copy;
        public TextView view_offer_details;
        public LinearLayout offer_details_lay;
        public TextView details_offers_show;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cart_offer_title = (TextView) itemView.findViewById(R.id.cart_offer_title);
            this.cart_offer_decs = (TextView) itemView.findViewById(R.id.cart_offer_decs);
            this.cart_offer_copy = (AppCompatButton) itemView.findViewById(R.id.cart_offer_copy);
            this.view_offer_details = (TextView) itemView.findViewById(R.id.view_offer_details);
            this.offer_details_lay = (LinearLayout) itemView.findViewById(R.id.offer_details_lay);
            this.details_offers_show = (TextView) itemView.findViewById(R.id.details_offers_show);
        }
    }
}