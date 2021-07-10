package com.ftunicamp.tcc.mappers;

import com.ftunicamp.tcc.dto.ConvenioDto;
import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.Convenio;
import com.ftunicamp.tcc.model.Docente;
import com.ftunicamp.tcc.model.StatusAtividade;
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
public abstract class ConvenioMapper {

    public static final ConvenioMapper INSTANCE = Mappers.getMapper(ConvenioMapper.class);

    @Mappings({
            @Mapping(target = "docente", source = "docente"),
            @Mapping(target = "dataCriacao", expression = "java(localDate.now())"),
            @Mapping(target = "dataModificacao", expression = "java(localDate.now())"),
            @Mapping(target = "observacao", expression = "java(dto.getObservacao() == null ? \"\" : dto.getObservacao())"),
            @Mapping(target = "prazo", source = "dto", qualifiedByName = "verificaPrazo"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "alocacao", ignore = true)
    })
    public abstract Convenio mapToConvenio(ConvenioDto dto, Docente docente, LocalDate localDate);

    @Mappings({
            @Mapping(target = "docente", source = "convenio.docente.nome"),
            @Mapping(target = "autorizado", source = "convenio", qualifiedByName = "verificaStatus"),
            @Mapping(target = "revisao", expression = "java(convenio.getRevisao() == null ? \"" + ITENS_REVISAO + "\" : convenio.getRevisao())")
    })
    public abstract ConvenioDto mapToConvenioDto(Convenio convenio);

    @Named("verificaPrazo")
    public long verificaPrazo(ConvenioDto dto) {
        return ChronoUnit.MONTHS.between(YearMonth.from(dto.getDataInicio()), YearMonth.from(dto.getDataFim()));
    }

    @Named("verificaStatus")
    public boolean verificaStatus(Atividade atividade) {
        return atividade.getStatus().equals(StatusAtividade.EM_ANDAMENTO);
    }
}
