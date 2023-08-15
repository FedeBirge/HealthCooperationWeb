package com.grupo3.HealthCooperationWeb.entidades;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.grupo3.HealthCooperationWeb.enumeradores.Dias;

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

    /*
     * @ElementCollection
     * 
     * @CollectionTable(name = "agenda_turnos", joinColumns = @JoinColumn(name =
     * "agenda_id"))
     * 
     * @MapKeyEnumerated(EnumType.STRING) // Esto es necesario si "Dias" es un enum
     * 
     * @MapKeyColumn(name = "dia")
     * 
     * @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
     * private Map<Dias, Turno> turnosXDia = new HashMap<>();
     */
}
