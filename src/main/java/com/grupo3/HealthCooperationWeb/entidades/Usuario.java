
package com.grupo3.HealthCooperationWeb.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Getter @Setter
@NoArgsConstructor  
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String password;
    private String telefono;
    private String direccion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_nac;
    private Boolean activo;
//    
//    @Enumerated(EnumType.STRING)
//    private Rol rol;
//    @OneToOne
//    private Imagen imagen;
    
}
