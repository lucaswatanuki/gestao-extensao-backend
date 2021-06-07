package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.model.Nivel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegenciaDto {

    private Nivel nivel;
    private String curso;
    private String coordenador;
    private String disciplinaParticipacao;
    private int cargaHoraTotalMinistrada;
    private int cargaHorariaTotalDedicada;
    private Double valorBrutoHoraAula;
    private Double valorBrutoTotalAula;
    private Double valorBrutoOutraAtividade;
    private String instituicao;
    private String diasTrabalhadosUnicamp;
    private String diasTrabalhadosOutraInstituicao;
    private boolean responsavel;
    private boolean unicoDocente;
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
