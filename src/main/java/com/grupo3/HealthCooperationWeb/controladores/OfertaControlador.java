
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/oferta")
public class OfertaControlador {
    
    @GetMapping("/editar/{id}")
    public String editarAgenda(ModelMap modelo, HttpSession session) {
  

            return "redirect: /dashboard";
        
    }
    @PostMapping("/editar/{id}") 
    public String ediar(ModelMap modelo, HttpSession session) {
      
            return "redirect: /dashboard";
        
    }
    
}
