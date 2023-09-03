
package com.grupo3.HealthCooperationWeb.repositorios;

import com.grupo3.HealthCooperationWeb.entidades.HistoriaClinica;
import com.grupo3.HealthCooperationWeb.entidades.Paciente;
import com.grupo3.HealthCooperationWeb.entidades.Profesional;
import com.grupo3.HealthCooperationWeb.entidades.Usuario;
import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
// creando interface marcada con @Repository que extiende de JpaRepository
// manejando la entidad Paciente cuya llave es de tipo de dato string
public interface PacienteRepositorio extends JpaRepository<Paciente, String> {

    public Optional<Paciente> findById(Paciente usuario);

    public Paciente findByEmail(@Param("email") String email);

    // MSJ LT: la queryno es necesaria, se usa lo del padre

    // @Query("SELECT p FROM Paciente p WHERE p.usuario.nombre = :nombre")
    // public Paciente buscarPorNombre(@Param("nombre") String nombre);

}
