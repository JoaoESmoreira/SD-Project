package com.example.demo.model;

import java.io.Serializable;

public class Url implements Serializable {
    private String url;
    public Url() {}
    public Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
