package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Imagen;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.ImagenRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import org.springframework.util.StreamUtils;

@Primary
@Service
// Srping Security: para autenticar extiendo de UserDetailsService
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepo; // Repositorio de usuarios

    @Autowired
    private ImagenServicio imagenServ; //
    @Autowired
    ImagenRepositorio imagenRepo;

    public static boolean validarFecha(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);

        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH) + 1;
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        if (año >= 1900 && año <= 2099 && mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    // Metodo para crear un usuario
    public Usuario crearUsuario(MultipartFile archivo, String nombre, String apellido, String dni, String email,
            String password, String password2, String telefono, String direccion, String fecha_nac) throws MyException, IOException, ParseException {
        // Se validan los datos ingresados
        validar("1", nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        if (usuarioRepo.buscarPorEmail(email) != null) {
            throw new MyException("EL mail ingresado ya existe! Ingreso otro!");
        }
        if (usuarioRepo.buscarPorDni(dni) != null) {
            throw new MyException("Existe un usuario con el N° de docuemnto!");
        }

        Date fecha = pasarStringDate(fecha_nac);
        if (!validarFecha(fecha)) {
            throw new MyException("la fecha no es valida");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setFecha_nac(fecha);
        usuario.setActivo(true);

        usuario.setRol(Rol.USUARIO);
        if (archivo.isEmpty()) {
            // Si el archivo está vacío, crea el paciente con una imagen predeterminada
            Imagen imagenPredeterminada = obtenerImagenPredeterminada(); // Implementa esta función para obtener la
            // imagen predeterminada
            usuario.setImagen(imagenPredeterminada);
        } else {
            // Si el archivo no está vacío, crea el paciente con la imagen proporcionada
            Imagen imagen = imagenServ.guardar(archivo);
            usuario.setImagen(imagen);
        }
        return usuarioRepo.save(usuario);

    }

    @Transactional
    // Metodo para modificar un usuario
    public void modificarUsuario(MultipartFile archivo, String id, String nombre, String apellido, String dni,
            String email, String password, String password2, String telefono, String direccion, String fecha_nac)
            throws MyException, IOException, ParseException {

        validar(id, nombre, apellido, dni, email, password, password2, telefono, direccion, fecha_nac);
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        Date fecha = pasarStringDate(fecha_nac);
        if (!validarFecha(fecha)) {
            throw new MyException("la fecha no es válida");
        }
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);

            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setFecha_nac(fecha);
            usuario.setActivo(true);

            String idImg = null;
            if (usuario.getImagen() != null) {
                idImg = usuario.getImagen().getId();
            }
            if (archivo != null && archivo.getBytes().length != 0) {
                Imagen imagen = imagenServ.actualizar(archivo, id);
                usuario.setImagen(imagen);
            } else {
                // No se proporcionó un archivo nuevo, no se actualiza la imagen del usuario
            }

            usuarioRepo.save(usuario);

        }

    }

    public Imagen obtenerImagenPredeterminada() {
        try {
            // Lee la imagen predeterminada desde recursos estáticos
            Resource resource = new ClassPathResource("/static/img/user_logo.png"); // Cambia la ruta según la ubicación de tu imagen
            byte[] contenidoImagen = StreamUtils.copyToByteArray(resource.getInputStream());

            // Crea una instancia de Imagen y establece sus propiedades
            Imagen imagenPredeterminada = new Imagen();
            imagenPredeterminada.setMime("image/png"); // Cambia el tipo MIME según tu imagen
            imagenPredeterminada.setNombre("user_logo.png"); // Cambia el nombre según tu imagen
            imagenPredeterminada.setContenido(contenidoImagen);
            imagenRepo.save(imagenPredeterminada);
            return imagenPredeterminada;
        } catch (IOException e) {
            // Maneja cualquier error de lectura de archivo aquí
            e.printStackTrace();
            return null; // O devuelve una imagen predeterminada alternativa o lanza una excepción
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
  @Transactional
    // Metodo para eliminar un usuario, se cambia el estado a inactivo
    public void altaUsuario(String id) {
        try {
            Optional<Usuario> resp = usuarioRepo.findById(id);
            if (resp.isPresent()) {
                Usuario user = (Usuario) (resp.get());
                user.setActivo(Boolean.TRUE);
                usuarioRepo.save(user);
            }
        } catch (Exception e) {
            System.out.println("No es posible dar de alta el ususario");
        }
    }
    private Rol pasarStringRol(String rol) throws MyException{
        if(!rol.isEmpty()){
        switch (rol) {
                case "USUARIO":
                   return Rol.USUARIO;
                   
                case "ADMINISTRADOR":
                    return Rol.ADMINISTRADOR;
                    
                case "MODERADOR":
                    return Rol.MODERADOR;
                   
               
                default:
                    throw new MyException("Rol no valido: " + rol);
            }
        }
        return null;
    
    
        
    }
    @Transactional
    // Metodo para eliminar un usuario, se cambia el estado a inactivo
    public void cambioRol(String id,String rol) {
        try {
            Optional<Usuario> resp = usuarioRepo.findById(id);
            if (resp.isPresent()) {
                Usuario user = (Usuario) (resp.get());
                user.setRol(pasarStringRol(rol));
                usuarioRepo.save(user);
            }
        } catch (Exception e) {
            System.out.println("No es posible cambiar de rol");
        }
    }
    // Metodo para listar todos los usuarios, sin tener en cuenta si estan dados de baja
    public List<Usuario> listarTodosUsuarios() {

        try {

            return usuarioRepo.findAll();

        } catch (Exception e) {
            System.out.println("No pudieron ser listados los usuarios");
            return null;
        }

    }

    // Metodo para listar todos los usuarios
    public List<Usuario> listarUsuarios() {

        try {
            List<Usuario> usuarios = usuarioRepo.findAll();

            return usuarios;

        } catch (Exception e) {
            System.out.println("No pudieron ser listados los usuarios");
            return null;
        }

    }
      public Usuario buscarPorDni(String dni) {
        return usuarioRepo.buscarPorEmail(dni);
    }
    public Usuario buscarPorMail(String email) {
        return usuarioRepo.buscarPorEmail(email);
    }

    // Metodo para validar los datos ingresados antes de persistirlos
    protected void validar(String id, String nombre, String apellido, String dni, String email,
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
            if (!usuarioRepo.buscarPorEmail(email).getId().equals(id)) {
                throw new MyException("Debe ingresar otro email. El ingresdo ya existe!");
            }
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
            throw new MyException("Debe ingresar un telefono");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new MyException("Debe ingresar una direccion");
        }
        if (fecha_nac == null || fecha_nac.isEmpty()) {
            throw new MyException("Debe ingresar una fecha");
        }

    }

    // Metodo para buscar un usuario por su id y devolverlo
    public Usuario getOne(String id) {
        return usuarioRepo.getOne(id);
    }

    // Pasar un string a date
    protected Date pasarStringDate(String fecha) throws MyException, ParseException {

        String pattern = "yyyy-MM-dd"; // Formato de la cadena de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            Date date = dateFormat.parse(fecha);

            return date;
        } catch (ParseException e) {
            throw new MyException("La fecha ingresada no es válida");
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
