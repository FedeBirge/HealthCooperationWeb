package com.grupo3.HealthCooperationWeb.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter

class Ficha extends Paciente {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private String nota;
    
    
    /*
        las relaciones en el UML son atributos de alguna entidad
            y se debe indicar con anotaciones cual es ese tipo de relacion
    */
    @OneToOne   
    private Profesional profesional;

}