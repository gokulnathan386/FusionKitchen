package com.fusionkitchen.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.activity.Order_Status_Activity;
import com.fusionkitchen.activity.Postcode_Activity;
import com.fusionkitchen.model.EatListPostelModel;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.fusionkitchen.model.orderstatus.orderstatus_model;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PopularRestaurantsListAdapter extends RecyclerView.Adapter<PopularRestaurantsListAdapter.MyViewHolder> {

    List<popular_restaurants_listmodel> popularlistmodule;
    Context context;
    private int lastPosition = -1;
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    Dialog dialog;
    private long mLastClickTime = 0;

    public PopularRestaurantsListAdapter(List<popular_restaurants_listmodel> popularlistmodule, Context context){
        this.popularlistmodule=popularlistmodule;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eatmostpopular_listview_design, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.Most_Popular_TextView_name.setText(popularlistmodule.get(position).getName());
        holder.Most_Popular_TextView_Area.setText(popularlistmodule.get(position).getArea());
        holder.Most_Popular_TextView_Rate.setText(popularlistmodule.get(position).getRating_average());
        holder.Most_Popular_TextView_TakeWay_status.setText(popularlistmodule.get(position).getTakeawaystatus());
        holder.Most_Popular_TextView_Offer.setText(popularlistmodule.get(position).getDiscount());


        Picasso.get()
                .load(popularlistmodule.get(position).getImage_url())
                .into(holder.Most_Popular_ImageView_Icon);


/*        holder.Most_Popular_Linear_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingshow();


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intent1 = new Intent("custom-message-menuurlpath");
                intent1.putExtra("menuurlpath",popularlistmodule.get(position).getmenupage_url());
                Log.d("menupath",popularlistmodule.get(position).getmenupage_url());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

                //Intent  intent   = new Intent(context, Item_Menu_Activity.class);

                sharedptcode = context.getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editorpostcode = sharedptcode.edit();
                editorpostcode.putString("KEY_area", popularlistmodule.get(position).getArea());
                editorpostcode.putString("KEY_postcode",popularlistmodule.get(position).getPost_code());
                editorpostcode.putString("KEY_address",popularlistmodule.get(position).getaddress_Location());
                editorpostcode.putString("KEY_posturl",popularlistmodule.get(position).getmenupage_url());
               // editorpostcode.putString("KEY_lat", response.body().getLocation().getLat());
               // editorpostcode.putString("KEY_lon", response.body().getLocation().getLng());
                editorpostcode.commit();


                hideloading();
              //  context.startActivity(intent);


            }
        });*/

    }

    public void loadingshow() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.custom_loading_layout);


        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(context)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(gifImageView);
        dialog.show();
    }

    public void hideloading() {
        dialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return popularlistmodule.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Most_Popular_TextView_name,Most_Popular_TextView_Area,Most_Popular_TextView_Offer;
        TextView Most_Popular_TextView_Rate,Most_Popular_TextView_TakeWay_status;
        ImageView Most_Popular_ImageView_Icon;
        LinearLayout Most_Popular_Linear_Layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            Most_Popular_TextView_name = (TextView) itemView.findViewById(R.id.Most_Popular_TextView_name);
            Most_Popular_TextView_Area = (TextView) itemView.findViewById(R.id.Most_Popular_TextView_Area);
            Most_Popular_TextView_Rate = (TextView) itemView.findViewById(R.id.Most_Popular_TextView_Rate);
            Most_Popular_TextView_TakeWay_status = (TextView) itemView.findViewById(R.id.Most_Popular_TextView_TakeWay_status);
            Most_Popular_TextView_Offer = (TextView) itemView.findViewById(R.id.Most_Popular_TextView_Offer);
            Most_Popular_ImageView_Icon = (ImageView) itemView.findViewById(R.id.Most_Popular_ImageView_Icon);
            Most_Popular_Linear_Layout = (LinearLayout)  itemView.findViewById(R.id.Most_Popular_Linear_Layout);

        }
    }
}
