package com.example.proy001.web;

import com.example.proy001.service.RegistroService;
import com.example.proy001.domain.Registro;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private RegistroService registroService;

    @GetMapping("/")
    public String inicio(Model model) {
        var registros = registroService.listarRegistros();
        model.addAttribute("registros", registros);
        return "index";
    }


    @GetMapping("/agregar")
    public String agregar(Registro registro) {
        return "modificar";
    }

    @GetMapping("/guardar")
    public String guardar(@Valid Registro registro, Errors errores) {
        if (errores.hasErrors()) {
            return "modificar";
        }

        registroService.guardar(registro);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String editar(Registro registro, Model model) {
        registro = registroService.encontrarRegistro(registro);
        model.addAttribute("registro", registro);
        return "modificar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(Registro registro) {
        registroService.eliminar(registro);
        return "redirect:/";
    }

    @GetMapping("/upload")
    public String mostrarFormularioCarga() {
        return "formularioCarga"; // Retorna la vista para mostrar el formulario
    }


    @PostMapping("/up")
    public String subirArchivo(@RequestParam("file") MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();

            String builder = "C://Cursos/" + file.getOriginalFilename();
            Path builderPath = Paths.get(builder);

            Files.write(builderPath,fileBytes);

            // Retornar una vista o redireccionar según necesites
            return "redirect:/";
        } catch (IOException e) {
            // Manejar el error de carga de archivo
            return "formularioCarga";
        }
    }



}