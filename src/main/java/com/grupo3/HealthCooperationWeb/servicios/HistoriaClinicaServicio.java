
package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Turno;
import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import com.grupo3.HealthCooperationWeb.repositorios.HistoriaClinicaRepositorio;
import com.grupo3.HealthCooperationWeb.repositorios.PacienteRepositorio;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoriaClinicaServicio {

    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;
    @Autowired
    PacienteRepositorio pacienteRepositorio;
    @Autowired
    PacienteServicio pacienteServicio;

    // dejo esta opción de crear Historia Clínica, pero agrego opción 2 a evaluar
    // (bren)
    @Transactional
    public void crearHistoriaClinica() throws MyException {

        HistoriaClinica historiaClinica = new HistoriaClinica();

        historiaClinica.setFichas(new ArrayList<Ficha>());

        historiaClinicaRepositorio.save(historiaClinica);

    }

    // opcion 2: crear historia clínica con el id de Paciente como un parámetro
    // (bren)
    @Transactional
    public void crearHistoriaClinica(String idPaciente) throws MyException {
        Paciente paciente = (Paciente) pacienteServicio.getOne(idPaciente);

        if (paciente == null) {
            throw new MyException("No existe el paciente con ese id");
        }

        HistoriaClinica historiaClinica = new HistoriaClinica();

        historiaClinica.setFichas(new ArrayList<Ficha>());
        // al paciente TAL con X id, le agrego su historia clínica creada
        paciente.setHistoria(historiaClinica);
        historiaClinicaRepositorio.save(historiaClinica);
        // DUDA: no sé si es innecesario guardar tanto en el repo de HistoriaClinica
        // como en el de PACIENTE:
        pacienteRepositorio.save(paciente);

    }

    @Transactional
    public void guardarHistoria(HistoriaClinica historiaClinica) {

        historiaClinicaRepositorio.save(historiaClinica);
    }

    @Transactional
    // no comprendo este método, la lista debería ser de turnos, cada paciente tiene
    // una relacion uno a uno con la Historia clínica, dejo opcion 2 (bren)
    public List<HistoriaClinica> mostrarHistoria() {
        List<HistoriaClinica> historia = new ArrayList();
        historia = (List<HistoriaClinica>) historiaClinicaRepositorio.findAll();
        return historia;
    }

    // opcion 2
    @Transactional
    // agrego parámetro idPaciente y completo lógica
    // para que se vea la historia clinica de UN paciente (bren)
    public HistoriaClinica mostrarHistoria(String idPaciente) throws MyException {

        Paciente paciente = (Paciente) pacienteServicio.getOne(idPaciente);

        if (paciente.getHistoria() == null) {
            throw new MyException("El paciente no tiene Historia Clínica, debe crearla");
        } else {
            return paciente.getHistoria();
        }
    }

    // por el momento no usé este método, sino el getOne de Paciente (bren)
    public HistoriaClinica getOne(String id) {
        return historiaClinicaRepositorio.getOne(id);
    }

}
