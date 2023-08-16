
package com.grupo3.HealthCooperationWeb.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @GetMapping("/dashboardAdmin")
    public String panelAdministrativo() {
        return "panelAdmin.html";
    }

}
