package com.vimalcvs.upgkhindi.rests;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vimalcvs.upgkhindi.Config;
import com.vimalcvs.upgkhindi.utils.MyApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {

    private static final int TIMEOUT = 10;
    private static final long cacheSize = 5 * 1024 * 1024;
    public static Retrofit retrofit;

    public static ApiInterface createAPI() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        if (retrofit == null) {
            Cache myCache = new Cache(MyApplication.getInstance().getCacheDir(), cacheSize);
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
            okHttpClientBuilder.cache(myCache);
            okHttpClientBuilder.addInterceptor(chain -> {
                Request request = chain.request();
                if (isNetworkConnected(MyApplication.getInstance())) {
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build();
                } else {
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                }
                return chain.proceed(request);
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.ADMIN_PANEL_URL + "/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }
        return retrofit.create(ApiInterface.class);

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
