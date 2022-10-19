package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;

public class MenuserachcatAdapter extends RecyclerView.Adapter<MenuserachcatAdapter.ViewHolder> {
    private menu_item_sub_model.searchcategory[] listdata;
    private Context mContext;


    // RecyclerView recyclerView;
    public MenuserachcatAdapter(Context mContext, List<menu_item_sub_model.searchcategory> listdata) {

        this.mContext = mContext;
        this.listdata = listdata.toArray(new menu_item_sub_model.searchcategory[0]);

    }

    @Override
    public MenuserachcatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_search_cat_details, parent, false);
        MenuserachcatAdapter.ViewHolder viewHolder = new MenuserachcatAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MenuserachcatAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.menu_item_name.setText(listdata[position].getName());


        holder.menu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("item_possion-message");
                intent.putExtra("itempossion", String.valueOf(position));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

              //  Toast.makeText(mContext,String.valueOf(position),Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView menu_item_name;
        public LinearLayout menu_layout;


        public ViewHolder(View itemView) {
            super(itemView);

            //this.report_details_address = (TextView) itemView.findViewById(R.id.report_details_address);


            this.menu_item_name = itemView.findViewById(R.id.menu_item_name);
            this.menu_layout = itemView.findViewById(R.id.menu_layout);


        }
    }
}