package com.fusionkitchen.adapter;


import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;


import com.fusionkitchen.activity.Add_to_Cart;
import com.fusionkitchen.activity.Dashboard_Activity;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.cart.Cartitem;
import com.fusionkitchen.model.cart.subcategory_printer_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.material.internal.ContextUtils.getActivity;

public class CartitemlistAdapter extends RecyclerView.Adapter<CartitemlistAdapter.ViewHolder> {
    private static Context context;
    private ArrayList<Cartitem> listContacts;
    private ArrayList<Cartitem> mArrayList;
    private SQLDBHelper mDatabase;
    int count, totalcount;
    private String menuurlpath;
    String subcatfullurl, subcategory_printers, fullUrladdon;
    double num1, num2, num3, num4, num5, num6, sum, smines, finsum;
    private String but_order_type, butordertype;
    // String completePath = Item_Menu_Activity.class.getResource(app.actNum).toString();
    private long mLastClickTime = 0;
    PopupWindow popupWindow;
    SharedPreferences sharedpreferences;
    String MyPREFERENCES = "MyPrefs_extra";
    SharedPreferences sharedpreferences1;
    String PREFS_MOREINFO = "PREFS_MOREINFO";


    public CartitemlistAdapter(Context context, ArrayList<Cartitem> listContacts, String menuurlpath, String subcategory_printers, String but_order_type) {
        this.context = context;
        this.listContacts = listContacts;
        this.mArrayList = listContacts;
        this.menuurlpath = menuurlpath;
        mDatabase = new SQLDBHelper(context);
        this.subcategory_printers = subcategory_printers;
        this.but_order_type = but_order_type;

        sharedpreferences =context.getSharedPreferences(MyPREFERENCES, context.MODE_PRIVATE);
        sharedpreferences1 = context.getSharedPreferences(PREFS_MOREINFO, Context.MODE_PRIVATE);
      /*  sharedpreferences.getString("orderactivetag", null);
        sharedpreferences.getString("ordertodattime", null);
        sharedpreferences.getString("orderlaterdate", null);
        sharedpreferences.getString("orderlatertime", null);*/


        if (but_order_type == null) {
            this.butordertype = "0";
        } else {
            this.butordertype = but_order_type;
        }
    }

