package br.edu.ifpb.pweb2.delibera_consilium.service;

import br.edu.ifpb.pweb2.delibera_consilium.model.Assunto;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AssuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;

    public List<Assunto> listarTodos() {
        return assuntoRepository.findAll();
    }

    public Assunto buscarPorId(Long id) {
        return assuntoRepository.findById(id).orElse(null);
    }

    public Assunto salvar(Assunto assunto) {
        return assuntoRepository.save(assunto);
    }

    public void excluir(Long id) {
        assuntoRepository.deleteById(id);
    }
}