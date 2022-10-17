package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.R;

import com.fusionkitchen.activity.Address_Book_Activity;
import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.address.getaddAddress_mode;
import com.fusionkitchen.model.address.updateaddress_mode;
import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class GetaddressListAdapter extends RecyclerView.Adapter<GetaddressListAdapter.ViewHolder> {
    private getaddAddress_mode.userdetail[] listdata;
    private Context mContext;

    /*   private static Switch lastChecked = null;
       private static int lastCheckedPos = 0;*/
    // RecyclerView recyclerView;
    public GetaddressListAdapter(Context mContext, List<getaddAddress_mode.userdetail> listdata) {
        this.mContext = mContext;
        this.listdata = listdata.toArray(new getaddAddress_mode.userdetail[0]);
    }

    @Override
    public GetaddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_address_book, parent, false);
        GetaddressListAdapter.ViewHolder viewHolder = new GetaddressListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(GetaddressListAdapter.ViewHolder holder, int position) {
        if (listdata[position].getType().equalsIgnoreCase("1")) {
            holder.simpleRadioButton.setText("Home");
        } else if (listdata[position].getType().equalsIgnoreCase("2")) {
            holder.simpleRadioButton.setText("Office");
        } else if (listdata[position].getType().equalsIgnoreCase("3")) {
            holder.simpleRadioButton.setText("Other");
        }
        holder.address_name.setText(listdata[position].getNo() + " " + listdata[position].getAddress1() + "," + listdata[position].getAddress2() + "," + listdata[position].getPostcode());
        holder.delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("address_delete-message");
                intent.putExtra("addressid", listdata[position].getId());
                intent.putExtra("addresscid", listdata[position].getCid());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });
        holder.address_edit.setOnClickListener(new View.OnClickListener() {
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
        if (listdata[position].getPrimary_address().equalsIgnoreCase("1")) {
            holder.simpleSwitch.setChecked(true);
            holder.simpleSwitch.setText("Primary");
        } else {
            holder.simpleSwitch.setChecked(false);
            holder.simpleSwitch.setText("Set as Primary");
        }
        holder.simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                //mContext = compoundButton.getContext();
                if (bChecked) {
                    updateprimaryAddress(listdata[position].getCid(), "1", listdata[position].getId());
                } else {
                    updateprimaryAddress(listdata[position].getCid(), "0", listdata[position].getId());
                }
            }
        });
       /* if (listdata[position].getPrimary_address().equalsIgnoreCase("1")) {
            holder.simpleSwitch.setChecked(true);
           *//* holder.simpleSwitch.setTag(new Integer(position));
            if (position == 0 && listdata[0].isSelected() && holder.simpleSwitch.isChecked()) {
                lastChecked = holder.simpleSwitch;
                lastCheckedPos = 0;
            }*//*
        } else {
            holder.simpleSwitch.setChecked(false);
           *//* holder.simpleSwitch.setChecked(listdata[position].isSelected());
            holder.simpleSwitch.setTag(new Integer(position));
            if (position == 0 && listdata[0].isSelected() && holder.simpleSwitch.isChecked()) {
                lastChecked = holder.simpleSwitch;
                lastCheckedPos = 0;
            }*//*

        }*/
        //   holder.simpleSwitch.setChecked(listdata[position].isSelected());
        //  holder.simpleSwitch.setTag(new Integer(position));
       /* if (position == 0 && listdata[0].isSelected() && holder.simpleSwitch.isChecked()) {
            lastChecked = holder.simpleSwitch;
            lastCheckedPos = 0;
        }
*/
       /* holder.simpleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch cb = (Switch) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();

                if (cb.isChecked()) {
                    // Toast.makeText(mContext, listdata[position].getId(), Toast.LENGTH_LONG).show();
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        listdata[lastCheckedPos].setSelected(false);

                    }
                    lastChecked = cb;
                    lastCheckedPos = clickedPos;
                    updateprimaryAddress(listdata[position].getCid(), "1", listdata[position].getId());
                } else
                    lastChecked = null;
                listdata[clickedPos].setSelected(cb.isChecked());
                //  updateprimaryAddress(listdata[position].getCid(), "0", listdata[position].getId());
            }

            private void updateprimaryAddress(String str_cid, String str_prim, String str_ids) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", str_cid);
                params.put("primarykey", str_prim);
                params.put("id", str_ids);


                ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                Call<updateaddress_mode> call = apiService.updateprimaryAddress(params);


                call.enqueue(new Callback<updateaddress_mode>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call<updateaddress_mode> call, Response<updateaddress_mode> response) {

                        int statusCode = response.code();

                        if (statusCode == 200) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                Intent intent = new Intent("update_primery_message");


                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                            } else {
                                Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_LONG).show();

                            }

                        } else {
                            Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<updateaddress_mode> call, Throwable t) {
                        Log.e("menuThrowable", "" + t);
                        Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                });


            }
        });*/


    }

    private void updateprimaryAddress(String str_cid, String str_prim, String str_ids) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", str_cid);
        params.put("primarykey", str_prim);
        params.put("id", str_ids);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<updateaddress_mode> call = apiService.updateprimaryAddress(params);


        call.enqueue(new Callback<updateaddress_mode>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<updateaddress_mode> call, Response<updateaddress_mode> response) {

                int statusCode = response.code();

                if (statusCode == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        Intent intent = new Intent("update_primery-message");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                    } else {
                        Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<updateaddress_mode> call, Throwable t) {
                Log.e("menuThrowable", "" + t);
                Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView simpleRadioButton, address_name, address_edit;
        public RelativeLayout layout_titel;
        public ImageView delete_address;
        public Switch simpleSwitch;

        public ViewHolder(View itemView) {
            super(itemView);


            this.layout_titel = itemView.findViewById(R.id.layout_titel);

            this.simpleRadioButton = itemView.findViewById(R.id.simpleRadioButton);
            this.address_name = itemView.findViewById(R.id.address_name);
            this.address_edit = itemView.findViewById(R.id.address_edit);
            this.delete_address = itemView.findViewById(R.id.delete_address);
            this.simpleSwitch = itemView.findViewById(R.id.simpleSwitch);


        }
    }
}