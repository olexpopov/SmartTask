package com.proj.Management.controllers;

import org.springframework.stereotype.Controller; // For the @Controller annotation
import org.springframework.web.bind.annotation.GetMapping; // For the @GetMapping annotation
import org.springframework.web.bind.annotation.RequestParam; // For the @RequestParam annotation
import org.springframework.ui.Model; // For the Model object parameter

@Controller
public class GreetingControllers {
    @GetMapping("/greeting")
    public String greeting( Model model) {
        // Add data to the model so the view can access it
        model.addAttribute("name", "this is a main page ");
        // Return the name of the view template (e.g., greeting.html)
        return "greeting";
    }
    // Inside your GreetingControllers class...

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "This is the dedicated home page.");
        return "greeting"; // You can reuse the greeting.html template
    }
}
