package com.example.demo;

import com.example.demo.model.Connection;
import com.example.demo.model.Search;
import com.example.demo.model.UrlModel;
import com.example.demo.model.Loginp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class DemoController {
    @Autowired
    private Connection loginService;

    @GetMapping("/")
    public String redirectRoot() {
        return "redirect:/search";
    }

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("search", new Search());
        return "search";
    }

    @PostMapping("/search-result")
    public String searchResult(@ModelAttribute Search search, Model model) throws RemoteException {
        ArrayList<UrlModel> searchResult = loginService.getConnection().saySearch(search.getSearch());
        model.addAttribute("search", searchResult);
        return "searchresult";
    }

    @GetMapping("/pointed-links")
    public String pointedToLink(Model model) {
        model.addAttribute("url", new UrlModel());
        return "pointed-links";
    }

    @PostMapping("/pointed-links-result")
    public String pointedToLinkResult(UrlModel url, Model model) throws RemoteException {
        ArrayList<UrlModel> pointedLinksResult = loginService.getConnection().pointToLink(url.getUrl());
        model.addAttribute("pointedLinks", pointedLinksResult);
        return "pointed-links-result";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    @GetMapping("/register")
    public String RegistForm(Model model) {
        model.addAttribute("regist", new Loginp());
        return "register";
    }

    @PostMapping("/register-result")
    public String Register(@ModelAttribute Loginp loginp, Model model) throws IOException {

        FileOutputStream fos = new FileOutputStream("users.txt", true);
        String str = loginp.getUsername() + ":" + loginp.getPassword() + "\n";
        fos.write(str.getBytes());
        fos.close();

        WebSecurityConfig securityConfig = new WebSecurityConfig();
        securityConfig.userDetailsService();

        model.addAttribute("regist", "registed");
        System.out.println(loginp.getPassword() + " " + loginp.getUsername());
        return "register_msg";
    }

    @GetMapping("/user")
    public String getUserStoriesForm() {
        return "user_stories_form";
    }

    @PostMapping("/user-urls")
    public String getUserStories(@RequestParam("username") String username, Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://hacker-news.firebaseio.com/v0/user/" + username + ".json?auth=pretty";
            HackerNewsUserRecord userRecord = restTemplate.getForObject(url, HackerNewsUserRecord.class);
            assert userRecord != null;
            List submittedStoryIds = userRecord.submitted();
            List<String> submittedStoriesURLS = new ArrayList<>();
            for (Object submittedStoryId : submittedStoryIds) {
                String urlStory = "https://hacker-news.firebaseio.com/v0/item/" + submittedStoryId + ".json?auth=pretty";
                HackerNewsItemRecord story = restTemplate.getForObject(urlStory, HackerNewsItemRecord.class);
                if (story != null) {
                    submittedStoriesURLS.add(story.url());
                    loginService.getConnection().sayURL(story.url());
                }
            }

            model.addAttribute("stories", submittedStoriesURLS);
            return "user_stories";
        } catch (Exception e) {
            // Handle the exception and return an appropriate error view
            return "error";
        }
    }

    @GetMapping("/list")
    public String hackerNewsTopStories( Model model) throws RemoteException {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://hacker-news.firebaseio.com/v0/topstories.json?auth=pretty";
        int[] storyIds = restTemplate.getForObject(url, int[].class);
        List<HackerNewsItemRecord> topStories = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10 && i < storyIds.length; i++) {
            String urlstory = "https://hacker-news.firebaseio.com/v0/item/" + storyIds[i] + ".json?auth=pretty";
            HackerNewsItemRecord story = restTemplate.getForObject(urlstory, HackerNewsItemRecord.class);
            topStories.add(story);
            if (story != null) {
                loginService.getConnection().sayURL(story.url());
                urls.add(story.url());
            }
        }
        model.addAttribute("stories", urls);
        return "top10stories";

    }



}
