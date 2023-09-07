
package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.HistoriaClinicaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoriaClinicaServicio {

    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;
    @Autowired
    PacienteRepositorio pacienteRepositorio;
    @Autowired
    PacienteServicio pacienteServicio;

   @Transactional
      public HistoriaClinica crearHistoriaClinica(String id) throws MyException {
    
      
      HistoriaClinica historiaClinica = new HistoriaClinica();
      Optional<Paciente> respuesta = pacienteRepositorio.findById(id);
                if (respuesta.isPresent()) {
                    Paciente pac= respuesta.get();
                    
                    historiaClinica.setPaciente(pac);
                    pac.setHistoria(historiaClinica);
                     historiaClinicaRepositorio.save(historiaClinica);
                return historiaClinica;
      
      
      }
        return null;
      }

    @Transactional
    // agrego parámetro idPaciente y completo lógica
    // para que se vea la historia clinica de UN paciente (bren)
    public HistoriaClinica mostrarHistoria(String idPaciente) throws MyException {

        Paciente paciente = (Paciente) pacienteServicio.getOne(idPaciente);

        if (paciente == null) {
            throw new MyException("No existe un paciente con ese ID");
        } else if (paciente.getHistoria() == null) {
            // si no tiene Historia Clínica, nos aseguramos de que se genere el espacio para
            // crear una, nos retornará su historia vacía:
          
         
            return paciente.getHistoria();
        } else {
            // agregué este método findByHistoriaClinica al repo de hisotriaClinica (bren)
            return historiaClinicaRepositorio.findByPaciente_Id(idPaciente);
        }
    }

    // por el momento no usé este método, sino el getOne de Paciente (bren)
    public HistoriaClinica getOne(String id) {
        return historiaClinicaRepositorio.getOne(id);
    }

}
