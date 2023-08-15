
package com.grupo3.HealthCooperationWeb.repositorios;


import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriaClinicaRepositorio extends CrudRepository{

    public HistoriaClinica getOne(String id);
    
    
    
}
