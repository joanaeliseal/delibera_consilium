package br.edu.ifpb.pweb2.delibera_consilium.config;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AlunoRepository alunoRepository, 
                           ProfessorRepository professorRepository, 
                           PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existem usuários para não duplicar toda vez que o sistema subir
        if (alunoRepository.count() == 0 && professorRepository.count() == 0) {
            
            // Criar Aluno
            Aluno aluno = new Aluno();
            aluno.setNome("Aluno de Teste");
            aluno.setLogin("aluno");
            aluno.setSenha(passwordEncoder.encode("123"));
            aluno.setMatricula("20240001");
            alunoRepository.save(aluno);

            // Criar Coordenador
            Professor coord = new Professor();
            coord.setNome("Professor Coordenador");
            coord.setLogin("coord");
            coord.setSenha(passwordEncoder.encode("123"));
            coord.setMatricula("11111111");
            professorRepository.save(coord);

            System.out.println("✅ Banco de dados inicializado com sucesso.");
        } else {
            System.out.println("ℹ️ Dados de teste já existem. Pulando inicialização.");
        }
    }
}