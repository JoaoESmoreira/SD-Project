package com.example.demo;

import java.util.HashSet;

public class test {
    public static HashSet<String> urlSet;
    public static void main(String[] args) {
        urlSet = new HashSet<>();

        urlSet.add("here");
        urlSet.add("here1");
        urlSet.add("here2");

        System.out.println(urlSet.size());

        urlSet.clear();

        System.out.println(urlSet.isEmpty());
    }
}
