package com.ftunicamp.tcc.mappers;

import com.ftunicamp.tcc.controllers.response.RegenciaDto;
import com.ftunicamp.tcc.dto.CursoExtensaoDto;
import com.ftunicamp.tcc.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import static com.ftunicamp.tcc.service.impl.AtividadeServiceImpl.ITENS_REVISAO;

@Mapper(componentModel = "spring")
public abstract class RegenciaMapper {

    public static final RegenciaMapper INSTANCE = Mappers.getMapper(RegenciaMapper.class);

    @Mappings({
            @Mapping(target = "docente", source = "docente"),
            @Mapping(target = "dataCriacao", expression = "java(localDate.now())"),
            @Mapping(target = "dataModificacao", expression = "java(localDate.now())"),
            @Mapping(target = "observacao", expression = "java(dto.getObservacao() == null ? \"\" : dto.getObservacao())"),
            @Mapping(target = "prazo", source = "dto", qualifiedByName = "verificaPrazo"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "alocacao", ignore = true)
    })
    public abstract Regencia mapToRegencia(RegenciaDto dto, Docente docente, LocalDate localDate);

    @Mappings({
            @Mapping(target = "docente", source = "regencia.docente.nome"),
            @Mapping(target = "autorizado", source = "regencia", qualifiedByName = "verificaStatus"),
            @Mapping(target = "revisao", expression = "java(regencia.getRevisao() == null ? \"" + ITENS_REVISAO + "\" : regencia.getRevisao())")
    })
    public abstract RegenciaDto mapToRegenciaDto(Regencia regencia);

    @Named("verificaPrazo")
    public long verificaPrazo(RegenciaDto dto) {
        return ChronoUnit.MONTHS.between(YearMonth.from(dto.getDataInicio()), YearMonth.from(dto.getDataFim()));
    }

    @Named("verificaStatus")
    public boolean verificaStatus(Atividade atividade) {
        return atividade.getStatus().equals(StatusAtividade.EM_ANDAMENTO);
    }
}
