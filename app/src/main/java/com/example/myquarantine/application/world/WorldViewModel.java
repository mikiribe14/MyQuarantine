package com.example.myquarantine.application.world;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myquarantine.api.CoronaRepository;
import com.example.myquarantine.data.Country;
import com.example.myquarantine.db.NoteRepository;

import java.util.HashMap;


public class WorldViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private CoronaRepository coronaRepository;

    public WorldViewModel(@NonNull Application application) {
        super(application);
        noteRepository = NoteRepository.getInstance(application);
        coronaRepository = CoronaRepository.getInstance();

    }

    LiveData<Country> getWorld() {
        return coronaRepository.getWorld();
    }

    LiveData<Country> getCountry() { return coronaRepository.getCountry(); }

    public LiveData<HashMap<String, String>> getCountryNames(){ return coronaRepository.getCountryList();}

    public String getSlug(String countryName){
        HashMap<String,String> countryList = coronaRepository.getCountryList().getValue();
        if (countryList.containsKey(countryName)) {
            return countryList.get(countryName);
        }
        return "";
    }

    public void updateCountry(String s)    {
        coronaRepository.updateCountry(s);
    }

    public void updateWorld() {
        coronaRepository.updateWorld();
    }

    public void updateCountryList(){
        coronaRepository.updateCountryList();
    }

}