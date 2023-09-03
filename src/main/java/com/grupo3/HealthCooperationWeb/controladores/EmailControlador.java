
package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.EmailServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/email")
public class EmailControlador {
     @Autowired
 private EmailServicio emailServ;

     
        @GetMapping("/contacto") // *******Boton de contactenos para la vista del form (LT)
    public String contacto(ModelMap modelo) {

        return "contacto.html";
    }

     @PostMapping("/contacto")
    public String enviarCorreo(ModelMap modelo,@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
            @RequestParam String telefono, @RequestParam String esp, @RequestParam String msj, RedirectAttributes redirectAttributes) {
        
        try{            
        emailServ.sendEmail(nombre, apellido, email, telefono, esp, "Soy Profesional: "+msj);
        redirectAttributes.addFlashAttribute("exito", "!Informacion enviada con exito!");
        return "redirect:/";// Redirecciona a la página principal después de enviar el correo

        }catch (MyException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/"; // Redirecciona a la página principal después de enviar el correo
    }
    }
     @PostMapping("/publico")
    public String enviarCorreo(ModelMap modelo,@RequestParam String nombre, @RequestParam String email,
            @RequestParam String telefono, @RequestParam String msj, RedirectAttributes redirectAttributes) {
        
        try{            
        emailServ.sendEmail(nombre, "no", email, telefono, "no","SOY usuario: "+ msj);
        redirectAttributes.addFlashAttribute("exito", "!Informacion enviada con exito!");
        return "redirect:/"; // Redirecciona a la página principal después de enviar el correo

        }catch (MyException ex){
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/"; // Redirecciona a la página principal después de enviar el correo
    }
    }
}
