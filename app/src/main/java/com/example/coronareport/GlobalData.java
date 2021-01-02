package com.example.coronareport;

public class GlobalData {

    int worldCases;
    int worldDeaths;
    String cityName;
    String countryName;
    int countryCases;
    int countryRecovered;
    int countryCriticalCases;
    int countryDeaths;
    String flagUrl;

    public GlobalData() {
        this.worldCases = -1;
        this.worldDeaths = -1;
        this.cityName = "";
        this.countryName = "";
        this.countryCases = -1;
        this.countryRecovered = -1;
        this.countryCriticalCases = -1;
        this.countryDeaths = -1;
        this.flagUrl = "";
    }

    public GlobalData(int worldCases, int worldDeaths, String cityName, String countryName, int countryCases, int countryRecovered, int countryCriticalCases, int countryDeaths, String flagUrl) {
        this.worldCases = worldCases;
        this.worldDeaths = worldDeaths;
        this.cityName = cityName;
        this.countryName = countryName;
        this.countryCases = countryCases;
        this.countryRecovered = countryRecovered;
        this.countryCriticalCases = countryCriticalCases;
        this.countryDeaths = countryDeaths;
        this.flagUrl = flagUrl;
    }
}
