package com.grupo3.HealthCooperationWeb.entidades;

import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
// Anotaciones Lombok para mejorar la legibilidad del código
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Oferta implements Serializable {

    // elementos necesarios para detallar lo que ofrece un profesional
    // a un dia especifico de la semana
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Enumerated(EnumType.STRING)
    private TipoOferta tipo;
    private String horaInicio;
    private String horaFin;
    private String duracionTurno;
    private String ubicacion;
    @ManyToMany
    private List<ObraSocial> obrasSociales;

}
