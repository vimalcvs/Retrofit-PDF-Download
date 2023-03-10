package com.vimalcvs.upgkhindi.rests;

import com.vimalcvs.upgkhindi.Config;
import com.vimalcvs.upgkhindi.callbacks.CallbackCategory;
import com.vimalcvs.upgkhindi.callbacks.CallbackChapter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface ApiInterface {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "Data-Agent: My Stream";
    String API_KEY = Config.API_KEY;

    @Headers({CACHE, AGENT})
    @GET("api/getCategory/?api_key=" + API_KEY)
    Call<CallbackCategory> getCategory();

    @Headers({CACHE, AGENT})
    @GET("api/getChapter/?api_key=" + API_KEY)
    Call<CallbackChapter> getChapter(@Query("id") String id, @Query("page") int page, @Query("count") int count);

}
