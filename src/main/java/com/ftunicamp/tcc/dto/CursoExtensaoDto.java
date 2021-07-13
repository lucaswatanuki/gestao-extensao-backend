package com.ftunicamp.tcc.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursoExtensaoDto {

    private String participacao;
    private String disciplinas;
    private int totalHorasMinistradas;
    private int totalHorasOutrasAtividades;
    private Double valorBrutoHora = 0.0;
    private Double valorBrutoTotal = 0.0;
    private int periodo;
    private String nomeCurso;
    private String instituicaoVinculada;
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
    private boolean urgente;
}
