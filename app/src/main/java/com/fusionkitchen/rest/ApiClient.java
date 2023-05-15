package com.fusionkitchen.rest;


import android.content.Context;


import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final static String BASE_URL = "https://www.api-dev.fusionkitchen.co.uk";//Testing
        //private final static String BASE_URL = "https://www.dev.fusionkitchen.co.uk";//Live
        //private final static String BASE_URL = "https://www.api.fusionkitchen.co.uk";//Live

    public static ApiClient apiClient;
    private Retrofit retrofit = null;

    public static ApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    public Retrofit getClient() {
        return getClient(null);
    }


    private Retrofit getClient(final Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
