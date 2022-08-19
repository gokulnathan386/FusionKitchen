package com.fusionkitchen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.model.offer.offer_list_model_details;

public class CardPromoAdapter extends RecyclerView.Adapter<CardPromoAdapter.ViewHolder> {

    private Context mContext;
    // private menu_offer_model[] listdata;

    private offer_list_model_details.promocode[] promocode;

    // RecyclerView recyclerView;
    public CardPromoAdapter(Context mContext, List<offer_list_model_details.promocode> promocode) {
        this.mContext = mContext;
        this.promocode = promocode.toArray(new offer_list_model_details.promocode[0]);
    }

    @Override
    public CardPromoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_card_offer_details, parent, false);
        CardPromoAdapter.ViewHolder viewHolder = new CardPromoAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardPromoAdapter.ViewHolder holder, int position) {


        if (promocode[position].getType().equalsIgnoreCase("0")) {
            holder.cart_offer_title.setText("GET " + promocode[position].getDiscount() + " % OFF");
            holder.cart_offer_decs.setText("");
        } else {
            holder.cart_offer_title.setText("GET £ " + promocode[position].getDiscount() + " OFF");
            holder.cart_offer_decs.setText("");
        }

        holder.cart_offer_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.cart_offer_copy.setClickable(false);
                holder.cart_offer_copy.setFocusable(false);
                holder.cart_offer_copy.setEnabled(false);


                Intent intent = new Intent("card_offer-message");
                intent.putExtra("cardoffertitle", promocode[position].getFree());
               /* intent.putExtra("cardoffertype", promocode[position].getType());
                intent.putExtra("cardofferdiscountamt", promocode[position].getDiscount());
                intent.putExtra("cardofferminorder", promocode[position].getMin_order());*/
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


            }
        });


        holder.details_offers_show.setText("\u2022  Promo Code applicable on all orders." + "\n"
                + "\u2022  Offer will be applicable on your first order only." + "\n"
                + "\u2022  Offer is valid only for this particular restaurant/takeaway." + "\n"
                + "\u2022  Apply the promo code at the checkout." + "\n"
                + "\u2022  Other T & C may also apply.");

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
        return promocode.length;
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