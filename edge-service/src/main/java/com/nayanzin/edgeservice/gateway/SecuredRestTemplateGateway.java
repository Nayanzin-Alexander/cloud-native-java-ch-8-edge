package com.nayanzin.edgeservice.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

@Profile({"default", "secure"})
@RestController
@RequestMapping("/api")
public class SecuredRestTemplateGateway {

    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    public SecuredRestTemplateGateway(@LoadBalanced OAuth2RestTemplate oAuth2RestTemplate) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    @GetMapping("/resttemplate/{name}")
    public Map<String, String> restTemplate(@PathVariable("name") String name) {

        ParameterizedTypeReference<Map<String, String>> ptr = new ParameterizedTypeReference<Map<String, String>>() {
        };

        ResponseEntity<Map<String, String>> responseEntity = oAuth2RestTemplate
                .exchange("http://greetings-service/greet/{name}", GET, null, ptr, name);

        return responseEntity.getBody();
    }
}
