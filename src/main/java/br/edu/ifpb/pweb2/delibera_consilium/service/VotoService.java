package br.edu.ifpb.pweb2.delibera_consilium.service;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.TipoVoto;
import br.edu.ifpb.pweb2.delibera_consilium.model.Voto;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProcessoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public Voto registrarVoto(Long processoId, Long professorId, TipoVoto tipoVoto, String justificativa) {
        Processo processo = processoRepository.findById(processoId)
                .orElseThrow(() -> new RuntimeException("Processo nao encontrado"));
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor nao encontrado"));

        // Verifica se o professor ja votou neste processo
        Optional<Voto> votoExistente = votoRepository.findByProcessoAndProfessor(processo, professor);

        Voto voto;
        if (votoExistente.isPresent()) {
            // Atualiza o voto existente
            voto = votoExistente.get();
        } else {
            // Cria novo voto
            voto = new Voto();
            voto.setProcesso(processo);
            voto.setProfessor(professor);
        }

        voto.setVoto(tipoVoto);
        voto.setJustificativa(justificativa);
        voto.setAusente(false);

        return votoRepository.save(voto);
    }

    public Voto registrarAusencia(Long processoId, Long professorId) {
        Processo processo = processoRepository.findById(processoId)
                .orElseThrow(() -> new RuntimeException("Processo nao encontrado"));
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor nao encontrado"));

        Optional<Voto> votoExistente = votoRepository.findByProcessoAndProfessor(processo, professor);

        Voto voto;
        if (votoExistente.isPresent()) {
            voto = votoExistente.get();
        } else {
            voto = new Voto();
            voto.setProcesso(processo);
            voto.setProfessor(professor);
        }

        voto.setAusente(true);
        voto.setVoto(null);
        voto.setJustificativa(null);

        return votoRepository.save(voto);
    }

    public List<Voto> listarPorProcesso(Long processoId) {
        return votoRepository.findByProcessoId(processoId);
    }

    public List<Voto> listarPorProcesso(Processo processo) {
        return votoRepository.findByProcesso(processo);
    }

    public boolean professorJaVotou(Processo processo, Professor professor) {
        return votoRepository.existsByProcessoAndProfessor(processo, professor);
    }

    public Optional<Voto> buscarVoto(Processo processo, Professor professor) {
        return votoRepository.findByProcessoAndProfessor(processo, professor);
    }
}
