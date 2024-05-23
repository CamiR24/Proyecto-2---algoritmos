package com.furrymate.demo.controller;

import com.furrymate.demo.model.*;
import com.furrymate.demo.service.DogService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;

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
    public String login(@RequestParam String usuario, @RequestParam String password, Model model, HttpSession session) {
        Perro perro = dogService.getPerroByUsuarioAndPassword(usuario, password);
        if (perro != null) {
            session.setAttribute("perro", perro);
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

    @GetMapping("/details")
    @ResponseBody
    public List<Perro> getPerrosDetails() {
        return dogService.getPerrosDetails();
    }

}

