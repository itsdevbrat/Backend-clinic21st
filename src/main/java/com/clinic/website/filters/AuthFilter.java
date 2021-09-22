package com.clinic.website.filters;

import com.clinic.website.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Configuration
@Order(2)
public class AuthFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (Objects.equals(exchange.getRequest().getMethod(), HttpMethod.OPTIONS)
                || exchange.getRequest().getPath().toString().equals("/admin/login")
                || exchange.getRequest().getPath().toString().equals("/user/login")
                || exchange.getRequest().getPath().toString().matches("/swagger-ui.*"))
            return chain.filter(exchange);


        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        try {
            if (hasText(token)
                    && jwtUtil.getEmailFromToken(token) != null
                    && !jwtUtil.getEmailFromToken(token).isEmpty()) {

                if ((exchange.getRequest().getPath().toString().equals("/user")
                        || exchange.getRequest().getPath().toString().matches("/user/verticals.*")
                        || exchange.getRequest().getPath().toString().matches("/vertical/id.*"))
                        && exchange.getRequest().getMethod().equals(HttpMethod.GET)) {
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("email", jwtUtil.getEmailFromToken(token)).build();
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    return chain.filter(mutatedExchange);
                }
                return chain.filter(exchange);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
