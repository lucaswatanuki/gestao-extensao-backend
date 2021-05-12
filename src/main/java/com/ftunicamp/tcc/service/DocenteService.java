package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.dto.AlocacaoDto;

import java.util.List;

public interface DocenteService {

    List<DocenteResponse> listarDocentes();

    void deletarDocente(String username);

    List<AlocacaoDto> consultarAlocacoesDocente(long docenteId);

    List<AlocacaoDto> getAlocacoes();

    void atualizarAlocacao(AlocacaoDto dto);

}
