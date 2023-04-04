package com.fusionkitchen.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.model.addon.menu_addons_model;

import static android.text.Html.fromHtml;
import static com.fusionkitchen.activity.Item_Menu_Activity.MyPREFERENCES;

public class MenuAddonItemAdapter extends RecyclerView.Adapter<MenuAddonItemAdapter.ViewHolder> {
    private menu_addons_model.dataval.addonitemslist[] listdata;
    private Context mContext;

    private Dialog dialog;
    String arrayData = "";
    String arrayextraData = "";
    String arrayextranameData = "";
    ArrayList<String> arrayListUser = new ArrayList<>();
    ArrayList<String> arrayextraListUser = new ArrayList<>();
    ArrayList<String> arrayextraname = new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs_extra";
    SharedPreferences sharedpreferences;

    private long mLastClickTime = 0;

    // RecyclerView recyclerView;
    public MenuAddonItemAdapter(Context mContext, List<menu_addons_model.dataval.addonitemslist> listdata) {

        this.mContext = mContext;
        this.listdata = listdata.toArray(new menu_addons_model.dataval.addonitemslist[0]);

    }


    @Override
    public MenuAddonItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_addon_item_details, parent, false);
        MenuAddonItemAdapter.ViewHolder viewHolder = new MenuAddonItemAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MenuAddonItemAdapter.ViewHolder holder, int position) {



        final menu_addons_model.dataval.addonitemslist student = listdata[position];//new add

        holder.checkBox.setChecked(student.student);//new add



        holder.menu_addon_item_name.setText(fromHtml(listdata[position].getName().replaceAll("Â", ""), Html.FROM_HTML_MODE_COMPACT));
        holder.menu_addon_item_amout.setText(listdata[position].getPrice());

        Intent intent = new Intent("addon_btn-nextid");
        intent.putExtra("addonbtnnext", listdata[0].getBtnnext());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);





        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingshow();
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        hideloading();
                    }
                }.start();

                Log.e("getBtnnext2", "" + listdata[position].getBtnnext());
                if (holder.checkBox.isChecked()) {

                    sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);
                    holder.menu_addon_item_extra.setText(sharedpreferences.getString("addon_extra", null));
                    if (!arrayListUser.contains(listdata[position].getItemid()))
                        arrayListUser.add(listdata[position].getItemid());
                    arrayData = arrayListUser.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//addon item id call

                    arrayextraListUser.add(holder.menu_addon_item_extra.getText().toString());
                    arrayextraData = arrayextraListUser.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//addon item extrtra call

                    arrayextraname.add(listdata[position].getName());
                    arrayextranameData = arrayextraname.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//addon item extrtra call

                    sharedpreferences.edit().remove("addon_extra").commit();


                    Intent intent = new Intent("addon_extra-message");
                    intent.putExtra("addonitemid", arrayData);
                    intent.putExtra("addonitemtype", "0");
                    intent.putExtra("addonidprice", listdata[position].getItemprice());
                    // intent.putExtra("addonidposion", String.valueOf(position));
                    intent.putExtra("arrayextraData", arrayextraData);
                    intent.putExtra("addonbtnnext", listdata[position].getBtnnext());
                    intent.putExtra("arrayextranameData", listdata[position].getName());
                    intent.putExtra("arrayaddonitemid", listdata[position].getItemid());
                    intent.putExtra("arrayaddonextraidsingle", holder.menu_addon_item_extra.getText().toString());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                } else {

                    arrayextraListUser.remove(holder.menu_addon_item_extra.getText().toString());
                    arrayextraData = arrayextraListUser.toString().replace("[", "").replace("]", "").replace(" ", "").trim();
                    Log.e("arrayextraData", "" + arrayextraData);


                    arrayListUser.remove(listdata[position].getItemid());
                    arrayData = arrayListUser.toString().replace("[", "").replace("]", "").replace(" ", "").trim();

                    Log.e("okkk2", "" + arrayData);
                    arrayextraname.remove(listdata[position].getName());
                    arrayextranameData = arrayextraname.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//addon item extrtra call

                    Intent intent = new Intent("addon_extra-message");
                    intent.putExtra("addonitemid", arrayData);
                    intent.putExtra("addonitemtype", "1");
                    intent.putExtra("addonidprice", listdata[position].getItemprice());
                    // intent.putExtra("addonidposion", String.valueOf(position));
                    intent.putExtra("arrayextraData", arrayextraData);
                    intent.putExtra("addonbtnnext", listdata[position].getBtnnext());
                    intent.putExtra("arrayextranameData", listdata[position].getName());
                    intent.putExtra("arrayaddonitemid", listdata[position].getItemid());
                    intent.putExtra("arrayaddonextraidsingle", holder.menu_addon_item_extra.getText().toString());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
                student.student = holder.checkBox.isChecked();//new add

                Log.e("checkboox", "" + holder.checkBox.isChecked());

            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menu_addon_item_name, menu_addon_item_amout, menu_addon_item_extra;
        protected CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            //this.report_details_address = (TextView) itemView.findViewById(R.id.report_details_address);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.cb);
            this.menu_addon_item_name = itemView.findViewById(R.id.menu_addon_item_name);
            this.menu_addon_item_amout = itemView.findViewById(R.id.menu_addon_item_amout);
            this.menu_addon_item_extra = itemView.findViewById(R.id.menu_addon_item_extra);
        }
    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);

        LottieAnimationView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        gifImageView.setAnimation(R.raw.newloader);
        gifImageView.playAnimation();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void hideloading() {
        dialog.dismiss();
    }


}
