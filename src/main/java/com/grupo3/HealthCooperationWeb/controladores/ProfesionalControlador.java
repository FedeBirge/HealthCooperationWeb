package com.grupo3.HealthCooperationWeb.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;

@Controller
// @PreAuthorize("hasRole('ROLE_MODERADOR')")
@RequestMapping("/profesionales")
public class ProfesionalControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;
    // Faltaría el endpoint para el escritorio tipo "/dashboard", no sé qué iría

    @GetMapping("/MiPerfil/{id}")
    public String vistaPerfilProfesional(@PathVariable("id") String id, ModelMap modelo) {
        profesionalServicio.ingresarMiPerfil(id);
        Profesional profesional = new Profesional();
        modelo.addAttribute("id", profesional);
        return "perfilProfesional.html";
    }

    @PostMapping("/crearProfesional")
    public String crearProfesional(MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam String telefono, @RequestParam String direccion,
            @RequestParam String fecha_nac, @RequestParam String rol,
            @RequestParam String especialidad, @RequestParam String valorConsulta, ModelMap modelo) throws MyException {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            if (Rol.ADMINISTRADOR != null) {
                profesionalServicio.registrarProfesional(nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac, rol, especialidad, valorConsulta);
                modelo.put("exito", "!Usuario registrado con exito!");
                return "registroProfesional.html";
            }

        } catch (MyException ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            System.out.println("Error de permisos para esta acción");
        }
        return "registro.html";

    }

    // darse de baja
    @PostMapping("/darseBaja/{id}")
    public String darseBaja(@PathVariable("id") String id, @RequestParam MultipartFile archivo, ModelMap modelo)
            throws MyException {

        if (Rol.MODERADOR != null) {
            profesionalServicio.darDeBajaProfesional(id);
            modelo.put("exito", "!Usuario registrado con exito!");
            return "darseBaja.html";
        }
        return "redirect:/darseBaja";

    }

    // mostrar por especialidad, ordenando según precio consulta:
    @GetMapping("/especialidades")
    public String listarEspecialistasXPrecio(ModelMap modelo, @ModelAttribute("mensaje") String mensaje,
            HttpSession session, String especialidad) {
        List<Profesional> profesionales = profesionalServicio.ordenarEspecialidadYPrecio(especialidad);
        modelo.addAttribute("profesionales", profesionales);

        if (mensaje != null) {
            modelo.addAttribute("mensaje", mensaje);
        }

        return "listaEspecialidadPrecio.html";
    }
}
