package com.grupo3.HealthCooperationWeb.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.UsuarioRepositorio;

@Service
@Transactional
public class AdminServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    public void modificarRol(String id, String rol) throws MyException {
        try {
            Usuario usuario = new Usuario();
            usuario = getOne(id);
            usuario.setRol(pasarStringRol(rol));
            usuarioRepo.save(usuario);
        } catch (Exception e) {
            System.out.println("Error al cambiar rol" + e.getMessage());
        }

    }

    private Usuario getOne(String id) {
        return usuarioRepo.getOne(id);
    }

    public Rol pasarStringRol(String rol) throws MyException {
        switch (rol) {
            case "ADMINISTRADOR":
                return Rol.ADMINISTRADOR;
            case "MODERADOR":
                return Rol.MODERADOR;
            case "USUARIO":
                return Rol.USUARIO;
            default:
                throw new MyException("Rol no válido: " + rol);
        }
    }
}