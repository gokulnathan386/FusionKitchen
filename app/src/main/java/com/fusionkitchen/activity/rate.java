package com.fusionkitchen.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fusionkitchen.R;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class rate extends AppCompatActivity {


    private ReviewInfo reviewInfo;
    private ReviewManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        activateReviewInfo();

        findViewById(R.id.btn_rate_app).setOnClickListener(view -> startReviewFlow());


    }


    void activateReviewInfo() {

        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> managerInfoTask = manager.requestReviewFlow();
        managerInfoTask.addOnCompleteListener((task ->
        {

            if (task.isSuccessful()) {


                reviewInfo = task.getResult();


            } else {
                Toast.makeText(getApplicationContext(), "Faild", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    void startReviewFlow() {

        if (reviewInfo != null) {
            Task<Void> flow = manager.launchReviewFlow(rate.this, reviewInfo);

            flow.addOnCompleteListener(task ->
            {
                Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_SHORT).show();
            });


        }
    }
}
