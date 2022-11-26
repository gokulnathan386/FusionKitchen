package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.R;
import com.fusionkitchen.model.address.getaddAddress_mode;
import com.fusionkitchen.model.address.updateaddress_mode;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetcartaddressListAdapter extends RecyclerView.Adapter<GetcartaddressListAdapter.ViewHolder> {
    private getaddAddress_mode.userdetail[] listdata;
    private Context mContext;
    String addresstypesshow;
    public GetcartaddressListAdapter(Context mContext, List<getaddAddress_mode.userdetail> listdata) {

        this.mContext = mContext;
        this.listdata = listdata.toArray(new getaddAddress_mode.userdetail[0]);

    }

    @Override
    public GetcartaddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_cart_address_list, parent, false);
        GetcartaddressListAdapter.ViewHolder viewHolder = new GetcartaddressListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(GetcartaddressListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Log.e("getaddresstype", "" + listdata[position].getType());
        Log.e("getaddressprimery", "" + listdata[position].getPrimary_address());


        if (listdata[position].getType().equalsIgnoreCase("1")) {
            holder.changea_ddress_titels.setText("Delivery to Home");
        } else if (listdata[position].getType().equalsIgnoreCase("2")) {
            holder.changea_ddress_titels.setText("Delivery to Office");
        } else if (listdata[position].getType().equalsIgnoreCase("3")) {
            holder.changea_ddress_titels.setText("Delivery to Other");
        }

        holder.change_cus_address.setText(listdata[position].getNo() + " " + listdata[position].getAddress1() + "," + listdata[position].getAddress2() + "," + listdata[position].getPostcode());
        for (int i = 0; i < listdata.length; i++) {

            Log.e("getaddressprimery", "" + listdata[i].getPrimary_address());

            if (listdata[i].getPrimary_address().equalsIgnoreCase("1")) {
                if (listdata[i].getType().equalsIgnoreCase("1")) {
                    addresstypesshow = "Delivery to Home";
                } else if (listdata[i].getType().equalsIgnoreCase("2")) {
                    addresstypesshow = "Delivery to Office";
                } else if (listdata[i].getType().equalsIgnoreCase("3")) {
                    addresstypesshow = "Delivery to Other";
                }

                Intent intent = new Intent("address_view-message");
                intent.putExtra("addressviewopen", "0");
                intent.putExtra("addresstitle", addresstypesshow);
                intent.putExtra("addressname", listdata[i].getNo() + " " + listdata[i].getAddress1() + "," + listdata[i].getAddress2() + "," + listdata[i].getPostcode());
                intent.putExtra("addressid", listdata[i].getId());
                intent.putExtra("customerpostcode", listdata[i].getPostcode());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                break;
            } else {
                if (listdata[0].getType().equalsIgnoreCase("1")) {
                    addresstypesshow = "Delivery to Home";
                    Intent intent = new Intent("address_view-message");
                    intent.putExtra("addressviewopen", "0");
                    intent.putExtra("addresstitle", addresstypesshow);
                    intent.putExtra("addressname", listdata[0].getNo() + " " + listdata[0].getAddress1() + "," + listdata[0].getAddress2() + "," + listdata[0].getPostcode());
                    intent.putExtra("addressid", listdata[0].getId());
                    intent.putExtra("customerpostcode", listdata[0].getPostcode());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                } else if (listdata[0].getType().equalsIgnoreCase("2")) {
                    addresstypesshow = "Delivery to Office";
                    Intent intent = new Intent("address_view-message");
                    intent.putExtra("addressviewopen", "0");
                    intent.putExtra("addresstitle", addresstypesshow);
                    intent.putExtra("addressname", listdata[0].getNo() + " " + listdata[0].getAddress1() + "," + listdata[0].getAddress2() + "," + listdata[0].getPostcode());
                    intent.putExtra("addressid", listdata[0].getId());
                    intent.putExtra("customerpostcode", listdata[0].getPostcode());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                } else if (listdata[0].getType().equalsIgnoreCase("3")) {
                    addresstypesshow = "Delivery to Other";
                    Intent intent = new Intent("address_view-message");
                    intent.putExtra("addressviewopen", "0");
                    intent.putExtra("addresstitle", addresstypesshow);
                    intent.putExtra("addressname", listdata[0].getNo() + " " + listdata[0].getAddress1() + "," + listdata[0].getAddress2() + "," + listdata[0].getPostcode());
                    intent.putExtra("addressid", listdata[0].getId());
                    intent.putExtra("customerpostcode", listdata[0].getPostcode());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }

        }


        holder.layout_titel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listdata[position].getType().equalsIgnoreCase("1")) {
                    addresstypesshow = "Delivery to Home";
                    Intent intent = new Intent("address_view-message");
                    intent.putExtra("addressviewopen", "1");
                    intent.putExtra("addresstitle", addresstypesshow);
                    intent.putExtra("addressname", listdata[position].getNo() + " " + listdata[position].getAddress1() + "," + listdata[position].getAddress2() + "," + listdata[position].getPostcode());
                    intent.putExtra("addressid", listdata[position].getId());
                    intent.putExtra("customerpostcode", listdata[position].getPostcode());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                } else if (listdata[position].getType().equalsIgnoreCase("2")) {
                    addresstypesshow = "Delivery to Office";
                    Intent intent = new Intent("address_view-message");
                    intent.putExtra("addressviewopen", "1");
                    intent.putExtra("addresstitle", addresstypesshow);
                    intent.putExtra("addressname", listdata[position].getNo() + " " + listdata[position].getAddress1() + "," + listdata[position].getAddress2() + "," + listdata[position].getPostcode());
                    intent.putExtra("addressid", listdata[position].getId());
                    intent.putExtra("customerpostcode", listdata[position].getPostcode());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                } else if (listdata[position].getType().equalsIgnoreCase("3")) {
                    addresstypesshow = "Delivery to Other";
                    Intent intent = new Intent("address_view-message");
                    intent.putExtra("addressviewopen", "1");
                    intent.putExtra("addresstitle", addresstypesshow);
                    intent.putExtra("addressname", listdata[position].getNo() + " " + listdata[position].getAddress1() + "," + listdata[position].getAddress2() + "," + listdata[position].getPostcode());
                    intent.putExtra("addressid", listdata[position].getId());
                    intent.putExtra("customerpostcode", listdata[position].getPostcode());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }

            }
        });


        holder.edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("address_update-message");
                intent.putExtra("addressid", listdata[position].getId());
                intent.putExtra("addresscid", listdata[position].getCid());
                intent.putExtra("addressno", listdata[position].getNo());
                intent.putExtra("addressaddress1", listdata[position].getAddress1());
                intent.putExtra("addressaddress2", listdata[position].getAddress2());
                intent.putExtra("addresspcode", listdata[position].getPostcode());
                intent.putExtra("addressgtype", listdata[position].getType());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView changea_ddress_titels, change_cus_address, edit_address;
        public RelativeLayout layout_titel;


        public ViewHolder(View itemView) {
            super(itemView);


            this.layout_titel = itemView.findViewById(R.id.layout_titel);
            this.changea_ddress_titels = itemView.findViewById(R.id.changea_ddress_titels);
            this.change_cus_address = itemView.findViewById(R.id.change_cus_address);
            this.edit_address = itemView.findViewById(R.id.edit_address);


        }
    }
}