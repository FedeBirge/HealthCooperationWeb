
package com.grupo3.HealthCooperationWeb.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Paciente extends Usuario implements Serializable {

    protected String grupoSanguineo;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Turno> turnos;
    @OneToOne
    private HistoriaClinica historia;
    @ManyToOne

    private ObraSocial obraSocial;

}
