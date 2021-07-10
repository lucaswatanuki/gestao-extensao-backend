package com.ftunicamp.tcc.mappers;

import com.ftunicamp.tcc.dto.CursoExtensaoDto;
import com.ftunicamp.tcc.model.Convenio;
import com.ftunicamp.tcc.model.CursoExtensao;
import com.ftunicamp.tcc.model.Docente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public abstract class CursoMapper {

    public static final CursoMapper INSTANCE = Mappers.getMapper(CursoMapper.class);

    @Mappings({
            @Mapping(target = "docente", source = "docente"),
            @Mapping(target = "dataCriacao", expression = "java(localDate.now())"),
            @Mapping(target = "dataModificacao", expression = "java(localDate.now())"),
            @Mapping(target = "observacao", expression = "java(dto.getObservacao() == null ? \"\" : dto.getObservacao())"),
            @Mapping(target = "prazo", source = "dto", qualifiedByName = "verificaPrazo"),
            @Mapping(target = "projeto", source = "dto.nomeCurso"),
            @Mapping(target = "disciplinaParticipacao", source = "dto.disciplinas"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "alocacao", ignore = true)
    })
    public abstract CursoExtensao mapToCursoExtensao(CursoExtensaoDto dto, Docente docente, LocalDate localDate);

    @Mappings({
            @Mapping(target = "docente", source = "curso.docente.nome")
    })
    public abstract CursoExtensaoDto mapToCursoExtensaoDto(CursoExtensao curso);

    @Named("verificaPrazo")
    public long verificaPrazo(CursoExtensaoDto dto) {
        return ChronoUnit.MONTHS.between(YearMonth.from(dto.getDataInicio()), YearMonth.from(dto.getDataFim()));
    }
}
