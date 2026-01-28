package br.edu.ifpb.pweb2.delibera_consilium.config;

import br.edu.ifpb.pweb2.delibera_consilium.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Configura o encoder de senha usando BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura o provider de autenticação usando nosso UserDetailsService customizado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Configuração de segurança HTTP
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos (CSS, JS, imagens, página de login)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/login", "/error").permitAll()
                
                // Rotas administrativas - apenas ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Rotas do coordenador - apenas COORDENADOR
                .requestMatchers("/coord/**").hasRole("COORDENADOR")
                
                // Rotas do professor - PROFESSOR ou COORDENADOR
                .requestMatchers("/professor/**").hasAnyRole("PROFESSOR", "COORDENADOR")
                
                // Rotas do aluno - apenas ALUNO
                .requestMatchers("/aluno/**").hasRole("ALUNO")
                
                // Qualquer outra requisição precisa estar autenticada
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // CSRF habilitado (mais seguro)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // Se usar H2 console
            )
            // Permite frames do mesmo domínio (para H2 console se necessário)
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}