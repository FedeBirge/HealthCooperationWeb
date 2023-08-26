package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepositorio extends JpaRepository<Turno, String> {

}
