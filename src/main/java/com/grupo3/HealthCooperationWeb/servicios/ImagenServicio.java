package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ImagenRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
// Servicio de Imagen
public class ImagenServicio {

    @Autowired
    ImagenRepositorio imagenRepo; // inyectamos el repositorio de imagen

    // Guardar imagen
    public Imagen guardar(MultipartFile archivo) throws MyException {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepo.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());

            }
        }
        return null;
    }

    // Actualizar imagen
    public Imagen actualizar(MultipartFile archivo, String id) throws MyException {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                if (id != null) {
                    Optional<Imagen> resp = imagenRepo.findById(id);
                    if (resp.isPresent()) {
                        imagen = resp.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepo.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());

            }
        }
        return null;
    }
}
