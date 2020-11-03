package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.entities.DocenteEntity;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.service.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;

    @Autowired
    public DocenteServiceImpl(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    @Override
    public List<DocenteResponse> listarDocentes() {

        List<DocenteResponse> response = new ArrayList<>();

        List<DocenteEntity> docentes = docenteRepository.findAll();

        docentes.forEach(docente -> {
            var docenteResponse = new DocenteResponse();
            docenteResponse.setNome(docente.getNome());
            docenteResponse.setRf(docente.getRf());
            docenteResponse.setEmail(docente.getEmail());
            docenteResponse.setAutorizado(docente.isAutorizado());
            docenteResponse.setTotalHoras(docente.getTotalHoras());

            response.add(docenteResponse);
        });

        return response;
    }
}
