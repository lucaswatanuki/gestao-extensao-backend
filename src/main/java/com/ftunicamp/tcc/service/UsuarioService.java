package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.dto.UsuarioDto;

public interface UsuarioService {
    UsuarioDto getDadosUsuario(long id);
    void alterarDadosUsuario(long id, UsuarioDto request);
}
