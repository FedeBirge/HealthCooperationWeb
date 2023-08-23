package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/imagen") //
public class ImagenControlador {

    @Autowired
    private UsuarioServicio userServ; // inyectamos el servicio de usuario

    @GetMapping("/perfil/{id}") // ver imagen de perfil de usuario
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {

        Usuario usuario = userServ.getOne(id);
        if(usuario.getImagen()!=null){
        byte[] imagen = usuario.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        }
        else{
        return null;
        }
    }

}
