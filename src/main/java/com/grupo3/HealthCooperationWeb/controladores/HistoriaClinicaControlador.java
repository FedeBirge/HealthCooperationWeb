
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.HistoriaClinicaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paciente/historia_clinica")
public class HistoriaClinicaControlador {

    @Autowired
    private HistoriaClinicaServicio historiaClinicaServicio;

    // (edito: bren)
    // para que se vea la HC del paciente seleccionado x id
    // recordar que este {id} corresponde al id del paciente
    @GetMapping("/verHistoriaClinica/{id}")
    public String mostrarHistoria(@PathVariable("id") String id, ModelMap modelo) throws MyException {

        try {
            modelo.addAttribute("HistoriaClinica", historiaClinicaServicio.mostrarHistoria(id));
            return "verHistoriaClinica.html";
        } catch (Exception e) {
            return "redirect: /panelProfesional.html";
        }
    }

    // (mariela)
    /*
     * @PostMapping("/historia/registro")
     * public String registro(@RequestParam List<Ficha> fichas, ModelMap modelo)
     * throws MyException {
     * 
     * // historiaClinicaServicio.crearHistoriaClinica( fichas);
     * // modelo.put("exito", "La historia clinica se registro correctamente");
     * 
     * return "index.html";
     * }
     */

}
