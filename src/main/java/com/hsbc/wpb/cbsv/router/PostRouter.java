package com.hsbc.wpb.cbsv.router;

import com.hsbc.wpb.cbsv.handler.PostHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

//@Configuration(proxyBeanMethods = false)
@Configuration
public class PostRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(PostHandler postController) {
        return route(GET("/posts"), postController::all)
                .andRoute(POST("/posts"), postController::create)
                .andRoute(GET("/posts/{id}"), postController::get);
//            .andRoute(PUT("/posts/{id}"), postController::update)
//            .andRoute(DELETE("/posts/{id}"), postController::delete);
    }
}
