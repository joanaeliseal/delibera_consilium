package br.edu.ifpb.pweb2.delibera_consilium.config;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Colegiado;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ColegiadoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final ColegiadoRepository colegiadoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AlunoRepository alunoRepository, 
                           ProfessorRepository professorRepository, 
                           ColegiadoRepository colegiadoRepository,
                           PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.colegiadoRepository = colegiadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // Só executa se o banco estiver realmente vazio
        if (professorRepository.count() == 0) {
            
            // 1. O ADMIN (Para gerenciar cadastros)
            Professor admin = new Professor();
            admin.setNome("Administrador do Sistema");
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin"));
            admin.setMatricula("00000000");
            professorRepository.save(admin);

            // 2. O COORDENADOR
            Professor coord = new Professor();
            coord.setNome("Professor Coordenador");
            coord.setLogin("coord");
            coord.setSenha(passwordEncoder.encode("123"));
            coord.setMatricula("11111111");
            coord.setCoordenador(true); // <--- ADICIONE ESTA LINHA
            coord = professorRepository.save(coord);

            // Criando o Colegiado e vinculando o Coordenador
            Colegiado colegiado = new Colegiado();
            colegiado.setDescricao("Colegiado de TSI");
            colegiado.setCurso("Sistemas para Internet");
            colegiado.setPortaria("Portaria 2026/IFPB");
            colegiado.setDataInicio(LocalDate.now());
            colegiado.setDataFim(LocalDate.now().plusYears(2));
            colegiado.setCoordenador(coord);
            colegiado.setMembros(new ArrayList<>());
            colegiado.getMembros().add(coord);
            colegiadoRepository.save(colegiado);

            // 3. O PROFESSOR RELATOR (Professor comum)
            Professor prof = new Professor();
            prof.setNome("Professor Relator");
            prof.setLogin("professor");
            prof.setSenha(passwordEncoder.encode("123"));
            prof.setMatricula("22222222");
            professorRepository.save(prof);

            // 4. O ALUNO
            if (alunoRepository.count() == 0) {
                Aluno aluno = new Aluno();
                aluno.setNome("Felipe Aluno");
                aluno.setLogin("aluno");
                aluno.setSenha(passwordEncoder.encode("123"));
                aluno.setMatricula("20240001");
                alunoRepository.save(aluno);
            }

            System.out.println("✅ Todos os perfis foram criados e o Colegiado foi configurado!");
        }
    }
}