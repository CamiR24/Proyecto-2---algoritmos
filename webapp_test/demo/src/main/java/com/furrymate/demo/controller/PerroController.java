package com.furrymate.demo.controller;

import com.furrymate.demo.model.*;
import com.furrymate.demo.repository.*;
import com.furrymate.demo.service.DogService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/perros")
public class PerroController {

    @Autowired
    private DogService dogService;

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("perro", new Perro());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarPerro(@ModelAttribute Perro perro, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Error de validación en el formulario.");
            return "registro";
        }
        try {
            dogService.createPerro(perro);
            return "redirect:/perros/registro?success";
        } catch (Exception e) {
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

