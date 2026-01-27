package br.edu.ifpb.pweb2.delibera_consilium.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/images/**", "/imagens/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/coord/**").hasRole("COORDENADOR")
                .requestMatchers("/professor/**").hasAnyRole("PROFESSOR", "COORDENADOR")
                .requestMatchers("/aluno/**").hasRole("ALUNO")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/auth/acesso-negado")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Usuários de teste - criados na 1ª execução da aplicação
        UserDetails aluno = User.withUsername("aluno")
            .password(passwordEncoder().encode("123456"))
            .roles("ALUNO")
            .build();

        UserDetails professor = User.withUsername("professor")
            .password(passwordEncoder().encode("123456"))
            .roles("PROFESSOR")
            .build();

        UserDetails coordenador = User.withUsername("coordenador")
            .password(passwordEncoder().encode("123456"))
            .roles("COORDENADOR", "PROFESSOR")
            .build();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("123456"))
            .roles("ADMIN")
            .build();

        // Gerenciador de usuários via JDBC (tabelas padrão do Spring Security)
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        // Evita duplicação dos usuários no banco
        if (!users.userExists(admin.getUsername())) {
            users.createUser(aluno);
            users.createUser(professor);
            users.createUser(coordenador);
            users.createUser(admin);
        }

        return users;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
