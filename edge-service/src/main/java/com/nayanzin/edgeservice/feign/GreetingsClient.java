package com.nayanzin.edgeservice.feign;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("greetings-service")
public interface GreetingsClient {

    @GetMapping("/greet/{name}")
    Map<String, String> greetGet(@PathVariable(name = "name") String name);

    @PostMapping("/greet")
    Map<String, String> greetPost(@RequestBody HiMessage name);

    @Data
    class HiMessage {
        private String name;
    }
}
