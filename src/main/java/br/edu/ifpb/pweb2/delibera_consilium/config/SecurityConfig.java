package br.edu.ifpb.pweb2.delibera_consilium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

// configuração de segurança da aplicação

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // recursos públicos
                .requestMatchers("/css/**", "/images/**", "/login", "/error").permitAll()

                .requestMatchers("/admin/**").hasRole("ADMIN") // recursos restritos a administradores

                .requestMatchers("/coord/**").hasAnyRole("COORDENADOR") // recursos restritos a coordenadores

                .requestMatchers("/professor/**").hasAnyRole("PROFESSOR", "COORDENADOR") // recursos restritos a professores

                .requestMatchers("/aluno/**").hasRole("ALUNO") // recursos restritos a alunos

                .requestMatchers("/", "/home").authenticated() // recursos acessíveis a todos os usuários autenticados
            
                .anyRequest().authenticated() // todas as outras requisições exigem autenticação

            )
        
            .formLogin(form -> form
                .loginPage("/login") // URL para login
                .loginProcessingUrl("/perform_login") // URL para submissão do formulário de login
                .defaultSuccessUrl("/home", true) // redireciona para /home após login bem-sucedido
                .failureUrl("/login?error=true") // redireciona para /login com erro em caso de falha
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL para logout
                .logoutSuccessUrl("/login?logout=true") // redireciona para /login após logout
                .invalidateHttpSession(true)  // Invalida sessão
                .deleteCookies("JSESSIONID")  // Remove cookie de sessão
                .permitAll()
            )

            .sessionManagement(session -> session
                .maximumSessions(1)  // Apenas 1 sessão por usuário
                .expiredUrl("/login?expired=true")
            );

        return http.build();
    }
}
