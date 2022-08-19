package com.fusionkitchen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.model.order_history.order_details_list_show;
import com.fusionkitchen.model.orderstatus.ordertracking_details_model;

import static java.lang.Double.valueOf;

public class OrderstatusitemListAdapter extends RecyclerView.Adapter<OrderstatusitemListAdapter.ViewHolder> {

    private Context mContext;


    private ordertracking_details_model.item[] orderhistory;

    // RecyclerView recyclerView;
    public OrderstatusitemListAdapter(Context mContext, List<ordertracking_details_model.item> orderhistory) {


        this.orderhistory = orderhistory.toArray(new ordertracking_details_model.item[0]);


        this.mContext = mContext;
    }

    @Override
    public OrderstatusitemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_orderdetails_sublist, parent, false);
        OrderstatusitemListAdapter.ViewHolder viewHolder = new OrderstatusitemListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderstatusitemListAdapter.ViewHolder holder, int position) {
        final ordertracking_details_model.item myorderhistory = orderhistory[position];

        List<ordertracking_details_model.item.addonlist> addondetailslist = orderhistory[position].getAddon();


        holder.item_name.setText(orderhistory[position].getItem_name());
        holder.item_qty.setText(orderhistory[position].getQty() + " X");
        Log.e("sixg", "" + addondetailslist.size());


        if (addondetailslist.size() == 0) {
            holder.add_on.setText(" ");
        } else if (addondetailslist.size() == 1) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name());
        } else if (addondetailslist.size() == 2) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name());
        } else if (addondetailslist.size() == 3) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name());
        } else if (addondetailslist.size() == 4) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name());
        } else if (addondetailslist.size() == 5) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name());
        } else if (addondetailslist.size() == 6) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name());
        } else if (addondetailslist.size() == 7) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name());
        } else if (addondetailslist.size() == 8) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name());
        } else if (addondetailslist.size() == 9) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name());
        } else if (addondetailslist.size() == 10) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name() + "," + addondetailslist.get(9).getAddon_name());
        } else if (addondetailslist.size() == 11) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name() + "," + addondetailslist.get(9).getAddon_name() + "," + addondetailslist.get(10).getAddon_name());
        } else if (addondetailslist.size() == 12) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name() + "," + addondetailslist.get(9).getAddon_name() + "," + addondetailslist.get(10).getAddon_name() + "," + addondetailslist.get(11).getAddon_name());
        } else if (addondetailslist.size() == 13) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name() + "," + addondetailslist.get(9).getAddon_name() + "," + addondetailslist.get(10).getAddon_name() + "," + addondetailslist.get(11).getAddon_name() + "," + addondetailslist.get(12).getAddon_name());
        } else if (addondetailslist.size() == 14) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name() + "," + addondetailslist.get(9).getAddon_name() + "," + addondetailslist.get(10).getAddon_name() + "," + addondetailslist.get(11).getAddon_name() + "," + addondetailslist.get(12).getAddon_name() + "," + addondetailslist.get(13).getAddon_name());
        } else if (addondetailslist.size() == 15) {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name() + "," + addondetailslist.get(9).getAddon_name() + "," + addondetailslist.get(10).getAddon_name() + "," + addondetailslist.get(11).getAddon_name() + "," + addondetailslist.get(12).getAddon_name() + "," + addondetailslist.get(13).getAddon_name() + "," + addondetailslist.get(14).getAddon_name());
        } else {
            holder.add_on.setText(addondetailslist.get(0).getAddon_name() + "," + addondetailslist.get(1).getAddon_name() + "," + addondetailslist.get(2).getAddon_name() + "," + addondetailslist.get(3).getAddon_name() + "," + addondetailslist.get(4).getAddon_name() + "," + addondetailslist.get(5).getAddon_name() + "," + addondetailslist.get(6).getAddon_name() + "," + addondetailslist.get(7).getAddon_name() + "," + addondetailslist.get(8).getAddon_name() + "," + addondetailslist.get(9).getAddon_name() + "," + addondetailslist.get(10).getAddon_name() + "," + addondetailslist.get(11).getAddon_name() + "," + addondetailslist.get(12).getAddon_name() + "," + addondetailslist.get(13).getAddon_name() + "," + addondetailslist.get(14).getAddon_name());
        }


        Log.e("addondetailslist", "amt: " + addondetailslist.size());
        if (addondetailslist.size() == 0) {
            holder.addon_amt.setText("£" + " " + orderhistory[position].getTotal());
        } else if (addondetailslist.size() == 1) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 2) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 3) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 4) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 5) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 6) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 7) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 8) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 9) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal()) + valueOf(addondetailslist.get(8).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 10) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal()) + valueOf(addondetailslist.get(8).getTotal()) + valueOf(addondetailslist.get(9).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 11) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal()) + valueOf(addondetailslist.get(8).getTotal()) + valueOf(addondetailslist.get(9).getTotal()) + valueOf(addondetailslist.get(10).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 12) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal()) + valueOf(addondetailslist.get(8).getTotal()) + valueOf(addondetailslist.get(9).getTotal()) + valueOf(addondetailslist.get(10).getTotal()) + valueOf(addondetailslist.get(11).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 13) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal()) + valueOf(addondetailslist.get(8).getTotal()) + valueOf(addondetailslist.get(9).getTotal()) + valueOf(addondetailslist.get(10).getTotal()) + valueOf(addondetailslist.get(11).getTotal()) + valueOf(addondetailslist.get(12).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 14) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal()) + valueOf(addondetailslist.get(8).getTotal()) + valueOf(addondetailslist.get(9).getTotal()) + valueOf(addondetailslist.get(10).getTotal()) + valueOf(addondetailslist.get(11).getTotal()) + valueOf(addondetailslist.get(12).getTotal()) + valueOf(addondetailslist.get(13).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else if (addondetailslist.size() == 15) {
            double res2 = valueOf(orderhistory[position].getTotal()) + valueOf(addondetailslist.get(0).getTotal()) + valueOf(addondetailslist.get(1).getTotal()) + valueOf(addondetailslist.get(2).getTotal()) + valueOf(addondetailslist.get(3).getTotal()) + valueOf(addondetailslist.get(4).getTotal()) + valueOf(addondetailslist.get(5).getTotal()) + valueOf(addondetailslist.get(6).getTotal()) + valueOf(addondetailslist.get(7).getTotal()) + valueOf(addondetailslist.get(8).getTotal()) + valueOf(addondetailslist.get(9).getTotal()) + valueOf(addondetailslist.get(10).getTotal()) + valueOf(addondetailslist.get(11).getTotal()) + valueOf(addondetailslist.get(12).getTotal()) + valueOf(addondetailslist.get(13).getTotal()) + valueOf(addondetailslist.get(14).getTotal());
            holder.addon_amt.setText("£" + " " + String.format("%.2f", res2));
        } else {
            holder.addon_amt.setText(orderhistory[position].getTotal());
        }


    }


    @Override
    public int getItemCount() {
        return orderhistory.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name, addon_amt, add_on, item_qty;


        public ViewHolder(View itemView) {
            super(itemView);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.addon_amt = itemView.findViewById(R.id.addon_amt);
            this.add_on = itemView.findViewById(R.id.add_on);
            this.item_qty = itemView.findViewById(R.id.item_qty);


        }
    }
}