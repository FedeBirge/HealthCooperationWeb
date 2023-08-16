
package com.grupo3.HealthCooperationWeb.entidades;

import com.grupo3.HealthCooperationWeb.enumeradores.TipoOferta;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Oferta {
    
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
    private String telefono;
    @OneToMany
    private List<ObraSocial> obrasSociales;
    
    
    
}
