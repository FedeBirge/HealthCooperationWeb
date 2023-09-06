package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    
    @Query("SELECT u FROM Usuario u WHERE u.email =:email")
    public Usuario buscarPorEmail(@Param("email") String email);

      @Query("SELECT u FROM Usuario u WHERE u.dni =:dni")
    public Usuario buscarPorDni(@Param("dni") String dni);
}
