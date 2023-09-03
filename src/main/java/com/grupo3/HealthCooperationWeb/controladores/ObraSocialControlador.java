package com.grupo3.HealthCooperationWeb.controladores;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.servicios.ObraSocialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/obra")
public class ObraSocialControlador {

    @Autowired
    private ObraSocialServicio obraServ;

    @GetMapping("/listarObras") // ruta para listar las obras sociales
    public String listarObras(ModelMap modelo) {
        try {
            List<ObraSocial> obras = obraServ.listarObrasSociales();
            modelo.addAttribute("obras", obras);
            return "lista_obras.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:";
        }

    }

    @GetMapping("/verObraSocial/{id}") // ruta para ver el perfil de un usuario
    public String verObraSocial(@PathVariable("id") String id, ModelMap modelo) throws MyException {
        try {
            modelo.put("obra", obraServ.getObraSocialById(id));
            return "obra.html";
        } catch (MyException ex) {
            System.out.println("");
            return "redirect:";
        }
    }

    @GetMapping("/crearObra") // ruta para crear una obra social GET
    public String crearObraSocial(ModelMap modelo) {

        return "registrar_obra.html";

    }

    @PostMapping("/crearObra") // ruta para crear una obra social GET
    public String crearObraSocial(ModelMap modelo, String nombre, String email, String telefono) {

        try {
            obraServ.crearObraSocial(nombre, email, telefono);
            modelo.put("exito", "!Obra social registrada con exito!");
            return "";

        } catch (MyException ex) {

            modelo.put("error", ex.getMessage());
            return "";
        }

    }

    @GetMapping("/modificarObra/{id}") // ruta para crear una obra social GET
    public String crearObraSocial(@PathVariable("id") String id, ModelMap modelo) throws MyException {
        modelo.put("obra", obraServ.getObraSocialById(id));
        modelo.addAttribute("id", obraServ.getObraSocialById(id).getId());
        return "modificar_obra.html";

    }

    @PostMapping("/modificarObra/{id}") // ruta para modificar una obra social POST
    public String crearObraSocial(@PathVariable("id") String id, ModelMap modelo, String nombre, String email,
            String telefono) {

        try {
            modelo.put("obra", obraServ.getObraSocialById(id));
            modelo.addAttribute("id", obraServ.getObraSocialById(id).getId());
            obraServ.crearObraSocial(nombre, email, telefono);
            modelo.put("exito", "!Obra social modificada con exito!");
            return "";

        } catch (MyException ex) {

            modelo.put("error", ex.getMessage());
            return "";
        }

    }

    @GetMapping("/eliminarObra/{id}") // ruta para eliminar (no tiene una vista, es para un boton
    public String eliminarObra(@PathVariable("id") String id, ModelMap modelo) {

        try {
            // (agrego siguiente línea: bren)
            obraServ.eliminarObraSocial(id);
            modelo.put("exito", "Obra Social eliminada con exito!");
            return "redirect:";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/";
        }

    }

    @PostMapping("/eliminarObra/{id}") // ruta para eliminar (no tiene una vista, es para un boton
    public String eliminarObra1(@PathVariable("id") String id, ModelMap modelo) {

        try {
            obraServ.eliminarObraSocial(id);
            modelo.put("exito", "Obra Social eliminada con exito!");
            ;
            return "";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "";
        }

    }

}
