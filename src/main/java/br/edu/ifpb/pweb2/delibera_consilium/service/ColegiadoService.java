package br.edu.ifpb.pweb2.delibera_consilium.service;

import br.edu.ifpb.pweb2.delibera_consilium.model.Colegiado;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ColegiadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColegiadoService {

    @Autowired
    private ColegiadoRepository colegiadoRepository;

    public List<Colegiado> listarTodos() {
        return colegiadoRepository.findAll();
    }

    public Colegiado salvar(Colegiado colegiado) {
        return colegiadoRepository.save(colegiado);
    }

    public Colegiado buscarPorId(Long id) {
        return colegiadoRepository.findById(id).orElse(null);
    }
    
    public void excluir(Long id) {
        colegiadoRepository.deleteById(id);
    }
}