package com.clinic.website.filters;

import com.clinic.website.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .ifPresentOrElse(token -> {
                    if (token.isEmpty() || jwtUtil.isTokenExpired(token))
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    else
                        exchange.getRequest().getHeaders().add("email", jwtUtil.getEmailFromToken(token));

                }, () -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
        return chain.filter(exchange);
    }
}
