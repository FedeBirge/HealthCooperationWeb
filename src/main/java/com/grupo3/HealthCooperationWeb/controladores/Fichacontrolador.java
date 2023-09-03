
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.FichaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paciente/historia_clilnica/ficha")
public class Fichacontrolador {

    @Autowired
    private FichaServicio fichaServicio;

    // ruta para ver todas las fichas de un paciente
    @GetMapping("/paciente/historia_clinica/ficha/{id}")
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
    @GetMapping("/paciente/historia_clinica/ficha/agregar_ficha/{id}") // ruta para crear una obra social GET
    public String agregarFicha(ModelMap modelo, @PathVariable("id") String idPaciente, String fecha_consulta,
            String nota) {
        try {
            fichaServicio.agregarFicha(idPaciente, fecha_consulta, nota);
            modelo.put("exito", "!Ficha agregada con exito!");
            return "ficha_form.html";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "";
        }
    }

    // ruta para agregar ficha POST
    @PostMapping("/paciente/historia_clinica/ficha/{id}")
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
