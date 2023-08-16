package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteServicio extends UsuarioServicio {

    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    @Transactional
    public void crearPaciente(String grupoSanguineo, List<Turno> turnos, HistoriaClinica historia, ObraSocial obraSocial) throws MyException {

        validar(grupoSanguineo, turnos, obraSocial);

        Paciente paciente = new Paciente();
        paciente.setGrupoSanguineo(grupoSanguineo);
        paciente.setTurnos(turnos);
        paciente.setHistoria(historia);
        paciente.setObraSocial(obraSocial);

        pacienteRepositorio.save(paciente);

    }

    @Transactional
    public void guardarPaciente(Paciente paciente) {

        pacienteRepositorio.save(paciente);
    }

    public List<Paciente> mostrarPacientes() {

        List<Paciente> pacientes = new ArrayList();

        pacientes = pacienteRepositorio.findAll();

        return pacientes;
    }

    public Paciente getOne(String id) {
        return pacienteRepositorio.getOne(id);
    }

    private void validar(String grupoSanguineo, List<Turno> turnos, ObraSocial obraSocial) throws MyException {

        if (grupoSanguineo == null || grupoSanguineo.isEmpty()) {
            throw new MyException("Debe completar el grupo de sangre");
            
        }
        if(turnos == null || turnos.isEmpty()){
            throw new MyException("Debe ingresar el turno pedido");
            
        }
        if(obraSocial == null){
            throw new MyException("Debe ingresar si tiene obra social o no");
        }
    }

}
