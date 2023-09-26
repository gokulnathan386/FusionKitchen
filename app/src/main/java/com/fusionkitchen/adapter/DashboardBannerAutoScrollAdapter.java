package com.fusionkitchen.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DashboardBannerAutoScrollAdapter extends RecyclerView.Adapter<DashboardBannerAutoScrollAdapter.ImageViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public DashboardBannerAutoScrollAdapter(Context context, List<String> imageUrls) {
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
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // String imageUrl = imageUrls.get(position);
        Log.d("wsfnsdjksdn", " " + imageUrls.get(position));
        Picasso.get().load(imageUrls.get(position)).into(holder.imageView);

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