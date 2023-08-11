package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepo; // Repositorio de usuarios

    @Transactional
    // Metodo para crear un usuario
    public void crearUsuario(String nombre, String apellido, String dni, String email,
            String password, String password2, String telefono, String direccion, String fecha_nac) throws MyException {
        // Se validan los datos ingresados
        validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setEmail(email);
        // usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setFecha_nac(pasarStringDate(fecha_nac));
        usuario.setActivo(true);

        // QUEDA DEFINIR EL ROL DE ACUEROD A COMO SE AVANCE
        // if (rol.equals("ADMIN")) {
        // usuario.setRol(Rol.ADMIN);
        // } else {
        // usuario.setRol(Rol.USER);
        // }
        // AUN FALTA LA ENTIDAD IMAGEN
        // Imagen imagen = imagenServ.guardar(archivo);
        // usuario.setImagen(imagen);

        usuarioRepo.save(usuario);

    }

    @Transactional
    // Metodo para modificar un usuario
    public void modificarUsuario(String id, String nombre, String apellido, String dni, String email,
            String password, String password2, String telefono, String direccion, String fecha_nac)
            throws MyException, IOException {
        // Se validan los datos ingresados
        validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            if (!usuarioRepo.buscarPorEmail(email).getId().equals(usuario.getId())) {
                throw new MyException("EL mail ingresado ya existe en otro ususario! Ingreso otro!");
            }
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            // usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setActivo(true);
            // if (rol.equals("ADMIN")) {
            // usuario.setRol(Rol.ADMIN);
            // }
            // if (rol.equals("USER")) {
            // usuario.setRol(Rol.USER);
            // }
            // if (rol.equals("JOURNALIST")) {
            // usuario.setRol(Rol.JOURNALIST);
            // }
            // String idImg = null;
            // if (usuario.getImagen() != null) {
            // idImg = usuario.getImagen().getId();
            // }
            // if (archivo.getBytes().length != 0 ) {
            // Imagen imagen = imagenServ.actualizar(archivo, idImg);
            // usuario.setImagen(imagen);
            // }

            usuarioRepo.save(usuario);
        }

    }

    @Transactional
    // Metodo para eliminar un usuario, se cambia el estado a inactivo
    public void eliminarUsuario(String id) {
        try {
            Optional<Usuario> resp = usuarioRepo.findById(id);
            if (resp.isPresent()) {
                Usuario user = (Usuario) (resp.get());
                user.setActivo(Boolean.FALSE);
                usuarioRepo.save(user);
            }
        } catch (Exception e) {
            System.out.println("No es posible eliminar el ususario");
        }
    }

    // Metodo para listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        List<Usuario> aux = new ArrayList();
        List<Usuario> usuarios = new ArrayList();

        try {
            aux = usuarioRepo.findAll();
            for (Usuario usuario : aux) {
                if (usuario.getActivo().equals(Boolean.TRUE)) {
                    usuarios.add(usuario);
                }
            }

            return usuarios;

        } catch (Exception e) {
            System.out.println("No pudieron ser listados los usuarios");
            return null;
        }

    }

    // Metodo para validar los datos ingresados
    protected void validar(String nombre, String apellido, String dni, String email,
            String password, String password2, String telefono, String direccion, String fecha_nac) throws MyException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar su nombre");
        }
        if (apellido == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar su apellido");
        }
        if (dni == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar su N° de documento");
        }

        if (email == null || email.isEmpty()) {
            throw new MyException("Debe ingresar un email");
        }
        if (usuarioRepo.buscarPorEmail(email) != null) {
            throw new MyException("EL mail ingresado ya existe! Ingreso otro!");
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            throw new MyException("Debe ingresar una contraseña válida");
        }
        if (password2 == null || password2.isEmpty() || password2.length() < 6) {
            throw new MyException("Debe ingresar una contraseña");
        }
        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas no coinciden");
        }
        if (telefono == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }
        if (direccion == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }
        if (fecha_nac == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }
        // if (rol == null || rol.isEmpty() || rol.equals("Seleccione Rol")) {
        // throw new MyException("Debe ingresar un rol");
        // }

    }

    // Metodo para buscar un usuario por su id
    public Usuario getOne(String id) {
        return usuarioRepo.getOne(id);
    }

    // Pasar un string a date
    private Date pasarStringDate(String fecha) {

        String pattern = "yyyy-MM-dd"; // Formato de la cadena de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            Date date = dateFormat.parse(fecha);

            return date;
        } catch (ParseException e) {
            return null;
        }
    }
}
