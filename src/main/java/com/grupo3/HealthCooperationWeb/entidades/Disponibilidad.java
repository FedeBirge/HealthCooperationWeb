
package com.grupo3.HealthCooperationWeb.entidades;

import com.grupo3.HealthCooperationWeb.enumeradores.Dias;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyEnumerated;
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
public class Disponibilidad {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
      @ElementCollection
    @CollectionTable(name = "dias_disponibles",
                     joinColumns = @JoinColumn(name = "entidad_principal_id"))
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Dias, Oferta> diasDisponibles;
    
}
