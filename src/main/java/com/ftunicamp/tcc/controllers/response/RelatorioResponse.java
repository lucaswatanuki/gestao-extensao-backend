package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.entities.StatusAtividade;
import com.ftunicamp.tcc.entities.TipoAtividade;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class RelatorioResponse implements Serializable {

    private String nomeDocente;
    private String tipoAtividade;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private StatusAtividade statusAtividade;

}
