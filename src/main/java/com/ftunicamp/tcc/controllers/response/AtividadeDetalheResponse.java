package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AtividadeDetalheResponse {

    private long id;
    private String projeto;
    private Double valorBruto;
    private String docente;
    private int horaMensal;
    private int horaSemanal;
    private long prazo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String observacao;
    private boolean autorizado;
    private String tipoAtividade;
    private String revisao;
    private List<AlocacaoDto> alocacoes;
    private boolean excedido;

}
