package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.PacienteServicio;
import com.grupo3.HealthCooperationWeb.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/paciente")//localhost:8080/paciente
public class PacienteControlador {

    @Autowired
    private PacienteServicio pacienteServicio;

    @GetMapping("/registrar")//localhost:8080/registrar//esto lo comento porque me tira error
    public String registrarPaciente(ModelMap modelo) {

        List<Paciente> pacientes = pacienteServicio.mostrarPacientes();

        modelo.addAttribute("pacientes", pacientes);

        return "paciente_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam Paciente usuario, ModelMap modelo) throws MyException {
        try {
            pacienteServicio.crearPaciente(usuario);//si todo sale bien retornamos al index

            modelo.put("exito", "El paciente se registro con exito");
        } catch (MyException ex) {
            List<Paciente> pacientes = pacienteServicio.mostrarPacientes();

            modelo.addAttribute("pacientes", pacientes);

            modelo.put("error", ex.getMessage());
            return "paciente_form.html";//volvemos a cargar el formulario
        }
        return "index.html";
    }

}
