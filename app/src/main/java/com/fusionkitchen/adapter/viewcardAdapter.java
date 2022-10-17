package com.fusionkitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Order_Status_Activity;
import com.fusionkitchen.activity.Show_Save_Card_Activity;
import com.fusionkitchen.model.Savecard.deletesavecard_model;
import com.fusionkitchen.model.Savecard.viewsavecard_details_model;
import com.fusionkitchen.model.favorite.insertfavorite_mode;
import com.fusionkitchen.model.offer.offer_list_model_details;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class viewcardAdapter extends RecyclerView.Adapter<viewcardAdapter.ViewHolder> {
    private viewsavecard_details_model.data[] listdata;

    private Context mContext;
    private long mLastClickTime = 0;
    private Dialog dialog;

    /*--------------Login store SharedPreferences------------------*/
    SharedPreferences slogin;
    String user_id;


    // RecyclerView recyclerView;
    public viewcardAdapter(Context mContext, List<viewsavecard_details_model.data> listdata) {
        this.listdata = listdata.toArray(new viewsavecard_details_model.data[0]);
        this.mContext = mContext;

    }

    @Override
    public viewcardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_show_save_card, parent, false);
        viewcardAdapter.ViewHolder viewHolder = new viewcardAdapter.ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(viewcardAdapter.ViewHolder holder, int position) {

        final viewsavecard_details_model.data myListData = listdata[position];


        /*--------------Login details get SharedPreferences------------------*/
        slogin = mContext.getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));


        holder.card_number.setText("XXXX  XXXX  XXXX  " + myListData.getCard_last4());
        holder.exp_date.setText(myListData.getCard_exp_month() + " / " + myListData.getCard_exp_year());


        Picasso.get()
                .load(myListData.getCard_image())
                .placeholder(R.drawable.headerplaceholder)
                .error(R.drawable.headerplaceholder)
                .into(holder.card_logo);

        holder.delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View popupView = LayoutInflater.from(mContext).inflate(R.layout.dialog_show_card, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                AppCompatButton discard_card = popupView.findViewById(R.id.discard_card);
                AppCompatButton delete_card = popupView.findViewById(R.id.delete_card);
                ImageView close_img = popupView.findViewById(R.id.close_img);

                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                discard_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                delete_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        loadingshow();

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("cid", user_id);
                        params.put("pmid", myListData.getCard_id());

                        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                        Call<deletesavecard_model> call = apiService.deletesavecard(params);

                        Log.e("fav_params", "" + params);

                        call.enqueue(new Callback<deletesavecard_model>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(Call<deletesavecard_model> call, Response<deletesavecard_model> response) {

                                int statusCode = response.code();
                                Log.e("fav_statusCode", "" + statusCode);

                                if (statusCode == 200) {
                                    hideloading();
                                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                                        Intent intent = new Intent(mContext, Show_Save_Card_Activity.class);
                                        mContext.startActivity(intent);

                                    } else {
                                        Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    hideloading();
                                    Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<deletesavecard_model> call, Throwable t) {
                                hideloading();
                                Log.e("menuThrowable", "" + t);
                                Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                });

                popupWindow.showAsDropDown(popupView, 0, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView card_logo, delete_card;
        public TextView card_number, exp_date;

        public ViewHolder(View itemView) {
            super(itemView);
            this.card_logo = itemView.findViewById(R.id.card_logo);
            this.delete_card = itemView.findViewById(R.id.delete_card);
            this.card_number = itemView.findViewById(R.id.card_number);
            this.exp_date = itemView.findViewById(R.id.exp_date);
        }
    }


    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loading_layout);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        // GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(mContext)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideloading() {
        dialog.dismiss();
    }
}