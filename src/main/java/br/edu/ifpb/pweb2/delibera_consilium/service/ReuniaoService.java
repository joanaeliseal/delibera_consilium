package br.edu.ifpb.pweb2.delibera_consilium.service;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Reuniao;
import br.edu.ifpb.pweb2.delibera_consilium.model.StatusReuniao;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProcessoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ReuniaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReuniaoService {

    @Autowired
    private ReuniaoRepository reuniaoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    public List<Reuniao> listarTodas() {
        return reuniaoRepository.findAllByOrderByDataReuniaoDesc();
    }

    public List<Reuniao> listarPorStatus(StatusReuniao status) {
        return reuniaoRepository.findByStatus(status);
    }

    public List<Reuniao> listarComFiltro(StatusReuniao status) {
        if (status == null) {
            return listarTodas();
        }
        return listarPorStatus(status);
    }

    public List<Reuniao> listarComFiltros(StatusReuniao status, Long colegiadoId) {
        return reuniaoRepository.findByFiltros(status, colegiadoId);
    }

    public List<Reuniao> listarPorMembro(Professor professor, StatusReuniao status) {
        return reuniaoRepository.findByMembro(professor, status);
    }

    public Reuniao buscarPorId(Long id) {
        return reuniaoRepository.findById(id).orElse(null);
    }

    public Reuniao salvar(Reuniao reuniao) {
        return reuniaoRepository.save(reuniao);
    }

    /**
     * Cria uma nova sessao com status PROGRAMADA
     */
    @Transactional
    public Reuniao criarSessao(Reuniao reuniao) {
        reuniao.setStatus(StatusReuniao.PROGRAMADA);
        return reuniaoRepository.save(reuniao);
    }

    /**
     * Inicia uma sessao, mudando o status para EM_ANDAMENTO
     */
    @Transactional
    public Reuniao iniciarSessao(Long reuniaoId) {
        Reuniao reuniao = buscarPorId(reuniaoId);
        if (reuniao != null && reuniao.getStatus() == StatusReuniao.PROGRAMADA) {
            if (reuniaoRepository.existsByStatus(StatusReuniao.EM_ANDAMENTO)) {
                throw new RuntimeException("Já existe uma sessão em andamento");
            }
            reuniao.setStatus(StatusReuniao.EM_ANDAMENTO);
            reuniao.setDataHoraInicio(LocalDateTime.now());
            return reuniaoRepository.save(reuniao);
        }
        return reuniao;
    }

    /**
     * Finaliza uma sessao, mudando o status para ENCERRADA
     */
    @Transactional
    public Reuniao finalizarSessao(Long reuniaoId) {
        Reuniao reuniao = buscarPorId(reuniaoId);
        if (reuniao != null && reuniao.getStatus() == StatusReuniao.EM_ANDAMENTO) {
            reuniao.setStatus(StatusReuniao.ENCERRADA);
            reuniao.setDataHoraFim(LocalDateTime.now());
            return reuniaoRepository.save(reuniao);
        }
        return reuniao;
    }

    /**
     * Adiciona um processo a pauta da reuniao
     */
    @Transactional
    public void adicionarProcessoAPauta(Long reuniaoId, Long processoId) {
        Reuniao reuniao = buscarPorId(reuniaoId);
        Processo processo = processoRepository.findById(processoId).orElse(null);

        if (reuniao != null && processo != null) {
            processo.setReuniao(reuniao);
            processoRepository.save(processo);
        }
    }

    /**
     * Remove um processo da pauta da reuniao
     */
    @Transactional
    public void removerProcessoDaPauta(Long reuniaoId, Long processoId) {
        Processo processo = processoRepository.findById(processoId).orElse(null);

        if (processo != null && processo.getReuniao() != null
                && processo.getReuniao().getId().equals(reuniaoId)) {
            processo.setReuniao(null);
            processoRepository.save(processo);
        }
    }

    /**
     * Lista processos disponiveis para adicionar a pauta
     * (processos distribuidos que ainda nao estao em nenhuma reuniao)
     */
    public List<Processo> listarProcessosDisponiveis() {
        return processoRepository.findByStatusAndReuniaoIsNull("DISTRIBUIDO");
    }

    /**
     * Exclui uma reuniao (apenas se estiver PROGRAMADA e sem processos)
     */
    @Transactional
    public void excluir(Long id) {
        Reuniao reuniao = buscarPorId(id);
        if (reuniao != null && reuniao.getStatus() == StatusReuniao.PROGRAMADA) {
            // Remove processos da pauta antes de excluir
            if (reuniao.getProcessosEmPauta() != null) {
                for (Processo p : reuniao.getProcessosEmPauta()) {
                    p.setReuniao(null);
                    processoRepository.save(p);
                }
            }
            reuniaoRepository.delete(reuniao);
        }
    }
}
