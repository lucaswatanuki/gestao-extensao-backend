package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.dto.ArquivoDto;
import com.ftunicamp.tcc.model.Arquivo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface ArquivoService {
    Arquivo salvar(MultipartFile multipartFile, long atividadeId) throws IOException;

    Arquivo getArquivo(String id);

    List<ArquivoDto> getArquivos(long atividadeId);
}