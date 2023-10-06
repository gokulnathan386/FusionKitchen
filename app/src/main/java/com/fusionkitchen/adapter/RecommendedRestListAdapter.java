package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class RecommendedRestListAdapter extends RecyclerView.Adapter<RecommendedRestListAdapter.MyViewHolder>{

        Context mcontext;
        List<location_fetch_details.AllClientRestDetails> allclient;
        String test;

public RecommendedRestListAdapter(Context mContext, List<location_fetch_details.AllClientRestDetails> allclient,String test) {
        this.mcontext = mContext;
        this.allclient=allclient;
        this.test=test;

        }



@Override
public RecommendedRestListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
              View listItem;
            if(test.equalsIgnoreCase("1")){
                 listItem = layoutInflater.inflate(R.layout.dashboard_rcommednded_design, parent, false);
            }else{
                listItem = layoutInflater.inflate(R.layout.dashboard_recommedend_mostpopular, parent, false);
            }

        RecommendedRestListAdapter.MyViewHolder viewHolder = new RecommendedRestListAdapter.MyViewHolder(listItem);
        return new RecommendedRestListAdapter.MyViewHolder(listItem);
        }

@Override
public void onBindViewHolder(RecommendedRestListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {



         holder.ClientName.setText(allclient.get(position).getClientName());

         Glide.with(mcontext)
        .load(allclient.get(position).getBgimge())
        .into(holder.recommendRestImage);





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


    TextView ClientName;
    ImageView recommendRestImage;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        ClientName = (TextView) itemView.findViewById(R.id.ClientName);
        recommendRestImage = (ImageView) itemView.findViewById(R.id.recommendRestImage);

    }

}
}

