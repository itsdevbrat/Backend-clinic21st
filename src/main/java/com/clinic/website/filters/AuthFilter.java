package com.clinic.website.filters;

import com.clinic.website.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Configuration
public class AuthFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getMethod().equals(HttpMethod.OPTIONS) || exchange.getRequest().getPath().toString().equals("/auth/login"))
            return chain.filter(exchange);
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (hasText(token) && !jwtUtil.isTokenExpired(token)) {
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().header("email", jwtUtil.getEmailFromToken(token)).build();
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return chain.filter(mutatedExchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
