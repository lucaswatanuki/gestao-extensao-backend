package com.ftunicamp.tcc.security.jwt;

import com.ftunicamp.tcc.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;


public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        autenticar(request);

        filterChain.doFilter(request, response);
    }

    private void autenticar(HttpServletRequest request) {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                var sessao = jwtUtils.obterSessao(jwt);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        request.getUserPrincipal(), null, sessao.getProfiles().stream()
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Não foi possível autenticar usuario: {}", e.getMessage());
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("jwt");

        if (StringUtils.hasText(headerAuth)) {
            return headerAuth;
        }

        return null;
    }
}
