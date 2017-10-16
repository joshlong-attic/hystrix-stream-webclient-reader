package com.example.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableCircuitBreaker
@SpringBootApplication
@RestController
public class ClientApplication {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    GreetingsClient client() {
        return new GreetingsClient(restTemplate());
    }

    public String emptyGreeting( String name) {
        return "Yo!";
    }

    @GetMapping("/client/{name}")
    @HystrixCommand(fallbackMethod = "emptyGreeting")
    public String read(@PathVariable String name) {
        return this.client().greet(name);
    }

}


class GreetingsClient {

    private final RestTemplate restTemplate;

    GreetingsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String greet(String name) {
        return this.restTemplate.getForObject(
                "http://localhost:8081/greet/{name}", String.class, name);
    }
}