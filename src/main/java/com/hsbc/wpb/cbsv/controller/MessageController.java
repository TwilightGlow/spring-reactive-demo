package com.hsbc.wpb.cbsv.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Date;

@RestController
public class MessageController {

    @GetMapping("flux")
    Flux<Object> allMessages(){
        return Flux.just("Hello", "World");
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getStreaming() throws URISyntaxException {
        Flux<String> input = Flux.<String>generate(sink -> sink.next(String.format("{ message: 'got message', date: '%s' }", new Date())))
                .delayElements(Duration.ofSeconds(1));
        WebSocketClient client = new ReactorNettyWebSocketClient();
        EmitterProcessor<String> output = EmitterProcessor.create();
        Mono<Void> sessionMono = client.execute(URI.create("ws://echo.websocket.org"), session -> session.send(input.map(session::textMessage))
                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).subscribeWith(output).then()).then());
        return output.doOnSubscribe(s -> sessionMono.subscribe());
    }
}
