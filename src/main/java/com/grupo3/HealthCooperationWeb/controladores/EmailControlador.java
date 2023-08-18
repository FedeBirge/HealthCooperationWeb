
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.EmailServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/email")
public class EmailControlador {
     @Autowired
 private EmailServicio emailServ;
//     Nombre
//Apellido
//Dirección de correo electrónico
//Número de teléfono
//Especialidad
//Texto grande para detalles adicionales
     @PostMapping("/contacto")
    public String enviarCorreo(ModelMap modelo,@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
            @RequestParam String telefono, @RequestParam String esp, @RequestParam String msj) {
        
        try{            
        emailServ.sendEmail(nombre, apellido, email, telefono, esp, msj);
        //modelo.put("exito", "!Informacion enviada con exito!");
        return "contacto.html"; // Redirecciona a la página principal después de enviar el correo

        }catch (MyException ex){
           // modelo.put("error", ex.getMessage());
        return "contacto.html"; // Redirecciona a la página principal después de enviar el correo
    }
    }
}
