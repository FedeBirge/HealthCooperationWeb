
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.FichaServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/paciente/historia_clilnica/ficha")
public class Fichacontrolador {
    
    @Autowired 
    private FichaServicio fichaServicio;
    
    
    public String registrarFicha(ModelMap modelo){
        
        List<Ficha> fichas = fichaServicio.mostrarFichas();
        
        modelo.addAttribute("fichas", fichas);
        
        return "ficha_form.html";
        
    }
    
    @PostMapping("/paciente/historia_clinica/ficha")
    public String registro(String id, Date fecha_consulta, String nota, ModelMap modelo) throws MyException{
        
        try{
            fichaServicio.crearFicha(nota, fecha_consulta, nota);
            
            modelo.put("exito", "La ficha ha sido registrada exitosamente");
            
            
        }catch(MyException ex){
            
            List<Ficha> fichas = fichaServicio.mostrarFichas();
            
            modelo.addAttribute("fichas", fichas);
            
            modelo.put("error", ex.getMessage());
            return "ficha_form.html";//volvemos a cargar el formulario
        }
        
        return "index.html";
    }
    
}