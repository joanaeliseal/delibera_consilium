package br.edu.ifpb.pweb2.delibera_consilium.service;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.TipoVoto;
import br.edu.ifpb.pweb2.delibera_consilium.model.Voto;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProcessoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true) // ADICIONADO: Resolve erro de LOB ao ler processos
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private VotoRepository votoRepository;

    public List<Processo> listarTodos() {
        return processoRepository.findAll();
    }
    
    // filtro pro professor ver os dele (REQFUNC 3)
    public List<Processo> listarPorRelator(Professor relator) {
        return processoRepository.findByRelator(relator);
    }

    public Processo buscarPorId(Long id) {
        return processoRepository.findById(id).orElse(null);
    }

    public List<Processo> listarPorInteressado(Aluno aluno, String status, Long assuntoId, String ordem) {
        if (status != null && status.isEmpty()) status = null;

        Sort sort = Sort.by("dataRecepcao").descending(); 
        if ("asc".equals(ordem)) {
            sort = Sort.by("dataRecepcao").ascending();
        }

        return processoRepository.findByAlunoFiltros(aluno, status, assuntoId, sort);
    }

    public List<Processo> listarComFiltros(String status, Long alunoId, Long relatorId) {

        if (status != null && status.isEmpty()) {
            status = null;
        }
        
        return processoRepository.findByFiltros(status, alunoId, relatorId);
    }

    // logica pra criar um processo
    @Transactional // ADICIONADO: Necessário para operações de escrita
    public Processo salvar(Processo processo) {
        // se for um processo novo (com id nulo)
        if (processo.getId() == null) {
            processo.setDataRecepcao(LocalDate.now());
            processo.setStatus("CRIADO");
            
            // gerador de protocolo (gera algo como 20250520-1A2B)
            String ano = String.valueOf(LocalDate.now().getYear());
            String codigoAleatorio = java.util.UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            processo.setNumero(ano + "-" + codigoAleatorio);
        }
        return processoRepository.save(processo);
    }

    public Page<Processo> listarProcessosPaginados(Pageable pageable) {
        return processoRepository.findAll(pageable);
    }

    public Page<Processo> listarComFiltrosPaginado(String status, Long alunoId, Long relatorId, Pageable pageable) {
        if (status != null && status.isEmpty()) status = null;
        return processoRepository.findByFiltrosPaginado(status, alunoId, relatorId, pageable);
    }

    // logica pra distribuir processo (REQFUNC 8)
    @Transactional // ADICIONADO: Necessário para operações de escrita
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

    /**
     * Conta os votos de um processo (REQFUNC 11)
     * @return Map com contagem: {"COM_RELATOR": x, "DIVERGENTE": y, "AUSENTES": z}
     */
    public Map<String, Long> contarVotos(Long processoId) {
        List<Voto> votos = votoRepository.findByProcessoId(processoId);

        long comRelator = votos.stream()
                .filter(v -> !v.isAusente() && v.getVoto() == TipoVoto.COM_RELATOR)
                .count();

        long divergente = votos.stream()
                .filter(v -> !v.isAusente() && v.getVoto() == TipoVoto.DIVERGENTE)
                .count();

        long ausentes = votos.stream()
                .filter(Voto::isAusente)
                .count();

        Map<String, Long> resultado = new HashMap<>();
        resultado.put("COM_RELATOR", comRelator);
        resultado.put("DIVERGENTE", divergente);
        resultado.put("AUSENTES", ausentes);
        resultado.put("TOTAL", (long) votos.size());

        return resultado;
    }
}