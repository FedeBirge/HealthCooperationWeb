
package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ObraSocialRepositorio;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObraSocialServicio {

    @Autowired
    private ObraSocialRepositorio obraRepo;

    @Transactional
    public void crearObraSocial(String nombre, String email, String telefono) throws MyException {
        // Se validan los datos ingresados
        validar(nombre, email, telefono);
        ObraSocial obra = new ObraSocial();
        obra.setNombre(nombre);
        obra.setEmail(email);
        obra.setTelefono(telefono);

        obraRepo.save(obra);

    }

    @Transactional
    public ObraSocial crearObraSocialReturn(String nombre, String email, String telefono) throws MyException {
        // Se validan los datos ingresados
        validar(nombre, email, telefono);
        ObraSocial obra = new ObraSocial();
        obra.setNombre(nombre);
        obra.setEmail(email);
        obra.setTelefono(telefono);

        obraRepo.save(obra);
        return obra;
    }

    public ObraSocial getObraSocialById(String id) throws MyException {
        return obraRepo.getById(id);
    }

    public List<String> listarNombreObrasSociales() {
        List<ObraSocial> obras = new ArrayList();
        List<String> nombres = new ArrayList();

        obras = obraRepo.findAll();
        for (ObraSocial obra : obras) {
            nombres.add(obra.getNombre());

        }
        return nombres;

    }

    public List<ObraSocial> listarObrasSociales() throws MyException {

        List<ObraSocial> obras = new ArrayList();

        List<ObraSocial> aux = new ArrayList();

        try {
            obras = obraRepo.findAll();
            for (ObraSocial obra : obras) {
                if (!obra.getNombre().equals("Particular")) {
                    aux.add(obra);
                }

            }
            return aux;

        } catch (Exception e) {
            throw new MyException("No pudieron ser listadas");

        }

    }

    @Transactional
    public void modificarObraSocial(String id, String nombre, String email, String telefono) throws MyException {

        validar(nombre, email, telefono);
        Optional<ObraSocial> respuesta = obraRepo.findById(id);
        if (respuesta.isPresent()) {
            ObraSocial obra = (ObraSocial) respuesta.get();
            if (!obraRepo.buscarPorEmail(email).getId().equals(obra.getId())) {
                throw new MyException("EL mail ingresado ya existe en otra Obra social! Ingreso otro!");
            }
            obra.setNombre(nombre);
            obra.setEmail(email);
            obra.setTelefono(telefono);

            obraRepo.save(obra);

        }
    }

    public ObraSocial buscarXNombre(String nombre) {
        return obraRepo.findByNombre(nombre);
    }

    public List<ObraSocial> pasarObras(List<String> selecciones) throws MyException {
        List<ObraSocial> obras = new ArrayList<>();
        List<String> nombres = listarNombreObrasSociales();

        for (String selc : selecciones) {

            if (nombres.contains(selc)) {
                obras.add(buscarXNombre(selc));
            } else {
                obras.add(crearObraSocialReturn(selc, "mail", "tel"));
            }

        }
        return obras;

    }

    @Transactional
    public void eliminarObraSocial(String id) throws MyException {

        try {
            Optional<ObraSocial> respuesta = obraRepo.findById(id);
            if (respuesta.isPresent()) {
                ObraSocial obra = (ObraSocial) respuesta.get();
                obraRepo.delete(obra);
            }
        } catch (Exception e) {
            System.out.println("No es posible eliminar el ususario");
        }
    }

    // Metodo para validar los datos ingresados antes de persistirlos
    private void validar(String nombre, String email, String telefono) throws MyException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar nombre de la obra social");
        }

        if (email == null || email.isEmpty()) {
            throw new MyException("Debe ingresar email del a obra social");
        }

        if (telefono == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar telefono de la obra social");
        }

    }
}
