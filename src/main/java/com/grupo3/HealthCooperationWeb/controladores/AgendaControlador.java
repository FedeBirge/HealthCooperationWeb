package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.AgendaServicio;
import com.grupo3.HealthCooperationWeb.servicios.ObraSocialServicio;
import com.grupo3.HealthCooperationWeb.servicios.OfertaServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import com.grupo3.HealthCooperationWeb.servicios.TurnoServicio;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/agenda")
public class AgendaControlador {

    @Autowired
    private TurnoServicio servTurno;
    @Autowired
    private AgendaServicio servAgenda;
    @Autowired
    private OfertaServicio servOferta;
    @Autowired
    private ObraSocialServicio servObra;
    @Autowired
    private ProfesionalServicio profesionalServicio;

    // todos pueden ver la agenda del controlador profesional, pero esta agenda
    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR','ADMINISTRADOR')")
    @GetMapping("/verAgenda/{id}") // Vista principal para el Admin al Logearse (LT)
    public String verrAgenda(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);

        Collections.sort(semanas, (semana1, semana2) -> {
            Date fecha1 = semana1.getFechasYTurnos().keySet().iterator().next();
            Date fecha2 = semana2.getFechasYTurnos().keySet().iterator().next();
            return fecha1.compareTo(fecha2);
        });

