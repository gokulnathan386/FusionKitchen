package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fusionkitchen.R;
import com.fusionkitchen.model.menu_model.Menu_Page_listmodel;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;

import java.util.List;

import static android.view.View.GONE;


public class MenuListViewAdapter extends RecyclerView.Adapter<MenuListViewAdapter.MyViewHolder> {

    List<menu_item_sub_model.categoryall> menu_page_listmodels;
    Context context;
    int selectedposition = -1;
    Dialog menulistpopup;
    RecyclerView recyclerviewitem;



    public MenuListViewAdapter(List<menu_item_sub_model.categoryall> menu_page_listmodels, Context context, Dialog menulistpopup,RecyclerView recyclerviewitem){
        this.menu_page_listmodels = menu_page_listmodels;
        this.context=context;
        this.menulistpopup = menulistpopup;
        this.recyclerviewitem = recyclerviewitem;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_menu_listview_design, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.menu_item_name.setText(menu_page_listmodels.get(position).getName());



        Log.d("getcategory---->"," " + menu_page_listmodels.get(position).getName());

        /*if (position == 0) {
            Intent intent = new Intent("item_possion-message");
            intent.putExtra("itempossion", String.valueOf(position));
            intent.putExtra("itempossionname", menu_page_listmodels.get(position).getName());
            LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);

        } else {
            Intent intent = new Intent("item_possion-message");
            intent.putExtra("itempossion", String.valueOf(position));
            intent.putExtra("itempossionname", menu_page_listmodels.get(position).getName());
            LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
        }*/

        if(position == selectedposition){
            holder.menu_layout.setBackgroundColor(Color.parseColor("#EEF6FF"));
            holder.menu_item_name.setTextColor(Color.parseColor("#0071E3"));
            holder.Enter_your_Left_arrow.setVisibility(View.VISIBLE);
        }else{
            holder.menu_layout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.menu_item_name.setTextColor(Color.parseColor("#17284D"));
            holder.Enter_your_Left_arrow.setVisibility(View.GONE);
        }

        holder.menu_item_name.setTag(position);
        holder.menu_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("click_menu_id");
                intent.putExtra("itempossion", String.valueOf(position));
                intent.putExtra("itempossionname", menu_page_listmodels.get(position).getName());
                LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(intent);
                selectedposition = (Integer)v.getTag();
                notifyDataSetChanged();
                menulistpopup.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return menu_page_listmodels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView menu_item_name;
        LinearLayout menu_layout;
        ImageView Enter_your_Left_arrow;

        public MyViewHolder(View itemView) {
            super(itemView);

            menu_item_name = (TextView) itemView.findViewById(R.id.menu_item_name);
            menu_layout = (LinearLayout) itemView.findViewById(R.id.menu_layout);
            Enter_your_Left_arrow = (ImageView) itemView.findViewById(R.id.Enter_your_Left_arrow);


        }
    }
}
