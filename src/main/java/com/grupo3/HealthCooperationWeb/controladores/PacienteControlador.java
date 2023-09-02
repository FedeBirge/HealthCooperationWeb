package com.grupo3.HealthCooperationWeb.controladores;


import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
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
@RequestMapping("/paciente") // localhost:8080/paciente
public class PacienteControlador {

    @Autowired
    private PacienteServicio pacienteServicio;

    @GetMapping("/dashboard") // ruta para el panel administrativo
    public String panelAdministrativo(ModelMap modelo, HttpSession session) {
        Paciente logueado = (Paciente) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        modelo.addAttribute("user", logueado);

        return "perfil.html";
    }

    @GetMapping("/registrar") // *************BOTON registrarme en index(LT)*****//
    public String registrar(ModelMap modelo) {
        try {

            return "registro.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "registro.html";

        }
    }

    @GetMapping("/crear") // ************* POST del form del registro.html LT)
    public String crearPaciente(ModelMap modelo, HttpSession session) throws IOException {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("log", logueado);
        return "altaPaciente.html";

    }

    @PostMapping("/crear") // ************* POST del form del registro.html LT)
    public String crearUsuario(MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido,
            String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, String telefono, String direccion,
            String fecha_nac, @RequestParam String obrasocial,  @RequestParam String gruposanguineo,
            String especialidad, String valorConsulta, ModelMap modelo, HttpSession session) throws IOException {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            pacienteServicio.registrarPaciente(archivo, nombre, apellido, dni,
                    email, password, password2, telefono, direccion, fecha_nac, gruposanguineo, obrasocial);
            modelo.put("exito", "¡Usuario registrado con exito!");
          
            return "redirect:/login";

        } catch (MyException ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("error", ex.getMessage());
            return registrar(modelo);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_ADMINISTRADOR','ROLE_MODERADOR')")
    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {

        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", pacienteServicio.getOne(id));

            return "modificar_paciente.html";

        } catch (Exception ex) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", pacienteServicio.getOne(id));
            modelo.put("error", ex.getMessage());
            return "modificar_paciente.html";
        }

    }

    @PostMapping("/modificar/{id}") // ******ruta para modificar un usuario POST(LT)
    public String modificarUsusarios(MultipartFile archivo, @PathVariable("id") String id,
            @RequestParam String nombre, @RequestParam String apellido,
            String dni, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, String telefono, String direccion,
            String fecha_nac, String gruposanguineo,
            String idObraSocial,
            String nombreObraSocial, String emailObraSocial, String telefonoObraSocial,
            ModelMap modelo, HttpSession session) throws IOException, MyException {

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.addAttribute("user", pacienteServicio.getOne(id));
            pacienteServicio.modificarPaciente(id, archivo, nombre, apellido, dni, email, password, password2, telefono,
                    direccion, fecha_nac, gruposanguineo, idObraSocial,
                    nombreObraSocial, emailObraSocial, telefonoObraSocial);

            modelo.put("exito", "¡Paciente modificado con exito!");
            return "modificar_paciente.html";

        } catch (MyException ex) {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            modelo.put("user", pacienteServicio.getOne(id));
            modelo.addAttribute("id", pacienteServicio.getOne(id).getId());

            modelo.put("error", ex.getMessage());
            return "modificar_paciente.html";
        }

    }
    // }
    // listar todos los pacientes activos(LT) panel del administrador

    @GetMapping("/listar")
    public String listarPacientes(ModelMap modelo, HttpSession session) {
        try {
            List<Paciente> users = pacienteServicio.mostrarPacientes();
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            modelo.addAttribute("users", users);
            return "verPacientes.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);

            List<Paciente> users = pacienteServicio.mostrarPacientes();
            modelo.addAttribute("users", users);
            modelo.put("error", e.getMessage());
            return "verPacientes.html";
        }
    }

    // Listar pacientes asociados al id del profesional logueado(LT)
    @GetMapping("/listar/{id}")
    public String listarPacientes(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
           List<Paciente> users = pacienteServicio.mostrarPacientes();
//            List<Paciente> users = pacienteServicio.listarPacientesXprof(id);
            
            modelo.addAttribute("users", users);
            return "verPacientes.html";
        } catch (Exception e) {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("log", logueado);
            List<Paciente> users = pacienteServicio.mostrarPacientes();
//            List<Paciente> users = pacienteServicio.listarPacientesXprof(id);
            modelo.addAttribute("users", users);
            modelo.put("error", e.getMessage());
            return "verPacientes.html";
        }
    }

}
