package com.hsbc.wpb.cbsv.handler;

import com.hsbc.wpb.cbsv.pojo.Greeting;
import com.hsbc.wpb.cbsv.service.GreetingService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler implements WebSocketHandler {

    private final GreetingService greetingService;

    public GreetingHandler(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        Flux<WebSocketMessage> messages = greetingService.getGreetings()
                .flatMap(Mono::just)
                .map(webSocketSession::textMessage);
        return webSocketSession.send(messages);
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Greeting("Hello, Spring!")));
    }
}
