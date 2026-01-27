package br.edu.ifpb.pweb2.delibera_consilium.service;

import br.edu.ifpb.pweb2.delibera_consilium.model.Reuniao;
import br.edu.ifpb.pweb2.delibera_consilium.model.StatusReuniao;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ReuniaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReuniaoService {

    @Autowired
    private ReuniaoRepository reuniaoRepository;

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

    public Reuniao buscarPorId(Long id) {
        return reuniaoRepository.findById(id).orElse(null);
    }

    public Reuniao salvar(Reuniao reuniao) {
        return reuniaoRepository.save(reuniao);
    }
}
