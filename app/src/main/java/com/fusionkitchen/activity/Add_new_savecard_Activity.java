package com.fusionkitchen.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fusionkitchen.R;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.Savecard.addnewsavecard_model;
import com.fusionkitchen.model.Savecard.deletesavecard_model;
import com.fusionkitchen.model.myaccount.get_account_modal;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_new_savecard_Activity extends AppCompatActivity {

    /*-------------------Back Button Click--------------------*/
    RelativeLayout clikback;

    EditText card_number, card_date, card_name;
    AppCompatButton card_save_button;

    /*--------------Login details get SharedPreferences------------------*/
    SharedPreferences slogin;
    String user_id, strcard_number, strcard_date, cardmonth, cardyear;
    private long mLastClickTime = 0;
    private Dialog dialog;

    RelativeLayout error_popup;
    TextView error_msg;
    AppCompatButton try_again_card;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_new_savecard);





        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        overridePendingTransition(R.anim.enter, R.anim.exit);


        card_number = findViewById(R.id.card_number);
        card_date = findViewById(R.id.card_expr);
        card_save_button = findViewById(R.id.card_save_button);
        clikback = findViewById(R.id.clikback);
        error_popup = findViewById(R.id.error_popup);
        card_name = findViewById(R.id.card_name);
        card_date.addTextChangedListener(mDateEntryWatcher);
        try_again_card = findViewById(R.id.try_again_card);

        error_msg = findViewById(R.id.error_msg);
        /*--------------Login details get SharedPreferences------------------*/
        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));
        Log.e("user_walletpage_ids", "" + user_id);


        try_again_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error_popup.setVisibility(View.GONE);

            }
        });







        /*-------------------card number validation----------------*/


        card_number.addTextChangedListener(new TextWatcher() {
            private boolean spaceDeleted;

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                CharSequence charDeleted = s.subSequence(start, start + count);
                spaceDeleted = " ".equals(charDeleted.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                card_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
                card_number.removeTextChangedListener(this);
                int cursorPosition = card_number.getSelectionStart();
                String withSpaces = formatTextVisaMasterCard(editable);
                card_number.setText(withSpaces);
                card_number.setSelection(cursorPosition + (withSpaces.length() - editable.length()));

                if (spaceDeleted) {
                    card_number.setSelection(card_number.getSelectionStart() - 1);
                    spaceDeleted = false;
                }
                card_number.addTextChangedListener(this);
                if (card_number.getText().toString().length() == 19) {
                    card_date.requestFocus();
                }
            }
        });
        /*-------------------Back Button Click--------------------*/
        clikback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intenthome = new Intent(getApplicationContext(), Show_Save_Card_Activity.class);
                startActivity(intenthome);
            }
        });
        card_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //   strcard_number = card_number.getText().toString().replace(" ", "");
                // addsavecard(user_id,);
                cartvalidate();
            }
        });
        startService(new Intent(getBaseContext(),MyService.class));
    }

    /*-------------------card number validation----------------*/
    private String formatTextVisaMasterCard(CharSequence text) {
        StringBuilder formatted = new StringBuilder();
        int count = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (Character.isDigit(text.charAt(i))) {
                if (count % 4 == 0 && count > 0)
                    formatted.append(" ");
                formatted.append(text.charAt(i));
                ++count;
            }
        }
        return formatted.toString();
    }

    /*---------------------card date validation---------------*/
    private TextWatcher mDateEntryWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length() == 2 && before == 0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                    isValid = false;
                    card_date.setText("");
                    card_date.setError("Expire date is invalid.");
                } else {
                    working += "/";
                    card_date.setText(working);
                    card_date.setSelection(working.length());
                }
            } else if (working.length() == 5 && before == 0) {
                String enteredYear = working.substring(3);
                Log.e("enteredYear", "" + enteredYear);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
                Log.e("currentYear", "" + currentYear);
                if (Integer.parseInt(enteredYear) < currentYear) {
                    isValid = false;
                    card_date.setError("Expire date is invalid.");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    /*---------------------------Card validate ----------------------------------------------------*/
    private void cartvalidate() {
        if (TextUtils.isEmpty(card_number.getText().toString())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_number.getWindowToken(), 0);
            card_number.setError("Please fill out this field.");
        } else if (TextUtils.isEmpty(card_date.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_date.getWindowToken(), 0);
            card_date.setError("Please fill out this field.");
        } else if (TextUtils.isEmpty(card_name.getText())) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(card_name.getWindowToken(), 0);
            card_name.setError("Please fill out this field.");
        } else if (!TextUtils.isEmpty(card_number.getText().toString()) && !TextUtils.isEmpty(card_date.getText()) && !TextUtils.isEmpty(card_name.getText())) {
            loadingshow();
            strcard_number = card_number.getText().toString().replace(" ", "");
            strcard_date = card_date.getText().toString();
            StringTokenizer st = new StringTokenizer(strcard_date, "/");
            cardmonth = st.nextToken();
            cardyear = st.nextToken();
            Log.e("card_number", "" + strcard_number);
            Log.e("cardmonth", "" + cardmonth);
            Log.e("cardyear", "" + cardyear);

            Map<String, String> params = new HashMap<String, String>();
            params.put("cid", user_id);
            params.put("card_number", strcard_number);
            params.put("exp_month", cardmonth);
            params.put("exp_year", cardyear);
            params.put("name", card_name.getText().toString());

            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            Call<addnewsavecard_model> call = apiService.addnewsavecard(params);
            Log.d("paramsadd", "ok" + params);
            call.enqueue(new Callback<addnewsavecard_model>() {
                @Override
                public void onResponse(Call<addnewsavecard_model> call, Response<addnewsavecard_model> response) {
                    int statusCode = response.code();
                    Log.d("responsemsg1", "ok" + statusCode);
                    /*Get Login Good Response...*/
                    if (statusCode == 200) {
                        hideloading();
                        error_popup.setVisibility(View.GONE);
                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            View popupView = LayoutInflater.from(Add_new_savecard_Activity.this).inflate(R.layout.dialog_savenew_card, null);
                            final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            AppCompatButton discard_card = popupView.findViewById(R.id.discard_card);
                            ImageView close_img = popupView.findViewById(R.id.close_img);

                            discard_card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                    Intent intent = new Intent(Add_new_savecard_Activity.this, Show_Save_Card_Activity.class);
                                    startActivity(intent);
                                }
                            });

                            close_img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                    Intent intent = new Intent(Add_new_savecard_Activity.this, Show_Save_Card_Activity.class);
                                    startActivity(intent);

                                }
                            });


                            popupWindow.showAsDropDown(popupView, 0, 0);

                        } else {

                            error_popup.setVisibility(View.VISIBLE);
                            error_msg.setText(response.body().getMessage());
                            // Snackbar.make(Add_new_savecard_Activity.this.findViewById(android.R.id.content), response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        hideloading();
                        Snackbar.make(Add_new_savecard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<addnewsavecard_model> call, Throwable t) {
                    hideloading();
                    Snackbar.make(Add_new_savecard_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                    //  Toast.makeText(SupportlistActivity.this, R.string.somthinnot_right, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /*-------------------Loading Images------------------*/
    public void loadingshow() {
        dialog = new Dialog(Add_new_savecard_Activity.this);
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

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            startActivity(new Intent(getApplicationContext(), Show_Save_Card_Activity.class));
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Add Save Card Activity");
    }

}
