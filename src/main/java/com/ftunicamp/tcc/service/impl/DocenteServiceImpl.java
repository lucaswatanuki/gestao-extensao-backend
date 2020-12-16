package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.entities.DocenteEntity;
import com.ftunicamp.tcc.entities.StatusAtividade;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.repositories.UserRepository;
import com.ftunicamp.tcc.service.DocenteService;
import com.ftunicamp.tcc.utils.AtividadeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final UserRepository userRepository;
    private final AtividadeRepository atividadeRepository;

    @Autowired
    public DocenteServiceImpl(DocenteRepository docenteRepository, UserRepository userRepository, AtividadeRepository atividadeRepository) {
        this.docenteRepository = docenteRepository;
        this.userRepository = userRepository;
        this.atividadeRepository = atividadeRepository;
    }

    @Override
    public List<DocenteResponse> listarDocentes() {

        List<DocenteResponse> response = new ArrayList<>();

        List<DocenteEntity> docentes = docenteRepository.findAll();

        docentes.forEach(docente -> {
            var docenteResponse = new DocenteResponse();
            docenteResponse.setNome(docente.getNome());
            docenteResponse.setMatricula(docente.getMatricula());
            docenteResponse.setEmail(docente.getEmail());
            docenteResponse.setAutorizado(docente.isAutorizado());
            docenteResponse.setTotalHorasEmAndamento(docente.getTotalHorasEmAndamento());
            docenteResponse.setTotalHorasFuturas(docente.getTotalHorasFuturas());
            response.add(docenteResponse);
        });

        return response;
    }

    @Override
    public DocenteResponse consultarDocente(Long id) {
        var docente = docenteRepository.findById(id);

        if (docente.isEmpty()) {
            throw new NegocioException("Docente não cadastrado");
        }

        var atividades = docente.get().getAtividades();

        atividades.forEach(atividade -> {
            var totalHorasAtividade = atividade.getHoraMensal() * atividade.getPrazo();
            var statusAtividadeAtualizado = AtividadeFactory.verificaStatusAtividade(atividade);
            if (!atividade.getStatus().equals(statusAtividadeAtualizado)) {
                atividade.setStatus(statusAtividadeAtualizado);
                atividadeRepository.save(atividade);

                if (statusAtividadeAtualizado.equals(StatusAtividade.CONCLUIDA)) {
                    docente.get().setTotalHorasEmAndamento(docente.get().getTotalHorasEmAndamento() - totalHorasAtividade);
                    docenteRepository.save(docente.get());
                }
                if (statusAtividadeAtualizado.equals(StatusAtividade.EM_ANDAMENTO)) {
                    docente.get().setTotalHorasEmAndamento(docente.get().getTotalHorasEmAndamento() + totalHorasAtividade);
                    docente.get().setTotalHorasFuturas(docente.get().getTotalHorasFuturas() - totalHorasAtividade);
                    docenteRepository.save(docente.get());
                }
            }
        });

        var response = new DocenteResponse();
        response.setTotalHorasEmAndamento(docente.get().getTotalHorasEmAndamento());
        response.setTotalHorasFuturas(docente.get().getTotalHorasFuturas());

        return response;
    }

    @Override
    public void deletarDocente(String username) {
       var docente = docenteRepository.findByUser_Username(username);
       docenteRepository.delete(docente);
       var user = userRepository.findByUsername(username);
       user.ifPresent(userRepository::delete);
    }


}
