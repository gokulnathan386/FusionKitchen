package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.R;
import com.fusionkitchen.model.dashboard.FetchFilterListModel;
import com.fusionkitchen.model.dashboard.location_fetch_details;
import java.util.List;




public class FetchFilterDetails extends RecyclerView.Adapter<FetchFilterDetails.MyViewHolder>{

    Context mcontext;
    List<FetchFilterListModel.GetAllCuisinefilter> allclient;


    public FetchFilterDetails(Context mContext, List<FetchFilterListModel.GetAllCuisinefilter> allclient) {
        this.mcontext = mContext;
        this.allclient=allclient;


    }



    @Override
    public FetchFilterDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.filterdesign, parent, false);
        FetchFilterDetails.MyViewHolder viewHolder = new FetchFilterDetails.MyViewHolder(listItem);
        return new FetchFilterDetails.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FetchFilterDetails.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(Integer.parseInt(allclient.get(position).getCount()) <= 1){
            holder.filtername.setText(allclient.get(position).getName());
        }else{
            holder.filtername.setText(allclient.get(position).getName() + " ("+allclient.get(position).getCount() +")");
        }


    }


    @Override
    public int getItemCount() {


        return allclient.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView filtername;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filtername = (TextView) itemView.findViewById(R.id.filtername);


        }

    }
}
