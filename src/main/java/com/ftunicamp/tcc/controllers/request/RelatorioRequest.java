package com.ftunicamp.tcc.controllers.request;

import com.ftunicamp.tcc.entities.StatusAtividade;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

@Data
public class RelatorioRequest {

    private long idDocente;
    private StatusAtividade statusAtividade;
    @Temporal(TemporalType.DATE)
    LocalDate dataInicio;
    @Temporal(TemporalType.DATE)
    LocalDate dataFim;
}
