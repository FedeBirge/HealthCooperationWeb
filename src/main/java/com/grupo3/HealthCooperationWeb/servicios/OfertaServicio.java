package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.OfertaRepositorio;
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

    @Transactional
    // Metodo para crear oferta
    public void crearOferta(TipoOferta tipo, String horaIni, String horaFin, String duracion,
            String ubicacion, String telefono, List<ObraSocial> obras) throws MyException {
        // Se validan los datos ingresados
        validar(tipo, horaIni, horaFin, duracion, ubicacion, telefono, obras);

        Oferta oferta = new Oferta();
        oferta.setTipo(tipo);
        oferta.setHoraInicio(horaIni);
        oferta.setHoraFin(horaFin);
        oferta.setDuracionTurno(duracion);
        oferta.setUbicacion(ubicacion);
        oferta.setTelefono(telefono);
        oferta.setObrasSociales(obras);

        ofertaRepo.save(oferta);

    }

    @Transactional
    // Metodo para modificar un usuario
    public void modificarOferta(String id, TipoOferta tipo, String horaIni, String horaFin,
            String duracion, String ubicacion, String telefono, List<ObraSocial> obras)
            throws MyException {

        validar(tipo, horaIni, horaFin, duracion, ubicacion, telefono, obras);

        Optional<Oferta> respuesta = ofertaRepo.findById(id);

        if (respuesta.isPresent()) {
            Oferta oerta = respuesta.get();

            Oferta oferta = new Oferta();
            oferta.setTipo(tipo);
            oferta.setHoraInicio(horaIni);
            oferta.setHoraFin(horaFin);
            oferta.setDuracionTurno(duracion);
            oferta.setUbicacion(ubicacion);
            oferta.setTelefono(telefono);
            oferta.setObrasSociales(obras);

            ofertaRepo.save(oferta);

        }

    }

    @Transactional
    // Metodo para eliminar un usuario, se cambia el estado a inactivo
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

    // Metodo para listar todos los usuarios
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
    protected void validar(TipoOferta tipo, String horaIni, String horaFin,
            String duracion, String ubicacion, String telefono,
            List<ObraSocial> obras) throws MyException {

        if (tipo == null) {
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

        if (telefono == null || telefono.isEmpty()) {
            throw new MyException("Debe ingresar un telefono de contacto");
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

}