    @Override
    public CartitemlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_card_items_list, parent, false);
        CartitemlistAdapter.ViewHolder viewHolder = new CartitemlistAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartitemlistAdapter.ViewHolder holder, int position) {
        final Cartitem contacts = listContacts.get(position);




        if (subcategory_printers.equalsIgnoreCase("enable")) {
            if (contacts.getCategoryname().equalsIgnoreCase(contacts.getSubcategoryname())) {
                holder.tvName.setText(contacts.getName());
            } else {
                holder.tvName.setText(contacts.getSubcategoryname() + " - " + contacts.getName());
            }
        } else {
            holder.tvName.setText(contacts.getName());
        }

        holder.tvitem_addon_name.setText(contacts.getDesc());
        holder.tvinteger_number.setText(contacts.getQty());
        holder.tvitem_total.setText(contacts.getFinalamt());

    /*    int totalPrice = 0;
        for (int i = 0; i<listContacts.size(); i++)
        {
            totalPrice += listContacts.get(i).getAmount();
        }*/


        if (holder.tvinteger_number.getText().toString().equalsIgnoreCase("1")) {
            // holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.delete_item));
            holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
            holder.delete_All_Item.setVisibility(View.GONE);
        } else {
            holder.delete_All_Item.setVisibility(View.VISIBLE);
            holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
        }


        holder.delete_All_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("1")){
                    SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                    editor_extra.putString("pop_up_show","2");
                    editor_extra.commit();
                }else if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("3")){
                  SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                  editor_extra.putString("pop_up_show","2");
                  editor_extra.commit();
              }

                SharedPreferences.Editor editor = sharedpreferences1.edit();
                editor.putString("More_info", "MoreInfo");
                editor.commit();
                Intent intent = new Intent("delete-itemid");
                intent.putExtra("ItemId", contacts.getId());
                intent.putExtra("menuurlpath", menuurlpath);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        holder.tvdecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count = Integer.parseInt(holder.tvinteger_number.getText().toString());
                if (count > 0) {
                    count--;
                    holder.tvinteger_number.setText(String.valueOf(count));
                }
                if (count == 1) {
                    holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
                    holder.delete_All_Item.setVisibility(View.GONE);
                } else {
                    holder.delete_All_Item.setVisibility(View.VISIBLE);
                    holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
                }
                if (count == 0) {

                    if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("1")){
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("pop_up_show","2");
                        editor_extra.commit();
                    }else if(sharedpreferences.getString("pop_up_show", null).equalsIgnoreCase("3")){
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("pop_up_show","2");
                        editor_extra.commit();
                    }

                    SharedPreferences.Editor editor = sharedpreferences1.edit();
                    editor.putString("More_info", "MoreInfo");
                    editor.commit();
                    Intent intent = new Intent("delete-itemid");
                    intent.putExtra("ItemId", contacts.getId());
                    intent.putExtra("menuurlpath", menuurlpath);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    //Toast.makeText(context, "Item Deleted", Toast.LENGTH_LONG).show();
                } else {
                    num3 = Double.parseDouble(holder.tvitem_total.getText().toString());
                    num4 = Double.parseDouble(contacts.getAmount());
                    smines = num3 - num4;
                    holder.tvitem_total.setText(String.format("%.2f", smines));
                    Log.e("num3", "" + num3);
                    Log.e("num4", "" + num4);
                    Intent intent = new Intent("update_itemqty");
                    intent.putExtra("ItemId", contacts.getId());
                    intent.putExtra("Itemqty", holder.tvinteger_number.getText().toString());
                    intent.putExtra("Itemamount", holder.tvitem_total.getText().toString());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    totalcount();
                }
            }
        });
        holder.tvincrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



               /* if (count == 1) {
                    // holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.delete_item));
                    holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
                } else {
                    //  holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.minus_item));
                    holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
                }

                count = Integer.parseInt(holder.tvinteger_number.getText().toString());
                count++;
                holder.tvinteger_number.setText(String.valueOf(count));
                num1 = Double.parseDouble(holder.tvitem_total.getText().toString());
                num2 = Double.parseDouble(contacts.getAmount());
                sum = num1 + num2;
                holder.tvitem_total.setText(String.format("%.2f", sum));
                Log.e("num1", "" + num1);
                Log.e("num2", "" + num2);

                Intent intent = new Intent("update_itemqty");
                intent.putExtra("ItemId", contacts.getId());
                intent.putExtra("Itemqty", holder.tvinteger_number.getText().toString());
                intent.putExtra("Itemamount", holder.tvitem_total.getText().toString());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                totalcount();*/
                Log.e("contactsdec", "" + contacts.getDesc());
                if (!contacts.getDesc().isEmpty()) {
                    // Toast.makeText(context, "On Click", Toast.LENGTH_LONG).show();
                    View popupViewrepet = LayoutInflater.from(view.getContext()).inflate(R.layout.item_repeat_popup, null);
                    popupWindow = new PopupWindow(popupViewrepet, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                    AppCompatButton repet_last = popupViewrepet.findViewById(R.id.repet_last);
                    AppCompatButton add_new = popupViewrepet.findViewById(R.id.add_new);

                    TextView item_names = popupViewrepet.findViewById(R.id.item_names);
                    TextView item_cusom = popupViewrepet.findViewById(R.id.item_cusom);

                    ImageView popup_close = popupViewrepet.findViewById(R.id.popup_close);
                    item_names.setText(contacts.getName());
                    item_cusom.setText(contacts.getDesc());

                    Log.e("cusionitem", "" + item_cusom.getText().toString());

                    popup_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    repet_last.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                         Log.d("CartitemlistAdapter",sharedpreferences.getString("orderactivetag", null));
                         Log.d("CartitemlistAdapter",sharedpreferences.getString("ordertodattime", null));
                         Log.d("CartitemlistAdapter", sharedpreferences.getString("orderlaterdate", null));
                         Log.d("CartitemlistAdapter",sharedpreferences.getString("orderlatertime", null));

                            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();

                            Map<String, String> params = new HashMap<String, String>();
                           /* params.put("id", contacts.getItemid());
                            params.put("ordermode", butordertype);*/



                            if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {

                                params.put("time", "");
                                params.put("id", contacts.getItemid());
                                params.put("ordermode", butordertype);
                                params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                                params.put("dates", sharedpreferences.getString("orderlaterdate", null));

                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {

                                params.put("time", sharedpreferences.getString("ordertodattime", null));
                                params.put("id", contacts.getItemid());
                                params.put("ordermode", butordertype);
                                params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                                params.put("dates", "");
                                params.put("date_string",sharedpreferences.getString("todaytimestring", null));


                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
                                params.put("time",sharedpreferences.getString("orderlatertime", null));
                                params.put("id", contacts.getItemid());
                                params.put("ordermode", butordertype);
                                params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                                params.put("dates", sharedpreferences.getString("orderlaterdate", null));
                                params.put("date_string", sharedpreferences.getString("latertimestring", null));

                            }



                       /*
                            params.put("time", sharedpreferences.getString("ordertodattime", null) + sharedpreferences.getString("orderlatertime", null));
                            params.put("id", contacts.getItemid());
                            params.put("ordermode", butordertype);
                            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                            params.put("dates", sharedpreferences.getString("orderlaterdate", null));
                            params.put("date_string",sharedpreferences.getString("todaytimestring", null) + sharedpreferences.getString("latertimestring", null));
*/
                            fullUrladdon = menuurlpath + "/menu" + "/itemadd";

                            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                            Call<menu_addon_status_model> call = apiService.menuaddon(fullUrladdon, params);

                            Log.e("addtocartparams4", ": " + params);
                            Log.e("fullUrladdon", ": " + fullUrladdon);

                            call.enqueue(new Callback<menu_addon_status_model>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onResponse(Call<menu_addon_status_model> call, Response<menu_addon_status_model> response) {
                                    //response.headers().get("Set-Cookie");
                                   // Log.d("gokulnathan", String.valueOf(response));
                                    int statusCode = response.code();
                                    if (statusCode == 200) {
                                        Log.e("statusfor1", ": " + response.body().getStatus());
                                        Log.e("statusfor2", ": " + response.body().getError_code());

                                        if (response.body().getError_code().equalsIgnoreCase("300")) {
                                            popupWindow.dismiss();

                                            View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.addon_popup, null);
                                            final PopupWindow popupWindowreatlast = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                                            AppCompatButton browse = popupView.findViewById(R.id.browse);
                                            AppCompatButton update = popupView.findViewById(R.id.update);

                                            TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);
                                            TextView takaway_status = popupView.findViewById(R.id.takaway_status);

                                            //ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);

                                            takaway_status.setText("Takeaway Closed");
                                            takaway_status_dec.setText(response.body().getError_message().getTakeawayclosed().getCurrentlytakeaway());

                                            update.setText("Cancel");
                                            browse.setText("Browse");
                                            update.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    popupWindowreatlast.dismiss();
                                                }
                                            });
                                            browse.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (browse.getText().toString().equalsIgnoreCase("Cancel")) {
                                                        popupWindowreatlast.dismiss();
                                                    } else {
                                                        popupWindowreatlast.dismiss();
                                                        Intent intent = new Intent(context, Dashboard_Activity.class);
                                                        context.startActivity(intent);
                                                    }

                                                }
                                            });

                                            popupWindowreatlast.showAsDropDown(popupView, 0, 0);

                                        } else if (response.body().getError_code().equalsIgnoreCase("101")) {
                                            popupWindow.dismiss();

                                            View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.addon_popup, null);
                                            final PopupWindow popupWindowreatlast = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                                            AppCompatButton browse = popupView.findViewById(R.id.browse);
                                            AppCompatButton update = popupView.findViewById(R.id.update);

                                            TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);
                                            TextView takaway_status = popupView.findViewById(R.id.takaway_status);

                                            //ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);

                                            takaway_status.setText(response.body().getError_message().getChooseMode().getPopupdelivery());
                                            takaway_status_dec.setText(response.body().getError_message().getChooseMode().getOnlycollection());


                                            update.setText("Collection");
                                            browse.setText("Cancel");
                                            update.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    popupWindowreatlast.dismiss();


                                                    //add con
                                                    /*------------------repead last-----------------------*/
                                                    popupWindow.dismiss();
                                                    if (count == 1) {
                                                        // holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.delete_item));
                                                        holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
                                                        holder.delete_All_Item.setVisibility(View.GONE);
                                                    } else {
                                                        holder.delete_All_Item.setVisibility(View.VISIBLE);
                                                          holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
                                                    }



                                                    count = Integer.parseInt(holder.tvinteger_number.getText().toString());
                                                    count++;
                                                    holder.tvinteger_number.setText(String.valueOf(count));

                                                    num1 = Double.parseDouble(holder.tvitem_total.getText().toString());
                                                    num2 = Double.parseDouble(contacts.getAmount());
                                                    sum = num1 + num2;
                                                    holder.tvitem_total.setText(String.format("%.2f", sum));
                                                    Log.e("num1", "" + num1);
                                                    Log.e("num2", "" + num2);

                                                    Intent intent = new Intent("update_itemqty");
                                                    intent.putExtra("ItemId", contacts.getId());
                                                    intent.putExtra("Itemqty", holder.tvinteger_number.getText().toString());
                                                    intent.putExtra("Itemamount", holder.tvitem_total.getText().toString());
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                                                    totalcount();
                                                    Intent intentincres = new Intent("collection_only_repatlast");
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentincres);

                                                }
                                            });


                                            browse.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (browse.getText().toString().equalsIgnoreCase("Cancel")) {
                                                        popupWindowreatlast.dismiss();
                                                    } else {
                                                        popupWindowreatlast.dismiss();
                                                        Intent intent = new Intent(context, Dashboard_Activity.class);
                                                        context.startActivity(intent);
                                                    }

                                                }
                                            });

                                            popupWindowreatlast.showAsDropDown(popupView, 0, 0);


                                        } else if (response.body().getError_code().equalsIgnoreCase("200")) {
                                            popupWindow.dismiss();


                                            View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.addon_popup, null);
                                            final PopupWindow popupWindowreatlast = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                                            AppCompatButton browse = popupView.findViewById(R.id.browse);
                                            AppCompatButton update = popupView.findViewById(R.id.update);

                                            TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);
                                            TextView takaway_status = popupView.findViewById(R.id.takaway_status);

                                            //ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);

                                            takaway_status.setText("Delivery Closed");
                                            takaway_status_dec.setText(response.body().getError_message().getMsg());


                                            update.setText("Collection");
                                            browse.setText("Cancel");
                                            update.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    popupWindowreatlast.dismiss();

                                                    //add con

                                                    /*------------------repead last-----------------------*/
                                                    popupWindow.dismiss();
                                                    if (count == 1) {
                                                        // holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.delete_item));
                                                        holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
                                                        holder.delete_All_Item.setVisibility(View.GONE);
                                                    } else {
                                                        holder.delete_All_Item.setVisibility(View.VISIBLE);
                                                        holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
                                                    }



                                                    count = Integer.parseInt(holder.tvinteger_number.getText().toString());
                                                    count++;
                                                    holder.tvinteger_number.setText(String.valueOf(count));

                                                    num1 = Double.parseDouble(holder.tvitem_total.getText().toString());
                                                    num2 = Double.parseDouble(contacts.getAmount());
                                                    sum = num1 + num2;
                                                    holder.tvitem_total.setText(String.format("%.2f", sum));
                                                    Log.e("num1", "" + num1);
                                                    Log.e("num2", "" + num2);

                                                    Intent intent = new Intent("update_itemqty");
                                                    intent.putExtra("ItemId", contacts.getId());
                                                    intent.putExtra("Itemqty", holder.tvinteger_number.getText().toString());
                                                    intent.putExtra("Itemamount", holder.tvitem_total.getText().toString());
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                                                    totalcount();

                                                    Intent intentincres = new Intent("collection_only_repatlast");
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentincres);

                                                }
                                            });


                                            browse.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (browse.getText().toString().equalsIgnoreCase("Cancel")) {
                                                        popupWindowreatlast.dismiss();
                                                    } else {
                                                        popupWindowreatlast.dismiss();
                                                        Intent intent = new Intent(context, Dashboard_Activity.class);
                                                        context.startActivity(intent);
                                                    }

                                                }
                                            });

                                            popupWindowreatlast.showAsDropDown(popupView, 0, 0);


                                        } else if (response.body().getError_code().equalsIgnoreCase("201")) {
                                            popupWindow.dismiss();


                                            View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.addon_popup, null);
                                            final PopupWindow popupWindowreatlast = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                                            AppCompatButton browse = popupView.findViewById(R.id.browse);
                                            AppCompatButton update = popupView.findViewById(R.id.update);

                                            TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);
                                            TextView takaway_status = popupView.findViewById(R.id.takaway_status);

                                            //ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);

                                            takaway_status.setText("Delivery Closed");
                                            takaway_status_dec.setText(response.body().getError_message().getMsg());

                                            update.setText("Collection");
                                            browse.setText("Cancel");
                                            update.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    popupWindowreatlast.dismiss();
                                                    //add con
                                                    /*------------------repead last-----------------------*/
                                                    popupWindow.dismiss();
                                                    if (count == 1) {
                                                        // holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.delete_item));
                                                        holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
                                                        holder.delete_All_Item.setVisibility(View.GONE);
                                                    } else {
                                                        holder.delete_All_Item.setVisibility(View.VISIBLE);
                                                        holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
                                                    }

                                                    count = Integer.parseInt(holder.tvinteger_number.getText().toString());
                                                    count++;
                                                    holder.tvinteger_number.setText(String.valueOf(count));

                                                    num1 = Double.parseDouble(holder.tvitem_total.getText().toString());
                                                    num2 = Double.parseDouble(contacts.getAmount());
                                                    sum = num1 + num2;
                                                    holder.tvitem_total.setText(String.format("%.2f", sum));
                                                    Log.e("num1", "" + num1);
                                                    Log.e("num2", "" + num2);

                                                    Intent intent = new Intent("update_itemqty");
                                                    intent.putExtra("ItemId", contacts.getId());
                                                    intent.putExtra("Itemqty", holder.tvinteger_number.getText().toString());
                                                    intent.putExtra("Itemamount", holder.tvitem_total.getText().toString());
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                                                    totalcount();
                                                    Intent intentincres = new Intent("collection_only_repatlast");
                                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentincres);
                                                }
                                            });


                                            browse.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if (browse.getText().toString().equalsIgnoreCase("Cancel")) {
                                                        popupWindowreatlast.dismiss();
                                                    } else {
                                                        popupWindowreatlast.dismiss();
                                                        Intent intent = new Intent(context, Dashboard_Activity.class);
                                                        context.startActivity(intent);
                                                    }

                                                }
                                            });

                                            popupWindowreatlast.showAsDropDown(popupView, 0, 0);


                                        } else {
                                            /*------------------repead last-----------------------*/
                                            popupWindow.dismiss();
                                            if (count == 1) {
                                                // holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.delete_item));
                                                holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
                                                holder.delete_All_Item.setVisibility(View.GONE);
                                            } else {
                                                 holder.delete_All_Item.setVisibility(View.VISIBLE);
                                                  holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
                                            }

                                            count = Integer.parseInt(holder.tvinteger_number.getText().toString());
                                            count++;
                                            holder.tvinteger_number.setText(String.valueOf(count));

                                            num1 = Double.parseDouble(holder.tvitem_total.getText().toString());
                                            num2 = Double.parseDouble(contacts.getAmount());
                                            sum = num1 + num2;
                                            holder.tvitem_total.setText(String.format("%.2f", sum));
                                            Log.e("num1", "" + num1);
                                            Log.e("num2", "" + num2);

                                            Intent intent = new Intent("update_itemqty");
                                            intent.putExtra("ItemId", contacts.getId());
                                            intent.putExtra("Itemqty", holder.tvinteger_number.getText().toString());
                                            intent.putExtra("Itemamount", holder.tvitem_total.getText().toString());
                                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                                            totalcount();
                                        }

                                        //Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();


                                    } else {
                                        Toast.makeText(context, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<menu_addon_status_model> call, Throwable t) {
                                    Log.e("menuThrowable", "" + t);
                                    Toast.makeText(context, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                    //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    });

                    add_new.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          /*  Intent intent = new Intent(context, Item_Menu_Activity.class);
                            intent.putExtra("menuurlpath", menuurlpath);
                            context.startActivity(intent);*/

                            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();


                            Map<String, String> params = new HashMap<String, String>();
                        /*    params.put("id", contacts.getItemid());
                            params.put("ordermode", butordertype);*/




                            if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("1")) {

                                params.put("time", "");
                                params.put("id", contacts.getItemid());
                                params.put("ordermode", butordertype);
                                params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                                params.put("dates", sharedpreferences.getString("orderlaterdate", null));

                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("2")) {

                                params.put("time", sharedpreferences.getString("ordertodattime", null));
                                params.put("id", contacts.getItemid());
                                params.put("ordermode", butordertype);
                                params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                                params.put("dates", "");
                                params.put("date_string",sharedpreferences.getString("todaytimestring", null));


                            } else if (sharedpreferences.getString("orderactivetag", null).equalsIgnoreCase("3")) {
                                params.put("time",sharedpreferences.getString("orderlatertime", null));
                                params.put("id", contacts.getItemid());
                                params.put("ordermode", butordertype);
                                params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                                params.put("dates", sharedpreferences.getString("orderlaterdate", null));
                                params.put("date_string", sharedpreferences.getString("latertimestring", null));

                            }


/*
                            params.put("time",sharedpreferences.getString("ordertodattime", null) + sharedpreferences.getString("orderlatertime", null));
                            params.put("id", contacts.getItemid());
                            params.put("ordermode", butordertype);
                            params.put("activetab", sharedpreferences.getString("orderactivetag", null));
                            params.put("dates", sharedpreferences.getString("orderlaterdate", null));*/

                            fullUrladdon = menuurlpath + "/menu" + "/itemadd";
                            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                            Call<menu_addon_status_model> call = apiService.menuaddon(fullUrladdon, params);


                            Log.e("addtocartparams4", ": " + params);
                            Log.e("fullUrladdon", ": " + fullUrladdon);

                            call.enqueue(new Callback<menu_addon_status_model>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onResponse(Call<menu_addon_status_model> call, Response<menu_addon_status_model> response) {
                                    //response.headers().get("Set-Cookie");
                                    int statusCode = response.code();
                                    if (statusCode == 200) {
                                        Log.e("statusfor1", ": " + response.body().getStatus());
                                        Log.e("statusfor2", ": " + response.body().getError_code());


                                      /*  Intent intentitemdetails = new Intent(context, Item_Menu_Activity.class);
                                        intentitemdetails.putExtra("item", contacts.getItemid());
                                        intentitemdetails.putExtra("addonid", response.body().getAddonId());
                                        intentitemdetails.putExtra("categoryname", contacts.getCategoryname());
                                        intentitemdetails.putExtra("subcategoryname", contacts.getSubcategoryname());
                                        intentitemdetails.putExtra("menuurlpath", menuurlpath);
                                        context.startActivity(intentitemdetails);*/


                                        if (response.body().getError_code().equalsIgnoreCase("300")) {
                                            popupWindow.dismiss();
                                            holder.showPopupaddon(view, "Takeaway Closed", response.body().getError_message().getTakeawayclosed().getCurrentlytakeaway(), "300", contacts.getItemid(), response.body().getAddonId(), contacts.getCategoryname(), contacts.getSubcategoryname(), menuurlpath);
                                        } else if (response.body().getError_code().equalsIgnoreCase("101")) {
                                            popupWindow.dismiss();
                                            holder.showPopupaddon(view, response.body().getError_message().getChooseMode().getPopupdelivery(), response.body().getError_message().getChooseMode().getOnlycollection(), "101", contacts.getItemid(), response.body().getAddonId(), contacts.getCategoryname(), contacts.getSubcategoryname(), menuurlpath);
                                        } else if (response.body().getError_code().equalsIgnoreCase("200")) {
                                            popupWindow.dismiss();
                                            holder.showPopupaddon(view, "Delivery Closed", response.body().getError_message().getMsg(), "200", contacts.getItemid(), response.body().getAddonId(), contacts.getCategoryname(), contacts.getSubcategoryname(), menuurlpath);
                                        } else if (response.body().getError_code().equalsIgnoreCase("201")) {
                                            popupWindow.dismiss();
                                            holder.showPopupaddon(view, "Delivery Closed", response.body().getError_message().getMsg(), "201", contacts.getItemid(), response.body().getAddonId(), contacts.getCategoryname(), contacts.getSubcategoryname(), menuurlpath);
                                        } else if (response.body().getError_code().equalsIgnoreCase("102")) {
                                            popupWindow.dismiss();
                                            //addon show
                                          /*  String ItemName = contacts.getId();
                                            Intent intent = new Intent("custom-message");
                                            intent.putExtra("item", ItemName);
                                            intent.putExtra("addonid", response.body().getAddonId());
                                            intent.putExtra("categoryname", contacts.getCategoryname());
                                            intent.putExtra("subcategoryname", contacts.getSubcategoryname());
                                            LocalBroadcastManager.getInstance((((Item_Menu_Activity) context).getBaseContext())).sendBroadcast(intent);
                                           */
                                            //String ItemName = contacts.getItemid();

                                            Log.e("senditem1", "" + contacts.getItemid());
                                            Log.e("senditem2", "" + response.body().getAddonId());
                                            Log.e("senditem3", "" + contacts.getCategoryname());
                                            Log.e("senditem4", "" + contacts.getSubcategoryname());
                                            Log.e("senditem5", "" + menuurlpath);

                                            Intent intentitemdetails = new Intent(context, Item_Menu_Activity.class);
                                            intentitemdetails.putExtra("item", contacts.getItemid());
                                            intentitemdetails.putExtra("addonid", response.body().getAddonId());
                                            intentitemdetails.putExtra("categoryname", contacts.getCategoryname());
                                            intentitemdetails.putExtra("subcategoryname", contacts.getSubcategoryname());
                                            intentitemdetails.putExtra("menuurlpath", menuurlpath);
                                            context.startActivity(intentitemdetails);
                                        } else {
                                            popupWindow.dismiss();
                                            Toast.makeText(context, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                        }

                                        //Snackbar.make(Item_Menu_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();


                                    } else {
                                        Toast.makeText(context, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<menu_addon_status_model> call, Throwable t) {
                                    Log.e("menuThrowable", "" + t);
                                    Toast.makeText(context, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                    //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    });

                    popupWindow.showAsDropDown(popupViewrepet, 0, 0);


                } else {

                    if (count == 1) {
                        //  holder.tvdecrease.setBackground(context.getResources().getDrawable(R.drawable.delete_item));
                        holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_item));
                        holder.delete_All_Item.setVisibility(View.GONE);
                    } else {
                         holder.delete_All_Item.setVisibility(View.VISIBLE);
                         holder.tvdecrease.setImageDrawable(context.getResources().getDrawable(R.drawable.minus_item));
                    }

                    count = Integer.parseInt(holder.tvinteger_number.getText().toString());
                    count++;
                    holder.tvinteger_number.setText(String.valueOf(count));

                    num1 = Double.parseDouble(holder.tvitem_total.getText().toString());
                    num2 = Double.parseDouble(contacts.getAmount());
                    sum = num1 + num2;
                    holder.tvitem_total.setText(String.format("%.2f", sum));
                    Log.e("num1", "" + num1);
                    Log.e("num2", "" + num2);


                    Intent intent = new Intent("update_itemqty");
                    intent.putExtra("ItemId", contacts.getId());
                    intent.putExtra("Itemqty", holder.tvinteger_number.getText().toString());
                    intent.putExtra("Itemamount", holder.tvitem_total.getText().toString());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    totalcount();

                }


            }
        });


        totalcount();

    }


    @Override
    public int getItemCount() {
        return listContacts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvitem_addon_name, tvinteger_number, tvitem_total;
        public ImageView tvdecrease, tvincrease,delete_All_Item;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_name);
            tvitem_addon_name = itemView.findViewById(R.id.item_addon_name);
            tvinteger_number = itemView.findViewById(R.id.integer_number);
            tvitem_total = itemView.findViewById(R.id.item_total);
            tvdecrease = itemView.findViewById(R.id.decrease);
            tvincrease = itemView.findViewById(R.id.increase);
            delete_All_Item = itemView.findViewById(R.id.delete_All_Item);
        }


        public void showPopupaddon(View view, String title, String msg, String codes, String stritem, String straddonid, String strcategoryname, String strsubcategoryname, String strmenuurlpath) {
            View popupView = LayoutInflater.from(view.getContext()).inflate(R.layout.addon_popup, null);
            final PopupWindow popupWindowaddon = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //Button takaway_btn_dismiss = popupView.findViewById(R.id.takaway_btn_dismiss);
            //300 Takeaway Closed
            //101 Delivery Closed
            //200 Delivery Closed collection only
            //201 Delivery Closed collection only
            /*Intent intent = new Intent(context, Item_Menu_Activity.class);
            intent.putExtra("menuurlpath", listdata[position].getMenuurlpath());
            context.startActivity(intent);*/

            AppCompatButton browse = popupView.findViewById(R.id.browse);
            AppCompatButton update = popupView.findViewById(R.id.update);

            TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);
            TextView takaway_status = popupView.findViewById(R.id.takaway_status);

            //ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);

            takaway_status.setText(title);
            takaway_status_dec.setText(msg);

            if (codes.equalsIgnoreCase("300")) {
                update.setText("Cancel");
                browse.setText("Browse");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowaddon.dismiss();
                    }
                });

            } else if (codes.equalsIgnoreCase("101")) {
                update.setText("Collection");
                browse.setText("Cancel");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowaddon.dismiss();
                        Intent intent = new Intent("collection_only");
                        intent.putExtra("item", stritem);
                        intent.putExtra("addonid", straddonid);
                        intent.putExtra("categoryname", strcategoryname);
                        intent.putExtra("subcategoryname", strsubcategoryname);
                        intent.putExtra("menuurlpath", strmenuurlpath);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    }
                });

            } else if (codes.equalsIgnoreCase("200")) {
                update.setText("Collection");
                browse.setText("Cancel");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowaddon.dismiss();
                        Intent intent = new Intent("collection_only");
                        intent.putExtra("item", stritem);
                        intent.putExtra("addonid", straddonid);
                        intent.putExtra("categoryname", strcategoryname);
                        intent.putExtra("subcategoryname", strsubcategoryname);
                        intent.putExtra("menuurlpath", strmenuurlpath);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                });

            } else if (codes.equalsIgnoreCase("201")) {
                update.setText("Collection");
                browse.setText("Cancel");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowaddon.dismiss();
                        Intent intent = new Intent("collection_only");
                        intent.putExtra("item", stritem);
                        intent.putExtra("addonid", straddonid);
                        intent.putExtra("categoryname", strcategoryname);
                        intent.putExtra("subcategoryname", strsubcategoryname);
                        intent.putExtra("menuurlpath", strmenuurlpath);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    }
                });
            }

            browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (browse.getText().toString().equalsIgnoreCase("Cancel")) {
                        popupWindowaddon.dismiss();
                    } else {
                        popupWindowaddon.dismiss();
                        Intent intent = new Intent(context, Dashboard_Activity.class);
                        context.startActivity(intent);
                    }

                }
            });

            popupWindowaddon.showAsDropDown(popupView, 0, 0);
        }

    }


    public void totalcount() {
        num5 = 0.00;

        for (int i = 0; i < listContacts.size(); i++) {

            num6 = Double.parseDouble(listContacts.get(i).getFinalamt());
            num5 = num5 + num6;
        }

        Intent intent = new Intent("update_subtotal");
        intent.putExtra("Itemsubtotal", String.format("%.2f", num5));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        Log.e("totalPrice", "" + String.format("%.2f", num5));
    }

}
//"£ "+