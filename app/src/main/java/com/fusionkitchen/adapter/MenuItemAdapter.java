package com.fusionkitchen.adapter;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.os.Handler;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;

import static android.text.Html.fromHtml;
import static android.view.View.GONE;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private menu_item_sub_model.categoryall[] listdata;
    private static Context mContext;
    String cat_dayOfTheWeek,cat_restaurants_working_time;
    ProgressDialog loader;

    List<String> itemcatname = new ArrayList<String>();
    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    private String  menuurlpath;
    Dialog dialog;
    RecyclerView recyclerviewitem;
    Handler handler = new Handler();


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

        recyclerviewitem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int total = ((LinearLayoutManager)recyclerView.getLayoutManager()).getItemCount();
                int firstVisibleItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleItemCount = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                  if(firstVisibleItemCount == position){
                      Intent intent = new Intent("menu_data_update_category");
                      intent.putExtra("menu_list_name",listdata[position].getName());
                      intent.putExtra("menu_list_position",position);
                      LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                  }

            }

            @Override
            public void onScrollStateChanged(RecyclerView r, int newState) {
                super.onScrollStateChanged(r, newState);

                int firstVisibleItemCount = ((LinearLayoutManager)r.getLayoutManager()).findFirstVisibleItemPosition();

            }

        });

        /*----------------------------------start category time ----------------------------------*/

        TimeZone tz = TimeZone.getTimeZone("Europe/London");
        Calendar c = Calendar.getInstance(tz);
   /*     String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , c.get(Calendar.MINUTE))+":"+
                String.format("%02d" , c.get(Calendar.SECOND)) + " : " +
                String.format("%02d" , c.get(Calendar.DAY_OF_MONTH));*/

        int days  = c.get(Calendar.DAY_OF_WEEK);

        switch (days) {
            case Calendar.SUNDAY:
                cat_dayOfTheWeek = "Sunday";
                cat_restaurants_working_time = listdata[position].getWorkingtime7();
                break;
            case Calendar.MONDAY:
                cat_dayOfTheWeek = "Monday";
                cat_restaurants_working_time = listdata[position].getWorkingtime1();
                break;
            case Calendar.TUESDAY:
                cat_dayOfTheWeek = "Tuesday";
                cat_restaurants_working_time = listdata[position].getWorkingtime2();
                break;
            case Calendar.WEDNESDAY:
                cat_dayOfTheWeek = "Wednesday";
                cat_restaurants_working_time = listdata[position].getWorkingtime3();
                break;
            case Calendar.THURSDAY:
                cat_dayOfTheWeek = "Thursday";
                cat_restaurants_working_time = listdata[position].getWorkingtime4();
                break;
            case Calendar.FRIDAY:
                cat_dayOfTheWeek = "Friday";
                cat_restaurants_working_time = listdata[position].getWorkingtime5();
                break;
            case Calendar.SATURDAY:
                cat_dayOfTheWeek = "Saturday";
                cat_restaurants_working_time = listdata[position].getWorkingtime6();
                break;
        }

        /*----------------------------------End category time ----------------------------------*/

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


            }
        });

        List<menu_item_sub_model.categoryall.subcat> itemsubcatname = new ArrayList<>();

        for (int j = 0; j < listdata[position].getSubcat().size(); j++) {

            itemsubcatname.add(listdata[position].getSubcat().get(j));

        }



        MenuSubcatnameAdapter menuitemnameadapter = new MenuSubcatnameAdapter(mContext, itemsubcatname, listdata[position].getSubcat(),  menuurlpath, listdata[position],cat_restaurants_working_time);

        holder.child_recyclerview.setHasFixedSize(true);
        holder.child_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        holder.child_recyclerview.setAdapter(menuitemnameadapter);


        itemcatname.add(listdata[position].getName());


        holder.menu_item_cat_name.setText(itemcatname.get(position));
        holder.child_recyclerview.setVisibility(View.GONE);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String itempossion = intent.getStringExtra("itempossion");

                for(int y = 0; y < listdata.length; y++){
                    if(position==Integer.parseInt(itempossion)){

                        holder.child_recyclerview.setVisibility(View.VISIBLE);
                        holder.dropdownindicator.setRotation((float) 180.0);

                    }else{
                        holder.child_recyclerview.setVisibility(View.GONE);
                        holder.dropdownindicator.setRotation((float) 360.0);
                    }
                }


            }
        }, new IntentFilter("click_menu_id"));


    }

    private void loadingshow() {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);
        LottieAnimationView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        gifImageView.setAnimation(R.raw.newloader);
        gifImageView.playAnimation();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void hideloading() {
        dialog.dismiss();
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
