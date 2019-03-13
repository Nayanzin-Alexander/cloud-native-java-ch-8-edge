package com.nayanzin.greetingsservice;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Profile({"default", "insecure"})
@RestController
public class DefaultGreetingsRestController {

    @GetMapping("/greet/{name}")
    public Map<String, String> hi(@PathVariable("name") String name) {
        return Collections.singletonMap("greeting", "Hello, " + name + "!");
    }
}
