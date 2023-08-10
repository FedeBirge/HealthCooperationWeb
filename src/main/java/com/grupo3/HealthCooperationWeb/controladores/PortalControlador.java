
package com.grupo3.HealthCooperationWeb.controladores;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author feder
 */

@Controller
@RequestMapping("/")
public class PortalControlador {
     @GetMapping("/")
    public String index(ModelMap modelo) {
     
        return "index.html";
    }
    
    
}
