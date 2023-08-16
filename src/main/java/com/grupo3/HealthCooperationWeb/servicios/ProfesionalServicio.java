package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.AgendaSemanal;
import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.UsuarioRepositorio;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfesionalServicio extends UsuarioServicio {

    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;
    @Autowired
    private ImagenServicio imagenServ; //
    @Autowired
    private UsuarioRepositorio usuarioRepo;

    // listar todos los médicos
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


    @Transactional
    public void registrarProfesional(MultipartFile archivo, String nombre, String apellido, String dni, String email, String password,
            String password2, String telefono, String direccion, String fecha_nac, Especialidad especialidad) throws MyException {

        super.validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        Profesional prof = (Profesional) super.crearUsuario(archivo, nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);

        if (especialidad == null) {
            throw new MyException("Debe ingresar una especialidad al profesional");
        }
        prof.setEspecialidad(especialidad);

        prof.setAgenda(new AgendaSemanal());
        prof.setOferta(new Oferta());
        prof.setDiasDisponibles(new ArrayList());
        prof.setRol(Rol.MODERADOR);
        Imagen imagen = imagenServ.guardar(archivo);
        prof.setImagen(imagen);

        profesionalRepositorio.save(prof);

    }
    
    @Transactional
    // mopdificamos como si fuera un usuario, luego metodos especificos cambiar cada cosa
    public void modificarProfesional(String id,MultipartFile archivo, String nombre, 
            String apellido, String dni, String email, String password,
            String password2, String telefono, String direccion, String fecha_nac) throws MyException, IOException {        
        super.validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
    
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Profesional prof = respuesta.get();
            if (!usuarioRepo.buscarPorEmail(email).getId().equals(prof.getId())) {
                throw new MyException("EL mail ingresado ya existe en otro ususario! Ingreso otro!");
            }
        super.validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        super.modificarUsuario(archivo, id, nombre, apellido, dni, email, password, password2, telefono, 
                direccion, fecha_nac);       

        Imagen imagen = imagenServ.guardar(archivo);
        prof.setImagen(imagen);

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
// Métodos relacionados con el profesional

}
