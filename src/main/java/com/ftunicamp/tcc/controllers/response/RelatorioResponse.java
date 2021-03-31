package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.model.StatusAtividade;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RelatorioResponse implements Serializable {

    private String nomeDocente;
    private String tipoAtividade;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private StatusAtividade statusAtividade;
    private long prazo;

}
