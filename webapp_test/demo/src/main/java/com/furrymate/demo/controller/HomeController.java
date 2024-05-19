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

    @GetMapping("/dog-register")
    public String dogsRegister(Model model) {
        model.addAttribute("perro", new Perro());  // Asegúrate de que el modelo Perro esté añadido al modelo
        return "registro";
    }

    @GetMapping("/match")
    public String match(Model model, HttpSession session) {
        Perro myDog = (Perro) session.getAttribute("perro");
        if (myDog == null) {
            return "redirect:/";  // Asegúrate de redireccionar o manejar cuando no hay perro en la sesión
        }

        List<Perro> allPerros = dogService.getPerrosDetails(); // Asume que este método trae todos los perros disponibles
        Perro bestMatch = dogService.findBestMatch(myDog, allPerros);
        
        model.addAttribute("bestMatch", bestMatch);
        return "Profile";  // Nombre del archivo HTML en 'src/main/resources/templates'
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMessage", "Ha ocurrido un error inesperado. Por favor intenta nuevamente.");
        return "Error";  // Nombre del archivo HTML en 'src/main/resources/templates'
    }
}
