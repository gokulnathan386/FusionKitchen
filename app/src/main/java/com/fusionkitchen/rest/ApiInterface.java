package com.fusionkitchen.rest;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;


import com.fusionkitchen.model.App_download_record_Tracking;
import com.fusionkitchen.model.HomeFetch_Detail_Model;
import com.fusionkitchen.model.Login.Login_mobile_email;
import com.fusionkitchen.model.Savecard.addnewsavecard_model;
import com.fusionkitchen.model.Savecard.deletesavecard_model;
import com.fusionkitchen.model.Savecard.getclientscSecret_model;
import com.fusionkitchen.model.Savecard.viewsavecard_details_model;
import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.addon.menu_addons_model;
import com.fusionkitchen.model.address.addAddress_mode;
import com.fusionkitchen.model.address.getaddAddress_mode;
import com.fusionkitchen.model.address.deleteaddress_mode;
import com.fusionkitchen.model.address.getaddressforpostcode_modal;
import com.fusionkitchen.model.address.updateaddress_mode;
import com.fusionkitchen.model.cart.coupon_valid_model;
import com.fusionkitchen.model.cart.serviceDelCharge_model;
import com.fusionkitchen.model.cart.subcategory_printer_model;
import com.fusionkitchen.model.checkout.CheckloginModel;
import com.fusionkitchen.model.checkout.chechoutvalidate_model;

import com.fusionkitchen.model.dashboard.location_fetch_details;
import com.fusionkitchen.model.favorite.insertfavorite_mode;
import com.fusionkitchen.model.gpay.getgpayclientscSecret_model;
import com.fusionkitchen.model.gpay.getgpaystuartpayment_model;
import com.fusionkitchen.model.home_model.location_type_modal;

