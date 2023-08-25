package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ObraSocialRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PacienteServicio extends UsuarioServicio {

    @Autowired
    private PacienteRepositorio pacienteRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private ObraSocialRepositorio obraRepo;
    @Autowired
    ObraSocialServicio obraSocialServicio;

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

    @Transactional
    // mopdificamos como si fuera un profesional, luego metodos especificos cambiar
    // cada cosa
    public void modificarPaciente(String id, MultipartFile archivo, String nombre, String apellido, String dni,
            String email,
            String password, String password2,
            String telefono, String direccion, String fecha_nac, String grupoSanguineo, String obraSocial)
            throws MyException, IOException {
        super.validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);

        Optional<Paciente> respuesta = pacienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Paciente pas = respuesta.get();
            // if (!super.buscarPorMail(email).getId().equals(prof.getId())) {
            // throw new MyException("EL mail ingresado ya existe en otro ususario! Ingreso
            // otro!");
            // }
            super.validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
            super.modificarUsuario(archivo, id, nombre, apellido, dni, email, password, password2, telefono, direccion,
                    fecha_nac);
            pas.setGrupoSanguineo(grupoSanguineo);

            // FALTA PENSAR ESTO
            // ObraSocial obraSocial1 = new ObraSocial();
            // obraSocial1.setNombre(obraSocial);
            // pas.setObraSocial(obraSocial1);

            Imagen imagen = imagenServicio.actualizar(archivo, id);
            pas.setImagen(imagen);

            pacienteRepositorio.save(pas);

        }
    }

    // se muestran todos que son activos por ddefecto, no se pueden dar de baja
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
    public List<Paciente> listarPacientesXprof(String id) {

        // List<Paciente> aux = new ArrayList<>();
        // List<Paciente> pacientes = new ArrayList<>();
        //
        // try {
        // aux = (ArrayList<Paciente>) pacienteRepositorio.findAll();
        // for (Paciente paciente : aux) {
        // if (paciente.getActivo().equals(Boolean.TRUE)) {
        // pacientes.add(paciente);
        // }
        // }
        // return pacientes;
        // } catch (Exception e) {
        // System.out.println("Hubo un error al listar profesionales.");
        // return null;
        // }
        return null;
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
