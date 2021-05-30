package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CursoExtensaoDto {

    private String participacao;
    private String disciplinas;
    private int cargaHorariaTotal;
    private double valorBrutoHoraAula;
    private double valorBrutoTotalAula;
    private double valorBrutoOutraAtividade;
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
}
