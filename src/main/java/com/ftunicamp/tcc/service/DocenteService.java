package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;

import java.util.List;

public interface DocenteService {

    List<DocenteResponse> listarDocentes();

    void deletarDocente(String username);
}
