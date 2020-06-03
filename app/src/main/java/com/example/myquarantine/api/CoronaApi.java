package com.example.myquarantine.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CoronaApi {
    @GET("/world/total")
    Call<CoronaResponse> getWorldData();

    @GET("total/dayone/country/{country}/status/confirmed")
    Call<List<CoronaByCountry>> getConfirmedByCountry(@Path("country") String country);

    @GET("total/dayone/country/{country}/status/deaths")
    Call<List<CoronaByCountry>> getDeathsByCountry(@Path("country") String country);

    @GET("/countries")
    Call<List<CountryName>> getCountryList();

}
