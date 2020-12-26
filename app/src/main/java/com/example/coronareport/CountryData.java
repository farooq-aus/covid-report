package com.example.coronareport;

import java.util.ArrayList;

public class CountryData extends CoronaData {

    int totalCases;
    int totalDeaths;
    int totalTests;
    int recovered;
    int critical;
    String flagSrc;
    ArrayList<Integer> casesTrend;
    ArrayList<Integer> deathsTrend;
    ArrayList<Integer> recoveriesTrend;

    public CountryData() {
        super();
        this.totalCases = -1;
        this.totalDeaths = -1;
        this.totalTests = -1;
        this.recovered = -1;
        this.critical = -1;
        this.flagSrc = "";
        casesTrend = new ArrayList<>();
        deathsTrend = new ArrayList<>();
        recoveriesTrend = new ArrayList<>();
    }

    public CountryData(String country, int todayCases, int deaths, int active, int totalCases, int totalDeaths, int totalTests, int recovered, int critical, String flagSrc, ArrayList<Integer> casesTrend, ArrayList<Integer> deathsTrend, ArrayList<Integer> recoveriesTrend) {
        super(country, todayCases, deaths, active);
        this.totalCases = totalCases;
        this.totalDeaths = totalDeaths;
        this.totalTests = totalTests;
        this.recovered = recovered;
        this.critical = critical;
        this.flagSrc = flagSrc;
        this.casesTrend = casesTrend;
        this.deathsTrend = deathsTrend;
        this.recoveriesTrend = recoveriesTrend;
    }
}