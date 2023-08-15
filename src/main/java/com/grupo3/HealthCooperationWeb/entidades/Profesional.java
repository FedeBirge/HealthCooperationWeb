package com.grupo3.HealthCooperationWeb.entidades;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profesionales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profesional extends Usuario implements Serializable {

    @Enumerated(EnumType.STRING)
    protected Especialidad especialidad;
    protected String valorConsulta;
    // falta incluir reputación y descripcion como parámetro
    // en todos los servicios que lo requieran
    protected Integer reputacion;
    protected String descripcion;
    // protected AgendaSemanal agenda;
    // protected Disponibilidad disponible;

}
