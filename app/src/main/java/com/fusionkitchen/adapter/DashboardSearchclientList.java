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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;


import java.util.List;


import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;

import com.fusionkitchen.model.home_model.serachgetshop_modal;


import static android.content.Context.MODE_PRIVATE;

//Search client
public class DashboardSearchclientList extends RecyclerView.Adapter<DashboardSearchclientList.ViewHolder> {
    private serachgetshop_modal.clientinfos[] listdata;
    private Context mContext;

    String offertypeone, offertypetwo;
    // public String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7"};
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
    public DashboardSearchclientList(Context mContext, List<serachgetshop_modal.clientinfos> listdata) {

        this.mContext = mContext;
        this.listdata = listdata.toArray(new serachgetshop_modal.clientinfos[0]);

    }

    @Override
    public DashboardSearchclientList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_dashboard_search_details, parent, false);
        DashboardSearchclientList.ViewHolder viewHolder = new DashboardSearchclientList.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(DashboardSearchclientList.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        List<serachgetshop_modal.clientinfos.onlinediscount> myofferData = listdata[position].getOnlinediscount();

        List<serachgetshop_modal.clientinfos.client_cuisines> myclientcuisinesData = listdata[position].getClient_cuisines();


        List<serachgetshop_modal.clientinfos.offer> offerData = listdata[position].getOffer();
        // holder.relative.setBackgroundColor(Color.parseColor(mColors[position % 4]));

        //banner img set





       // Log.d("Clientpathurl",""+listdata[position].getpath());
        /*--------------Login details get SharedPreferences------------------*/
        slogin = mContext.getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));

        Log.e("dasboard_user_id", "" + user_id);

        if (user_id != null && !user_id.equals("")) {
         //   holder.fav_btn.setVisibility(View.VISIBLE);

            if (listdata[position].getFavroite().equalsIgnoreCase("0")) {
               // holder.fav_btn.setImageResource(R.drawable.heartnoselect);
            } else {
                //holder.fav_btn.setImageResource(R.drawable.heartselect);
            }


        } else {
            //holder.fav_btn.setVisibility(View.GONE);
        }


        Picasso.get()
                .load(listdata[position].getBgimge())
                .placeholder(R.drawable.headerplaceholder)
                .error(R.drawable.headerplaceholder)
                .into(holder.shopimg);


       /* ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        holder.shopimg.setColorFilter(filter);*/


//client logo set
     /*   Picasso.get()
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
            holder.collection_takeway.setVisibility(View.INVISIBLE);
        }


        if (listdata[position].getDeliverytime() != null && !listdata[position].getDeliverytime().isEmpty()) {

            holder.clent_delivery.setText(listdata[position].getDeliverytime());

        } else {
           holder.delivery_linearlayout.setVisibility(View.INVISIBLE);
        }


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
         // holder.client_openstatus.setBackgroundResource(R.drawable.open_background);
            holder.client_openstatus.setBackgroundResource(R.drawable.preorder_background);
        } else if (listdata[position].getTakeawayStautsDetails().equalsIgnoreCase("openorder")) {
            holder.client_openstatus.setText("OPEN");
            holder.client_openstatus.setBackgroundResource(R.drawable.open_background);
        } else {
            holder.client_openstatus.setText("CLOSED");
            holder.client_openstatus.setBackgroundResource(R.drawable.close_background);
        }


      /*  if (listdata[position].getStatus().equalsIgnoreCase("0")) {
            holder.client_openstatus.setText("CLOSED");
            holder.client_openstatus.setBackgroundResource(R.drawable.close_background);
        } else if (listdata[position].getStatus().equalsIgnoreCase("1")) {
            holder.client_openstatus.setText(listdata[position].getOrdernow());
            holder.client_openstatus.setBackgroundResource(R.drawable.open_background);
        } else {
            holder.client_openstatus.setText("CLOSED");
            holder.client_openstatus.setBackgroundResource(R.drawable.close_background);
        }*/

/*        if (listdata[position].getStatus().equalsIgnoreCase("0")) {
            holder.client_openstatus.setText(listdata[position].getPreorder());

            if (holder.client_openstatus.getText().toString().equalsIgnoreCase("Pre-Order")) {
                holder.client_openstatus.setBackgroundResource(R.drawable.preorder_background);
            } else {
                holder.client_openstatus.setText("CLOSED");
                holder.client_openstatus.setBackgroundResource(R.drawable.close_background);
            }
        } else if (listdata[position].getStatus().equalsIgnoreCase("1")) {
            holder.client_openstatus.setText(listdata[position].getOrdernow());
            holder.client_openstatus.setBackgroundResource(R.drawable.open_background);
        } else {
            holder.client_openstatus.setText("closed");
            holder.client_openstatus.setBackgroundResource(R.drawable.close_background);
        }*/


