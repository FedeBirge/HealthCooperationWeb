package com.grupo3.HealthCooperationWeb.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @GetMapping("/dashboard") // Vista principal para el Admin al Logearse (LT)
    public String panelAdministrador(ModelMap modelo) {
        try {

            return "panelAdmin.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }

}
