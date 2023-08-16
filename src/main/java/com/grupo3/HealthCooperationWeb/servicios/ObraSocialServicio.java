/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ObraSocialRepositorio;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ezequiel
 */
@Service
public class ObraSocialServicio {

    @Autowired
    private ObraSocialRepositorio obraRepo;

    public void crearObraSocial(String nombre, String email, String telefono) throws MyException {
        // Se validan los datos ingresados
        validar(nombre, email, telefono);
        ObraSocial obra = new ObraSocial();
        obra.setNombre(nombre);
        obra.setEmail(email);
        obra.setTelefono(telefono);

        obraRepo.save(obra);

    }

    public ObraSocial getObraSocialById(String id) throws MyException {
           return obraRepo.getById(id);
    }

    public List<ObraSocial> listarObrasSociales() {
        
        List<ObraSocial> obras = new ArrayList();

        try {
            obras = obraRepo.findAll();           

            return obras;

        } catch (Exception e) {
            System.out.println("No pudieron ser listadas");
            return null;
        }


    }

    public void modificarObraSocial(String id,String nombre, String email, String telefono) throws MyException {
        
         validar(nombre, email, telefono);
       Optional<ObraSocial> respuesta = obraRepo.findById(id);
        if (respuesta.isPresent()) {
             ObraSocial obra = (ObraSocial)respuesta.get();
            if (!obraRepo.buscarPorEmail(email).getId().equals(obra.getId())) {
                throw new MyException("EL mail ingresado ya existe en otra Obra social! Ingreso otro!");
            }
        obra.setNombre(nombre);
        obra.setEmail(email);
        obra.setTelefono(telefono);

        obraRepo.save(obra);

    }
    }

    public void eliminarObraSocial(String id) throws MyException {
        
        try {
             Optional<ObraSocial> respuesta = obraRepo.findById(id);
            if (respuesta.isPresent()) {
                      ObraSocial obra = (ObraSocial)respuesta.get();
               obraRepo.delete(obra);
            }
        } catch (Exception e) {
            System.out.println("No es posible eliminar el ususario");
        }
    }

    // Metodo para validar los datos ingresados antes de persistirlos
    private void validar(String nombre, String email, String telefono) throws MyException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar su nombre");
        }

        if (email == null || email.isEmpty()) {
            throw new MyException("Debe ingresar un email");
        }

        if (telefono == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }

    }
}
