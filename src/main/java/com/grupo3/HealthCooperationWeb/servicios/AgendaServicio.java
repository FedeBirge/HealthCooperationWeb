package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.enumeradores.Dias;
import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.AgendaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.DiaAgendaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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
    @Autowired
    private DiaAgendaServicio diaServ;

    private LocalDate obtenerFecha(DayOfWeek dia, int sem) {

        LocalDate fechaActual = LocalDate.now().plusDays(7 * sem);
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

    private ArrayList<Turno> armarTurnos(Oferta oferta, Date fecha, String idProf) throws MyException {
        ArrayList<Turno> turnos = new ArrayList();
        LocalTime inicio = LocalTime.parse(oferta.getHoraInicio());
        LocalTime fin = LocalTime.parse(oferta.getHoraFin());
        Long duracion = Long.parseLong(oferta.getDuracionTurno());

        while (!inicio.isAfter(fin)) {
            if (inicio.equals(fin) || inicio.isAfter(fin)) {
                break;
            }
            Turno turno = turnoServ.crearTurno(fecha.toLocaleString(), inicio.toString(), EstadoTurno.DISPONIBLE, "Su motivo Aquí", idProf);

            turnos.add(turno);
            inicio = inicio.plusMinutes(duracion); // Incrementar minutos
        }
        Collections.sort(turnos, Comparator.comparing(Turno::getHora));

        return turnos;

    }

    @Transactional
    // Metodo para crear una Agenda, la misma se genera a partir del profesional
    // Con los atributos diasDisponibles y oferta
    public ArrayList<AgendaSemanal> crearAgenda(String idProf) throws MyException {
        ArrayList<AgendaSemanal> semanas = new ArrayList<>();

        Optional<Profesional> respuesta = profRepo.findById(idProf);
        System.out.println(idProf);
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

            List<Dias> dias = prof.getDiasDisponibles();

            for (int i = 0; i < 3; i++) { // Recorro 3 veces para armar 3 semanas
                AgendaSemanal agenda = new AgendaSemanal();
                TreeMap<Date, DiaAgenda> fechaYTurnos = new TreeMap<>(Comparator.comparingLong(Date::getDay));

                for (Dias dia : dias) {             // recorro los dias disponibles
                    // si el dia pertenece a los disponibles del prof armo la lista de turnos

                    // primero paso el dia Enum a fecha calendario, y d LocalDate a Date(por ahora)
                    Date fechaturno = Date.from(obtenerFecha(convertirADayOfWeek(dia), i).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    // armo lsita de turno segun oferta y fecha
                    ArrayList<Turno> turnos = armarTurnos(prof.getOferta(), fechaturno, idProf);
                    //armo el dia de agenda
                    DiaAgenda diaAgenda = diaServ.crearDia(fechaturno, turnos);
                    fechaYTurnos.put(fechaturno, diaAgenda);
//                fechaYTurnos = ordenarMapPorFecha(fechaYTurnos);
                }

                agenda.setFechasYTurnos(fechaYTurnos);
                agendaRepo.save(agenda);
                semanas.add(agenda);
//                for (Map.Entry<Date, DiaAgenda> entry : fechaYTurnos.entrySet()) {
//                    Date key = entry.getKey();
//                    System.out.println("Dia: ");
//                    DiaAgenda value = entry.getValue();
//
//                    for (Turno turno : value.getTurnos()) {
//                        System.out.println("turno: " + turno.getId());
//                    }

            }

            return semanas;
        }

        return null;

    }

    public AgendaSemanal getOne(String id) {
        return agendaRepo.getOne(id);
    }

    public Map<Date, DiaAgenda> ordenarMapPorFecha(Map<Date, DiaAgenda> mapa) {
        List<Map.Entry<Date, DiaAgenda>> listaOrdenada = new ArrayList<>(mapa.entrySet());

        Collections.sort(listaOrdenada, Map.Entry.comparingByKey());

        LinkedHashMap<Date, DiaAgenda> mapaOrdenado = new LinkedHashMap<>();
        for (Map.Entry<Date, DiaAgenda> entry : listaOrdenada) {
            mapaOrdenado.put(entry.getKey(), entry.getValue());
        }

        return mapaOrdenado;
    }
   public List<AgendaSemanal> obtenerSemanaActual(String id,List<AgendaSemanal> semanas) {
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        List<AgendaSemanal> una = new ArrayList<>();
        
           for (AgendaSemanal semana : semanas) {
                Map<Date, DiaAgenda> fechasYTurnos = semana.getFechasYTurnos();
                for (Map.Entry<Date, DiaAgenda> entry : fechasYTurnos.entrySet()) {
                    Date key = entry.getKey();
                    if(key.before(new Date())|| key.equals(new Date())){
                        
                        una.add(semana);
                        return una;
                    }
                    DiaAgenda value = entry.getValue();
                    
                }
               
                
            }
        return null;
    }
    public List<AgendaSemanal> obtenerAgendaxProf(String id) {
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Profesional prof = respuesta.get();
            for (AgendaSemanal semana : prof.getAgendasSemanales()) {
                List<Map.Entry<Date, DiaAgenda>> fechasOrdenadas = new ArrayList<>(semana.getFechasYTurnos().entrySet());
                Collections.sort(fechasOrdenadas, Map.Entry.comparingByKey()); // Ordenar por clave (fecha)
                Map<Date, DiaAgenda> fechasOrdenadasMap = new LinkedHashMap<>();
                for (Map.Entry<Date, DiaAgenda> entry : fechasOrdenadas) {
                    fechasOrdenadasMap.put(entry.getKey(), entry.getValue());
                }

                semana.setFechasYTurnos(ordenarMapPorFecha(fechasOrdenadasMap));
            }
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
