
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/agenda")
public class AgendaControlador {
    
    @GetMapping("/editar/{id}") // Vista principal para el Admin al Logearse (LT)
    public String editarAgenda(ModelMap modelo, HttpSession session) {
    
     
        return null;
    
     
    }
    @PostMapping("/editar/{id}") // Vista principal para el Admin al Logearse (LT)
    public String ediar(ModelMap modelo, HttpSession session) {
        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
  
            return "panelAdmin.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
  
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }
    
}
