package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.ObraSocialServicio;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import java.io.IOException;
import java.text.ParseException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

@RequestMapping("/user")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio userServ; // inyectamos el servicio de usuario
    @Autowired
    private ProfesionalServicio profServ;

    @Autowired
    private PacienteServicio pacienteServ;
    @Autowired
    private ObraSocialServicio obraServ;

    @GetMapping("/dashboard") // ruta para el panel administrativo
    public String panelAdministrativo(ModelMap modelo) {
        List<Usuario> usuarios = userServ.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "panelAdmin.html";
    }

    @GetMapping("/verUsuario/{id}") // ruta para ver el perfil de un usuario
    public String verPerfilUsusario(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);

        if (userServ.getOne(id).getRol().toString().equals("MODERADOR")) {
            System.out.println("veo admin");

            modelo.addAttribute("user", profServ.getOne(id));
        }
        if (userServ.getOne(id).getRol().toString().equals("USUARIO")) {
            System.out.println("veo usuario");

            modelo.addAttribute("user", pacienteServ.getOne(id));

        }
        if (userServ.getOne(id).getRol().toString().equals("ADMINISTRADOR")) {
            System.out.println("veo prof");
            modelo.addAttribute("user", pacienteServ.getOne(id));

        }
     System.out.println("veo ver usuaraio");

        return "perfil.html";

    }

    @PostMapping("/crearUsuario") // ruta para crear un usuario POST
    public String crearUsuario(HttpSession session, MultipartFile archivo, @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String dni,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2,
            @RequestParam String telefono, @RequestParam String direccion, @RequestParam String fecha_nac,
            ModelMap modelo) throws IOException, ParseException {
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
            modelo.addAttribute("log", logueado);
            List<Usuario> users = userServ.listarUsuarios();
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.addAttribute("users", users);

            return "verUsuarios.html";
        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            List<Usuario> users = userServ.listarUsuarios();
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.addAttribute("users", users);
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/dashboard";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMINISTRADOR','ROLE_MODERADOR')")
    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable("id") String id, ModelMap modelo, HttpSession session) throws MyException {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);

            if (userServ.getOne(id).getRol().toString().equals("ADMINISTRADOR")) {
                modelo.addAttribute("user", userServ.getOne(id));

                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                return "modificar_user.html";
            }
            if (userServ.getOne(id).getRol().toString().equals("MODERADOR")) {
                modelo.addAttribute("user", profServ.getOne(id));

                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("especialidades", especialidades);
                return "modificar_user.html";
            }
            if (userServ.getOne(id).getRol().toString().equals("USUARIO")) {
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("user", pacienteServ.getOne(id));
                List<ObraSocial> obras = obraServ.listarObrasSociales();
                modelo.addAttribute("obras", obras);
                modelo.addAttribute("log", logueado);

                return "modificar_user.html";
            }

        } catch (Exception ex) {
            Rol[] roles = Rol.values();
            List<ObraSocial> obras = obraServ.listarObrasSociales();
            modelo.addAttribute("obras", obras);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", pacienteServ.getOne(id));
            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }
        return "modificar_user.html";

    }

    @PostMapping("/modificarUsuario/{id}") // ******ruta para modificar un usuario POST(LT)
    public String modificarUsusarios(MultipartFile archivo, @PathVariable("id") String id,
            @RequestParam String nombre, @RequestParam String apellido,
            String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, String telefono, String direccion,
            String fecha_nac, String obraSocial, String gruposanguineo, String descripcion,
            String especialidad, String valorConsulta, ModelMap modelo, HttpSession session,
            RedirectAttributes redirectAttributes)
            throws IOException, MyException, ParseException {

        try {

            Rol[] roles = Rol.values();

            if (userServ.getOne(id).getRol().toString().equals("ADMINISTRADOR")) {
                userServ.modificarUsuario(archivo, id, nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac);
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("user", userServ.getOne(id));
                redirectAttributes.addFlashAttribute("exito", "¡Admin modificado con exito!");
                return "redirect:/admin/dashboard";
            }
            if (userServ.getOne(id).getRol().toString().equals("MODERADOR")) {

                profServ.modificarProfesional(id, archivo, nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac, especialidad, valorConsulta,descripcion);
                Especialidad[] especialidades = Especialidad.values();
                modelo.addAttribute("especialidades", especialidades);
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("user", profServ.getOne(id));
                redirectAttributes.addFlashAttribute("exito", "¡Profesional modificado con exito!");
                 return "modificar_user.html";
            }
            if (userServ.getOne(id).getRol().toString().equals("USUARIO")) {
                System.out.println("obra Cont " + obraSocial);
                pacienteServ.modificarPaciente(id, archivo, nombre, apellido, dni, email, password, password2, telefono,
                        direccion, fecha_nac, gruposanguineo, obraSocial);
             
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("user", pacienteServ.getOne(id));
                redirectAttributes.addFlashAttribute("exito", "¡Usuario modificado con exito!");
                List<ObraSocial> obras = obraServ.listarObrasSociales();
                modelo.addAttribute("obras", obras);

               return "modificar_user.html";
            }

        } catch (MyException ex) {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            Especialidad[] especialidades = Especialidad.values();
            modelo.addAttribute("especialidades", especialidades);
            modelo.put("error", ex.getMessage());
            return perfil(id, modelo, session);
        }
        return "redirect:/";

    }

    @GetMapping("/eliminar/{id}") // ********** ruta para eliminar un usuario
    // (no tiene una vista, es para un boton de la vista listar_usuarios)
    public String eliminarU(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", userServ.getOne(id));

            return "redirect:/admin/dashboard";
        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", userServ.getOne(id));
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/dashboard";
        }

    }

    @PostMapping("/eliminar/{id}") // ********************** ruta para eliminar un usuario
    // (no tiene una vista, es para un boton de la //
    // vista listar_usuarios)
    public String eliminarUser(@PathVariable("id") String id, ModelMap modelo,
            HttpSession session,RedirectAttributes redirectAttributes) {

        try {
            userServ.eliminarUsuario(id);
            modelo.put("exito", "Usuario eliminado con exito!");
            System.out.println("post eliminar");
            if (userServ.getOne(id).getRol().toString().equals("MODERADOR")) {
              
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("user", profServ.getOne(id));
                redirectAttributes.addFlashAttribute("exito", "¡Profesional eliminado!");
            

            }
            if (userServ.getOne(id).getRol().toString().equals("USUARIO")) {
              
                Usuario logueado = (Usuario) session.getAttribute("usuariosession");
                modelo.addAttribute("log", logueado);
                modelo.addAttribute("user", pacienteServ.getOne(id));
                redirectAttributes.addFlashAttribute("exito", "¡Usuario eliminado!");
         
            }
        } catch (Exception ex) {
            modelo.addAttribute("user", userServ.getOne(id));
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
          
        }
        return "redirect:/user/listar";
    }

}
