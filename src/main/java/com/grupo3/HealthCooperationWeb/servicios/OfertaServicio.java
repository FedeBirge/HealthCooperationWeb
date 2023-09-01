package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.OfertaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.ProfesionalRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfertaServicio {

    @Autowired
    private OfertaRepositorio ofertaRepo;
    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;

    // paso el string que viene del controlador al Emun correspondiente
    public TipoOferta pasarStringTipo(String tipo) throws MyException {
        switch (tipo) {
            case "PRESENCIAL":
                return TipoOferta.PRESENCIAL;
            case "VIRTUAL":
                return TipoOferta.VIRTUAL;
            case "DOMICILIO":
                return TipoOferta.DOMICILIO;
            default:
                throw new MyException("Tipo de oferta no válido: " + tipo);
        }
    }

    @Transactional
    // Metodo para crear oferta
    public Oferta crearOferta(String tipo, String horaIni, String horaFin, String duracion,
            String ubicacion, List<ObraSocial> obras) throws MyException {
        // Se validan los datos ingresados
        validar(tipo, horaIni, horaFin, duracion, ubicacion, obras);

        Oferta oferta = new Oferta();
        oferta.setTipo(pasarStringTipo(tipo));
        oferta.setHoraInicio(horaIni);
        oferta.setHoraFin(horaFin);
        oferta.setDuracionTurno(duracion);
        oferta.setUbicacion(ubicacion);

        oferta.setObrasSociales(obras);

        ofertaRepo.save(oferta);
        return oferta;

    }

    @Transactional
    // Metodo para modificar una oferta
    public Oferta modificarOferta(String id, String tipo, String horaIni, String horaFin,
            String duracion, String ubicacion, List<ObraSocial> obras)
            throws MyException {

        validar(tipo, horaIni, horaFin, duracion, ubicacion, obras);

        Optional<Oferta> respuesta = ofertaRepo.findById(id);

        if (respuesta.isPresent()) {
            Oferta oferta = respuesta.get();           
            oferta.setTipo(pasarStringTipo(tipo));
            oferta.setHoraInicio(horaIni);
            oferta.setHoraFin(horaFin);
            oferta.setDuracionTurno(duracion);
            oferta.setUbicacion(ubicacion);

            oferta.setObrasSociales(obras);

            ofertaRepo.save(oferta);
            return oferta;

        }
        return null;

    }

    @Transactional
    // Metodo para eliminar ua oferta
    public void eliminarOferta(String id) {
        try {
            Optional<Oferta> resp = ofertaRepo.findById(id);
            if (resp.isPresent()) {
                Oferta oferta = (Oferta) (resp.get());
                ofertaRepo.delete(oferta);
            }
        } catch (Exception e) {
            System.out.println("No es posible eliminar la oferta");
        }
    }

    // Metodo para listar todas las ofertas
    public List<Oferta> listarOfertas() {
        List<Oferta> ofertas = new ArrayList();
        try {
            ofertas = ofertaRepo.findAll();

            return ofertas;

        } catch (Exception e) {
            System.out.println("No pudieron ser listadas las ofertas");
            return null;
        }

    }

    // Metodo para validar los datos ingresados antes de persistirlos
    protected void validar(String tipo, String horaIni, String horaFin,
            String duracion, String ubicacion, List<ObraSocial> obras) throws MyException {

        if (tipo == null || tipo.isEmpty()) {
            throw new MyException("Debe indicar un tipo de oferta");
        }
        if (horaIni == null || horaIni.isEmpty()) {
            throw new MyException("Debe ingresar hora de inicio");
        }
        if (horaFin == null || horaFin.isEmpty()) {
            throw new MyException("Debe ingresar hora de final");
        }

        if (duracion == null || duracion.isEmpty()) {
            throw new MyException("Debe ingresar cuanto dura un turno");
        }

        if (ubicacion == null || ubicacion.isEmpty()) {
            throw new MyException("Debe ingresar una ubicacion(direccion)");
        }
        if (obras == null) {
            throw new MyException("Debe ingresar un nombre");
        }

    }

    // Metodo para buscar un usuario por su id y devolverlo
    public Oferta getOne(String id) {
        return ofertaRepo.getOne(id);
    }

    public Oferta obtenerOfertaxProf(String id) {
        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Profesional prof = respuesta.get();
            return prof.getOferta();
        }
        return null;
    }

}
