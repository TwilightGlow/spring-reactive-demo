package com.hsbc.wpb.cbsv.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class EchoHandler implements WebSocketHandler {

    // https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-websocket
    @Override
    public Mono<Void> handle(WebSocketSession session) {
//        log.error("This is ws");
//        return session.send(session.receive()
//                .doOnNext(WebSocketMessage::retain)// Use retain() for Reactor Netty
//                .map(m -> session.textMessage("received:" + m.getPayloadAsText())));
        return session
                .send(session.receive()
                        .delayElements(Duration.ofSeconds(1))
                        .map(msg -> "Echo TestWebSocket " + msg.getPayloadAsText())
                        .delayElements(Duration.ofSeconds(1))
                        .map(session::textMessage)
                );
    }
}
