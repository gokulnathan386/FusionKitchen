package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.model.dashboard.location_fetch_details;

import java.util.List;



public class MostPopularRestListAdapter extends RecyclerView.Adapter<MostPopularRestListAdapter.MyViewHolder>{

    Context mcontext;
    List<location_fetch_details.restaurantList> allclient;


    public MostPopularRestListAdapter(Context mContext, List<location_fetch_details.restaurantList> allclient) {
        this.mcontext = mContext;
        this.allclient=allclient;


    }



    @Override
    public MostPopularRestListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        listItem = layoutInflater.inflate(R.layout.dashboard_recommedend_mostpopular, parent, false);

        MostPopularRestListAdapter.MyViewHolder viewHolder = new MostPopularRestListAdapter.MyViewHolder(listItem);
        return new MostPopularRestListAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MostPopularRestListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {



        holder.ClientName.setText(allclient.get(position).getName());
        holder.cookingTime.setText(allclient.get(position).getCookingTimeStart() + " - " + allclient.get(position).getCookingTimeEnd()+ " " + "Mins");
        holder.milesAway.setText(allclient.get(position).getMiles() + " " +"Miles Away");


        String ratingCondition = allclient.get(position).getRating().getRate();

        if(Double.parseDouble(ratingCondition) < 3){
            holder.ratingDetailsCount.setText(allclient.get(position).getRating().getRate() + " " +"(New)");
        }else{
            if(Integer.parseInt(allclient.get(position).getRating().getCount()) > 1) {
                holder.ratingDetailsCount.setText(allclient.get(position).getRating().getRate() + " " + "Good" +" " + "(" +allclient.get(position).getRating().getCount()+")");
            }else{
                holder.ratingDetailsCount.setText(allclient.get(position).getRating().getRate() + " " + "Good");
            }

        }


        Glide.with(mcontext)
                .load(allclient.get(position).getBackgroundImage())
                .into(holder.recommendRestImage);

        if(allclient.get(position).getRestaurantStatus().getStatus().equalsIgnoreCase("0")){  // open
            holder.takewayStatus.setText("Open");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takeOpen));
        }else if(allclient.get(position).getRestaurantStatus().getStatus().equalsIgnoreCase("1")){ // pre order
            holder.takewayStatus.setText("Pre Order");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takePreOrder));
        }else if(allclient.get(position).getRestaurantStatus().getStatus().equalsIgnoreCase("2")){ // Closed
            holder.takewayStatus.setText("Closed");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takeClosed));
        }else if(allclient.get(position).getRestaurantStatus().getStatus().equalsIgnoreCase("3")){ // collection or delivery -> closed
            holder.takewayStatus.setText("Open");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takeOpen));
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


        TextView ClientName,milesAway,cookingTime,ratingDetailsCount,takewayStatus;
        ImageView recommendRestImage;
        LinearLayout takewayBackgroundColor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ClientName = (TextView) itemView.findViewById(R.id.ClientName);
            milesAway = (TextView) itemView.findViewById(R.id.milesAway);
            cookingTime = (TextView) itemView.findViewById(R.id.cookingTime);
            ratingDetailsCount = (TextView) itemView.findViewById(R.id.ratingDetailsCount);
            recommendRestImage = (ImageView) itemView.findViewById(R.id.recommendRestImage);
            takewayStatus = (TextView) itemView.findViewById(R.id.takewayStatus);
            takewayBackgroundColor = (LinearLayout) itemView.findViewById(R.id.takewayBackgroundColor);

        }

    }
}



