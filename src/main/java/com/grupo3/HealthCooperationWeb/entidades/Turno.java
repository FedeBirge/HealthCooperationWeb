
package com.grupo3.HealthCooperationWeb.entidades;

import com.grupo3.HealthCooperationWeb.enumeradores.EstadoTurno;
import java.io.Serializable;
import javax.persistence.Entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Turno implements Serializable {
    
    // representa un turno para el paciente, con su fecha y hora, 
    // y el motivo del mismo, asociado al profesional
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;
  private String fecha;
  private String hora;
  @Enumerated(EnumType.STRING)
  private EstadoTurno estado;
  private String motivo;
  
  @OneToOne
  private Profesional profesional;

}