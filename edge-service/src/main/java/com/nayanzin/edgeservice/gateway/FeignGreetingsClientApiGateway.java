package com.nayanzin.edgeservice.gateway;

import com.nayanzin.edgeservice.feign.GreetingsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Profile("feign")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeignGreetingsClientApiGateway {

    private final GreetingsClient greetingsClient;

    @GetMapping("/feign/{name}")
    public Map<String, String> feign(@PathVariable("name") String name) {
        Map<String, String> response = this.greetingsClient.greet(name);
        return isNull(response) ? new HashMap<>() : response;
    }
}
