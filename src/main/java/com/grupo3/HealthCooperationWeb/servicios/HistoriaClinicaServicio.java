
package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.HistoriaClinicaRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoriaClinicaServicio{
    
    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;
    
    @Transactional
    public void crearHistoriaClinica(HistoriaClinica historiaClinica) throws MyException{
        
        validar(historiaClinica);
        
        historiaClinicaRepositorio.save(historiaClinica);
        
        
        
    }
    
    @Transactional
    public void guardarHistoria(HistoriaClinica historiaClinica){
        
        historiaClinicaRepositorio.save(historiaClinica);
    }
    
    public List<HistoriaClinica> mostrarHistoria(){
        
      List<HistoriaClinica> historia = new ArrayList();
        
        historia = (List<HistoriaClinica>) historiaClinicaRepositorio.findAll();
        
        return historia;
    }
    
    public HistoriaClinica getOne(String id){
        return historiaClinicaRepositorio.getOne(id);
    }
    
    
    private void validar(HistoriaClinica paciente) throws MyException{
        
        if(paciente == null){
            
             throw  new MyException("Debe existir un paciente");
        }
        
    }
    
}
