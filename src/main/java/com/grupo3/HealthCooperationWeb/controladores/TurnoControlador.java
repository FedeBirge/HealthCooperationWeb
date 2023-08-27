package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.servicios.TurnoServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/turno")
public class TurnoControlador {
    
    TurnoServicio turnoServ = new TurnoServicio();
    
    @GetMapping("/misturnos/{id}") // ruta para el panel administrativo
    public String misTurnos(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
            List<Turno> turnos = turnoServ.misTurnos(id);
        modelo.addAttribute("turnos", turnos);

        return "verTurnos.html";
    }

}
