package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.*;
import com.ftunicamp.tcc.dto.ConvenioDto;
import com.ftunicamp.tcc.dto.CursoExtensaoDto;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AtividadeService {

    AtividadeResponse cadastrarConvenio(ConvenioDto request) throws UnsupportedEncodingException, MessagingException;

    AtividadeResponse cadastrarCursoExtensao(CursoExtensaoDto request);

    AtividadeResponse cadastrarRegencia(RegenciaRequest request);

    Response<String> cadastrarAtividadeUnivesp(UnivespRequest request);

    void excluirAtividade(Long id);

    AtividadeResponse editarAtividade(Long id);

    List<AtividadeResponse> listarAtividades();

    ConvenioDto consultarConvenio(long id);

    CursoExtensaoDto consultarCursoExtensao(long id);

    RegenciaDto consultarRegencia(long id);

    void updateConvenio(ConvenioDto convenioDto);

    void updateCursoExtensao(CursoExtensaoDto cursoExtensaoDto);

    void updateRegencia(RegenciaDto regenciaDto);
}
