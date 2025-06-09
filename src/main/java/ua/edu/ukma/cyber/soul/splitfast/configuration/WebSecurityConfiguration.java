package ua.edu.ukma.cyber.soul.splitfast.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ExceptionTranslator;
import ua.edu.ukma.cyber.soul.splitfast.security.JWTAuthenticationFilter;
import ua.edu.ukma.cyber.soul.splitfast.security.JWTAuthenticationProvider;
import ua.edu.ukma.cyber.soul.splitfast.security.JWTService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ExceptionTranslator exceptionTranslator, AuthenticationManager authenticationManager, ObjectMapper objectMapper) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterAfter(new JWTAuthenticationFilter(authenticationManager), LogoutFilter.class)
            .exceptionHandling(e -> e
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                        response.getWriter().write(objectMapper.writeValueAsString(exceptionTranslator.translate(authException)));
                    })
            )
            .authorizeHttpRequests(r -> r
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs*/**").permitAll()
                    .requestMatchers("/register/user", "/auth/login", "/auth/reset-token").permitAll()
                    .anyRequest().authenticated()
            )
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(JWTService jwtService, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        JWTAuthenticationProvider jwtAuthenticationProvider = new JWTAuthenticationProvider(jwtService);
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(List.of(jwtAuthenticationProvider, daoAuthenticationProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL);
        corsConfiguration.setAllowedHeaders(
                List.of(
                        HttpHeaders.AUTHORIZATION, HttpHeaders.USER_AGENT, HttpHeaders.ORIGIN,
                        HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_LANGUAGE,
                        HttpHeaders.CACHE_CONTROL, HttpHeaders.IF_MODIFIED_SINCE, HttpHeaders.LAST_MODIFIED
                )
        );
        corsConfiguration.setAllowedMethods(
                List.of(
                        HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name()
                )
        );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
