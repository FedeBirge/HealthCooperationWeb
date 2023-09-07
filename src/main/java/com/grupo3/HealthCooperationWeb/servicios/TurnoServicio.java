package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;

import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.TurnoRepositorio;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        turno.setEstado(estado);
        turno.setMotivo(motivo);
        turno.setProfesional(prof);
        turnoRepo.saveAndFlush(turno);

        return turno;
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

    public EstadoTurno pasarStringEstado(String estado) throws MyException {
        switch (estado) {
            case "CANCELADO":
                return EstadoTurno.CANCELADO;
            case "COMPLETADO":
                return EstadoTurno.COMPLETADO;
            case "DISPONIBLE":
                return EstadoTurno.DISPONIBLE;
            case "RESERVADO":
                return EstadoTurno.RESERVADO;
            default:
                throw new MyException("Estado turno no válido: " + estado);
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
    public Turno reservarTurno(String id) throws MyException {
        Optional<Turno> op = turnoRepo.findById(id);
        if (op.isPresent()) {
            Turno turno = op.get();
            turno.setEstado(EstadoTurno.RESERVADO);
            turnoRepo.save(turno);
            return turno;
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

    public void cancelarTurnosProf(String id) {
        try {
            List<Turno> turnos = new ArrayList();
            Profesional prof = profServ.getOne(id);

            turnos = turnoRepo.findAll();

            for (Turno turno : turnos) {
                if (turno.getProfesional().getId().equals(prof.getId())) {

                   
                    cancelarTurno(turno.getId());
                }
            }

        } catch (Exception e) {
            System.out.println("Turno: No pudieron ser listados");

        }

    }

    public List<Turno> misTurnos(String id) {

        try {
            Optional<Paciente> respuesta = pacienteRepo.findById(id);
            if (respuesta.isPresent()) {
                Paciente pace = respuesta.get();
                for (Turno turno : pace.getTurnos()) {
                 
                }
                return pace.getTurnos();
            }

        } catch (Exception e) {
            System.out.println("Turno: No pudieron ser listados");
            return null;
        }
        return null;
    }

    public List<Turno> ordenarTurnos(List<Turno> turnos) {

       try {
        // Define un comparador personalizado para ordenar por fecha y hora
        Comparator<Turno> comparadorTurno = new Comparator<Turno>() {
            @Override
            public int compare(Turno turno1, Turno turno2) {
                try {
                    SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date fechaHora1 = formatoFechaHora.parse(turno1.getFecha() + " " + turno1.getHora());
                    Date fechaHora2 = formatoFechaHora.parse(turno2.getFecha() + " " + turno2.getHora());
                    return fechaHora1.compareTo(fechaHora2);
                } catch (ParseException e) {
                    // Manejo de excepción en caso de que las fechas o horas no sean válidas
                    e.printStackTrace();
                    return 0; // O maneja la excepción de acuerdo a tus necesidades
                }
            }
        };

        Collections.sort(turnos, comparadorTurno);

        return turnos;
    } catch (Exception e) {
        System.out.println("Turno: No pudieron ser listados");
        return null;
    }
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

    public Turno getOne(String id) {
        return turnoRepo.getOne(id);
    }

    @Transactional
    public void cancelarTurnosSemana(String id, List<AgendaSemanal> semana) throws MyException {

        Map<Date, DiaAgenda> fechasYturnos = semana.get(0).getFechasYTurnos();

        for (Map.Entry<Date, DiaAgenda> entry : fechasYturnos.entrySet()) {
            Date key = entry.getKey();
            DiaAgenda value = entry.getValue();
            List<Turno> turnos = value.getTurnos();
            for (Turno turno : turnos) {

                cancelarTurno(turno.getId());
            }

        }
    }

    @Transactional
    public void actualizarEstados(List<Turno> turnos) throws MyException {
        List<Turno> turnero = turnoRepo.findAll();
        if (turnos != null) {
            for (Turno turno : turnos) {
                Turno inter = getOne(turno.getId());
                inter.setEstado(turno.getEstado());
                turnoRepo.save(inter);

            }
        }
    }

}
