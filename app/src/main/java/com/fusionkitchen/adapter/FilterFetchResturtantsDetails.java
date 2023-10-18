package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.fusionkitchen.model.FilterFetchDetails;
import com.fusionkitchen.model.dashboard.location_fetch_details;

import java.util.List;

public class FilterFetchResturtantsDetails extends RecyclerView.Adapter<FilterFetchResturtantsDetails.MyViewHolder>{

    Context mcontext;
    List<FilterFetchDetails.showRestaurantist> allclient;


    public FilterFetchResturtantsDetails(Context mContext, List<FilterFetchDetails.showRestaurantist> allclient) {
        this.mcontext = mContext;
        this.allclient=allclient;


    }



    @Override
    public FilterFetchResturtantsDetails.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        listItem = layoutInflater.inflate(R.layout.dashboard_recommedend_mostpopular, parent, false);

        FilterFetchResturtantsDetails.MyViewHolder viewHolder = new FilterFetchResturtantsDetails.MyViewHolder(listItem);
        return new FilterFetchResturtantsDetails.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FilterFetchResturtantsDetails.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {



        holder.ClientName.setText(allclient.get(position).getRestaurantList().getName());
        holder.cookingTime.setText(allclient.get(position).getRestaurantList().getCookingTimeStart() + " - " + allclient.get(position).getRestaurantList().getCookingTimeEnd()+ " " + "Mins");
        holder.milesAway.setText(allclient.get(position).getRestaurantList().getMiles() + " " +"Miles Away");

        holder.offerItemTxt.setText(allclient.get(position).getRestaurantList().getDiscount().getMinOrder() + " " +
                allclient.get(position).getRestaurantList().getDiscount().getDiscountType() +" " +
                allclient.get(position).getRestaurantList().getDiscount().getDescription() +" " +
                allclient.get(position).getRestaurantList().getDiscount().getDiscount() +" " +
                allclient.get(position).getRestaurantList().getDiscount().getDiscountType()

        );

        String ratingCondition = allclient.get(position).getRestaurantList().getRating().getRate();

        if(Double.parseDouble(ratingCondition) < 3){
            holder.ratingDetailsCount.setText(allclient.get(position).getRestaurantList().getRating().getRate() + " " +"(New)");
        }else{
            if(Integer.parseInt(allclient.get(position).getRestaurantList().getRating().getCount()) > 1) {
                holder.ratingDetailsCount.setText(allclient.get(position).getRestaurantList().getRating().getRate() + " " + "Good" +" " + "(" +allclient.get(position).getRestaurantList().getRating().getCount()+")");
            }else{
                holder.ratingDetailsCount.setText(allclient.get(position).getRestaurantList().getRating().getRate() + " " + "Good");
            }

        }


        Glide.with(mcontext)
                .load(allclient.get(position).getRestaurantList().getBackgroundImage())
                .into(holder.recommendRestImage);

        if(allclient.get(position).getRestaurantList().getRestaurantStatus().getStatus().equalsIgnoreCase("0")){  // open
            holder.takewayStatus.setText("Open");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takeOpen));
        }else if(allclient.get(position).getRestaurantList().getRestaurantStatus().getStatus().equalsIgnoreCase("1")){ // pre order
            holder.takewayStatus.setText("Pre Order");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takePreOrder));
        }else if(allclient.get(position).getRestaurantList().getRestaurantStatus().getStatus().equalsIgnoreCase("2")){ // Closed
            holder.takewayStatus.setText("Closed");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takeClosed));
        }else if(allclient.get(position).getRestaurantList().getRestaurantStatus().getStatus().equalsIgnoreCase("3")){ // collection or delivery -> closed
            holder.takewayStatus.setText("Open");
            holder.takewayBackgroundColor.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.takeOpen));
        }

        String cuisinename = "";

        for(int k=0;k<allclient.get(position).getRestaurantList().getCuisineName().size();k++){

            if(k==0){
                cuisinename = allclient.get(position).getRestaurantList().getCuisineName().get(k);
            }else{
                cuisinename = cuisinename + "," + allclient.get(position).getRestaurantList().getCuisineName().get(k);
            }

        }

        holder.cusinesName.setText(cuisinename);



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


        TextView ClientName,milesAway,cookingTime,ratingDetailsCount,takewayStatus,offerItemTxt,cusinesName;
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
            offerItemTxt = (TextView) itemView.findViewById(R.id.offerItemTxt);
            cusinesName = (TextView) itemView.findViewById(R.id.cusinesName);

        }

    }
}



