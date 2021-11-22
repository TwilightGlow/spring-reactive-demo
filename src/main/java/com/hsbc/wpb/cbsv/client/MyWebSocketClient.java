package com.hsbc.wpb.cbsv.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Slf4j
public class MyWebSocketClient {

    public static void main(String[] args) throws URISyntaxException {

        WebSocketClient client = new ReactorNettyWebSocketClient();
//        client.execute(new URI("ws://localhost:8080/echo"), (WebSocketSession session) -> {
//            session.send().log().;
//        });

        int count = 50;
        Flux<String> input = Flux.range(1, count).map(index -> "client msg-" + index);
        ReplayProcessor<Object> output = ReplayProcessor.create(count);

        client.execute(new URI("ws://localhost:8080/echo"),
                        session -> {
                            log.debug("Starting to send messages");
                            return session
                                    .send(input.doOnNext(s -> log.debug("Send outbound " + s)).map(session::textMessage))
                                    .thenMany(session.receive().take(count).map(WebSocketMessage::getPayloadAsText))
//                                    .subscribeWith(output)
                                    .doOnNext(s -> log.debug("inbound " + s))
                                    .then()
                                    .doOnTerminate(() -> log.debug("Done"));
                        })
                .block(Duration.ofMillis(100000));

//		assertEquals(input.collectList().block(Duration.ofMillis(5000)),
//				output.collectList().block(Duration.ofMillis(5000)));
//        client.execute(new URI("ws://localhost:8080/echo")), session -> {
//            session.
//        }
//        ).blockMillis(5000);
    }
}