        if (semanas.size() != 0) {

            // for (int i = 0; i < 3; i++) {
            // LocalDate fechaActual = LocalDate.now().plusDays(7*i);
            // int daysToAdd = DayOfWeek.TUESDAY.getValue() -
            // fechaActual.getDayOfWeek().getValue()-1;
            //
            // System.out.println("Lunes: " +fechaActual.plusDays(daysToAdd));
            // }

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("semanas", semanas);
            modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
            modelo.addAttribute("obras", servObra.listarObrasSociales());

            return "verAgenda.html";

        } else {
            modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
            modelo.addAttribute("obras", servObra.listarObrasSociales());
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("vacia", "¡Su Agenda no tiene semanas disponibles! Seleccione el botón Generar Agenda");
            return "verAgenda.html";

        }

    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/crear/{id}")
    public String crear(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        Oferta oferta = servOferta.obtenerOfertaxProf(id);
        if (oferta != null) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("oferta", oferta);

            return "crearAgenda.html";
        } else {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("vacia", "¡Oferta y disponibilidad no creada! Puede hacerlo aquí");
            TipoOferta[] tipos = TipoOferta.values();
            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);
            modelo.addAttribute("tipos", tipos);
            return "miOfertayDisponibilidad.html";
        }

    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @PostMapping("/crear/{id}")
    public String crearAgenda(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            List<AgendaSemanal> semanasprof = servAgenda.obtenerAgendaxProf(id);
            /// si la lista del prof está vacia, le creo una Agenda nueva
            if (semanasprof.isEmpty()) {
                ArrayList<AgendaSemanal> semanas = servAgenda.crearAgenda(id);
                profesionalServicio.asignarAgenda(id, semanas);
                modelo.addAttribute("semanas", semanas);
                modelo.put("exito", "¡Agenda generada con exito!");
            } else { // sino agrego semanas
                // profesionalServicio.agregarSemanas(id);
                // modelo.addAttribute("semanas", semanas);
                modelo.put("exito", "¡Agenda generada con exito!");
            }

            return "verAgenda.html";
        } catch (MyException e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);

            modelo.put("error", e.getMessage());
            return crear(id, modelo, session);
        }
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/editar/{id}") // Vista principal para el Admin al Logearse (LT)
    public String editarAgenda(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {
        try {
            List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
            Collections.sort(semanas, (semana1, semana2) -> {
                Date fecha1 = semana1.getFechasYTurnos().keySet().iterator().next();
                Date fecha2 = semana2.getFechasYTurnos().keySet().iterator().next();
                return fecha1.compareTo(fecha2);
            });

            if (semanas.size() != 0) {

                EstadoTurno[] estados = EstadoTurno.values();
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("semanas", semanas);
                modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
                modelo.addAttribute("obras", servObra.listarObrasSociales());
                modelo.addAttribute("estados", estados);
                  
                return "editarAgenda.html";

            } else {

                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.put("vacia", "¡Su Agenda no ha sido creada! Seleccione el botón Generar Agenda");
                return "editarAgenda.html";

            }
        } catch (MyException ex) {
            List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
            Collections.sort(semanas, (semana1, semana2) -> {
                Date fecha1 = semana1.getFechasYTurnos().keySet().iterator().next();
                Date fecha2 = semana2.getFechasYTurnos().keySet().iterator().next();
                return fecha1.compareTo(fecha2);
            });

            if (semanas.size() != 0) {

                EstadoTurno[] estados = EstadoTurno.values();
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("semanas", semanas);
                modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
                modelo.addAttribute("obras", servObra.listarObrasSociales());
                modelo.addAttribute("estados", estados);
                return editarAgenda(id, modelo, session);
            }
        }
        return null;
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @PostMapping("/editar/{id}") // Vista principal para el Admin al Logearse (LT)
    public String ediar(@PathVariable("id") String id, ModelMap modelo,
            HttpSession session, @RequestParam("turnoList") ArrayList<String> turnoList) throws MyException {

        ArrayList<Turno> turnos = new ArrayList();

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            for (String turno : turnoList) {
                String[] partes = turno.split(",");

                Turno tur = new Turno();
                tur.setId(partes[0]);
                tur.setEstado(servTurno.pasarStringEstado(partes[1]));
                turnos.add(tur);
            }

            servTurno.actualizarEstados(turnos);
            modelo.put("exito", "Estados cambiados!!");
            return verrAgenda(id, modelo, session);
        } catch (MyException e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            modelo.put("error", e.getMessage());
            return editarAgenda(id, modelo, session);
        }
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/editarActual/{id}") 
    public String editarActual(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {
        try {
            List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
            semanas = servAgenda.obtenerSemanaActual(id, semanas);

            Collections.sort(semanas, (semana1, semana2) -> {
                Date fecha1 = semana1.getFechasYTurnos().keySet().iterator().next();
                Date fecha2 = semana2.getFechasYTurnos().keySet().iterator().next();
                return fecha1.compareTo(fecha2);
            });
       
            if (semanas.size() != 0) {

                EstadoTurno[] estados = EstadoTurno.values();
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("semanas", semanas);
                modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
                modelo.addAttribute("obras", servObra.listarObrasSociales());
                modelo.addAttribute("estados", estados);
            
                return "editarAgenda.html";

            } else {

                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.put("vacia", "¡No existe informacion para la semana actual");
                return "editarAgenda.html";

            }
        } catch (MyException ex) {
            List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
            Collections.sort(semanas, (semana1, semana2) -> {
                Date fecha1 = semana1.getFechasYTurnos().keySet().iterator().next();
                Date fecha2 = semana2.getFechasYTurnos().keySet().iterator().next();
                return fecha1.compareTo(fecha2);
            });

            if (semanas.size() != 0) {

                EstadoTurno[] estados = EstadoTurno.values();
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("semanas", semanas);
                modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
                modelo.addAttribute("obras", servObra.listarObrasSociales());
                modelo.addAttribute("estados", estados);
                return editarAgenda(id, modelo, session);
            }
        }
        return null;
    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/agregar/{id}")
    public String agregarsemamas(@PathVariable("id") String id, ModelMap modelo, HttpSession session,
            RedirectAttributes redirectAttributes)
            throws MyException {

        Oferta oferta = servOferta.obtenerOfertaxProf(id);
        if (oferta != null) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("oferta", oferta);
            redirectAttributes.addFlashAttribute("exito", "¡Se agregaron 3 semanas a su agenda, de acuerdo a su disponibilidad");
                profesionalServicio.asignarAgenda(id, servAgenda.agregarSemanas(id));
          return "redirect:/profesionales/dashboard";
        } else {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("vacia", "¡Oferta y disponibilidad no creada! Puede hacerlo aquí");
            TipoOferta[] tipos = TipoOferta.values();
            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);
            modelo.addAttribute("tipos", tipos);
            return "miOfertayDisponibilidad.html";
        }

    }

    // solo la ve el doctor con este id
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/eliminarAgenda/{id}") // ruta para eliminar (no tiene una vista, es para un boton
    public String eliminarAgenda(@PathVariable("id") String id, ModelMap modelo) {

        try {
            servAgenda.eliminarAgenda(id);
            modelo.put("exito", "Agenda eliminada con exito!");
            return "redirect:";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/";
        }

    }

}
