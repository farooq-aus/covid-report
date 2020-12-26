package com.example.coronareport;

public class CountryData extends CoronaData {

    int totalCases;
    int totalDeaths;
    int totalTests;
    int recovered;
    int critical;
    int casesPerMillion;
    int deathsPerMillion;
    int testsPerMillion;
    String flagSrc;

    public CountryData() {
        super();
        this.totalCases = -1;
        this.totalDeaths = -1;
        this.totalTests = -1;
        this.recovered = -1;
        this.critical = -1;
        this.casesPerMillion = -1;
        this.deathsPerMillion = -1;
        this.testsPerMillion = -1;
        this.flagSrc = "";
    }

    public CountryData(String country, int todayCases, int deaths, int active, int totalCases, int totalDeaths, int totalTests, int recovered, int critical, int casesPerMillion, int deathsPerMillion, int testsPerMillion, String flagSrc) {
        super(country, todayCases, deaths, active);
        this.totalCases = totalCases;
        this.totalDeaths = totalDeaths;
        this.totalTests = totalTests;
        this.recovered = recovered;
        this.critical = critical;
        this.casesPerMillion = casesPerMillion;
        this.deathsPerMillion = deathsPerMillion;
        this.testsPerMillion = testsPerMillion;
        this.flagSrc = flagSrc;
    }
}