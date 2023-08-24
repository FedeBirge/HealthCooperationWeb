
package com.grupo3.HealthCooperationWeb.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller

@RequestMapping("/turno")
public class TurnoControlador {
    
     @GetMapping("/panel") // ruta para el panel administrativo
    public String panelAdministrativo(ModelMap modelo) {
        
        return "turnero.html";
    }
    
}
