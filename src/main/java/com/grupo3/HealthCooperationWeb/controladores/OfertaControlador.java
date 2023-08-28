package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import com.grupo3.HealthCooperationWeb.servicios.ObraSocialServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/editar/{id}")
    public String editarAgenda(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {
            TipoOferta[] tipos = TipoOferta.values();
            List<ObraSocial> obras = servObra.listarObrasSociales();
                   modelo.addAttribute("obras", obras);
            System.out.println("GET " + obras.toString());
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("tipos", tipos);
            modelo.addAttribute("diasSeleccionados", new ArrayList<String>());

     

            return "miOfertayDisponibilidad.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("diasSeleccionados", new ArrayList<String>());
            List<ObraSocial> obras = servObra.listarObrasSociales();
            modelo.addAttribute("obras", obras);

            modelo.put("error", e.getMessage());
            return "miOfertayDisponibilidad.html";
        }

    }

    @PostMapping("/editar/{id}")
    public String ediar(@PathVariable("id") String id, ModelMap modelo, HttpSession session,
            @RequestParam ArrayList<String> diasSeleccionados, @RequestParam("horaInicial") String horaInicial,
            @RequestParam("horaFinal") String horaFinal, @RequestParam String duracion,
            @RequestParam String tipoOferta, @RequestParam String direccion) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);

        List<ObraSocial> obras = servObra.listarObrasSociales();
        modelo.addAttribute("obras", obras);
        System.out.println("POST " + obras.toString());
/// Asignar a diasDisponibles(Enum lista) los diasSeleccionados que traigo al prof
        //    profesionalServicio.asignarDisponibilidad(id,diasSeleccionados);

        /// Asignar Oferta  al prof con
        //    profesionalServicio.asignarOferta(id,horaInicial,HoraFinal,duracion, tipoOferta);
        ///
       
        modelo.put("exito", "¡Oferta y disponibilidad cargada con exito!");
        return "miOfertayDisponibilidad.html";

    }

}
