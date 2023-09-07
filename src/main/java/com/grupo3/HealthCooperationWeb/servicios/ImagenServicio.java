package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ImagenRepositorio;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
// Servicio de Imagen
public class ImagenServicio {

    @Autowired
    ImagenRepositorio imagenRepo; // inyectamos el repositorio de imagen

    // Guardar imagen
    @Transactional
    public Imagen guardar(MultipartFile archivo) throws MyException, IOException {
        try {
            Imagen imagen = new Imagen();

            if (archivo != null && !archivo.isEmpty()) {
                // Si se proporciona un archivo y no está vacío, guarda la imagen desde el archivo
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
            } else {
                // Si el archivo está vacío o no se proporciona, guarda la imagen predeterminada
                // Puedes obtener la imagen predeterminada desde una ruta de archivo o URL
                String urlPredeterminada = "classpath:static/img/user_logo.png"; // Cambia la ruta según tu imagen predeterminada
                File file = new File(urlPredeterminada);
                if (file.exists()) {
                    imagen.setMime(Files.probeContentType(file.toPath()));
                    imagen.setNombre(file.getName());
                    imagen.setContenido(Files.readAllBytes(file.toPath()));
                }
            }

            return imagenRepo.save(imagen);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new MyException("Error al guardar la imagen");
        }
    }

    // Actualizar imagen
    @Transactional
    public Imagen actualizar(MultipartFile archivo, String id) throws MyException {
        try {
            Imagen imagen = new Imagen();
            if (id != null) {
                Optional<Imagen> resp = imagenRepo.findById(id);
                if (resp.isPresent()) {
                    imagen = resp.get();
                }
            }

            if (!archivo.isEmpty()) {
                // Si se proporciona un archivo y no está vacío, actualiza la imagen desde el archivo
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepo.save(imagen);
            } else {
                // Si el archivo está vacío o no se proporciona, no se realiza ninguna actualización
                return imagen;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new MyException("Error al actualizar la imagen");
        }
    }

    @Transactional
    public void eliminar(MultipartFile archivo, String id) throws MyException {
        if (archivo.isEmpty()) {

        } else {
            try {
                Imagen imagen = new Imagen();
                if (id != null) {
                    Optional<Imagen> resp = imagenRepo.findById(id);
                    if (resp.isPresent()) {
                        imagen = resp.get();
                    }
                    imagenRepo.delete(imagen);
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());

            }
        }

    }

    public Imagen getOne(String id) {
        return imagenRepo.getOne(id);
    }

}
