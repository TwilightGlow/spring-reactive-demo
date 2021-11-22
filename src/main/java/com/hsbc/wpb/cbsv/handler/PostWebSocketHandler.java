package com.hsbc.wpb.cbsv.handler;

import com.hsbc.wpb.cbsv.repository.PostRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class PostWebSocketHandler implements WebSocketHandler {

    private final PostRepository posts;

    public PostWebSocketHandler(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    public List<String> getSubProtocols() {
        return List.of("test");
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String protocol = session.getHandshakeInfo().getSubProtocol();
        WebSocketMessage message = session.textMessage(this.posts.findAll().takeLast(0).toString());
        return doSend(session, Mono.just(message));
    }

    // TODO: workaround for suspected RxNetty WebSocket client issue
    // https://github.com/ReactiveX/RxNetty/issues/560
    private Mono<Void> doSend(WebSocketSession session, Publisher<WebSocketMessage> output) {
        return session.send(Mono.delay(Duration.ofMillis(100)).thenMany(output));
    }
}
