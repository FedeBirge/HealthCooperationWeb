package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.DiaAgendaRepositorio;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiaAgendaServicio {

    @Autowired
    private DiaAgendaRepositorio diaAgendaRepo;

    public DiaAgenda getOne(String id) {
        return diaAgendaRepo.getOne(id);
    }

    public DiaAgenda crearDia( Date fecha,  ArrayList<Turno> turnos  ) throws MyException {
        DiaAgenda dia = new DiaAgenda();
        
        dia.setFecha(fecha);
        Collections.sort(turnos, Comparator.comparing(turno -> LocalTime.parse(turno.getHora())));
        dia.setTurnos(turnos);
        for (Turno turno : turnos) {
         
        }
        diaAgendaRepo.save(dia);
        
        return dia;
    }

public DiaAgenda modificarDia(DiaAgenda dia) {
        return null;
    }

    public DiaAgenda eliminarDia(DiaAgenda dia) {
        return null;
    }
    
}
