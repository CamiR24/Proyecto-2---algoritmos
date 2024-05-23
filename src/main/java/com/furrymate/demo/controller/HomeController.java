package com.furrymate.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.ui.Model;

import com.furrymate.demo.model.Perro;
import com.furrymate.demo.service.DogService;

import jakarta.servlet.http.HttpSession;


@Controller
@SessionAttributes("perro")
public class HomeController {

    @Autowired
    private DogService dogService;

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("perro");
        return "redirect:/";
    }

    @GetMapping("/dog-register")
    public String dogsRegister(Model model) {
        model.addAttribute("perro", new Perro());  // Asegúrate de que el modelo Perro esté añadido al modelo
        return "registro";
    }

    @GetMapping("/recommended")
    public String recommend(Model model, HttpSession session) {
        Perro myDog = (Perro) session.getAttribute("perro");
        if (myDog == null) {
            return "redirect:/";  // Asegúrate de redireccionar o manejar cuando no hay perro en la sesión
        }

        List<Perro> allPerros = dogService.getPerrosDetails(); // Este método trae todos los perros disponibles
        Perro bestMatch = dogService.findBestMatch(myDog, allPerros);
        
        model.addAttribute("bestMatch", bestMatch);
        return "Recommended";  // Nombre del archivo HTML en 'src/main/resources/templates'
    }

    @GetMapping("/chat")
    public String chat(Model model) {
        return "Chat";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        Perro myDog = (Perro) session.getAttribute("perro");
        if (myDog == null) {
            return "redirect:/";  
        }
        return "Profile";
    }

    @GetMapping("/search")
    public String search(Model model) {
        List<Perro> allPerros = dogService.getPerrosDetails(); // Este método trae todos los perros disponibles
        return "Search";
    }

    @GetMapping("/vision")
    public String vision() {
        return "Vision";  
    }

    @GetMapping("/mission")
    public String mission() {
        return "Mission";  
    }

    @GetMapping("/about")
    public String about() {
        return "About";  
    }

    @GetMapping("/contact")
    public String contact() {
        return "Contact";  
    }
}
