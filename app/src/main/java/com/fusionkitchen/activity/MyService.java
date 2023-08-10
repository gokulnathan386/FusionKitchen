package com.fusionkitchen.activity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.fusionkitchen.model.App_download_record_Tracking;
import com.fusionkitchen.rest.ApiClient;
import com.fusionkitchen.rest.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service {

    SharedPreferences slogin;
    String user_id;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "SERVICE STRAT ", Toast.LENGTH_SHORT).show();

      /*  WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());*/

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        slogin = getSharedPreferences("myloginPreferences", MODE_PRIVATE);
        user_id = (slogin.getString("login_key_cid", null));


        if (user_id != null && !user_id.isEmpty()) {
            Appdownload_record(user_id,deviceId);
        }


        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void Appdownload_record(String user_id, String device_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("ip_address", device_id);
        ApiInterface apiService = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<App_download_record_Tracking> call = apiService.getappcountrecord(params);
        Log.e("Myservice", "" + params);
        call.enqueue(new Callback<App_download_record_Tracking>() {
            @Override
            public void onResponse(Call<App_download_record_Tracking> call, Response<App_download_record_Tracking> response) {
                int statusCode = response.code();
                Log.e("Myservice", "" + statusCode);
                if (statusCode == 200) {
                    if (response.body().getSTATUS().equalsIgnoreCase("true")) {



                    }
                }
            }

            @Override
            public void onFailure(Call<App_download_record_Tracking> call, Throwable t) {
                Log.e("errororder", "" + t);

            }
        });

    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "SERVICE STOP", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}