package com.ftunicamp.tcc.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AtividadeResponse implements Serializable {

    private Long id;
    private LocalDate dataCriacao;
    private long prazo;
    private String projeto;

}
