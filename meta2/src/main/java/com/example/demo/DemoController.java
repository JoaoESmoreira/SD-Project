package com.example.demo;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.ArrayList;


@Controller
@EnableScheduling
public class DemoController {
    @Autowired
    private Connection loginService;
/*
    @GetMapping("/")
    public String redirectRoot() {
        return "redirect:/search";
    }*/

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

    @Scheduled(fixedRate=1000)
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message onMessage() throws InterruptedException, RemoteException {
        System.out.println("Message received ");
        return new Message(loginService.getConnection().Stats());
    }

    // @Autowired
    // private SimpMessagingTemplate messagingTemplate;
    // @Scheduled(fixedRate=1000)
    // @MessageMapping("/message2")
    // public void sendPeriodicMessage() throws RemoteException {
    //     String destination = "/topic/message";
    //     String payload = "Hello, client!";
    //     System.out.println(loginService.getConnection().Stats());
    //     messagingTemplate.convertAndSend(destination, payload);
    // }
}
