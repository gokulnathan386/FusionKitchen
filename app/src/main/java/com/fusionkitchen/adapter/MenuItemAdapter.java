package com.fusionkitchen.adapter;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;

import static android.text.Html.fromHtml;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private menu_item_sub_model.categoryall[] listdata;
    private static Context mContext;

    List<String> itemcatname = new ArrayList<String>();
    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    private String  menuurlpath;
    Dialog dialog;
    RecyclerView recyclerviewitem;

    // HashSet<String> hashSet1 = new HashSet<String>();


    // RecyclerView recyclerView;
    public MenuItemAdapter(Context mContext, List<menu_item_sub_model.categoryall> listdata,  String menuurlpath,RecyclerView recyclerviewitem) {

        this.mContext = mContext;
        this.listdata = listdata.toArray(new menu_item_sub_model.categoryall[0]);
        this.menuurlpath = menuurlpath;
        this.recyclerviewitem = recyclerviewitem;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_catag_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {


          /* LocalBroadcastManager.getInstance(mContext).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String itempossion = intent.getStringExtra("itempossion");
                String item_postion_name = intent.getStringExtra("itempossionname");

                if(Integer.parseInt(itempossion) == position){

                    holder.child_recyclerview.setVisibility(View.VISIBLE);
                    holder.dropdownindicator.setRotation((float) 180.0);

                }

            }
        }, new IntentFilter("item_possion-message"));*/



        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.child_recyclerview.getVisibility() == View.GONE) {
                    holder.child_recyclerview.setVisibility(View.VISIBLE);
                    holder.dropdownindicator.setRotation((float) 180.0);
                } else {
                    holder.child_recyclerview.setVisibility(View.GONE);
                    holder.dropdownindicator.setRotation((float) 360.0);
                }

                Intent intent = new Intent("item_possion-message");
                intent.putExtra("itempossion", String.valueOf(position));
                intent.putExtra("itempossionname",listdata[position].getName());
                LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);

                Log.v("getpositiponofheader", String.valueOf(position));
            }
        });


        List<menu_item_sub_model.categoryall.subcat> itemsubcatname = new ArrayList<>();

        for (int j = 0; j < listdata[position].getSubcat().size(); j++) {

            itemsubcatname.add(listdata[position].getSubcat().get(j));

        }
        MenuSubcatnameAdapter menuitemnameadapter = new MenuSubcatnameAdapter(mContext, itemsubcatname, listdata[position].getSubcat(),  menuurlpath, listdata[position]);

        // MenuSubcatnameAdapter menuitemnameadapter = new MenuSubcatnameAdapter(mContext, itemsubcatname, ordertypevalue, menuurlpath, listdata[position]);
        holder.child_recyclerview.setHasFixedSize(true);
        holder.child_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        holder.child_recyclerview.setAdapter(menuitemnameadapter);


        itemcatname.add(listdata[position].getName());

       /* if (position == 0) {
            holder.menu_item_cat_name.setText(itemcatname.get(position));
            holder.child_recyclerview.setVisibility(View.VISIBLE);
            holder.dropdownindicator.setRotation((float) 180.0);

            Intent intent = new Intent("item_possion-message");
            intent.putExtra("itempossion", String.valueOf(position));
            intent.putExtra("itempossionname",listdata[position].getName());
            LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(intent);
            Log.d("menu_item_cat_name",""+listdata[position].getName() + " " +listdata.length);

        }else{
            holder.menu_item_cat_name.setText(itemcatname.get(position));
            holder.child_recyclerview.setVisibility(View.GONE); // Visible subtitle
        }*/

        holder.menu_item_cat_name.setText(itemcatname.get(position));
        holder.child_recyclerview.setVisibility(View.GONE);

    }
    @SuppressLint("LogNotTimber")
    @Override
    public int getItemCount() {
        return listdata.length;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView menu_item_cat_name;
        public ImageView dropdownindicator;
        public RelativeLayout relativeLayout;
        public RecyclerView child_recyclerview;

        public LinearLayout mainLL;


        public ViewHolder(View itemView) {
            super(itemView);
            this.menu_item_cat_name = itemView.findViewById(R.id.menu_item_cat_name);
            this.dropdownindicator = itemView.findViewById(R.id.dropdownindicator);
            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
            this.child_recyclerview = itemView.findViewById(R.id.child_recyclerview);
            this.mainLL = itemView.findViewById(R.id.categorymainLL);
        }
    }
}
