package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller

@RequestMapping("/user")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio userServ; // inyectamos el servicio de usuario
    @Autowired
    private ProfesionalServicio profServ;

    @Autowired
    private PacienteServicio pacienteServ;

    @GetMapping("/dashboard") // ruta para el panel administrativo
    public String panelAdministrativo(ModelMap modelo) {
        List<Usuario> usuarios = userServ.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "panelAdmin.html";
    }

    @GetMapping("/verUsuario/{id}") // ruta para ver el perfil de un usuario
    public String verPerfilUsusario(@PathVariable("id") String id, ModelMap modelo) {

        modelo.put("user", userServ.getOne(id));
        return "perfil.html";

    }

    @PostMapping("/crearUsuario") // ruta para crear un usuario POST
    public String crearUsuario(HttpSession session, MultipartFile archivo, @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String dni,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2,
            @RequestParam String telefono, @RequestParam String direccion, @RequestParam String fecha_nac,
            ModelMap modelo) throws IOException {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            userServ.crearUsuario(archivo, nombre, apellido, dni, email, password, password2, telefono, direccion,
                    fecha_nac);
            modelo.put("exito", "!Usuario registrado con exito!");
            return "altaUsuario.html";

        } catch (MyException ex) {
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            modelo.put("error", ex.getMessage());
            return "altaUsuario.html";
        }

    }

    @GetMapping("/listar") // *********ruta para listar los usuarios(LT)
    // en panel del administrador
    public String listarUsusario(ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            List<Usuario> users = userServ.listarUsuarios();
            modelo.addAttribute("users", users);
            return "verUsuarios.html";
        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            List<Usuario> users = userServ.listarUsuarios();
            modelo.addAttribute("users", users);
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/dashboard";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMINISTRADOR','ROLE_MODERADOR')")
    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            if (userServ.getOne(id).getRol().toString().equals("ADMINISTRADOR")) {
                modelo.addAttribute("user", userServ.getOne(id));
                modelo.addAttribute("id", userServ.getOne(id).getId());

                return "modificar_user.html";
            }
            if (userServ.getOne(id).getRol().toString().equals("MODERADOR")) {
                modelo.addAttribute("user", profServ.getOne(id));
                modelo.addAttribute("id", profServ.getOne(id).getId());

                modelo.addAttribute("especialidades", especialidades);
                return "modificar_user.html";
            }
            if (userServ.getOne(id).getRol().toString().equals("USUARIO")) {
                modelo.addAttribute("user", pacienteServ.getOne(id));
                modelo.addAttribute("id", pacienteServ.getOne(id).getId());
                return "modificar_user.html";
            }

        } catch (Exception ex) {
            Rol[] roles = Rol.values();
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }
        return "modificar_user.html";

    }

    // @GetMapping("/modificarUsuario/{id}") // ruta para modificar un usuario GET
    // public String modificarUsusario(@PathVariable("id") String id, ModelMap
    // modelo) {
    //
    // try {
    // Rol[] roles = Rol.values();
    // modelo.addAttribute("roles", roles);
    // modelo.put("usuario", userServ.getOne(id));
    // modelo.addAttribute("id", userServ.getOne(id).getId());
    //
    // return "modificar_user.html";
    // } catch (Exception ex) {
    // Rol[] roles = Rol.values();
    // modelo.addAttribute("roles", roles);
    // modelo.put("error", ex.getMessage());
    // return "modificar_user.html";
    // }
    //
    // }
    @PostMapping("/modificarUsuario/{id}") // ******ruta para modificar un usuario POST(LT)
    public String modificarUsusarios(MultipartFile archivo, @PathVariable("id") String id,
            @RequestParam String nombre, @RequestParam String apellido,
            String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, String telefono, String direccion,
            String fecha_nac, String idObraSocial,
            String nombreObraSocial, String emailObraSocial, String telefonoObraSocial, String gruposanguineo,
            String especialidad, String valorConsulta, ModelMap modelo, HttpSession session)
            throws IOException, MyException {

        try {
            System.out.println(userServ.getOne(id).getFecha_nac());
            Rol[] roles = Rol.values();
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            if (userServ.getOne(id).getRol().toString().equals("ADMINISTRADOR")) {
                userServ.modificarUsuario(archivo, id, nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac);
                modelo.put("exito", "¡Admin modificadodo con exito!");
                return "modificar_user.html";
            }
            if (userServ.getOne(id).getRol().toString().equals("MODERADOR")) {
                System.out.println(profServ.getOne(id).getEspecialidad());
                profServ.modificarProfesional(id, archivo, nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac, especialidad, valorConsulta);
                modelo.put("exito", "¡Profesional modificado con exito!");
                return "redirect:/admin/dashboard";
            }
            if (userServ.getOne(id).getRol().toString().equals("USUARIO")) {
                pacienteServ.modificarPaciente(id, archivo, nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac, gruposanguineo, idObraSocial, nombreObraSocial, emailObraSocial,
                        telefonoObraSocial);
                modelo.put("exito", "¡Usuario modificado con exito!");
                return "modificar_user.html";
            }

        } catch (MyException ex) {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("user", userServ.getOne(id));
            modelo.addAttribute("id", userServ.getOne(id).getId());
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);

            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }
        return "redirect:/";

    }

    @GetMapping("/eliminar/{id}") // ********** ruta para eliminar un usuario
    // (no tiene una vista, es para un boton de la vista listar_usuarios)
    public String eliminarU(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            modelo.put("exito", "Usuario eliminado con exito!");
            return "panelAdmin.html";
        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            modelo.put("error", ex.getMessage());
            return "panelAdmin.html";
        }

    }

    @PostMapping("/eliminar/{id}") // ********************** ruta para eliminar un usuario
    // (no tiene una vista, es para un boton de la //
    // vista listar_usuarios)
    public String eliminarUser(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            userServ.eliminarUsuario(id);

            modelo.put("exito", "Usuario eliminado con exito!");
            return "redirect:/user/listar";
        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("error", ex.getMessage());
            return "panelAdmin.html";
        }
    }

}
