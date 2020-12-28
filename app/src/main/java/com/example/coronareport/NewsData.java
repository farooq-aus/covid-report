package com.example.coronareport;

public class NewsData {

    String source;
    String title;
    String url;
    String imgSrc;
    String time;

    public NewsData(String source, String title, String url, String imgSrc, String time) {
        this.source = source;
        this.title = title;
        this.url = url;
        this.imgSrc = imgSrc;
        this.time = time;
    }
}
