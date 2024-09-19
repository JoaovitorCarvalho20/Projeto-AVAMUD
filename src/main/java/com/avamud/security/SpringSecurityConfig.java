package com.avamud.security;

import com.avamud.security.jwt.AuthEntrypointJwt;
import com.avamud.security.jwt.AuthFilterToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Habilita a segurança por métodos, permitindo uso de anotações como @PreAuthorize em métodos específicos
public class SpringSecurityConfig {

    // Injeção de dependência da classe AuthEntrypointJwt, que lida com erros de autenticação
    @Autowired
    private AuthEntrypointJwt authEntrypointJwt;

    // Bean que define o algoritmo de codificação de senha como BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthFilterToken authFilterToken(){
        return new AuthFilterToken();
    } ;

    // Configura o AuthenticationManager, que gerencia o processo de autenticação dos usuários
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Define a cadeia de filtros de segurança para a aplicação
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntrypointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users").permitAll()
                        .requestMatchers("/payments/**").authenticated() // Permitir apenas para autenticados
                        .anyRequest().authenticated());

        http.addFilterBefore(authFilterToken(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
