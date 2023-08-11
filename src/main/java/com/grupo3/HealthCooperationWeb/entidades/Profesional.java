package com.grupo3.HealthCooperationWeb.entidades;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.grupo3.HealthCooperationWeb.enumeradores.Especialidad;
import com.grupo3.HealthCooperationWeb.enumeradores.Rol;

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

    @Enumerated(EnumType.STRING)
    protected Especialidad especialidad;
    protected String valorConsulta;

    // nos faltaría un ENUM para reputacion del 1 al 5
    // protected Reputacion reputacion;

    // @OneToOne
    // protected Descripcion descripcion;

}
