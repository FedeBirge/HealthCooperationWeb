package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.ObraSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ObraSocialRepositorio extends JpaRepository<ObraSocial, String> {
    @Query("SELECT o FROM ObraSocial o WHERE o.email =:email")
    public ObraSocial buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT o FROM ObraSocial o WHERE o.nombre =:nombre")
    public ObraSocial findByNombre(@Param("nombre") String nombre);

    public Optional<ObraSocial> findById(String id);

}
