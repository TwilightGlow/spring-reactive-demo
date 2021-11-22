package com.hsbc.wpb.cbsv.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.wpb.cbsv.handler.EchoHandler;
import com.hsbc.wpb.cbsv.handler.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFlux
public class WebConfig {

    @Bean
    public HandlerMapping handlerMapping(EchoHandler echoHandler, GreetingHandler greetingHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/echo", echoHandler);
        map.put("/greetings", greetingHandler);
//        map.put("/posts", new PostWebSocketHandler(this.posts));
//        map.put("/ws/messages", new MessagetHandler(objectMapper));
//			map.put("/custom-header", new CustomHeaderHandler());
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        int order = -1;
        return new SimpleUrlHandlerMapping(map, order);
    }

    //  If using the WebFlux Config there is nothing further to do,
    //  or otherwise if not using the WebFlux config you’ll need to declare a WebSocketHandlerAdapter
//    @Bean
//    WebSocketHandlerAdapter webSocketHandlerAdapter() {
//        return new WebSocketHandlerAdapter();
//    }

    @Bean
    ObjectMapper jackson2ObjectMapper() {
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
        builder.indentOutput(true);
        return builder.build();
    }
}
