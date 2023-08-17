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

import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaServicio {

    @Autowired
    private AgendaRepositorio agendaRepo;
    @Autowired
    private ProfesionalRepositorio profRepo;

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

    private ArrayList<Turno> armarTurnos(Oferta oferta, Date fecha) {
        ArrayList<Turno> turnos = new ArrayList();
        LocalTime inicio = LocalTime.parse(oferta.getHoraInicio());
        LocalTime fin = LocalTime.parse(oferta.getHoraFin());
        Long duracion = Long.parseLong(oferta.getUbicacion());

        while (!inicio.isAfter(fin)) {
            if (inicio.equals(fin)) {
                break;
            }
            Turno turno = new Turno();
            turno.setFecha(fecha.toLocaleString());
            turno.setHora(inicio.toString());
            turnos.add(turno);
            inicio = inicio.plusMinutes(duracion); // Incrementar  minutos
        }
        return turnos;

    }

    @Transactional
    // Metodo para crear una Agenda, la misma se genera a partir del profesional
    // Con los atributos diasDisponibles y oferta 
    public AgendaSemanal crearAgenda(String idProf) throws MyException {
        AgendaSemanal agenda = new AgendaSemanal();
        try {
            Optional<Profesional> respuesta = profRepo.findById(idProf);
            
            if (respuesta.isPresent()) {
                Profesional prof = respuesta.get();
                if (prof == null) {
                    throw new MyException("No existe profesional!");
                }
                if (prof.getDiasDisponibles() == null) {
                    throw new MyException("No hay dias diasponilbes para el profesional!");
                }
                if (prof.getOferta() == null) {
                    throw new MyException("No hay Oferta disponible del profesional!");
                }

                // Recorro los dias disponibles para armar agenda
                Map<Date, ArrayList<Turno>> fechaYTurnos = new HashMap();

                for (Dias dia : Dias.values()) {  // recorro los dias de la semana
                    if (prof.getDiasDisponibles().contains(dia)) {
                        // si el dia pertenece a los disponibles del prof armo la lista de turnos

                        // primero paso el dia Enum a fecha calendario, y d LocalDate a Date(por ahora) 
                        Date fechaturno = Date.from(obtenerFecha(convertirADayOfWeek(dia)).atStartOfDay(ZoneId.systemDefault()).toInstant());
                        // armo lsita de turno segun oferta y fecha
                        ArrayList<Turno> turnos = armarTurnos(prof.getOferta(), fechaturno);
                        fechaYTurnos.put(fechaturno, turnos);

                    }

                } 
                agenda.setFechaYTurnos(fechaYTurnos);
                agendaRepo.save(agenda);
             
            }
        } catch (Exception e) {
            throw new MyException("No es posible crear la agenda");

        }
         return agenda; 
    }
    
    public AgendaSemanal modificarAgenda(AgendaSemanal agenda){
        return null;
    }
      public AgendaSemanal eliminarAgenda(AgendaSemanal agenda){
        return null;
    }
      public AgendaSemanal mostrarAgenda(AgendaSemanal agenda){
        return null;
    }  
      public AgendaSemanal cancelarAgenda(AgendaSemanal agenda){
        return null;
    }  
      public AgendaSemanal cancelarAgendaDia(Dias dia, AgendaSemanal agenda){
        return null;
    }  
       public AgendaSemanal cancelarAgendaFecha(Date fecha , AgendaSemanal agenda){
        return null;
    }  
}
