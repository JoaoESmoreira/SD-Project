package com.example.demo;

import com.example.demo.model.Search;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.rmi.RemoteException;

@SpringBootApplication
@Controller
public class DemoApplication {
	private static Inter connection;

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
		String s = connection.saySomething(search.getSearch());
		System.out.println(s);
		model.addAttribute("search", s);
		return "searchresult";
	}

	public static void main(String[] args) {
		connection = SpringApplication.run(DemoApplication.class, args).getBean(SearchModule.class);
	}
}
