
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.HistoriaClinicaServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/historia")
public class HistoriaClinicaControlador {

    @Autowired
    private HistoriaClinicaServicio historiaClinicaServicio;

    // (edito: bren)
    // para que se vea la HC del paciente seleccionado x id
    // recordar que este {id} corresponde al id del paciente
    @GetMapping("/verHistoria/{id}")
    public String mostrarHistoria(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        try {
             Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
//            modelo.addAttribute("HistoriaClinica", historiaClinicaServicio.mostrarHistoria(id));
            return "Consulta historial.html";
        } catch (Exception e) {
                return "Consulta historial.html";
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
