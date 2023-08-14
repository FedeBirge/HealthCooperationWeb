package com.egg.eggNews.repositorios;


import com.grupo3.HealthCooperationWeb.entidades.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoRepositorio extends JpaRepository<Turno, Long> {

    @Query("SELECT t, COUNT(t) AS cantidad FROM Turno t GROUP BY t ORDER BY cantidad DESC")
    List<Object[]> encontrarTurnosMasUtilizados();
    
    /*La consulta busca todos los turnos en la tabla Turno, 
    cuenta cuántas veces se repite cada turno y los ordena en 
    orden descendente según la cantidad de repeticiones. 
    El resultado de la consulta es una lista de arrays de 
    objetos, donde cada array contiene un objeto Turno y 
    la cantidad de veces que se repite.*/
}

