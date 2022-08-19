package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.fusionkitchen.R;

import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.model.wallet.wallet_transaction_model;
import com.squareup.picasso.Picasso;

import java.util.List;


public class wallethistoryadapter extends RecyclerView.Adapter<wallethistoryadapter.ViewHolder> {
    private wallet_transaction_model.tranferdetails[] wallethistorylistdata;
    String walletlent;
    private  Context mcontext;

    // RecyclerView recyclerView;
    public wallethistoryadapter(Context mcontext,List<wallet_transaction_model.tranferdetails> wallethistorylistdata, String walletlent) {


        this.wallethistorylistdata = wallethistorylistdata.toArray(new wallet_transaction_model.tranferdetails[0]);
        this.walletlent = walletlent;
        this.mcontext = mcontext;
    }

    @Override
    public wallethistoryadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_view_wallet_history, parent, false);
        wallethistoryadapter.ViewHolder viewHolder = new wallethistoryadapter.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(wallethistoryadapter.ViewHolder holder, int position) {

       Log.e("imged", "" + wallethistorylistdata[position].getTransfer_image());

        if (wallethistorylistdata[position].getTransfer_symbol().equalsIgnoreCase("+")) {
            holder.img_log.setBackgroundResource(R.color.car_total);
        } else {
            holder.img_log.setBackgroundResource(R.color.mines);
        }


        Picasso.get()
                .load(wallethistorylistdata[position].getTransfer_image())
                .placeholder(R.drawable.headerplaceholder)
                .error(R.drawable.headerplaceholder)
                .into(holder.list_client_logo);


        holder.list_client_name.setText(wallethistorylistdata[position].getTransfer_word());
        holder.history_date.setText(wallethistorylistdata[position].getTransfer_date());
        holder.list_total_amt.setText(wallethistorylistdata[position].getTransfer_symbol() + " £ " + wallethistorylistdata[position].getTransfer_amount());

    }


    @Override
    public int getItemCount() {
        if (walletlent.equalsIgnoreCase("1")) {
            int limit =7;
            return Math.min(wallethistorylistdata.length, limit);
        } else {
            return wallethistorylistdata.length;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView list_client_name, history_date, list_total_amt;
        public ImageView list_client_logo;
        public RelativeLayout img_log;


        public ViewHolder(View itemView) {
            super(itemView);
            //this.report_details_address = (TextView) itemView.findViewById(R.id.report_details_address);
            this.list_client_logo = itemView.findViewById(R.id.list_client_logo);
            this.list_client_name = itemView.findViewById(R.id.list_client_name);
            this.history_date = itemView.findViewById(R.id.history_date);
            this.list_total_amt = itemView.findViewById(R.id.list_total_amt);
            this.img_log = itemView.findViewById(R.id.img_log);
        }
    }


}
