package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.dto.ArquivoDto;
import com.ftunicamp.tcc.model.Arquivo;
import com.ftunicamp.tcc.repositories.ArquivosRepository;
import com.ftunicamp.tcc.service.ArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ArquivoServiceImpl implements ArquivoService {

    private final ArquivosRepository arquivosRepository;

    @Autowired
    public ArquivoServiceImpl(ArquivosRepository arquivosRepository) {
        this.arquivosRepository = arquivosRepository;
    }

    @Override
    public Arquivo salvar(MultipartFile file, long atividadeId) throws IOException {
        var fileName = StringUtils.cleanPath(file.getOriginalFilename());
        var arquivo = new Arquivo(fileName, file.getContentType(), file.getBytes(), atividadeId);

        return arquivosRepository.save(arquivo);
    }

    @Override
    public Arquivo getArquivo(String id) {
        return arquivosRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Arquivo não encontrado!"));
    }

    @Override
    @Transactional
    public List<ArquivoDto> getArquivos(long atividadeId) {
        return arquivosRepository.findAllByAtividadeId(atividadeId)
                .stream()
                .map(arquivo -> {
                    var downloadPath = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/download/")
                            .path(arquivo.getId())
                            .toUriString();

                    return ArquivoDto.builder()
                            .nome(arquivo.getNome())
                            .tipo(arquivo.getTipo())
                            .url(downloadPath)
                            .tamanho(arquivo.getData().length)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
