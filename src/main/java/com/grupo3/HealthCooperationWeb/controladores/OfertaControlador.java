package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.ObraSocialServicio;
import com.grupo3.HealthCooperationWeb.servicios.OfertaServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oferta")
public class OfertaControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;
    @Autowired
    private ObraSocialServicio servObra;
    @Autowired
    private OfertaServicio servOferta;

    // solo el doc crea su oferta
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/crear/{id}")
    public String crear(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        try {
            TipoOferta[] tipos = TipoOferta.values();
            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("tipos", tipos);
            ArrayList<String> diasSeleccionados = new ArrayList<>();
            modelo.addAttribute("diasSeleccionados", diasSeleccionados);
            return "miOfertayDisponibilidad.html";
        } catch (MyException e) {
            TipoOferta[] tipos = TipoOferta.values();
            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("tipos", tipos);
            ArrayList<String> diasSeleccionados = new ArrayList<>();
            modelo.addAttribute("diasSeleccionados", diasSeleccionados);
            modelo.put("error", e.getMessage());
            return "miOfertayDisponibilidad.html";
        }

    }

    // solo el doc crea su oferta
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @PostMapping("/crear/{id}")
    public String crear(@PathVariable("id") String id, ModelMap modelo, HttpSession session,
            @RequestParam(required = false) ArrayList<String> diasSeleccionados,
            @RequestParam("horaInicial") String horaInicial,
            @RequestParam("horaFinal") String horaFinal, @RequestParam String duracion,
            String tipoOferta, @RequestParam String direccion,
            @RequestParam("selecciones") String selecciones) throws MyException {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            List<String> listaDeSelecciones = Arrays.asList(selecciones.split(","));

            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);
            // Asignar a diasDisponibles(Enum lista) los diasSeleccionados que traigo al
            // prof
            profesionalServicio.asignarDisponibilidad(id, diasSeleccionados);

            /// Asignar Oferta al prof con
            profesionalServicio.asignarOferta(id, horaInicial, horaFinal, duracion, tipoOferta, direccion,
                    listaDeSelecciones);
            ///

            modelo.put("exito", "¡Oferta y disponibilidad cargada con exito!");
            return verOferta(id, modelo, session);
        } catch (MyException e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);

            modelo.put("error", e.getMessage());
            return crear(id, modelo, session);
        }
    }

    // todos pueden ver la oferta
    @GetMapping("/verOferta/{id}")
    public String verOferta(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        Oferta oferta = servOferta.obtenerOfertaxProf(id);
        if (oferta != null) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("oferta", oferta);
            modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
            modelo.addAttribute("obras", oferta.getObrasSociales());
            return "verOferta.html";
        } else {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("vacia", "¡Oferta y disponibilidad no creada! Seleccione el botón Nueva Oferta");
            return "verOferta.html";
        }

    }

    // solo el doc edita su oferta
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        Oferta oferta = servOferta.obtenerOfertaxProf(id);
        if (oferta != null) {
            TipoOferta[] tipos = TipoOferta.values();
            List<ObraSocial> todas = servObra.listarObrasSociales();
            modelo.addAttribute("todas", todas);

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("tipos", tipos);
            ArrayList<String> diasSeleccionados = new ArrayList<>();
            modelo.addAttribute("diasSeleccionados", diasSeleccionados);

            modelo.addAttribute("oferta", oferta);
            modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
            modelo.addAttribute("obras", oferta.getObrasSociales());
            return "editarOferta.html";
        } else {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("vacia", "¡Oferta y disponibilidad no creada! No es posible editar");
            return "editarOferta.html";
        }

    }

    // solo el doc edita su oferta
    @PreAuthorize("hasAnyRole('ROLE_MODERADOR')")
    @PostMapping("/editar/{id}")
    public String editar(@PathVariable("id") String id, ModelMap modelo, HttpSession session,
            @RequestParam(required = false) ArrayList<String> diasSeleccionados,
            @RequestParam("horaInicial") String horaInicial,
            @RequestParam("horaFinal") String horaFinal, @RequestParam String duracion,
            String tipoOferta, @RequestParam String direccion,
            @RequestParam("selecciones") String selecciones) throws MyException {
        try {
            Oferta oferta = servOferta.obtenerOfertaxProf(id);
            List<String> listaDeSelecciones = Arrays.asList(selecciones.split(","));
            if (oferta != null) {
                profesionalServicio.asignarDisponibilidad(id, diasSeleccionados);
                profesionalServicio.getOne(id).setOferta(servOferta.modificarOferta(oferta.getId(), tipoOferta,
                        horaInicial, horaFinal, duracion, direccion, servObra.pasarObras(listaDeSelecciones)));
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("oferta", oferta);
                modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
                modelo.addAttribute("obras", oferta.getObrasSociales());
                modelo.put("exito", "¡Oferta y disponibilidad modificada con exito!");
                return "editarOferta.html";
            } else {

            }
        } catch (MyException e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);

            modelo.put("error", e.getMessage());
            return editar(id, modelo, session);
        }
        return editar(id, modelo, session);

    }
}
