package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Controlador para levantar pagina de inicio
    @GetMapping("/")
    public String index(ModelMap modelo) {

        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);

        return "index.html";
    }

    // Spring Security

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String dni, @RequestParam String email,
            @RequestParam String password, String password2, @RequestParam String telefono,
            @RequestParam String direccion, @RequestParam String fecha_nac, ModelMap modelo) throws MyException {
        String rol = "USUARIO";
        try {
            usuarioServicio.crearUsuario(archivo, nombre, apellido, dni, email, password, password2, telefono,
                    direccion,
                    fecha_nac, rol);
            modelo.put("exito", "Usuario registrado correctamente");
            return "index.html";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("email", email);
            modelo.put("telefono", telefono);
            modelo.put("direccion", direccion);
            modelo.put("direccion", direccion);
            modelo.put("fecha_nac", fecha_nac);

            return "registro.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseñas invalidos");
        }
        return "login.html";
    }

    @GetMapping("inicio")
    public String inicio() {
        return "inicio.html";
    }

}
