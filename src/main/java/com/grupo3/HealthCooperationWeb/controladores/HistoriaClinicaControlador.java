
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.HistoriaClinicaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/paciente/historia_clinica")
public class HistoriaClinicaControlador {
    
    @Autowired
    private HistoriaClinicaServicio historiaClinicaServicio;
    
    @GetMapping("/registrar")
    public String registrarHistoria(ModelMap modelo){
        
       List<HistoriaClinica> historia = historiaClinicaServicio.mostrarHistoria();
       
       modelo.addAttribute("historia", historia );
       
       return "historia_form.html";
        
        
    }
    
    @PostMapping("/historia/registro")
    public String registro(@RequestParam HistoriaClinica paciente, ModelMap modelo) throws MyException{
        
        try{
            
        
        historiaClinicaServicio.crearHistoriaClinica(paciente);
        
        modelo.put("exito", "La historia clinica se registro correctamente");
    
        }catch(MyException ex){
            
            List<HistoriaClinica> historia = historiaClinicaServicio.mostrarHistoria();
            
            modelo.addAttribute("historia", historia);
            
            modelo.put("error", ex.getMessage());
            
            return "historia_form.html";//vuelve a cargar el formulario
        
        }
        
        return "index.html";
        
        }
    
}
