package com.sparta.logistics.infrastructure.security;

import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.presentation.common.constant.HeaderConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class GatewayHeaderAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String userIdHeader = request.getHeader(HeaderConstants.USER_ID);
    String roleHeader = request.getHeader(HeaderConstants.USER_ROLE);

    // 헤더가 없는 경우
    if (userIdHeader == null || roleHeader == null) {
      filterChain.doFilter(request, response);
      return;
    }

    UUID userId = UUID.fromString(userIdHeader);
    Role role = Role.valueOf(roleHeader);

    GatewayUserPrincipal userPrincipal =
        new GatewayUserPrincipal(userId, role);

    SimpleGrantedAuthority simpleGrantedAuthority =
        new SimpleGrantedAuthority("ROLE_" + role.name());

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            List.of(simpleGrantedAuthority)
    );

    SecurityContextHolder.getContext()
        .setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
