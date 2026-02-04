package br.edu.ifpb.pweb2.delibera_consilium;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Colegiado;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ColegiadoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
public class DeliberaConsiliumApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliberaConsiliumApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AlunoRepository alunoRepository, 
                          ProfessorRepository professorRepository, 
                          ColegiadoRepository colegiadoRepository,
                          PasswordEncoder passwordEncoder) {
        return args -> {

            // 1. CRIAR ALUNO DE TESTE
            if (alunoRepository.findByLogin("aluno") == null) {
                Aluno aluno = new Aluno();
                aluno.setNome("Aluno de Teste");
                aluno.setLogin("aluno");
                aluno.setSenha(passwordEncoder.encode("123"));
                aluno.setMatricula("20240001");
                alunoRepository.save(aluno);
                System.out.println("✅ Usuário ALUNO criado");
            }

            // 2. CRIAR PROFESSOR COORDENADOR
            Professor coordenador = professorRepository.findByLogin("coord");
            if (coordenador == null) {
                coordenador = new Professor();
                coordenador.setNome("Professor Coordenador");
                coordenador.setLogin("coord");
                coordenador.setSenha(passwordEncoder.encode("123"));
                coordenador.setMatricula("11111111"); 
                coordenador = professorRepository.save(coordenador);
                System.out.println("✅ Usuário COORDENADOR criado");
            }

            // VINCULAR COORDENADOR A UM COLEGIADO
            if (colegiadoRepository.findByCoordenador(coordenador).isEmpty()) {
                Colegiado colegiado = new Colegiado();
                colegiado.setDescricao("Colegiado de TSI");
                colegiado.setCurso("Sistemas para Internet"); // <--- ADICIONADO PARA CORRIGIR O ERRO
                colegiado.setPortaria("Portaria 2024/IFPB");
                colegiado.setDataInicio(LocalDate.now());
                colegiado.setDataFim(LocalDate.now().plusYears(2));
                colegiado.setCoordenador(coordenador);
                
                colegiado.setMembros(new ArrayList<>());
                colegiado.getMembros().add(coordenador);
                
                colegiadoRepository.save(colegiado);
                System.out.println("✅ Colegiado criado e vinculado");
            }

            // 3. CRIAR PROFESSOR RELATOR
            if (professorRepository.findByLogin("professor") == null) {
                Professor prof = new Professor();
                prof.setNome("Professor Relator");
                prof.setLogin("professor");
                prof.setSenha(passwordEncoder.encode("123"));
                prof.setMatricula("22222222");
                professorRepository.save(prof);
                System.out.println("✅ Usuário PROFESSOR criado");
            }

            // 4. CRIAR ADMINISTRADOR
            if (professorRepository.findByLogin("admin") == null) {
                Professor admin = new Professor();
                admin.setNome("Administrador");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin"));
                admin.setMatricula("00000000");
                professorRepository.save(admin);
                System.out.println("✅ Usuário ADMIN criado");
            }
        };
    }
}