package com.example.myquarantine.data;

public class Country {
    private String name;
    private int totalCases;
    private int totalDeath;

    public Country (String name, Integer totalCases, Integer totalDeath){
        this.name = name;
        this.totalCases = totalCases;
        this.totalDeath = totalDeath;
    }
    public Country (){
        super();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public void setTotalDeath(int totalDeath) {
        this.totalDeath = totalDeath;
    }

    public String getName() {
        return name;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public int getTotalDeath() {
        return totalDeath;
    }

}
