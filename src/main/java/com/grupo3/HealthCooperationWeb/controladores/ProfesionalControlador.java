package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
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
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.AgendaServicio;
import com.grupo3.HealthCooperationWeb.servicios.EmailServicio;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import com.grupo3.HealthCooperationWeb.servicios.TurnoServicio;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

@RequestMapping("/profesionales")
public class ProfesionalControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;
    @Autowired
    private PacienteServicio pacienteServicio;
    @Autowired
    UsuarioServicio usuarioServicio;
    @Autowired
    private TurnoServicio turnoServ;
    @Autowired
    private EmailServicio emailServ;
    @Autowired
    private AgendaServicio servAgenda;

    // Listado de todos los pacientes
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo, HttpSession session) throws MyException {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        List<Paciente> pacientes = pacienteServicio.mostrarPacientes();
        modelo.addAttribute("pacientes", pacientes);

        return "panelProfesional.html";
    }

    // listado SOLO de SUS pacientes (con los que tiene o ha tenido algun turno):
    @GetMapping("/mis_pacientes")
    public String listarMisPacientes(ModelMap modelo, HttpSession session, String idProfesional) throws MyException {
        try {
            modelo.addAttribute("pacientes", pacienteServicio.listarPacientesXprof(idProfesional));
            modelo.put("Exito", "Lista de pacientes encontrada con exito");
            return "panelProfesional.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect:/dashboard";
        }

    }

    // Acceden pacientes y profesionales al perfil del profesional
    @GetMapping("/MiPerfil/{id}")
    public String vistaPerfilProfesional(@PathVariable("id") String id, ModelMap modelo) throws MyException {

        try {
            modelo.addAttribute("profesional", usuarioServicio.getOne(id));
            return "perfilProfesional.html";
        } catch (Exception e) {
            return "redirect:";
        }
    }

    @GetMapping("/turnoIndividual/{id}")
    public String panelTurno(@PathVariable("id") String id, ModelMap modelo, HttpSession session,
            RedirectAttributes redirectAttributes)throws MyException {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        modelo.addAttribute("prof", profesionalServicio.getOne(id));
        List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);

        
        if (semanas.isEmpty()) {
            redirectAttributes.addFlashAttribute("vacia", "No existen turnos para mosrtar. Disculpe las molestias");
         return "redirect:/turno/panel";
        } else {
            Collections.sort(semanas, (semana1, semana2) -> {
                Date fecha1 = semana1.getFechasYTurnos().keySet().iterator().next();
                Date fecha2 = semana2.getFechasYTurnos().keySet().iterator().next();
                return fecha1.compareTo(fecha2);
            });
            modelo.addAttribute("semanas", semanas);
            return "turneroindividual.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR')")
    @GetMapping("/registrar") // *************REgistro de prof por el admin(LT)*****//
    public String registrar(ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);

            return "altaProfesional.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            return "altaProfesional.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR')")
    @PostMapping("/crear") // ruta para crear un usuario POST
    public String crearUsuario(HttpSession session, MultipartFile archivo, @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String dni,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2,
            @RequestParam String telefono, @RequestParam String direccion, @RequestParam String fecha_nac,
            String especialidad, String valorConsulta, ModelMap modelo) throws IOException, ParseException {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            profesionalServicio.registrarProfesional(archivo, nombre, apellido, dni, email, password, password2,
                    telefono, direccion, fecha_nac, especialidad, valorConsulta);
            modelo.put("exito", "!Profesional registrado con exito!");
            return registrar(modelo, session);

        } catch (MyException ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            modelo.put("error", ex.getMessage());
            return registrar(modelo, session);
        }

    }

    // listar todos los médicos activos(LT) panel del administrador
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR')")
    @GetMapping("/listar")
    public String listarProfesionales(ModelMap modelo, HttpSession session) {
        try {
            List<Profesional> users = profesionalServicio.listarProfesionales();
            modelo.addAttribute("users", users);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("id", logueado.getId());
            return "verProfesionales.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("id", logueado.getId());
            List<Profesional> users = profesionalServicio.listarProfesionales();
            modelo.addAttribute("users", users);
            modelo.put("error", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    // SOLO EL DOCTOR VE SU PERFIL
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            modelo.addAttribute("user", profesionalServicio.getOne(id));

            return "modificar_prof.html";

        } catch (Exception ex) {
            modelo.addAttribute("user", profesionalServicio.getOne(id));

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("error", ex.getMessage());
            return "modificar_prof.html";
        }

    }

    // EL ADMIN da de alta a doctores, pero SOLO los docs pueden modificar su perfil
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @PostMapping("/modificar/{id}") // ******ruta para modificar un usuario POST(LT)
    public String modificarUsusarios(MultipartFile archivo, @PathVariable("id") String id,
            @RequestParam String nombre, @RequestParam String apellido,
            String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, String telefono, String direccion, String descripcion,
            String fecha_nac, String especialidad, String valorConsulta, ModelMap modelo, HttpSession session)
            throws IOException, MyException, ParseException {

        try {
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", profesionalServicio.getOne(id));
            modelo.addAttribute("id", profesionalServicio.getOne(id).getId());
            profesionalServicio.modificarProfesional(id, archivo, nombre, apellido, dni, email, password, password2,
                    telefono, direccion, fecha_nac, especialidad, valorConsulta, descripcion);
            modelo.put("exito", "¡Profesional modificado con exito!");
            return "modificar_prof.html";
        } catch (MyException ex) {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("user", profesionalServicio.getOne(id));
            modelo.addAttribute("id", profesionalServicio.getOne(id).getId());
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);

            modelo.put("error", ex.getMessage());
            return "modificar_prof.html";
        }

    }

    // darse de baja con GET: puede darlo de baja el ADMIN o el mismo médico a sí
    // mismo
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR','ROLE_MODERADOR')")
    @GetMapping("/solicitarBaja/{id}")
    public String darseBaja(@PathVariable("id") String id, ModelMap modelo, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            Profesional logueado = (Profesional) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            emailServ.sendEmail(logueado.getNombre(), logueado.getApellido(),
                    logueado.getEmail(), logueado.getTelefono(),
                    logueado.getEspecialidad().toString(), "Solicita la baja de mis servicios en el Sitio");

            redirectAttributes.addFlashAttribute("exito", "!Solicitud de baja de servicios envíada!");
            profesionalServicio.darDeBajaProfesional(id);

            return "redirect:/profesionales/dashboard";
        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/profesionales/dashboard";
        }
    }

