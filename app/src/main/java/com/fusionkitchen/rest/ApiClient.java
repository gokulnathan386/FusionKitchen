package com.fusionkitchen.rest;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

      private final static String BASE_URL = "https://www.api.fusionkitchen.co.uk";//Testing

    // private final static String BASE_URL = "https://www.dev.fusionkitchen.co.uk";//Live
    // private final static String BASE_URL2 = "https://www.api.fusionkitchen.co.uk";//Test

    public static ApiClient apiClient;
    private Retrofit retrofit = null;
    //  private Retrofit retrofit2=null;

    public static ApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }


//private static Retrofit storeRetrofit = null;

    public Retrofit getClient() {
        return getClient(null);
    }

   /* public Retrofit getClient2() {
        return getClient2(null);
    }*/


    private Retrofit getClient(final Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        // init cookie manager
        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(interceptor);

     /*   client.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                return chain.proceed(request);
            }
        });*/

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

   /* private Retrofit getClient2(final Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.addInterceptor(interceptor);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }*/


    //old
   /* public static final String BASE_URL = "https://www.dev.fusionkitchen.co.uk"; //liVE
    public static final String BASE_URL_new = "https://www.dev.fusionkitchen.co.uk/location/SK11-Macclesfield"; //liVE


    private static Retrofit retrofit = null;

    public static Retrofit getResponse() {
        final okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

//Log API Calling
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


//       okHttpClient.interceptors().add(logging);


        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }*/


}
