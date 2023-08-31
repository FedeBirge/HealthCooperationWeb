package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.DiaAgendaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiaAgendaServicio {

    @Autowired
    private DiaAgendaRepositorio diaAgendaRepo;

    public DiaAgenda getOne(String id) {
        return diaAgendaRepo.getOne(id);
    }

    public DiaAgenda crearDia(String idProf) throws MyException {
        return null;
    }

public DiaAgenda modificarDia(DiaAgenda dia) {
        return null;
    }

    public DiaAgenda eliminarDia(DiaAgenda dia) {
        return null;
    }
    
}
