package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.model.dashboard.location_fetch_details;
import java.util.List;


public class LocationfetchDetailsRest extends RecyclerView.Adapter<LocationfetchDetailsRest.MyViewHolder> {

    Context mcontext;
    List<location_fetch_details.showCuisine> all_cuisine;

    public LocationfetchDetailsRest(Context mContext, List<location_fetch_details.showCuisine> all_cuisine) {
        this.mcontext = mContext;
        this.all_cuisine=all_cuisine;

    }



    @Override
    public LocationfetchDetailsRest.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cuisine_layout_list, parent, false);
        LocationfetchDetailsRest.MyViewHolder viewHolder = new LocationfetchDetailsRest.MyViewHolder(listItem);

        return new LocationfetchDetailsRest.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(LocationfetchDetailsRest.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.cusinesName.setText(all_cuisine.get(position).getName());
        Glide.with(mcontext)
                .load(all_cuisine.get(position).getImage())
                .into(holder.cusinesImage);


    }


    @Override
    public int getItemCount() {


        return all_cuisine.size();

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


        TextView cusinesName;
        ImageView cusinesImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cusinesName = (TextView) itemView.findViewById(R.id.cusinesName);
            cusinesImage = (ImageView) itemView.findViewById(R.id.cusinesImage);

        }

    }
}

