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

    /**
     * Define o algoritmo de criptografia. 
     * O Spring Security usará este bean automaticamente para validar as senhas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuração de segurança HTTP.
     * O Spring encontrará automaticamente seu CustomUserDetailsService por ser um @Service.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos e páginas públicas
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/login", "/error", "/acesso-negado").permitAll()
                
                // Regras de Autorização baseadas em ROLE
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/coord/**").hasRole("COORDENADOR")
                .requestMatchers("/professor/**").hasAnyRole("PROFESSOR", "COORDENADOR")
                .requestMatchers("/aluno/**").hasRole("ALUNO")
                
                // Qualquer outra página exige apenas estar logado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(handling -> handling
                .accessDeniedPage("/acesso-negado")
            )
            // Mantém proteção CSRF (padrão de segurança)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") 
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}