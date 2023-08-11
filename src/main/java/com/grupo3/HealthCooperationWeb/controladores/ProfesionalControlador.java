package com.grupo3.HealthCooperationWeb.controladores;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;

@RestController
@RequestMapping("/profesionales")
public class ProfesionalControlador {

    @Autowired
    ProfesionalServicio profesionalServicio;

    @GetMapping()
    public ArrayList<Profesional> obtenerProfesionales() {
        return profesionalServicio.mostrarProfesionales();
    }

    // Hay que refinar esto para sumarlo a los atributos de usuario... falta mucho
    // aca
    @PostMapping("/crear")
    public Profesional crearProfesional(@RequestBody Profesional profesional) {
        return this.profesionalServicio.crearProfesional(profesional);
    }

    // funcionará? con postman no pude verificar
    @GetMapping("/precio")
    public ArrayList<Profesional> obtenerListadoPreciosConsultas() {
        return this.profesionalServicio.ordenarPorValorConsulta();
    }

    // falta crear y dar de baja

}
