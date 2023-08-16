package com.grupo3.HealthCooperationWeb.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
public class HistoriaClinica  {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
     /*
        las relaciones en el UML son atributos de alguna entidad
            y se debe indicar con anotaciones cual es ese tipo de relacion
    */
    @OneToMany
    private List<Ficha> fichas;

}
