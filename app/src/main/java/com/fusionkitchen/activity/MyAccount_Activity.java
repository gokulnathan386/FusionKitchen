package com.fusionkitchen.activity;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;

import com.fusionkitchen.app.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import com.fusionkitchen.DBHelper.SQLDBHelper;
import com.fusionkitchen.R;
import com.fusionkitchen.check_internet.Internet_connection_checking;
import com.fusionkitchen.model.myaccount.get_account_modal;
import com.fusionkitchen.model.myaccount.update_account_modal;
import com.fusionkitchen.model.version_code_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccount_Activity extends AppCompatActivity {

    /*---------------------------check internet connection----------------------------------------------------*/
    boolean isShown = false, Connection;
    Internet_connection_checking int_chk;
    int bottonkey;

    /*---------------------------BottomNavigationView----------------------------------------------------*/
    BottomNavigationView bottomNav;

    /*---------------------------Back Button Click----------------------------------------------------*/
    ImageView back;

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    SQLDBHelper dbHelper;
    int cursor;


    /*---------------------------XML ID Call----------------------------------------------------*/
    TextView personal_edit, personal_save, password_edit, password_save;
    ImageView personal_close, password_close;
    EditText first_name, last_name, email, mobile_number, password, old_password, new_password, confirm_password;
    LinearLayout password_liner1, password_liner2;
    RelativeLayout password_layouts;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    SharedPreferences.Editor sloginEditor;
    String user_id, login_type_id;
    Dialog dialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_myaccount);






        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        overridePendingTransition(R.anim.enter, R.anim.exit);



        /*---------------------------check internet connection----------------------------------------------------*/
        int_chk = new Internet_connection_checking(MyAccount_Activity.this);
        Connection = int_chk.checkInternetConnection();
        if (!Connection) {
            MyAccount_Activity.ViewDialog alert = new MyAccount_Activity.ViewDialog();
            alert.showDialog(MyAccount_Activity.this);
        }


        /*---------------------------Back Button Click----------------------------------------------------*/
        //Back Boutton Click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*---------------------------Sql Lite DataBase----------------------------------------------------*/
        dbHelper = new SQLDBHelper(MyAccount_Activity.this);
        getContactsCount();

        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        login_type_id = (slogin.getString("type_of_login", null));
        Log.e("ideuser_ids1", "" + user_id);
        Log.e("ideuser_ids2", "" + login_type_id);


        /*---------------------------XML ID Call----------------------------------------------------*/

        personal_edit = findViewById(R.id.personal_edit);
        personal_save = findViewById(R.id.personal_save);
        password_edit = findViewById(R.id.password_edit);
        password_save = findViewById(R.id.password_save);


        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        mobile_number = findViewById(R.id.mobile_number);
        password = findViewById(R.id.password);
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);


        personal_close = findViewById(R.id.personal_close);
        password_close = findViewById(R.id.password_close);

        password_liner1 = findViewById(R.id.password_liner1);
        password_liner2 = findViewById(R.id.password_liner2);


        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        old_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        new_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        confirm_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        password_layouts = findViewById(R.id.password_layouts);
        /*---------------------------On Click----------------------------------------------------*/

        personal_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_edit.setVisibility(View.GONE);
                personal_save.setVisibility(View.VISIBLE);
                personal_close.setVisibility(View.VISIBLE);

                first_name.setEnabled(true);
                last_name.setEnabled(true);
                mobile_number.setEnabled(true);


            }
        });
        personal_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(first_name.getText().toString().trim().isEmpty()){
                    Toast.makeText(MyAccount_Activity.this, "Please Enter Your are FirstName", Toast.LENGTH_SHORT).show();
                }else if(last_name.getText().toString().trim().isEmpty()){
                    Toast.makeText(MyAccount_Activity.this, "Please Enter Your are LastName", Toast.LENGTH_SHORT).show();
                }else if(email.getText().toString().trim().isEmpty()){
                    Toast.makeText(MyAccount_Activity.this, "Please Enter Your are Email", Toast.LENGTH_SHORT).show();
                }else if(mobile_number.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(MyAccount_Activity.this, "Please Enter Your are Mobile number", Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(String.valueOf(mobile_number.getText().length())) < 11){
                    Toast.makeText(MyAccount_Activity.this, " Phone Number should be 11 digit", Toast.LENGTH_SHORT).show();
                }else{
                    updateaccountdetails(first_name.getText().toString(), last_name.getText().toString(), mobile_number.getText().toString(), user_id);
                }
            }
        });
        personal_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personal_edit.setVisibility(View.VISIBLE);
                personal_save.setVisibility(View.GONE);
                personal_close.setVisibility(View.GONE);

                first_name.setEnabled(false);
                last_name.setEnabled(false);
                mobile_number.setEnabled(false);
            }
        });
        password_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_edit.setVisibility(View.GONE);
                password_save.setVisibility(View.VISIBLE);
                password_close.setVisibility(View.VISIBLE);


                password_liner1.setVisibility(View.GONE);
                password_liner2.setVisibility(View.VISIBLE);


            }
        });
        password_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateaccountpassword(old_password.getText().toString(), new_password.getText().toString(), confirm_password.getText().toString(), user_id);
            }
        });


        password_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_edit.setVisibility(View.VISIBLE);
                password_save.setVisibility(View.GONE);
                password_close.setVisibility(View.GONE);

                password_liner1.setVisibility(View.VISIBLE);
                password_liner2.setVisibility(View.GONE);
            }
        });


        /*---------------------------Fresh Chat----------------------------------------------------*/

        FreshchatConfig config = new FreshchatConfig("67d078d8-604f-44c7-8807-c8a96810af62", "0604e381-8106-48e8-95bf-bc74bc8893fe");
        config.setDomain("msdk.in.freshchat.com");
        config.setCameraCaptureEnabled(true);
        config.setGallerySelectionEnabled(true);
        config.setResponseExpectationEnabled(true);
        Freshchat.getInstance(getApplicationContext()).init(config);

        /*---------------------------BottomNavigationView----------------------------------------------------*/

        bottomNav = findViewById(R.id.bottom_navigation);
        //  bottomNav.getMenu().setGroupCheckable(0, false, true);

        bottomNav.getMenu().findItem(R.id.home_account).setChecked(true);
        //  bottomNav.getOrCreateBadge(R.id.home_card).setNumber(5);

        if (cursor != 0) {
            bottomNav.getOrCreateBadge(R.id.home_card).setNumber(cursor);
        }
        bottomNav.getMenu().findItem(R.id.home_search).setVisible(false);
        bottomNav.getMenu().findItem(R.id.home_chat).setVisible(true);


        final Handler handler = new Handler();
        final int delay = 2500;

        handler.postDelayed(new Runnable() {
            public void run() {
                if(bottonkey == 2){
                    bottomNav.getMenu().findItem(R.id.home_card).setChecked(true);
                }else{
                    bottomNav.getMenu().findItem(R.id.home_account).setChecked(true);
                }

                handler.postDelayed(this, delay);
            }
        }, delay);


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_bottom:
                        Intent intenthome = new Intent(getApplicationContext(), Postcode_Activity.class);
                        startActivity(intenthome);
                        break;
                    case R.id.home_chat:
                        Freshchat.showConversations(getApplicationContext());
                        bottonkey = 3;
                        break;
                    case R.id.home_card:
                        bottonkey = 2;
                        /*Intent intentreview = new Intent(getApplicationContext(), Review.class);
                        startActivity(intentreview);*/
                        // Toast.makeText(getApplicationContext(), "Card", Toast.LENGTH_SHORT).show();

                        if (cursor != 0) {
                            Intent intentcard = new Intent(getApplicationContext(), Add_to_Cart.class);
                            startActivity(intentcard);
                        }else{
                            Toast.makeText(MyAccount_Activity.this,"Your cart is Empty!",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.home_account:
                        bottonkey = 3;
                       /* Intent intentmore = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentmore);*/
                        Toast.makeText(getApplicationContext(), "My Account", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });


        getaccountdetails(user_id, login_type_id);


    }


    /*---------------------------update account details----------------------------------------------------*/
    //get api values
    private void updateaccountdetails(final String fname, final String lname, final String phonen, final String cid) {
        loadingshow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", fname);
        params.put("phone", phonen);
        params.put("lname", lname);
        params.put("id", cid);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<update_account_modal> call = apiService.updateaccount(params);
        call.enqueue(new Callback<update_account_modal>() {
            @Override
            public void onResponse(Call<update_account_modal> call, Response<update_account_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        personal_edit.setVisibility(View.VISIBLE);
                        personal_save.setVisibility(View.GONE);
                        personal_close.setVisibility(View.GONE);

                        first_name.setEnabled(false);
                        last_name.setEnabled(false);
                        mobile_number.setEnabled(false);

                        getaccountdetails(user_id, login_type_id);


                        Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), response.body().getData(), Snackbar.LENGTH_LONG).show();

                    } else {
                        Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }


            @Override
            public void onFailure(Call<update_account_modal> call, Throwable t) {
                hideloading();
                Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }


    /*---------------------------update account password----------------------------------------------------*/
    //get api values
    private void updateaccountpassword(final String olds, final String news, final String cons, final String cid) {
        loadingshow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldpassword", olds);
        params.put("newpassword", news);
        params.put("confirmpassword", cons);
        params.put("cid", cid);

        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<update_account_modal> call = apiService.updateaccountpassword(params);
        call.enqueue(new Callback<update_account_modal>() {
            @Override
            public void onResponse(Call<update_account_modal> call, Response<update_account_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        password_edit.setVisibility(View.VISIBLE);
                        password_save.setVisibility(View.GONE);
                        password_close.setVisibility(View.GONE);

                        password_liner1.setVisibility(View.VISIBLE);
                        password_liner2.setVisibility(View.GONE);


                        getaccountdetails(user_id, login_type_id);


                        Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), response.body().getData(), Snackbar.LENGTH_LONG).show();

                    } else {
                        Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }


            @Override
            public void onFailure(Call<update_account_modal> call, Throwable t) {
                hideloading();
                Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }


    /*---------------------------get account details----------------------------------------------------*/
    //get api values
    private void getaccountdetails(final String cid, final String logintype) {
        loadingshow();
        Map<String, String> params = new HashMap<String, String>();
        params.put("cid", cid);
        params.put("typeoflogin", logintype);


        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);

        Log.d("getaccount","Cid" + cid + "Login Type " + logintype);

        Call<get_account_modal> call = apiService.getaccountdetails(params);
        call.enqueue(new Callback<get_account_modal>() {
            @Override
            public void onResponse(Call<get_account_modal> call, Response<get_account_modal> response) {
                int statusCode = response.code();
                Log.d("responsemsg1", "ok" + statusCode);
                /*Get Login Good Response...*/
                if (statusCode == 200) {
                    hideloading();
                    if (response.body().getStatus().equalsIgnoreCase("true")) {
                        if (response.body().getRESPONSE_CODE().equalsIgnoreCase("201")) {
                            first_name.setText(response.body().getData().getFname());
                            last_name.setText(response.body().getData().getLname());
                            email.setText(response.body().getData().getEmail());
                            mobile_number.setText(response.body().getData().getPhone());
                            password.setText(response.body().getData().getPassword());
                            password_layouts.setVisibility(View.VISIBLE);
                        } else if (response.body().getRESPONSE_CODE().equalsIgnoreCase("202")) {
                            first_name.setText(response.body().getData().getSsologin().getFname());
                            last_name.setText(response.body().getData().getSsologin().getLname());
                            email.setText(response.body().getData().getSsologin().getEmail());
                            mobile_number.setText(response.body().getData().getSsologin().getPhone());
                            password_layouts.setVisibility(View.GONE);
                        } else if (response.body().getRESPONSE_CODE().equalsIgnoreCase("203")) {
                            first_name.setText(response.body().getData().getSsologin().getFname());
                            last_name.setText(response.body().getData().getSsologin().getLname());
                            email.setText(response.body().getData().getSsologin().getEmail());
                            mobile_number.setText(response.body().getData().getSsologin().getPhone());
                            password.setText(response.body().getData().getLogindata().getPassword());
                            password_layouts.setVisibility(View.GONE);
                        }

                    } else {
                        Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), response.body().getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    hideloading();
                    Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<get_account_modal> call, Throwable t) {
                hideloading();
                Snackbar.make(MyAccount_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
            }


        });


    }

    /*---------------------------check internet connection----------------------------------------------------*/

    public class ViewDialog {

        public void showDialog(Activity activity) {
            Rect displayRectangle = new Rect();
            Window window = MyAccount_Activity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            final AlertDialog.Builder builder = new AlertDialog.Builder(MyAccount_Activity.this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);

            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button buttonOk = dialogView.findViewById(R.id.retry);
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int_chk = new Internet_connection_checking(MyAccount_Activity.this);
                    Connection = int_chk.checkInternetConnection();
                    if (Connection) {

                        alertDialog.dismiss();
                    }


                }
            });
            alertDialog.show();

        }
    }

    /*---------------------------Sql Lite DataBase----------------------------------------------------*/
    public int getContactsCount() {
        cursor = dbHelper.numberOfRows();
        Log.e("tmpStr10", "" + cursor);
        return cursor;
    }


    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {

        dialog = new Dialog(MyAccount_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);

        dialog.setContentView(R.layout.custom_loading_layout);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);


        Glide.with(MyAccount_Activity.this)
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
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Account Activity");
    }
}
