package com.grupo3.HealthCooperationWeb.repositorios;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;

@Repository
// extiendo de CrudRepository para poder implementar todos sus métodos
// interpreta todos los llamados CRUD
public interface ProfesionalRepositorio extends CrudRepository<Profesional, String> {
    // hago un método abstracto findByEspecialidad
    // (podría ser findByLoQueSea, el atributo q necesitemos)
    // haciéndolo abstracto, SpringBoot interpreta la acción buscada
    public abstract ArrayList<Profesional> findByEspecialidad(Especialidad especialidad);

}
