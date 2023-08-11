
package com.grupo3.HealthCooperationWeb.entidades;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Usuario{
    
    protected String grupoSanguineo;
    

  
    

   
}
