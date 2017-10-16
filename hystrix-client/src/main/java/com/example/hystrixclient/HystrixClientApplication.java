package com.example.hystrixclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@SpringBootApplication
public class HystrixClientApplication {

    @Bean
    WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8080").build();
    }

    @Bean
    ApplicationRunner consumeHystrixStream(WebClient client) {
        return args ->
                client.get().uri("/hystrix.stream")
                        .retrieve()
                        .bodyToFlux(Map.class)
                        .subscribe(map -> log.info(map.toString()));
    }

    public static void main(String[] args) {
        SpringApplication.run(HystrixClientApplication.class, args);
    }
}
