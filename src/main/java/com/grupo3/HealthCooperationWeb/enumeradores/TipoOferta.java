/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo3.HealthCooperationWeb.enumeradores;

/**
 *
 * @author Ezequiel
 */
public enum TipoOferta {
    
    PRESENCIAL("Presencial"),
    VIRTUAL("Virtual"),
   DOMICILIO("Domicilio");

    private final String descripcion;

    TipoOferta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

  
}
