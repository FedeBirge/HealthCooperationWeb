
package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
// faltaba completar
public interface HistoriaClinicaRepositorio extends JpaRepository<HistoriaClinica, String> {

    public HistoriaClinica getOne(String id);

    public HistoriaClinica findByPaciente_Id(String id);

}
