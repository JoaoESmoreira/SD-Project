package com.example.demo;

import com.example.demo.model.Connection;
import com.example.demo.model.Search;
import com.example.demo.model.Loginp;
import com.example.demo.model.Url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.rmi.RemoteException;


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
        String s = loginService.getConnection().saySearch(search.getSearch());
        model.addAttribute("search", s);
        return "searchresult";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        // model.addAttribute("loginp", new Loginp());
        // model.addAttribute("error", false);

        return "login";
    }

    @GetMapping("/pointed-links")
    public String pointedToLink(Model model) {
        model.addAttribute("url", new Url());
        return "pointed-links";
    }

    @PostMapping("/pointed-links-result")
    public String pointedToLinkResult(Url url, Model model) throws RemoteException {
        String s = loginService.getConnection().pointToLink(url.getUrl());
        model.addAttribute("url", s);
        return "pointed-links-result";
    }


    /*@PostMapping("/login")
    public String login(@ModelAttribute Loginp loginp, Model model) throws RemoteException {
        // Need to register
        // LOGGED IN
        // Wrong password
        // No SearchBarrel Operational

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        Integer sessionLogin = null;
        if (session != null) {
            sessionLogin = (Integer) session.getAttribute("sessionLogin");
            session.setAttribute("sessionLogin", 1);
        }
        int c;
        if (sessionLogin == null) {
            c = 1;
            System.out.println("DOING");
        } else {
            c = sessionLogin + 1;
            System.out.println(sessionLogin);
        }
        if (c != 1) {
            System.out.println("ALREADY LOGGED IN");
        }
        assert session != null;
        session.setAttribute("sessionLogin", c);
        model.addAttribute("sessioncounter", c);

        String result = loginService.getConnection().Login(loginp.getUsername(), loginp.getPassword());
        System.out.println(result);
        switch (result) {
            case "Need to register" -> {
                model.addAttribute("error", true);
                return "redirect:/register";
            }
            case "LOGGED IN" -> {
                return "redirect:/";
            }
            case "Wrong password" -> {
                model.addAttribute("error", true);
                return "redirect:/login";
            }
            case "No SearchBarrel Operational" -> {
                model.addAttribute("error", true);
                return "redirect:/";
            }
        }
        return "redirect:/";
    }*/



    @GetMapping("/register")
    public String RegistForm(Model model) {
        model.addAttribute("Registp", new Loginp());
        model.addAttribute("error", false);
        return "register";
    }

    @PostMapping("/register")
    public String Register(@ModelAttribute Loginp loginp, Model model) {
        if (loginp.getUsername().equals("user") && loginp.getPassword().equals("password")) {
            return "redirect:/";
        } else {
            model.addAttribute("error", true);
            return "register";
        }
    }
}
