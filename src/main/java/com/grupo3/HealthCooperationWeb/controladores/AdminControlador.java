
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
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;

    // listar todos los profesionales ACTIVOS
    @GetMapping("/dashboardAdmin")
    public String listarProfesionales(ModelMap modelo) {
        try {
            List<Profesional> profesionales = profesionalServicio.listarProfesionales();
            modelo.addAttribute("profesionales", profesionales);
            return "listar_profesionales.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }

    // crear con GET
    @GetMapping("/crearProfesional")
    public String crearProfesional(ModelMap modelo) {
        Rol[] roles = Rol.values();
        modelo.addAttribute("roles", roles);
        return "registro.html";

    }

    // crear con POST
    @PostMapping("/crearProfesional")
    public String crearProfesional(MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam String telefono, @RequestParam String direccion,
            @RequestParam String fecha_nac,
            @RequestParam String especialidad, @RequestParam String valorConsulta, ModelMap modelo) throws MyException {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            profesionalServicio.registrarProfesional(archivo,nombre, apellido, dni, email, password, password2, telefono,
                    direccion, fecha_nac, especialidad, valorConsulta);
            modelo.put("exito", "¡Profesional registrado con exito!");
            return "registroProfesional.html";

        } catch (MyException ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            System.out.println("Error de permisos para esta acción");
            return "registro.html";
        }

    }
 
}
