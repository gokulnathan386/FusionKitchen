package com.fusionkitchen.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.R;
import com.fusionkitchen.activity.DashboardListActivity;
import com.fusionkitchen.model.dashboard.location_fetch_details;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DashboardBannerAutoScrollAdapter extends RecyclerView.Adapter<DashboardBannerAutoScrollAdapter.ImageViewHolder> {
    private Context context;
    private List<location_fetch_details.BannerImage> imageUrls;

    public DashboardBannerAutoScrollAdapter(Context context, List<location_fetch_details.BannerImage> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner_item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Picasso.get().load(imageUrls.get(position).getImage()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof DashboardListActivity) {
                    DashboardListActivity dashboardListActivity = (DashboardListActivity) context;
                    dashboardListActivity.getFilterListView(0,"BannerFilter",null, imageUrls.get(position).getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}