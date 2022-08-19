package com.fusionkitchen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.fusionkitchen.R;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;

public class MenuSubcatnameAdapter extends RecyclerView.Adapter<MenuSubcatnameAdapter.ViewHolder> {


    private final menu_item_sub_model.categoryall listdatum;
    private menu_item_sub_model.categoryall.subcat[] items;
    private menu_item_sub_model.categoryall.subcat[] itemsubcatname;
    private Context mContext;
    private String  menuurlpath;


    // RecyclerView recyclerView;
    //  public SubcatnameAdapter(Context mContext, List<String> itemsubcatname, List<menu_item_sub_model.categoryall.subcat> items) {
    public MenuSubcatnameAdapter(Context mContext, List<menu_item_sub_model.categoryall.subcat> itemsubcatname, List<menu_item_sub_model.categoryall.subcat> items,  String menuurlpath, menu_item_sub_model.categoryall listdatum) {
        // public MenuSubcatnameAdapter(Context mContext, List<menu_item_sub_model.categoryall.subcat> itemsubcatname,  String ordertypevalue, String menuurlpath, menu_item_sub_model.categoryall listdatum) {
        this.items = items.toArray(new menu_item_sub_model.categoryall.subcat[0]);
        this.itemsubcatname = itemsubcatname.toArray(new menu_item_sub_model.categoryall.subcat[0]);

        this.mContext = mContext;
        this.menuurlpath = menuurlpath;

        this.listdatum = listdatum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_subcat_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.e("subcatvals1", "" + itemsubcatname[position].getName());
        Log.e("subcatvals2", "" + itemsubcatname[position].getDescription());



        if (!itemsubcatname[position].getName().equals("") || !itemsubcatname[position].getName().isEmpty() || !itemsubcatname[position].getName().equalsIgnoreCase("")) {
            holder.menu_item_subcat_name.setText(itemsubcatname[position].getName());
            holder.menu_item_subcat_desc.setText(itemsubcatname[position].getDescription());

        } else {
            holder.relativetextLL.setVisibility(View.GONE);
        }


       /* holder.menu_item_subcat_name.setText(itemsubcatname[position].getName());
        holder.menu_item_subcat_desc.setText(itemsubcatname[position].getDescription());*/

        List<menu_item_sub_model.categoryall.subcat.items> itemname = new ArrayList<>();


        for (int k = 0; k < itemsubcatname[position].getItems().size(); k++) {

            itemname.add(itemsubcatname[position].getItems().get(k));


        }


        MenuitemnameAdapter menuitemnameadapter = new MenuitemnameAdapter(mContext, itemname,  menuurlpath, itemsubcatname[position], listdatum);
        holder.child_item_recyclerview.setHasFixedSize(true);
        holder.child_item_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        holder.child_item_recyclerview.setAdapter(menuitemnameadapter);

        // Log.e("sixg", "" + orderhistory.size());

    }


    @Override
    public int getItemCount() {
        return itemsubcatname.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menu_item_subcat_name, menu_item_subcat_desc;

        public RecyclerView child_item_recyclerview;
        public RelativeLayout relativeLayout;

       public LinearLayout relativetextLL;

        public ViewHolder(View itemView) {
            super(itemView);
            this.menu_item_subcat_name = itemView.findViewById(R.id.menu_item_subcat_name);
            this.menu_item_subcat_desc = itemView.findViewById(R.id.menu_item_subcat_desc);
            this.child_item_recyclerview = itemView.findViewById(R.id.child_item_recyclerview);

            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
            this.relativetextLL = itemView.findViewById(R.id.relativetextLL);
        }
    }
}