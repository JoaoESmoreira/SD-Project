package com.example.demo;

import com.example.demo.model.Connection;
import com.example.demo.model.Search;
import com.example.demo.model.Loginp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
        //String s = connection.saySomething(search.getSearch());

        //System.out.println(s);
        //model.addAttribute("search", s);
        //return "searchresult";

        /*
        try {
            Registry registry = LocateRegistry.getRegistry(RMI_HOST, RMI_PORT);
            Inter loginService = (Inter) registry.lookup(RMI_NAME);
            String s = loginService.saySearch(search.getSearch());
            model.addAttribute("search", s);
            return "searchresult";

        } catch (RemoteException | NotBoundException e) {
            // lidar com possíveis erros de conexão RMI
            model.addAttribute("error", "Erro de conexão com o servidor de login");
            return "redirect:/";
        }
         */
        String s = loginService.getConnection().saySearch(search.getSearch());
        model.addAttribute("search", s);
        return "searchresult";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginp", new Loginp());
        model.addAttribute("error", false);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Loginp loginp, Model model) {


        if (loginp.getUsername().equals("user") && loginp.getPassword().equals("password")) {
            return "redirect:/";
        } else {
            model.addAttribute("error", true);
            return "login";
        }


    }


    @GetMapping("/register")
    public String RegistForm(Model model) {
        model.addAttribute("Registp", new Loginp());
        return "register";
    }

    @PostMapping("/register-msg")
    public String Register(@ModelAttribute Loginp loginp, Model model) throws RemoteException {


        String s = loginService.getConnection().Register(loginp.getUsername(),loginp.getPassword());
        model.addAttribute("message", s);
        System.out.println(s);
        return "/register_msg";


    }
}
