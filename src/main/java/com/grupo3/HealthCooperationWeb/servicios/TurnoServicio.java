/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.TurnoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoServicio {

    private final TurnoRepositorio turnoRepositorio;

    @Autowired
    public TurnoServicio(TurnoRepositorio turnoRepositorio) {
        this.turnoRepositorio = turnoRepositorio;
    }

    public List<Turno> obtenerTodosLosTurnos() {
        return turnoRepositorio.findAll();
    }

    public Turno obtenerTurnoPorId(String id) throws MyException {
        Optional<Turno> optionalTurno = turnoRepositorio.findById(id);
        if (optionalTurno.isPresent()) {
            return optionalTurno.get();
        } else {
            throw new MyException("Turno no encontrado con ID: " + id);
        }
    }

    public Turno crearTurno(Turno turno) {
        return turnoRepositorio.save(turno);
    }

    public Turno actualizarTurno(String id, Turno turnoActualizado) throws MyException {
        Optional<Turno> optionalTurno = turnoRepositorio.findById(id);
        if (optionalTurno.isPresent()) {
            Turno turnoExistente = optionalTurno.get();
            turnoExistente.setFecha(turnoActualizado.getFecha());
            turnoExistente.setHora(turnoActualizado.getHora());
            // Actualizar otros atributos según sea necesario
            return turnoRepositorio.save(turnoExistente);
        } else {
            throw new MyException("Turno no encontrado con ID: " + id);
        }
    }

    public void eliminarTurno(String id) throws MyException {
        Optional<Turno> optionalTurno = turnoRepositorio.findById(id);
        if (optionalTurno.isPresent()) {
            turnoRepositorio.delete(optionalTurno.get());
        } else {
            throw new MyException("Turno no encontrado con ID: " + id);
        }
    }
}
