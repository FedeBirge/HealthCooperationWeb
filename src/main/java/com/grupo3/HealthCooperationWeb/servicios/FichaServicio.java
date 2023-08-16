package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.FichaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.UsuarioRepositorio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class FichaServicio extends HistoriaClinicaServicio {

    @Autowired
    private FichaRepositorio fichaRepositorio;

    @Autowired
    private UsuarioRepositorio UsuarioRepositorio;

    @Transactional
    public void crearFicha(String id, Date fecha_consulta, String nota) throws MyException {

        validar(fecha_consulta, nota);

        Ficha ficha = new Ficha();
        ficha.setId(id);
        ficha.setFecha_consulta(fecha_consulta);
        ficha.setNota(nota);

        fichaRepositorio.save(ficha);

    }
    
    public void guardarFicha(Ficha ficha){
        
        fichaRepositorio.save(ficha);
        
    }

    @Transactional
    public void modificarFicha(String id, Date fecha_consulta, String nota) throws MyException {

        validar(fecha_consulta, nota);
        
        Optional<Ficha> respuesta = fichaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Ficha ficha = respuesta.get();

            ficha.setFecha_consulta(fecha_consulta);
            ficha.setNota(nota);

            fichaRepositorio.save(ficha);
        }

    }
    
   
    
    
    public List<Ficha> mostrarFichas(){
        
        List<Ficha> fichas = new ArrayList();
        
        
        fichas = fichaRepositorio.findAll();
        
        return fichas;
    }
    
    
    

    protected void validar(Date fecha_consulta, String nota) throws MyException {

        if (nota == null || nota.isEmpty()) {

            throw new MyException("Debe ingresar una descripcion de la consulta");
        }

    }

    protected Date pasoDeStringDate(String fecha) {

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            Date date = dateFormat.parse(fecha);

            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        Usuario usuario = UsuarioRepositorio.buscarPorEmail(email);

        if (usuario != null && usuario.getActivo().equals(Boolean.TRUE)) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

}
