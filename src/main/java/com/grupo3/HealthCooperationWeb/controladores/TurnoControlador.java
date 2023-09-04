package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.AgendaServicio;
import com.grupo3.HealthCooperationWeb.servicios.ObraSocialServicio;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import com.grupo3.HealthCooperationWeb.servicios.TurnoServicio;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

@RequestMapping("/turno")
public class TurnoControlador {

    @Autowired
    private TurnoServicio turnoServ;
    @Autowired
    private AgendaServicio servAgenda;
    @Autowired
    private PacienteServicio pacServ;
    
 @Autowired
    private ProfesionalServicio profesionalServicio;
   @Autowired
    private ObraSocialServicio obraServ;

    @GetMapping("/panel") // ruta para el panel administrativo
    public String panelturnos(ModelMap modelo, HttpSession session) throws MyException {
        
         List<Profesional> profes = profesionalServicio.listarProfesionales();
         Especialidad[] especialidades = Especialidad.values();
          List<ObraSocial> obras = obraServ.listarObrasSociales();
            modelo.addAttribute("obras", obras);
         modelo.addAttribute("profes", profes);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("especialidades", especialidades);
            
            if(profes.isEmpty()){
                modelo.put("vacia", "No existen profesionales para mosrtar. Disculpe las molestias");
            }
        return "turnero.html";
    }
    
       @GetMapping("/misturnos/{id}") // ruta para el panel administrativo
    public String misTurnos(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        List<Turno> turnos = turnoServ.misTurnos(id);  // el paciente ve sus turnos
        modelo.addAttribute("turnos", turnos);

        return "verTurnos.html";
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/cancelarTodos/{id}") // ruta para el panel administrativo
    public String cancelarmisTurnos(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        turnoServ.cancelarTurnosProf(id);
        modelo.put("Exito", "Estados cancelados!!");
        List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
        modelo.addAttribute("semanas", semanas);
        return "verAgenda.html";
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/cancelarSemana/{id}") // ruta para el panel administrativo
    public String cancelarSemana(@PathVariable("id") String id, ModelMap modelo, HttpSession session)
            throws MyException {

        try {
            List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            if (semanas.size() != 0) {
                semanas = servAgenda.obtenerSemanaActual(id, semanas);
                turnoServ.cancelarTurnosSemana(id, semanas);

                modelo.addAttribute("semanas", semanas);
                modelo.addAttribute("log", logueado);
                modelo.put("Exito", "Estados cancelados!!");
                return "verAgenda.html";
            } else {
                modelo.addAttribute("log", logueado);
                modelo.put("vacia", "¡Su Agenda no ha sido creada! Seleccione el botón Generar Agenda");
                return "verAgenda.html";

            }
        } catch (MyException ex) {
            List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
            Collections.sort(semanas, (semana1, semana2) -> {
                Date fecha1 = semana1.getFechasYTurnos().keySet().iterator().next();
                Date fecha2 = semana2.getFechasYTurnos().keySet().iterator().next();
                return fecha1.compareTo(fecha2);
            });

            if (semanas.size() != 0) {

                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("semanas", semanas);

                return "verAgenda.html";
            }
        }
        return null;
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @PostMapping("/cancelarSemana/{id}") // ruta para el panel administrativo
    public String cancelarSemanaActual(@PathVariable("id") String id, ModelMap modelo, HttpSession session)
            throws MyException {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);

        if (semanas.size() != 0) {
            semanas = servAgenda.obtenerSemanaActual(id, semanas);
            turnoServ.cancelarTurnosSemana(id, semanas);

            modelo.addAttribute("semanas", semanas);
            modelo.addAttribute("log", logueado);
            modelo.put("Exito", "Estados cancelados!!");
            return "verAgenda.html";
        } else {
            modelo.addAttribute("log", logueado);
            modelo.put("vacia", "¡Su Agenda no ha sido creada! Seleccione el botón Generar Agenda");
            return "verAgenda.html";

        }
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/verHoy/{id}") // ruta para el panel administrativo
    public String verHoy(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        Map<Turno, Paciente> turnos = pacServ.mapearPacientesXprofHoy(id);

        List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
        modelo.addAttribute("turnos", turnos);
        return "verTurnos.html";
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/verSemana/{id}") // ruta para el panel administrativo
    public String verSemana(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
        semanas = servAgenda.obtenerSemanaActual(id, semanas);
        Map<Turno, Paciente> turnos = pacServ.mapearPacientesXprofSemana(id, semanas);

        modelo.addAttribute("turnos", turnos);
        return "verTurnos.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/verTodos/{id}") // ruta para el panel administrativo
    public String vertodos(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
//        List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
//        semanas = servAgenda.obtenerSemanaActual(id, semanas);
//        Map<Turno, Paciente> turnos = pacServ.mapearPacientesXprofTodos(id, semanas);

//        modelo.addAttribute("turnos", turnos);
        return "verTurnos.html";
    }
}
