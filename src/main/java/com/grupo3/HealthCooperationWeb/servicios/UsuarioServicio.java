package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Primary
@Service
// Srping Security: para autenticar extiendo de UserDetailsService
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepo; // Repositorio de usuarios

    @Autowired
    private ImagenServicio imagenServ; //

    @Transactional
    // Metodo para crear un usuario
    public Usuario crearUsuario(MultipartFile archivo, String nombre, String apellido, String dni, String email,
            String password, String password2, String telefono, String direccion, String fecha_nac) throws MyException {
        // Se validan los datos ingresados
        validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setFecha_nac(pasarStringDate(fecha_nac));
        usuario.setActivo(true);

        usuario.setRol(Rol.USUARIO);

        Imagen imagen = imagenServ.guardar(archivo);
        usuario.setImagen(imagen);
        return usuarioRepo.save(usuario);

    }

    @Transactional
    // Metodo para modificar un usuario
    public void modificarUsuario(MultipartFile archivo, String id, String nombre, String apellido, String dni,
            String email, String password, String password2, String telefono, String direccion, String fecha_nac)
            throws MyException, IOException {

        validar(nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            if (!usuarioRepo.buscarPorEmail(email).getId().equals(usuario.getId())) {
                throw new MyException("EL mail ingresado ya existe en otro ususario! Ingreso otro!");
            }
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setActivo(true);

            String idImg = null;
            if (usuario.getImagen() != null) {
                idImg = usuario.getImagen().getId();
            }
            if (archivo.getBytes().length != 0) {
                Imagen imagen = imagenServ.actualizar(archivo, idImg);
                usuario.setImagen(imagen);
            }
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
    public Usuario buscarPorMail(String email){
        return usuarioRepo.buscarPorEmail(email);
    }
    // Metodo para validar los datos ingresados antes de persistirlos
    protected void validar(String nombre, String apellido, String dni, String email,
            String password, String password2, String telefono, String direccion, String fecha_nac)
            throws MyException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar su nombre");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MyException("Debe ingresar su apellido");
        }
        if (dni == null || dni.isEmpty()) {
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
        if (direccion == null || direccion.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }
        if (fecha_nac == null || fecha_nac.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }

    }

    // Metodo para buscar un usuario por su id y devolverlo
    public Usuario getOne(String id) {
        return usuarioRepo.getOne(id);
    }

    // Pasar un string a date
    protected Date pasarStringDate(String fecha) {

        String pattern = "yyyy-MM-dd"; // Formato de la cadena de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            Date date = dateFormat.parse(fecha);

            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    // Spring Security
    // para otorgar permisos de usuario
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.buscarPorEmail(email);

        if (usuario != null && usuario.getActivo().equals(Boolean.TRUE)) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            return null;
        }
    }

}
