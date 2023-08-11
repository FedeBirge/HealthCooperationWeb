package com.grupo3.HealthCooperationWeb.servicios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;

@Service
public class ProfesionalServicio {

    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;

    // puse todos los métodos con return para facilitar tarea front (según entiendo)

    // faltaría agregar atributos restantes por parámetro (descripcion y
    // especialidad)
    @Transactional
    public Profesional crearProfesional(Profesional profesional) {
        return profesionalRepositorio.save(profesional);
    }

    @Transactional
    public Profesional guardarProfesional(Profesional profesional) {
        return profesionalRepositorio.save(profesional);
    }

    // ¿acá no hace falta transactional, no?
    public ArrayList<Profesional> mostrarProfesionales() {
        return (ArrayList<Profesional>) profesionalRepositorio.findAll();
    }

    // ¿acá no hace falta transactional, no?
    public ArrayList<Profesional> ordenarPorValorConsulta() {
        ArrayList<Profesional> profesionales = (ArrayList<Profesional>) profesionalRepositorio.findAll();
        // Ordenar los profesionales por el valor de consulta usando Collections.sort()
        Collections.sort(profesionales, Comparator.comparing(Profesional::getValorConsulta));
        return profesionales;
    }

    @Transactional
    public String darDeBajaProfesional(String id) {

        try {
            Optional<Profesional> respuesta = profesionalRepositorio.findById(id);

            if (respuesta.isPresent()) {
                Profesional profesional = (Profesional) (respuesta.get());
                profesional.setActivo(false); // Establecer el atributo activo como false
                profesionalRepositorio.save(profesional);
                return "Profesional dado de baja exitosamente";

            }
        } catch (Exception e) {
            System.out.println("El id ingresado es inválido");
        }
        return "Lo sentimos, no fue posible dar de baja al profesional";

    }

}
