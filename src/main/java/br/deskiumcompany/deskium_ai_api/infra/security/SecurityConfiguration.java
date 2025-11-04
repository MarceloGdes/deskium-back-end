package br.deskiumcompany.deskium_ai_api.infra.security;

import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity //Habilitando a configuração do web security
public class SecurityConfiguration {
    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                // 1. Desabilita proteção CSRF
                .csrf(csrf -> csrf.disable())

                // define que a authentificação será Stateless ou seja, por token JWT, padrão REST.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //definindo regra de roles onde cada usuário podeira acessar ou não
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/arquivos/{fileName}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/empresas").hasAnyRole(TipoUsuario.SUPORTE.name())
                        .requestMatchers(HttpMethod.GET, "/solicitantes/me").hasAnyRole(TipoUsuario.SOLICITANTE.name())
                        .requestMatchers(HttpMethod.POST, "/tickets").hasAnyRole(TipoUsuario.SOLICITANTE.name())
                        .anyRequest().authenticated()
                )
                //Tirando bloqueio de CORS - Apenas para dev, em prod deve ser configurado
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("*"));
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));

                    return corsConfig;
                }))
                //Filtro antes da validação das ROLES
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Gerencia autenticação - responsável por validar credenciais (email e senha)
    // Usado quando o usuário faz login;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 2. PasswordEncoder - Encripta e valida senhas
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
