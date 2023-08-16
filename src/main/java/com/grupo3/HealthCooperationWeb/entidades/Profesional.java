package com.grupo3.HealthCooperationWeb.entidades;

import com.grupo3.HealthCooperationWeb.enumeradores.Dias;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import java.io.Serializable;
import java.util.List;
import javax.persistence.ElementCollection;
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
public class Profesional extends Usuario implements Serializable {

    @Enumerated(EnumType.STRING)
    protected Especialidad especialidad;
    protected String valorConsulta;

    protected Integer reputacion;
    protected String descripcion;
    @OneToOne
    protected Oferta oferta;
    @OneToOne
    protected AgendaSemanal agenda;
    
    @ElementCollection(targetClass = Dias.class)
    @Enumerated(EnumType.STRING)
    private List<Dias> diasDisponibles;
    
   

}
