package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.DiaAgenda;
import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.enumeradores.Dias;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfesionalServicio extends UsuarioServicio {

    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;
    @Autowired
    private ImagenServicio imagenServ; //
    @Autowired
    private OfertaServicio servOferta;
    @Autowired
    private ObraSocialServicio servObra;
     @Autowired
    private UsuarioServicio usuarioServicio;

    // listar todos los médicos ACTIVOS
    @Transactional
    public List<Profesional> listarProfesionales() {
        List<Profesional> profAux = new ArrayList<>();
        List<Profesional> profesionales = new ArrayList<>();

        try {
            profAux = (ArrayList<Profesional>) profesionalRepositorio.findAll();
            for (Profesional profesional : profAux) {
                if (profesional.getActivo().equals(Boolean.TRUE)) {
                    profesionales.add(profesional);
                }
            }
            return profesionales;
        } catch (Exception e) {
            System.out.println("Hubo un error al listar profesionales.");
            return null;
        }
    }

    public Profesional getOne(String id) {
        return profesionalRepositorio.getOne(id);
    }

    private Especialidad pasarStringEspecialidad(String espe) throws MyException {
        switch (espe) {
            case "PEDIATRÍA":
                return Especialidad.PEDIATRÍA;
            case "GINECOLOGÍA":
                return Especialidad.GINECOLOGÍA;
            case "CLÍNICA":
                return Especialidad.CLÍNICA;
            case "CARDIOLOGÍA":
                return Especialidad.CARDIOLOGÍA;
            default:
                throw new MyException("Especialidad no válida: " + espe);
        }
    }

    @Transactional
    // el administrador crea un Profesional, luego ´ste actualiza sus atributos
    // particulares
    public void registrarProfesional(MultipartFile archivo, String nombre, String apellido, String dni, String email,
            String password,
            String password2, String telefono, String direccion, String fecha_nac, String especialidad,
            String valorConsulta) throws MyException, IOException, ParseException {
        // Se validan los datos especificos de profesional
        // faltaria descripcion
        super.validar("1", nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        Date fecha = pasarStringDate(fecha_nac);
        if (!validarFecha(fecha)) {
            throw new MyException("la fecha no es válida");
        }
        if (usuarioServicio.buscarPorDni(dni) != null) {
            throw new MyException("Existe un usuario con el N° de docuemnto!");
        }
        Profesional profesional = new Profesional();
        profesional.setNombre(nombre);
        profesional.setApellido(apellido);
        profesional.setDni(dni);
        profesional.setEmail(email);
        profesional.setPassword(new BCryptPasswordEncoder().encode(password));
        profesional.setTelefono(telefono);
        profesional.setDireccion(direccion);
        profesional.setFecha_nac(fecha);
        profesional.setActivo(true);
        profesional.setReputacion(0);
        if (especialidad == null) {

            throw new MyException("Debe ingresar una especialidad al profesional");
        }
        profesional.setEspecialidad(pasarStringEspecialidad(especialidad));
        profesional.setValorConsulta(valorConsulta);
        profesional.setRol(Rol.MODERADOR);

        if (archivo.isEmpty()) {
            // Si el archivo está vacío, crea el paciente con una imagen predeterminada
            Imagen imagenPredeterminada = obtenerImagenPredeterminada(); // Implementa esta función para obtener la
            // imagen predeterminada
            profesional.setImagen(imagenPredeterminada);
        } else {
            // Si el archivo no está vacío, crea el paciente con la imagen proporcionada
            Imagen imagen = imagenServ.guardar(archivo);
            profesional.setImagen(imagen);
        }

        profesionalRepositorio.save(profesional);

    }

    @Transactional
    // mopdificamos como si fuera un profesional, luego metodos especificos cambiar
    // cada cosa
    public void modificarProfesional(String id, MultipartFile archivo, String nombre,
            String apellido, String dni, String email, String password,
            String password2, String telefono, String direccion, String fecha_nac,
            String especialidad, String valorConsulta, String descripcion) throws MyException, IOException, ParseException {

        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Profesional prof = respuesta.get();
            super.validar(id, nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
            super.modificarUsuario(archivo, id, nombre, apellido, dni, email, password, password2, telefono, direccion,
                    fecha_nac);
            if (especialidad == null) {

                throw new MyException("Debe ingresar una especialidad al profesional");
            }

            prof.setEspecialidad(pasarStringEspecialidad(especialidad));
            prof.setValorConsulta(valorConsulta);
            prof.setDescripcion(descripcion);
            String idImg = null;
            if (prof.getImagen() != null) {
                idImg = prof.getImagen().getId();
            }
            if (archivo != null && archivo.getBytes().length != 0) {
                Imagen imagen = imagenServ.actualizar(archivo, id);
                prof.setImagen(imagen);
            } else {
                // No se proporcionó un archivo nuevo, no se actualiza la imagen del usuario
            }

            profesionalRepositorio.save(prof);

        }
    }

    @Transactional
    // no sé si este método funcione por el enum, la dejo porque es una opcion breve
    // si no funciona, solo hay que copiar la opcion 2 y quitarle el método
    // ordenarPorPrecio
    public List<Profesional> buscarPorEspecialidad(Especialidad especialidad) {
        return profesionalRepositorio.findByEspecialidad(especialidad);
    }

    // opcion 2:
    @Transactional
    public List<Profesional> ordenarEspecialidadYPrecio(String especialidad) {
        ArrayList<Profesional> profesionales = (ArrayList<Profesional>) profesionalRepositorio.findAll();

        try {
            if (especialidad.equalsIgnoreCase("pediatría")) {
                for (Profesional profesional : profesionales) {
                    if (profesional.getEspecialidad().equals(Especialidad.PEDIATRÍA)) {
                        ArrayList<Profesional> pediatras = new ArrayList<>();
                        pediatras.add(profesional);
                        return ordenarPorValorConsulta(pediatras);
                    }
                }
            }

            if (especialidad.equalsIgnoreCase("ginecología")) {
                for (Profesional profesional : profesionales) {
                    if (profesional.getEspecialidad().equals(Especialidad.GINECOLOGÍA)) {
                        ArrayList<Profesional> ginecos = new ArrayList<>();
                        ginecos.add(profesional);
                        return ordenarPorValorConsulta(ginecos);
                    }
                }
            }

            if (especialidad.equalsIgnoreCase("clínica")) {
                for (Profesional profesional : profesionales) {
                    if (profesional.getEspecialidad().equals(Especialidad.CLÍNICA)) {
                        ArrayList<Profesional> clinicos = new ArrayList<>();
                        clinicos.add(profesional);
                        return ordenarPorValorConsulta(clinicos);
                    }
                }
            }

            if (especialidad.equalsIgnoreCase("cardiología")) {
                for (Profesional profesional : profesionales) {
                    if (profesional.getEspecialidad().equals(Especialidad.CARDIOLOGÍA)) {
                        ArrayList<Profesional> cardios = new ArrayList<>();
                        cardios.add(profesional);
                        return ordenarPorValorConsulta(cardios);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(
                    "No hay médicos disponibles para esa especialidad o no ingresó correctamente la especialidad.");
        }

        return null;
    }

    @Transactional
    public List<Profesional> ordenarPorValorConsulta(ArrayList<Profesional> profesionales) {
        // -----ArrayList<Profesional> profesionales = (ArrayList<Profesional>)
        // profesionalRepositorio.findAll();
        // Ordenar los profesionales por el valor de consulta usando Collections.sort()
        Collections.sort(profesionales, Comparator.comparing(Profesional::getValorConsulta));
        return profesionales;
    }

    @Transactional
    public List<Profesional> ordenarPorValorConsulta() {
        ArrayList<Profesional> profesionales = (ArrayList<Profesional>) profesionalRepositorio.findAll();
        Collections.sort(profesionales, Comparator.comparing(Profesional::getValorConsulta));
        return profesionales;
    }

    @Transactional
    public String darDeBajaProfesional(String id) {

        try {
            Optional<Profesional> respuesta = profesionalRepositorio.findById(id);

            if (respuesta.isPresent()) {
                Profesional profesional = (Profesional) (respuesta.get());
                profesional.setActivo(false); // Establecer el atributo activo como false
                profesionalRepositorio.save(profesional);
                return "Profesional dado de baja exitosamente";

            }
        } catch (Exception e) {
            System.out.println("El id ingresado es inválido");
        }
        return "Lo sentimos, no fue posible dar de baja al profesional";

    }

    // FALTAN METODOS PARA LA AGENDA, LA OFERTA Y DIAS DISPONIBLES
    private List<Dias> pasarDiasEnum(ArrayList<String> diasSeleccionados) throws MyException {
        List<Dias> diasEnum = new ArrayList<>();
        for (String diasSeleccionado : diasSeleccionados) {
            switch (diasSeleccionado.toUpperCase()) {
                case "LUNES":
                    diasEnum.add(Dias.LUNES);
                    break;
                case "MARTES":
                    diasEnum.add(Dias.MARTES);
                    break;
                case "MIERCOLES":
                    diasEnum.add(Dias.MIERCOLES);
                    break;
                case "JUEVES":
                    diasEnum.add(Dias.JUEVES);
                    break;
                case "VIERNES":
                    diasEnum.add(Dias.VIERNES);
                    break;
                default:
                    throw new MyException("Dia no valido: " + diasSeleccionado);
            }
        }
        return diasEnum;

    }

    @Transactional
    public void asignarDisponibilidad(String id, ArrayList<String> diasSeleccionados) throws MyException {

        if (diasSeleccionados == null || diasSeleccionados.isEmpty()) {
            throw new MyException("Debe seleccionar al menos un día disponible");
        }
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Profesional profesional = (Profesional) (respuesta.get());

            List<Dias> diasDisponibles = pasarDiasEnum(diasSeleccionados);
            profesional.setDiasDisponibles(diasDisponibles);

        }

    }

    @Transactional
    public void asignarAgenda(String id, List<AgendaSemanal> semanas) throws MyException {

        if (semanas == null || semanas.isEmpty()) {
            throw new MyException("la agenda semanal no puede estar vacia");
        }
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Profesional profesional = (Profesional) (respuesta.get());

            profesional.setAgendasSemanales(semanas);
        }

    }

    public boolean existeSemana(AgendaSemanal semana, Profesional prof) {

        List<AgendaSemanal> agendasProf = prof.getAgendasSemanales();
        for (AgendaSemanal agendaSemanal : agendasProf) {
            Map<Date, DiaAgenda> fechas = agendaSemanal.getFechasYTurnos();
            for (Map.Entry<Date, DiaAgenda> entry : fechas.entrySet()) {
                Date key = entry.getKey();
                DiaAgenda value = entry.getValue();
                if (semana.getFechasYTurnos().containsKey(key)) {
                    return true;
                }

            }
        }
        return false;

    }

    @Transactional
    public void asignarOferta(String id, String horaInicial, String horaFinal,
            String duracion, String tipoOferta, String direccion,
            List<String> selecciones) throws MyException {

        Oferta oferta = servOferta.crearOferta(tipoOferta, horaInicial,
                horaFinal, duracion, direccion, servObra.pasarObras(selecciones));
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Profesional profesional = (Profesional) (respuesta.get());
            profesional.setOferta(oferta);
        }
    }

    // agrego esto para indicar permisos particulares de profesional (rol MODERADOR)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profesional profesional = profesionalRepositorio.findByEmail(email);
        if (profesional != null && profesional.getActivo().equals(Boolean.TRUE)) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + profesional.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("pacientesession", profesional);

            return new User(profesional.getEmail(), profesional.getPassword(), permisos);

        } else {
            return null;
        }
    }

    @Transactional
    public void valorar(String id, String valor) throws MyException {
        try {
            Integer valoracion = Integer.parseInt(valor);

            if (valoracion >= 1 && valoracion <= 5) {
                Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
                if (respuesta.isPresent()) {
                    Profesional profesional = respuesta.get();
                    if (profesional.getReputacion() == 0) {
                        profesional.setReputacion(valoracion);
                    } else {
                        Integer reputacion = Math.round((profesional.getReputacion() + valoracion) / 2);
                        profesional.setReputacion(reputacion);

                    }
                    profesionalRepositorio.save(profesional);
                    // Realiza cualquier otra operación necesaria con el profesional
                } else {
                    throw new MyException("Profesional no encontrado");
                }
            } else {
                throw new MyException("La valoración debe estar en el rango de 1 a 5");
            }
        } catch (NumberFormatException ex) {
            throw new MyException("El valor de valoración no es un número válido");
        }
    }

}
