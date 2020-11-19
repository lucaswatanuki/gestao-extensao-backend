package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeDetalheResponse;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.controllers.response.Response;

import java.util.List;

public interface AtividadeService {

    Response<String> cadastrarConvenio(ConvenioRequest request);

    Response<String> cadastrarCursoExtensao(CursoExtensaoRequest request);

    Response<String> cadastrarRegencia(RegenciaRequest request);

    Response<String> cadastrarAtividadeUnivesp(UnivespRequest request);

    AtividadeDetalheResponse buscarAtividade(Long id);

    void excluirAtividade(Long id);

    AtividadeResponse editarAtividade(Long id);

    List<AtividadeResponse> listarAtividades();
}
