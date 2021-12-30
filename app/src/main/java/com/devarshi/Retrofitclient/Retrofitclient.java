package com.devarshi.Retrofitclient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofitclient {

    public static final String baseURL = "http://buzoadmin.appearth.xyz";
    public static final String baseURL1 = "https://buzosearch.superherowall.in";
    public static Retrofit retrofit = null;

    public static Retrofit getRetrofit(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitForTitles(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(baseURL1).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
