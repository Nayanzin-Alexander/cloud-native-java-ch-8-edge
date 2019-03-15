package com.nayanzin.edgeservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient("greetings-service")
public interface GreetingsClient {

    @GetMapping("/greet/{name}")
    Map<String, String> greet(@PathVariable(name = "name") String name);
}
