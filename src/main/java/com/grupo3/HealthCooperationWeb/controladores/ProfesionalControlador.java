package com.grupo3.HealthCooperationWeb.controladores;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;

@Controller
// @PreAuthorize("hasRole('ROLE_MODERADOR')")
@RequestMapping("/profesionales")
public class ProfesionalControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;
    @Autowired
    private PacienteServicio pacienteServicio;
    @Autowired
    UsuarioServicio usuarioServicio;

    // En el panel, el doc ve la lista de pacientes
    // Falta refinar esto para que sean solo SUS pacientes, no todos
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {
        List<Paciente> pacientes = pacienteServicio.mostrarPacientes();
        modelo.addAttribute("pacientes", pacientes);
        return "panelProfesional.html";
    }

    // Acceden pacientes y profesionales al perfil del profesional
    @GetMapping("/MiPerfil/{id}")
    public String vistaPerfilProfesional(@PathVariable("id") String id, ModelMap modelo) throws MyException {

        try {
            modelo.addAttribute("profesional", usuarioServicio.getOne(id));
            return "perfilProfesional.html";
        } catch (Exception e) {
            return "redirect: /panelProfesional.html";
        }
    }

    @GetMapping("/registrar") // *************REgistro de prof por el admin(LT)*****//
    public String registrar(ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("user", logueado);
            modelo.addAttribute("id", logueado.getId());
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);

            return "altaProfesional.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("user", logueado);
            modelo.addAttribute("id", logueado.getId());
            return "redirect:/profesionales/registrar";
        }
    }

    // listar todos los médicos activos(LT) panel del administrador
    @GetMapping("/listar")
    public String listarProfesionales(ModelMap modelo,HttpSession session) {
        try {
            List<Profesional> users = profesionalServicio.listarProfesionales();            
            modelo.addAttribute("users", users);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("user", logueado);
            modelo.addAttribute("id", logueado.getId());
            return "verProfesionales.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("user", logueado);
            modelo.addAttribute("id", logueado.getId());
            List<Profesional> users = profesionalServicio.listarProfesionales();
            modelo.addAttribute("users", users);
            modelo.put("error", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    // darse de baja con GET
    @GetMapping("/darseBaja/{id}")
    public String darseBaja(@PathVariable("id") String id, ModelMap modelo) {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("profesional", usuarioServicio.getOne(id));
            modelo.addAttribute("id", usuarioServicio.getOne(id).getId());
            profesionalServicio.darDeBajaProfesional(id);
            return "darseBaja.html";
        } catch (Exception ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            return "redirect: /dashboard";
        }
    }

    // darse de baja con POST
    @PostMapping("/darseBaja/{id}")
    public String darseBaja(@PathVariable("id") String id, @RequestParam MultipartFile archivo, ModelMap modelo)
            throws IOException {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("profesional", usuarioServicio.getOne(id));
            modelo.addAttribute("id", usuarioServicio.getOne(id).getId());
            profesionalServicio.darDeBajaProfesional(id);
            modelo.put("exito", "Se ha dado de baja su usuario.");

            return "darseBaja.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect: /dashboard";
        }

    }

    // Listar por especialidad, ordenando según precio consulta:
    @GetMapping("/especialidadesYPrecios")
    public String listarEspecialistasXPrecio(ModelMap modelo, String especialidad) {
        try {
            List<Profesional> profesionales = profesionalServicio.ordenarEspecialidadYPrecio(especialidad);
            modelo.addAttribute("profesionales", profesionales);
            return "listar_profesionales.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }

    // Listar por especialidad, SIN ordenar por precio de consulta
    @GetMapping("/especialidades")
    public String listarXEspecialidad(ModelMap modelo, Especialidad especialidad) {
        try {
            List<Profesional> profesionales = profesionalServicio.buscarPorEspecialidad(especialidad);
            modelo.addAttribute("profesionales", profesionales);
            return "listar_profesionales.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }

    // Listar por precio, sin tener en cuenta la especialidad
    @GetMapping("/nuestrosPrecios")
    public String listarXEspecialidad(ModelMap modelo) {
        try {
            List<Profesional> profesionales = profesionalServicio.ordenarPorValorConsulta();
            modelo.addAttribute("profesionales", profesionales);
            return "listar_profesionales.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }
}
