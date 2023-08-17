package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
// @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
@RequestMapping("/user")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio userServ; // inyectamos el servicio de usuario

    @GetMapping("/dashboard") // ruta para el panel administrativo
    public String panelAdministrativo(ModelMap modelo) {
        List<Usuario> usuarios = userServ.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "panelAdmin.html";
    }

    @GetMapping("/verUsuario/{id}") // ruta para ver el perfil de un usuario
    public String verPerfilUsusario(@PathVariable("id") String id, ModelMap modelo) {

        modelo.put("usuario", userServ.getOne(id));
        return "perfil.html";

    }

    @GetMapping("/crearUsuario") // ruta para crear un usuario GET
    public String crearUsusario(ModelMap modelo) {
        Rol[] roles = Rol.values();
        modelo.addAttribute("roles", roles);
        return "registro.html";

    }

    @PostMapping("/crearUsuario") // ruta para crear un usuario POST
    public String crearUsuario(MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String dni,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2,
            @RequestParam String telefono, @RequestParam String direccion, @RequestParam String fecha_nac,
             ModelMap modelo) {
        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            userServ.crearUsuario(archivo, nombre, apellido, dni, email, password, password2, telefono, direccion,
                    fecha_nac);
            modelo.put("exito", "!Usuario registrado con exito!");
            return "registro.html";

        } catch (MyException ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            return "registro.html";
        }

    }

    @GetMapping("/listarUsuarios") // ruta para listar los usuarios
    public String listarUsusario(ModelMap modelo) {
        try {
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            return "listar_usuarios.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/dashboard";
        }

    }

    @GetMapping("/modificarUsuario/{id}") // ruta para modificar un usuario GET
    public String modificarUsusario(@PathVariable("id") String id, ModelMap modelo) {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", userServ.getOne(id).getId());

            return "modificar_user.html";
        } catch (Exception ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }

    }

    @PostMapping("/modificarUsuario/{id}") // ruta para modificar un usuario POST
    public String modificarUsusarios(MultipartFile archivo,@PathVariable("id") String id, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String dni,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2,
            @RequestParam String telefono, @RequestParam String direccion, @RequestParam String fecha_nac,
            ModelMap modelo) throws IOException {

        try {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", userServ.getOne(id).getId());
//
            userServ.modificarUsuario(archivo, id, nombre,apellido, dni, email, 
                    password, password2,telefono, direccion, fecha_nac );

            modelo.put("exito", "!Usuario modificado con exito!");
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.put("usuarios", usuarios);
            return "listar_usuarios.html";
        } catch (MyException ex) {
            Rol[] roles = Rol.values();
            modelo.addAttribute("roles", roles);
            modelo.put("usuario", userServ.getOne(id));
            modelo.addAttribute("id", userServ.getOne(id).getId());

            modelo.put("error", ex.getMessage());
            return "modificar_user.html";
        }

    }

    @GetMapping("/eliminar/{id}") // ruta para eliminar un usuario(no tiene una vista, es para un boton de la
                                  // vista listar_usuarios)
    public String eliminarU(@PathVariable("id") String id, ModelMap modelo) {

        try {
            modelo.put("exito", "Usuario eliminado con exito!");
            return "redirect:/admin/listarUsuarios";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/listarUsusarios";
        }

    }

    @PostMapping("/eliminar/{id}") // ruta para eliminar un usuario(no tiene una vista, es para un boton de la //
                                   // vista listar_usuarios)
    public String eliminarUser(@PathVariable("id") String id, ModelMap modelo) {

        try {
            userServ.eliminarUsuario(id);
            List<Usuario> usuarios = userServ.listarUsuarios();
            modelo.addAttribute("usuarios", usuarios);
            modelo.put("exito", "Usuario eliminado con exito!");
            return "listar_usuarios.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/admin/listarUsusarios";
        }

    }

}
