package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio userServ;

    @GetMapping("/dashboard") 
    public String panelAdministrador(ModelMap modelo, HttpSession session) {
 
            return "panelAdmin.html";
       
    }

    @GetMapping("/registrar") 
    public String registrar(ModelMap modelo, HttpSession session) {
    
            return "altaUsuario.html";

        
    }

}
