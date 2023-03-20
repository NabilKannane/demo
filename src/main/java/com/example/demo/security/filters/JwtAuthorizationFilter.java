package com.example.demo.security.filters;

import com.example.demo.security.jwt.JwtConfig;
import com.example.demo.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final JwtConfig jwtConfig;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    String authorizationToken = request.getHeader(this.jwtConfig.getAuthorizationHeader());

    if (authorizationToken != null && authorizationToken.startsWith(this.jwtConfig.getTokenPrefix())) {
      try {
        String jwt = authorizationToken.substring(this.jwtConfig.getTokenPrefix().length());
        String username = this.jwtUtil.extractUsername(jwt);
        Collection<GrantedAuthority> authorities = this.jwtUtil.extractRoles(jwt);
        UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
      } catch (BadCredentialsException | UsernameNotFoundException  e) {
        log.error("",e.getCause());
        Map<String, Object> idToken = new HashMap<>();
        idToken.put("message", "Vous ne pouvez pas acceder au  " + request.getRequestURL().toString());
        idToken.put("status", HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
      }
    } else {
      filterChain.doFilter(request, response);
    }

  }

}
