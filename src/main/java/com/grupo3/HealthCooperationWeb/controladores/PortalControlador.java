package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
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
        
        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);

        return "index.html";
    }
}
