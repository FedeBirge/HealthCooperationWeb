package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.AdminServicio;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio userServ;
    @Autowired
    private AdminServicio adminServicio;

    @GetMapping("/dashboard")
    public String panelAdministrador(ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        return "panelAdmin.html";

    }

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        return "altaUsuario.html";

    }

    @GetMapping("/verUsuario/{id}") // ruta para ver el perfil de un usuario
    public String verPerfilUsusario(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        modelo.addAttribute("user", userServ.getOne(id));
        return "perfil.html";
    }

    @GetMapping("/modificarRol/{id}") // ruta modificar el rol de un usuario
    public String modificarRol(@PathVariable("id") String id, ModelMap modelo, String rol) throws MyException {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("rol", roles);
            adminServicio.modificarRol(id, rol);
            modelo.put("éxito", "Se ha cambiado el rol");
            // completar ruta
            return "";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            // completar ruta
            return "redirect:/";
        }
    }

    @PostMapping("/modificarRol/{id}") // ruta modificar el rol de un usuario
    public String modificarRolPost(@PathVariable("id") String id, ModelMap modelo, String rol) throws MyException {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("rol", roles);
            adminServicio.modificarRol(id, rol);
            modelo.put("éxito", "Se ha cambiado el rol");
            // completar ruta
            return "";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            // completar ruta
            return "redirect:/";
        }
    }

}
