package com.nayanzin.authservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class PrincipalRestController {

    @GetMapping("/user")
    Principal principal(Principal p) {
        return p;
    }
}
