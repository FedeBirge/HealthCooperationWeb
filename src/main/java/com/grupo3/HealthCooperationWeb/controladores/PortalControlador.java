package com.grupo3.HealthCooperationWeb.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PortalControlador {

    // Controlador para levantar pagina de inicio
    @GetMapping("/")
    public String index(ModelMap modelo) {

        return "index.html";
    }
}
