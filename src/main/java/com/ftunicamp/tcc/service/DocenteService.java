package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.dto.AlocacaoDto;

import java.util.List;

public interface DocenteService {

    List<DocenteResponse> listarDocentes();

    DocenteResponse consultarDocente(Long id);

    void deletarDocente(String username);

    List<AlocacaoDto> consultarAlocacoes(long docenteId);
}
