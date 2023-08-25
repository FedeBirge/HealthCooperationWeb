package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ObraSocialRepositorio extends JpaRepository<ObraSocial, String> {
  
    @Query("SELECT o FROM ObraSocial o WHERE o.email =:email")
    public ObraSocial buscarPorEmail(@Param("email") String email);
   
}
