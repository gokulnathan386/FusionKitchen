package com.fusionkitchen.adapter;


import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.R;
import com.fusionkitchen.model.ExamData;
import com.fusionkitchen.model.moreinfo.about_us_model;


public class MoreinfoopenhrsAdapter extends RecyclerView.Adapter<MoreinfoopenhrsAdapter.ViewHolder> {

    private Context mContext;

    private about_us_model.aboutdetails.openinghours[] listdata;
    private HashMap<ArrayList<String>,String> listdatahash;
    private List<ExamData> examdata;
    public int[] mColors = {R.drawable.cell_shape_list, R.drawable.cell_shape, R.drawable.cell_shape_list, R.drawable.cell_shape, R.drawable.cell_shape_list, R.drawable.cell_shape,R.drawable.cell_shape_list};//int or string

    // RecyclerView recyclerView;
    public MoreinfoopenhrsAdapter(Context mContext, List<about_us_model.aboutdetails.openinghours> listdata, HashMap<ArrayList<String>, String> stronggg, List<ExamData> list) {
        this.mContext = mContext;
        this.listdata = listdata.toArray(new about_us_model.aboutdetails.openinghours[0]);
        this.listdatahash= stronggg;
        this.examdata= list;
    }



    @Override
    public MoreinfoopenhrsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_open_time, parent, false);
        MoreinfoopenhrsAdapter.ViewHolder viewHolder = new MoreinfoopenhrsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoreinfoopenhrsAdapter.ViewHolder holder, int position) {
//        holder.day.setBackgroundResource((mColors[position % 20]));
        //      holder.time.setBackgroundResource((mColors[position % 20]));





        Log.d("dcbgayhaaaagdcb",""+examdata.get(position).getDate());


        if (examdata.get(position).getDate()!="null" && examdata.get(position).getName().toString()!="[]"){
            // holder.day.setText(examdata.get(position).getName().toString());
            holder.time.setText(examdata.get(position).getDate());
            holder.day.setText(examdata.get(position).getName().toString());

            examdata.get(position).getDate();

        }else{

            holder.time.setVisibility(View.GONE);
            holder.day.setVisibility(View.GONE);
        }




    }

    @Override
    public int getItemCount() {
        return examdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day, time;
        public ViewHolder(View itemView) {
            super(itemView);
            this.day = (TextView) itemView.findViewById(R.id.day);
            this.time = (TextView) itemView.findViewById(R.id.time);

        }
    }
}



/*

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.model.moreinfo.about_us_model;


public class MoreinfoopenhrsAdapter extends RecyclerView.Adapter<MoreinfoopenhrsAdapter.ViewHolder> {

    private Context mContext;

    private about_us_model.aboutdetails.openinghours[] listdata;
   // private HashMap<ArrayList<String>, String> listdata;
    //.public int[] mColors = {R.drawable.cell_shape_list, R.drawable.cell_shape, R.drawable.cell_shape_list, R.drawable.cell_shape, R.drawable.cell_shape_list, R.drawable.cell_shape,R.drawable.cell_shape_list};//int or string

    // RecyclerView recyclerView;
    public MoreinfoopenhrsAdapter(Context mContext, List<about_us_model.aboutdetails.openinghours> listdata) {
        this.mContext = mContext;
        this.listdata = listdata.toArray(new about_us_model.aboutdetails.openinghours[0]);
      //  this.listdata = listdata;
    }

    @Override
    public MoreinfoopenhrsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_open_time, parent, false);
        MoreinfoopenhrsAdapter.ViewHolder viewHolder = new MoreinfoopenhrsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoreinfoopenhrsAdapter.ViewHolder holder, int position) {
       // holder.day.setBackgroundResource((mColors[position % 20]));
      //  holder.time.setBackgroundResource((mColors[position % 20]));
        holder.day.setText(listdata[position].getDay());
        holder.time.setText(listdata[position].getTime());





    }

    @Override
    public int getItemCount() {

        return listdata.length;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView day, time;
        public ViewHolder(View itemView) {
            super(itemView);
            this.day = (TextView) itemView.findViewById(R.id.day);
            this.time = (TextView) itemView.findViewById(R.id.time);

        }
    }
}*/
