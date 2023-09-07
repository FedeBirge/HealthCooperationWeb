package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaRepositorio extends JpaRepository<Oferta, String> {
    
    @Query("SELECT o FROM Oferta o WHERE o.tipo =:tipo")
    public Oferta buscarPorTipo(@Param("tipo") String tipo);

    
}
