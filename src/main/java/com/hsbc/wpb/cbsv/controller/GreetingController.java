package com.hsbc.wpb.cbsv.controller;

import com.hsbc.wpb.cbsv.pojo.Greeting;
import com.hsbc.wpb.cbsv.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GreetingController {
    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/api/greeting")
    public void sendGreeting(@RequestParam String message) {
        greetingService.onNext(message);
    }

    @GetMapping("/api/greeting/hello")
    public Mono<Greeting> hello(@RequestParam String message) {
        return Mono.just(new Greeting(message));
    }
}
