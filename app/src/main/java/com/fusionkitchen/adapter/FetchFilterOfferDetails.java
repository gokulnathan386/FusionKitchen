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
import java.util.List;



public class FetchFilterOfferDetails extends RecyclerView.Adapter<FetchFilterOfferDetails.MyViewHolder>{

    Context mcontext;
    List<FetchFilterListModel.GetOfferFilterDetails> allclient;

    public FetchFilterOfferDetails(Context mContext, List<FetchFilterListModel.GetOfferFilterDetails> allclient) {
        this.mcontext = mContext;
        this.allclient=allclient;

    }



    @Override
    public FetchFilterOfferDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.filterofferdesign, parent, false);
        FetchFilterOfferDetails.MyViewHolder viewHolder = new FetchFilterOfferDetails.MyViewHolder(listItem);
        return new FetchFilterOfferDetails.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FetchFilterOfferDetails.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.filterOfferName.setText(allclient.get(position).getType());

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


        TextView filterOfferName;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filterOfferName = (TextView) itemView.findViewById(R.id.filterOfferName);


        }

    }
}
