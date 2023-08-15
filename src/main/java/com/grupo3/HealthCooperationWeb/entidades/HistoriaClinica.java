package com.grupo3.HealthCooperationWeb.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HistoriaClinica extends Paciente {

    @Id

    private String id;

    List<Ficha> fichas = new ArrayList();

}
