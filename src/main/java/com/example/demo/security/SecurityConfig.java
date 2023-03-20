package com.example.demo.security;


import com.example.demo.security.filters.JwtAuthorizationFilter;
import com.example.demo.services.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final ApplicationUserService userDetailsService;
  private final JwtAuthorizationFilter jwtAuthorizationFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();// disactiver security statfull (SESSION ID)
    http.headers().frameOptions().disable();// poretection vers les frames
    http.cors().and()
      .authorizeRequests()
      .antMatchers("/api/login",
        "/api/user/signup")
      .permitAll();// permet URL /signup
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// ACTIVER MODE AUTH STATELESS
    http.authorizeRequests().anyRequest().authenticated();// forcer authentification pour les autres url
    http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);// ajouter filtre jwt
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
    configuration.addAllowedHeader("*");
    configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
