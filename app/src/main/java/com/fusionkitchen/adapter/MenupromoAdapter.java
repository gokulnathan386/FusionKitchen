package com.fusionkitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.model.offer.offer_list_model_details;

public class MenupromoAdapter extends RecyclerView.Adapter<MenupromoAdapter.ViewHolder> {
    private offer_list_model_details.discountcode[] listdata;
    private offer_list_model_details.promocode[] promocode;

    private Context mContext;
    Dialog offer_popup,Coupen_popup;
   // public int[] mColors = {R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two, R.drawable.menu_offer_one, R.drawable.menu_offer_two};//int or string

    // RecyclerView recyclerView;
    public MenupromoAdapter(Context mContext, List<offer_list_model_details.discountcode> listdata, List<offer_list_model_details.promocode> promocode) {
        this.listdata = listdata.toArray(new offer_list_model_details.discountcode[0]);
        this.promocode = promocode.toArray(new offer_list_model_details.promocode[0]);
        this.mContext = mContext;

    }

    @Override
    public MenupromoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_menu_offer_details, parent, false);
        MenupromoAdapter.ViewHolder viewHolder = new MenupromoAdapter.ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MenupromoAdapter.ViewHolder holder, int position) {

        final offer_list_model_details.promocode myListData = promocode[position];

    //    holder.relativeLayout.setBackgroundResource((mColors[position % 20]));

        if (promocode[position].getType().equalsIgnoreCase("0")) {
            holder.offer_title.setText("GET " + promocode[position].getDiscount() + " % OFF");
            holder.offer_decs.setText("");
        } else {
            holder.offer_title.setText("GET £ " + promocode[position].getDiscount() + " OFF");
            holder.offer_decs.setText("");
        }

/*
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent("custom-message-promooffer");
                intent.putExtra("promocode", promocode[position].getFree());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


            }
        });*/


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                offer_popup  = new Dialog(view.getContext());
                offer_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                offer_popup.setContentView(R.layout.offer_popup_design);

                TextView details_offers_show = offer_popup.findViewById(R.id.details_offers_show);
                TextView TextView_Offer  = offer_popup.findViewById(R.id.TextView_Offer);
                LinearLayout description = offer_popup.findViewById(R.id.description);
                //  menu_offer_details_view.setVisibility(View.VISIBLE);
                //confirm_code.setText(promocode[position].getFree());

                TextView view_offer_details  = offer_popup.findViewById(R.id.view_offer_details);


                AppCompatButton offer_promo_btn = offer_popup.findViewById(R.id.offer_promo_btn);
                offer_promo_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        offer_popup.dismiss();

                        Coupen_popup = new Dialog(mContext);
                        Coupen_popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Coupen_popup.setContentView(R.layout.coupon_popup_design);
                        ImageView imageView = Coupen_popup.findViewById(R.id.imageview);
                        TextView   add_more_button_textview = Coupen_popup.findViewById(R.id.add_more_button_textview);
                        Glide.with(mContext).load(R.drawable.offer_gif).into(imageView);

                        add_more_button_textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Coupen_popup.dismiss();
                            }
                        });

                        Coupen_popup.show();
                        Coupen_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        Coupen_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Coupen_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        Coupen_popup.getWindow().setGravity(Gravity.BOTTOM);


                    }
                });


                view_offer_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (description.getVisibility() == View.GONE) {
                            description.setVisibility(View.VISIBLE);
                            view_offer_details.setText("Hide Details");
                        } else {
                            description.setVisibility(View.GONE);
                            view_offer_details.setText("View Details");
                        }
                    }
                });

                TextView_Offer.setText(promocode[position].getDiscount()+"% OFF");
                details_offers_show.setText("Promo Code applicable on all orders." + "\n"
                        + "Offer will be applicable on your first order only." + "\n"
                        + "Offer is valid only for this particular restaurant/takeaway." + "\n"
                        + "Apply the promo code at the checkout." + "\n"
                        + "Other T & C may also apply.");

               /* confirm_copy_button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                menu_offer_details_view.setVisibility(View.GONE);
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", promocode);
                                clipboard.setPrimaryClip(clip);
                            }
                        });*/

                offer_popup.show();
                offer_popup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                offer_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                offer_popup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                offer_popup.getWindow().setGravity(Gravity.BOTTOM);

            }
        });


    }


    @Override
    public int getItemCount() {
        return promocode.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView offer_title;
        public TextView offer_decs;
        public LinearLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.offer_title = (TextView) itemView.findViewById(R.id.offer_title);
            this.offer_decs = (TextView) itemView.findViewById(R.id.offer_decs);
            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}