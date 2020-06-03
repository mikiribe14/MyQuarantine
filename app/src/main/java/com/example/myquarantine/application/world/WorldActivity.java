package com.example.myquarantine.application.world;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.myquarantine.R;
import com.example.myquarantine.data.Country;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldActivity extends AppCompatActivity {
    private WorldViewModel worldViewModel;

    private TextView worldCases;
    private TextView worldDeaths;
    private TextView countryCases;
    private TextView countryDeaths;
    private Spinner spinner;

    private String countrySelected;
    private int lastSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.world);
        worldViewModel = new ViewModelProvider(this).get(WorldViewModel.class);

        worldCases = findViewById(R.id.worldCases);
        worldDeaths = findViewById(R.id.worldDeaths);
        countryCases = findViewById(R.id.countryActives);
        countryDeaths = findViewById(R.id.countryDeaths);
        setSpinner();

        worldViewModel.updateWorld();
        worldViewModel.updateCountryList();

        worldViewModel.getCountryNames().observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> countries){
                List<String> namesList = new ArrayList<String>();
                namesList.addAll(countries.keySet()); //get Name, that are the keys
                spinner.setAdapter(new ArrayAdapter<>(WorldActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, namesList));
              spinner.setSelection(getLastSelected());
            }
        });


        worldViewModel.getWorld().observe(this, new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
                worldCases.setText(String.valueOf(country.getTotalCases()));
                worldDeaths.setText(String.valueOf(country.getTotalDeath()));
            }
            //ho fa on create, like la silueta negra de bulbasur
        });


        worldViewModel.getCountry().observe(this, new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
                countryCases.setText(String.valueOf(country.getTotalCases()));
                countryDeaths.setText(String.valueOf(country.getTotalDeath()));
            }

        });

    }

    @Override
    protected void onStop() {
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor pEditor = prefs.edit();
        pEditor.putInt("lastSelected",lastSelected);
        pEditor.apply();
        super.onStop();
    }


    public void setSpinner() {
        //code from https://www.youtube.com/watch?v=JtAiHBbaxbw
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countrySelected = parent.getItemAtPosition(position).toString();
                worldViewModel.updateCountry(worldViewModel.getSlug(countrySelected));
                lastSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public int  getLastSelected(){
        SharedPreferences prefs = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        if (!prefs.contains("lastSelected")){ return 0; }; //if there is not, quit

        return prefs.getInt ( "lastSelected", 0);
    }


}