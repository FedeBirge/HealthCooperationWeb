
package com.grupo3.HealthCooperationWeb.servicios;


import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteServicio extends UsuarioServicio{
    
    @Autowired
    private PacienteRepositorio pacienteRepositorio;
    
    @Transactional
    public void crearPaciente(Paciente paciente ){
       
        pacienteRepositorio.save(paciente);
        
    }
    
    @Transactional
    public void guardarPaciente(Paciente paciente){
        
        pacienteRepositorio.save(paciente);
    }
    
    public List<Paciente> mostrarPacientes(){
        
        List<Paciente> pacientes = new ArrayList();
        
        pacientes = pacienteRepositorio.findAll();
        
        return pacientes;
    }
}
