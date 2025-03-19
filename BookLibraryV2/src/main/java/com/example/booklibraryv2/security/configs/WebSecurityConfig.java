package com.example.booklibraryv2.security.configs;

import com.example.booklibraryv2.security.entitites.Role;
import com.example.booklibraryv2.security.jwt.JwtAuthenticationFilter;
import com.example.booklibraryv2.security.services.CustomUserDetailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomUserDetailService userDetailsService;


  @Bean
  public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    http.formLogin(AbstractHttpConfigurer::disable);

    http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(request ->
        request
            .requestMatchers("/")
            .permitAll()
            .requestMatchers("/v3/api-docs/**")
            .permitAll()
            .requestMatchers("/swagger-ui/**")
            .permitAll()
            .requestMatchers("/error", BaseEndpoint.AUTH_V1.getEndpoint() + "/login")
            .permitAll()
            .requestMatchers(BaseEndpoint.AUTH_V1.getEndpoint() + "/refresh-tokens")
            .authenticated()
            .requestMatchers(HttpMethod.POST, BaseEndpoint.BOOKS_V1.getEndpoint() + "/add")
            .hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.PATCH, BaseEndpoint.BOOKS_V1.getEndpoint() + "/update")
            .hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE, BaseEndpoint.BOOKS_V1.getEndpoint() + "/delete")
            .hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.POST, BaseEndpoint.LIBRARY_USERS_V1.getEndpoint() + "/add")
            .hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.PATCH, BaseEndpoint.LIBRARY_USERS_V1.getEndpoint() + "/update")
            .hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.DELETE, BaseEndpoint.LIBRARY_USERS_V1.getEndpoint() + "/delete")
            .hasRole(Role.ADMIN.name())
            .anyRequest().hasAnyRole(Role.ADMIN.name(), Role.USER.name()));

    return http.build();
  }

  @RequiredArgsConstructor
  @Getter
  private enum BaseEndpoint {
    BOOKS_V1("/api/v1/books"),
    LIBRARY_USERS_V1("/api/v1/libraryUsers"),
    AUTH_V1("/api/v1/auth");

    private final String endpoint;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

    builder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());

    return builder.build();
  }
}
