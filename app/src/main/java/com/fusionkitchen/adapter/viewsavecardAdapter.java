package com.fusionkitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Show_Save_Card_Activity;
import com.fusionkitchen.model.Savecard.deletesavecard_model;
import com.fusionkitchen.model.Savecard.viewsavecard_details_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class viewsavecardAdapter extends RecyclerView.Adapter<viewsavecardAdapter.ViewHolder> {
    private viewsavecard_details_model.data[] listdata;

    private Context mContext;
    private long mLastClickTime = 0;
    private Dialog dialog;
    private RadioButton lastCheckedRB = null;
    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    String user_id;

    // RecyclerView recyclerView;
    public viewsavecardAdapter(Context mContext, List<viewsavecard_details_model.data> listdata) {
        this.listdata = listdata.toArray(new viewsavecard_details_model.data[0]);
        this.mContext = mContext;

    }

    @Override
    public viewsavecardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_checkout_save_card, parent, false);
        viewsavecardAdapter.ViewHolder viewHolder = new viewsavecardAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewsavecardAdapter.ViewHolder holder, int position) {
        final viewsavecard_details_model.data myListData = listdata[position];
        /*--------------Login details get SharedPreferences------------------*/
        slogin = mContext.getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        holder.card_number.setText("****  ****  ****  " + myListData.getCard_last4());
        holder.order_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = holder.offer_select;
                lastCheckedRB.setChecked(true);

                Intent intent = new Intent("Save_card_details");
                intent.putExtra("savecardmid", myListData.getCard_id());
                intent.putExtra("savecardnumbers", myListData.getCard_last4());
                intent.putExtra("savecardexpmonth", myListData.getCard_exp_month());
                intent.putExtra("savecardexpyear", myListData.getCard_exp_year());
                intent.putExtra("savecardholdername", myListData.getCard_holder());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }
        });
        holder.offer_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = holder.offer_select;
                lastCheckedRB.setChecked(true);

                Intent intent = new Intent("Save_card_details");
                intent.putExtra("savecardmid", myListData.getCard_id());
                intent.putExtra("savecardnumbers", myListData.getCard_last4());
                intent.putExtra("savecardexpmonth", myListData.getCard_exp_month());
                intent.putExtra("savecardexpyear", myListData.getCard_exp_year());
                intent.putExtra("savecardholdername", myListData.getCard_holder());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView card_number;
        public CardView order_show1;
        public RadioButton offer_select;

        public ViewHolder(View itemView) {
            super(itemView);

            this.card_number = itemView.findViewById(R.id.card_number);
            this.order_show1 = itemView.findViewById(R.id.order_show1);
            this.offer_select = itemView.findViewById(R.id.offer_select);
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