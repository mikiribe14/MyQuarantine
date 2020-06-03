package com.example.myquarantine.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://api.covid19api.com/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static CoronaApi coronaApi = retrofit.create(CoronaApi.class);

    public static CoronaApi getCoronaApi(){
        return coronaApi;
    }

}
