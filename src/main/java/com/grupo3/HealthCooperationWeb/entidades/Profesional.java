package com.grupo3.HealthCooperationWeb.entidades;

import javax.persistence.Entity;
import javax.persistence.Table;

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
public class Profesional extends Usuario {

    // --falta el enum de especialidad y la clase Descripcion

    // @ManyToOne
    // @JoinColumn(name = "especialidad_id")
    // protected Especialidad especialidad;

    // @OneToOne
    // @JoinColumn(name = "descripcion_id")
    // protected Descripcion descripcion;

    protected Integer valorConsulta;

}
