package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;

import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ObraSocialRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.TurnoRepositorio;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PacienteServicio extends UsuarioServicio implements UserDetailsService {

    @Autowired
    private AgendaServicio agendaServ;
    @Autowired
    private PacienteRepositorio pacienteRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private ObraSocialRepositorio obraRepo;
    @Autowired
    private ObraSocialServicio obraSocialServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private TurnoRepositorio turnoRepositorio;
    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;
    @Autowired
    private TurnoServicio turnoServ;

    @Transactional
    public Paciente registrarPaciente(MultipartFile archivo, String nombre, String apellido, String dni, String email,
            String password, String password2,
            String telefono, String direccion, String fecha_nac, String grupoSanguineo, String obraSocial)
            throws MyException, IOException, ParseException {

        super.validar("1", nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        validar(grupoSanguineo, obraSocial);
        if (usuarioServicio.buscarPorDni(dni) != null) {
            throw new MyException("Existe un usuario con el N° de docuemnto!");
        }
        Date fecha = pasarStringDate(fecha_nac);
        if (!validarFecha(fecha)) {
            throw new MyException("la fecha no es válida");
        }
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDni(dni);
        paciente.setEmail(email);
        paciente.setPassword(new BCryptPasswordEncoder().encode(password));
        paciente.setTelefono(telefono);
        paciente.setDireccion(direccion);
        paciente.setFecha_nac(fecha);
        paciente.setActivo(true);
        paciente.setGrupoSanguineo(grupoSanguineo);

        // Crear obra social o buscar
        ObraSocial obraSocial2 = obraSocialServicio.buscarXNombre(obraSocial);

        if (obraSocial2 == null) {
            obraSocial2 = obraSocialServicio.crearObraSocialReturn(obraSocial, "Completar email", "Completar telefono");
            obraRepo.save(obraSocial2);
        }

        paciente.setObraSocial(obraSocial2);

        paciente.setRol(Rol.USUARIO);
        if (archivo.isEmpty()) {
            // Si el archivo está vacío, crea el paciente con una imagen predeterminada
            Imagen imagenPredeterminada = obtenerImagenPredeterminada(); // Implementa esta función para obtener la
            // imagen predeterminada
            paciente.setImagen(imagenPredeterminada);
        } else {
            // Si el archivo no está vacío, crea el paciente con la imagen proporcionada
            Imagen imagen = imagenServicio.guardar(archivo);
            paciente.setImagen(imagen);
        }

        pacienteRepositorio.save(paciente);
        return paciente;

    }

    public Paciente getOne(String id) {
        return pacienteRepositorio.getOne(id);
    }

    @Transactional
    public void modificarPaciente(String id, MultipartFile archivo, String nombre,
            String apellido, String dni,
            String email,
            String password, String password2,
            String telefono, String direccion, String fecha_nac, String grupoSanguineo,
            String obraSocial)
            throws MyException, IOException, ParseException {

        Optional<Paciente> respuesta = pacienteRepositorio.findById(id);
        Date fecha = pasarStringDate(fecha_nac);
        if (!validarFecha(fecha)) {
            throw new MyException("la fecha no es válida");
        }
        if (respuesta.isPresent()) {
            Paciente pas = respuesta.get();
            // if (!super.buscarPorMail(email).getId().equals(prof.getId())) {
            // throw new MyException("EL mail ingresado ya existe en otro ususario! Ingreso
            // otro!");
            // }
            super.modificarUsuario(archivo, id, nombre, apellido, dni, email, password, password2, telefono, direccion,
                    fecha_nac);

            ObraSocial obraSocial2 = obraSocialServicio.buscarXNombre(obraSocial);

            pas.setObraSocial(obraSocial2);
            pas.setGrupoSanguineo(grupoSanguineo);

            String idImg = null;
            if (pas.getImagen() != null) {
                idImg = pas.getImagen().getId();
            }
            if (archivo != null && archivo.getBytes().length != 0) {
                Imagen imagen = imagenServicio.actualizar(archivo, id);
                pas.setImagen(imagen);
            } else {
                // No se proporcionó un archivo nuevo, no se actualiza la imagen del usuario
            }

            pacienteRepositorio.save(pas);

        }
    }

    // se muestran todos que son activos por defecto, no se pueden dar de baja
    public List<Paciente> mostrarPacientes() {

        List<Paciente> aux = new ArrayList<>();
        List<Paciente> pacientes = new ArrayList<>();

        try {
            aux = (ArrayList<Paciente>) pacienteRepositorio.findAll();
            for (Paciente paciente : aux) {
                if (paciente.getActivo().equals(Boolean.TRUE)) {
                    pacientes.add(paciente);
                }
            }
            return pacientes;
        } catch (Exception e) {
            System.out.println("Hubo un error al listar profesionales.");
            return null;
        }
    }



    @Transactional
    public Paciente asignarTurnoPaciente(String idP, String idTurno,  String msj) throws MyException {

        // Busca el paciente por su ID o cualquier otro criterio de búsqueda
        Optional<Paciente> respuesta = pacienteRepositorio.findById(idP);
        if (respuesta.isPresent()) {
            Paciente pas = respuesta.get();

            List<Turno> turnos = new ArrayList<>();
            System.out.println("como el paciente tiene la lista? " + pas.getTurnos().size());
            if (pas.getTurnos() != null) {
                turnos = turnoServ.misTurnos(pas.getId());
            }

            Turno turno = turnoServ.getOne(idTurno);
            if (turno != null) {
                turno.setMotivo(msj);
                turno = turnoServ.reservarTurno(idTurno);
                turnos.add(turno);
                pas.setTurnos(turnos);
            }

            // Guarda el paciente actualizado en la base de datos
            pacienteRepositorio.save(pas);
            return pas;
        }
        return null;
    }


// *****COMPLETAR para traer los pacientes asociados a un profesional(id)
public List<Paciente> listarPacientesXprof(String idProfesional) {
    // Creo un paciente
    List<Paciente> pacientesXProfesional = new ArrayList<>();

    // Obtengo el profesional
    Profesional profesional = profesionalRepositorio.getOne(idProfesional);

    try {
        for (Paciente paciente : pacienteRepositorio.findAll()) {
            if (paciente.getTurnos() != null) {
                for (Turno turno : paciente.getTurnos()) {
                    if (turno.getProfesional().getId().equals(profesional.getId())) {
                        // Solo agrego el paciente una vez, si no está en la lista
                        if (!pacientesXProfesional.contains(paciente)) {
                            pacientesXProfesional.add(paciente);
                        }
                        // Si ya encontré un turno con el profesional, no es necesario seguir buscando en este paciente
                        break;
                    }
                }
            }
        }
        return pacientesXProfesional;
    } catch (Exception e) {
        System.out.println("Servicio paciente: Hubo un error al listar pacientes por profesional");
        return null;
    }
}

    public Map<Turno, Paciente> ordenarMapPorTurno(Map<Turno, Paciente> turnoYpaciente) {
        try {
            // Convierte el Map en una lista de pares clave-valor
            List<Map.Entry<Turno, Paciente>> listaTurnos = new ArrayList<>(turnoYpaciente.entrySet());

            // Defino un comparador con Turno
            Comparator<Map.Entry<Turno, Paciente>> comparadorTurno = (entry1, entry2) -> {
                Turno turno1 = entry1.getKey();
                Turno turno2 = entry2.getKey();

                // Comparo por fecha
                int comparacionFecha = turno1.getFecha().compareTo(turno2.getFecha());
                if (comparacionFecha != 0) {
                    return comparacionFecha;
                }

                // Si las fechas son iguales, comparar por hora
                return turno1.getHora().compareTo(turno2.getHora());
            };

            // Ordena la lista de pares clave-valor utilizando el comparador
            Collections.sort(listaTurnos, comparadorTurno);

            // Crea un nuevo LinkedHashMap para almacenar los elementos ordenados
            Map<Turno, Paciente> mapaOrdenado = new LinkedHashMap<>();
            for (Map.Entry<Turno, Paciente> entry : listaTurnos) {
                mapaOrdenado.put(entry.getKey(), entry.getValue());
            }

            return mapaOrdenado;
        } catch (Exception e) {
            System.out.println("Error al ordenar el mapa por Turno");
            return null;
        }
    }

    // Mapeo los turnos y paciente filtraos por profesional para el dia de hoy.
    // para la vista de turnos hoy
    public Map<Turno, Paciente> mapearPacientesXprofHoy(String idProfesional) throws MyException {

        // preparo la lsita de pacietnes que voy a devolver
        List<Paciente> pacientesXProfesional = listarPacientesXprof(idProfesional);
        
        List<Turno> turnos = new ArrayList<>();

        SimpleDateFormat formatoDeseado = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date fechaActual = new Date();
        fechaActual.setHours(0);
        fechaActual.setMinutes(0);
        fechaActual.setSeconds(0);
        

        Map<Turno, Paciente> turnoYpaciente = new HashMap<>();

        try {
            if (pacientesXProfesional != null) {
                for (Paciente paciente : pacientesXProfesional) {
                

                    turnos = paciente.getTurnos();
                    for (Turno turno : turnos) {

                      
                        if ((turno.getFecha()).equals(fechaActual.toLocaleString())) {
                            turnoYpaciente.put(turno, paciente);

                        }
                    }

                }
            }
            return ordenarMapPorTurno(turnoYpaciente);
        } catch (Exception e) {
            throw new MyException("Servicio paciente: Hubo un error al mapear pacientes por profesional");

        }

    }
    // Mapeo los turnos y paciente filtrados por profesional para la semana actual.
    // para la vista de turnos semanal

    public Map<Turno, Paciente> mapearPacientesXprofSemana(String idProfesional, List<AgendaSemanal> semana) {

        // preparo la lsita de pacietnes que voy a devolver
        List<Paciente> pacientesXProfesional = listarPacientesXprof(idProfesional);
        List<Turno> turnosPaciente = new ArrayList<>();
        // Repo de turnos
        //
        try {

            Map<Turno, Paciente> turnoYpaciente = new HashMap<>();

            AgendaSemanal sem = semana.get(0);

            Map<Date, DiaAgenda> fechasTturnos = sem.getFechasYTurnos();

            for (Map.Entry<Date, DiaAgenda> entry : fechasTturnos.entrySet()) {
                Date key = entry.getKey();
                DiaAgenda value = entry.getValue();

                for (Paciente paciente : pacientesXProfesional) {
                    if (paciente.getTurnos() != null) {
                        turnosPaciente = paciente.getTurnos();
                        for (Turno turno : turnosPaciente) {

                            if (value.getTurnos().contains(turno)) {
                                turnoYpaciente.put(turno, paciente);
                            }

                        }
                    }
                }
            }

              return ordenarMapPorTurno(turnoYpaciente);
        } catch (Exception e) {
            System.out.println("Servicio paciente: Hubo un error al mapear pacientes por profesional");
            return null;
        }

    }

    // Mapeo los turnos y paciente filtraos por profesional
    // para la vista de todos los turnos 
    public Map<Turno, Paciente> mapearPacientesXprofTodos(String idProfesional) {

        // preparo la lsita de pacietnes que voy a devolver
        List<Paciente> pacientesXProfesional = listarPacientesXprof(idProfesional);

        // Repo de turnos
        List<Turno> turnos = turnoServ.listarTurnosXProfesional(idProfesional);

        // en esa lista de turnos, cada turno tiene un profesional:
        Map<Turno, Paciente> turnoYpaciente = new HashMap<>();
        try {
            for (Paciente paciente : pacientesXProfesional) {
                if (paciente.getTurnos() != null) {
                    turnos = paciente.getTurnos();
                    for (Turno turno : turnos) {

                        turnoYpaciente.put(turno, paciente);

                    }
                }
            }
          return ordenarMapPorTurno(turnoYpaciente);
        } catch (Exception e) {
            System.out.println("Servicio paciente: Hubo un error al mapear pacientes por profesional");
            return null;
        }

    }

    private void validar(String grupoSanguineo, String obraSocial) throws MyException {

        if (grupoSanguineo == null || grupoSanguineo.isEmpty()) {
            throw new MyException("Debe completar el grupo de sangre");

        }
        // No se debería validar el turno para crear un paciente, puesto que esto es
        // algo que hace luego de estar registrado- Brenda
        // if(turnos == null || turnos.isEmpty()){
        // throw new MyException("Debe ingresar el turno pedido");
        // }

        if (obraSocial == null || obraSocial.isEmpty()) {
            throw new MyException("Debe ingresar si tiene obra social o no");
        }
    }

    // agrego esto para indicar permisos particulares de paciente (son los mismos
    // que el rol usuario)
    @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Paciente paciente = pacienteRepositorio.findByEmail(email);
        if (paciente != null && paciente.getActivo().equals(Boolean.TRUE)) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + paciente.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("pacientesession", paciente);

            return new User(paciente.getEmail(), paciente.getPassword(), permisos);

        } else {
            return null;
        }
    }

}
