package com.grupo3.HealthCooperationWeb.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio userServ;
    @Autowired

    private ProfesionalServicio profesionalServicio;


    @GetMapping("/dashboard") // Vista principal para el Admin al Logearse (LT)
    public String panelAdministrador(ModelMap modelo) {
        try {
            List<Profesional> profesionales = profesionalServicio.listarProfesionales();
            List<Usuario> users = userServ.listarUsuarios();

            modelo.addAttribute("profesionales", profesionales);
            modelo.addAttribute("users", users);

            return "panelAdmin.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }

    // crear con GET
    @GetMapping("/crearUsuario")  // Boton en el dash de Admin(LT)
    public String crearProfesional(ModelMap modelo, HttpSession session) {

        List<Profesional> profesionales = profesionalServicio.listarProfesionales();
        modelo.addAttribute("profesionales", profesionales);
        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        return "registro.html";

    }

    // crear con POST
    @PostMapping("/crearUsuario") // POST del form del registro.html LT)
    public String crearUsuario(MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido,
            String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, String telefono, String direccion,
            String fecha_nac,
            String especialidad, String valorConsulta, ModelMap modelo, HttpSession session) {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");

            if (logueado.getRol().toString().equals("ADMINISTRADOR")) {
                profesionalServicio.registrarProfesional(archivo, nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac, especialidad, valorConsulta);
                modelo.put("exito", "¡Profesional registrado con exito!");
                return "redirect:/admin/dashboard";
            }
            else{
                userServ.crearUsuario(archivo, nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
                     modelo.put("exito", "¡Usuario registrado con exito!");  
                return "inicio.html";
            }
            }catch (MyException ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            
            return "registro.html";
        }

        }

    }