//    // darse de baja con POST
//    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR','ROLE_MODERADOR')")
//    @PostMapping("/solicitarBaja/{id}")
//    public String darseBaja(@PathVariable("id") String id, @RequestParam MultipartFile archivo, ModelMap modelo,
//            RedirectAttributes redirectAttributes, HttpSession session)
//            throws IOException {
//        try {
//            Profesional logueado = (Profesional) session.getAttribute("usuariosession");
//            modelo.addAttribute("log", logueado);
//            emailServ.sendEmail(logueado.getNombre(), logueado.getApellido(),
//                    logueado.getEmail(), logueado.getTelefono(),
//                    logueado.getEspecialidad().toString(), "Solicita la baja de mis servicios en el Sitio");
//
//            redirectAttributes.addFlashAttribute("exito", "!Solicitud de baja de servicios envíada!");
//            profesionalServicio.darDeBajaProfesional(id);
//
//            return "redirect:/pprofesionales/dashboard";
//        } catch (Exception ex) {
//            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
//            modelo.addAttribute("log", logueado);
//            redirectAttributes.addFlashAttribute("error", ex.getMessage());
//            return "redirect:/pprofesionales/dashboard";
//        }
//
//    }

    // Listar por especialidad, ordenando según precio consulta:
    // acceden todos
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
    // acceden todos
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

    @PostMapping("/agenda/{id}")
    // acceden todos
    public String agenda(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            return "";

        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            return "";

        }
    }
     @PostMapping("/valorar/{id}")
   
    public String valorar(@PathVariable("id") String id,  
            @RequestParam String valor, ModelMap modelo,  HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            System.out.println("valora: "+valor);
            profesionalServicio.valorar(id,valor);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
             redirectAttributes.addFlashAttribute("log", logueado);
             redirectAttributes.addFlashAttribute("exito", "Valoración Exitosa!");

            return "redirect:/profesionales/turnoIndividual/"+id;

        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
             redirectAttributes.addFlashAttribute("log", logueado);
                 redirectAttributes.addFlashAttribute("error", "Valorar no fue posible!");
             return "redirect:/profesionales/turnoIndividual/"+id;

        }
    }
    @GetMapping("/agenda/{id}")
    public String agendaa(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            return "";

        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            return "";

        }
    }
}
