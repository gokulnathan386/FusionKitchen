package com.fusionkitchen.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.activity.Add_to_Cart;
import com.fusionkitchen.activity.Dashboard_Activity;
import com.fusionkitchen.activity.Item_Menu_Activity;
import com.fusionkitchen.model.order_history.reorder_details_show;
import com.fusionkitchen.model.order_history.reorderdetail_mode;
import com.fusionkitchen.model.order_history.takeawystatusdetail_mode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fusionkitchen.R;
import com.fusionkitchen.activity.Order_History_Activity;
import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.menu_model.menu_item_sub_model;
import com.fusionkitchen.model.menu_model.menu_offer_model;
import com.fusionkitchen.model.order_history.order_details_list_show;
import com.fusionkitchen.model.order_history.orderdetail_mode;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryListAdapter.ViewHolder> {

    private Context mContext;
    private ordhistorys_list_model.reorder_details[] orderhistory;
    private int num;
    int cursor;
    String itemnamevalus;

    // RecyclerView recyclerView;
    public OrderHistoryListAdapter(Context mContext, List<ordhistorys_list_model.reorder_details> orderhistory, int num) {
        this.orderhistory = orderhistory.toArray(new ordhistorys_list_model.reorder_details[0]);
        this.mContext = mContext;
        this.num = num;
    }

    @Override
    public OrderHistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.raw_order_history, parent, false);
        OrderHistoryListAdapter.ViewHolder viewHolder = new OrderHistoryListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    ArrayList<String> addonnamearrayListUser = new ArrayList<>();
    ArrayList<String> addonnamearrayListUserid = new ArrayList<>();
    ArrayList<String> addonnamearrayListUserextra = new ArrayList<>();

    ArrayList<String> notavaiableitem = new ArrayList<>();

    ArrayList<String> repetaddonnameary = new ArrayList<>();
    ArrayList<String> repetaddonnameidary = new ArrayList<>();
    ArrayList<String> repetaddonextraary = new ArrayList<>();


    String addonnamearrayData = "";
    String addonnameidarrayData = "";
    String addonextraarrayData = "";
    String itemname = "";
    String itemqty = "";
    String itemnameid = "";
    String itemnametotal = "";
    String itemsubcatname = "";
    String addonnamearrayDataid = "";
    String addonnamearrayDataextra = "";
    String itemAmt;
    double itemamtotal = 0.00;
    double itemaddonAmts = 0.00;
    private SQLDBHelper dbHelper;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs_extra";
    String menuurlpath;
    Dialog dialog;
    Dialog warningdialog, removeditemdiag;
    PopupWindow takeawyWindowopen;

    @Override
    public void onBindViewHolder(OrderHistoryListAdapter.ViewHolder holder, int position) {
        final ordhistorys_list_model.reorder_details myListData = orderhistory[position];
        //client logo set
        Picasso.get()
                .load("https://fusionbucket.co.uk/img/" + orderhistory[position].getClient().getLogo())
                .placeholder(R.drawable.hederlocoplaceimg)
                .error(R.drawable.hederlocoplaceimg)
                .into(holder.list_client_logo);
        holder.list_total_amt.setText("£ " + orderhistory[position].getOrder().getTotal());
        holder.list_client_name.setText(orderhistory[position].getClient().getClientname());
        holder.list_order_date.setText(orderhistory[position].getOrder().getDate());

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(mContext);

        getContactsCount();


        holder.list_show_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderdetailshow(orderhistory[position].getOrder().getId(), orderhistory[position].getClient().getPath(), "0");
            }

            private void orderdetailshow(String id, String path, String stype) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderdetails", id);
                params.put("path", path);
                params.put("ordermode", stype);
                ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                Call<orderdetail_mode> call = apiService.orderdetail(params);

                Log.e("paramsvalues", "" + params);
                call.enqueue(new Callback<orderdetail_mode>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call<orderdetail_mode> call, Response<orderdetail_mode> response) {
                        //response.headers().get("Set-Cookie");

                        int statusCode = response.code();
                        if (statusCode == 200) {
                            Log.e("statusfor1", ": " + response.body().getStatus());

                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                holder.order_show2.setVisibility(View.VISIBLE);
                                holder.list_hide_bill.setVisibility(View.VISIBLE);
                                holder.list_show_bill.setVisibility(View.GONE);

                               /* ViewGroup.LayoutParams params = holder.order_show2.getLayoutParams();
                                params.height = 0;
                                params.width = 0;
                                holder.order_show2.setLayoutParams(params);

                                ViewGroup.LayoutParams params = holder.list_hide_bill.getLayoutParams();
                                params.height = 0;
                                params.width = 0;
                                holder.list_hide_bill.setLayoutParams(params);

                                ViewGroup.LayoutParams params = holder.list_show_bill.getLayoutParams();
                                params.height = 0;
                                params.width = 0;
                                holder.list_show_bill.setLayoutParams(params);*/

                                holder.sub_amt.setText("£ " + response.body().getReorder_details().getOrder().getOrder().getSub_total());
                                holder.total_amt.setText("£ " + response.body().getReorder_details().getOrder().getOrder().getTotal());

                                if (response.body().getReorder_details().getOrder().getOrder().getBank().equalsIgnoreCase("0.00") || response.body().getReorder_details().getOrder().getOrder().getBank().equalsIgnoreCase("0.0") || response.body().getReorder_details().getOrder().getOrder().getBank().equalsIgnoreCase("0")) {
                                    holder.servicel_layout.setVisibility(View.GONE);
                                } else {
                                    holder.servicel_layout.setVisibility(View.VISIBLE);
                                    holder.service_amt.setText("£ " + response.body().getReorder_details().getOrder().getOrder().getBank());
                                }
                                if (response.body().getReorder_details().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0.00") || response.body().getReorder_details().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0.0") || response.body().getReorder_details().getOrder().getOrder().getDelivery_charge().equalsIgnoreCase("0")) {
                                    holder.delivery_layout.setVisibility(View.GONE);
                                } else {
                                    holder.delivery_layout.setVisibility(View.VISIBLE);
                                    holder.delivery_amt.setText("£ " + response.body().getReorder_details().getOrder().getOrder().getDelivery_charge());
                                }
                                if (response.body().getReorder_details().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0.00") || response.body().getReorder_details().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0.0") || response.body().getReorder_details().getOrder().getOrder().getPromo_discount().equalsIgnoreCase("0")) {
                                    holder.coupon_layout.setVisibility(View.GONE);
                                } else {
                                    holder.coupon_layout.setVisibility(View.VISIBLE);
                                    holder.coupon_amt.setText(response.body().getReorder_details().getOrder().getOrder().getPromo_discount());
                                }
                                if (response.body().getReorder_details().getOrder().getOrder().getBagage().equalsIgnoreCase("0.00") || response.body().getReorder_details().getOrder().getOrder().getBagage().equalsIgnoreCase("0.0") || response.body().getReorder_details().getOrder().getOrder().getBagage().equalsIgnoreCase("0")) {
                                    holder.bag_layout.setVisibility(View.GONE);
                                } else {
                                    holder.bag_layout.setVisibility(View.VISIBLE);
                                    holder.bag_amt.setText(response.body().getReorder_details().getOrder().getOrder().getBagage());
                                }
                                List<order_details_list_show.item> orderhistory = (response.body().getReorder_details().getOrder().getItem());
                                OrderHistoryitemListAdapter menuitemnameadapter = new OrderHistoryitemListAdapter(mContext, (List<order_details_list_show.item>) orderhistory);
                                holder.myorderList.setHasFixedSize(true);
                                holder.myorderList.setLayoutManager(new LinearLayoutManager(mContext));
                                holder.myorderList.setAdapter(menuitemnameadapter);
                            } else {
                                holder.order_show2.setVisibility(View.GONE);
                                holder.list_hide_bill.setVisibility(View.GONE);
                                holder.list_show_bill.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(mContext, R.string.somthinnot_right, LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<orderdetail_mode> call, Throwable t) {
                        Log.e("menuThrowable", "" + t);
                        Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        holder.list_hide_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.order_show2.setVisibility(View.GONE);
                holder.list_hide_bill.setVisibility(View.GONE);
                holder.list_show_bill.setVisibility(View.VISIBLE);
            }
        });
        holder.repeatorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notavaiableitem.clear();
                reordertakeawaydetails(orderhistory[position].getClient().getPath());
                //reorderdetails(orderhistory[position].getOrder().getId(), orderhistory[position].getClient().getPath(), "0");
            }

            private void reordertakeawaydetails(String path) {
                loadingshow();
                Map<String, String> params = new HashMap<String, String>();
                params.put("path", path);
                ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                Call<takeawystatusdetail_mode> call = apiService.takeawystatusdetail(params);
                Log.e("paramsvalues", "" + params);
                call.enqueue(new Callback<takeawystatusdetail_mode>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call<takeawystatusdetail_mode> call, Response<takeawystatusdetail_mode> response) {
                        //response.headers().get("Set-Cookie");
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            hideloading();
                            Log.e("statusfor1", ": " + response.body().getStatus());
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                /*---------------------------Get Menu URL using SharedPreferences----------------------------------------------------*/
                                sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, mContext.MODE_PRIVATE);
                                menuurlpath = sharedpreferences.getString("menuurlpath", null);
                                Log.e("addtocarturl", "" + menuurlpath);
                                if (menuurlpath.equalsIgnoreCase(orderhistory[position].getClient().getPath())) {
                                    // Toast.makeText(mContext, "Equal", LENGTH_LONG).show();
                                    reorderdetails(orderhistory[position].getOrder().getId(), orderhistory[position].getClient().getPath(), orderhistory[position].getOrder().getOtype());
                                } else {
                                    if (cursor != 0) {
                                        //Toast.makeText(mContext, "Not Equal", LENGTH_LONG).show();
                                        warningdialog = new Dialog(mContext, R.style.MaterialDialogSheet);
                                        warningdialog.setContentView(R.layout.warning_cart_remove);
                                        AppCompatButton go_back = warningdialog.findViewById(R.id.go_back);
                                        AppCompatButton remove_cart = warningdialog.findViewById(R.id.remove_cart);
                                        go_back.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                warningdialog.dismiss();
                                            }
                                        });
                                        remove_cart.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                warningdialog.dismiss();
                                                reorderdetails(orderhistory[position].getOrder().getId(), orderhistory[position].getClient().getPath(), orderhistory[position].getOrder().getOtype());
                                            }
                                        });
                                        warningdialog.show();
                                    } else {
                                        reorderdetails(orderhistory[position].getOrder().getId(), orderhistory[position].getClient().getPath(), orderhistory[position].getOrder().getOtype());
                                    }

                                }
                            } else {
                                View popupView = LayoutInflater.from(mContext).inflate(R.layout.takeawaystaus_popup, null);
                                takeawyWindowopen = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                                AppCompatButton update = popupView.findViewById(R.id.update);
                                TextView takaway_status = popupView.findViewById(R.id.takaway_status);
                                TextView takaway_status_dec = popupView.findViewById(R.id.takaway_status_dec);

                                //ImageView takaway_status_img = popupView.findViewById(R.id.takaway_status_img);

                                takaway_status.setText("Takeaway Closed");
                                takaway_status_dec.setText(response.body().getMsg());


                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        takeawyWindowopen.dismiss();
                                    }
                                });


                                takeawyWindowopen.showAsDropDown(popupView, 0, 0);


                            }
                        } else {
                            hideloading();
                            Toast.makeText(mContext, R.string.somthinnot_right, LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<takeawystatusdetail_mode> call, Throwable t) {
                        hideloading();
                        Log.e("menuThrowable", "" + t);
                        Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                });
            }

            private void reorderdetails(String id, String path, String stype) {
                loadingshow();
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", id);
                params.put("path", path);
                params.put("ordermode", stype);

                ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                Call<reorderdetail_mode> call = apiService.reorderdetail(params);
                Log.e("paramsvalues", "" + params);
                call.enqueue(new Callback<reorderdetail_mode>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call<reorderdetail_mode> call, Response<reorderdetail_mode> response) {
                        //response.headers().get("Set-Cookie");
                        int statusCode = response.code();

                        Log.e("reorderstatus", "" + statusCode);
                        if (statusCode == 200) {
                            hideloading();
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                List<reorder_details_show.item> reorderhistory = (response.body().getReorder_details().getOrder().getItem());
                                Log.e("reorderhistory", "" + reorderhistory.size());
                                for (int i = 0; i < reorderhistory.size(); i++) {
                                    if (reorderhistory.get(i).getNotavaiableitem() != null) {
                                        notavaiableitem.add(reorderhistory.get(i).getNotavaiableitem());
                                    }
                                    // notavaiableitem.add(reorderhistory.get(i).getNotavaiableitem());
                                }
                                Log.e("notavaiableitemsize", "" + notavaiableitem.size());

                                if (notavaiableitem.size() == 0) {

                                    dbHelper.deleteAll();
                                    SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                    editor_extra.putString("menuurlpath", orderhistory[position].getClient().getPath());
                                    editor_extra.commit();
                                    Log.e("reorderpath", "" + orderhistory[position].getClient().getPath());
                                    getContactsCount();
                                    Toast.makeText(mContext, "Your cart has been cleared with old items. It's been refilled with re-order items!", LENGTH_LONG).show();

                                    //add insert method
                                    for (int i = 0; i < reorderhistory.size(); i++) {
                                        if (reorderhistory.get(i).getAvaiableitems() != null) {
                                            itemamtotal = Double.parseDouble(reorderhistory.get(i).getAvaiableitems().getPrice());
                                            itemaddonAmts = Double.parseDouble(reorderhistory.get(i).getAvaiableitems().getPrice());

                                            Log.e("reorderdetails03", ": " + reorderhistory.get(i).getAvaiableitems().getName());
                                            if (reorderhistory.get(i).getAddon() != null) {
                                                for (int j = 0; j < reorderhistory.get(i).getAddon().size(); j++) {
                                                    repetaddonnameary.add(reorderhistory.get(i).getAddon().get(j).getAddon_name());
                                                    repetaddonnameidary.add(reorderhistory.get(i).getAddon().get(j).getAddonid());
                                                    repetaddonextraary.add(reorderhistory.get(i).getAddon().get(j).getAddonextra());


                                                    double d2 = Double.parseDouble(reorderhistory.get(i).getAddon().get(j).getAddonitem().getPrice());//Add addon total amount
                                                    itemamtotal = itemamtotal + d2;
                                                    itemaddonAmts = itemaddonAmts + d2;
                                                    Log.e("addoonprices", ": " + d2);

                                                }
                                            }

                                            itemamtotal = itemamtotal * Double.parseDouble(reorderhistory.get(i).getQty());

                                            addonnamearrayData = repetaddonnameary.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Name
                                            addonnameidarrayData = repetaddonnameidary.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Name
                                            addonextraarrayData = repetaddonextraary.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Name

                                            repetaddonnameary.clear();
                                            repetaddonnameidary.clear();
                                            repetaddonextraary.clear();


                                            insertreorderitem(reorderhistory.get(i).getAvaiableitems().getName(), reorderhistory.get(i).getAvaiableitems().getId(), addonnamearrayData, addonnameidarrayData, addonextraarrayData, String.format("%.2f", itemaddonAmts), reorderhistory.get(i).getQty(), String.format("%.2f", itemaddonAmts), String.format("%.2f", itemamtotal), reorderhistory.get(i).getAvaiableitems().getCatname(), reorderhistory.get(i).getAvaiableitems().getSubname());


                                        }


                                    }


                                } else {

                                    Log.e("itemsize1", "" + reorderhistory.size());
                                    Log.e("itemsize2", "" + notavaiableitem.size());
                                    if (reorderhistory.size() == notavaiableitem.size()) {


                                        removeditemdiag = new Dialog(mContext, R.style.MaterialDialogSheet);
                                        removeditemdiag.setContentView(R.layout.dialog_show_removeditem);

                                        TextView item_mes = removeditemdiag.findViewById(R.id.item_mes);
                                        TextView delete_item_name = removeditemdiag.findViewById(R.id.delete_item_name);


                                        AppCompatButton cancel_last = removeditemdiag.findViewById(R.id.cancel_last);
                                        AppCompatButton add_cart_new = removeditemdiag.findViewById(R.id.add_cart_new);

                                        item_mes.setVisibility(View.VISIBLE);
                                        delete_item_name.setVisibility(View.GONE);
                                        cancel_last.setVisibility(View.VISIBLE);
                                        add_cart_new.setVisibility(View.GONE);


                                        item_mes.setText("Uh-Oh! All items in your re-order are not available");


                                        cancel_last.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                removeditemdiag.dismiss();
                                            }
                                        });

                                        removeditemdiag.show();

                                    } else {

                                        removeditemdiag = new Dialog(mContext, R.style.MaterialDialogSheet);
                                        removeditemdiag.setContentView(R.layout.dialog_show_removeditem);

                                        TextView item_mes = removeditemdiag.findViewById(R.id.item_mes);
                                        TextView delete_item_name = removeditemdiag.findViewById(R.id.delete_item_name);

                                        AppCompatButton cancel_last = removeditemdiag.findViewById(R.id.cancel_last);
                                        AppCompatButton add_cart_new = removeditemdiag.findViewById(R.id.add_cart_new);


                                        item_mes.setVisibility(View.VISIBLE);
                                        delete_item_name.setVisibility(View.VISIBLE);
                                        cancel_last.setVisibility(View.VISIBLE);
                                        add_cart_new.setVisibility(View.VISIBLE);


                                        item_mes.setText("Uh-Oh! These items in your re-order are not available");
                                        delete_item_name.setText(notavaiableitem.toString().replace("[", "").replace("]", "").replace(" ", "").trim());

                                        cancel_last.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                removeditemdiag.dismiss();
                                            }
                                        });
                                        add_cart_new.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dbHelper.deleteAll();
                                                SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                                editor_extra.putString("menuurlpath", orderhistory[position].getClient().getPath());
                                                editor_extra.commit();
                                                Log.e("reorderpath", "" + orderhistory[position].getClient().getPath());

                                                getContactsCount();
                                                removeditemdiag.dismiss();
                                                Toast.makeText(mContext, "Your cart has been cleared with old items. It's been refilled with re-order items!", LENGTH_LONG).show();

                                                //add insert method
                                                for (int i = 0; i < reorderhistory.size(); i++) {
                                                    if (reorderhistory.get(i).getAvaiableitems() != null) {
                                                        itemamtotal = Double.parseDouble(reorderhistory.get(i).getAvaiableitems().getPrice());
                                                        itemaddonAmts = Double.parseDouble(reorderhistory.get(i).getAvaiableitems().getPrice());

                                                        Log.e("reorderdetails03", ": " + reorderhistory.get(i).getAvaiableitems().getName());
                                                        if (reorderhistory.get(i).getAddon() != null) {
                                                            for (int j = 0; j < reorderhistory.get(i).getAddon().size(); j++) {
                                                                repetaddonnameary.add(reorderhistory.get(i).getAddon().get(j).getAddon_name());
                                                                repetaddonnameidary.add(reorderhistory.get(i).getAddon().get(j).getAddonid());
                                                                repetaddonextraary.add(reorderhistory.get(i).getAddon().get(j).getAddonextra());

                                                                double d2 = Double.parseDouble(reorderhistory.get(i).getAddon().get(j).getPrice());//Add addon total amount
                                                                itemamtotal = itemamtotal + d2;
                                                                itemaddonAmts = itemaddonAmts + d2;


                                                            }
                                                        }

                                                        itemamtotal = itemamtotal * Double.parseDouble(reorderhistory.get(i).getQty());

                                                        addonnamearrayData = repetaddonnameary.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Name
                                                        addonnameidarrayData = repetaddonnameidary.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Name
                                                        addonextraarrayData = repetaddonextraary.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Name

                                                        repetaddonnameary.clear();
                                                        repetaddonnameidary.clear();
                                                        repetaddonextraary.clear();


                                                        insertreorderitem(reorderhistory.get(i).getAvaiableitems().getName(), reorderhistory.get(i).getAvaiableitems().getId(), addonnamearrayData, addonnameidarrayData, addonextraarrayData, String.format("%.2f", itemaddonAmts), reorderhistory.get(i).getQty(), String.format("%.2f", itemaddonAmts), String.format("%.2f", itemamtotal), reorderhistory.get(i).getAvaiableitems().getCatname(), reorderhistory.get(i).getAvaiableitems().getSubname());


                                                    }


                                                }


                                            }
                                        });
                                        removeditemdiag.show();
                                    }
                                }
                            } else {
                                Toast.makeText(mContext, R.string.somthinnot_right, LENGTH_LONG).show();
                            }


                        } else {
                            hideloading();
                            Toast.makeText(mContext, R.string.somthinnot_right, LENGTH_LONG).show();
                        }
                    }

                    private void insertreorderitem(String str_itemname, String str_itemid, String str_addonnames, String str_addonnamesid, String str_addonextraid, String str_itemamt, String str_qty, String str_itemamtone, String str_itemtotalamt, String str_catname, String str_subname) {
                        //delete all db
                       /* dbHelper.deleteAll();
                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("menuurlpath", orderhistory[position].getClient().getPath());
                        editor_extra.commit();
                        Log.e("reorderpath", "" + orderhistory[position].getClient().getPath());
*/
                        repetaddonnameary.clear();
                        repetaddonnameidary.clear();
                        repetaddonextraary.clear();

                        Log.e("str_itemname", "" + str_itemname);
                        Log.e("str_addonnames", "" + str_addonnames);


                        SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                        editor_extra.putString("ordermodetype", orderhistory[position].getOrder().getOtype());
                        editor_extra.commit();


                        if (dbHelper.insertItem(str_itemname, str_itemid, str_addonnames, str_addonnamesid, str_addonextraid, str_itemamt, str_qty, str_itemamtone, str_itemtotalamt, str_catname, str_subname)) {
                            Intent intent = new Intent(mContext, Add_to_Cart.class);
                            intent.putExtra("cooking_insttruction", "");
                            mContext.startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<reorderdetail_mode> call, Throwable t) {
                        hideloading();
                        Log.e("reorderThrowable", "" + t);
                        Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /*private void reorderdetails(String id, String path, String stype) {
                loadingshow();
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderdetails", id);
                params.put("path", path);
                params.put("ordermode", stype);

                ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
                Call<orderdetail_mode> call = apiService.orderdetail(params);
                Log.e("paramsvalues", "" + params);
                call.enqueue(new Callback<orderdetail_mode>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call<orderdetail_mode> call, Response<orderdetail_mode> response) {
                        //response.headers().get("Set-Cookie");
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            hideloading();
                            Log.e("statusfor1", ": " + response.body().getStatus());
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                //delete all db
                                dbHelper.deleteAll();
                                SharedPreferences.Editor editor_extra = sharedpreferences.edit();
                                editor_extra.putString("menuurlpath", orderhistory[position].getClient().getPath());
                                editor_extra.commit();


                                List<order_details_list_show.item> orderhistory = (response.body().getReorder_details().getOrder().getItem());
                                OrderHistoryitemListAdapter menuitemnameadapter = new OrderHistoryitemListAdapter(mContext, (List<order_details_list_show.item>) orderhistory);
                                holder.myorderList.setHasFixedSize(true);
                                holder.myorderList.setLayoutManager(new LinearLayoutManager(mContext));
                                holder.myorderList.setAdapter(menuitemnameadapter);

                                for (int i = 0; i < orderhistory.size(); i++) {
                                    for (int j = 0; j < orderhistory.get(i).getAddon().size(); j++) {
                                        Log.e("reorderdetails01", ": " + orderhistory.get(i).getAddon().size());
                                        Log.e("reorderdetails0", ": " + orderhistory.get(i).getAddon().get(j).getAddon_name());
                                        addonnamearrayListUser.add(orderhistory.get(i).getAddon().get(j).getAddon_name());//Addon Name
                                        addonnamearrayListUserid.add(orderhistory.get(i).getAddon().get(j).getAddonid());//Addon ID
                                        addonnamearrayListUserextra.add("");//Addon Extra

                                        // itemAmt = itemAmt + orderhistory.get(i).getAddon().get(j).getTotal();

                                        double d2 = Double.parseDouble(orderhistory.get(i).getAddon().get(j).getPrice());//Add addon total amount
                                        itemAmt = itemAmt + d2;

                                    }
                                    addonnamearrayData = addonnamearrayListUser.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Name
                                    addonnamearrayDataid = addonnamearrayListUserid.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon ID
                                    addonnamearrayDataextra = addonnamearrayListUserextra.toString().replace("[", "").replace("]", "").replace(" ", "").trim();//Addon Extra


                                    itemname = orderhistory.get(i).getItem_name();//Item Name
                                    itemnameid = orderhistory.get(i).getItemid();//Item ID
                                    itemnametotal = orderhistory.get(i).getPrice();//Item Total
                                    itemqty = orderhistory.get(i).getQty();//Item QTY
                                    itemsubcatname = orderhistory.get(i).getSub_name();//Item sub catagroy Name
                                    itemamtotal = (Double.parseDouble(orderhistory.get(i).getPrice()) + itemAmt) * Double.parseDouble(orderhistory.get(i).getQty());//Add addon total amount + Item total amount


                                    itemAmts = (Double.parseDouble(orderhistory.get(i).getPrice()) + itemAmt);
                                    addonnamearrayListUser.clear();
                                    addonnamearrayListUserid.clear();
                                    addonnamearrayListUserextra.clear();
                                    Log.e("reorderdetails1", ": " + itemname);
                                    Log.e("reorderdetails2", ": " + itemqty);
                                    Log.e("reorderdetails3", ": " + addonnamearrayData);
                                    Log.e("reorderdetails4", ": " + itemnameid);
                                    Log.e("reorderdetails5", ": " + itemnametotal);
                                    Log.e("reorderdetails6", ": " + itemsubcatname);
                                    Log.e("reorderdetails7", ": " + addonnamearrayDataid);
                                    Log.e("reorderdetails8", ": " + addonnamearrayDataextra);
                                    Log.e("reorderdetails9", ": " + String.format("%.2f", itemamtotal));
                                    itemAmt = 0.00;
                                    if (dbHelper.insertItem(itemname, itemnameid, addonnamearrayData, addonnamearrayDataid, addonnamearrayDataextra, String.format("%.2f", itemAmts), itemqty, String.format("%.2f", itemAmts), String.format("%.2f", itemamtotal), "", itemsubcatname)) {
                                        Intent intent = new Intent(mContext, Add_to_Cart.class);
                                        intent.putExtra("cooking_insttruction", "");
                                        mContext.startActivity(intent);

                                    }
                                }
                            } else {
                                Toast.makeText(mContext, R.string.somthinnot_right, LENGTH_LONG).show();
                            }
                        } else {
                            hideloading();
                            Toast.makeText(mContext, R.string.somthinnot_right, LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<orderdetail_mode> call, Throwable t) {
                        hideloading();
                        Log.e("menuThrowable", "" + t);
                        Toast.makeText(mContext, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                    }
                });
            }*/
        });
    }

    @Override
    public int getItemCount() {
        // return orderhistory.length;
        if (num * 10 > orderhistory.length) {
            return orderhistory.length;
        } else {
            return num * 10;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView list_total_amt, list_client_name, list_order_date, list_show_bill, list_hide_bill;
        public TextView sub_amt, service_amt, coupon_amt, total_amt, delivery_amt, bag_amt, repeatorder;
        public ImageView list_client_logo;
        public CardView order_show2;
        public RecyclerView myorderList;
        public RelativeLayout servicel_layout, delivery_layout, coupon_layout, bag_layout;


        public ViewHolder(View itemView) {
            super(itemView);
            this.list_client_logo = itemView.findViewById(R.id.list_client_logo);
            this.list_total_amt = itemView.findViewById(R.id.list_total_amt);
            this.list_client_name = itemView.findViewById(R.id.list_client_name);
            this.list_order_date = itemView.findViewById(R.id.list_order_date);
            this.list_show_bill = itemView.findViewById(R.id.list_show_bill);
            this.list_hide_bill = itemView.findViewById(R.id.list_hide_bill);
            this.order_show2 = itemView.findViewById(R.id.order_show2);
            this.repeatorder = itemView.findViewById(R.id.repeatorder);


            this.sub_amt = itemView.findViewById(R.id.sub_amt);
            this.service_amt = itemView.findViewById(R.id.service_amt);
            this.coupon_amt = itemView.findViewById(R.id.coupon_amt);
            this.total_amt = itemView.findViewById(R.id.total_amt);
            this.myorderList = itemView.findViewById(R.id.myorderList);
            this.delivery_amt = itemView.findViewById(R.id.delivery_amt);
            this.bag_amt = itemView.findViewById(R.id.bag_amt);

            this.servicel_layout = itemView.findViewById(R.id.servicel_layout);
            this.delivery_layout = itemView.findViewById(R.id.delivery_layout);
            this.coupon_layout = itemView.findViewById(R.id.coupon_layout);
            this.bag_layout = itemView.findViewById(R.id.bag_layout);



           /* this.client_logo = itemView.findViewById(R.id.client_logo);
            this.client_logo = itemView.findViewById(R.id.client_logo);
            this.client_logo = itemView.findViewById(R.id.client_logo);
            this.client_logo = itemView.findViewById(R.id.client_logo);
            this.client_logo = itemView.findViewById(R.id.client_logo);
            this.client_logo = itemView.findViewById(R.id.client_logo);
            this.client_logo = itemView.findViewById(R.id.client_logo);
            this.client_logo = itemView.findViewById(R.id.client_logo);*/
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    public int getContactsCount() {
        cursor = dbHelper.numberOfRows();
        Log.e("tmpStr10", "" + cursor);
        return cursor;
    }


}