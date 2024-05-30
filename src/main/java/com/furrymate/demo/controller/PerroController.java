package com.furrymate.demo.controller;

import com.furrymate.demo.model.*;
import com.furrymate.demo.service.DogService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/perros")
@SessionAttributes("perro")
public class PerroController {

    @Autowired
    private DogService dogService;

    private static final Logger logger = LoggerFactory.getLogger(PerroController.class);

    @PostMapping("/login")
    public String login(@RequestParam String usuario, @RequestParam String password, Model model, HttpSession session, HttpServletRequest request) {
        // Limpiar la sesión antes de iniciar una nueva sesión
        session.invalidate();
        session = request.getSession(true);  // Crear una nueva sesión

        Perro perro = dogService.getPerroByUsuarioAndPassword(usuario, password);
        System.out.println("Login - Perro obtenido: " + (perro != null ? perro.getNombre() : "No se encontró el perro con las credenciales dadas"));
        if (perro != null) {
            // Actualizar el perro en la sesión con los detalles completos
            session.setAttribute("perro", perro);
            Perro currentUser = (Perro) session.getAttribute("perro");
            System.out.println("Login - Perro actual: " + currentUser.getNombre());
            return "redirect:/recommended";
        } else {
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos.");
            return "redirect:/login";
        }
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String usuario, @RequestParam String password, HttpSession session, Model model) {
        if (usuario.isEmpty() || password.isEmpty()) {
            model.addAttribute("errorMessage", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            return "redirect:/signup"; // El nombre del archivo HTML para el registro debe ser correcto
        }

        // Guardar usuario y contraseña en la sesión para usarlos luego en el registro del perro
        session.setAttribute("usuario", usuario);
        session.setAttribute("password", password);

        return "redirect:/dog-register"; // Redirige al formulario de registro del perro
    }

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model, HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        String password = (String) session.getAttribute("password");
    
        if (usuario == null || password == null) {
            return "redirect:/signup"; // Redirige a la página de signup si no hay datos de usuario en la sesión
        }
    
        Perro perro = new Perro();
        model.addAttribute("perro", perro);
        return "registro"; // Muestra el formulario de registro del perro
    }

    @PostMapping("/registro")
    public String registrarPerro(@ModelAttribute Perro perro, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Error de validación en el formulario.");
            return "registro";
        }
        try {
            String usuario = (String) session.getAttribute("usuario");
            String password = (String) session.getAttribute("password");
            System.out.println("================> Usuario: " + usuario + ", Password: " + password);

            dogService.createPerro(perro, usuario, password);
            return "redirect:/recommended?success";
        } catch (Exception e) {
            logger.error("Error al crear perro: ", e);
            model.addAttribute("errorMessage", "Error al guardar el perro: " + e.getMessage());
            return "registro";
        }
    }

    @PostMapping("/like/{otherPerroUsuario}")
    public ResponseEntity<Perro> handleLike(@PathVariable String otherPerroUsuario, HttpSession session) {
        Perro currentUser = (Perro) session.getAttribute("perro");
        if (currentUser != null) {
            System.out.println("Current User: " + currentUser.getUsuario());
            Perro newRecommendation = dogService.handleLike(otherPerroUsuario, currentUser);
            return ResponseEntity.ok(newRecommendation);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @PostMapping("/dislike/{otherPerroUsuario}")
    public ResponseEntity<Perro> handleDislike(@PathVariable String otherPerroUsuario, HttpSession session) {
        Perro currentUser = (Perro) session.getAttribute("perro");
        if (currentUser != null) {
            System.out.println("Current User: " + currentUser.getUsuario());
            Perro newRecommendation = dogService.handleDislike(otherPerroUsuario, currentUser);
            return ResponseEntity.ok(newRecommendation);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }    

    @GetMapping("/details")
    @ResponseBody
    public List<Perro> getPerrosDetails() {
        return dogService.getPerrosDetails();
    }

}

