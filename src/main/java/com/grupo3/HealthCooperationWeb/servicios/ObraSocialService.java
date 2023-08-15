/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.repositorios.ObraSocialRepository;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ezequiel
 */
@Service
public class ObraSocialService {
    @Autowired
    private ObraSocialRepository obraSocialRepository;

    public ObraSocial createObraSocial(ObraSocial obraSocial) {
        // Validar que el nombre de la obra social no esté vacío
        if (obraSocial.getNombre() == null || obraSocial.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la obra social no puede estar vacío.");
        }
        return obraSocialRepository.save(obraSocial);
    }

    public ObraSocial getObraSocialById(String id) throws MyException {
    // Manejar la excepción si no se encuentra la obra social
    return obraSocialRepository.findById(id).orElseThrow(() ->
        new MyException("Obra social no encontrada con ID: " + id)
    );
}


    public List<ObraSocial> getAllObrasSociales() {
        return obraSocialRepository.findAll();
    }

    public ObraSocial updateObraSocial(String id, ObraSocial updatedObraSocial) throws MyException {
        ObraSocial existingObraSocial = obraSocialRepository.findById(id).orElseThrow(() ->
            new MyException("Obra social no encontrada con ID: " + id)
        );
        // Validar que el nombre de la obra social no esté vacío
        if (updatedObraSocial.getNombre() == null || updatedObraSocial.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la obra social no puede estar vacío.");
        }
        existingObraSocial.setNombre(updatedObraSocial.getNombre());
        existingObraSocial.setEmail(updatedObraSocial.getEmail());
        existingObraSocial.setTelefono(updatedObraSocial.getTelefono());
        return obraSocialRepository.save(existingObraSocial);
    }

    public void deleteObraSocial(String id) throws MyException {
        ObraSocial obraSocial = obraSocialRepository.findById(id).orElseThrow(() ->
            new MyException("Obra social no encontrada con ID: " + id)
        );
        obraSocialRepository.delete(obraSocial);
    }
}
