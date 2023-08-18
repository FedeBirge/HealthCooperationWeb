package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Controlador para levantar pagina de inicio
    @GetMapping("/") // Vista principal (LT)
    public String index(ModelMap modelo) {
        Especialidad[] especialidades = Especialidad.values();
        modelo.addAttribute("especialidades", especialidades);
        return "index.html";
    }

    // Spring Security
    @GetMapping("/registrar") // BOTON registrarme ne index(LT3)
    public String registrar(ModelMap modelo) {

        return "registro.html";
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
            if (logueado.getRol().toString().equals("ADMINISTRADOR")) {
                return "redirect:/admin/dashboard";
            } else {
                if (logueado.getRol().toString().equals("MODERADOR")) {
                    return "redirect:/profesional/dashboard";
                } else {
                    return "redirect:/paciente/perfil";
                }
            }

        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "login.html";
        }
    }
      @GetMapping("/contacto") // Boton de contactenos para la vista del form (LT)
    public String contacto(ModelMap modelo) {

        return "contacto.html";
    }

//    @PostMapping("/registro") // se usa en AdminCOntrolador
//    public String registro(MultipartFile archivo, @RequestParam String nombre,
//            @RequestParam String apellido, @RequestParam String dni, @RequestParam String email,
//            @RequestParam String password, String password2, @RequestParam String telefono,
//            @RequestParam String direccion, @RequestParam String fecha_nac, ModelMap modelo) throws MyException {
//        
//        try {
//           
//            
//            usuarioServicio.crearUsuario(archivo, nombre, apellido, dni, email, password, password2, telefono,
//                    direccion, fecha_nac);
//            modelo.put("exito", "Usuario registrado correctamente");
//            return "index.html";
//
//        } catch (Exception e) {
//            modelo.put("error", e.getMessage());
//          
//            return "registro.html";
//        }
//
//    }
  

  

}
