package br.edu.ifpb.pweb2.delibera_consilium.service;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProcessoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public List<Processo> listarTodos() {
        return processoRepository.findAll();
    }
    
    // Filtro pro professor ver os dele (REQFUNC 3)
    public List<Processo> listarPorRelator(Professor relator) {
        return processoRepository.findByRelator(relator);
    }

    public Processo buscarPorId(Long id) {
        return processoRepository.findById(id).orElse(null);
    }

    public List<Processo> listarPorInteressado(Aluno aluno, String statusFiltro) {
        // Se tem status e não ta vazio, filtra
        if (statusFiltro != null && !statusFiltro.isEmpty()) {
            return processoRepository.findByInteressadoAndStatus(aluno, statusFiltro);
        }
        // Se não tem filtro, retorna todos desse aluno
        return processoRepository.findByInteressado(aluno);
    }

    public List<Processo> listarComFiltros(String status, Long alunoId, Long relatorId) {
        return processoRepository.findByFiltros(status, alunoId, relatorId);
    }

    // Logica pra criar um processo
    public Processo salvar(Processo processo) {
        // Se for um processo novo (que nao tem ID), define data e status inicial
        if (processo.getId() == null) {
            processo.setDataRecepcao(LocalDate.now());
            processo.setStatus("CRIADO");
        }
        return processoRepository.save(processo);
    }

    // Lógica pra distribuir processo (REQFUNC 8)
    public void distribuirProcesso(Long idProcesso, Long idRelator) {
        Processo processo = this.buscarPorId(idProcesso);
        Professor relator = professorRepository.findById(idRelator).orElse(null);

        if (processo != null && relator != null) {
            processo.setRelator(relator);
            processo.setDataDistribuicao(LocalDate.now());
            processo.setStatus("DISTRIBUIDO");
            processoRepository.save(processo);
        }
    }
}