package com.example.coronareport;

public class CoronaData {

    String country;
    int todayCases;
    int todayDeaths;
    int active;

    public CoronaData() {
        this.country = "";
        this.todayCases = -1;
        this.todayDeaths = -1;
        this.active = -1;
    }

    CoronaData(String country,
               int todayCases,
               int deaths,
               int active)
    {
        this.country = country;
        this.todayCases = todayCases;
        this.todayDeaths = deaths;
        this.active = active;
    }

}
