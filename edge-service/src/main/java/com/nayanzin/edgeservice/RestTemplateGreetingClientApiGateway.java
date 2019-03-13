package com.nayanzin.edgeservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

@Profile({"default", "insecure"})
@RestController
@RequestMapping("/api")
public class RestTemplateGreetingClientApiGateway {

    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateGreetingClientApiGateway(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("resttemplate/{name}")
    public Map<String, String> restTemplate(@PathVariable("name") String name) {

        ParameterizedTypeReference<Map<String, String>> ptr = new ParameterizedTypeReference<Map<String, String>>() {};

        ResponseEntity<Map<String, String>> responseEntity = restTemplate
                .exchange("http://greetings-service/greet/{name}", GET, null, ptr, name);

        return responseEntity.getBody();
    }
}
