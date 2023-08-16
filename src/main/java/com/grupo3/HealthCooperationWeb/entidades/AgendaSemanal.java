package com.grupo3.HealthCooperationWeb.entidades;


import java.util.Map;


import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;


import org.hibernate.annotations.GenericGenerator;


import java.util.ArrayList;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaSemanal {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

  
   @ElementCollection
    @CollectionTable(name = "fecha_y_turnos",
                     joinColumns = @JoinColumn(name = "entidad_principal_id"))
    @MapKeyColumn(name = "fecha")
   
    private Map<Date, ArrayList<Turno>> fechaYTurnos;


    
}