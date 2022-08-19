package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import com.fusionkitchen.R;


import com.fusionkitchen.model.moreinfo.review_list_model;

import static java.lang.Integer.parseInt;

public class ReviewDetailsAdapter extends RecyclerView.Adapter<ReviewDetailsAdapter.ViewHolder> {
    private review_list_model.review_list[] listdata;
    private Context mContext;


    // RecyclerView recyclerView;
    public ReviewDetailsAdapter(Context mContext, List<review_list_model.review_list> listdata) {

        this.mContext = mContext;
        this.listdata = listdata.toArray(new review_list_model.review_list[0]);

    }

    @Override
    public ReviewDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_review_details, parent, false);
        ReviewDetailsAdapter.ViewHolder viewHolder = new ReviewDetailsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ReviewDetailsAdapter.ViewHolder holder, int position) {


        holder.name.setText(listdata[position].getName());
        holder.review_date.setText(listdata[position].getDatetime());
        holder.review_details.setText(listdata[position].getComment());


        if (listdata[position].getRate().equalsIgnoreCase("")) {
            holder.rating.setRating(0);

        } else {
            holder.rating.setRating(parseInt(listdata[position].getRate()));

        }

        holder.review_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.replay_review.setVisibility(View.VISIBLE);
                holder.review_hide.setVisibility(View.VISIBLE);
                holder.review_view.setVisibility(View.GONE);
            }
        });
        holder.review_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.replay_review.setVisibility(View.GONE);
                holder.review_hide.setVisibility(View.GONE);
                holder.review_view.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView name, review_details, review_date, review_view,review_hide;

        public RatingBar rating;

        public RelativeLayout replay_review;

        public ViewHolder(View itemView) {
            super(itemView);

            //this.report_details_address = (TextView) itemView.findViewById(R.id.report_details_address);

            this.name = itemView.findViewById(R.id.name);
            this.review_details = itemView.findViewById(R.id.review_details);
            this.review_date = itemView.findViewById(R.id.review_date);
            this.review_view = itemView.findViewById(R.id.review_view);
            this.rating = itemView.findViewById(R.id.rating);

            this.replay_review = itemView.findViewById(R.id.replay_review);
            this.review_hide= itemView.findViewById(R.id.review_hide);

        }
    }
}