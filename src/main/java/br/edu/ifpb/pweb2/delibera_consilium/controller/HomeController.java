package br.edu.ifpb.pweb2.delibera_consilium.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping({"/", "/home"})
    public String showHomePage() {
        return "home";
    }

    @ModelAttribute("menu")
    public String selectMenu() {
        return "home";
    }
}
