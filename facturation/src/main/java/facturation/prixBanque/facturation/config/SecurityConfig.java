package facturation.prixBanque.facturation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/accounts/**", "/factures/create").permitAll() // Autoriser l'accès public à ces endpoints
                        .requestMatchers("/factures/**").authenticated() // Protéger les endpoints de factures
                        .anyRequest().authenticated() // Exiger une authentification pour toutes les autres requêtes
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Politique de session : sans état (JWT)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Ajouter le filtre JWT avant le filtre d'authentification
                .build();
    }

}