//set offer set

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
               /* holder.client_offerone.setText(offerData.get(offerData.size() - 1).getProdiscount() + " OFF ");// + myofferData.get(0).getDiscount_code()
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
                // offertypeone = "%";
                holder.client_offerone.setText(myofferData.get(myofferData.size() - 1).getDiscount() + " " + "%" + " OFF ");// + myofferData.get(0).getDiscount_code()
                holder.client_offerone.setBackgroundResource(R.color.white);
            } else {
                // offertypeone = "£";
                holder.client_offerone.setText("£" + " " + myofferData.get(myofferData.size() - 1).getDiscount() + " OFF ");// + myofferData.get(0).getDiscount_code()
                holder.client_offerone.setBackgroundResource(R.color.white);
            }
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


/*        if (myclientcuisinesData.size() == 0) {
            holder.clent_submenu.setVisibility(View.INVISIBLE);
        } else if (myclientcuisinesData.size() == 1) {
            holder.clent_submenu.setText(myclientcuisinesData.get(0).getName());
        } else if (myclientcuisinesData.size() == 2) {
            holder.clent_submenu.setText(myclientcuisinesData.get(0).getName() + "," + myclientcuisinesData.get(1).getName());
        } else if (myclientcuisinesData.size() == 3) {
            holder.clent_submenu.setText(myclientcuisinesData.get(0).getName() + "," + myclientcuisinesData.get(1).getName() + "," + myclientcuisinesData.get(2).getName());
        } else {
            holder.clent_submenu.setText(myclientcuisinesData.get(0).getName() + "," + myclientcuisinesData.get(1).getName() + "," + myclientcuisinesData.get(2).getName());
        }*/

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
                  Log.d("Clientpathurl",""+listdata[position].getpath());
                     Intent intent = new Intent("custom-message-menuurlpath");
                  //  intent.putExtra("menuurlpath", listdata[position].getMenuurl());
                    intent.putExtra("menuurlpath", listdata[position].getpath());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                //Toast.makeText(view.getContext(), "click on item: " + listdata[position].getId(), Toast.LENGTH_LONG).show();
             /*   Intent intent = new Intent(mContext, Item_Menu_Activity.class);
                intent.putExtra("menuurlpath", listdata[position].getMenuurlpath());

                mContext.startActivity(intent);*/

             /*   //get intent values
                Intent intent = getIntent();
                orderid = intent.getStringExtra("orderid");
                str_iid = intent.getStringExtra("iid");*/
            }
        });


        //Add Fab
/*        holder.fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingshow();


                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("path", listdata[position].getMenuurl());
                params.put("client_id", listdata[position].getClientID());

                ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                Call<insertfavorite_mode> call = apiService.insertfavorite(params);

                Log.e("fav_params", "" + params);

                call.enqueue(new Callback<insertfavorite_mode>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call<insertfavorite_mode> call, Response<insertfavorite_mode> response) {

                        int statusCode = response.code();
                        Log.e("fav_statusCode", "" + statusCode);

                        if (statusCode == 200) {
                            hideloading();
                            if (response.body().getStatus().equalsIgnoreCase("true")) {


                                if (response.body().getData().equalsIgnoreCase("delete successfully")) {
                                    holder.fav_btn.setImageResource(R.drawable.heartnoselect);
                                } else {
                                    holder.fav_btn.setImageResource(R.drawable.heartselect);
                                }
                                Intent intent = new Intent("custom-message-reloadlist");
                                intent.putExtra("reloadlist", "2");
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                                Toast.makeText(mContext, response.body().getData(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            hideloading();
                            Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<insertfavorite_mode> call, Throwable t) {
                        hideloading();
                        Log.e("menuThrowable", "" + t);
                        Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });*/

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
        LinearLayout View_your_star_rating,collection_takeway,delivery_linearlayout;
        TextView use_code_textview,use_code_discount,three_dot;


        public ViewHolder(View itemView) {
            super(itemView);

            //this.report_details_address = (TextView) itemView.findViewById(R.id.report_details_address);

            this.shopimg = itemView.findViewById(R.id.shopimg);
           // this.client_logo = itemView.findViewById(R.id.client_logo);
            this.clent_name = itemView.findViewById(R.id.clent_name);
            this.clent_rating = itemView.findViewById(R.id.clent_rating);
            this.client_takeaway = itemView.findViewById(R.id.client_takeaway);
            this.client_deliverytime = itemView.findViewById(R.id.client_deliverytime);
            this.clent_delivery = itemView.findViewById(R.id.clent_delivery);
            this.client_deliverymin = itemView.findViewById(R.id.client_deliverymin);
            this.client_openstatus = itemView.findViewById(R.id.client_openstatus);
            this.client_offerone = itemView.findViewById(R.id.client_offerone);
            // this.client_offertwo = itemView.findViewById(R.id.client_offertwo);
            //this.takway_layout = itemView.findViewById(R.id.takway_layout);
            //this.delivery_layout = itemView.findViewById(R.id.delivery_layout);
            //this.delivery_time_layout = itemView.findViewById(R.id.delivery_time_layout);
            //this.min_time_layout = itemView.findViewById(R.id.min_time_layout);
            this.clent_submenu = itemView.findViewById(R.id.clent_submenu);
            this.linerhead = itemView.findViewById(R.id.linerhead);
        //    this.fav_btn = itemView.findViewById(R.id.fav_btn);

            this.View_your_star_rating = itemView.findViewById(R.id.View_your_star_rating);
            this.collection_takeway = itemView.findViewById(R.id.collection_takeway);
            this.delivery_linearlayout = itemView.findViewById(R.id.delivery_linearlayout);
            this.use_code_textview = itemView.findViewById(R.id.use_code_textview);
            this.use_code_discount =itemView.findViewById(R.id.use_code_discount);
            this.three_dot = itemView.findViewById(R.id.three_dot);
        }
    }

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    public int getContactsCount() {
        cursor = dbHelper.numberOfRows();

        Log.e("tmpStr10", "" + cursor);
        //Log.e("totalitemamut12", "" + totalitemamut);
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