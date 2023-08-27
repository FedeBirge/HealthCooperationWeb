package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.TurnoRepositorio;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import javax.transaction.Transactional;

@Service
public class TurnoServicio {

    @Autowired
    private ProfesionalServicio profServ;
    @Autowired
    private TurnoRepositorio turnoRepo;
     @Autowired
    private PacienteRepositorio pacienteRepo;

    @Transactional
    public Turno crearTurno(String fecha, String hora, EstadoTurno estado,
            String motivo, String idProf) throws MyException {

        validar(fecha, hora, estado, motivo, idProf);
        Profesional prof = (Profesional) profServ.getOne(idProf);
        if (prof == null) {
            throw new MyException("No existe profesional asociado al turno!");
        }
        Turno turno = new Turno();
        turno.setFecha(fecha);
        turno.setHora(hora);
        turno.setEstado(EstadoTurno.DISPONIBLE);
        turno.setMotivo(motivo);
        turno.setProfesional(prof);
        return turnoRepo.save(turno);
    }

    public Turno buscarTurno(String id) throws MyException {
        Optional<Turno> optionalTurno = turnoRepo.findById(id);
        if (optionalTurno.isPresent()) {
            return optionalTurno.get();
        } else {
            throw new MyException("Turno no encontrado");
        }
    }

    @Transactional
    public Turno actualizarTurno(String id, String fecha, String hora, String motivo, EstadoTurno estado, String idProf)
            throws MyException {

        validar(fecha, hora, estado, motivo, idProf);
        Profesional prof = (Profesional) profServ.getOne(idProf);
        if (prof == null) {
            throw new MyException("No existe profesional asociado al turno");
        }
        Optional<Turno> optionalTurno = turnoRepo.findById(id);
        if (optionalTurno.isPresent()) {
            Turno turno = optionalTurno.get();
            turno.setFecha(fecha);
            turno.setHora(hora);
            turno.setEstado(estado);
            turno.setMotivo(motivo);
            turno.setProfesional(prof);
            return turnoRepo.save(turno);
        } else {
            throw new MyException("Turno no encontrado ");
        }
    }

    @Transactional
    public void cancelarTurno(String id) throws MyException {
        Optional<Turno> op = turnoRepo.findById(id);
        if (op.isPresent()) {
            Turno turno = op.get();
            turno.setEstado(EstadoTurno.CANCELADO);
            turnoRepo.save(turno);
        } else {
            throw new MyException("Turno no encontrado(CANCELAR)");
        }
    }

    @Transactional
    public void reservarTurno(String id) throws MyException {
        Optional<Turno> op = turnoRepo.findById(id);
        if (op.isPresent()) {
            Turno turno = op.get();
            turno.setEstado(EstadoTurno.RESERVADO);
            turnoRepo.save(turno);
        } else {
            throw new MyException("Turno no encontrado(RESERVAR)");
        }
    }

    @Transactional
    public void completarTurno(String id) throws MyException {
        Optional<Turno> op = turnoRepo.findById(id);
        if (op.isPresent()) {
            Turno turno = op.get();
            turno.setEstado(EstadoTurno.COMPLETADO);
            turnoRepo.save(turno);
        } else {
            throw new MyException("Turno no encontrado(COMPLETAR)");
        }
    }

    @Transactional
    public void eliminarTurno(String id) {
        try {
            Optional<Turno> respuesta = turnoRepo.findById(id);
            if (respuesta.isPresent()) {
                Turno turno = (Turno) respuesta.get();
                turnoRepo.delete(turno);
            }
        } catch (Exception e) {
            System.out.println("No es posible eliminar el turno");
        }
    }

    public List<Turno> listarTurnos() {

        List<Turno> turnos = new ArrayList();

        try {
            turnos = turnoRepo.findAll();

            return turnos;

        } catch (Exception e) {
            System.out.println("Turno: No pudieron ser listados");
            return null;
        }

    }
    public List<Turno> misTurnos(String id) {

        try {
             Optional<Paciente> respuesta = pacienteRepo.findById(id);
        if (respuesta.isPresent()) {
            Paciente pace = respuesta.get();
            return pace.getTurnos();
        }

        } catch (Exception e) {
            System.out.println("Turno: No pudieron ser listados");
            return null;
        }
         return null;
    }

    protected void validar(String fecha, String hora, EstadoTurno estado, String motivo, String idProf)
            throws MyException {
        if (fecha == null || fecha.isEmpty()) {
            throw new MyException("Debe indicar uan fecha para el turno");
        }
        if (hora == null || hora.isEmpty()) {
            throw new MyException("Debe indicar un horario para el turno");
        }
        if (motivo == null || motivo.isEmpty()) {
            throw new MyException("Debe ingresar una descripcion de la visita");
        }

        if (idProf == null || idProf.isEmpty()) {
            throw new MyException("El turno debe tener un Profesional Asociado");
        }
        if (estado == null) {
            throw new MyException("El turno debe tener un estado");
        }

    }

    public List<Turno> listarTurnosXProfesional(String idProfesional) {
        Profesional profesional = new Profesional();

        List<Turno> turnos = turnoRepo.findAll();
        List<Turno> turnosXProfesional = new ArrayList<>();

        try {
            profesional = (Profesional) profServ.getOne(idProfesional);

            for (Turno turno : turnos) {
                if (turno.getProfesional().getId() == profesional.getId()) {
                    turnosXProfesional.add(turno);
                }
            }
            return turnosXProfesional;
        } catch (Exception e) {
            System.out.println("Turno: No pudieron ser listados los turnos de este idProfesional");
            return null;
        }

    }

}
