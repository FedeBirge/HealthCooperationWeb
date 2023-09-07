package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.FichaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.HistoriaClinicaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;
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
public class FichaServicio {

    @Autowired
    private HistoriaClinicaServicio historiaServ;
    @Autowired
    private FichaRepositorio fichaRepositorio;
    @Autowired
    private UsuarioRepositorio UsuarioRepositorio;
    @Autowired
    private PacienteServicio pacienteServicio;
    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;
    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    @Transactional
    // método que usamos internamente (bren)
    public void crearFicha(String fecha_consulta, String nota, String idPaciente) throws MyException {

        validar(fecha_consulta, nota);
             System.out.println("ficha crear");
        Ficha ficha = new Ficha();
        ficha.setFecha_consulta(pasoDeStringDate(fecha_consulta));
        ficha.setNota(nota);
        fichaRepositorio.save(ficha);
        guardarFichaEnHCPaciente(ficha, idPaciente);

    }

    @Transactional
    // método que usamos internamente (bren)
    public void guardarFichaEnHCPaciente(Ficha ficha, String idPaciente) throws MyException {
             System.out.println("ficha guardar paciene");
        List<Ficha> fichas = new ArrayList<>();
        HistoriaClinica hc = new HistoriaClinica();
        // igualo la variable hc a la HistoriaClinica del paciente con el id buscado
         Paciente paciente = (Paciente) pacienteServicio.getOne(idPaciente);
        hc = historiaClinicaRepositorio.findByPaciente_Id(idPaciente);
       
        // igualo la variable fichas a las fichas disponibles en la hc encontrada
        fichas = hc.getFichas();
       
        
        // agrego la nueva fichita al lista de fichas
        fichas.add(ficha);
        // seteo la historia clínica de ese paciente,
        // cargando la HC con las fichas anteriores y la nueva
       hc.setFichas(fichas);
        paciente.setHistoria(hc);
          System.out.println(paciente.getHistoria());
        historiaClinicaRepositorio.save(paciente.getHistoria());
        pacienteRepositorio.save(paciente);

    }

    @Transactional
    // método que va a un controlador (bren)
    public void agregarFicha(String idPaciente, String fecha_consulta, String nota) throws MyException {

        Paciente paciente = (Paciente) pacienteServicio.getOne(idPaciente);
        System.out.println("ficha agregar");
        if (paciente == null) {
            throw new MyException("No existe un paciente con ese ID");
        } else {
            crearFicha(fecha_consulta, nota, idPaciente);
        }

    }

    // (edito para que se muestren todas las fichas según paciente: bren)
    public List<Ficha> mostrarFichas(String idPaciente) throws MyException {
        Paciente paciente = (Paciente) pacienteServicio.getOne(idPaciente);

        if (paciente == null) {
            throw new MyException("No existe un paciente con ese ID");
        } else {
            List<Ficha> fichas = new ArrayList();
            fichas = paciente.getHistoria().getFichas();
            return fichas;
        }
    }

    // (mariela)
    protected void validar(String fecha_consulta, String nota) throws MyException {

        if (fecha_consulta == null) {
            throw new MyException("Debe completar la fecha");
        }
        if (nota == null || nota.isEmpty()) {
            throw new MyException("Debe ingresar una descripcion de la consulta");
        }
    }

    // (mariela)
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

    // (mariela)
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
