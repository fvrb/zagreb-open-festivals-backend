package com.zagrebopenfestivals.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityProblemDetailWriter problemDetailWriter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // --- Auth (javno) ---
                        .requestMatchers("/auth/**").permitAll()

                        // --- Festivali (čitanje javno, pisanje samo ADMIN) ---
                        .requestMatchers(HttpMethod.GET, "/festivals").permitAll()
                        .requestMatchers(HttpMethod.GET, "/festivals/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/festivals").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/festivals/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/festivals/*").hasRole("ADMIN")

                        // --- Hrana (čitanje javno, pisanje samo ADMIN) ---
                        .requestMatchers(HttpMethod.GET, "/festivals/*/foods").permitAll()
                        .requestMatchers(HttpMethod.POST, "/festivals/*/foods").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/foods/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/foods/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/festivals/*/drinks").permitAll()
                        .requestMatchers(HttpMethod.POST, "/festivals/*/drinks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/drinks/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/drinks/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/users/me").authenticated()

                        // --- Favoriti (samo prijavljeni USER) ---
                        .requestMatchers("/favorites/**").hasAnyRole("USER", "ADMIN")

                        // TODO (studentski zadatak): Piće (Drink) trenutno nema definirana
                        // pravila pristupa ovdje - endpointi /festivals/*/drinks, /drinks/**
                        // trenutno padaju na ".anyRequest().authenticated()" niže, što znači
                        // da bilo koji prijavljeni korisnik (i USER, ne samo ADMIN) može
                        // trenutno raditi POST/PUT/DELETE na piću. Dodajte pravila analogno
                        // gornjem bloku za Food (GET javno, write samo ADMIN) kad implementirate
                        // DrinkController/DrinkService.

                        // TODO (studentski zadatak): Favoriti trenutno nemaju definirana
                        // pravila pristupa ovdje - /favorites/** padaju na
                        // ".anyRequest().authenticated()" niže. Kad implementirate
                        // FavoriteController/FavoriteService, dodajte pravilo da su ti
                        // endpointi dostupni samo prijavljenom USER-u (hasRole("USER")).

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, e) ->
                                problemDetailWriter.write(response, HttpStatus.UNAUTHORIZED, "Potrebna je autentifikacija"))
                        .accessDeniedHandler((request, response, e) ->
                                problemDetailWriter.write(response, HttpStatus.FORBIDDEN, "Pristup odbijen"))
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
