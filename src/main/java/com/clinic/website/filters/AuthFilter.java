package com.clinic.website.filters;

import com.clinic.website.service.AdminUserService;
import com.clinic.website.service.AppUserService;
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
    private final AppUserService appUserService;
    private final AdminUserService adminUserService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (Objects.equals(exchange.getRequest().getMethod(), HttpMethod.OPTIONS)
                || exchange.getRequest().getPath().toString().equals("/admin/login")
                || exchange.getRequest().getPath().toString().equals("/user/login")
                || exchange.getRequest().getPath().toString().matches("/swagger-ui.*"))
            return chain.filter(exchange);


        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (hasText(token)
                && !jwtUtil.isTokenExpired(token)) {

            if (exchange.getRequest().getPath().toString().equals("/user")
                    && exchange.getRequest().getMethod().equals(HttpMethod.GET))
                return appUserService.getUser(jwtUtil.getEmailFromToken(token))
                        .flatMap(user -> {
                            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                    .header("email", jwtUtil.getEmailFromToken(token)).build();
                            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                            return chain.filter(mutatedExchange);
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }));


            return adminUserService.getUser(jwtUtil.getEmailFromToken(token))
                    .flatMap(user -> chain.filter(exchange))
                    .switchIfEmpty(Mono.defer(() -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }));

        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
