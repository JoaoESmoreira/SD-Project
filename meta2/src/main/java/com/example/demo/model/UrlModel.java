package com.example.demo.model;

import java.io.Serializable;

public class UrlModel implements Serializable {
    private String url;
    private String title;
    private String paragraph;
    public UrlModel() {}
    public UrlModel(String url, String title, String paragraph) {
        this.url = url;
        this.title = title;
        this.paragraph = paragraph;
    }

    public String getTitle() {
        return title;
    }

    public String getParagraph() {
        return paragraph;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
