package com.nayanzin.html5client;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class GreetingsClientUrisController {

    private final LoadBalancerClient loadBalancerClient;

    @GetMapping(value = "/greetings-client-uri",
            produces = APPLICATION_JSON_VALUE)
    public Map<String, String> greetingsClientURI() {
        return Optional.ofNullable(loadBalancerClient.choose("greetings-client"))
                .map(serviceInstance -> Collections.singletonMap("uri", serviceInstance.getUri().toString()))
                .orElse(null);
    }
}
