package com.fusionkitchen.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.model.DashboardListViewModel;
import com.fusionkitchen.model.DashboardMostPopularModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DashboardMostPopularAdapter  extends RecyclerView.Adapter<DashboardMostPopularAdapter.MyViewHolder>{

    List<DashboardMostPopularModel> dashboardMostPopularModel;
    Context context;
    SharedPreferences sharedptcode;
    public static final String MyPOSTCODEPREFERENCES = "MyPostcodePrefs_extra";
    Dialog dialog;
    private long mLastClickTime = 0;

    private int lastPosition = -1;
    public DashboardMostPopularAdapter(List<DashboardMostPopularModel> dashboardMostPopularModel, Context context) {
        this.dashboardMostPopularModel=dashboardMostPopularModel;
        this.context=context;
    }
    @NonNull
    @Override
    public DashboardMostPopularAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dashboard_mostpopular_design, viewGroup, false);

        return new DashboardMostPopularAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(DashboardMostPopularAdapter.MyViewHolder viewHolder, int i) {
        DashboardMostPopularModel data = dashboardMostPopularModel.get(i);

       viewHolder.Enter_your_offer_TextView.setText(dashboardMostPopularModel.get(i).getDiscount());
       viewHolder.Enter_your_Name_TextView.setText(dashboardMostPopularModel.get(i).getName());
       viewHolder.Enter_your_Rating.setText(dashboardMostPopularModel.get(i).getRating_average());
       viewHolder.Enter_Your_Area_textView.setText(dashboardMostPopularModel.get(i).getArea());
       viewHolder.Enter_your_PerOrder_textView.setText(dashboardMostPopularModel.get(i).getTakeawaystatus());

        Picasso.get()
                .load(dashboardMostPopularModel.get(i).getImage_url())
                .into(viewHolder.Most_Popular_ImageView_Icon);


       /* viewHolder.linear_layout_listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingshow();


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intent1 = new Intent("custom-message-menuurlpath");
                intent1.putExtra("menuurlpath", dashboardMostPopularModel.get(i).getmenupage_url());
                Log.d("menupath",dashboardMostPopularModel.get(i).getmenupage_url());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);




               // Intent intent   = new Intent(context, Item_Menu_Activity.class);
                sharedptcode = context.getSharedPreferences(MyPOSTCODEPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editorpostcode = sharedptcode.edit();
                editorpostcode.putString("KEY_area", dashboardMostPopularModel.get(i).getArea());
                editorpostcode.putString("KEY_postcode",dashboardMostPopularModel.get(i).getPost_code());
                editorpostcode.putString("KEY_address",dashboardMostPopularModel.get(i).getaddress_Location());
                editorpostcode.commit();
             //   intent.putExtra("menuurlpath",dashboardMostPopularModel.get(i).getmenupage_url());
                hideloading();
             //   context.startActivity(intent);
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
        return dashboardMostPopularModel.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
          TextView Enter_your_offer_TextView,Enter_your_Name_TextView;
          TextView Enter_your_Rating,Enter_Your_Area_textView;
          ImageView Most_Popular_ImageView_Icon;
          TextView Enter_your_PerOrder_textView;
          LinearLayout linear_layout_listview;
        public MyViewHolder(View itemView) {
            super(itemView);

            Most_Popular_ImageView_Icon = itemView.findViewById(R.id.Most_Popular_ImageView_Icon);
            Enter_your_offer_TextView  = itemView.findViewById(R.id.Enter_your_offer_TextView);
            Enter_your_Name_TextView = itemView.findViewById(R.id.Enter_your_Name_TextView);
            Enter_your_Rating = itemView.findViewById(R.id.Enter_your_Rating);
            Enter_Your_Area_textView = itemView.findViewById(R.id.Enter_Your_Area_textView);
            Enter_your_PerOrder_textView = itemView.findViewById(R.id.Enter_your_PerOrder_textView);
            linear_layout_listview = itemView.findViewById(R.id.linear_layout_listview);

        }
    }


}
