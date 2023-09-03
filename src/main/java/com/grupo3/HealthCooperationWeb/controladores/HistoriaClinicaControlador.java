
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

    // ruta para ver la historia clínica según id paciente
    @GetMapping("/ver/{id}")
    public String mostrarHistoria(@PathVariable("id") String id, ModelMap modelo, HttpSession session)
            throws MyException {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("HistoriaClinica", historiaClinicaServicio.mostrarHistoria(id));
            return "Consulta historial.html";
        } catch (Exception e) {
            return "Consulta historial.html";
        }
    }

    // las Historias Clínicas no se modifican ni se eliminan (pedido del cliente)

}
