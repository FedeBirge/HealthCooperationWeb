package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.enumeradores.Dias;
import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.AgendaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaServicio {

    @Autowired
    private AgendaRepositorio agendaRepo;
    @Autowired
    private ProfesionalRepositorio profRepo;
    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;
    @Autowired
    private TurnoServicio turnoServ;

    private LocalDate obtenerFecha(DayOfWeek dia) {

        LocalDate fechaActual = LocalDate.now();
        int daysToAdd = dia.getValue() - fechaActual.getDayOfWeek().getValue();

        return fechaActual.plusDays(daysToAdd);

    }

    private DayOfWeek convertirADayOfWeek(Dias diaEnum) throws MyException {
        switch (diaEnum) {
            case LUNES:
                return DayOfWeek.MONDAY;
            case MARTES:
                return DayOfWeek.TUESDAY;
            case MIERCOLES:
                return DayOfWeek.WEDNESDAY;
            case JUEVES:
                return DayOfWeek.THURSDAY;
            case VIERNES:
                return DayOfWeek.FRIDAY;
            case SABADO:
                return DayOfWeek.SATURDAY;
            case DOMINGO:
                return DayOfWeek.SUNDAY;
            default:
                throw new MyException("Enum Dias no válido");
        }
    }

//    private ArrayList<Turno> armarTurnos(Oferta oferta, Date fecha, String idProf) throws MyException {
//        ArrayList<Turno> turnos = new ArrayList();
//        LocalTime inicio = LocalTime.parse(oferta.getHoraInicio());
//        LocalTime fin = LocalTime.parse(oferta.getHoraFin());
//        Long duracion = Long.parseLong(oferta.getDuracionTurno());
//
//        while (!inicio.isAfter(fin)) {
//            if (inicio.equals(fin) || inicio.isAfter(fin)) {
//                break;
//            }
//            Turno turno = turnoServ.crearTurno(fecha.toLocaleString(), inicio.toString(), EstadoTurno.DISPONIBLE, "Su motivo Aquí", idProf);
//
//            turnos.add(turno);
//            inicio = inicio.plusMinutes(duracion); // Incrementar minutos
//        }
//        return turnos;
//
//    }
//
//    @Transactional
//    // Metodo para crear una Agenda, la misma se genera a partir del profesional
//    // Con los atributos diasDisponibles y oferta
//    public AgendaSemanal crearAgenda(String idProf) throws MyException {
//        AgendaSemanal agenda = new AgendaSemanal();
//
//        Optional<Profesional> respuesta = profRepo.findById(idProf);
//        System.out.println(idProf);
//        if (respuesta.isPresent()) {
//            Profesional prof = respuesta.get();
//            if (prof == null) {
//                throw new MyException("No existe profesional!");
//            }
//            if (prof.getDiasDisponibles() == null) {
//                throw new MyException("No hay dias diasponilbes para el profesional!");
//            }
//            if (prof.getOferta() == null) {
//                throw new MyException("No hay Oferta disponible del profesional!");
//            }
//
//            // Recorro los dias disponibles para armar agenda
//            Map<Date, ArrayList<String>> fechaYTurnos = new HashMap();
//
//            for (Dias dia : Dias.values()) { // recorro los dias de la semana
//                if (prof.getDiasDisponibles().contains(dia)) {
//                    // si el dia pertenece a los disponibles del prof armo la lista de turnos
//
//                    // primero paso el dia Enum a fecha calendario, y d LocalDate a Date(por ahora)
//                    Date fechaturno = Date.from(obtenerFecha(convertirADayOfWeek(dia)).atStartOfDay(ZoneId.systemDefault()).toInstant());
//                    // armo lsita de turno segun oferta y fecha
//                    ArrayList<Turno> turnos = armarTurnos(prof.getOferta(), fechaturno, idProf);
//                  
//                    ArrayList<String> turnoIds = (ArrayList<String>) turnos.stream().map(Turno::getId).collect(Collectors.toList());
//                    fechaYTurnos.put(fechaturno, turnoIds);
//
//                  
//
//                }
//
//            }
//            for (Map.Entry<Date, ArrayList<String>> entry : fechaYTurnos.entrySet()) {
//                Date key = entry.getKey();
//                System.out.println("key: "+key+" Turnos:");
//                List<String> value = entry.getValue();
//                for (String turno : value) {
//                    System.out.println(turno);
//                    
//                }
//                
//            }
//            agenda.setFechaYTurnos(fechaYTurnos);
//
//            agendaRepo.saveAndFlush(agenda);
//            return agenda;
//        }
//        return null;
//
//    }

    public AgendaSemanal getOne(String id) {
        return agendaRepo.getOne(id);
    }

    public List<AgendaSemanal> obtenerAgendaxProf(String id) {
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Profesional prof = respuesta.get();
            return prof.getAgendasSemanales();

        }
        return null;
    }

    public AgendaSemanal modificarAgenda(AgendaSemanal agenda) {
        return null;
    }

    public AgendaSemanal eliminarAgenda(AgendaSemanal agenda) {
        return null;
    }

    public AgendaSemanal mostrarAgenda(AgendaSemanal agenda) {
        return null;
    }

    public AgendaSemanal cancelarAgenda(AgendaSemanal agenda) {
        return null;
    }

    public AgendaSemanal cancelarAgendaDia(Dias dia, AgendaSemanal agenda) {
        return null;
    }

    public AgendaSemanal cancelarAgendaFecha(Date fecha, AgendaSemanal agenda) {
        return null;
    }
}
