
package com.grupo3.HealthCooperationWeb.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Usuario{
    
    protected String grupoSanguineo;
    
    /*
        las relaciones en el UML son atributos de alguna entidad
            y se debe indicar con anotaciones cual es ese tipo de relacion
    */
    
    @OneToMany
    private List<Turno> turnos;
    @OneToOne 
    private HistoriaClinica historia;
    
    private String obraSocial;
    

   
}
