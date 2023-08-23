
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
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
@RequestMapping("/paciente") // localhost:8080/paciente
public class PacienteControlador {

    @Autowired
    private PacienteServicio pacienteServicio;

    @GetMapping("/registrar") // localhost:8080/registrar//esto lo comento porque me tira error
    public String registrarPaciente(ModelMap modelo) {

        List<Paciente> pacientes = pacienteServicio.mostrarPacientes();

        modelo.addAttribute("pacientes", pacientes);

        return "paciente_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String dni, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam String fecha_nac, @RequestParam String grupoSanguineo, @RequestParam String obraSocial,
            ModelMap modelo) throws MyException, IOException {
        try {

            pacienteServicio.registrarPaciente(archivo, nombre, apellido, dni, email, password, password2, telefono,
                    direccion, fecha_nac, grupoSanguineo, obraSocial);
            modelo.put("exito", "El paciente se registro con exito");

        } catch (MyException ex) {
            List<Paciente> pacientes = pacienteServicio.mostrarPacientes();

            modelo.addAttribute("pacientes", pacientes);

            modelo.put("error", ex.getMessage());
            return "paciente_form.html";// volvemos a cargar el formulario
        }
        return "index.html";
    }
      // listar todos los pacientes activos(LT) panel del administrador
    @GetMapping("/listar")
    public String listarProfesionales(ModelMap modelo) {
        try {
            List<Paciente> users = pacienteServicio.mostrarPacientes();
            System.out.println(users);
            modelo.addAttribute("users",users);
            return "verProfesionales.html";
        } catch (Exception e) {

            List<Paciente> users = pacienteServicio.mostrarPacientes();
            modelo.addAttribute("users", users);
            modelo.put("error", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }
    //Listar pacientes asociados al id del profesional logueado(LT)
     @GetMapping("/listar/{id}")
    public String listarProfesionales(@PathVariable("id") String id, ModelMap modelo) {
        try {
            List<Paciente> users = pacienteServicio.mostrarPacientes();
            System.out.println(users);
            modelo.addAttribute("users",users);
            return "verProfesionales.html";
        } catch (Exception e) {

            List<Paciente> users = pacienteServicio.mostrarPacientes();
            modelo.addAttribute("users", users);
            modelo.put("error", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

}
