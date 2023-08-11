package com.example.demo.model;

import java.io.Serializable;

public class Search implements Serializable {
    private String search;

    public Search () {}
    public Search (String seach) {
        this.search = seach;
    }

    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
}
