package com.hsbc.wpb.cbsv.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class GreetingService {
    private final Sinks.Many<String> greetingSinks = Sinks.many().multicast().onBackpressureBuffer();

    public void onNext(String greeting) {
        greetingSinks.tryEmitNext(greeting);
    }

    public Flux<String> getGreetings() {
        return greetingSinks.asFlux();
    }
}
