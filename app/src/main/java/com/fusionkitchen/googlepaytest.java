package com.fusionkitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.googlepaylauncher.GooglePayEnvironment;
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher;

import org.jetbrains.annotations.NotNull;

public class googlepaytest extends AppCompatActivity {

    Button googlePayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlepaytest);

        PaymentConfiguration.init(this,"pk_test_9CnYE16SY0ju0M4GcnOBHzku00gC8VQDPF");

        googlePayButton = findViewById(R.id.googlePayButton);

        final GooglePayPaymentMethodLauncher googlePayLauncher = new GooglePayPaymentMethodLauncher(
                this,
                new GooglePayPaymentMethodLauncher.Config(
                        GooglePayEnvironment.Test,
                        "US",
                        "Fusion Kitchen"
                ),
                this::onGooglePayReady,
                this::onGooglePayResult
        );

        googlePayButton.setOnClickListener(
                v -> googlePayLauncher.present("GBP", 2)
        );



    }


    private void onGooglePayReady(boolean b) {
        Log.d("googlepayissue", String.valueOf(b));
        googlePayButton.setEnabled(b);
    }

    private void onGooglePayResult(@NotNull GooglePayPaymentMethodLauncher.Result result) {
        if (result instanceof GooglePayPaymentMethodLauncher.Result.Completed) {
            final String paymentMethodId =
                    ((GooglePayPaymentMethodLauncher.Result.Completed) result).getPaymentMethod().id;
        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Canceled) {

        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Failed) {

        }
    }



}