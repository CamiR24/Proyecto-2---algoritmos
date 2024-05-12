package com.furrymate.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Furrymate";  // Nombre del archivo HTML en 'src/main/resources/templates'
    }

    @GetMapping("/login")
    public String login() {
        return "Login";  // Nombre del archivo HTML en 'src/main/resources/templates'
    }

    @GetMapping("/signup")
    public String signup() {
        return "Sign up";  // Nombre del archivo HTML en 'src/main/resources/templates'
    }

    @GetMapping("/dog-register")
    public String dogsRegister() {
        return "registro";  // Nombre del archivo HTML en 'src/main/resources/templates'
    }
}
