package com.devarshi.Retrofitclient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitRequestApi {

    @FormUrlEncoded
    @POST("api/v1/get-videos-pagination")
    Call<Example> PostDataIntoServer(@Field("sort_by") String sort_by,@Field("video_loaded_ids") String video_loaded_ids,
                                     @Field("app_name") String app_name);
}
