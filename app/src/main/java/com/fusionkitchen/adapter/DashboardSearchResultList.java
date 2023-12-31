package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fusionkitchen.model.favorite.insertfavorite_mode;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.model.home_model.location_type_sub_modal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
//Firse client list show addapter

public class DashboardSearchResultList<sharedpreferences> extends RecyclerView.Adapter<DashboardSearchResultList.ViewHolder> {
    private location_type_sub_modal.clients[] listdata;
    private Context mContext;

    String offertypeone, offertypetwo;
    // public String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7"};

    public static final String Favourite_data = "favourite_store_data";
    private long mLastClickTime = 0;

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    private SQLDBHelper dbHelper;
    int cursor;

    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id;

    private Dialog dialog;


    // RecyclerView recyclerView;
    public DashboardSearchResultList(Context mContext, List<location_type_sub_modal.clients> listdata) {

        this.mContext = mContext;
        this.listdata = listdata.toArray(new location_type_sub_modal.clients[0]);

    }

    @Override
    public DashboardSearchResultList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.dashboard_restaurants_listview, parent, false);
        //View listItem = layoutInflater.inflate(R.layout.raw_dashboard_search_details, parent, false);
        DashboardSearchResultList.ViewHolder viewHolder = new DashboardSearchResultList.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        List<location_type_sub_modal.clients.onlinediscount> myofferData = listdata[position].getOnlinediscount();
        List<location_type_sub_modal.clients.client_cuisines> myclientcuisinesData = listdata[position].getClient_cuisines();
        List<location_type_sub_modal.clients.offer> offerData = listdata[position].getOffer();
        // holder.relative.setBackgroundColor(Color.parseColor(mColors[position % 4]));
        //banner img set

        /*--------------Login details get SharedPreferences------------------*/
        slogin = mContext.getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));

        Log.e("dasboard_user_id", "" + user_id);


