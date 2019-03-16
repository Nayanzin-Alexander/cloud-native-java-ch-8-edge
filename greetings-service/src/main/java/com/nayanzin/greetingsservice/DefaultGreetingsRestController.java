package com.nayanzin.greetingsservice;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Profile({"default", "cloud", "insecure"})
@RestController
@RequiredArgsConstructor
public class DefaultGreetingsRestController {

    private final Environment env;

    @GetMapping("/greet/{name}")
    public Map<String, String> hi(@PathVariable("name") String name) {
        Map<String, String> responseMap = new HashMap<>(this.getSomeEnvironmentalVariables());
        responseMap.put("greeting", "Hello, " + name + "!");
        return responseMap;
    }

    @PostMapping("/greet")
    public Map<String, String> hiPost(@RequestBody HiMessage hiMessage) {
        Map<String, String> responseMap = new HashMap<>(this.getSomeEnvironmentalVariables());
        responseMap.put("greeting", "Hello, " + hiMessage.getName() + "!");
        return responseMap;
    }

    private Map<String, String> getSomeEnvironmentalVariables() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("local.server.port", env.getProperty("local.server.port"));
        return envVars;
    }

    @Data
    private static class HiMessage {
        private String name;
    }
}