import com.fusionkitchen.model.home_model.order_type_modal;
import com.fusionkitchen.model.home_model.popular_restaurants_listmodel;
import com.fusionkitchen.model.home_model.serachgetshop_modal;
import com.fusionkitchen.model.loginsignup.forgot_password_model;
import com.fusionkitchen.model.loginsignup.login_model;
import com.fusionkitchen.model.loginsignup.signup_model;
import com.fusionkitchen.model.menu_model.collDelivery_model;
import com.fusionkitchen.model.menu_model.final_addon_add_model;
import com.fusionkitchen.model.menu_model.menu_item_model;
import com.fusionkitchen.model.menu_model.ordertype_change_menu_model;
import com.fusionkitchen.model.menu_model.search_menu_item_model;
import com.fusionkitchen.model.modeoforder.getlatertime_model;
import com.fusionkitchen.model.modeoforder.modeof_order_popup_model;
import com.fusionkitchen.model.moreinfo.about_us_model;
import com.fusionkitchen.model.moreinfo.review_list_model;
import com.fusionkitchen.model.myaccount.get_account_modal;
import com.fusionkitchen.model.myaccount.update_account_modal;
import com.fusionkitchen.model.offer.offer_code_model;
import com.fusionkitchen.model.order_history.orderdetail_mode;
import com.fusionkitchen.model.order_history.ordhistorys_list_model;
import com.fusionkitchen.model.order_history.reorderdetail_mode;
import com.fusionkitchen.model.order_history.takeawystatusdetail_mode;
import com.fusionkitchen.model.orderstatus.orderstatus_model;
import com.fusionkitchen.model.orderstatus.ordertracking_model;
import com.fusionkitchen.model.orderstatus.submitfeedback_model;
import com.fusionkitchen.model.paymentgatway.appkey;
import com.fusionkitchen.model.paymentgatway.completpay_model;
import com.fusionkitchen.model.paymentgatway.getclientSecret_model;
import com.fusionkitchen.model.paymentmethod.order_payment_model;
import com.fusionkitchen.model.post_code_modal;
import com.fusionkitchen.model.social_signup_model;
import com.fusionkitchen.model.updatestuartaddress_modal;
import com.fusionkitchen.model.version_code_modal;
import com.fusionkitchen.model.wallet.get_wallet_amount;
import com.fusionkitchen.model.wallet.wallet_getrefer_details;
import com.fusionkitchen.model.wallet.wallet_transaction_model;
import com.fusionkitchen.model.wallet.wallet_walletbutton_model;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {

    //send post code for version update
    @POST("/versions")
    Call<version_code_modal> getversion();


    //send post code for login page
    @POST("/search-postcode")
    @FormUrlEncoded
    Call<post_code_modal> getpostcode(@FieldMap Map<String, String> params);



    @POST("/login_otp")
    @FormUrlEncoded
    Call<Login_mobile_email> sendotpemailphone(@FieldMap Map<String, String> params);

  /*  @POST("/ordertype")
    Call<post_code_modal> getordertype(@Path("order_type") Integer otp);*/

    @POST("{fullUrl}")
    @FormUrlEncoded
    Call<location_type_modal> getlocationshop(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/ordertype")
    @FormUrlEncoded
    Call<order_type_modal> getordertype(@Header("Cookie") String authKey, @FieldMap Map<String, Integer> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<ordertype_change_menu_model> changeordertypemenuitem(@Path(value = "fullUrl", encoded = true) String fullUrl, @Header("Cookie") String authKey, @FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<menu_item_model> getmenuitem(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<menu_addon_status_model> menuaddon(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<menu_addons_model> addonslist(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


   /* @POST("/login")
    @FormUrlEncoded
    Call<login_model> logininterfas(@FieldMap Map<String, String> params);*/

    @POST("/login_check")
    @FormUrlEncoded
    Call<login_model> logininterfas(@FieldMap Map<String, String> params);

    @POST("/registerprocess")
    @FormUrlEncoded
    Call<signup_model> signupinterfas(@FieldMap Map<String, String> params);

    @POST("/sociallogin")
    @FormUrlEncoded
    Call<social_signup_model> gmailsignupinterfas(@FieldMap Map<String, String> params);


    @POST("/orderhistorys")
    @FormUrlEncoded
    Call<ordhistorys_list_model> orderhistoryslist(@FieldMap Map<String, String> params);

    @POST("/forgotpasswordapi")
    @FormUrlEncoded
    Call<forgot_password_model> forgotpassword(@FieldMap Map<String, String> params);

    @POST("/apionlinediscount")
    @FormUrlEncoded
    Call<offer_code_model> offershow(@FieldMap Map<String, String> params);


    @POST("/getaddAddress")
    @FormUrlEncoded
    Call<getaddAddress_mode> getaddAddress(@FieldMap Map<String, String> params);


    @POST("/addAddress")
    @FormUrlEncoded
    Call<addAddress_mode> addAddress(@FieldMap Map<String, String> params);

    @POST("/updateaddress")
    @FormUrlEncoded
    Call<addAddress_mode> updateaddress(@FieldMap Map<String, String> params);


    @POST("/deleteaddress")
    @FormUrlEncoded
    Call<deleteaddress_mode> deleteaddress(@FieldMap Map<String, String> params);


    @POST("/updateprimaryAddress")
    @FormUrlEncoded
    Call<updateaddress_mode> updateprimaryAddress(@FieldMap Map<String, String> params);


    @POST("/orderdetail")
    @FormUrlEncoded
    Call<orderdetail_mode> orderdetail(@FieldMap Map<String, String> params);

    @POST("/opencloseReorder")
    @FormUrlEncoded
    Call<takeawystatusdetail_mode> takeawystatusdetail(@FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<coupon_valid_model> getcouponvalid(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<serviceDelCharge_model> getserviceDelCharge(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<review_list_model> reviewlist(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<about_us_model> getaboutus(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    //send post code for login page
    @POST("/userMyaccout")
    @FormUrlEncoded
    Call<get_account_modal> getaccountdetails(@FieldMap Map<String, String> params);

    @POST("/userupdateAPI")
    @FormUrlEncoded
    Call<update_account_modal> updateaccount(@FieldMap Map<String, String> params);

    @POST("/myaccountpassword")
    @FormUrlEncoded
    Call<update_account_modal> updateaccountpassword(@FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<chechoutvalidate_model> chechoutvalidate(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/orderstatus")
    @FormUrlEncoded
    Call<orderstatus_model> orderstatus(@FieldMap Map<String, String> params);


    @POST("/ordertracking")
    @FormUrlEncoded
    Call<ordertracking_model> ordertracking(@FieldMap Map<String, String> params);

    @POST("/stuartOrderTracking")
    @FormUrlEncoded
    Call<ordertracking_model> stuartordertracking(@FieldMap Map<String, String> params);



    // @Headers("Content-Type: application/json")
    @POST("/{fullUrl}")
    Call<CheckloginModel> insertOrder(@Path(value = "fullUrl", encoded = true) String fullUrl, @Body RequestBody requestBody);


    @POST("/{fullUrl}")
    Call<appkey> stripepubliskey(@Path(value = "fullUrl", encoded = true) String fullUrl);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<getclientSecret_model> getclientSecret(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params, @Header("Authorization") String auth);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<completpay_model> completpay(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params, @Header("Authorization") String auth);


    //send post code for login page
    @POST("/addresspostcode")
    @FormUrlEncoded
    Call<getaddressforpostcode_modal> getaddressforpostcode(@FieldMap Map<String, String> params);

    @POST("/userdetailupdate")
    @FormUrlEncoded
    Call<updatestuartaddress_modal> updatestuartaddress(@FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    Call<collDelivery_model> changecollDelivery(@Path(value = "fullUrl", encoded = true) String fullUrl);


    @POST("/{fullUrl}")
    Call<subcategory_printer_model> subcategoryprinter(@Path(value = "fullUrl", encoded = true) String fullUrl);

    @POST("/feedbackAPI")
    @FormUrlEncoded
    Call<submitfeedback_model> submitfeedback(@FieldMap Map<String, String> params);

/*
    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<search_menu_item_model> search_menu_item(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);*/


    @POST("/menu/searchAPI")
    @FormUrlEncoded
    Call<search_menu_item_model> search_menu_item(@FieldMap Map<String, String> params);


  /*  @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<offer_code_model> offershowmethod(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);*/


    @POST("/apionlinediscount")
    @FormUrlEncoded
    Call<offer_code_model> offershowmethod(@FieldMap Map<String, String> params);


    @POST("/SearchCusineAPI")
    @FormUrlEncoded
    Call<serachgetshop_modal> serachgetshop(@FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<order_payment_model> orderpayment(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<final_addon_add_model> final_addon_add(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);


    @POST("/reorderitems")
    @FormUrlEncoded
    Call<reorderdetail_mode> reorderdetail(@FieldMap Map<String, String> params);

    @POST("/addfavroitelist")
    @FormUrlEncoded
    Call<insertfavorite_mode> insertfavorite(@FieldMap Map<String, String> params);


    @POST("/favourite")
    @FormUrlEncoded
    Call<location_type_modal> getfavorite(@FieldMap Map<String, String> params);

    @POST("/couponlist")
    @FormUrlEncoded
    Call<location_type_modal> getofferlist(@FieldMap Map<String, String> params);


    @POST("/wallet")
    @FormUrlEncoded
    Call<get_wallet_amount> getwalletamt(@FieldMap Map<String, String> params);

    @POST("/wallet_transaction")
    @FormUrlEncoded
    Call<wallet_transaction_model> wallettransaction(@FieldMap Map<String, String> params);

    @POST("/walletbutton")
    @FormUrlEncoded
    Call<wallet_walletbutton_model> walletbutton(@FieldMap Map<String, String> params);


    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<wallet_getrefer_details> walletgetrefer(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params, @Header("Authorization") String auth);

    @POST("/savecards")
    @FormUrlEncoded
    Call<viewsavecard_details_model> viewsavecard(@FieldMap Map<String, String> params);


    @POST("/deletesavecard")
    @FormUrlEncoded
    Call<deletesavecard_model> deletesavecard(@FieldMap Map<String, String> params);

    @POST("/addsavecard")
    @FormUrlEncoded
    Call<addnewsavecard_model> addnewsavecard(@FieldMap Map<String, String> params);




    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<getclientscSecret_model> getscclientsecret(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);

    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<getgpayclientscSecret_model> getgpaylientsecret(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);

    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<getgpaystuartpayment_model> getstuartpayment(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);



    @POST("/{fullUrl}")
    Call<modeof_order_popup_model> modeofordershow(@Path(value = "fullUrl", encoded = true) String fullUrl);



    @POST("/{fullUrl}")
    @FormUrlEncoded
    Call<getlatertime_model> loadLaterTime(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);

    @POST("/appTracking")
    @FormUrlEncoded
    Call<App_download_record_Tracking> getappcountrecord(@FieldMap Map<String, String> params);



    @POST("/homePage")
    @FormUrlEncoded
    Call<HomeFetch_Detail_Model> getHomePage(@FieldMap Map<String, String> params);

 /*   @POST("{fullUrl}")
    @FormUrlEncoded
    Call<location_fetch_details> getlocationfetchdetails(@Path(value = "fullUrl", encoded = true) String fullUrl, @FieldMap Map<String, String> params);
*/
    @POST("/getRestaurant")
    Call<location_fetch_details> getlocationfetchdetails(@Body RequestBody jsonObj );

}
