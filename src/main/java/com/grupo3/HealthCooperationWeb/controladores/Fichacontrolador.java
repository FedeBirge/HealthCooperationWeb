
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.FichaServicio;
import com.grupo3.HealthCooperationWeb.servicios.HistoriaClinicaServicio;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import java.time.LocalDate;
import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ficha")
public class Fichacontrolador {

    @Autowired
    private FichaServicio fichaServicio;
    @Autowired
    private HistoriaClinicaServicio historiaClinicaServicio;
     @Autowired
    private PacienteServicio paciServ; 

    // ruta para ver todas las fichas de un paciente
    @GetMapping("/ficha/{id}")
    public String mostrarFicha(ModelMap modelo, @PathVariable("id") String idPaciente) {
        try {
            modelo.put("ficha", fichaServicio.mostrarFichas(idPaciente));
            modelo.addAttribute("id", fichaServicio.mostrarFichas(idPaciente));
            return "ficha.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect:";
        }
    }

    // ruta para agregar una ficha GET
    @PostMapping("/guardar/{id}") // ruta para crear una obra social GET
    public String agregarFicha(ModelMap modelo, @PathVariable("id") String id,
            @RequestParam String nota,HttpSession session) {
        try {
        
            fichaServicio.agregarFicha(id, LocalDate.now().toString(), nota);
            modelo.addAttribute("exito", "!Ficha agregada con exito!");
             Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", paciServ.getOne(id));
            modelo.addAttribute("historia", historiaClinicaServicio.mostrarHistoria(id));
            return "Consulta historial.html";

        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
         return "Consulta historial.html";
        }
    }

    // ruta para agregar ficha POST
    @PostMapping("/ficha/{id}")
    public String agregarFicha(@PathVariable("id") String idPaciente, String fecha_consulta, String nota,
            ModelMap modelo)
            throws MyException {

        try {
            fichaServicio.agregarFicha(idPaciente, fecha_consulta, nota);
            modelo.put("exito", "La ficha ha sido agregada exitosamente");
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "ficha_form.html";// volvemos a cargar el formulario
        }
        return "index.html";
    }

    // las fichas no deben ni eliminarse ni editarse...

}
