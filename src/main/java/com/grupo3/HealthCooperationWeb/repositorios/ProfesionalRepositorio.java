package com.grupo3.HealthCooperationWeb.repositorios;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
// extiendo de CrudRepository para poder implementar todos sus métodos
// interpreta todos los llamados CRUD
public interface ProfesionalRepositorio extends JpaRepository<Profesional, String> {

    @Query("SELECT p FROM Profesional p WHERE p.especialidad = :especialidad")
    public abstract ArrayList<Profesional> findByEspecialidad(@Param("especialidad") Especialidad especialidad);

    public Profesional findByEmail(@Param("email") String email);

}
