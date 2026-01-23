package com.ajemi.backend.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        
        try {
            String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 🚩 Hna fin kadd-tra UsernameNotFoundException ila l-user msa7ti account-ou
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                Long tokenId = jwtService.extractClaim(jwt, claims -> claims.get("userId", Long.class));
                if(!userDetails.isAccountNonLocked()){
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\": \"Your account is banned\"}");
                    return;
                }
                if (jwtService.isTokenValid(jwt, userDetails.getUsername()) && 
                    userDetails.isAccountNonLocked() &&
                    tokenId != null && tokenId.equals(userDetailsImpl.getId())) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    System.out.println("Authenticated user: " + username);
                } else {
                    System.out.println("Invalid Token or User ID mismatch!");
                }
            }
         } catch (UsernameNotFoundException e) {
            // 🚩 Hna ghadi n-sifto JSON nqi l l-Frontend fih l-message s-s7i7
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"User not found or deleted\"}");
            return; // ⛔ 7bess l-filter chain hna
        } 
            filterChain.doFilter(request, response);
        
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || path.startsWith("/uploads/");
    }
}

// @Component
// @RequiredArgsConstructor
// public class JwtAuthFilter extends OncePerRequestFilter {

//     private final JwtService jwtService;
//     private final CustomUserDetailsService userDetailsService;

//     @Override
//     protected void doFilterInternal(
//        @NonNull HttpServletRequest request,
//        @NonNull HttpServletResponse response,
//         @NonNull FilterChain filterChain
//     ) throws ServletException, IOException {

//         String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         String jwt = authHeader.substring(7);
//         String username = jwtService.extractUsername(jwt);

//         if (username != null &&
//             SecurityContextHolder.getContext().getAuthentication() == null) {

//             UserDetails userDetails =
//                 userDetailsService.loadUserByUsername(username);
//                 System.out.println("Authenticated user: " + userDetails.getUsername());
//                 // 1️⃣ Cast l-userDetails l l-implémentation dyalk bach n-jebdo l-ID mn l-DB
//                 UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                
//                 // 2️⃣ Jbed l-ID li m-khbi wast l-Token (khass t-koun zedtih f l-claims mlli katcharger l-token)
//                 Long tokenId = jwtService.extractClaim(jwt, claims -> claims.get("userId", Long.class));
//             if (jwtService.isTokenValid(jwt, userDetails.getUsername()) && userDetails.isAccountNonLocked()&&
//                     tokenId != null && tokenId.equals(userDetailsImpl.getId())) {

//                 UsernamePasswordAuthenticationToken authToken =
//                     new UsernamePasswordAuthenticationToken(
//                         userDetails,
//                         null,
//                         userDetails.getAuthorities()
//                     );

//                 authToken.setDetails(
//                     new WebAuthenticationDetailsSource()
//                         .buildDetails(request)
//                 );

//                 SecurityContextHolder.getContext()
//                     .setAuthentication(authToken);
//             }else {
//                 // Ila wa7ed f hado ghalat, SecurityContext kiy-bqa khawi -> Error 403 automatiqument
//                  System.out.println("Invalid Token or User ID mismatch!");
//             }
//         }

//         filterChain.doFilter(request, response);
//     }
//         @Override
//     protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//         String path = request.getServletPath();
//         System.err.println(path);
//         // 🚩 Goul l-Spring mat-khdemch had l-Filter f had l-blayss
//         return path.startsWith("/api/auth/") || path.startsWith("/uploads/");
//     }
// }

