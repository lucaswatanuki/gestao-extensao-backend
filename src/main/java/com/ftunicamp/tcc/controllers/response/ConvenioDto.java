package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class ConvenioDto {
    private String descricao;
    private String instituicao;
    private String tipoAtividadeSimultanea;
    private String coordenador;
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
