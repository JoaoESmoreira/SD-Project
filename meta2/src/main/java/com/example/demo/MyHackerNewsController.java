package com.example.demo;

import com.example.demo.model.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class MyHackerNewsController {
    private static final Logger logger = LoggerFactory.getLogger(MyHackerNewsController.class);
    @Autowired
    private Connection loginService;

    @GetMapping("/list")
    private List<HackerNewsItemRecord> hackerNewsTopStories() throws RemoteException {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://hacker-news.firebaseio.com/v0/topstories.json?auth=pretty";
        int[] storyIds = restTemplate.getForObject(url, int[].class);
        List<HackerNewsItemRecord> topStories = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10 && i < storyIds.length; i++) {
            String urlstory = "https://hacker-news.firebaseio.com/v0/item/" + storyIds[i] + ".json?auth=pretty";
            HackerNewsItemRecord story = restTemplate.getForObject(urlstory, HackerNewsItemRecord.class);
            topStories.add(story);
            assert story != null;
            loginService.getConnection().sayURL(story.url());
            //urls.add(story.url());
        }

        return topStories;

    }

}