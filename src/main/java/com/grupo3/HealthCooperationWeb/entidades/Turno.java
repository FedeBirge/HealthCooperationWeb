
package com.grupo3.HealthCooperationWeb.entidades;


import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Turno extends Usuario{
   @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String fecha;
    private String hora;
    private EstadoTurno estado;
    private String motivo;
    
     /*
        las relaciones en el UML son atributos de alguna entidad
            y se debe indicar con anotaciones cual es ese tipo de relacion
    */
      @OneToOne 
    private Profesional profesional;

}