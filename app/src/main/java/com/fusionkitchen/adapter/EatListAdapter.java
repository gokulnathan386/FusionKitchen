package com.fusionkitchen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fusionkitchen.R;
import com.fusionkitchen.model.EatListPostelModel;

import java.util.List;

public class EatListAdapter extends RecyclerView.Adapter<EatListAdapter.MyViewHolder> {
    List<EatListPostelModel> studentDataList;
    Context context;
    private int lastPosition = -1;
    public EatListAdapter(List<EatListPostelModel> studentDataList, Context context) {
        this.studentDataList=studentDataList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.eat_listview_design, viewGroup, false);

        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        EatListPostelModel data=studentDataList.get(i);
        viewHolder.eat_icon.setImageResource(data.image);
   //     viewHolder.eat_name.setText(String.valueOf(data.name));
        setAnimation(viewHolder.parent, i);

    }
    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return studentDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
       // TextView eat_name;
        ImageView eat_icon;
        LinearLayout parent;
        public MyViewHolder(View itemView) {
            super(itemView);
            parent=itemView.findViewById(R.id.parent);
            eat_icon=itemView.findViewById(R.id.eat_icon);
           // eat_name=itemView.findViewById(R.id.eat_name);
        }
    }
}
