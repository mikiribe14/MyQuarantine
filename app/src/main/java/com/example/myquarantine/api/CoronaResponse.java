package com.example.myquarantine.api;

import com.example.myquarantine.data.Country;

public class CoronaResponse {
    private String Country;
    private int Deaths;
    private int Confirmed;

    private int TotalConfirmed;
    private  int TotalDeaths;

    public Country getCountry(){
        return new Country(Country, Confirmed,Deaths);
    }

    public Country getWorld(){
        return new Country("World", TotalConfirmed, TotalDeaths);
    }

}
