package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ObraSocialRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.TurnoRepositorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PacienteServicio extends UsuarioServicio {

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
    public void registrarPaciente(MultipartFile archivo, String nombre, String apellido, String dni, String email,
            String password, String password2,
            String telefono, String direccion, String fecha_nac, String grupoSanguineo, String obraSocial)
            throws MyException, IOException {

        super.validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        validar(grupoSanguineo, obraSocial);

        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDni(dni);
        paciente.setEmail(email);
        paciente.setPassword(new BCryptPasswordEncoder().encode(password));
        paciente.setTelefono(telefono);
        paciente.setDireccion(direccion);
        paciente.setFecha_nac(pasarStringDate(fecha_nac));
        paciente.setActivo(true);
        paciente.setGrupoSanguineo(grupoSanguineo);

        // Crear obra social
        ObraSocial obraSocial2 = obraSocialServicio.crearObraSocialReturn(obraSocial, "Completar email",
                "Completar telefono");
        obraRepo.save(obraSocial2);
        paciente.setObraSocial(obraSocial2);

        // paciente.setTurnos(new ArrayList<Turno>());
        // paciente.setHistoria(new HistoriaClinica());
        paciente.setRol(Rol.USUARIO);
        Imagen imagen = imagenServicio.guardar(archivo);
        paciente.setImagen(imagen);
        pacienteRepositorio.save(paciente);

    }

    public Paciente getOne(String id) {
        return pacienteRepositorio.getOne(id);
    }

    @Transactional
    public void modificarPaciente(String id, MultipartFile archivo, String nombre, String apellido, String dni,
            String email,
            String password, String password2,
            String telefono, String direccion, String fecha_nac, String grupoSanguineo, String idObraSocial,
            String nombreObraSocial, String emailObraSocial, String telefonoObraSocial)
            throws MyException, IOException {

        Optional<Paciente> respuesta = pacienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Paciente pas = respuesta.get();
            // if (!super.buscarPorMail(email).getId().equals(prof.getId())) {
            // throw new MyException("EL mail ingresado ya existe en otro ususario! Ingreso
            // otro!");
            // }
            super.modificarUsuario(archivo, id, nombre, apellido, dni, email, password, password2, telefono, direccion,
                    fecha_nac);
            obraSocialServicio.modificarObraSocial(idObraSocial, nombreObraSocial, "Completar email",
                    "Completar telefono");
            pas.setGrupoSanguineo(grupoSanguineo);

            Imagen imagen = imagenServicio.actualizar(archivo, id);
            pas.setImagen(imagen);

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

    // *****COMPLETAR para traer los pacientes asociados a un profesional(id)
    public List<Paciente> listarPacientesXprof(String idProfesional) {
        // un paciente tiene una lista de turnos...
        List<Paciente> pacientes = new ArrayList<>();
        // traigo todos los pacientes
        pacientes = pacienteRepositorio.findAll();
        // preparo la lsita de pacietnes que voy a devolver
        List<Paciente> pacientesXProfesional = new ArrayList<>();

        // Repo de turnos
        List<Turno> turnos = new ArrayList<>();
        turnos = turnoRepositorio.findAll();

        // en esa lista de turnos, cada turno tiene un profesional:
        Profesional profesional = profesionalRepositorio.getOne(idProfesional);

        try {
            for (Paciente paciente : pacientes) {
                if (paciente.getTurnos() != null) {
                    turnos = paciente.getTurnos();
                    for (Turno turno : turnos) {
                        if (turno.getProfesional().getId().equals(profesional.getId())) {
                            pacientesXProfesional.add(paciente);

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

    // Mapeo los turnos y paciente filtraos por profesional para el dia de hoy.
    // para la vista de turnos hoy
    public Map<Turno, Paciente> mapearPacientesXprofHoy(String idProfesional) {
        // un paciente tiene una lista de turnos...
        List<Paciente> pacientes = new ArrayList<>();
        // traigo todos los pacientes
        pacientes = pacienteRepositorio.findAll();
        // preparo la lsita de pacietnes que voy a devolver
        List<Paciente> pacientesXProfesional = listarPacientesXprof(idProfesional);

        // Repo de turnos
        List<Turno> turnos = turnoServ.listarTurnosXProfesional(idProfesional);

        // en esa lista de turnos, cada turno tiene un profesional:
        Profesional profesional = profesionalRepositorio.getOne(idProfesional);
        Map<Turno, Paciente> turnoYpaciente = new HashMap<>();
        try {
            for (Paciente paciente : pacientes) {
                if (paciente.getTurnos() != null) {
                    turnos = paciente.getTurnos();
                    for (Turno turno : turnos) {
                        if (turno.getFecha().equals(new Date())) {
                            turnoYpaciente.put(turno, paciente);

                        }
                    }
                }
            }
            return turnoYpaciente;
        } catch (Exception e) {
            System.out.println("Servicio paciente: Hubo un error al mapear pacientes por profesional");
            return null;
        }

    }
    // Mapeo los turnos y paciente filtrados por profesional para la semana actual.
    // para la vista de turnos semanal

    public Map<Turno, Paciente> mapearPacientesXprofSemana(String idProfesional, List<AgendaSemanal> semana) {
        // un paciente tiene una lista de turnos...
        List<Paciente> pacientes = new ArrayList<>();
        // traigo todos los pacientes
        pacientes = pacienteRepositorio.findAll();
        // preparo la lsita de pacietnes que voy a devolver
        List<Paciente> pacientesXProfesional = listarPacientesXprof(idProfesional);
        List<Turno> turnosPaciente = new ArrayList<>();
        // Repo de turnos
//       
        try {
            Profesional profesional = profesionalRepositorio.getOne(idProfesional);
            Map<Turno, Paciente> turnoYpaciente = new HashMap<>();
            AgendaSemanal sem = semana.get(0);
            Map<Date, DiaAgenda> fechasTturnos = sem.getFechasYTurnos();
            for (Map.Entry<Date, DiaAgenda> entry : fechasTturnos.entrySet()) {
                Date key = entry.getKey();
                DiaAgenda value = entry.getValue();
                
                for (Paciente paciente : pacientes) {
                    if (paciente.getTurnos() != null) {
                        turnosPaciente = paciente.getTurnos();
                        for (Turno turno : turnosPaciente) {
                               
                            if(value.getTurnos().contains(turno)){ 
                                turnoYpaciente.put(turno, paciente);
                               }
                            
                        }
                    }
                }
            }
            

            return turnoYpaciente;
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

}
