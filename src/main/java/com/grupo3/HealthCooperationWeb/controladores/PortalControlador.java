package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import com.grupo3.HealthCooperationWeb.servicios.ProfesionalServicio;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private ProfesionalServicio profesionalServicio;

    @Autowired
    private PacienteServicio pacienteServ;

    @GetMapping("/") // ************ Vista principal (LT)***************///
    public String index(ModelMap modelo) {
        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);
        return "index.html";
    }

    @GetMapping("/login") // Boton para logearme en el index(LT)
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o contraseñas invalidos");
        }
        return "login.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMINISTRADOR','ROLE_MODERADOR')")
    @GetMapping("/inicio") // PASO UNO, la pirmer interaccion despues del login segun ROL
    public String inicio(ModelMap modelo, HttpSession session) {

        // para que según los roles se dirija a las vistas correspondientes
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", logueado);
            if (logueado.getRol().toString().equals("ADMINISTRADOR")) {
                return "redirect:/admin/dashboard";
            } else {
                if (logueado.getRol().toString().equals("MODERADOR")) {
                    return "redirect:/profesionales/dashboard";
                } else {
                    return "redirect:/paciente/dashboard";
                }
            }

        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", logueado);
            modelo.put("error", ex.getMessage());
            return "login.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR','ROLE_MODERADOR')")
    @PostMapping("/crear") // ************* POST del form del registro.html LT)
    public String crearUsuario(MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido,
            String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, String telefono, String direccion,
            String fecha_nac, String obrasocial, String gruposanguineo,
            String especialidad, String valorConsulta, ModelMap modelo, HttpSession session)
            throws IOException, ParseException {

        try {
            pacienteServ.registrarPaciente(archivo, nombre, apellido, dni,
                    email, password, password2, telefono, direccion,
                    fecha_nac, gruposanguineo, obrasocial);
            modelo.put("exito", "¡Usuario registrado con exito!");
            return "redirect:/";

        } catch (MyException ex) {

            return "altaProfesional.html";
        }

    }

}
