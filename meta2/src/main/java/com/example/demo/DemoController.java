package com.example.demo;

import com.example.demo.model.Connection;
import com.example.demo.model.Search;
import com.example.demo.model.UrlModel;
import com.example.demo.model.Loginp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.rmi.RemoteException;
import java.util.ArrayList;


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

    @GetMapping("/register")
    public String RegistForm(Model model) {
        model.addAttribute("Registp", new Loginp());
        return "register";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/register-msg")
    public String Register(@ModelAttribute Loginp loginp, Model model) throws RemoteException {


        String s = loginService.getConnection().Register(loginp.getUsername(),loginp.getPassword());
        model.addAttribute("message", s);
        System.out.println(s);
        return "/register_msg";


    }
}