//Add Fab
        if (user_id != null && !user_id.equals("")) {
          //  holder.fav_btn.setVisibility(View.VISIBLE);

            if (listdata[position].getFavroite().equalsIgnoreCase("0")) {
               // holder.fav_btn.setImageResource(R.drawable.heartnoselect);
            } else {
               // holder.fav_btn.setImageResource(R.drawable.heartselect);
            }


        } else {
           // holder.fav_btn.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(listdata[position].getBgimge())
                .placeholder(R.drawable.headerplaceholder)
                .error(R.drawable.headerplaceholder)
                .into(holder.shopimg);



       /* ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter_popup_category.xml = new ColorMatrixColorFilter(matrix);
        holder.shopimg.setColorFilter(filter_popup_category.xml);*/


//client logo set
      /*  Picasso.get()
                .load(listdata[position].getClientlogo())
                .placeholder(R.drawable.hederlocoplaceimg)
                .error(R.drawable.hederlocoplaceimg)
                .into(holder.client_logo);*/
//client name and rate set
        holder.clent_name.setText(listdata[position].getClientName());



         holder.clent_rating.setText(listdata[position].getRating());

        if(listdata[position].getRating().equalsIgnoreCase("0")){
            holder.View_your_star_rating.setVisibility(View.GONE);
        }


//client takeaway dliver 4 opp set


        if (listdata[position].getcollectiontime() != null && !listdata[position].getcollectiontime().isEmpty()) {
            holder.client_takeaway.setText(listdata[position].getcollectiontime());
        } else {
            holder.takeeay_layout.setVisibility(View.INVISIBLE);
        }


        if (listdata[position].getDeliverytime() != null && !listdata[position].getDeliverytime().isEmpty()) {

            holder.clent_delivery.setText(listdata[position].getDeliverytime());

        } else {
            holder.delivery_linearlayout.setVisibility(View.INVISIBLE);
        }


      /*if (listdata[position].getDeliverytime() != null && !listdata[position].getDeliverytime().isEmpty()) {
            holder.client_deliverytime.setText(listdata[position].getDeliverytime());
        } else {
            holder.delivery_time_layout.setVisibility(View.GONE);
        }*/

        if (listdata[position].getDistance() != null && !listdata[position].getDistance().isEmpty()) {
            holder.client_deliverymin.setText(listdata[position].getDistance() + " miles");
        }




      /*  if (listdata[position].getDeliverymin() != null && !listdata[position].getDeliverymin().isEmpty()) {
            holder.client_deliverymin.setText(listdata[position].getDeliverymin());
        } else {
            holder.min_time_layout.setVisibility(View.INVISIBLE);
        }*/


        Log.e("deliverymode", "" + listdata[position].getDelivery());
        Log.e("collectionmode", "" + listdata[position].getTakeaway());


//set shop open or not
//PRE-ORDER

        if (listdata[position].getTakeawayStautsDetails().equalsIgnoreCase("closed")) {
            holder.client_openstatus.setText("CLOSED");
            holder.client_openstatus.setBackgroundResource(R.drawable.close_background);
        } else if (listdata[position].getTakeawayStautsDetails().equalsIgnoreCase("preorder")) {
            holder.client_openstatus.setText("PRE-ORDER");
            holder.client_openstatus.setBackgroundResource(R.drawable.preorder_background);
        } else if (listdata[position].getTakeawayStautsDetails().equalsIgnoreCase("openorder")) {
            holder.client_openstatus.setText("OPEN");
            holder.client_openstatus.setBackgroundResource(R.drawable.open_background);
        } else {
            holder.client_openstatus.setText("CLOSED");
            holder.client_openstatus.setBackgroundResource(R.drawable.close_background);
        }



       if (myofferData.size() == 0) { //online offer
            if (offerData.size() == 0) { //offer
                holder.client_offerone.setVisibility(View.INVISIBLE);
            } else {
                holder.client_offerone.setVisibility(View.VISIBLE);
                if (offerData.get(offerData.size() - 1).getType().equalsIgnoreCase("0")) {
                    holder.client_offerone.setText(offerData.get(offerData.size() - 1).getProdiscount() + " " + "%" + " OFF ");// + myofferData.get(0).getDiscount_code()
                    holder.client_offerone.setBackgroundResource(R.color.white);
                } else {
                    holder.client_offerone.setText("£" + " " + offerData.get(offerData.size() - 1).getProdiscount() + " OFF ");// + myofferData.get(0).getDiscount_code()
                    holder.client_offerone.setBackgroundResource(R.color.white);
                }
              /*  holder.client_offerone.setText(offerData.get(offerData.size() - 1).getProdiscount() + " OFF ");// + myofferData.get(0).getDiscount_code()
                holder.client_offerone.setBackgroundResource(R.color.das_offer);*/
            }
        } else {
            String usetextview ="UseCode";
            holder.use_code_textview.setText(usetextview);
            if(myofferData.get(0).getDiscount_code().length() >= 8){
                holder.use_code_discount.setText("\""+myofferData.get(0).getDiscount_code()+"\"");  // Use Code Text view
                holder.three_dot.setVisibility(View.VISIBLE);
            }else{// Use Code Text view
                holder.use_code_discount.setText("\""+myofferData.get(0).getDiscount_code()+"\"");
                holder.three_dot.setVisibility(View.GONE);
            }

           holder.client_offerone.setVisibility(View.VISIBLE);

            if (myofferData.get(myofferData.size() - 1).getType().equalsIgnoreCase("0")) {
                //  offertypeone = "%";
                holder.client_offerone.setText(myofferData.get(myofferData.size() - 1).getDiscount() + " " + "%" + " OFF ");// + myofferData.get(0).getDiscount_code()
                holder.client_offerone.setBackgroundResource(R.color.white);
            } else {
                // offertypeone = "£";
                holder.client_offerone.setText("£" + " " + myofferData.get(myofferData.size() - 1).getDiscount() + " OFF ");// + myofferData.get(0).getDiscount_code()
                holder.client_offerone.setBackgroundResource(R.color.white);
            }
        }

        if(holder.client_offerone.getText().toString().isEmpty()){
            final LinearLayout.LayoutParams layoutparams = (LinearLayout.LayoutParams)holder.client_offerone.getLayoutParams();
            layoutparams.setMargins(5,0,0,0);
            holder.client_offerone.setLayoutParams(layoutparams);
        }


       /* if (myofferData.size() == 0) {
            holder.client_offerone.setVisibility(View.INVISIBLE);
            holder.client_offertwo.setVisibility(View.INVISIBLE);
        } else if (myofferData.size() == 1) {

            if (myofferData.get(0).getType().equalsIgnoreCase("0")) {
                offertypeone = "%";
                holder.client_offerone.setText(myofferData.get(0).getDiscount() + " " + offertypeone + " OFF ");// + myofferData.get(0).getDiscount_code()
                holder.client_offerone.setBackgroundResource(R.color.das_offer);
            } else {
                offertypeone = "£";
                holder.client_offerone.setText(offertypeone + " " + myofferData.get(0).getDiscount() + " OFF ");// + myofferData.get(0).getDiscount_code()
                holder.client_offerone.setBackgroundResource(R.color.das_offer);
            }

            holder.client_offertwo.setVisibility(View.INVISIBLE);
        } else if (myofferData.size() == 2) {
            if (myofferData.get(0).getType().equalsIgnoreCase("0")) {
                offertypeone = "%";
                holder.client_offerone.setText(myofferData.get(0).getDiscount() + " " + offertypeone + " OFF ");
                holder.client_offerone.setBackgroundResource(R.color.das_offer);
            } else {
                offertypeone = "£";
                holder.client_offerone.setText(offertypeone + " " + myofferData.get(0).getDiscount() + " OFF ");
                holder.client_offerone.setBackgroundResource(R.color.das_offer);
            }
            if (myofferData.get(1).getType().equalsIgnoreCase("0")) {
                offertypetwo = "%";
                holder.client_offertwo.setText(myofferData.get(1).getDiscount() + " " + offertypetwo + " OFF ");
                holder.client_offertwo.setBackgroundResource(R.color.das_offer);
            } else {
                offertypetwo = "£";
                holder.client_offertwo.setText(offertypeone + " " + myofferData.get(1).getDiscount() + " OFF ");
                holder.client_offertwo.setBackgroundResource(R.color.das_offer);
            }

        } else {
            if (myofferData.get(0).getType().equalsIgnoreCase("0")) {
                offertypeone = "%";
                holder.client_offerone.setText(myofferData.get(0).getDiscount() + " " + offertypeone + " OFF ");
                holder.client_offerone.setBackgroundResource(R.color.das_offer);
            } else {
                offertypeone = "£";
                holder.client_offerone.setText(offertypeone + " " + myofferData.get(0).getDiscount() + " OFF ");
                holder.client_offerone.setBackgroundResource(R.color.das_offer);
            }
            if (myofferData.get(1).getType().equalsIgnoreCase("0")) {
                offertypetwo = "%";
                holder.client_offertwo.setText(myofferData.get(1).getDiscount() + " " + offertypetwo + " OFF ");
                holder.client_offertwo.setBackgroundResource(R.color.das_offer);
            } else {
                offertypetwo = "£";
                holder.client_offertwo.setText(offertypeone + " " + myofferData.get(1).getDiscount() + " OFF ");
                holder.client_offertwo.setBackgroundResource(R.color.das_offer);
            }

        }*/

//sub menu add

        if (myclientcuisinesData.size() == 0) {
            holder.clent_submenu.setVisibility(View.INVISIBLE);
        } else if (myclientcuisinesData.size() == 1) {
            holder.clent_submenu.setText(myclientcuisinesData.get(0).getName());
        } else if (myclientcuisinesData.size() == 2) {
            holder.clent_submenu.setText(myclientcuisinesData.get(0).getName() + " , " + myclientcuisinesData.get(1).getName());
        } else {
            holder.clent_submenu.setText(myclientcuisinesData.get(0).getName() + " , " + myclientcuisinesData.get(1).getName());
        }

        //    Log.e("deliverymode", "" + listdata[position].getDelivery());
        //   Log.e("collectionmode", "" + listdata[position].getTakeaway());



        /*---------------------------Sql Lite DataBase----------------------------------------------------*/

        dbHelper = new SQLDBHelper(mContext);
        getContactsCount();

        holder.linerhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Favourite_data,MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("menuurlpath", listdata[position].getMenuurlpath());
                myEdit.putInt("client_id", Integer.parseInt(listdata[position].getClientID()));
                myEdit.commit();

                Intent intent = new Intent("custom-message-menuurlpath");
                intent.putExtra("menuurlpath", listdata[position].getMenuurlpath());
                intent.putExtra("client_id", listdata[position].getClientID());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //  public TextView report_details_address;
        public ImageView shopimg, client_logo, fav_btn;
        public TextView clent_name, clent_rating, client_takeaway, client_deliverytime, clent_delivery, client_deliverymin, client_openstatus, client_offerone, client_offertwo, clent_submenu;
        public LinearLayout takway_layout, delivery_layout, delivery_time_layout, min_time_layout, linerhead;
        public TextView coupon_code;
        LinearLayout View_your_star_rating;
        TextView use_code_discount,use_code_textview,three_dot;
        LinearLayout takeeay_layout,delivery_linearlayout;
        public ViewHolder(View itemView) {
            super(itemView);

            //this.report_details_address = (TextView) itemView.findViewById(R.id.report_details_address);

            this.shopimg = itemView.findViewById(R.id.shopimg);

           // this.client_logo = itemView.findViewById(R.id.client_logo);
            this.clent_name = itemView.findViewById(R.id.clent_name);
            this.clent_rating = itemView.findViewById(R.id.clent_rating);
            this.client_takeaway = itemView.findViewById(R.id.client_takeaway);
          //  this.client_deliverytime = itemView.findViewById(R.id.client_deliverytime);
            this.clent_delivery = itemView.findViewById(R.id.clent_delivery);
            this.client_deliverymin = itemView.findViewById(R.id.client_deliverymin);
            this.client_openstatus = itemView.findViewById(R.id.client_openstatus);
            this.client_offerone = itemView.findViewById(R.id.client_offerone);
            //  this.client_offertwo = itemView.findViewById(R.id.client_offertwo);
         //   this.takway_layout = itemView.findViewById(R.id.takway_layout);
        //    this.delivery_layout = itemView.findViewById(R.id.delivery_layout);
          //  this.delivery_time_layout = itemView.findViewById(R.id.delivery_time_layout);
         //   this.min_time_layout = itemView.findViewById(R.id.min_time_layout);
            this.clent_submenu = itemView.findViewById(R.id.clent_submenu);
            this.linerhead = itemView.findViewById(R.id.linerhead);
         //   this.fav_btn = itemView.findViewById(R.id.fav_btn);

           this.coupon_code = itemView.findViewById(R.id.coupon_code);
           this.View_your_star_rating = itemView.findViewById(R.id.View_your_star_rating);
           this.use_code_discount = itemView.findViewById(R.id.use_code_discount);

           this.use_code_textview = itemView.findViewById(R.id.use_code_textview);
           this.takeeay_layout = itemView.findViewById(R.id.takeeay_layout);
           this.delivery_linearlayout = itemView.findViewById(R.id.delivery_linearlayout);
           this.three_dot = itemView.findViewById(R.id.three_dot);


        }
    }

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    public int getContactsCount() {
        cursor = dbHelper.numberOfRows();

        Log.e("tmpStr10", "" + cursor);

        return cursor;
    }


    /*-------------------Loading Images------------------*/
    public void loadingshow() {

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


}