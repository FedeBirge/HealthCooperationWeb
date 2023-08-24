
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio userServ;

    @GetMapping("/dashboard") // Vista principal para el Admin al Logearse (LT)
    public String panelAdministrador(ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("id", logueado.getId());
            return "panelAdmin.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("id", logueado.getId());
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }

    @GetMapping("/registrar") // **regsitro de usuario para el admin(LT)**//
    public String registrar(ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            return "altaUsuario.html";
        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("error", ex.getMessage());
            return "altaUsuario.html";

        }
    }

}