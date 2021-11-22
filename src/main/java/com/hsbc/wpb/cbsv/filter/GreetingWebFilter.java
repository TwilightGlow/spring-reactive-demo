package com.hsbc.wpb.cbsv.filter;

import com.hsbc.wpb.cbsv.pojo.Greeting;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class GreetingWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        PathPattern pattern = new PathPatternParser().parse("/api/greeting/*");
        ServerHttpRequest request = exchange.getRequest();
        if (pattern.matches(request.getPath().pathWithinApplication())) {
            System.out.println("This is Greeting Web Filter .....");
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.BAD_GATEWAY);
            DataBuffer buffer = response.bufferFactory()
                    .wrap(new Greeting("Filter").toString().getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }
}
