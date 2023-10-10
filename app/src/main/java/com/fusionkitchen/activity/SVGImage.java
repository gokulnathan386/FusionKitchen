package com.fusionkitchen.activity;

import android.content.Context;
import android.widget.ImageView;

import com.pixplicity.sharp.Sharp;

import okhttp3.*;
import java.io.IOException;
import java.io.InputStream;

public class SVGImage {

    private OkHttpClient httpClient;

    public void fetchSVG(Context context, String url, ImageView target) {

        if (httpClient == null) {

            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1024)) // 5 MB cache
                    .build();
        }


        Request request = new Request.Builder()
                .url(url)
                .build();


        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                target.setImageResource(android.R.drawable.stat_notify_error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream stream = response.body().byteStream();
                Sharp.loadInputStream(stream).into(target);
                stream.close();
            }
        });
    }
}