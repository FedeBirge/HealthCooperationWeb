
package com.grupo3.HealthCooperationWeb.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
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
// Entidad para guardar imagenes en la base de datos
public class Imagen {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id; // id de la imagen
    private String mime; // tipo de imagen
    private String nombre; // nombre de la imagen

    @Lob // para guardar archivos en la base de datos
    @Basic(fetch = FetchType.LAZY) // para que no se cargue la imagen hasta que se la necesite
    private byte[] contenido; // contenido de la imagen

}
