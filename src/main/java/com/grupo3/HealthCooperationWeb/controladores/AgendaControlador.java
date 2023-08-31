package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.AgendaServicio;
import com.grupo3.HealthCooperationWeb.servicios.ObraSocialServicio;
import com.grupo3.HealthCooperationWeb.servicios.OfertaServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agenda")
public class AgendaControlador {

    @Autowired
    private AgendaServicio servAgenda;
       @Autowired
    private OfertaServicio servOferta;
   @Autowired
    private ObraSocialServicio servObra;
     @Autowired
    private ProfesionalServicio profesionalServicio;

    @GetMapping("/verAgenda/{id}") // Vista principal para el Admin al Logearse (LT)
    public String verrAgenda(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

    List<AgendaSemanal> semanas = servAgenda.obtenerAgendaxProf(id);
    
        if (semanas.size()!=0 ) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("agenda", semanas);
            modelo.addAttribute("dias", profesionalServicio.getOne(id).getDiasDisponibles());
            modelo.addAttribute("obras", servObra.listarObrasSociales());
     
            return "verAgenda.html";

        } else {
           
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("vacia", "¡Su Agenda no ha sido creada! Seleccione el botón Generar Agenda");
            return "verAgenda.html";

        }

    }
    

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

//    @PostMapping("/crear/{id}")
//    public String crearAgenda(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {
//
//        try {
//            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
//            modelo.addAttribute("log", logueado);
//            
//           List<AgendaSemanal> semanas = servAgenda.crearAgenda(id):
//           Profesional prof = profesionalServicio.getOne(id);
//           prof.setAgenda(agenda);
//
//            modelo.put("exito", "¡Agenda generada con exito!");
//            return "verAgenda.html";
//        } catch (MyException e) {
//            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
//            modelo.addAttribute("log", logueado);
//
//            List<ObraSocial> obras = servObra.listarObrasSociales();
//            modelo.addAttribute("obras", obras);
//
//            modelo.put("error", e.getMessage());
//            return crear(id, modelo, session);
//        }
//    }
    

    @GetMapping("/editar/{id}") // Vista principal para el Admin al Logearse (LT)
    public String editarAgenda(ModelMap modelo, HttpSession session) {

        return null;

    }

    @PostMapping("/editar/{id}") // Vista principal para el Admin al Logearse (LT)
    public String ediar(ModelMap modelo, HttpSession session) {
        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            return "panelAdmin.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            modelo.put("error", e.getMessage());
            return "redirect: /dashboard";
        }
    }

}
