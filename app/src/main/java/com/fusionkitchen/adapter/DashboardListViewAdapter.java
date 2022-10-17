package com.fusionkitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.R;
import com.fusionkitchen.model.DashboardListViewModel;
import com.fusionkitchen.model.EatListPostelModel;

import java.util.List;

public class DashboardListViewAdapter extends RecyclerView.Adapter<DashboardListViewAdapter.MyViewHolder> {
    List<DashboardListViewModel> dashboardListViewModels;
    Context context;
    private int lastPosition = -1;
    public DashboardListViewAdapter(List<DashboardListViewModel> dashboardListViewModels, Context context) {
        this.dashboardListViewModels=dashboardListViewModels;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.postal_listview_design, viewGroup, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        DashboardListViewModel data=dashboardListViewModels.get(i);
        viewHolder.icon.setImageResource(data.image);
        //     viewHolder.eat_name.setText(String.valueOf(data.name));
        //setAnimation(viewHolder.parent, i);

    }
   /* private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }*/
    @Override
    public int getItemCount() {
        return dashboardListViewModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        // TextView eat_name;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            // eat_name=itemView.findViewById(R.id.eat_name);
        }
    }
}

