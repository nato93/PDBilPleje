package dk.aau.hr.pdbilpleje.Payment.Network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.aau.hr.pdbilpleje.Payment.PaymentConstants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApiClient {

    public static Retrofit getInstance()  {
        Log.e("Building", "Retrofit");
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create()
                ).baseUrl(PaymentConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}
