package com.fusionkitchen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.model.paymentgatway.appkey;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.R;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.activity.Order_Status_Activity;
import com.fusionkitchen.model.order_history.order_details_list_show;
import com.fusionkitchen.model.order_history.orderdetail_mode;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.model.orderstatus.orderstatus_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderstatusListAdapter extends RecyclerView.Adapter<OrderstatusListAdapter.ViewHolder> {


    private Context mContext;


    private orderstatus_model.orderstatus[] orderhistory;

    private int num;
    String bulkeyfullUrl,token,apikey;

    // RecyclerView recyclerView;
    public OrderstatusListAdapter(Context mContext, List<orderstatus_model.orderstatus> orderhistory, int num) {

        this.orderhistory = orderhistory.toArray(new orderstatus_model.orderstatus[0]);
        this.mContext = mContext;
        this.num = num;


    }

    @Override
    public OrderstatusListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_order_status_list, parent, false);
        OrderstatusListAdapter.ViewHolder viewHolder = new OrderstatusListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderstatusListAdapter.ViewHolder holder, int position) {
        final orderstatus_model.orderstatus myListData = orderhistory[position];


        //client logo set
        Picasso.get()
                .load("https://fusionbucket.co.uk/img/" + orderhistory[position].getClient().getLogo())
                .placeholder(R.drawable.hederlocoplaceimg)
                .error(R.drawable.hederlocoplaceimg)
                .into(holder.list_client_logo);


        holder.list_total_amt.setText("£ " + orderhistory[position].getOrder().getTotal());
        holder.list_client_name.setText(orderhistory[position].getClient().getClientname());
        holder.list_order_date.setText(orderhistory[position].getOrder().getDate());

        if (orderhistory[position].getOrder().getStatus().equalsIgnoreCase("0")) {
            // holder.raw_order_list_status.setText("In Progress");
            holder.raw_order_list_status.setText("Track");
        } else if (orderhistory[position].getOrder().getStatus().equalsIgnoreCase("1")) {
            // holder.raw_order_list_status.setText("In Progress");
            holder.raw_order_list_status.setText("Track");
        } else if (orderhistory[position].getOrder().getStatus().equalsIgnoreCase("2")) {
            //  holder.raw_order_list_status.setText("Rejected");
            holder.raw_order_list_status.setText("Rejected");
        } else if (orderhistory[position].getOrder().getStatus().equalsIgnoreCase("3") && orderhistory[position].getOrder().getOtype().equalsIgnoreCase("0")) {
            // holder.raw_order_list_status.setText("Delivered");
            holder.raw_order_list_status.setText("Track");
        } else if (orderhistory[position].getOrder().getStatus().equalsIgnoreCase("3") && orderhistory[position].getOrder().getOtype().equalsIgnoreCase("1")) {
            //  holder.raw_order_list_status.setText("Collected");
            holder.raw_order_list_status.setText("Track");
        }


        holder.rel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   Intent intent = new Intent(mContext, Order_Status_Activity.class);
                intent.putExtra("orderid", orderhistory[position].getOrder().getId());
                intent.putExtra("orderpath", orderhistory[position].getClient().getPath());
                intent.putExtra("orderdate", orderhistory[position].getOrder().getDate());
                intent.putExtra("clientname", orderhistory[position].getClient().getClientname());
                intent.putExtra("clientid", orderhistory[position].getClient().getCid());
                intent.putExtra("clientphonenumber", orderhistory[position].getClient().getClientno());
                mContext.startActivity(intent);*/




                getpublisekey(orderhistory[position].getOrder().getId(),orderhistory[position].getClient().getPath(),
                        orderhistory[position].getOrder().getDate(),orderhistory[position].getClient().getClientname(),
                        orderhistory[position].getClient().getCid(), orderhistory[position].getClient().getClientno(),
                        orderhistory[position].getClient().getPostcode(),orderhistory[position].getClient().getArea(),
                        orderhistory[position].getClient().getAddresslocation(),orderhistory[position].getClient().getLatitude(),
                        orderhistory[position].getClient().getLongitude()
                        );



            }


        });


    }



    private void getpublisekey(String id,String menuurlpath,String Date,String Clientname,String cid,String Clientno,
                               String O_postcode,String O_area,String O_address,String O_lat,String O_lon
    ) {
        bulkeyfullUrl = menuurlpath + "/stripeAppId";
        Log.d("stripeappid",bulkeyfullUrl);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<appkey> call = apiService.stripepubliskey(bulkeyfullUrl);
        call.enqueue(new Callback<appkey>() {
            @Override
            public void onResponse(Call<appkey> call, Response<appkey> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        Log.e("api_id", "" + response.body().getData().getApi_id());
                        Log.e("securitykey", "" + response.body().getData().getSecuritykey());
                        token = response.body().getData().getSecuritykey();
                        apikey = response.body().getData().getApi_id();

                        Intent intent = new Intent(mContext, Order_Status_Activity.class);
                        intent.putExtra("orderid", id);
                        intent.putExtra("orderpath",menuurlpath);
                        intent.putExtra("orderdate",Date);
                        intent.putExtra("clientname", Clientname);
                        intent.putExtra("clientid", cid);
                        intent.putExtra("clientphonenumber",Clientno);
                        intent.putExtra("gpay_apikey",apikey);
                        /*----------------------Order Tracking--------------------*/
                        intent.putExtra("order_postcode",O_postcode);
                        intent.putExtra("order_area",O_area);
                        intent.putExtra("order_address",O_address);
                        intent.putExtra("order_lat",O_lat);
                        intent.putExtra("order_lon",O_lon);

                        mContext.startActivity(intent);

                    }
                }
            }

            @Override
            public void onFailure(Call<appkey> call, Throwable t) {
                Log.d("API_Key_Error"," " + t);
             }
        });
    }

    @Override
    public int getItemCount() {


        if (num * 10 > orderhistory.length) {

            return orderhistory.length;
        } else {

            return num * 10;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView list_client_logo;


        public TextView list_total_amt, list_client_name, list_order_date, raw_order_list_status;

        public RelativeLayout rel_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.list_client_logo = itemView.findViewById(R.id.list_client_logo);
            this.list_total_amt = itemView.findViewById(R.id.list_total_amt);
            this.list_client_name = itemView.findViewById(R.id.list_client_name);
            this.list_order_date = itemView.findViewById(R.id.list_order_date);
            this.raw_order_list_status = itemView.findViewById(R.id.raw_order_list_status);


            this.rel_layout = itemView.findViewById(R.id.rel_layout);


        }
    }
}