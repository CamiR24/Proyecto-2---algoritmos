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
    public String recommended(Model model, HttpSession session) {
        // Obtener el perro actual desde la sesión
        Perro currentUser = (Perro) session.getAttribute("perro");
    
        System.out.println("PRE Perro usuario: " + (currentUser != null ? currentUser.getNombre() : "No hay perro en la sesión"));
        
        // Si no hay perro en la sesión, redirigir a la página de inicio de sesión
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        // Obtener los detalles completos del perro desde la base de datos
        currentUser = dogService.getPerroByUsuarioAndPassword(currentUser.getUsuario(), currentUser.getPassword());
    
        System.out.println("POST Perro usuario: " + (currentUser != null ? currentUser.getNombre() : "No se pudo obtener el perro de la base de datos"));
        
        // Si no se pudo obtener el perro de la base de datos, redirigir a la página de inicio de sesión
        if (currentUser == null || currentUser.getUsuario() == null) {
            return "redirect:/login";
        }
        
        // Actualizar el perro en la sesión
        session.setAttribute("perro", currentUser);
    
        // Añadir el perro al modelo para la vista
        model.addAttribute("perro", currentUser);
    
        // Obtener todos los perros disponibles y sus detalles
        List<Perro> allPerros = dogService.getPerrosDetails();
        
        // Obtener listas de likes y dislikes del perro actual
        List<Perro> likes = currentUser.getLikes();
        List<Perro> dislikes = currentUser.getDislikes();
        
        // Encontrar el mejor match
        Perro bestMatch = dogService.findBestMatch(currentUser, allPerros, likes, dislikes);
    
        System.out.println("Recomendado: " + (bestMatch != null ? bestMatch.getNombre() : "No se encontró un buen match"));
        
        // Añadir el mejor match al modelo para la vista
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

    @GetMapping("/match")
    public String match(Model model) {
        return "Match";
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
