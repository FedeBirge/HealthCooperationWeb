
package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaRepositorio extends JpaRepository<Ficha, String> {

}
