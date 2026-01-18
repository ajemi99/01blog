package com.ajemi.backend.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
       @NonNull HttpServletRequest request,
       @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);

        if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);
                System.out.println("Authenticated user: " + userDetails.getUsername());
                // 1️⃣ Cast l-userDetails l l-implémentation dyalk bach n-jebdo l-ID mn l-DB
                UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                
                // 2️⃣ Jbed l-ID li m-khbi wast l-Token (khass t-koun zedtih f l-claims mlli katcharger l-token)
                Long tokenId = jwtService.extractClaim(jwt, claims -> claims.get("userId", Long.class));
            if (jwtService.isTokenValid(jwt, userDetails.getUsername()) && userDetails.isAccountNonLocked()&&
                    tokenId != null && tokenId.equals(userDetailsImpl.getId())) {

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                    .setAuthentication(authToken);
            }else {
                // Ila wa7ed f hado ghalat, SecurityContext kiy-bqa khawi -> Error 403 automatiqument
                 System.out.println("Invalid Token or User ID mismatch!");
            }
        }

        filterChain.doFilter(request, response);
    }
}

