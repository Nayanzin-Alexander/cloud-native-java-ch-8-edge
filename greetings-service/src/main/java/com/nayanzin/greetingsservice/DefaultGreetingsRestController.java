package com.nayanzin.greetingsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Profile({"default", "cloud", "insecure"})
@RestController
@RequiredArgsConstructor
public class DefaultGreetingsRestController {

    private final Environment env;

    @GetMapping("/greet/{name}")
    public Map<String, String> hi(@PathVariable("name") String name) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("local.server.port", env.getProperty("local.server.port"));
        responseMap.put("greeting", "Hello, " + name + "!");
        return responseMap;
    }
}
