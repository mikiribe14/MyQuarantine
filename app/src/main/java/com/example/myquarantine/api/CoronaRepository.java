package com.example.myquarantine.api;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.myquarantine.data.Country;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoronaRepository {
    private static CoronaRepository instance;
    private MutableLiveData<Country> world;
    private MutableLiveData<Country> countries;
    private MutableLiveData<HashMap<String,String>> countryNames;
    public Country country;

    private CoronaRepository() {
        world = new MutableLiveData<>();
        countries = new MutableLiveData<>();
        countryNames = new MutableLiveData<>();
        country = new Country();
    }

    public static synchronized CoronaRepository getInstance() {
        if (instance == null) {
            instance = new CoronaRepository();
        }
        return instance;
    }

    public LiveData<Country> getWorld() {//canviar nom
        return world;
    }

    public LiveData<Country> getCountry() {//canviar nom
        return countries;
    }

    public LiveData<HashMap<String,String>> getCountryList(){
        return countryNames;
    }


    public void updateCountryList(){
        CoronaApi coronaApi = ServiceGenerator.getCoronaApi();
        Call<List<CountryName>> call = coronaApi.getCountryList();
        call.enqueue(new Callback<List<CountryName>>() {
            @Override
            public void onResponse(Call<List<CountryName>> call, Response<List<CountryName>> response) {
                if (response.code()==200){
                    HashMap<String,String> hashNames = new HashMap<>();
                    for (int i = 0; i < response.body().size()-1; i++){
                        Log.println(Log.ASSERT,"countries",response.body().get(i).getName());
                        hashNames.put(response.body().get(i).getName(),
                                         response.body().get(i).getSlug());
                    }
                    countryNames.setValue(hashNames);
                }
            }

            @Override
            public void onFailure(Call<List<CountryName>> call, Throwable t) {
                Log.e("Retrofit","Error in updating country list");
            }
        });

    }


    public void updateCountry(final String name){
        country.setName(name);
        CoronaApi coronaApi = ServiceGenerator.getCoronaApi();

        Call<List<CoronaByCountry>> callConfirmed = coronaApi.getConfirmedByCountry(name);
        callConfirmed.enqueue(new Callback<List<CoronaByCountry>>() {
            @Override
            public void onResponse(Call<List<CoronaByCountry>> call, Response<List<CoronaByCountry>> response) {
                if (response.code()==200){
                    int size = response.body().size();
                    if (size>0){
                        country.setTotalCases( response.body().get(size-1).getCases() );
                        countries.setValue(country);
                    } else{
                        country.setTotalCases(0);
                        countries.setValue(country);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<CoronaByCountry>> call, Throwable t) {
                Log.e("Retrofit","Error in updating country information");
                country.setTotalCases(0);
                countries.setValue(country);
            }
        });


        Call<List<CoronaByCountry>> callDeaths = coronaApi.getDeathsByCountry(name);
        callDeaths.enqueue(new Callback<List<CoronaByCountry>>() {
            @Override
            public void onResponse(Call<List<CoronaByCountry>> call, Response<List<CoronaByCountry>> response) {
                if (response.code()==200){
                    int size = response.body().size();
                    if (size>0){
                        country.setTotalDeath( response.body().get(size-1).getCases() );
                        countries.setValue(country);
                    } else{
                        country.setTotalDeath(0);
                        countries.setValue(country);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<CoronaByCountry>> call, Throwable t) {
                Log.e("Retrofit","Error in updating country information");
                country.setTotalDeath(0);
                countries.setValue(country);
            }
        });
    }


    public void updateWorld(){
        CoronaApi coronaApi = ServiceGenerator.getCoronaApi();
        Call<CoronaResponse> call = coronaApi.getWorldData();
        call.enqueue(new Callback<CoronaResponse>() {
            @Override
            public void onResponse(Call<CoronaResponse> call, Response<CoronaResponse> response) {
                if (response.code()==200){
                    world.setValue(response.body().getWorld());//this in corona response (the getWorld)
                }
            }

            @Override
            public void onFailure(Call<CoronaResponse> call, Throwable t) {
                Log.e("Retrofit","Error in updating world information");
            }
        });
    }

}
