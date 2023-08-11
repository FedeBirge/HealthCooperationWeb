
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
import org.springframework.context.annotation.Primary;

//
@Primary
@Entity
// Anotaciones Lombok para mejorar la legibilidad del código
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Clase Usuario, que representa a un usuario del sistema.
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
    private Date fecha_nac; // Fecha de nacimiento
    private Boolean activo; // Para indicar si el usuario es eliminado o no, y no borrarlo de la base de
                            // datos
    //
    // @Enumerated(EnumType.STRING)
    // private Rol rol; // Rol del usuario
    // @OneToOne
    // private Imagen imagen; // Imagen de perfil del usuario

}
