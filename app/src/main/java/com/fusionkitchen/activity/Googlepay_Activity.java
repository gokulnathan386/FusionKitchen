package com.fusionkitchen.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.fusionkitchen.R;
import com.fusionkitchen.app.MyApplication;
import com.fusionkitchen.model.gpay.getgpayclientscSecret_model;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import com.google.android.material.snackbar.Snackbar;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.googlepaylauncher.GooglePayEnvironment;
import com.stripe.android.googlepaylauncher.GooglePayLauncher;
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodOptionsParams;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
/*import com.stripe.jetbrains.annotations.NotNull;*/

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class Googlepay_Activity extends AppCompatActivity {
    // private View googlePayButton;
    private AppCompatButton googlePayButton;
    private Stripe stripe;
    String metdpasfullUrl, gpay_client_secret;
    GooglePayPaymentMethodLauncher googlePayLauncher;
    private PaymentSheet paymentSheet;

    PaymentSheet.CustomerConfiguration customerConfig;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTheme().applyStyle(R.style.OverlayThemeLime, true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_google_payment);
        /*---------------------------hind actionbar----------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        overridePendingTransition(R.anim.enter, R.anim.exit);




        PaymentConfiguration.init(Googlepay_Activity.this, "pk_test_9CnYE16SY0ju0M4GcnOBHzku00gC8VQDPF");

        stripe = new Stripe(
                Googlepay_Activity.this,
                Objects.requireNonNull("pk_test_9CnYE16SY0ju0M4GcnOBHzku00gC8VQDPF")
        );
        googlePayButton = findViewById(R.id.googlePayButton);

        googlePayLauncher = new GooglePayPaymentMethodLauncher(

                Googlepay_Activity.this,
                new GooglePayPaymentMethodLauncher.Config(
                        GooglePayEnvironment.Production,
                        "US",
                        "Fusion Kitchen"
                ),
                Googlepay_Activity.this::onGooglePayReady,
                Googlepay_Activity.this::onGooglePayResult


        );


        googlePayButton.setOnClickListener(
                v -> googlePayLauncher.present("GBP", 15)
        );

    }

    private void onGooglePayReady(boolean isReady) {

        Log.d("isReady", String.valueOf(isReady));
        googlePayButton.setEnabled(isReady);

    }

    private void onGooglePayResult(@NotNull GooglePayPaymentMethodLauncher.Result result) {


        Log.e("paymentresult", "" + result);
        if (result instanceof GooglePayPaymentMethodLauncher.Result.Completed) {
            // Payment details successfully captured.
            // Send the paymentMethodId to your server to finalize payment.
            final String paymentMethodId =
                    ((GooglePayPaymentMethodLauncher.Result.Completed) result).getPaymentMethod().id;
            Log.e("paymentMethodId", "" + paymentMethodId);


            Map<String, String> params = new HashMap<String, String>();
            params.put("total", "15");
            params.put("app_id", "0");
            params.put("fname", "Gokulnsthan");
            params.put("lname", "E");
            params.put("email", "gokulnathane@fusioninnovative.com");
            params.put("cid", "19797");
            params.put("payment_method_id", paymentMethodId);
            params.put("url", "demo2.fusionepos.co.uk");


            metdpasfullUrl = "restaurant-demo-2-watford-po8%20" + "/gppaymentProcess";
            //, "Bearer " + token
            //ApiInterface apiService = ApiClient.getInstance().getClient2().create(ApiInterface.class);
            ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
            retrofit2.Call<getgpayclientscSecret_model> call = apiService.getgpaylientsecret(metdpasfullUrl, params);
            Log.e("paramsintentpass", "" + params);
            call.enqueue(new retrofit2.Callback<getgpayclientscSecret_model>() {
                @Override
                public void onResponse(Call<getgpayclientscSecret_model> call, Response<getgpayclientscSecret_model> response) {
                    int statusCode = response.code();
                    Log.e("statusCode", "" + statusCode);
                    if (statusCode == 200) {

                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            gpay_client_secret = response.body().getData().getClient_secret();
                            Log.e("gpay_client_secret", "" + gpay_client_secret);

                            //googlePayLauncher.presentForPaymentIntent(gpay_client_secret);
                            //  Googlepay_Activity.this.runOnUiThread(() -> stripe.handleNextActionForPayment(Googlepay_Activity.this, gpay_client_secret));

                            final ConfirmPaymentIntentParams params =
                                    ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                            paymentMethodId,//'{{PAYMENT_METHOD_ID}}'
                                            gpay_client_secret,
                                            null);
                            //  new PaymentMethodOptionsParams.Card(cvc)
                            stripe.confirmPayment(Googlepay_Activity.this, params);
                            Log.e("sc_params", "" + params);
                        }
                    } else {

                        Snackbar.make(Googlepay_Activity.this.findViewById(android.R.id.content), "Client secret id not found", Snackbar.LENGTH_LONG).show();

                    }
                }


                @Override
                public void onFailure(retrofit2.Call<getgpayclientscSecret_model> call, Throwable t) {

                    Log.e("errorcode1", "" + t);
                    Snackbar.make(Googlepay_Activity.this.findViewById(android.R.id.content), R.string.somthinnot_right, Snackbar.LENGTH_LONG).show();
                }
            });


        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Canceled) {
            // User cancelled the operation
        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Failed) {
            // Operation failed; inspect `result.getError()` for the exception
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "" + requestCode);
        Log.e("retysdata", "" + data);
        Log.e("resultCode", "" + resultCode);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new Googlepay_Activity.PaymentResultCallback(Googlepay_Activity.this));
        /// Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
    }


    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<Googlepay_Activity> activityRef;

        PaymentResultCallback(@NonNull Googlepay_Activity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final Googlepay_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            Log.e("paymentstatus", "" + status);
            Log.e("paymentIntent", "" + paymentIntent);
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Log.e("paymentintentSucceeded", "" + paymentIntent.getId());

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                Log.e("paymentIntenterror1", "" + paymentIntent.getLastPaymentError().getMessage());

                Log.e("paymentIntenterror2", "" + paymentIntent.getLastErrorMessage());

                Log.e("paymentIntenterror3", "" + paymentIntent);


            } else if (status == PaymentIntent.Status.RequiresConfirmation) {

                Log.e("RequiresConfirmation5", "" + paymentIntent.getId());

            } else if (status == PaymentIntent.Status.RequiresAction) {
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final Googlepay_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.e("errormsg", "" + e.toString());
            // Payment request failed – allow retrying using the same payment method
            activity.runOnUiThread(() -> {
                Log.e("errormsg", "" + e.toString());
                //activity.displayAlert("Error", e.toString(), false);
                // showPopup(e.toString());
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Gpay Activity");
    }
}
